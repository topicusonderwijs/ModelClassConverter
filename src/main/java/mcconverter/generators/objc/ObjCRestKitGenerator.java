package mcconverter.generators.objc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.base.CaseFormat;

import mcconverter.configuration.CustomClass;
import mcconverter.generators.AbstractGenerator;
import mcconverter.main.Main;
import mcconverter.model.MCEntity;
import mcconverter.model.MCClass;
import mcconverter.model.MCEnum;
import mcconverter.model.MCPackage;
import mcconverter.model.MCProperty;
import mcconverter.model.MCType;
import mcconverter.model.MCTypeParameter;

public class ObjCRestKitGenerator extends AbstractGenerator {
	
	/* ===== Public Functions ===== */
	
	public List<String> getTemplates(MCPackage pack) {
		
		return Arrays.asList("ObjCRestKitRegistry.ftl", "ObjCRestKitRegistryHeader.ftl");
		
	}
	
	public List<String> getTemplates(MCEntity entity) {
		
		List<String> templates;
		
		if ( entity instanceof MCEnum ) {
			
			templates = Arrays.asList("ObjCRestKitEnum.ftl", "ObjCRestKitEnumHeader.ftl");
			
		} else if ( entity instanceof MCClass ) {
			
			templates = Arrays.asList("ObjCRestKitClass.ftl", "ObjCRestKitClassHeader.ftl");
			
		} else {
			
			templates = new ArrayList<String>();
			
		}
		
		return templates;
		
	}
	
	public List<String> generateImports(MCEntity entity) {
		
		List<String> imports = new ArrayList<String>();
		
		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			
			for ( MCProperty property : c.getProperties() ) {
				
				importType(imports, property.getType());
				
			}
			
		}
		
		return imports;
		
	}
	
	public String generatePropertyName(MCProperty property) {
		
		String name = replacePropertyName(property.getName());
		
		if ( property.isConstant() ) {
			
			name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
			
		}
		
		return name;
		
	}
	
	public String generatePropertyLiteral(MCProperty property) {
		
		String literal = "";
		
		String propertyValue = generatePropertyValue(property);
		
		if ( property.isConstant() && property.isStatic() && propertyValue != null ) {
			
			literal = generatePropertyName(property) + " " + propertyValue;
			
		} else {
			
			literal = "@property (";
			
			if ( isPointer(property.getType()) ) {
				literal += "strong, ";
			}
			
			if ( property.isConstant() ) {
				
				literal += "readonly, ";
				
			}
			
			literal += "nonatomic) ";
			literal += generateTypeLiteral(property.getType());
			literal += " ";
			literal += generatePropertyName(property);
			
		}
		
		return literal;
		
	}
	
	public String generatePropertyValue(MCProperty property) {
		
		String value = null;
		
		if ( property.hasValue() ) {
			
			if ( property.getValue().isLiteral() ) {
				
				value = property.getValue().getValue();
				
			} else {
				
				switch ( property.getType().getNativeType() ) {
					
				case String:
					value = "@\"" + property.getValue().getValue() + "\"";
					break;
				default:
					value = property.getValue().getValue();
					break;
				}
				
			}
			
		} else if ( isEnum(property.getType()) ) {
			
			value = "[[" + generateTypeName(property.getType()) + " alloc] initWithValue:0]";
			
		} else {
			
			switch ( property.getType().getNativeType() ) {
			
			case List:
			case Set:
				value = "[NSMutableArray array]";
				break;
			case Map:
				value = "[NSMutableDictionary dictionary]";
				break;
			case Boolean:
				value = "false";
				break;
			case Integer:
			case Long:
			case BigInteger:
			case Double:
			case Float:
			case BigDecimal:
				value = "0";
				break;
			default:
				break;
				
			}
			
		}
		
		return value;
		
	}
	
	public String generatePropertyMapping(MCProperty property) {
		
		return property.getName();
	}
	
	public String generateTypeName(MCType type) {
		
		String name = "NSObject";
		
		if ( type != null ) {
			
			switch ( type.getNativeType() ) {
				
				case Boolean:
					name = "BOOL";
					break;
				case Integer:
					name = "NSInteger";
					break;
				case Long:
				case BigInteger:
					name = "long";
					break;
				case Double:
				case BigDecimal:
					name = "double";
					break;
				case Float:
					name = "float";
					break;
				case String:
				case URI:
				case LocalTime:
					name = "NSString";
					break;
				case List:
				case Set:
					name = "NSMutableArray";
					break;
				case Map:
					name = "NSMutableDictionary";
					break;
				case DateTime:
				case LocalDate:
				case LocalDateTime:
					name = "NSDate";
					break;
				default:
					
					if ( getPackage().hasEntity(type.getIdentifier())) {
						
						name = getPackage().getEntity(type.getIdentifier()).getName();
						
					} else {
						
						Main.warning("Ignoring identifier: " + type.getIdentifier());
						
					}

					
					CustomClass c = getConfiguration().getCustomClass(name);
					
					if ( c != null && c.hasRename() ) {
						
						name = c.getRename();
						
					}
				
					break;
					
			}
				
		}
		
		return name;
		
	}
	
	public String generateTypeLiteral(MCType type) {
		
		String literal = generateTypeName(type);
		
		if ( type.getOwner() instanceof MCProperty && isPointer(type) ) {
			
			literal += "*";
			
		}
		
		return literal;
		
	}
	
	public String generateTypeParameterLiteral(MCTypeParameter parameter) {
		
		return generateTypeLiteral(parameter.getType());
	}
	
	public String generateFileName(MCPackage pack, String template) {
		
		return "EntityRegistry" + extension(template);
	}
	
	public String generateFileName(MCEntity entity, String template) {
		
		return entity.getName() + extension(template);
		
	}
	
	public boolean validateModel(MCPackage pack, Map<String, Object> model) {
		
		return true;
	}
	
	
	
	/* ===== Private Functions ===== */
	
	private static boolean isPointer(MCType type) {
		
		boolean pointer = true;
		
		switch ( type.getNativeType() ) {
			
		case Boolean:
		case Integer:
		case Long:
		case BigInteger:
		case Double:
		case Float:
		case BigDecimal:
			pointer = false;
			break;
		default:
			break;
			
		}
		
		return pointer;
		
	}
	
	private static String extension(String template) {
		
		return template.contains("Header") ? ".h" : ".m";
		
	}
	
	private void importType(List<String> imports, MCType type) {
		
		if ( !type.isNativeType() ) {
			
			String name = generateTypeName(type);
			
			if ( !name.equals("NSObject") ) {
				
				imports.add(generateTypeName(type));
				
			}
			
		}
		
		if ( type.hasParameters() ) {
			
			for ( MCTypeParameter parameter : type.getParameters() ) {
				
				if ( parameter.hasType() ) {
					
					importType(imports, parameter.getType());
					
				}
				
			}
			
		}
		
	}
	
}

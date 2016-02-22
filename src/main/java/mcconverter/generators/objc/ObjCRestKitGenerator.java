package mcconverter.generators.objc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import mcconverter.configuration.Configuration;
import mcconverter.configuration.CustomClass;
import mcconverter.generators.Generator;
import mcconverter.main.Main;
import mcconverter.model.MCEntity;
import mcconverter.model.MCClass;
import mcconverter.model.MCEnum;
import mcconverter.model.MCPackage;
import mcconverter.model.MCProperty;
import mcconverter.model.MCType;
import mcconverter.model.MCTypeParameter;

public class ObjCRestKitGenerator extends Generator {
	
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
	
	public String generatePropertyName(MCProperty property) {
		
		return replacePropertyName(property.getName());
		
	}
	
	public String generatePropertyLiteral(MCProperty property) {
		
		String literal = "@property (";
		
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
		
		return literal;
		
	}
	
	public String generatePropertyValue(MCProperty property) {
		
		return null;
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
					name = "NSMutableArray";
					break;
				case Set:
					name = "NSSet";
					break;
				case Map:
					name = "NSMutableDictionary";
					break;
				case Date:
				case DateTime:
				case LocalDate:
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
	
	public boolean validateEntity(MCEntity entity) {
		
		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			
			//All properties need to be optional since RestKit likes it that way.
			for (MCProperty property : c.getProperties() ) {
				
				if ( getPackage().hasEnum(property.getType().getIdentifier()) ) {
					
					property.setKey(property.getKey() + ".stringValue");
					
				}
				
			}
			
		}
		
		return super.validateEntity(entity);
		
	}
	
	public boolean validateModel(MCPackage pack, Map<String, Object> model) {
		
		return true;
	}

	public boolean validateModel(MCEntity entity, Map<String, Object> model) {
		
		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			
			//Two separate model properties are required as RestKit likes the relations separated from the other properties.
			List<Map<String, Object>> natives = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> relations = new ArrayList<Map<String, Object>>();
			List<String> imports = new ArrayList<String>();
			
			for ( MCProperty property : c.getProperties() ) {
				
				Map<String, Object> m = property.getModel(this);
				
				//The dominant type of the property is determined as RestKit requires this for mapping lists.
				Object dominantType = generateTypeName(property.getType());
				MCTypeParameter firstParameter = property.getType().getParameter(0);
				
				if ( firstParameter != null && !isRawType(firstParameter.getType()) ) {
					
					dominantType = firstParameter.hasName() ? firstParameter.getName() : generateTypeName(firstParameter.getType());
					relations.add(m);
					
				} else if ( isRawType(property.getType()) ) {
					
					natives.add(m);
					
				} else {
					
					relations.add(m);
					
				}
				
				m.put("property_dominant_type", dominantType);
				importType(imports, property.getType());
				
			}
			
			model.put("class_properties_natives", natives);
			model.put("class_properties_relations", relations);
			model.put("class_imports", imports);
			
		}
		
		//Find descriptor of entity
		String descriptor = entity.getIdentifier();
		
		for( String prefix : Configuration.current().getPackages() ) {
			
			if ( descriptor.startsWith(prefix) && descriptor.length() > prefix.length() ) {
				
				descriptor = descriptor.substring(prefix.length() + 1);
				break;
				
			}
			
		}
		
		model.put("entity_descriptor", descriptor);
		
		return super.validateModel(entity, model);
		
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
	
	private boolean isRawType(MCType type) {
		
		return type.isNativeType() || getPackage().hasEnum(type.getIdentifier());
		
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

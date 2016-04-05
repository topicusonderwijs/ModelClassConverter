package mcconverter.generators.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.CaseFormat;

import mcconverter.configuration.CustomClass;
import mcconverter.generators.AbstractGenerator;
import mcconverter.main.Main;
import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCEnum;
import mcconverter.model.MCEnumValue;
import mcconverter.model.MCPackage;
import mcconverter.model.MCProperty;
import mcconverter.model.MCType;
import mcconverter.model.MCTypeParameter;

public class JavaGenerator extends AbstractGenerator {

	public List<String> getTemplates(MCPackage pack) {
		
		return new ArrayList<String>();
		
	}
	
	public List<String> getTemplates(MCEntity entity) {
		
		List<String> templates = new ArrayList<String>();
		
		if ( entity instanceof MCEnum ) {
			
			templates.add("JavaEnum.ftl");
			
		} else if ( entity instanceof MCClass ) {
			
			templates.add("JavaClass.ftl");
			
		}
		
		return templates;
		
	}
	
	public String generateTypeName(MCType type) {
		
		String name = "Object";
		
		if ( type != null ) {
			
			switch ( type.getNativeType() ) {
				
				case Boolean:
					name = "boolean";
					break;
				case Integer:
					name = "int";
					break;
				case Long:
					name = "Long";
					break;
				case BigInteger:
					name = "BigInteger";
					break;
				case Double:
					name = "double";
					break;
				case Float:
					name = "float";
					break;
				case BigDecimal:
					name = "BigDecimal";
					break;
				case String:
					name = "String";
					break;
				case List:
					name = "List";
					break;
				case Set:
					name = "Set";
					break;
				case URI:
					name = "URI";
					break;
				case Map:
					name = "Map";
					break;
				case DateTime:
					name = "Date";
					break;
				case LocalTime:
					name = "LocalTime";
					break;
				case LocalDate:
					name = "LocalDate";
					break;
				case LocalDateTime:
					name = "LocalDateTime";
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
		
		String t = "Object";
		
		if ( type != null ) {

			switch ( type.getNativeType() ) {
				
				case List:
					t = "List<" + generateTypeParameterLiteral(type.getParameter(0), false) + ">";
					break;
				case Set:
					t = "Set<" + generateTypeParameterLiteral(type.getParameter(0), false) + ">";
					break;
				case Map:
					t = "Map<"
						+ generateTypeParameterLiteral(type.getParameter(0), false)
						+ ", "
						+ generateTypeParameterLiteral(type.getParameter(1), false)
						+ ">";
					break;
				default:
					t = generateTypeName(type);
					break;
				
			}
			
		}
		
		return t;
	}
	
	public String generateTypeParameterLiteral(MCTypeParameter parameter) {
		
		return generateTypeParameterLiteral(parameter, true);
		
	}
	
	public String generateEnumValueName(MCEnumValue value) {
		
		return replacePropertyName(value.getName());
		
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
		
		if ( property.isStatic() ) {
			
			literal += "static ";
			
		}
		
		if ( property.isConstant()  ) {
			
			literal += "final ";
			
		}
		
		literal += generateTypeLiteral(property.getType()) + " " + generatePropertyName(property);
		
		String propertyValue = generatePropertyValue(property);
		
		if ( propertyValue != null ) {
			
			literal += " = " + propertyValue;
			
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
					value = "\"" + property.getValue().getValue() + "\"";
					break;
				default:
					value = property.getValue().getValue();
					break;
				}
				
			}
			
		} else {
			
			switch ( property.getType().getNativeType() ) {
			
			case List:
				value = "new ArrayList<>()";
				break;
			case Set:
				value = "new HashSet<>()";
				break;
			case Map:
				value = "new HashMap<>()";
				break;
			default:
				break;
				
			}
			
		}
		
		return value;
		
	}
	
	public String generatePropertyMapping(MCProperty property) {
		
		return "";
		
	}
	
	public String generatePropertyTransform(MCProperty property) {
		
		return "";
		
	}
	
	public String generatePropertyTransformName(MCProperty property) {
		
		return "";
		
	}
	
	
	public String generateFileName(MCPackage pack, String template) {
		
		return null;
		
	}
	
	public String generateFileName(MCEntity entity, String template) {
		
		String name = entity.getName() + ".java";
		
		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			
			if ( getConfiguration().getIgnoreProtocols() && c.isProtocol() ) {
				
				name = null;
				
			}
			
		}
		
		return name;
		
	}
	
	public boolean validateEntity(MCEntity entity) {
		
		return super.validateEntity(entity);
		
	}
	
	public boolean validateModel(MCPackage pack, Map<String, Object> model) {
		
		return true;
		
	}
	
	
	
	/* ===== Protected Functions ===== */
	
	protected String generateTypeParameterLiteral(MCTypeParameter parameter, boolean applyType) {
		
		String name = "";
		
		if ( parameter != null ) {
			
			applyType &= parameter.hasType();
			
			if ( parameter.hasName() ) {
				name = parameter.getName();
				
				if ( applyType ) {
					name += " extends ";
				}
				
			}
			
			if ( !parameter.hasName() || applyType ) {
				name += generateTypeLiteral(parameter.getType().copy(false));
			}
			
		} else {
			
			name = "AnyObject";
			
		}
		
		return name;
		
	}
	
}

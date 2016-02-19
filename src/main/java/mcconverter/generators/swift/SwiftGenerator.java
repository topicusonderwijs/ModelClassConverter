package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.CaseFormat;

import mcconverter.generators.Generator;
import mcconverter.main.Main;
import mcconverter.model.*;

public class SwiftGenerator extends Generator {
	
	public List<String> getTemplates(MCPackage pack) {
		
		return new ArrayList<String>();
		
	}
	
	public List<String> getTemplates(MCEntity entity) {
		
		List<String> templates = new ArrayList<String>();
		
		if ( entity instanceof MCEnum ) {
			
			templates.add("SwiftEnum.ftl");
			
		} else if ( entity instanceof MCClass ) {
			
			templates.add("SwiftClass.ftl");
			
		}
		
		return templates;
		
	}
	
	public String generateTypeName(MCType type) {
		
		String name = "AnyObject";
		
		if ( type != null ) {
			
			switch ( type.getNativeType() ) {
				
				case Boolean:
					name = "Bool";
					break;
				case Integer:
					name = "Int";
					break;
				case Long:
					name = "Int64";
					break;
				case BigInteger:
					name = "Int64";
					break;
				case Double:
					name = "Double";
					break;
				case Float:
					name = "Float";
					break;
				case BigDecimal:
					name = "Double";
					break;
				case String:
					name = "String";
					break;
				case List:
					name = "Array";
					break;
				case Set:
					name = "Set";
					break;
				case URI:
					name = "NSURL";
					break;
				case Map:
					name = "Dictionary";
					break;
				case Date:
					name = "NSDate";
					break;
				case LocalTime:
					name = "NSDate";
					break;
				case DateTime:
					name = "NSDate";
					break;
				case LocalDate:
					name = "NSDate";
					break;
				default:
					
					if ( getPackage().hasEntity(type.getIdentifier())) {
						
						name = getPackage().getEntity(type.getIdentifier()).getName();
						
					} else {
						
						Main.warning("Ignoring identifier: " + type.getIdentifier());
						
					}
					
					if ( getConfiguration().hasMappedEntity(name) ) {
						
						name = getConfiguration().getMappedEntity(name);
						
					}
					
					break;
					
			}
				
		}
		
		return name;	
		
	}
	
	public String generateTypeLiteral(MCType type) {
		
		String t = "AnyObject";
		
		if ( type != null ) {

			switch ( type.getNativeType() ) {
				
				case List:
					t = "[" + generateTypeParameterLiteral(type.getParameter(0), false) + "]";
					break;
				case Set:
					t = "Set<" + generateTypeParameterLiteral(type.getParameter(0), false) + ">";
					break;
				case Map:
					t = "["
						+ generateTypeParameterLiteral(type.getParameter(0), false)
						+ ": "
						+ generateTypeParameterLiteral(type.getParameter(1), false)
						+ "]";
					break;
				default:
					t = generateTypeName(type);
					break;
				
			}
			
			if ( type.isOptional() ) {
				
				t += "?";
				
			}
			
		}
		
		return t;
	}
	
	public String generateTypeParameterLiteral(MCTypeParameter parameter) {
		
		return generateTypeParameterLiteral(parameter, true);
		
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
		
		literal += ( property.isConstant() ? "let " : "var " );
		literal += generatePropertyName(property);
		
		String propertyValue = generatePropertyValue(property);
		
		if ( !property.isConstant() ) {
			
			literal += " : " + generateTypeLiteral(property.getType());
			
		}
		
		if ( propertyValue != null ) {
			
			literal += " = " + propertyValue;
			
		}
		
		return literal;
		
	}
	
	public String generatePropertyValue(MCProperty property) {
		
		String value = null;
		
		if ( property.hasValue() ) {
			
			switch ( property.getType().getNativeType() ) {
				
			case String:
				value = "\"" + property.getValue() + "\"";
				break;
			default:
				value = property.getValue();
				break;
			}
			
		} else {
			
			switch ( property.getType().getNativeType() ) {
			
			case List:
				value = "[]";
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
			case String:
				value = "\"\"";
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
	
	public String generateFileName(MCPackage pack) {
		
		return null;
		
	}
	
	public String generateFileName(MCEntity entity, String template) {
		
		String name = entity.getName() + ".swift";;
		
		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			
			if ( getConfiguration().getIgnoreProtocols() && c.isProtocol() ) {
				
				name = null;
				
			}
			
		}
		
		return name;
		
	}
	
	public void validateEntity(MCEntity entity) {
		
		
		
	}
	
	public boolean validateModel(MCPackage pack, Map<String, Object> model) {
		
		return true;
		
	}
	
	
	
	/* ===== Private Functions ===== */
	
	private String generateTypeParameterLiteral(MCTypeParameter parameter, boolean applyType) {
		
		String name = "";
		
		if ( parameter != null ) {
			
			applyType &= parameter.hasType();
			
			if ( parameter.hasName() ) {
				name = parameter.getName();
				
				if ( applyType ) {
					name += " : ";
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

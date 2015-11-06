package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.CaseFormat;

import mcconverter.generators.Generator;
import mcconverter.model.*;

public class SwiftGenerator extends Generator {
	
	public List<String> getTemplates(MCEntity entity) {
		
		List<String> templates = new ArrayList<String>();
		
		if ( entity instanceof MCEnum ) {
			
			templates.add("SwiftEnum.ftl");
			
		} else if ( entity instanceof MCClass ) {
			
			templates.add("SwiftClass.ftl");
			
		}
		
		return templates;
		
	}
	
	public String generateTypeLiteral(MCType type) {
		
		String t = "AnyObject";
		
		if ( type != null ) {

			switch ( type.getNativeType() ) {
				
				case Boolean:
					t = "Bool";
					break;
					
				case Integer:
					t = "Int";
					break;
					
				case Long:
					t = "Long";
					break;
					
				case BigInteger:
					t = "Long";
					break;
					
				case Double:
					t = "Double";
					break;
					
				case Float:
					t = "Float";
					break;
					
				case BigDecimal:
					t = "Double";
					break;
					
				case String:
					t = "String";
					break;
					
				case List:
					
					t = "[" + generateTypeParameterLiteral(type.getParameter(0), false) + "]";
					break;
					
				case Set:
					t = "Set<" + generateTypeParameterLiteral(type.getParameter(0), false) + ">";
					break;
					
				case URI:
					t = "NSURL";
					break;
					
				case Map:
					t = "["
						+ generateTypeParameterLiteral(type.getParameter(0), false)
						+ ": "
						+ generateTypeParameterLiteral(type.getParameter(1), false)
						+ "]";
					break;
					
				case Date:
					t = "NSDate";
					break;
					
				case LocalTime:
					t = "NSDate";
					break;
					
				case DateTime:
					t = "NSDate";
					break;
					
				case LocalDate:
					t = "NSDate";
					break;
					
				default:
					
					if ( getPackage().hasEntity(type.getIdentifier())) {
						
						t = getPackage().getEntity(type.getIdentifier()).getName();
						
					} else {
						
						System.err.println("Unknown identifier: " + type.getIdentifier());
						
					}
					
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
		
		if ( property.hasValue() ) {
			
			literal += " = \"" + property.getValue() + "\"";
			
		} else {
			
			literal += " : " + generateTypeLiteral(property.getType());
			
			String propertyValue = null;
			
			switch ( property.getType().getNativeType() ) {
			case List:
				propertyValue = "[]";
				break;
			default:
				break;
			}
			
			if ( propertyValue != null ) {
				
				literal += " = " + propertyValue;				
			}
			
		}
		
		return literal;
		
	}
	
	public String generatePropertyMapping(MCProperty property) {
		
		return "";
		
	}
	
	public String generateEnumValueName(MCEnum.MCEnumValue value) {
		
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, replacePropertyName(value.getName()));
		
	}
	
	public String generateFileName(MCEntity entity) {
		
		return entity.getName() + ".swift";
		
	}
	
	public void validateEntity(MCEntity entity) {
		
		
		
	}
	
	public void validateModel(MCEntity entity, Map<String, Object> model) {
		
		
		
	}
	
	
	
	/* ===== Private Functions ===== */
	
	private String replacePropertyName(String name) {
		
		if ( name.equals("id") ) {
			
			name = "objectId";
			
		} else if ( name.toLowerCase().equals("self") ) {
			
			name = "zelf";
			
		}
		
		return name;
		
	}
	
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

package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.CaseFormat;

import mcconverter.configuration.CustomClass;
import mcconverter.configuration.CustomProperty;
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

public class SwiftGenerator extends AbstractGenerator {
	
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
	
	public List<String> generateImports(MCEntity entity) {
		
		return new ArrayList<>();
		
	}
	
	public String generateEnumValueName(MCEnumValue value) {
		
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, replacePropertyName(value.getName()));
		
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
					name = "URL";
					break;
				case Map:
					name = "Dictionary";
					break;
				case DateTime:
				case LocalTime:
				case LocalDate:
				case LocalDateTime:
					name = "Date";
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
			
			t += generateOptionalLiteral(type);
			
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
		
		if ( !property.isConstant() ) {
			
			literal += " : " + generateTypeLiteral(property.getType());
			
		} else {
			
			String propertyValue = generatePropertyValue(property);
			
			if ( propertyValue != null ) {
				
				literal += " = " + propertyValue;
				
			}
			
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
			case Set:
				value = "[]";
				break;
			case Map:
				value = "[:]";
				break;
			default:
				break;
				
			}
			
		}
		
		return value;
		
	}
	
	public String generateFileName(MCPackage pack, String template) {
		
		return null;
		
	}
	
	public String generateFileName(MCEntity entity, String template) {
		
		String name = entity.getName() + ".swift";
		
		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			
			if ( getConfiguration().getIgnoreProtocols() && c.isProtocol() ) {
				
				name = null;
				
			}
			
		}
		
		return name;
		
	}
	
	public boolean validateModel(MCPackage pack, Map<String, Object> model) {
		return true;
	}
	
	public boolean validateEntity(MCEntity entity) {
		
		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			
			for ( MCProperty property : c.getProperties() ) {
				applyOptional(property.getType());
			}
			
		}
		
		return super.validateEntity(entity);
		
	}
	
	
	
	/* ===== Protected Functions ===== */
	
	protected String generateOptionalLiteral(MCType type) {
		
		return type != null && type.isOptional() ? "?" : "";
		
	}
	
	protected String generateTypeParameterLiteral(MCTypeParameter parameter, boolean applyType) {
		
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
	
	private void applyOptional(MCType type) {
		
		// By default use no optional for lists, maps and sets
		if ( type.getOwner() instanceof MCProperty ) {
			
			MCProperty property = (MCProperty)type.getOwner();
			
			CustomProperty customProperty = getConfiguration().getSpecificCustomProperty(property);
			
			if ( customProperty == null || !customProperty.hasType() ) {

				switch ( type.getNativeType() ) {
				
				case List:
				case Map:
				case Set:
					type.setOptional(false);
					break;
				default:
					break;
					
				}
				
			}
			
		}
		
	}
	
}

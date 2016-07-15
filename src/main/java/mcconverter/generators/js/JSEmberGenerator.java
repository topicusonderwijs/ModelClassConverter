package mcconverter.generators.js;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mcconverter.configuration.CustomProperty;
import mcconverter.generators.AbstractGenerator;
import mcconverter.model.*;

public class JSEmberGenerator extends AbstractGenerator {

	/* ===== Public Functions ===== */
	
	public List<String> getTemplates(MCPackage pack) {
		
		return new ArrayList<>();
		
	}
	
	public List<String> getTemplates(MCEntity entity) {
		
		List<String> templates = new ArrayList<>();
		
		if ( entity instanceof MCEnum ) {
			templates.add("JSEmberEnum.ftl");
		} else {
			templates.add("JSEmberClass.ftl");
		}
		
		return templates;
		
	}
	
	public String generatePropertyName(MCProperty property) {
		
		return replacePropertyName(property.getName());
		
	}
	
	public String generatePropertyLiteral(MCProperty property) {
		
		return "";
		
	}
	
	public String generatePropertyValue(MCProperty property) {
		
		return "";
		
	}
	
	public String generatePropertyMapping(MCProperty property) {
		
		String mapping = "";
		
		String transform = generatePropertyTransform(property);
		
		if ( transform != null && !transform.isEmpty() ) {
			mapping = "\"" + transform + "\"";
		}
		
		return mapping;
		
	}
	
	public String generatePropertyTransform(MCProperty property) {
		
		String transform = "";
		
		CustomProperty customProperty = getConfiguration().getCustomTransformForProperty(property);
		
		if ( customProperty != null ) {
			
			transform = customProperty.getTransform();
			
		} else {
			
			switch ( property.getType().getNativeType() ) {
			
			case String:
				transform = "string";
				break;
				
			case Boolean:
				transform = "boolean";
				break;
				
			case Integer:
			case Long:
			case BigInteger:
			case Double:
			case Float:
			case BigDecimal:
				transform = "number";
				break;
				
			case DateTime:
			case LocalTime:
			case LocalDate:
			case LocalDateTime:
				transform = "date";
				break;
				
			default:
				break;
				
			}
			
		}
		
		return transform;
		
	}
	
	public String generatePropertyTransformName(MCProperty property) {
		
		return "";
		
	}
	
	public String generateTypeName(MCType type) {
		
		return type.getNativeType().name();
		
	}
	
	public String generateTypeLiteral(MCType type) {
		
		return generateTypeName(type);
		
	}
	
	public String generateTypeParameterLiteral(MCTypeParameter parameter) {
		
		return "";
		
	}
	
	public String generateFileName(MCPackage pack, String template) {
		
		return null;
		
	}
	
	public String generateFileName(MCEntity entity, String template) {
		
		return entity.getName().toLowerCase() + ".js";
		
	}
	
	
	
	/* ===== Protected Functions ===== */
	
	protected boolean validateModel(MCPackage pack, Map<String, Object> model) {
		
		return true;
		
	}
	
}

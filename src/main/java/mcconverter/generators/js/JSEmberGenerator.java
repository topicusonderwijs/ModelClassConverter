package mcconverter.generators.js;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mcconverter.configuration.CustomProperty;
import mcconverter.configuration.CustomTransform;
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
			
			CustomTransform customTransform = customProperty.getTransform();
			
			if ( customTransform != null ) {
				
				transform = customTransform.getTransform();
				
			}
			
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
	
	public String generateRelationTypeName(MCRelationType type) {
		
		String name = "";
		
		switch ( type ) {
		case ToOne:
			name = "belongsTo";
			break;
		case ToMany:
			name = "hasMany";
			break;
		}
		
		return name;
		
	}
	
	public String generateTypeName(MCType type) {
		
		String name = type.getNativeType().name();
		
		if ( !type.isNativeType() && getPackage().hasEntity(type.getIdentifier()) ) {
			name = getPackage().getEntity(type.getIdentifier()).getName();
		}
		
		return name;
		
	}
	
	public String generateTypeLiteral(MCType type) {
		
		return generateTypeName(type);
		
	}
	
	public String generateFileName(MCPackage pack, String template) {
		
		return null;
		
	}
	
	public String generateFileName(MCEntity entity, String template) {
		
		return entity.getName() + ".js";
		
	}
	
	
	
	/* ===== Protected Functions ===== */
	
	protected boolean validateModel(MCPackage pack, Map<String, Object> model) {
		
		return true;
		
	}
	
}

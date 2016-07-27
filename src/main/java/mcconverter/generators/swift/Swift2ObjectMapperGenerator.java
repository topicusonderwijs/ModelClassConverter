package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.CaseFormat;

import mcconverter.configuration.CustomProperty;
import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCEnum;
import mcconverter.model.MCPackage;
import mcconverter.model.MCProperty;
import mcconverter.model.MCType;
import mcconverter.utils.ListUtils;

public class Swift2ObjectMapperGenerator extends SwiftGenerator {
	
	/* ===== Public Functions ===== */
	
	public List<String> getTemplates(MCPackage pack) {
		
		return Arrays.asList("Swift2ObjectMapperRegistry.ftl");
		
	}
	
	public List<String> getTemplates(MCEntity entity) {
		
		List<String> templates = new ArrayList<String>();
		
		if ( entity instanceof MCEnum ) {
			
			templates.add("Swift2ObjectMapperEnum.ftl");
			
		} else if ( entity instanceof MCClass ) {
			
			templates.add("Swift2ObjectMapperClass.ftl");
			
		}
		
		return templates;
		
	}
	
	public String generateFileName(MCPackage pack, String template) {
		
		return "EntityRegistry.swift";
	}
	
	public String generateTypeLiteral(MCType type) {
		
		String t = super.generateTypeLiteral(type);
		
		if ( type != null ) {
			
			switch ( type.getNativeType() ) {
			case Long:
			case BigInteger:
				t = "NSNumber" + generateOptionalLiteral(type);
				break;
			case URI:
				t = "String" + generateOptionalLiteral(type);
				break;
			default:
				break;
				
			}
			
		}
		
		return t;
		
	}
	
	public String generatePropertyValue(MCProperty property) {
		
		String value = super.generatePropertyValue(property);
		
		if ( !property.hasValue() ) {
			
			switch ( property.getType().getNativeType() ) {
			
			case URI:
				value = "\"\"";
				break;
			default:
				break;
				
			}
			
		}
		
		return value;
		
	}
	
	public String generatePropertyMapping(MCProperty property) {
		
		String mapping = generatePropertyName(property) + " <- ";
		String transformName = generatePropertyTransformName(property);
		
		if ( transformName != null ) {
			
			mapping += "(map[\"" + property.getKey() + "\"], Map." + transformName + "Transform)";
			
		} else {
			
			mapping += "map[\"" + property.getKey() + "\"]";
			
		}
		
		return mapping;
		
	}
	
	public String generatePropertyTransform(MCProperty property) {
		
		String transform = "";
		
		CustomProperty customProperty = getConfiguration().getCustomTransformForProperty(property);
		
		if ( customProperty != null ) {
			
			switch ( property.getType().getNativeType() ) {

			case DateTime:
			case LocalTime:
			case LocalDate:
			case LocalDateTime:
				transform = "DateFormatterTransform(format: \"" + customProperty.getTransform() + "\")";
				break;
			default:
				transform = customProperty.getTransform();
				break;
				
			}
			
		}
		
		return transform;
		
	}

	public String generatePropertyTransformName(MCProperty property) {
		
		String name = null;

		CustomProperty customProperty = getConfiguration().getCustomTransformForProperty(property);
		
		if ( customProperty != null ) {
			
			name = "";
			MCEntity e = property.getEntity();
			
			if ( customProperty.hasClasss() && e instanceof MCClass ) {
				
				MCClass c = (MCClass)e;
				
				name += c.getName() + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, property.getName());
				
			} else if ( customProperty.hasName() ) {
				
				name += CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, customProperty.getName());
				
			} else if ( property.getType().isNativeType() ) {
				
				name += property.getType().getNativeType();
				
			} else {
				
				name += generateTypeName(property.getType());
				
			}
			
		}
		
		return name;
		
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
	
	public boolean validateModel(MCEntity entity, Map<String, Object> model) {
		
		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			
			model.put(
				"class_properties_all_notvalued",
				MCProperty.getModel(this, ListUtils.filter(c.getAllProperties(), p -> generatePropertyValue(p) == null))
			);
			model.put(
				"class_properties_all_required_notvalued",
				MCProperty.getModel(this, ListUtils.filter(c.getAllRequiredProperties(), p -> generatePropertyValue(p) == null))
			);
			
		}
		
		return super.validateModel(entity, model);
		
	}
	
	public boolean validateModel(MCPackage pack, Map<String, Object> model) {
		
		//Determine all transforms
		Map<String, String> transforms = new HashMap<>();
		
		for( MCClass c : pack.getClasses() ) {
			
			for ( MCProperty p : c.getProperties() ) {
				
				CustomProperty customProperty = getConfiguration().getCustomTransformForProperty(p);
				
				if ( customProperty != null && customProperty.hasTransform() ) {
					
					transforms.put(generatePropertyTransformName(p), generatePropertyTransform(p));
					
				}
				
			}
			
		}
		
		model.put("package_transforms", transforms);
		
		return super.validateModel(pack, model);
		
	}
	
	
	/* ===== Private Functions ===== */
	
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

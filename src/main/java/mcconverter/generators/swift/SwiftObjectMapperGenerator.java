package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCEnum;
import mcconverter.model.MCPackage;
import mcconverter.model.MCProperty;
import mcconverter.model.MCType;

public class SwiftObjectMapperGenerator extends SwiftGenerator {
	
	/* ===== Public Functions ===== */
	
	public List<String> getTemplates(MCPackage pack) {
		
		return Arrays.asList("SwiftObjectMapperRegistry.ftl");
		
	}
	
	public List<String> getTemplates(MCEntity entity) {
		
		List<String> templates = new ArrayList<String>();
		
		if ( entity instanceof MCEnum ) {
			
			templates.add("SwiftObjectMapperEnum.ftl");
			
		} else if ( entity instanceof MCClass ) {
			
			templates.add("SwiftObjectMapperClass.ftl");
			
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
		
		return "";
		
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
			
			List<Map<String, Object>> optional = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> required = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> valued = new ArrayList<Map<String, Object>>();
			
			for ( MCProperty property : c.getProperties() ) {
				
				Map<String, Object> m = property.getModel(this);
				
				//Determine initializers
				if ( property.getType().isOptional() ) {
					
					optional.add(m);
					
				} else {
					
					required.add(m);
					
					if ( generatePropertyValue(property) != null ) {
						
						valued.add(m);
						
					}
					
				}
				
			}
			
			model.put("class_properties_optional", optional);
			model.put("class_properties_required", required);
			model.put("class_properties_valued", valued);
			model.put("class_initialize", applyInitialize(c));
			
		}
		
		return super.validateModel(entity, model);
		
	}
	
	
	
	/* ===== Private Functions ===== */
	
	private boolean applyInitialize(MCClass c) {
		
		boolean initialize = true;
		
		if ( c.hasParent() ) {
			
			MCClass s = getPackage().getClass(c.getParent().getIdentifier());
			
			initialize = s == null || (validateEntity(s) && applyInitialize(s));
			
		}
		
		if ( initialize ) {
			
			for ( MCProperty property : c.getProperties() ) {
				
				if ( !property.getType().isOptional() && generatePropertyValue(property) == null ) {
						
					initialize = false;
					break;
					
				}
				
			}
			
		}
		
		return initialize;
		
	}
	
	private void applyOptional(MCType type) {
		
		// Use no optional for lists, maps and sets
		if ( type.getOwner() instanceof MCProperty ) {
			
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

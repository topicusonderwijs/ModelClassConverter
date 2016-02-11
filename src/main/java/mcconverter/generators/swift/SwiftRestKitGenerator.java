package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import mcconverter.configuration.Configuration;
import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCEnum;
import mcconverter.model.MCNativeType;
import mcconverter.model.MCPackage;
import mcconverter.model.MCProperty;
import mcconverter.model.MCType;
import mcconverter.model.MCTypeParameter;

public class SwiftRestKitGenerator extends SwiftGenerator {
	
	public List<String> getTemplates(MCPackage pack) {
		
		return Arrays.asList("SwiftRestKitRegistry.ftl");
		
	}
	
	public List<String> getTemplates(MCEntity entity) {
		
		List<String> templates = new ArrayList<String>();
		
		if ( entity instanceof MCEnum ) {
			
			templates.add("SwiftRestKitEnum.ftl");
			
		} else if ( entity instanceof MCClass ) {
			
			templates.add("SwiftRestKitClass.ftl");
			
		}
		
		return templates;
		
	}
	
	public String generateTypeName(MCType type) {
		
		return type.isNativeType(MCNativeType.Object) ? "NSObject" : super.generateTypeName(type);
		
	}
	
	public String generateTypeLiteral(MCType type) {
		
		String literal = super.generateTypeLiteral(type.copy(false));
		
		if ( type.getOwner() instanceof MCProperty ) {
			
			literal += type.isOptional() ? "?" : "!";
			
		}
		
		return literal;
	}
	
	public String generateTypeParameterLiteral(MCTypeParameter parameter) {
		
		return super.generateTypeParameterLiteral(parameter);
		
	}
	
	public String generateFileName(MCPackage pack) {
		
		return "EntityRegistry.swift";
		
	}
	
	public void validateEntity(MCEntity entity) {
		
		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			
			//All properties need to be optional since RestKit likes it that way.
			for (MCProperty property : c.getProperties() ) {
				
				if ( getPackage().hasEnum(property.getType().getIdentifier()) ) {
					
					property.setKey(property.getKey() + ".stringValue");
					
				}
				
			}
			
		}
		
	}
	
	public boolean validateModel(MCEntity entity, Map<String, Object> model) {
			
		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			
			//Two separate model properties are required as RestKit likes the relations separated from the other properties.
			List<Map<String, Object>> natives = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> relations = new ArrayList<Map<String, Object>>();
			
			for ( MCProperty property : c.getProperties() ) {
				
				Map<String, Object> m = property.getModel(this);
				
				if ( property.getType().isNativeType() || getPackage().hasEnum(property.getType().getIdentifier() ) ) {
					
					natives.add(m);
					
				} else {
					
					relations.add(m);
					
				}
				
			}
			
			model.put("class_properties_natives", natives);
			model.put("class_properties_relations", relations);
			
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
	
}

package mcconverter.generators.swift;

import mcconverter.model.*;

public class SwiftObjectMapperParroGenerator extends SwiftObjectMapperGenerator {
	
	public String generatePropertyMapping(MCProperty property) {
		
		String mapping = "";
		
		if ( property.getType().isNativeType(MCNativeType.Date) || property.getType().isNativeType(MCNativeType.DateTime) ) {
			
			mapping = generatePropertyName(property) + " <- ( map[\"" + property.getKey() + "\"], ObjectMapping.dateTransformer )";
			
			//(map["createdAt"], ObjectMapping.dateTransformer)
			
		} else {
			
			mapping = generatePropertyName(property) + " <- map[\"" + property.getKey() + "\"]";
			
		}
		
		//${property.property_name} <- map["${property.property_key}"]	
		
		return mapping;
		
	}
	
	public void validateEntity(MCEntity entity) {
		
		if ( entity instanceof MCClass ) {
			
			MCClass dataClass = (MCClass)entity;
			
			if ( dataClass.getName().equals("Linkable") ) {
				
				dataClass.addProperty(
					new MCProperty(dataClass.getIdentifier() + ".type", "type", "$type", new MCType(MCNativeType.String))
				);
				
			}
			
			for (MCProperty property : dataClass.getProperties()) {
				
				if ( property.getType().isNativeType(MCNativeType.List)) {
					
					property.getType().setOptional(false);
					
				}
				
			}
			
		}
		
	}
	
}

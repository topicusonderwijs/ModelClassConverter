package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCEnum;
import mcconverter.model.MCNativeType;
import mcconverter.model.MCType;

public class SwiftObjectMapperGenerator extends SwiftGenerator {
	
	public List<String> getTemplates(MCEntity entity) {
		
		List<String> templates = new ArrayList<String>();
		
		if ( entity instanceof MCEnum ) {
			
			templates.add("SwiftObjectMapperEnum.ftl");
			
		} else if ( entity instanceof MCClass ) {
			
			templates.add("SwiftObjectMapperClass.ftl");
			
		}
		
		return templates;
		
	}
	
	public String generateTypeName(MCType type) {
		
		String t = super.generateTypeLiteral(type);
		
		if ( type != null ) {
			
			MCNativeType n = type.getNativeType();
			
			if (
				n == MCNativeType.Integer ||
				n == MCNativeType.Long ||
				n == MCNativeType.BigInteger ||
				n == MCNativeType.Double ||
				n == MCNativeType.Float ||
				n == MCNativeType.BigDecimal
			) {
				
				t = "NSNumber";
				
				if ( type.isOptional() ) {
					
					t += "?";
					
				}
				
			}
			
		}
		
		return t;
		
	}
	
	public void validateModel(MCEntity entity, Map<String, Object> model) {
		
		super.validateModel(entity, model);
		
	}
	
}

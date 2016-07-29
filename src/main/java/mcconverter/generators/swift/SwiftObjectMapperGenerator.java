package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCEnum;
import mcconverter.model.MCPackage;
import mcconverter.model.MCType;

public class SwiftObjectMapperGenerator extends Swift2ObjectMapperGenerator {
	
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
	
	public String generateTypeName(MCType type) {
		
		String name = super.generateTypeName(type);
		
		if ( type != null ) {
			
			switch ( type.getNativeType() ) {
				
				case DateTime:
				case LocalTime:
				case LocalDate:
				case LocalDateTime:
					name = "Date";
					break;
				default:
					break;
					
			}
				
		}
		
		return name;	
		
	}
	
}

package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.List;

import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCEnum;
import mcconverter.model.MCType;

public class SwiftGenerator extends mcconverter.generators.swift.Swift2Generator {
	
	public List<String> getTemplates(MCEntity entity) {
		
		List<String> templates = new ArrayList<String>();
		
		if ( entity instanceof MCEnum ) {
			
			templates.add("SwiftEnum.ftl");
			
		} else if ( entity instanceof MCClass ) {
			
			templates.add("SwiftClass.ftl");
			
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

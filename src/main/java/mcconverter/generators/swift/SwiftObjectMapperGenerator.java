package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCEnum;
import mcconverter.model.MCPackage;

public class SwiftObjectMapperGenerator extends mcconverter.generators.swift.Swift2ObjectMapperGenerator {
	
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
	
}

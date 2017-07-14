package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCEnum;
import mcconverter.model.MCPackage;
import mcconverter.model.MCProperty;

public class SwiftLemonGenerator extends SwiftGenerator {
	
	public List<String> getTemplates(MCPackage pack) {
		
		return Arrays.asList("SwiftLemonModel.ftl");
		
	}
	
	public List<String> getTemplates(MCEntity entity) {
		
		List<String> templates = new ArrayList<String>();
		
		if ( entity instanceof MCEnum ) {
			
			templates.add("SwiftLemonEnum.ftl");
			
		} else if ( entity instanceof MCClass ) {
			
			templates.add("SwiftLemonClass.ftl");
			
		}
		
		return templates;
		
	}
	
	public String generateFileName(MCPackage pack, String template) {
		return pack.getName() + "Model.swift";
	}
	
	public String generatePropertyMapping(MCProperty property) {
		
		List<String> keys = Arrays.asList(property.getKey().split("."));
		String mapping = keys.stream().map(k -> "\"" + k + "\"").collect(Collectors.joining(", "));
		
		String transformName = generatePropertyTransformName(property);
		
		if ( transformName != null ) {
			mapping += ", transform: " + transformName;
		}
		
		return mapping;
		
	}
	
}

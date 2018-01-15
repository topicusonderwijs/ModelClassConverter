package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import mcconverter.configuration.CustomProperty;
import mcconverter.configuration.CustomTransform;
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
		List<String> keys = Arrays.asList(property.getKey().split("\\."));
		return keys.stream().map(k -> "\"" + k + "\"").collect(Collectors.joining(", "));
	}
	
	public String generatePropertyReading(MCProperty property) {
		String reading = generatePropertyMapping(property);
		
		CustomProperty customProperty = getConfiguration().getCustomTransformForProperty(property);
		
		if (customProperty != null) {
			CustomTransform customTransform = customProperty.getTransform();
			if (customTransform != null) {
				String transform = customTransform.getTransform();
				if (transform != null) {
					reading += ", " + transform;
				}
			}
		}
		
		return reading;
	}
	
	public String generatePropertyWriting(MCProperty property) {
		String writing = generatePropertyMapping(property) + ", value: " + property.getName();
		String custom = null;
		
		switch (property.getType().getNativeType()) {
		
		case LocalTime:
			custom = "string(.localTimeLong)";
			break;
		case LocalDate:
			custom = "string(.localDate)";
			break;
		case LocalDateTime:
			custom = "string(.localDateTime)";
			break;
		default:
			break;
			
		}
		
		if (custom != null) {
			if (property.getType().isOptional()) {
				writing += "?";
			}
			writing += "." + custom;
		}
		
		return writing;
	}
	
}

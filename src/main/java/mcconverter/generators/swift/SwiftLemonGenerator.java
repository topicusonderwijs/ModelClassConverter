package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCEnum;
import mcconverter.model.MCPackage;

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
	
}
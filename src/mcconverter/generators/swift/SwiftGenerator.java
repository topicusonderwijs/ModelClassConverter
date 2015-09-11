package mcconverter.generators.swift;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mcconverter.generators.Generator;
import mcconverter.model.*;

public class SwiftGenerator extends Generator {
	
	public List<String> getTemplates(MCEntity entity) {
		
		List<String> templates = new ArrayList<String>();
		
		if ( entity instanceof MCEnum ) {
			
			templates.add("SwiftEnum.ftl");
			
		} else if ( entity instanceof MCClass ) {
			
			templates.add("SwiftClass.ftl");
			
		}
		
		return templates;
		
	}
	
	public String generateType(MCType type) {
		
		String t = "";
		
		switch ( type.getNativeType() ) {
			
			case Boolean:
				t = "Bool";
				break;
				
			case Integer:
				t = "Int";
				break;
				
			case Long:
				t = "Long";
				break;
				
			case BigInteger:
				t = "Long";
				break;
				
			case Double:
				t = "Double";
				break;
				
			case Float:
				t = "Float";
				break;
				
			case BigDecimal:
				t = "Double";
				break;
				
			case String:
				t = "String";
				break;
				
			case List:
				t = "[";
				
				if ( type.hasParameters() ) {
					
					t += "ShouldBe";
					
				} else {
					
					t += "AnyObject";
					
				}
				
				t += "]";
				break;
				
			case Set:
				
				t = "Set<AnyObject>";
				
				break;
			case URI:
				t = "NSURL";
				break;
				
			case Map:
				t = "[AnyObject : AnyObject";
				
				
				
				t += "]";
				break;
			case Date:
				t = "NSDate";
				break;
			case LocalTime:
				t = "NSDate";
				break;
			case DateTime:
				t = "NSDate";
				break;
			case LocalDate:
				t = "NSDate";
				break;
			default:
				t = "Unknown";
				
				if ( getPackage().hasEntity(type.getIdentifier())) {
					
					t = getPackage().getEntity(type.getIdentifier()).getName();
					
				} else {
					
					System.err.println("Unknown identifier: " + type.getIdentifier());
					
				}
				
				break;
			
		}
		
		if ( type.isOptional() ) {
			
			t += "?";
			
		}
		
		return t;
	}
	
	public String generateFileName(MCEntity entity) {
		
		return entity.getName() + ".swift";
		
	}
	
	public void validateModel(MCEntity entity, Map<String, Object> model) {
		
		
		
	}
	
	
	
}

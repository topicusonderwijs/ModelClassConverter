package mcconverter.model;

import java.util.HashMap;
import java.util.Map;

import mcconverter.generators.Generator;

public class MCEntity {
	
	private String identifier;
	private String name;
	
	public MCEntity(String identifier, String name) {
		
		this.identifier = identifier;
		this.name = name;
		
	}
	
	public String getIdentifier() {
		
		return identifier;
		
	}
	
	public String getName() {
		
		return name;
		
	}
	
	public Map<String, Object> getModel(Generator generator) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("entity_identifier", getIdentifier());
		model.put("entity_name", getName());
		
		return model;
		
	}
	
}
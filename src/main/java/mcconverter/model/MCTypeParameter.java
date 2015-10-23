package mcconverter.model;

import java.util.HashMap;
import java.util.Map;

import mcconverter.generators.Generator;

public class MCTypeParameter {
	
	private String name;
	private MCType type;
	
	public MCTypeParameter(MCType type) {
		
		this(null, type);
		
	}
	
	public MCTypeParameter(String name, MCType type) {
		
		this.name = name;
		this.type = type;
		
	}
	
	public boolean hasName() {
		
		return getName() != null; 
		
	}
	
	public String getName() {
		
		return name;
		
	}
	
	public boolean hasType() {
		
		return getType() != null;
		
	}
	
	public MCType getType() {
		
		return type;
		
	}
	
	public Map<String, Object> getModel(Generator generator) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("parameter_literal", generator.generateTypeParameterLiteral(this));
		
		return model;
		
	}
	
}

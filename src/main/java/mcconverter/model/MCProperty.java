package mcconverter.model;

import java.util.Map;

import mcconverter.generators.Generator;

import org.apache.commons.lang.StringUtils;

public class MCProperty extends MCEntity {

	private String key;
	private MCType type;
	
	public MCProperty(String identifier, String name, String key, MCType type) {
		
		super( identifier, name );
		
		this.key = key;
		this.type = type;
		
	}
	
	public String getKey() {
		
		return key;
		
	}
	
	public MCType getType() {
		
		return type;
		
	}
	
	public Map<String, Object> getModel(Generator generator) {
		
		Map<String, Object> model = super.getModel(generator);
		
		model.put("property_name", generator.generatePropertyName(this));
		model.put("property_key", getKey());
		model.put("property_type", getType().getModel(generator));
		
		return model;
		
	}
	
	public String toString(int indent) {
		
		String t = StringUtils.repeat("\t", indent);
		
		String s = "";
		
		s += t + "Property(" + getName() + " (" + getKey() + ") : " + getType().toString() + ")";
		
		return s;
		
	}
	
	public String toString() {
		
		return toString(0);
		
	}
	
}

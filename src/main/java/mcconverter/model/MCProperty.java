package mcconverter.model;

import java.util.Map;

import mcconverter.generators.Generator;

import org.apache.commons.lang.StringUtils;

public class MCProperty extends MCEntity {
	
	private MCType type;
	
	public MCProperty(String identifier, String name, MCType type) {
		
		super( identifier, name );
		
		this.type = type;
		
	}
	
	public MCType getType() {
		
		return type;
		
	}
	
	public Map<String, Object> getModel(Generator generator) {
		
		Map<String, Object> model = super.getModel(generator);
		
		model.put("property_type", generator.generateType(getType()));
		
		return model;
		
	}
	
	public String toString(int indent) {
		
		String t = StringUtils.repeat("\t", indent);
		
		String s = "";
		
		s += t + "Property(" + getName() + " : " + getType().toString() + ")";
		
		return s;
		
	}
	
	public String toString() {
		
		return toString(0);
		
	}
	
}

package mcconverter.model;

import java.util.Map;

import mcconverter.generators.AbstractGenerator;

import org.apache.commons.lang.StringUtils;

public class MCProperty extends MCEntity implements MCModelable {

	private String key;
	private MCType type;
	private String value;
	private boolean isStatic;
	private boolean isConstant;
	private boolean isInitialized;
	
	public MCProperty(String identifier, String name, String key, MCType type) {
		
		this(identifier, name, key, type, null, false, false);
		
	}
	
	public MCProperty(String identifier, String name, String key, MCType type, String value, boolean isStatic, boolean isConstant) {
		
		super( identifier, name );
		
		setKey(key);
		
		this.type = type;
		this.value = value;
		this.isStatic = isStatic;
		this.isConstant = isConstant;
		this.isInitialized = true;
		
		type.setOwner(this);
		
	}
	
	public String getKey() {
		
		return key;
		
	}
	
	public void setKey(String key) {
		
		this.key = key;
		
	}
	
	public MCType getType() {
		
		return type;
		
	}
	
	public void setType(MCType type) {
		
		this.type = type;
		
	}
	
	public boolean hasValue() {
		
		return getValue() != null;
		
	}
	
	public String getValue() {
		
		return value;
		
	}
	
	public boolean isStatic() {
		
		return isStatic;
		
	}
	
	public boolean isConstant() {
		
		return isConstant;
		
	}
	
	public boolean isInitialized() {
		
		return isInitialized;
		
	}
	
	public void setInitialized(boolean initialized) {
		
		isInitialized = initialized;
		
	}
	
	public Map<String, Object> getModel(AbstractGenerator generator) {
		
		Map<String, Object> model = super.getModel(generator);
		
		model.put("property_literal", generator.generatePropertyLiteral(this));
		model.put("property_name", generator.generatePropertyName(this));
		model.put("property_key", getKey());
		model.put("property_type", getType().getModel(generator));
		model.put("property_value", generator.generatePropertyValue(this));
		model.put("property_isStatic", isStatic());
		model.put("property_isConstant", isConstant());
		model.put("property_mapping", generator.generatePropertyMapping(this));
		
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

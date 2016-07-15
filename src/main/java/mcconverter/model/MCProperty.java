package mcconverter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mcconverter.generators.Generator;

import org.apache.commons.lang3.StringUtils;

public class MCProperty extends MCEntity implements MCModelable {
	
	/* ===== Private Properties ===== */
	
	private String key;
	private MCType type;
	private MCEntity entity;
	private MCPropertyValue value;
	private boolean isStatic;
	private boolean isConstant;
	private boolean isInitialized;
	
	
	
	/* ===== Construction ===== */
	
	public MCProperty(String identifier, String name, String key, MCType type, MCPropertyValue value) {
		
		this(identifier, name, key, type, value, false, false);
		
	}
	
	public MCProperty(String identifier, String name, String key, MCType type, MCPropertyValue value, boolean isStatic, boolean isConstant) {
		
		super( identifier, name );
		
		setKey(key);
		
		this.type = type;
		this.value = value;
		this.isStatic = isStatic;
		this.isConstant = isConstant;
		this.isInitialized = true;
		
		type.setOwner(this);
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public String getKey() {
		
		return key;
		
	}
	
	public void setKey(String key) {
		
		this.key = key;
		
	}
	
	public void setEntity(MCEntity entity) {
		
		this.entity = entity;
		
	}
	
	public MCEntity getEntity() {
		
		return entity;
		
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
	
	public MCPropertyValue getValue() {
		
		return value;
		
	}
	
	public void setValue(MCPropertyValue value) {
		
		this.value = value;
		
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
	
	public static List<Map<String, Object>> getModel(Generator generator, List<MCProperty> properties) {
		
		List<Map<String, Object>> model = new ArrayList<Map<String, Object>>();
		
		for ( MCProperty property : properties ) {
			
			model.add(property.getModel(generator));
			
		}
		
		return model;
		
	}
	
	public Map<String, Object> getModel(Generator generator) {
		
		Map<String, Object> model = super.getModel(generator);
		
		model.put("property_literal", generator.generatePropertyLiteral(this));
		model.put("property_name", generator.generatePropertyName(this));
		model.put("property_key", getKey());
		model.put("property_type", getType().getModel(generator));
		model.put("property_value", generator.generatePropertyValue(this));
		model.put("property_isStatic", isStatic());
		model.put("property_isConstant", isConstant());
		model.put("property_mapping", generator.generatePropertyMapping(this));
		model.put("property_transform", generator.generatePropertyTransform(this));
		
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

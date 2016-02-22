package mcconverter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mcconverter.generators.AbstractGenerator;

import org.apache.commons.lang.StringUtils;

public class MCClass extends MCEntity {
	
	/* ===== Private Properties ===== */
	
	private MCType type;
	private MCType parent;
	private List<MCProperty> properties;
	private List<MCProperty> constants;
	private boolean isProtocol;
	
	
	
	/* ===== Construction ===== */
	
	public MCClass(MCType type, String name, boolean isProtocol) {
		
		super ( type.getIdentifier(), name );
		
		this.type = type;
		this.properties = new ArrayList<MCProperty>();
		this.constants = new ArrayList<MCProperty>();
		this.isProtocol = isProtocol;
		
		type.setOwner(this);
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public MCType getType() {
		
		return type;
		
	}
	
	public boolean hasParent() {
		
		return getParent() != null;
		
	}
	
	public MCType getParent() {
		
		return parent;
		
	}
	
	public void setParent(MCType parent) {
		
		this.parent = parent;
		
	}
	
	public boolean isProtocol() {
		
		return isProtocol;
		
	}
	
	public void addProperty(MCProperty property) {
		
		if ( property.isConstant() ) {
			
			getConstants().add(property);
			
		} else {
			
			getProperties().add(property);
			
		}
		
	}
	
	public void removeProperty(MCProperty property) {
		
		if ( property.isConstant() ) {
			
			getConstants().remove(property);
			
		} else {
			
			getProperties().remove(property);
			
		}
		
	}
	
	public boolean hasProperties() {
		
		return getProperties().size() > 0;
		
	}
	
	public boolean hasProperty(String name) {
		
		return getProperty(name) != null;
		
	}
	
	public MCProperty getProperty(String name) {
		
		MCProperty property = null;
		
		for (MCProperty current : getProperties()) {
			
			if ( current.getName().equals(name) ) {
				
				property = current;
				break;
				
			}
			
		}
		
		return property;
		
	}
	
	public List<MCProperty> getProperties() {
		
		return properties;
		
	}
	
	public boolean hasConstants() {
		
		return getConstants().size() > 0;
		
	}
	
	public List<MCProperty> getConstants() {
		
		return constants;
		
	}
	
	public Map<String, Object> getModel(AbstractGenerator generator) {
		
		Map<String, Object> model = super.getModel(generator);
		
		if ( hasParent() ) {
			
			model.put("class_parent", generator.generateTypeLiteral(getParent()));
			
		}
		
		List<Map<String, Object>> constants = new ArrayList<Map<String, Object>>();
		
		for ( MCProperty constant : getConstants() ) {
			
			constants.add(constant.getModel(generator));
			
		}
		
		List<Map<String, Object>> properties = new ArrayList<Map<String, Object>>();
		
		for ( MCProperty property : getProperties() ) {
			
			properties.add(property.getModel(generator));
			
		}
		
		model.put("class_isProtocol", isProtocol());
		model.put("class_constants", constants);
		model.put("class_properties", properties);
		model.put("class_type", getType().getModel(generator));
		
		return model;
		
	}
	
	public String toString(int indent) {
		
		String t = StringUtils.repeat("\t", indent);
		
		String s = "";
		
		s += t + "Class(" + getName();
		
		if ( hasParent() ) {
			
			s += " : " + getParent();
			
		}
		
		s += "){\n";
		
		for ( MCProperty property : getProperties() ) {
			
			s += t + "\t" + property.toString(indent + 1) + "\n";
			
		}
		
		s += t + "}";
		
		return s;
		
	}
	
	public String toString() {
		
		return toString(0);
		
	}
	
}

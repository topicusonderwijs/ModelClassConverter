package mcconverter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mcconverter.generators.Generator;

import org.apache.commons.lang.StringUtils;

public class MCClass extends MCEntity {
	
	private MCType type;
	private MCType parent;
	private List<MCProperty> properties;
	
	public MCClass(MCType type, String name) {
		
		this( type, name, null );
		
	}
	
	public MCClass(MCType type, String name, MCType parent) {
		
		super ( type.getIdentifier(), name );
		
		this.type = type;
		this.parent = parent;
		this.properties = new ArrayList<MCProperty>();
		
	}
	
	public MCType getType() {
		
		return type;
		
	}
	
	public boolean hasParent() {
		
		return getParent() != null;
		
	}
	
	public MCType getParent() {
		
		return parent;
		
	}
	
	public void addProperty(MCProperty property) {
		
		getProperties().add(property);
		
	}
	
	public boolean hasProperties() {
		
		return getProperties().size() > 0;
		
	}
	
	public List<MCProperty> getProperties() {
		
		return properties;
		
	}
	
	public Map<String, Object> getModel(Generator generator) {
		
		Map<String, Object> model = super.getModel(generator);
		
		if ( hasParent() ) {
			
			model.put("class_parent", generator.generateTypeLiteral(getParent()));
			
		}
		
		List<Map<String, Object>> properties = new ArrayList<Map<String, Object>>();
		
		for ( MCProperty property : getProperties() ) {
			
			properties.add(property.getModel(generator));
			
		}
		
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
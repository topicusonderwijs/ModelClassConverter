package mcconverter.configuration;

import java.util.ArrayList;
import java.util.List;

import mcconverter.model.MCClass;
import mcconverter.model.MCType;

public class CustomClass extends CustomEntity {
	
	/* ===== Private Properties ===== */
	
	private String parent;
	private List<CustomProperty> properties;
	
	
	/* ===== Construction ===== */
	
	/**
	 * Constructs a custom class.
	 */
	public CustomClass() {
		
		properties = new ArrayList<CustomProperty>();
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public boolean hasParent() {
		
		return getParent() != null;
		
	}
	
	public String getParent() {
		
		return parent;
		
	}
	
	public void setParent(String parent) {
		
		this.parent = parent;
		
	}
	
	public boolean hasProperties() {
		
		return getProperties() != null && getProperties().size() > 0;
		
	}
	
	public boolean hasProperty(String name) {
		
		return getProperty(name) != null;
		
	}
	
	public boolean hasIgnoredProperty(String name) {
		
		return hasProperty(name) && getProperty(name).isIgnored();
		
	}
	
	public CustomProperty getProperty(String name) {
		
		CustomProperty property = null;
		
		for ( CustomProperty current : getProperties() ) {
			
			if ( current.getName().equals(name) ) {
				
				property = current;
				break;
				
			}
			
		}
		
		return property;
		
	}
	
	public List<CustomProperty> getProperties() {
		
		return properties;
		
	}
	
	public void addProperty(CustomProperty property) {
		
		getProperties().add(property);
		
	}
	
	public MCClass toClass() {
		
		MCType type;
		
		if ( hasType() ) {
			
			type = getType().toType();
			
		} else {
			
			type = new MCType(getName(), false);
			
		}
		
		MCClass c = new MCClass(type, getName(), false);
		
		if ( hasParent() ) {
			
			c.setParent(new MCType(getParent(), false));
			
		}
		
		return c;
		
	}
	
}

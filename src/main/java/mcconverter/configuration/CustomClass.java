package mcconverter.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mcconverter.model.MCClass;
import mcconverter.model.MCPackage;
import mcconverter.model.MCType;

public class CustomClass extends CustomEntity {
	
	/* ===== Private Properties ===== */
	
	private String parent;
	private String descriptor;
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
		property.setClasss(this);
		
	}
	
	public MCClass toClass(MCPackage pack) {
		
		MCType type;
		
		if ( hasType() ) {
			
			type = getType().toType();
			
		} else {
			
			type = new MCType(getName(), false);
			
		}
		
		MCClass c = new MCClass(type, getName(), false);
		
		if ( hasParent() && pack.hasClass(getParent()) ) {
			
			c.setParent(pack.getClass(getParent()));
			
		}
		
		return c;
		
	}
	
	public String toString() {
		
		String s = "CustomClass(\n";
		
		s += "\tName = " + getName();
		
		if ( hasProperties() ) {
			
			s += "\n\tProperties = ";
			
			Iterator<CustomProperty> iterator = getProperties().iterator();
			while ( iterator.hasNext() ) {
				
				s += iterator.next();
				
				if ( iterator.hasNext() ) {
					
					s += ", ";
					
				}
				
			}
			
		}
		
		s += "\n)";
		
		return s;
		
	}

	public boolean hasDescriptor() {
		
		return getDescriptor() != null;
	}
	
	public String getDescriptor() {
		
		return descriptor;
		
	}
	
	public void setDescriptor(String descriptor) {
	
		this.descriptor = descriptor;
	
	}
}

package mcconverter.configuration;

public class CustomProperty extends CustomEntity {

	/* ===== Private Properties ===== */
	
	private String key;
	private String transform;
	private boolean initialize;
	private CustomValue value;
	private CustomClass classs;
	
	
	
	/* ===== Public Functions ===== */
	
	public String getKey() {
		
		String key = this.key;
		
		if ( key == null ) {
			
			key = getName();
			
		}
		
		return key;
		
	}
	
	public void setKey(String key) {
		
		this.key = key;
		
	}
	
	public String getTransform() {
		
		return transform;
		
	}
	
	public boolean hasTransform() {
		
		return getTransform() != null;
		
	}
	
	public void setTransform(String transform) {
		
		this.transform = transform;
		
	}
	
	public boolean isInitialized() {
		
		return initialize;
		
	}
	
	public void setInitialized(boolean initialize) {
		
		this.initialize = initialize;
		
	}
	
	public boolean hasValue() {
		
		return getValue() != null;
		
	}
	
	public CustomValue getValue() {
		
		return value;
		
	}
	
	public void setValue(CustomValue value) {
		
		this.value = value;
		
	}
	
	public boolean hasClasss() {
		
		return getClasss() != null;
		
	}
	
	public CustomClass getClasss() {
		
		return classs;
		
	}
	
	public void setClasss(CustomClass classs) {
		
		this.classs = classs;
		
	}
	
	public String toString() {
		
		String s = "CustomProperty(\n";
		
		s += "\tName = " + getName() + "\n";
		s += "\tValue = " + getValue() + "\n";
		s += "\tTransform = " + getTransform() + "\n";
		s += "\tType = " + getType() + "\n";
		
		s += ")";
		
		return s;
		
	}
	
}

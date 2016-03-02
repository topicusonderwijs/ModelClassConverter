package mcconverter.configuration;

public class CustomProperty extends CustomEntity {

	/* ===== Private Properties ===== */
	
	private String key;
	private boolean initialize;
	private CustomValue value;
	
	
	
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
	
}

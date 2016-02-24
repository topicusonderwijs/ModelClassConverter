package mcconverter.configuration;

public class CustomProperty extends CustomEntity {

	/* ===== Private Properties ===== */
	
	private String key;
	private boolean initialize;
	
	
	
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
	
}

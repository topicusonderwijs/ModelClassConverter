package mcconverter.model;

public class MCPropertyValue {
	
	/* ===== Private Properties ===== */
	
	private String value;
	private boolean literal;
	
	
	
	/* ===== Public Functions ===== */
	
	public MCPropertyValue(String value, boolean literal) {
		
		this.value = value;
		this.literal = literal;
		
	}
	
	
	/* ===== Public Functions ===== */
	
	public boolean isLiteral() {
		
		return literal;
		
	}
	
	public String getValue() {
		
		return value;
		
	}
	
}

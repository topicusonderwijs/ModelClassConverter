package mcconverter.configuration;

import mcconverter.model.MCPropertyValue;

public class CustomValue {
	
	/* ===== Constants ===== */
	
	private static final String LiteralSymbol = "$";
	
	
	
	/* ===== Private Properties ===== */
	
	private String value;
	private boolean literal;
	
	
	
	/* ===== Public Functions ===== */
	
	public CustomValue(String value) {
		
		literal = value.startsWith(LiteralSymbol);
		
		if ( literal ) {
			
			value = value.substring(1);
			
		}
		
		this.value = value;
		
	}
	
	
	/* ===== Public Functions ===== */
	
	public boolean isLiteral() {
		
		return literal;
		
	}
	
	public String getValue() {
		
		return value;
		
	}
	
	public MCPropertyValue toValue() {
		
		return new MCPropertyValue(getValue(), isLiteral());
		
	}
	
}

package mcconverter.configuration;

public class CustomTransform {
	
	/* ===== Constants ===== */
	
	private static final String LiteralSymbol = "$";
	
	
	
	/* ===== Private Properties ===== */
	
	private String transform;
	private boolean literal;
	
	
	
	/* ===== Public Functions ===== */
	
	private CustomTransform(String input) {
		
		literal = input.startsWith(LiteralSymbol);
		
		if ( literal ) {
			
			input = input.substring(1);
			
		}
		
		this.transform = input;
		
	}
	
	
	/* ===== Public Functions ===== */
	
	public static CustomTransform fromString(String input) {
		
		CustomTransform transform = null;
		
		if ( input != null && !input.isEmpty() ) {
			
			transform = new CustomTransform(input);
			
		}
		
		return transform;
		
	}
	
	public boolean isLiteral() {
		
		return literal;
		
	}
	
	public String getTransform() {
		
		return transform;
		
	}
	
}

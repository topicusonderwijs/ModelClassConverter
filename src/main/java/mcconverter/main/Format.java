package mcconverter.main;

public class Format {
	
	/* ===== Constants ===== */
	
	public static final int Black = 30;
	public static final int Red = 31;
	public static final int Green = 32;
	public static final int Yellow = 33;
	public static final int Blue = 34;
	public static final int Magenta = 35;
	public static final int Cyan = 36;
	public static final int White = 37;
	public static final int Light = 1;
	public static final int Dark = 21;
	public static final int Reset = 0;
	
	
	
	/* ===== Public Properties ===== */
	
	/**
	 * A flag indicating whether formatting is enabled (`true`) or not (`false`).
	 */
	public static boolean Enabled = false;
	
	
	
	/* ===== Public Functions ===== */
	
	/**
	 * Creates and returns a formatted string by formatting the given message using the given formats.
	 * The format is only applied when colouring is enabled.
	 */
	public static String format(String message, int... format) {
		
		return format(format) + message + format(Reset);
		
	}
	
	
	
	/* ===== Private Functions ===== */
	
	private static String format(int... format) {
		
		String output = "";
		
		if ( Enabled && format.length > 0 ) {
			
			output += (char)27 + "[";
			boolean first = true;
			
			for ( int code : format ) {
				
				if ( !first ) {
					
					output += ";";
					
				}
				
				output += code;
				first = false;
				
			}
			
			output += "m";
			
		}
		
		return output;
		
	}
	
}

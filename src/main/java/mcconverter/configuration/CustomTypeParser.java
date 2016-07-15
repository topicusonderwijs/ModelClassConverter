package mcconverter.configuration;

import java.util.ArrayList;
import java.util.List;

import mcconverter.model.MCNativeType;

import org.apache.commons.lang3.ArrayUtils;

public class CustomTypeParser {

	/* ===== Constants ===== */

	private static final char ParameterOpenToken = '(';
	private static final char ParameterSeperatorToken = ',';
	private static final char ParameterCloseToken = ')';
	private static final String ParameterOptionalToken = "?";
	private static final char[] TypeEndingTokens = {ParameterOpenToken, ParameterSeperatorToken, ParameterCloseToken};
	
	
	
	/* ===== Private Properties ===== */
	
	private String input;
	
	
	
	/* ===== Construction ===== */
	
	public CustomTypeParser(String input) {
		
		this.input = input;
		
	}
	
	
	/* ===== Public Functions ===== */
	
	public CustomType parse() {
		
		CustomType type = null;
		
		if ( input != null ) {

			List<CustomType> types = enter();
			
			if ( types.size() > 0 ) {
				
				type = types.get(0);
				
			}
			
		}
		
		return type;
		
	}
	
	
	/* ===== Private Functions ===== */
	
	private List<CustomType> enter() {
		
		List<CustomType> types = new ArrayList<CustomType>();
		
		boolean done = false;
		
		while ( !done ) {
			
			CustomType type = new CustomType();
			types.add(type);
			
			String name = read();
			
			if ( name.length() > 0 ) {
				
				if ( name.endsWith(ParameterOptionalToken) ) {
					
					type.setOptional(true);
					name = name.substring(0, name.length() - ParameterOptionalToken.length());
					
				}
				
				MCNativeType nativeType = MCNativeType.fromName(name);
				
				if ( nativeType != MCNativeType.NonNative ) {
					
					type.setNativeType(nativeType);
					
				} else {
					
					type.setName(name);
					
				}
				
			}
			
			switch ( peek() ) {
			
			case ParameterOpenToken:
				pop(1);
				List<CustomType> parameters = enter();
				type.addParameters(parameters);
				break;
				
			case ParameterSeperatorToken:
				pop(1);
				break;
				
			case ParameterCloseToken:
				pop(1);
				done = true;
				break;
				
			default:
				break;
				
			}
			
			done |= input.length() == 0;
			
		}
		
		return types;
		
	}
	
	private String read() {
		
		String read = "";
		
		for ( int i = 0; i < input.length(); i++ ) {
			
			char c = input.charAt(i);
			
			if ( !ArrayUtils.contains(TypeEndingTokens, c) ) {
				
				read += c;
				
			} else {
				
				break;
				
			}
			
		}
		

		pop(read.length());
		
		return read;
		
	}
	
	private char peek() {
		
		char p = 0;
		
		if ( input.length() > 0 ) {
			
			p = input.charAt(0);
			
		}
		
		return p;
		
	}
	
	private char pop(int length) {
		
		char i = 0;
		
		if ( length > 0 ) {

			if ( input.length() > length ) {
				
				input = input.substring(length);
				i = input.charAt(0);
				
			} else {
				
				input = "";
				
			}
			
		}
		
		return i;
		
	}
	
}

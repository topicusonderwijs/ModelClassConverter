package mcconverter.model;

import java.util.List;

public class MCType {
	
	/* ===== Private Properties ===== */
	
	private String identifier;
	private MCNativeType nativeType;
	private boolean optional;
	private List<MCType> parameters;
	
	
	
	/* ===== Construction ===== */
	
	public MCType(MCNativeType nativeType, boolean optional) {
		
		this(nativeType, null, optional);
		
	}
	
	public MCType(MCNativeType NativeType, List<MCType> parameters, boolean optional) {
		
		this.nativeType = NativeType;
		this.parameters = parameters;
		this.identifier = nativeType.getIdentifier();
		this.optional = optional;
		
	}
	
	public MCType(String identifier, boolean optional) {
		
		this.nativeType = MCNativeType.NonNative;
		this.identifier = identifier;
		this.optional = optional;
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public String getIdentifier() {
		
		return identifier;
		
	}
	
	public boolean isNativeType() {
		
		return nativeType != MCNativeType.NonNative;
		
	}
	
	public MCNativeType getNativeType() {
		
		return nativeType;
		
	}
	
	public boolean isOptional() {
		
		return optional;
		
	}
	
	public boolean hasParameters() {
		
		return getParameters() != null && getParameters().size() > 0;
		
	}
	
	public List<MCType> getParameters() {
		
		return parameters;
		
	}
	
	public String toString() {
		
		String s = "";
		
		if ( isNativeType() ) {
			
			s += "Native";
			
		}
		
		s += "Type(" + getIdentifier();
		
		if ( isOptional() ) {
			
			s += "?";
			
		}
		
		if ( hasParameters() ) {
			
			s += " <";
			
			boolean first = true;
			
			for (MCType parameter : getParameters()) {
				
				if ( !first ) {
					
					s += ", ";
					
				}
				
				s += parameter.toString();
				first = false;
				
			}
			
			s += ">";
			
		}
		
		s += ")";
		
		return s;
		
	}
	
}

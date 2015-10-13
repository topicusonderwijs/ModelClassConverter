package mcconverter.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mcconverter.generators.Generator;

public class MCType {
	
	/* ===== Private Properties ===== */
	
	private String identifier;
	private MCNativeType nativeType;
	private boolean optional;
	private List<MCType> parameters;
	
	
	
	/* ===== Construction ===== */
	
	public MCType(MCNativeType nativeType) {
		
		this(nativeType, false);
		
	}
	
	public MCType(MCNativeType nativeType, boolean optional) {
		
		this(nativeType, null, optional);
		
	}
	
	public MCType(MCNativeType nativeType, List<MCType> parameters, boolean optional) {
		
		this.nativeType = nativeType;
		this.parameters = parameters;
		this.identifier = nativeType.getIdentifier();
		this.optional = optional;
		
	}
	
	public MCType(String identifier, boolean optional) {
		
		this(identifier, null, optional);
		
	}
	
	public MCType(String identifier, List<MCType> parameters, boolean optional) {
		
		this.nativeType = MCNativeType.NonNative;
		this.parameters = parameters;
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
	
	/**
	 * Determines and returns whether the type has exactly the given amount of parameters.
	 */
	public boolean hasParameterCount(int count) {
		
		return hasParameters() && getParameters().size() == count;
		
	}
	
	/**
	 * Determines and returns whether the type has a parameter at the given index.
	 */
	public boolean hasParameter(int index) {
		
		return hasParameters() && index >= 0 && index < getParameters().size();
		
	}
	
	/**
	 * Determines and returns the parameter at the given index or null if there is none.
	 */
	public MCType getParameter(int index) {
		
		MCType parameter = null;
		
		if ( hasParameter(index) ) {
			
			parameter = getParameters().get(index);
			
		}
		
		return parameter;
		
	}
	
	public Map<String, Object> getModel(Generator generator) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("type_identifier", getIdentifier());
		model.put("type_name", generator.generateTypeName(this));
		model.put("type_optional", isOptional());
		
		return model;
		
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

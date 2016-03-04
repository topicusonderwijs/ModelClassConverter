package mcconverter.configuration;

import java.util.ArrayList;
import java.util.List;

import mcconverter.model.MCNativeType;
import mcconverter.model.MCType;
import mcconverter.model.MCTypeParameter;

public class CustomType {
	
	/* ===== Private Properties ===== */
	
	private String name;
	private MCNativeType nativeType;
	private boolean optional;
	private List<CustomType> parameters;
	
	
	
	/* ===== Construction ===== */
	
	public CustomType() {
		
		nativeType = MCNativeType.NonNative;
		parameters = new ArrayList<CustomType>();
		setOptional(false);
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public static CustomType fromString(String input) {
		
		CustomTypeParser parser = new CustomTypeParser(input);
		
		return parser.parse();
		
	}
	
	public boolean hasName() {
		
		return name != null;
		
	}
	
	public String getName() {
		
		String name = getNativeType().getIdentifier();
		
		if ( hasName() ) {
			
			name = this.name;
			
		}
		
		return name;
		
	}
	
	public void setName(String name) {
		
		this.name = name;
		
	}
	
	public void setNativeType(MCNativeType nativeType) {
		
		this.nativeType = nativeType;
		
	}
	
	public MCNativeType getNativeType() {
		
		return nativeType;
		
	}
	
	public boolean isNativeType() {
		
		return getNativeType() != MCNativeType.NonNative;
		
	}
	
	public boolean isOptional() {
		
		return optional;
		
	}
	
	public void setOptional(boolean optional) {
		
		this.optional = optional;
		
	}
	
	public boolean hasParameters() {
		
		return getParameters() != null && getParameters().size() > 0;
		
	}
	
	public void addParameters(List<CustomType> parameters) {
		
		for ( CustomType parameter : parameters ) {
			
			addParameter(parameter);
			
		}
		
	}
	
	public void addParameter(CustomType parameter) {
		
		getParameters().add(parameter);
		
	}
	
	public List<CustomType> getParameters() {
		
		return parameters;
		
	}
	
	public MCType toType() {
		
		return toType(1);
		
	}
	
	private MCType toType(int depth) {
		
		List<MCTypeParameter> parameters = new ArrayList<MCTypeParameter>();
		
		for ( CustomType parameter : getParameters() ) {
			
			parameters.add(new MCTypeParameter(null, parameter.toType(depth + 1), depth));
			
		}
		
		MCType type;
		
		if ( isNativeType() ) {
			
			type = new MCType(getNativeType(), parameters, false);
			
		} else {
			
			type = new MCType(getName(), parameters, false);
			
		}
		
		type.setOptional(isOptional());
		
		return type;
		
	}
	
	public String toString() {
		
		return "CustomType(" + getName() + ")";
		
	}
	
}

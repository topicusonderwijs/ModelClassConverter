package mcconverter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mcconverter.generators.AbstractGenerator;

public class MCType implements MCModelable {
	
	/* ===== Private Properties ===== */
	
	private String identifier;
	private MCNativeType nativeType;
	private boolean optional;
	private MCModelable owner;
	private List<MCTypeParameter> parameters;
	private List<MCType> protocols;
	
	
	
	/* ===== Construction ===== */
	
	/**
	 * Construction a type from the given native type.
	 * The constructed type is not optional.
	 */
	public MCType(MCNativeType nativeType) {
		
		this(nativeType, false);
		
	}
	
	/**
	 * Constructs a type from the given native type which can be potentially optional.
	 */
	public MCType(MCNativeType nativeType, boolean optional) {
		
		this(nativeType, null, optional);
		
	}
	
	/**
	 * Constructs a type from the given native type.
	 * @param nativeType The native type of the type.
	 * @param parameters The type parameters of the type.
	 * @param optional A flag indicating whether the type is optional (`true`) or not (`false`).
	 */
	public MCType(MCNativeType nativeType, List<MCTypeParameter> parameters, boolean optional) {
		
		this.nativeType = nativeType;
		this.owner = null;
		this.parameters = parameters;
		this.protocols = new ArrayList<MCType>();
		this.identifier = nativeType.getIdentifier();
		this.optional = optional;
		
	}
	
	public MCType(String identifier, boolean optional) {
		
		this(identifier, null, optional);
		
	}
	
	public MCType(String identifier, List<MCTypeParameter> parameters) {
		
		this(identifier, parameters, false);
		
	}
	
	public MCType(String identifier, List<MCTypeParameter> parameters, boolean optional) {
		
		this.nativeType = MCNativeType.NonNative;
		this.parameters = parameters;
		this.protocols = new ArrayList<MCType>();
		this.identifier = identifier;
		this.optional = optional;
		
	}	
	
	
	
	/* ===== Public Functions ===== */
	
	public String getIdentifier() {
		
		return identifier;
		
	}
	
	/**
	 * Determines and returns whether the type is a native type.
	 */
	public boolean isNativeType() {
		
		return nativeType != MCNativeType.NonNative;
		
	}
	
	/**
	 * Determines and returns whether the type is one of the given native types.
	 */
	public boolean isNativeType(MCNativeType... types) {
		
		boolean nativeType = false;
		
		if ( isNativeType() ) {

			for ( MCNativeType type : types ) {
				
				if ( getNativeType() == type ) {
					
					nativeType = true;
					break;
					
				}
				
			}
			
		}
		
		return nativeType;
		
	}
	
	public MCNativeType getNativeType() {
		
		return nativeType;
		
	}
	
	public boolean isOptional() {
		
		return optional;
		
	}
	
	public void setOptional(boolean optional) {
		
		this.optional = optional;
		
	}
	
	/**
	 * Determines and returns whether the type has an owner.
	 */
	public boolean hasOwner() {
		
		return getOwner() != null;
		
	}
	
	/**
	 * Returns the owner of the type if available.
	 * If there is no owner then `null` is returned.
	 * The owner of a type can be a class, property or type parameter depending on where it is used.
	 * TODO: Perhaps there should be subclasses to distinguish between the different uses of type.
	 */
	public MCModelable getOwner() {
		
		return owner;
		
	}
	
	/**
	 * Sets the owner of the type.
	 */
	public void setOwner(MCModelable owner) {
		
		this.owner = owner;
		
	}
	
	
	public boolean hasParameters() {
		
		return getParameters() != null && getParameters().size() > 0;
		
	}
	
	public List<MCTypeParameter> getParameters() {
		
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
	public MCTypeParameter getParameter(int index) {
		
		MCTypeParameter parameter = null;
		
		if ( hasParameter(index) ) {
			
			parameter = getParameters().get(index);
			
		}
		
		return parameter;
		
	}
	
	/**
	 * Determines and returns a copy of the type of the parameter at the given index.
	 * The returned type is set to be non-optional.
	 */
	public MCType getNonOptionalParameterType(int index) {
		
		MCType type = null;
		
		MCTypeParameter parameter = getParameter(index);
		
		if ( parameter != null ) {
			
			type = parameter.getType().copy(false);
			
		}
		
		return type;
		
	}
	
	
	public void addProtocol(MCType protocol) {
		
		protocols.add(protocol);
		
	}
	
	public void addProtocols(List<MCType> protocols) {
		
		for ( MCType protocol : protocols ) {
			
			protocols.add(protocol);
			
		}
		
	}
	
	/**
	 * Determines and returns whether the type implements any protocols (or interfaces).
	 */
	public boolean hasProtocols() {
		
		return getProtocols().size() > 0;
		
	}
	
	/**
	 * Returns the protocols that the type implements if available.
	 */
	public List<MCType> getProtocols() {
		
		return protocols;
		
	}
	
	public Map<String, Object> getModel(AbstractGenerator generator) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<Map<String, Object>> parameters = new ArrayList<Map<String, Object>>();
		
		if ( hasParameters() ) {

			for ( MCTypeParameter parameter : getParameters() ) {
				
				parameters.add(parameter.getModel(generator));
				
			}
			
		}
		
		List<Map<String, Object>> protocols = new ArrayList<Map<String, Object>>();
		
		if ( hasProtocols() ) {

			for ( MCType protocol : getProtocols() ) {
				
				protocols.add(protocol.getModel(generator));
				
			}
			
		}

		model.put("type_name", generator.generateTypeName(this));
		model.put("type_parameters", parameters);
		model.put("type_protocols", protocols);
		model.put("type_identifier", getIdentifier());
		model.put("type_literal", generator.generateTypeLiteral(this));
		model.put("type_optional", isOptional());
		model.put("type_isNativeType", isNativeType());
		
		return model;
		
	}
	
	/**
	 * Creates and returns a copy of the type with its optional property set to the given value.
	 * All other properties of the type will remain equal.
	 */
	public MCType copy(boolean optional) {
		
		MCType copy;
		
		if ( isNativeType() ) {
			copy = new MCType(getNativeType(), getParameters(), optional);
		} else {
			copy = new MCType(getIdentifier(), getParameters(), optional);
		}
		
		copy.setOwner(getOwner());
		copy.addProtocols(getProtocols());
		
		return copy;
		
	}
	
	/**
	 * Creates and returns a copy of the type.
	 * All other properties of the type will remain equal.
	 */
	public MCType copy() {
		
		return copy(isOptional());
		
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
			
			for (MCTypeParameter parameter : getParameters()) {
				
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
	
	public boolean equals(Object other) {
		
		boolean equal = false;
		
		if ( other != null && other instanceof MCType ) {
			
			MCType otherType = (MCType)other;
			
			equal = (
				(otherType.isNativeType() && isNativeType() && otherType.getNativeType() == getNativeType())
				||
				(otherType.getIdentifier().equals(getIdentifier()))
			);
			
		}
		
		return equal;
		
	}
	
}

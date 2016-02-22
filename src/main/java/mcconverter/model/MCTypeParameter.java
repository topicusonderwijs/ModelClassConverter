package mcconverter.model;

import java.util.HashMap;
import java.util.Map;

import mcconverter.generators.AbstractGenerator;

public class MCTypeParameter implements MCModelable {
	
	/* ===== Private Properties ===== */
	
	private String name;
	private MCType type;
	private int depth;
	
	
	
	/* ===== Construction ===== */
	
	/**
	 * Constructs a type parameter using the given name, type and depth.
	 */
	public MCTypeParameter(String name, MCType type, int depth) {
		
		this.name = name;
		this.type = type;
		this.depth = depth;
		
		type.setOwner(this);
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	/**
	 * Determines and returns whether the type parameter has a name.
	 */
	public boolean hasName() {
		
		return getName() != null; 
		
	}
	
	/**
	 * Returns the name of the type parameter if available.
	 * A value of `null` is returned if the type parameter has no name.
	 */
	public String getName() {
		
		return name;
		
	}
	
	/**
	 * Determines and returns whether the type parameter has a type associated with it.
	 */
	public boolean hasType() {
		
		return getType() != null;
		
	}
	
	/**
	 * Returns the type of the type parameter if available.
	 * If there is no type then `null` is returned.
	 */
	public MCType getType() {
		
		return type;
		
	}
	
	/**
	 * The depth of the type parameter.
	 * For instance:
	 *           Map<Set<Number>, String>
	 *  Depth:       1   2        1
	 */
	public int getDepth() {
		
		return depth;
		
	}
	
	public Map<String, Object> getModel(AbstractGenerator generator) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("parameter_name", getName());
		model.put("parameter_type", type.getModel(generator));
		model.put("parameter_literal", generator.generateTypeParameterLiteral(this));
		
		return model;
		
	}
	
	public String toString() {
		
		String s = "TypeParameter(Depth(" + getDepth() + ")";
		
		if ( hasName() ) {
			
			s += ", Name(" + getName() + ")";
			
		}
		
		if ( hasType() ) {
			
			s += ", " + getType();
			
		}
		
		s += ")";
		
		return s;
		
	}
	
}

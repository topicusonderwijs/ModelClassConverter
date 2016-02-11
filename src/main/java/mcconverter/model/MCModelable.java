package mcconverter.model;

import java.util.Map;

import mcconverter.generators.Generator;

public interface MCModelable {
	
	/**
	 * Generates a model from the entity using the given generator.
	 */
	public Map<String, Object> getModel(Generator generator);
	
}

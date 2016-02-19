package mcconverter.model;

import java.util.Map;

import mcconverter.generators.AbstractGenerator;

public interface MCModelable {
	
	/**
	 * Generates a model from the entity using the given generator.
	 */
	public Map<String, Object> getModel(AbstractGenerator generator);
	
}

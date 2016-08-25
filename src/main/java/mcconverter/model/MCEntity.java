package mcconverter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import mcconverter.generators.Generator;
import mcconverter.utils.ListUtils;

public class MCEntity implements MCModelable {
	
	/* ===== Private Properties ===== */
	
	private String identifier;
	private String name;
	private List<MCProperty> properties;
	private List<MCProperty> constants;
	
	
	
	/* ===== Construction ===== */
	
	public MCEntity(String identifier, String name) {
		
		this.identifier = identifier;
		this.name = name;
		this.properties = new ArrayList<MCProperty>();
		this.constants = new ArrayList<MCProperty>();
		
	}
	
	
	/* ===== Public Functions ===== */
	
	public String getIdentifier() {
		
		return identifier;
		
	}
	
	public String getName() {
		
		return name;
		
	}
	

	
	public void addProperty(MCProperty property) {
		
		if ( property.isConstant() ) {
			
			getConstants().add(property);
			
		} else {
			
			getProperties().add(property);
			
		}
		
		property.setEntity(this);
		
	}
	
	public void removeProperty(MCProperty property) {
		
		if ( property.isConstant() ) {
			
			getConstants().remove(property);
			
		} else {
			
			getProperties().remove(property);
			
		}
		
	}
	
	public boolean hasProperties() {
		
		return getProperties().size() > 0;
		
	}
	
	public boolean hasProperty(String name) {
		
		return getProperty(name) != null;
		
	}
	
	public MCProperty getProperty(String name) {
		
		MCProperty property = null;
		
		for (MCProperty current : getProperties()) {
			
			if ( current.getName().equals(name) ) {
				
				property = current;
				break;
				
			}
			
		}
		
		return property;
		
	}
	
	public List<MCProperty> getProperties() {
		
		return properties;
		
	}
	
	public List<MCProperty> getOptionalProperties() {
		
		return filter(getProperties(), p -> p.getType().isOptional());
		
	}
	
	public List<MCProperty> getRequiredProperties() {
		
		return filter(getProperties(), p -> !p.getType().isOptional());
		
	}
	
	public boolean hasConstants() {
		
		return getConstants().size() > 0;
		
	}
	
	public List<MCProperty> getConstants() {
		
		return constants;
		
	}
	
	public Map<String, Object> getModel(Generator generator) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("class_constants", MCProperty.getModel(generator, getConstants()));
		model.put("class_properties", MCProperty.getModel(generator, getProperties()));
		model.put("class_properties_valued",
			MCProperty.getModel(generator, ListUtils.filter(getProperties(), p -> generator.generatePropertyValue(p) != null))
		);
		model.put("class_properties_required", MCProperty.getModel(generator, getRequiredProperties()));
		model.put("class_properties_optional", MCProperty.getModel(generator, getOptionalProperties()));
		
		model.put("class_imports", generator.generateImports(this));
		
		model.put("entity_identifier", getIdentifier());
		model.put("entity_name", getName());
		
		return model;
		
	}
	
	

	
	
	/* ===== Private Functions ===== */
	
	protected List<MCProperty> filter(List<MCProperty> properties, Predicate<MCProperty> predicate) {
		
		return properties.stream().filter(predicate).collect(Collectors.toList());
		
	}
	
}

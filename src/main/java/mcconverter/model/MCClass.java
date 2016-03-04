package mcconverter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import mcconverter.generators.Generator;
import mcconverter.utils.ListUtils;

import org.apache.commons.lang3.StringUtils;

public class MCClass extends MCEntity {
	
	/* ===== Private Properties ===== */
	
	private MCType type;
	private MCClass parent;
	private List<MCProperty> properties;
	private List<MCProperty> constants;
	private boolean isProtocol;
	
	
	
	/* ===== Construction ===== */
	
	public MCClass(MCType type, String name, boolean isProtocol) {
		
		super ( type.getIdentifier(), name );
		
		this.type = type;
		this.properties = new ArrayList<MCProperty>();
		this.constants = new ArrayList<MCProperty>();
		this.isProtocol = isProtocol;
		
		type.setOwner(this);
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public MCType getType() {
		
		return type;
		
	}
	
	public boolean hasParent() {
		
		return getParent() != null;
		
	}
	
	public MCClass getParent() {
		
		return parent;
		
	}
	
	public void setParent(MCClass parent) {
		
		this.parent = parent;
		
	}
	
	public boolean isProtocol() {
		
		return isProtocol;
		
	}
	
	public void addProperty(MCProperty property) {
		
		if ( property.isConstant() ) {
			
			getConstants().add(property);
			
		} else {
			
			getProperties().add(property);
			
		}
		
		property.setClasss(this);
		
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
	
	public List<MCProperty> getInheritedProperties() {
		
		List<MCProperty> inherited = new ArrayList<MCProperty>();
		
		if ( hasParent() ) {
			
		 	inherited.addAll(getParent().getAllProperties());
			
		}
		
		return inherited;
		
	}
	
	public List<MCProperty> getInheritedOptionalProperties() {
		
		return filter(getInheritedProperties(), p -> p.getType().isOptional());
		
	}
	
	public List<MCProperty> getInheritedRequiredProperties() {
		
		return filter(getInheritedProperties(), p -> !p.getType().isOptional());
		
	}
	
	public List<MCProperty> getAllProperties() {
		
		List<MCProperty> all = new ArrayList<MCProperty>(getProperties());
		
		all.addAll(getInheritedProperties());
		
		return all;
		
	}
	

	public List<MCProperty> getAllOptionalProperties() {
		
		return filter(getAllProperties(), p -> p.getType().isOptional());
		
	}
	
	public List<MCProperty> getAllRequiredProperties() {
		
		return filter(getAllProperties(), p -> !p.getType().isOptional());
		
	}
	
	public boolean hasConstants() {
		
		return getConstants().size() > 0;
		
	}
	
	public List<MCProperty> getConstants() {
		
		return constants;
		
	}
	
	public Map<String, Object> getModel(Generator generator) {
		
		Map<String, Object> model = super.getModel(generator);
		
		if ( hasParent() ) {

			model.put("class_parent", generator.generateModel(getParent()));
			model.put("class_parent_literal", generator.generateTypeLiteral(getParent().getType()));
			
		}
		
		model.put("class_properties", properties);
		model.put("class_isProtocol", isProtocol());
		model.put("class_constants", MCProperty.getModel(generator, getConstants()));
		model.put("class_properties", MCProperty.getModel(generator, getProperties()));
		model.put("class_properties_valued",
			MCProperty.getModel(generator, ListUtils.filter(getProperties(), p -> generator.generatePropertyValue(p) != null))
		);
		model.put("class_properties_required", MCProperty.getModel(generator, getRequiredProperties()));
		model.put("class_properties_optional", MCProperty.getModel(generator, getOptionalProperties()));
		model.put("class_properties_inherited", MCProperty.getModel(generator, getInheritedProperties()));
		model.put("class_properties_inherited_required", MCProperty.getModel(generator, getInheritedRequiredProperties()));
		model.put("class_properties_inherited_optional", MCProperty.getModel(generator, getInheritedOptionalProperties()));
		model.put("class_properties_all", MCProperty.getModel(generator, getAllProperties()));
		model.put("class_properties_all_required", MCProperty.getModel(generator, getAllRequiredProperties()));
		model.put("class_properties_all_optional", MCProperty.getModel(generator, getAllOptionalProperties()));
		model.put("class_type", getType().getModel(generator));
		
		return model;
		
	}
	
	public String toString(int indent) {
		
		String t = StringUtils.repeat("\t", indent);
		
		String s = "";
		
		s += t + "Class(" + getName();
		
		if ( hasParent() ) {
			
			s += " : " + getParent();
			
		}
		
		s += "){\n";
		
		for ( MCProperty property : getProperties() ) {
			
			s += t + "\t" + property.toString(indent + 1) + "\n";
			
		}
		
		s += t + "}";
		
		return s;
		
	}
	
	public String toString() {
		
		return toString(0);
		
	}
	
	
	
	/* ===== Private Functions ===== */
	
	private List<MCProperty> filter(List<MCProperty> properties, Predicate<MCProperty> predicate) {
		
		return properties.stream().filter(predicate).collect(Collectors.toList());
		
	}
	
}

package mcconverter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import mcconverter.generators.Generator;

public class MCClass extends MCEntity {
	
	/* ===== Private Properties ===== */
	
	private MCType type;
	private MCClass parent;
	private boolean isProtocol;
	
	
	
	/* ===== Construction ===== */
	
	public MCClass(MCType type, String name, boolean isProtocol) {
		
		super ( type.getIdentifier(), name );
		
		this.type = type;
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
	
	
	public Map<String, Object> getModel(Generator generator) {
		
		Map<String, Object> model = super.getModel(generator);
		
		if ( hasParent() ) {

			model.put("class_parent", generator.generateModel(getParent()));
			model.put("class_parent_literal", generator.generateTypeLiteral(getParent().getType()));
			
		}
		
		model.put("class_isProtocol", isProtocol());
		model.put("class_type", getType().getModel(generator));
		
		model.put("class_properties_inherited", MCProperty.getModel(generator, getInheritedProperties()));
		model.put("class_properties_inherited_required", MCProperty.getModel(generator, getInheritedRequiredProperties()));
		model.put("class_properties_inherited_optional", MCProperty.getModel(generator, getInheritedOptionalProperties()));
		model.put("class_properties_all", MCProperty.getModel(generator, getAllProperties()));
		model.put("class_properties_all_required", MCProperty.getModel(generator, getAllRequiredProperties()));
		model.put("class_properties_all_optional", MCProperty.getModel(generator, getAllOptionalProperties()));
		
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
	
}

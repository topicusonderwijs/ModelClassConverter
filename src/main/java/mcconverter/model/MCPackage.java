package mcconverter.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mcconverter.generators.AbstractGenerator;

public class MCPackage implements MCModelable {
	
	private String name;
	private Map<String, MCEntity> entities;
	
	public MCPackage(String name) {
		
		this.name = name;
		entities = new HashMap<String, MCEntity>();
		
	}
	
	public String getName() {
		
		return name;
		
	}
	
	public Set<String> getEntityIdentifiers() {
		
		return entities.keySet();
		
	}
	
	public void addEntity(MCEntity entity) {
		
		entities.put(entity.getIdentifier(), entity);
		
	}
	
	public boolean hasEntity(String identifier) {
		
		return entities.containsKey(identifier);
		
	}
	
	public MCEntity getEntity(String identifier) {
		
		return entities.get(identifier);
		
	}
	
	public boolean hasClass(String identifier) {
		
		return getClass(identifier) != null;
		
	}
	
	public MCClass getClass(String identifier) {
		
		MCClass cls = null;
		
		MCEntity ent = getEntity(identifier);
		
		if ( ent instanceof MCClass ) {
			
			cls = (MCClass)ent;
			
		}
		
		return cls;
		
	}
	
	public boolean hasEnum(String identifier) {
		
		return getEnum(identifier) != null;
		
	}
	
	public MCEnum getEnum(String identifier) {
		
		MCEnum cls = null;
		
		MCEntity ent = getEntity(identifier);
		
		if ( ent instanceof MCEnum ) {
			
			cls = (MCEnum)ent;
			
		}
		
		return cls;
		
	}
	
	public Map<String, Object> getModel(AbstractGenerator generator) {
		
		List<Map<String, Object>> entities = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> classes = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> enums = new ArrayList<Map<String, Object>>();
		
		List<String> entityIdentifiers = new ArrayList<String>(getEntityIdentifiers());
		Collections.sort(entityIdentifiers);
		
		for ( String entityIdentifier : entityIdentifiers ) {
			
			MCEntity entity = getEntity(entityIdentifier);
			Map<String, Object> entityModel = entity.getModel(generator);
			
			if ( generator.validateModel(entity, entityModel) ) {
				
				entities.add(entityModel);
				
				if ( entity instanceof MCEnum ) {
					
					enums.add(entityModel);
					
				} else if ( entity instanceof MCClass ) {
					
					classes.add(entityModel);
					
				}
				
			}
			
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("package_name", getName());
		model.put("package_entities", entities);
		model.put("package_classes", classes);
		model.put("package_enums", enums);
		
		return model;
		
	}
	
	public String toString() {
		
		String i = "[";
		
		for ( MCEntity entity : entities.values() ) {
			
			i += entity.toString() + "\n";
			
		}
		
		i += "]";
		
		return i;
		
	}

}

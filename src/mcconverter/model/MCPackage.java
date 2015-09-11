package mcconverter.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MCPackage {
	
	private Map<String, MCEntity> entities;
	
	public MCPackage() {
		
		entities = new HashMap<String, MCEntity>();
		
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
	
	public MCClass getClass(String identifier) {
		
		MCClass cls = null;
		
		MCEntity ent = getEntity(identifier);
		
		if ( ent instanceof MCClass ) {
			
			cls = (MCClass)ent;
			
		}
		
		return cls;
		
	}
	
	public MCEnum getEnum(String identifier) {
		
		MCEnum cls = null;
		
		MCEntity ent = getEntity(identifier);
		
		if ( ent instanceof MCEnum ) {
			
			cls = (MCEnum)ent;
			
		}
		
		return cls;
		
	}

}

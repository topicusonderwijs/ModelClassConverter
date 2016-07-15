package mcconverter.generators;

import java.util.Map;

import com.google.common.base.CaseFormat;

import mcconverter.configuration.Configuration;
import mcconverter.configuration.CustomClass;
import mcconverter.configuration.CustomProperty;
import mcconverter.configuration.CustomType;
import mcconverter.model.*;

public abstract class AbstractGenerator extends Generator {
	
	public String generateEnumValueName(MCEnumValue value) {
		
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, replacePropertyName(value.getName()));
		
	}
	
	public boolean validatePackage(MCPackage pack) {
		
		//Insert custom classes
		for ( CustomClass c : getConfiguration().getCustomClasses() ) {
			
			if ( !pack.hasClass(c.getName()) && !c.isIgnored() && !c.hasRename() ) {
				
				pack.addEntity(c.toClass(pack));
				
			}
			
		}
		
		
		return true;
		
	}
	
	//TODO:
	// Configuration should be a little refined, using subclasses from MCType for instance.
	public boolean validateEntity(MCEntity entity) {
		
		boolean valid = true;

		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			CustomClass customClass = getConfiguration().getCustomClass(c.getName());
			
			if ( customClass == null ) {
				
				customClass = getConfiguration().getCustomClass(c.getIdentifier());
				
			}
			
			valid = !getConfiguration().hasIgnoredClass(c.getName());
			
			if ( valid ) {
				
				//Apply global configuration of properties
				for ( MCProperty property : c.getProperties() ) {
					
					if ( getConfiguration().hasCustomProperty(property.getName()) ) {
						
						CustomProperty customProperty = getConfiguration().getCustomProperty(property.getName());
						
						property.setInitialized(customProperty.isInitialized());
						
						if ( property.hasValue() ) {
							
							property.setValue(customProperty.getValue().toValue());
							
						}
						
					}
					
					if ( getConfiguration().hasIgnoredProperty(property.getName()) ) {
						
						c.removeProperty(property);
						
					}
					
				}
				
				//Apply custom entities
				if ( customClass != null && customClass.hasProperties() ) {
					
					for ( CustomProperty customProperty : customClass.getProperties() ) {
						
						String customPropertyName = customProperty.getName();
						
						if ( c.hasProperty(customPropertyName) ) {
							
							MCProperty p = c.getProperty(customPropertyName);
							
							//Remove if property is ignored
							if ( customProperty.isIgnored() ) {
								
								c.removeProperty(p);
								
							}
							
							//Set custom value if set
							if ( customProperty.hasValue() ) {
								
								p.setValue(customProperty.getValue().toValue());
								
							}
							
							//Add type if custom type is set
							if ( customProperty.hasType() ) {
								
								p.setType(customProperty.getType().toType());
								
							}
							
						} else {
							
							CustomType customPropertyType = customProperty.getType();
							
							if ( customPropertyType != null ) {
								
								MCType customType = customPropertyType.toType();
								MCPropertyValue customValue = customProperty.hasValue() ? customProperty.getValue().toValue() : null;
								
								c.addProperty(
									new MCProperty(
										c.getIdentifier() + "." + customPropertyName,
										customPropertyName,
										customProperty.getKey(),
										customType,
										customValue
									)
								);
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
		return valid;
		
	}
	
	public boolean validateModel(MCEntity entity, Map<String, Object> model) {

		boolean valid = true;
		
		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			
			if ( getConfiguration().getIgnoreProtocols() ) {
				
				valid = !c.isProtocol();
				model.remove("class_protocols");
				
			}
			
		}
		
		//Find descriptor of entity
		String descriptor = entity.getIdentifier();
		
		for( String prefix : Configuration.current().getPackages() ) {
			
			if ( descriptor.startsWith(prefix) && descriptor.length() > prefix.length() ) {
				
				descriptor = descriptor.substring(prefix.length() + 1);
				break;
				
			}
			
		}
		
		model.put("entity_descriptor", descriptor);
		
		return valid;
		
	}
	
	protected String replacePropertyName(String name) {
		
		for ( CustomProperty property : getConfiguration().getCustomProperties() ) {
			
			if ( property.hasRename() && property.getName().toLowerCase().equals(name.toLowerCase()) ) {
				
				name = property.getRename();
				
			}
			
		}
		
		return name;
		
	}
	
}

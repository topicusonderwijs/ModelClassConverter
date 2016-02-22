package mcconverter.generators;

import java.util.Map;

import com.google.common.base.CaseFormat;

import mcconverter.configuration.CustomClass;
import mcconverter.configuration.CustomProperty;
import mcconverter.configuration.CustomType;
import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCPackage;
import mcconverter.model.MCType;
import mcconverter.model.MCEnum.MCEnumValue;
import mcconverter.model.MCProperty;

public abstract class Generator extends AbstractGenerator {
	
	public String generateEnumValueName(MCEnumValue value) {
		
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, replacePropertyName(value.getName()));
		
	}
	
	public boolean validatePackage(MCPackage pack) {
		
		//Insert custom classes
		for ( CustomClass c : getConfiguration().getCustomClasses().values() ) {
			
			if ( !pack.hasClass(c.getName()) && !c.isIgnored() ) {
				
				pack.addEntity(c.toClass());
				
			}
			
		}
		
		
		return true;
		
	}
	
	public boolean validateEntity(MCEntity entity) {
		
		boolean valid = true;

		if ( entity instanceof MCClass ) {
			
			MCClass c = (MCClass)entity;
			CustomClass customClass = getConfiguration().getCustomClass(c.getName());
			
			valid = !getConfiguration().hasIgnoredClass(c.getName());
			
			if ( valid ) {
				
				//Remove properties that should be ignored
				for ( MCProperty property : c.getProperties() ) {
					
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
							
							if ( customProperty.isIgnored() ) {
								
								c.removeProperty(p);
								
							}
							
						} else {
							

							CustomType customPropertyType = customProperty.getType();
							
							if ( customPropertyType != null ) {
								
								MCType customType = customPropertyType.toType();
								
								c.addProperty(
									new MCProperty(
										c.getIdentifier() + "." + customPropertyName,
										customPropertyName,
										customProperty.getKey(),
										customType
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
		
		return valid;
		
	}
	
	protected String replacePropertyName(String name) {
		
		//TODO: Should be in configuration
		if ( name.equals("id") ) {
			
			name = "objectId";
			
		} else if ( name.toLowerCase().equals("self") ) {
			
			name = "zelf";
			
		}
		
		return name;
		
	}
	
}

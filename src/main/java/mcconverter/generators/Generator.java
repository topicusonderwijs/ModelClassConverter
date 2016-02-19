package mcconverter.generators;

import java.util.Map;

import com.google.common.base.CaseFormat;

import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCEnum.MCEnumValue;

public abstract class Generator extends AbstractGenerator {
	
	public String generateEnumValueName(MCEnumValue value) {
		
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, replacePropertyName(value.getName()));
		
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

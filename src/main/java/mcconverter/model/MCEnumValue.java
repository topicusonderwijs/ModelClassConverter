package mcconverter.model;

import java.util.Map;

import mcconverter.generators.Generator;

import org.apache.commons.lang3.StringUtils;

public class MCEnumValue extends MCEntity {
	
	/* ===== Private Properties ===== */
	
	private MCType rawType;
	private String rawValue;
	
	
	
	/* ===== Construction ===== */
	
	public MCEnumValue(String identifier, String name, MCType rawType, String rawValue) {
		
		super ( identifier, name );
		
		this.rawType = rawType;
		this.rawValue = rawValue;
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public MCType getRawType() {
		
		return rawType;
		
	}
	
	public boolean hasRawType() {
		
		return getRawType() != null;
		
	}
	
	public String getRawValue() {
		
		return rawValue;
		
	}
	
	public boolean hasRawValue() {
		
		return getRawValue() != null;
		
	}
	
	public Map<String, Object> getModel(Generator generator) {
		
		Map<String, Object> model = super.getModel(generator);
		
		model.put("enum_value_name", generator.generateEnumValueName(this));
		model.put("enum_value_rawName", getName());
		model.put("enum_value_rawType", getRawType());
		model.put("enum_value_rawValue", getRawValue());
		
		return model;
		
	}
	
	public String toString(int indent) {
		
		String s = StringUtils.repeat("\t", indent) + "Value(" + getName();
		
		if ( hasRawType() ) {
			
			s += " : " + getRawType().toString();
			
		}
		
		s += ")";
		
		if ( hasRawValue() ) {
			
			s += " = " + getRawValue();
			
		}
		
		return s;
		
	}
	
	public String toString() {
		
		return toString(0);
		
	}
	
}

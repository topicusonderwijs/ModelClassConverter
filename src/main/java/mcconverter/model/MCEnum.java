package mcconverter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mcconverter.generators.AbstractGenerator;
import mcconverter.main.Main;

import org.apache.commons.lang.StringUtils;

public class MCEnum extends MCEntity {
	
	public class MCEnumValue extends MCEntity {
		
		private MCType rawType;
		private String rawValue;
		
		public MCEnumValue(String identifier, String name, MCType rawType, String rawValue) {
			
			super ( identifier, name );
			
			this.rawType = rawType;
			this.rawValue = rawValue;
			
		}
		
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
		
		public Map<String, Object> getModel(AbstractGenerator generator) {
			
			Map<String, Object> model = super.getModel(generator);
			
			String valueName = generator.generateEnumValueName(this);
			
			model.put("enum_value_name", valueName);
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
	
	private List<MCEnumValue> values;
	
	public MCEnum(String identifier, String name) {
		
		super( identifier, name );
		
		values = new ArrayList<MCEnumValue>();
		
	}
	
	public void addValue(String name) {
		
		addValue( name, null, null );
		
	}
	
	public void addValue(String name, MCType rawType, String rawValue) {
		
		values.add( new MCEnumValue(getIdentifier() + "." + name, name, rawType, rawValue) );
		
	}
	
	public List<MCEnumValue> getValues() {
		
		return values;
		
	}
	
	public Map<String, Object> getModel(AbstractGenerator generator) {
		
		Map<String, Object> model = super.getModel(generator);
		
		List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
		
		for ( MCEnumValue value : getValues() ) {
			
			values.add(value.getModel(generator));
			
		}
		
		model.put("enum_values", values);
		
		return model;
		
	}
	
	public String toString(int indent) {
		
		String t = StringUtils.repeat("\t", indent);
		
		String s = "";
		
		s += t + "Enum(" + getName() + "){\n";
		
		for ( MCEnumValue value : getValues() ) {
			
			s += t + "\t" + value.toString(indent + 1) + "\n";
			
		}
		
		s += t + "}";
		
		return s;
		
	}
	
	public String toString() {
		
		return toString(0);
		
	}
	
	
}

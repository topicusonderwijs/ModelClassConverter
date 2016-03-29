package mcconverter.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mcconverter.generators.Generator;

import org.apache.commons.lang3.StringUtils;

public class MCEnum extends MCEntity {
	
	/* ===== Private Properties ===== */
	
	private List<MCEnumValue> values;
	
	
	
	/* ===== Construction ===== */
	
	public MCEnum(String identifier, String name) {
		
		super( identifier, name );
		
		values = new ArrayList<MCEnumValue>();
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public void addValue(String name) {
		
		addValue( name, null, null );
		
	}
	
	public void addValue(String name, MCType rawType, String rawValue) {
		
		values.add( new MCEnumValue(getIdentifier() + "." + name, name, rawType, rawValue) );
		
	}
	
	public List<MCEnumValue> getValues() {
		
		return values;
		
	}
	
	public boolean hasValues() {
		
		return getValues() != null && !getValues().isEmpty();
		
	}
	
	public Set<MCType> getRawValueTypes() {
		
		Set<MCType> rawValueTypes = new HashSet<>();
		
		for ( MCEnumValue value : getValues() ) {
			
			if ( value.hasRawType() ) {
				
				rawValueTypes.add(value.getRawType());
				
			}
			
		}
		
		return rawValueTypes;
		
	}
	
	public Map<String, Object> getModel(Generator generator) {
		
		Map<String, Object> model = super.getModel(generator);

		List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
		
		for ( MCEnumValue value : getValues() ) {
			
			values.add(value.getModel(generator));
			
		}
		
		
		List<Map<String, Object>> rawValueTypes = new ArrayList<Map<String, Object>>();
		
		for ( MCType rawValueType : getRawValueTypes() ) {
			
			rawValueTypes.add(rawValueType.getModel(generator));
			
		}
		
		model.put("enum_rawValueTypes", rawValueTypes);
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

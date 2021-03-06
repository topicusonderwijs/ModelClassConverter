package mcconverter.model;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

public enum MCNativeType {
	
	/* ===== Values ===== */
	
	NonNative,
	
	Object,
	
	Boolean,
	Integer,
	Long,
	BigInteger,
	Double,
	Float,
	BigDecimal,
	String,
	List,
	Set,
	URI,
	Map,
	
	/**
	 * A native type describing a date and a time.
	 */
	DateTime,
	
	/**
	 * A native type describing only a time, possibly without a specific timezone.
	 */
	LocalTime,
	
	/**
	 * A native type describing only a date, possibly without a specific timezone.
	 */
	LocalDate,
	
	/**
	 * A native type describing both a date and a time, possibly without a specific timezone.
	 */
	LocalDateTime;
	
	
	/* ===== Mapping ===== */
	
	private static final Map<Type, MCNativeType> nativeTypeMap;
	
	static {
		
		Map<Type, MCNativeType> m = new HashMap<Type, MCNativeType>();
		
		m.put(Object.class, MCNativeType.Object);
		m.put(Boolean.class, MCNativeType.Boolean);
		m.put(boolean.class, MCNativeType.Boolean);
		m.put(Integer.class, MCNativeType.Integer);
		m.put(int.class, MCNativeType.Integer);
		m.put(Long.class, MCNativeType.Long);
		m.put(long.class, MCNativeType.Long);
		m.put(BigInteger.class, MCNativeType.BigInteger);
		m.put(Double.class, MCNativeType.Double);
		m.put(double.class, MCNativeType.Double);
		m.put(Float.class, MCNativeType.Float);
		m.put(float.class, MCNativeType.Float);
		m.put(BigDecimal.class, MCNativeType.BigDecimal);
		m.put(String.class, MCNativeType.String);
		m.put(List.class, MCNativeType.List);
		m.put(Set.class, MCNativeType.Set);
		m.put(URI.class, MCNativeType.URI);
		m.put(Map.class, MCNativeType.Map);
		m.put(Date.class, MCNativeType.DateTime);
		m.put(LocalTime.class, MCNativeType.LocalTime);
		m.put(LocalDate.class, MCNativeType.LocalDate);
		m.put(LocalDateTime.class, MCNativeType.LocalDateTime);
		m.put(java.sql.Time.class, MCNativeType.LocalTime);
		m.put(org.joda.time.DateTime.class, MCNativeType.DateTime);
		m.put(org.joda.time.LocalTime.class, MCNativeType.LocalTime);
		m.put(org.joda.time.LocalDate.class, MCNativeType.LocalDate);
		m.put(org.joda.time.LocalDateTime.class, MCNativeType.LocalDateTime);
		
		nativeTypeMap = m;
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public static MCNativeType fromName(String name) {
		
		MCNativeType type = MCNativeType.NonNative;
		
		if ( name != null ) {
			
			try {
				
				type = MCNativeType.valueOf(name);
				
			} catch (IllegalArgumentException e) {}
			
			if ( type == MCNativeType.NonNative ) {

				try {
					
					type = fromType(Class.forName(name));
					
				} catch (ClassNotFoundException e) {}
				
			}
			
		}
		
		return type;
		
	}
	
	public static MCNativeType fromType(Type t) {
		
		MCNativeType type = MCNativeType.NonNative;
		Class<?> c = null;
		
		if ( t instanceof Class ) {
			
			c = (Class<?>)t;
			
		}
		
		if ( c != null && c.isArray() ) {
			
			type = MCNativeType.List;
			
		} else if ( nativeTypeMap.containsKey(t) ) {
			
			type = nativeTypeMap.get(t);
			
		}
		
		return type;
		
	}
	
	public String getIdentifier() {
		
		return name();
		
	}
	
	/**
	 * Creates a type that represents the native type.
	 */
	public MCType toType() {
		
		return new MCType(this);
		
	}
	
	/**
	 * Creates a type that represents the native type that can potentially be optional.
	 */
	public MCType toType(boolean optional) {
		
		return new MCType(this, optional);
		
	}
	
	
	
	
	
}
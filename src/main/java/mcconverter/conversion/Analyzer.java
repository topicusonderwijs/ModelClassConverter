package mcconverter.conversion;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.Modifier;
import mcconverter.configuration.Configuration;
import mcconverter.main.Main;
import mcconverter.model.*;

import org.apache.commons.lang.StringUtils;
import org.reflections.ReflectionUtils;

public class Analyzer {
	
	/* ===== Constants ===== */
	
	private static final String GenericsPattern = "(<.*>)";
	
	
	
	/* ===== Private Properties ===== */
	
	private Map<String, Class<?>> classes;
	private Map<String, MCEntity> entities;
	
	
	
	/* ===== Public Functions ===== */
	
	public MCPackage analyze(String name, Map< String, Class<?> > classes) {
		
		this.classes = classes;
		entities = new HashMap<String, MCEntity>();
		
		for ( Class<?> c : classes.values() ) {
			
			analyze( c, 0 );
			
		}
		
		MCPackage pack = new MCPackage(name);
		
		for ( MCEntity entity : entities.values() ) {
			
			pack.addEntity(entity);
			
		}
		
		return pack;
		
	}
	
	
	
	/* ===== Private Functions ===== */
	
	private static final Configuration getConfiguration() {
		
		return Configuration.current();
		
	}
	
	private MCEntity analyze(Class<?> c, int indent) {
		
		MCEntity dataEntity = null;
		
		if ( c != null ) {
			
			String identifier = c.getCanonicalName();
			String name = c.getSimpleName();
			
			if ( entities.containsKey(identifier) ) {
				
				dataEntity = entities.get(identifier);
				
			} else if ( classes.containsKey(identifier) && !StringUtils.isEmpty(name) ) {
					
				MCEnum dataEnum = analyzeEnum(c, identifier, name);
				
				if ( dataEnum != null ) {
					
					dataEntity = dataEnum;
					
				} else {
					
					dataEntity = analyzeClass(c, identifier, name, indent);
					
				}
				
				if ( dataEntity != null ) {
					
					entities.put(dataEntity.getIdentifier(), dataEntity);
					Main.entry("Analyzed", dataEntity.getIdentifier(), indent);
					
				}
				
			}
			
		}
		
		return dataEntity;
		
	}
	
	private MCEnum analyzeEnum(Class<?> c, String identifier, String name) {
		
		MCEnum dataEnum = null;
		
		Object[] enumValues = c.getEnumConstants();
		
		if ( enumValues != null ) {
			
			dataEnum = new MCEnum( identifier, name );
			
			for ( Object enumValue : enumValues ) {
				
				if ( enumValue instanceof Enum ) {
					
					Enum<?> eValue = (Enum<?>)enumValue;
					
					dataEnum.addValue(eValue.name(), MCNativeType.String.toType(), eValue.toString());
					
				}
				
			}
			
		}
		
		return dataEnum;
		
	}
	
	private MCClass analyzeClass(Class<?> c, String identifier, String name, int indent) {
		
		//Determine parameters
		List<MCTypeParameter> parameters = new ArrayList<MCTypeParameter>();
		
		for( TypeVariable<?> generic : c.getTypeParameters() ) {
			
			parameters.add(analyzeTypeParameter(generic, 0).get(0));
			
		}
		
		MCType dataType = new MCType(identifier, parameters);
		MCClass dataClass = new MCClass( dataType, name, c.isInterface() );
		
		//Determine parent
		MCEntity parentEntity = analyze(c.getSuperclass(), indent + 1);
		
		if ( parentEntity != null && parentEntity instanceof MCClass ) {
			
			dataClass.setParent(((MCClass)parentEntity).getType());
			
		}
		
		//Determine protocols
		if ( !getConfiguration().getIgnoreProtocols() ) {

			for ( Class<?> protocol : c.getInterfaces() ) {
				
				MCEntity protocolEntity = analyze(protocol, indent + 1);
				
				if ( protocolEntity != null && protocolEntity instanceof MCClass ) {
					
					dataClass.getType().addProtocol(((MCClass)protocolEntity).getType());
					
				}
				
			}
			
		}
		
		//Analyze properties
		@SuppressWarnings("unchecked")
		Set<Field> properties = ReflectionUtils.getFields(c);
		
		for ( Field property : properties ) {
			
			String propertyName = property.getName();
			
			if ( !getConfiguration().hasIgnoredProperty(propertyName) ) {

				String propertyKey = propertyName;
				boolean propertyTypeNonOptional = false;
				
				Annotation[] annotations = property.getAnnotations();
				
				for ( Annotation annotation : annotations ) {
					
					propertyTypeNonOptional = propertyTypeNonOptional || annotation.toString().contains("required=true");
					
				}
				
				int modifiers = property.getModifiers();
				
				MCType propertyType = analyzeType(property.getGenericType(), !propertyTypeNonOptional, 0);
				String propertyValue = null;
				boolean propertyStatic = Modifier.isStatic(modifiers);
				boolean propertyConstant = Modifier.isFinal(modifiers);
				
				if ( propertyStatic ) {
					
					//Try to get value of static property
					if ( propertyType.isNativeType(MCNativeType.String, MCNativeType.Integer) ) {
						
						try {
							propertyValue = property.get(null).toString();
						} catch (Exception e) {}
						
					}
					
				}
				
				dataClass.addProperty(new MCProperty(
					dataClass.getIdentifier() + "." + propertyName,
					propertyName,
					propertyKey,
					propertyType,
					propertyValue,
					propertyStatic,
					propertyConstant
				));
				
			}
			
		}
		
		return dataClass;
		
	}
	
	private MCType analyzeType(Type type) {
		
		return analyzeType(type, true, 1);
		
	}
	
	private MCType analyzeType(Type type, boolean optional, int depth) {
		
		MCType t = null;
		
		//Determine parameters
		List<MCTypeParameter> parameters = analyzeTypeParameter(type, depth + 1);
		
		//Determine if native type
		MCNativeType nativeType;

		//Strips away the generics
		String baseType = type.getTypeName().replaceAll(GenericsPattern, "");
		
		if ( !baseType.equals(type.getTypeName()) ) {
			
			nativeType = MCNativeType.fromName(baseType);
			
		} else {
			
			nativeType = MCNativeType.fromType(type);
			
		}
		
		if ( nativeType != MCNativeType.NonNative ) {
			
			t = new MCType(nativeType, parameters, optional);
			
		} else {
			
			t = new MCType(baseType, parameters, optional);
			
		}
		
		return t;
		
	}
	
	/**
	 * Determines the type parameters for the given type.
	 */
	private List<MCTypeParameter> analyzeTypeParameter(Type type, int depth) {
		
		List<MCTypeParameter> parameters = new ArrayList<MCTypeParameter>();
		
		if ( type instanceof TypeVariable) {
			
			TypeVariable<?> aType = ((TypeVariable<?>)type);
			Type[] typeBounds = aType.getBounds();
			
			if ( typeBounds.length > 0 ) {
				
				parameters.add(new MCTypeParameter(aType.getName(), analyzeType(typeBounds[0]), depth));
				
			}
			
		} else if ( type instanceof ParameterizedType ) {
			
			Type[] aTypes = ((ParameterizedType)type).getActualTypeArguments();
			
			for ( Type aType : aTypes ) {
				
				String name = null;
				
				if ( aType instanceof TypeVariable ) {
					
					name = ((TypeVariable<?>)aType).getName();
					
				}
				
				parameters.add(new MCTypeParameter(name, analyzeType(aType), depth));
				
			}
			
		}
		
		return parameters;
		
	}
	
	
}

package mcconverter.conversion;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mcconverter.main.Main;
import mcconverter.model.*;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.reflections.ReflectionUtils;

public class Analyzer {
	
	/* ===== Constants ===== */
	
	private static final String GenericsPattern = "(<.*>)";
	
	
	/* ===== Private Properties ===== */
	
	private Map<String, Class<?>> classes;
	private Map<String, MCEntity> entities;
	
	
	
	/* ===== Public Functions ===== */
	
	public MCPackage analyze( String name, Map< String, Class<?> > classes ) {
		
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
	
	private MCEntity analyze ( Class<?> c, int indent ) {
		
		MCEntity dataEntity = null;
		
		if ( c != null ) {
			
			String identifier = c.getCanonicalName();
			String name = c.getSimpleName();
			
			if ( !entities.containsKey(identifier) && classes.containsKey(identifier) ) {
				
				if ( !StringUtils.isEmpty(name) ) {
					
					Object[] enumValues = c.getEnumConstants();
					
					if ( enumValues != null ) {
						
						MCEnum dataEnum = new MCEnum( identifier, name );
						
						for ( Object enumValue : enumValues ) {
							
							if ( enumValue instanceof Enum ) {
								
								Enum<?> eValue = (Enum<?>)enumValue;
								
								dataEnum.addValue(eValue.name(), MCNativeType.String.toType(), eValue.toString());
								
							}
							
						}
						
						dataEntity = dataEnum;
						
					} else {
						
						MCClass dataClass = null;
						
						
						// TODO: Filtering on super class and interfaces should occur here
						// The loader already loads all classes necessary, only the top level classes should be passed to the analyzer!
						// This means functionality of the loader should be moved to the analyzer.
						// The analyzer should then get the super class and interfaces through reflection!
						// This also applies for the generics realization.
						
						
						MCEntity superClass = analyze(c.getSuperclass(), indent + 1);
						
						if ( superClass != null ) {
							
							MCType parent = new MCType( superClass.getIdentifier(), false );
							
							dataClass = new MCClass( identifier, name, parent );
							
						} else {
							
							dataClass = new MCClass( identifier, name );
							
						}
						
						@SuppressWarnings("unchecked")
						Set<Field> properties = ReflectionUtils.getFields(c);
						
						for ( Field property : properties ) {
							
							String propertyName = property.getName();
							
							if ( !ArrayUtils.contains(Main.IGNORED_PROPERTIES, propertyName) ) {
								
								MCType propertyType = analyzeType(property.getGenericType());
								
								if ( propertyType != null ) {
									
									dataClass.addProperty(propertyName, propertyType);
									
								}
								
							}
							
						}
						
						dataEntity = dataClass;
						
					}
					
				}
				
				if ( dataEntity != null ) {
					
					entities.put(dataEntity.getIdentifier(), dataEntity);
					
					System.out.println(StringUtils.repeat("\t", indent) + "-> Analyzed: " + dataEntity.getIdentifier());
					
				}
				
			}
			
		}
		
		return dataEntity;
		
	}
	
	private MCType analyzeType(Type type) {
		
		MCType t = null;
		boolean optional = true;
		
		//Determine if optional
		@SuppressWarnings("unchecked")
		Set<Annotation> annotations = ReflectionUtils.getAnnotations(type.getClass());
		
		for ( Annotation annotation : annotations ) {
			
			optional = optional || annotation.toString().contains("required=true");
			
		}
		
		//Determine parameters
		List<MCType> parameters = new ArrayList<MCType>();
		
		if ( type instanceof ParameterizedType ) {
			
			Type[] aTypes = ((ParameterizedType)type).getActualTypeArguments();
			
			for ( Type aType : aTypes ) {
				
				MCType parameter = analyzeType(aType);
				
				if ( parameter != null ) {
					
					parameters.add(parameter);
					
				}
				
			}
			
		}
		
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
	
	
}

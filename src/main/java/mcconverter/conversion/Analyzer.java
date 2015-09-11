package mcconverter.conversion;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mcconverter.main.Main;
import mcconverter.model.*;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.reflections.ReflectionUtils;

public class Analyzer {
	
	/* ===== Private Properties ===== */
	
	private Map<String, Class<?>> classes;
	private Map<String, MCEntity> entities;
	
	
	
	/* ===== Public Functions ===== */
	
	public MCPackage analyze( Map< String, Class<?> > classes ) {
		
		this.classes = classes;
		entities = new HashMap<String, MCEntity>();
		
		for ( Class<?> c : classes.values() ) {
			
			analyze( c, 0 );
			
		}
		
		MCPackage pack = new MCPackage();
		
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
							
							dataEnum.addValue(enumValue.toString());
							
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
								
								MCType propertyType = analyzeType(property.getType());
								
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
	
	private MCType analyzeType(Class<?> classType) {
		
		MCType t;
		boolean optional = true;
		
		//Determine if optional
		@SuppressWarnings("unchecked")
		Set<Annotation> annotations = ReflectionUtils.getAnnotations(classType);
		
		for ( Annotation annotation : annotations ) {
			
			optional = optional || annotation.toString().contains("required=true");
			
		}
		
		
		MCNativeType nativeType = MCNativeType.fromType(classType);
		/*List<MCType> parameters = new ArrayList<MCType>();
		
		TypeVariable<?>[] parameterTypes = classType.getTypeParameters();
		
		for ( TypeVariable<?> parameterType : parameterTypes ) {
			
			Object generic = parameterType.getGenericDeclaration();
			
			if ( generic instanceof Class ) {
				
				Class<?> genericClass = (Class<?>)generic;
				
				genericClass.get
				
				System.out.println("Generic: " + genericClass);
				
				MCType p = analyzeType(genericClass);
				
				if ( p != null ) {
					
					parameters.add(p);
					
				}
				
			}
			
		}
		*/
		
		if ( nativeType != MCNativeType.NonNative ) {
			
			t = new MCType(nativeType, optional);
			
		} else {
			
			t = new MCType(classType.getName(), optional);
			
		}
		
		
		//System.out.println(t);
		
		
		return t;
		
	}
	
	
}

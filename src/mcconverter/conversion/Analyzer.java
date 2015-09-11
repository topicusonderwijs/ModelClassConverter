package mcconverter.conversion;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mcconverter.model.*;

import org.apache.commons.lang.ArrayUtils;
import org.reflections.ReflectionUtils;
import org.sonatype.aether.util.StringUtils;

public class Analyzer {
	
	public static final String[] IGNORED_PROPERTIES = { "serialVersionUID" };
	
	private Map<String, MCEntity> map;
	
	public MCPackage analyze( List< Class<?> > classes ) {
		
		map = new HashMap<String, MCEntity>();
		
		for ( Class<?> c : classes ) {
			
			analyze( c );
			
		}
		
		MCPackage pack = new MCPackage();
		
		for ( MCEntity entity : map.values() ) {
			
			pack.addEntity(entity);
			
		}
		
		return pack;
		
	}
	
	private void analyze ( Class<?> cls ) {
		

		String identifier = cls.getCanonicalName();
		String name = cls.getSimpleName();
		
		if ( !map.containsKey(identifier) ) {
			
			MCEntity dataEntity = null;
			
			if ( !StringUtils.isEmpty(name) ) {
				
				Object[] enumValues = cls.getEnumConstants();
				
				if ( enumValues != null ) {
					
					MCEnum dataEnum = new MCEnum( identifier, name );
					
					for ( Object enumValue : enumValues ) {
						
						dataEnum.addValue(enumValue.toString());
						
					}
					
					dataEntity = dataEnum;
					
				} else {
					
					MCClass dataClass = null;
					
					Class<?> superClass = cls.getSuperclass();
					
					if ( superClass != null ) {
						
						analyze(superClass);
						
						MCType parent = new MCType( superClass.getName(), false );
						
						dataClass = new MCClass( identifier, name, parent );
						
					} else {
						
						dataClass = new MCClass( identifier, name );
						
					}
					
					@SuppressWarnings("unchecked")
					Set<Field> properties = ReflectionUtils.getFields(cls);
					
					for ( Field property : properties ) {
						
						String propertyName = property.getName();
						
						if ( !ArrayUtils.contains(IGNORED_PROPERTIES, propertyName) ) {
							
							MCType propertyType = analyzeType(property.getType());
							
							if ( propertyType != null ) {
								
								dataClass.addProperty(propertyName, propertyType);
								
							}
							
						}
						
					}
					
					if ( !dataClass.hasProperties() ) {
						
						dataClass = null;
						
					} else {
						
						dataEntity = dataClass;
						
					}
					
				}
				
			}
			
			if ( dataEntity != null ) {
				
				map.put(dataEntity.getIdentifier(), dataEntity);
				
			}
			
		}
		
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
		
		
		
		return t;
		
	}
	
	
}

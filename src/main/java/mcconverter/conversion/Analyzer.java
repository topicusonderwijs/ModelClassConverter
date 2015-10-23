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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.*;

import mcconverter.main.Main;
import mcconverter.model.*;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.reflections.ReflectionUtils;

public class Analyzer {
	
	/* ===== Constants ===== */
	
	private static final String GenericsPattern = "(<.*>)";
	private static final Pattern PropertyKeyPattern = Pattern.compile("(property=)(.*)(,.*)");
	
	
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
			
			if ( !entities.containsKey(identifier) && classes.containsKey(identifier) && !StringUtils.isEmpty(name) ) {
					
				MCEnum dataEnum = analyzeEnum(c, identifier, name);
				
				if ( dataEnum != null ) {
					
					dataEntity = dataEnum;
					
				} else {
					
					dataEntity = analyzeClass(c, identifier, name, indent);
					
				}
				
				if ( dataEntity != null ) {
					
					entities.put(dataEntity.getIdentifier(), dataEntity);
					
					System.out.println(StringUtils.repeat("\t", indent) + "-> Analyzed: " + dataEntity.getIdentifier());
					
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
		
		MCClass dataClass;
		
		// TODO: Filtering on super class and interfaces should occur here
		// The loader already loads all classes necessary, only the top level classes should be passed to the analyzer!
		// This means functionality of the loader should be moved to the analyzer.
		// The analyzer should then get the super class and interfaces through reflection!
		// This also applies for the generics realization.
		
		//Determine parameters
		List<MCTypeParameter> parameters = new ArrayList<MCTypeParameter>();
		
		for( TypeVariable<?> generic : c.getTypeParameters() ) {
			
			parameters.add(analyzeTypeParameter(generic).get(0));
			
		}
		
		MCType dataType = new MCType(identifier, parameters);
		
		//Determine super class
		MCEntity superEntity = analyze(c.getSuperclass(), indent + 1);
		
		if ( superEntity != null && superEntity instanceof MCClass ) {
			
			dataClass = new MCClass( dataType, name, ((MCClass)superEntity).getType() );
			
		} else {
			
			dataClass = new MCClass( dataType, name );
			
		}
		
		
		//Analyze properties
		@SuppressWarnings("unchecked")
		Set<Field> properties = ReflectionUtils.getFields(c);
		
		for ( Field property : properties ) {
			
			String propertyName = property.getName();
			
			if ( !ArrayUtils.contains(Main.IGNORED_PROPERTIES, propertyName) ) {
				
				MCType propertyType = analyzeType(property.getGenericType());
				String propertyKey = propertyName;
				
				for ( Annotation annotation : property.getAnnotations() ) {
					
					//TODO: Extract property key
					
					
					if ( annotation.toString().contains("property")) {
						
						System.err.println("Match: " + annotation.toString());
					}
					
					Matcher matcher = PropertyKeyPattern.matcher(annotation.toString());
					if ( matcher.find() ) {
						propertyKey = matcher.group(2);
					}
					
				}
				
				dataClass.addProperty(new MCProperty(
					dataClass.getIdentifier() + "." + propertyName,
					propertyName,
					propertyKey,
					propertyType
				));
				
			}
			
		}
		
		return dataClass;
		
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
		List<MCTypeParameter> parameters = analyzeTypeParameter(type);
		
		
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
	 * TODO: Should be split into two different functions.
	 */
	private List<MCTypeParameter> analyzeTypeParameter(Type type) {
		
		List<MCTypeParameter> parameters = new ArrayList<MCTypeParameter>();
		
		if ( type instanceof TypeVariable) {
			
			TypeVariable<?> aType = ((TypeVariable<?>)type);
			Type[] typeBounds = aType.getBounds();
			
			if ( typeBounds.length > 0 ) {
				
				parameters.add(new MCTypeParameter(aType.getName(), analyzeType(typeBounds[0])));
				
			}
			
		} else if ( type instanceof ParameterizedType ) {
			
			Type[] aTypes = ((ParameterizedType)type).getActualTypeArguments();
			
			for ( Type aType : aTypes ) {
				
				String name = null;
				
				if ( aType instanceof TypeVariable ) {
					
					name = ((TypeVariable<?>)aType).getName();
					
				}
				
				parameters.add(new MCTypeParameter(name, analyzeType(aType)));
				
			}
			
		}
		
		return parameters;
		
	}
	
	
}

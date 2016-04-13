package mcconverter.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mcconverter.model.MCClass;
import mcconverter.model.MCEntity;
import mcconverter.model.MCProperty;

public final class Configuration {
		
	/* ===== Private Properties ===== */
	
	private static Configuration current;
	
	private String modelVersion;
	private String productName;
	private String productPackage;
	private File templateLocation;
	private File outputLocation;
	private String generatorName;
	private List<String> dependencies;
	private List<String> packages;
	private List<String> deepestSuperClasses;
	private boolean ignoreProtocols;
	private List<CustomClass> customClasses;
	private List<CustomProperty> customProperties;
	
	
	
	/* ===== Construction ===== */
	
	private Configuration() {
		
		setModelVersion("");
		setProductName("");
		setOutputLocation("");
		setGeneratorName("");
		setIgnoreProtocols(false);
		dependencies = new ArrayList<String>();
		packages = new ArrayList<String>();
		deepestSuperClasses = new ArrayList<String>();
		customClasses = new ArrayList<CustomClass>();
		customProperties = new ArrayList<CustomProperty>();
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	/**
	 * Constructs a new configuration using default properties.
	 */
	public static Configuration defaultConfiguration() {
		
		Configuration configuration = new Configuration();
		
		configuration.setTemplateLocation("templates/");
		configuration.addIgnoredClasses("package-info", "java.lang.Object", "java.lang.Enum");
		configuration.addIgnoredProperty("serialVersionUID");
		
		return configuration;
		
	}
	
	/**
	 * Sets the configuration to be used to the given configuration.
	 */
	public static void setCurrent(Configuration configuration) {
		
		current = configuration;
		
	}
	
	/**
	 * Returns the currently used configuration.
	 */
	public static Configuration current() {
		
		return current;
		
	}
	
	/**
	 * The name of the product that will be using the generated model classes.
	 */
	public String getProductName() {
		
		return productName;
		
	}
	
	/**
	 * Sets the name of the product that will be using the generated model classes.
	 */
	public void setProductName(String productName) {
		
		this.productName = productName;
		
	}
	
	/**
	 * The package that the generated model classes should be placed in.
	 */
	public String getProductPackage() {
		
		return productPackage;
		
	}
	
	/**
	 * Sets the package that the generated model classes should be placed in.
	 */
	public void setProductPackage(String productPackage) {
		
		this.productPackage = productPackage;
		
	}
	
	/**
	 * The version of the model that is to be generated.
	 */
	public String getModelVersion() {
		
		return modelVersion;
		
	}
	
	/**
	 * Sets the version of the model that is to be generated.
	 */
	public void setModelVersion(String modelVersion) {
		
		this.modelVersion = modelVersion;
		
	}
	
	/**
	 * The file to write the generated classes to.
	 * This can be relative to the working directory or absolute.
	 */
	public File getOutputLocation() {
		
		return outputLocation;
		
	}
	
	/**
	 * Sets the location to write the generated classes to.
	 */
	public void setOutputLocation(String location) {
		
		this.outputLocation = new File(location);
		
	}
	
	/**
	 * The path to read the templates from.
	 */
	public File getTemplateLocation() {
		
		return templateLocation;
		
	}
	
	/**
	 * Sets the path to read the templates from to the given location.
	 */
	public void setTemplateLocation(String location) {
		
		this.templateLocation = new File(location);
		
	}
	
	/**
	 * The class name of the generator to be used.
	 */
	public String getGeneratorName() {
		
		return generatorName;
		
	}
	
	/**
	 * Sets the class name of the generator to be used to the given name.
	 */
	public void setGeneratorName(String generatorName) {
		
		this.generatorName = generatorName;
		
	}
	
	/**
	 * The Maven dependencies required by the model classes.
	 */
	public List<String> getDependencies() {
		
		return dependencies;
		
	}
	
	/**
	 * Adds the given Maven dependency required by the model classes.
	 */
	public void addDependency(String dependency) {
		
		getDependencies().add(dependency);
		
	}
	
	/**
	 * The packages containing the classes that should be converted.
	 */
	public List<String> getPackages() {
		
		return packages;
		
	}
	
	/**
	 * Adds the given package to the packages containing the classes that should be converted.
	 */
	public void addPackage(String pack) {
		
		getPackages().add(pack);
		
	}
	
	/**
	 * The names of the deepest super classes to consider for converting.
	 */
	public List<String> getDeepestSuperClasses() {
		
		return deepestSuperClasses;
		
	}
	
	/**
	 * Determines and returns whether the given class name is one of the deepest super classes to consider for converting.
	 */
	public boolean hasDeepestSuperClass(String parent) {
		
		return getDeepestSuperClasses().contains(parent);
		
	}
	
	public void addDeepestSuperClass(String parent) {
		
		getDeepestSuperClasses().add(parent);
		
	}
	
	public boolean getIgnoreProtocols() {
		
		return ignoreProtocols;
		
	}
	
	public void setIgnoreProtocols(boolean ignore) {
		
		this.ignoreProtocols = ignore;
		
	}
	
	public void addIgnoredClasses(String... ignoredClasses) {
		
		for ( String ignoredClass : ignoredClasses ) {
			
			addIgnoredClass(ignoredClass);
			
		}
		
	}
	
	public void addIgnoredClass(String ignoredClass) {
		
		addIgnoredEntity(new CustomClass(), ignoredClass);
		
	}
	
	public boolean hasIgnoredClass(String name) {
		
		return hasCustomClass(name) && getCustomClass(name).isIgnored();
		
	}
	
	public List<CustomClass> getIgnoredClasses() {
		
		return getCustomClasses().stream().filter(c -> c.isIgnored()).collect(Collectors.toList());
		
	}
	
	public void addIgnoredProperties(String... ignoredProperties) {
		
		for ( String ignoredProperty : ignoredProperties ) {
			
			addIgnoredProperty(ignoredProperty);
			
		}
		
	}
	
	public void addIgnoredProperty(String ignoredProperty) {
		
		addIgnoredEntity(new CustomProperty(), ignoredProperty);
		
	}
	
	public boolean hasIgnoredProperty(String name) {
		
		return hasCustomProperty(name) && getCustomProperty(name).isIgnored();
		
	}
	
	public List<CustomProperty> getIgnoredProperties() {
		
		return getCustomProperties().stream().filter(p -> p.isIgnored()).collect(Collectors.toList());
		
	}
	
	public void addCustomEntity(CustomEntity entity) {
		
		if ( entity instanceof CustomClass ) {
			
			getCustomClasses().add((CustomClass)entity);
			
		} else if ( entity instanceof CustomProperty ) {
			
			getCustomProperties().add((CustomProperty)entity);
			
		}
		
	}
	
	public boolean hasCustomClass(String name) {
		
		return getCustomClass(name) != null;
		
	}
	
	public CustomClass getCustomClass(String name) {
		
		return getEntity(getCustomClasses(), name);
		
	}
	
	public CustomClass getCustomClass(MCClass classs) {
		
		CustomClass c = getCustomClass(classs.getIdentifier());
		
		if ( c == null ) {
			
			c = getCustomClass(classs.getName());
			
		}
		
		return c;
		
	}
	
	public List<CustomClass> getCustomClasses() {
		
		return customClasses;
		
	}
	
	public boolean hasCustomProperty(String name) {
		
		return getCustomProperty(name) != null;
		
	}
	
	public CustomProperty getCustomProperty(String name) {
		
		return getEntity(getCustomProperties(), name);
		
	}
	
	public CustomProperty getSpecificCustomProperty(MCProperty property) {
		
		CustomProperty customProperty = null;

		if ( property != null ) {
			
			MCEntity e = property.getEntity();
			
			if ( e instanceof MCClass ) {

				CustomClass customClass = getCustomClass((MCClass)e);
				
				if ( customClass != null ) {
					
					customProperty = customClass.getProperty(property.getName());
					
				}
				
			}
			
		}
		
		return customProperty;
		
	}
	
	public CustomProperty getCustomTransformForProperty(MCProperty property) {
		
		CustomProperty customProperty = null;
		
		//Find specific property transform
		customProperty = getSpecificCustomProperty(property);
		
		if ( customProperty == null || !customProperty.hasTransform() ) {
			
			//Find generic property transform
			customProperty = getCustomProperties()
				.stream()
				.filter(p -> p.hasType() && p.getType().toType().equals(property.getType()))
				.findFirst().orElse(null);
			
		}
		
		return customProperty;
		
	}
	
	public List<CustomProperty> getCustomProperties() {
		
		return customProperties;
		
	}
	
	
	
	/* ===== Private Functions ===== */
	
	private void addIgnoredEntity(CustomEntity entity, String name) {
		
		entity.setName(name);
		entity.setIgnored(true);
		
		addCustomEntity(entity);
		
	}
	
	private <T extends CustomEntity> T getEntity(List<T> entities, String name) {
		
		T entity = null;
		
		for (T current : entities) {
			
			if ( name.equals(current.getName()) ) {
				
				entity = current;
				break;
				
			}
			
		}
		
		return entity;
		
	}
	
}

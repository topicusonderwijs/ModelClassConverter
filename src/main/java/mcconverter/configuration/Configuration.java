package mcconverter.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
		
	/* ===== Private Properties ===== */
	
	private static Configuration current;
	
	private String productName;
	private File templateLocation;
	private File outputLocation;
	private String generatorName;
	private List<String> dependencies;
	private List<String> packages;
	private List<String> deepestSuperClasses;
	private List<String> ignoredClasses;
	private List<String> ignoredProperties;
	private boolean ignoreProtocols;
	private Map<String, String> mappedEntities;
	
	
	
	/* ===== Construction ===== */
	
	private Configuration() {
		
		setProductName("");
		setOutputLocation("");
		setGeneratorName("");
		setIgnoreProtocols(false);
		dependencies = new ArrayList<String>();
		packages = new ArrayList<String>();
		deepestSuperClasses = new ArrayList<String>();
		ignoredClasses = new ArrayList<String>();
		ignoredProperties = new ArrayList<String>();
		mappedEntities = new HashMap<String, String>();
		
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
	
	public List<String> getDeepestSuperClasses() {
		
		return deepestSuperClasses;
		
	}
	
	public void addDeepestSuperClass(String parent) {
		
		getDeepestSuperClasses().add(parent);
		
	}
	
	public List<String> getIgnoredClasses() {
		
		return ignoredClasses;
		
	}
	
	public void addIgnoredClasses(String... ignoredClasses) {
		
		for ( String ignoredClass : ignoredClasses ) {
			
			addIgnoredClass(ignoredClass);
			
		}
		
	}
	
	public void addIgnoredClass(String ignoredClass) {
		
		getIgnoredClasses().add(ignoredClass);
		
	}
	
	public List<String> getIgnoredProperties() {
		
		return ignoredProperties;
		
	}
	
	public void addIgnoredProperties(String... ignoredProperties) {
		
		for ( String ignoredProperty : ignoredProperties ) {
			
			addIgnoredProperty(ignoredProperty);
			
		}
		
	}
	
	public void addIgnoredProperty(String ignoredProperty) {
		
		getIgnoredProperties().add(ignoredProperty);
		
	}
	
	public boolean getIgnoreProtocols() {
		
		return ignoreProtocols;
		
	}
	
	public void setIgnoreProtocols(boolean ignore) {
		
		this.ignoreProtocols = ignore;
		
	}
	
	/**
	 * A map of entities that require custom mapping.
	 */
	public Map<String, String> getMappedEntities() {
		
		return mappedEntities;
		
	}
	
	/**
	 * Returns the mapped entity for the given name.
	 * If there is no custom mapping for the given name then the given name is returned.
	 */
	public String getMappedEntity(String from) {
		
		String to = from;
		
		if ( hasMappedEntity(from) ) {
			
			to = getMappedEntities().get(from);
			
		}
		
		return to;
		
	}
	
	/**
	 * Determines and returns whether there is a custom mapping for the given entity name.
	 */
	public boolean hasMappedEntity(String from) {
		
		return getMappedEntities().containsKey(from);
		
	}
	
	/**
	 * Adds custom mapping for the given entity (`from`) to the new name (`to`).
	 */
	public void addMappedEntity(String from, String to) {
		
		mappedEntities.put(from, to);
		
	}
	
}

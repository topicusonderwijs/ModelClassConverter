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
	
	public static Configuration defaultConfiguration() {
		
		Configuration configuration = new Configuration();
		
		configuration.setTemplateLocation("templates/");
		configuration.addIgnoredClasses("package-info", "java.lang.Object", "java.lang.Enum");
		configuration.addIgnoredProperty("serialVersionUID");
		
		return configuration;
		
	}
	
	public static void setCurrent(Configuration configuration) {
		
		current = configuration;
		
	}
	
	public static Configuration current() {
		
		return current;
		
	}
	
	public String getProductName() {
		
		return productName;
		
	}
	
	public void setProductName(String productName) {
		
		this.productName = productName;
		
	}
	
	public File getOutputLocation() {
		
		return outputLocation;
		
	}
	
	public void setOutputLocation(String location) {
		
		this.outputLocation = new File(location);
		
	}
	
	public File getTemplateLocation() {
		
		return templateLocation;
		
	}
	
	public void setTemplateLocation(String location) {
		
		this.templateLocation = new File(location);
		
	}
	public String getGeneratorName() {
		
		return generatorName;
		
	}
	
	public void setGeneratorName(String generatorName) {
		
		this.generatorName = generatorName;
		
	}
	
	public List<String> getDependencies() {
		
		return dependencies;
		
	}
	
	public void addDependency(String dependency) {
		
		getDependencies().add(dependency);
		
	}
	
	public List<String> getPackages() {
		
		return packages;
		
	}
	
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
	
	public boolean hasMappedEntity(String from) {
		
		return getMappedEntities().containsKey(from);
		
	}
	
	public void addMappedEntity(String from, String to) {
		
		mappedEntities.put(from, to);
		
	}
	
}

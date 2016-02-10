package mcconverter.configuration;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigurationParser extends DefaultHandler {
	
	/* ===== Constants ===== */
	
	private static final String ProductTag = "product";
	private static final String TemplateTag = "template";
	private static final String OutputTag = "output";
	private static final String GeneratorTag = "generator";
	private static final String DependencyTag = "dependency";
	private static final String PackageTag = "package";
	private static final String DeepestSuperClassTag = "deepestSuperClass";
	private static final String IgnoredClassTag = "ignoredClass";
	private static final String IgnoredPropertyTag = "ignoredProperty";
	private static final String IgnoreProtocolsTag = "ignoreProtocols";
	private static final String MappedEntityTag = "mappedEntity";
	
	private static final String NameAttribute = "name";
	private static final String PathAttribute = "path";
	private static final String ValueAttribute = "value";
	private static final String FromAttribute = "from";
	private static final String ToAttribute = "to";
	
	
	
	/* ===== Private Properties ===== */
	
	private String location;
	private SAXParser parser;
	private Configuration configuration;
	
	
	
	/* ===== Construction ===== */
	
	/**
	 * Constructs a configuration parser.
	 * @param location The location of the configuration file.
	 */
	public ConfigurationParser(String location) {
		
		this.location = location;
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	/**
	 * Parses the configuration.
	 */
	public boolean parse() {
		
		boolean correct = true;
		
		try {
			
			configuration = Configuration.defaultConfiguration();
			
			parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new File(location), this);
			
		} catch (Exception e) {
			
			correct = false;
			
		}
		
		return correct;
		
	}
	
	/**
	 * Determines and returns whether the parser has a configuration.
	 */
	public boolean hasConfiguration() {
		
		return getConfiguration() != null;
		
	}
	
	/**
	 * Returns the parsed configuration or `null` if not available.
	 */
	public Configuration getConfiguration() {
		
		return configuration;
		
	}
	
	
	/* == DefaultHandler Functions == */
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		
		String name = attributes.getValue(NameAttribute);
		String path = attributes.getValue(PathAttribute);
		
		switch ( qName ) {
		
		case ProductTag:
			getConfiguration().setProductName(name);
			break;
		case TemplateTag:
			getConfiguration().setTemplateLocation(path);
			break;
		case OutputTag:
			getConfiguration().setOutputLocation(path);
			break;
		case GeneratorTag:
			getConfiguration().setGeneratorName(name);
			break;
		case DependencyTag:
			getConfiguration().addDependency(name);
			break;
		case PackageTag:
			getConfiguration().addPackage(name);
			break;
		case DeepestSuperClassTag:
			getConfiguration().addDeepestSuperClass(name);
			break;
		case IgnoredClassTag:
			getConfiguration().addIgnoredClass(name);
			break;
		case IgnoredPropertyTag:
			getConfiguration().addIgnoredProperty(name);
			break;
		case IgnoreProtocolsTag:
			getConfiguration().setIgnoreProtocols(getBoolean(attributes, ValueAttribute));
			break;
		case MappedEntityTag:
			getConfiguration().addMappedEntity(
				attributes.getValue(FromAttribute),
				attributes.getValue(ToAttribute)
			);
			break;
		default:
			break;
			
		}
		
	}
	
	
	
	/* ===== Private Functions ===== */
	
	private boolean getBoolean(Attributes attributes, String name) {
		
		return attributes.getValue(name).equals("true");
		
	}
	
}

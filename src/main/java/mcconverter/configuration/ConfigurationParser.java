package mcconverter.configuration;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import mcconverter.main.Main;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigurationParser extends DefaultHandler {
	
	/* ===== Constants ===== */
	
	private static final String ProductTag = "product";
	private static final String ModelTag = "model";
	private static final String TemplateTag = "template";
	private static final String OutputTag = "output";
	private static final String GeneratorTag = "generator";
	private static final String DependencyTag = "dependency";
	private static final String PackageTag = "package";
	private static final String DeepestSuperClassTag = "deepestSuperClass";
	private static final String IgnoreProtocolsTag = "ignoreProtocols";
	private static final String CustomClassTag = "customClass";
	private static final String CustomPropertyTag = "customProperty";

	private static final String NameAttribute = "name";
	private static final String PackageAttribute = "package";
	private static final String PathAttribute = "path";
	private static final String VersionAttribute = "version";
	private static final String ValueAttribute = "value";
	private static final String TransformAttribute = "transform";
	private static final String InitializedAttribute = "initialized";
	private static final String IgnoredAttribute = "ignored";
	private static final String ProvidedAttribute = "provided";
	private static final String ParentAttribute = "parent";
	private static final String AsAttribute = "as";
	private static final String KeyAttribute = "key";
	private static final String TypeAttribute = "type";
	
	
	
	/* ===== Private Properties ===== */
	
	private String location;
	private SAXParser parser;
	private Configuration configuration;
	
	private CustomClass currentClass;
	
	
	
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
			
		} catch (FileNotFoundException e) {
			
			Main.fatal("Could not find configuration file");
			
		} catch (Exception e) {
			
			e.printStackTrace();
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
		String as = attributes.getValue(AsAttribute);
		boolean ignored = getBoolean(attributes, IgnoredAttribute, false);
		boolean provided = getBoolean(attributes, ProvidedAttribute, false);
		
		switch ( qName ) {
		
		case ProductTag:
			getConfiguration().setProductName(name);
			getConfiguration().setProductPackage(attributes.getValue(PackageAttribute));
			break;
		case ModelTag:
			getConfiguration().setModelVersion(attributes.getValue(VersionAttribute));
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
		case IgnoreProtocolsTag:
			getConfiguration().setIgnoreProtocols(getBoolean(attributes, ValueAttribute, false));
			break;
		case CustomClassTag:
			
			CustomClass c = new CustomClass();
			c.setName(name);
			c.setIgnored(ignored);
			c.setRename(as);
			c.setParent(attributes.getValue(ParentAttribute));
			
			currentClass = c;
			getConfiguration().addCustomEntity(c);
			
			break;
		case CustomPropertyTag:
			
			CustomProperty p = new CustomProperty();
			
			p.setName(name);
			p.setIgnored(ignored);
			p.setRename(as);
			p.setKey(attributes.getValue(KeyAttribute));
			p.setType(CustomType.fromString(attributes.getValue(TypeAttribute)));
			p.setInitialized(getBoolean(attributes, InitializedAttribute, true));
			p.setTransform(CustomTransform.fromString(attributes.getValue(TransformAttribute)));
			
			String value = attributes.getValue(ValueAttribute);
			
			if ( value != null ) {
				
				p.setValue(new CustomValue(value));
				
			}
			
			if ( currentClass != null ) {
				
				currentClass.addProperty(p);
				
			} else {
				
				getConfiguration().addCustomEntity(p);
				
			}
			break;
		default:
			break;
			
		}
		
	}
	
	public void endElement(String uri, String localName, String qName) {
		
		if ( qName.equals(CustomClassTag) && currentClass != null ) {
			
			currentClass = null;
			
		}
		
	}
	
	
	
	/* ===== Private Functions ===== */
	
	private boolean getBoolean(Attributes attributes, String attributeName, boolean defaultValue) {
		
		boolean value = defaultValue;
		String stringValue = attributes.getValue(attributeName);
		
		if ( stringValue != null ) {
			
			value = stringValue.toLowerCase().equals("true");
			
		}
		
		return value;
		
	}
	
}

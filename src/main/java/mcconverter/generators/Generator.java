package mcconverter.generators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import mcconverter.main.Main;
import mcconverter.model.*;

public abstract class Generator {

	/* ===== Constants ===== */
	
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-YYYY");
	private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	
	
	
	/* ===== Private Properties ===== */
	
	private MCPackage pack;
	
	private freemarker.template.Configuration freemarker;
	private String date;
	private String time;
	
	public static final File OutputFolder = getConfiguration().getOutputLocation();
	
	
	
	/* ===== Construction ===== */
	
	public Generator() {
		
		freemarker = new Configuration(Configuration.VERSION_2_3_23);
		
		Date now = new Date();
		date = dateFormatter.format(now);
		time = timeFormatter.format(now);
		
		try {
			freemarker.setDirectoryForTemplateLoading(getConfiguration().getTemplateLocation());
		} catch (IOException e) {
			Main.fatal("Could not find location for templates");
		}
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public final void setPackage(MCPackage pack) {
		
		this.pack = pack;
		
	}
	
	public final MCPackage getPackage() {
		
		return pack;
		
	}
	
	public static mcconverter.configuration.Configuration getConfiguration() {
		
		return mcconverter.configuration.Configuration.current();
		
	}
	
	
	public final boolean generate() {
		
		boolean correct = false;
		
		if ( getPackage() != null && validatePackage(getPackage()) ) {

			try {
				
				if ( !OutputFolder.exists() ) {
					
					FileUtils.forceMkdir(OutputFolder);
					
				} else {
					
					FileUtils.cleanDirectory(OutputFolder);
					
				}
				
				List<MCEntity> entities = new ArrayList<>();
				
				//Validate entities
				for ( String identifier : getPackage().getEntityIdentifiers() ) {
					
					MCEntity entity = getPackage().getEntity(identifier);
					
					if ( validateEntity(entity) ) {
						entities.add(entity);
					}
					
				}
				
				//Generate entities
				for ( MCEntity entity : entities ) {
					
					Map<String, Object> model = generateModel(entity);
					
					if ( model != null && !getConfiguration().hasProvidedClass(entity.getName()) ) {
						
						for ( String templateName : getTemplates(entity) ) {
							writeModel(templateName, generateFileName(entity, templateName), model);
						}
						
					}
					
				}
				
				//Generate registry
				for ( String templateName : getTemplates(getPackage()) ) {

					Map<String, Object> model = getPackage().getModel(this);
					
					if ( validateModel(getPackage(), model) ) {
						
						writeModel(templateName, generateFileName(getPackage(), templateName), model);
						
					}
					
				}
				
				correct = true;
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			} catch (TemplateException e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		return correct;
		
	}
	
	
	/**
	 * Returns the templates to be used for a potential registry of the package.
	 * Note that an empty list should be returned if the generator does not require a registry.
	 */
	public abstract List<String> getTemplates(MCPackage pack);
	
	
	/**
	 * Returns the templates to be used for the given entity.
	 * Note that an empty list should be returned if the generator does not require a template for the given entity.
	 */
	public abstract List<String> getTemplates(MCEntity entity);
	
	
	/**
	 * Returns the imports required for the given entity.
	 * Note that an empty list should be returned if the entity does not require imports.
	 */
	public abstract List<String> generateImports(MCEntity entity);
	
	/**
	 * Returns the name to be used for the given property.
	 * This can be used to prevent from using names that are for instance keywords.
	 */
	public abstract String generatePropertyName(MCProperty property);
	
	/**
	 * Returns the literal to be used for the given property.
	 * The default implementation returns an empty string.
	 */
	public String generatePropertyLiteral(MCProperty property) {
		return "";
	}
	
	/**
	 * Returns the default value for the given property.
	 * A value of `null` indicates that the property should not have a default value.
	 * The default implementation returns `null`.
	 */
	public String generatePropertyValue(MCProperty property) {
		return null;
	}
	
	/**
	 * Generates and returns the mapping for the given property.
	 */
	public String generatePropertyMapping(MCProperty property) {
		return null;
	}
	
	/**
	 * Generates and returns the transform for the given property.
	 */
	public String generatePropertyTransform(MCProperty property) {
		return null;
	}
	
	/**
	 * Generates and returns the reading for the given property.
	 */
	public String generatePropertyReading(MCProperty property) {
		return null;
	}
	
	/**
	 * Generates and returns the writing for the given property.
	 */
	public String generatePropertyWriting(MCProperty property) {
		return null;
	}
	
	/**
	 * Generates and returns the name of the transform for the given property.
	 */
	public String generatePropertyTransformName(MCProperty property) {
		return null;
	}
	
	/**
	 * Generates the name for the given enum value.
	 */
	public abstract String generateEnumValueName(MCEnumValue value);
	
	/**
	 * Returns the name of the given type.
	 */
	public abstract String generateTypeName(MCType type);
	
	/**
	 * Returns the property syntax for representing the given type.
	 */
	public abstract String generateTypeLiteral(MCType type);
	
	/**
	 * Returns the literal for the given type parameter.
	 * The default implementation returns an empty string.
	 */
	public String generateTypeParameterLiteral(MCTypeParameter parameter) {
		return "";
	}

	/**
	 * Returns the file name that should be used for the given package and template.
	 * If no file should be created for the given package then `null` should be returned.
	 */
	public abstract String generateFileName(MCPackage pack, String template);
	
	/**
	 * Returns the file name that should be used for the given entity and template.
	 * If no file should be created for the given entity then `null` should be returned.
	 */
	public abstract String generateFileName(MCEntity entity, String template);
	
	/**
	 * Generates and validates a model for the given package.
	 * If the model for the given package is invalidated then `null` is returned.
	 */
	public final Map<String, Object> generateModel(MCPackage pack) {
		
		Map<String, Object> model = pack.getModel(this);
		
		return validateModel(pack, model) ? model : null;
		
	}
	
	/**
	 * Generates and validates a model for the given entity.
	 * If the model for the given entity is invalidated then `null` is returned.
	 */
	public final Map<String, Object> generateModel(MCEntity entity) {
		
		Map<String, Object> model = entity.getModel(this);
		
		return validateModel(entity, model) ? model : null;
		
	}
	
	
	
	/* ===== Protected Functions ===== */
	
	/**
	 * Validates the given package.
	 */
	protected abstract boolean validatePackage(MCPackage pack);
	
	/**
	 * Validates the given entity.
	 */
	protected abstract boolean validateEntity(MCEntity entity);

	/**
	 * Validates and potentially alters the model of the given package.
	 * The returned value should indicate whether the model is valid (`true`) or not (`false`).
	 */
	protected abstract boolean validateModel(MCPackage pack, Map<String, Object> model);
	
	/**
	 * Validates and potentially alters the model of the given entity.
	 * The returned value should indicate whether the model is valid (`true`) or not (`false`).
	 */
	protected abstract boolean validateModel(MCEntity entity, Map<String, Object> model);
	
	
	
	/* ===== Private Functions ===== */
	
	private void writeModel(String templateName, String fileName, Map<String, Object> model) throws TemplateException, IOException {
		
		if ( templateName != null && fileName != null ) {

			Template template = freemarker.getTemplate(templateName);
			File file = new File(OutputFolder.getAbsolutePath() + "/" + fileName);
			FileWriter writer = new FileWriter(file);
			
			model.put("user", getUser());
			model.put("product_name", getPackage().getName());
			model.put("product_package", getConfiguration().getProductPackage());
			model.put("model_version", getConfiguration().getModelVersion());
			model.put("file_name", fileName);
			model.put("file_date", date);
			model.put("file_time", time);
			
			template.process(model, writer);
			Main.entry("Generated", fileName, 0);
			
		}
		
	}
	
	/**
	 * Determines and returns the name of the current user.
	 * If the name could not be determines, a value of `null` will be returned.
	 */
	private String getUser() {
		
		String user = null;
		
		try {
		
			user = System.getProperty("user.name");
			
		} catch ( SecurityException e ) {}
		
		return user;
		
	}
	
}

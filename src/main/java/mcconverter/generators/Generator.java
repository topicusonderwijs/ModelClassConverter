package mcconverter.generators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
			e.printStackTrace();
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
		
		if ( getPackage() != null ) {

			try {
				
				FileUtils.cleanDirectory(OutputFolder);
				
				//Generate entities
				for ( String identifier : getPackage().getEntityIdentifiers() ) {
					
					MCEntity entity = getPackage().getEntity(identifier);
					validateEntity(entity);
					
					//Generate entity
					for (String templateName : getTemplates(entity)) {
						
						Map<String, Object> model = entity.getModel(this);
						validateModel(entity, model);
						
						writeModel(templateName, generateFileName(entity), model);
						
					}
					
				}
				
				//Generate registry
				for ( String templateName : getTemplates(getPackage()) ) {

					Map<String, Object> model = getPackage().getModel(this);
					validateModel(getPackage(), model);
					
					writeModel(templateName, generateFileName(getPackage()), model);
					
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
	 * Returns the name to be used for the given property.
	 * This can be used to prevent from using names that are for instance keywords.
	 */
	public abstract String generatePropertyName(MCProperty property);
	
	public abstract String generatePropertyLiteral(MCProperty property);
	
	public abstract String generatePropertyMapping(MCProperty property);
	
	
	public abstract String generateEnumValueName(MCEnum.MCEnumValue value);
	
	/**
	 * Returns the name of the given type.
	 */
	public abstract String generateTypeName(MCType type);
	
	/**
	 * Returns the property syntax for representing the given type.
	 */
	public abstract String generateTypeLiteral(MCType type);
	
	public abstract String generateTypeParameterLiteral(MCTypeParameter parameter);

	/**
	 * Returns the file name that should be used for the given package.
	 * If no file should be created for the given package then `null` should be returned.
	 */
	public abstract String generateFileName(MCPackage pack);
	
	/**
	 * Returns the file name that should be used for the given entity.
	 * If no file should be created for the given entity then `null` should be returned.
	 */
	public abstract String generateFileName(MCEntity entity);
	
	/**
	 * Validates the given entity.
	 */
	public abstract void validateEntity(MCEntity entity);

	/**
	 * Validates and potentially alters the model of the given package.
	 * The returned value should indicate whether the model is valid (`true`) or not (`false`).
	 */
	public abstract boolean validateModel(MCPackage pack, Map<String, Object> model);
	
	/**
	 * Validates and potentially alters the model of the given entity.
	 * The returned value should indicate whether the model is valid (`true`) or not (`false`).
	 */
	public abstract boolean validateModel(MCEntity entity, Map<String, Object> model);
	
	
	
	/* ===== Private Functions ===== */
	
	private void writeModel(String templateName, String fileName, Map<String, Object> model) throws TemplateException, IOException {
		
		if ( templateName != null && fileName != null ) {

			Template template = freemarker.getTemplate(templateName);
			File file = new File(OutputFolder.getAbsolutePath() + "/" + fileName);
			FileWriter writer = new FileWriter(file);
			
			model.put("product_name", getPackage().getName());
			model.put("file_name", fileName);
			model.put("file_date", date);
			model.put("file_time", time);
			
			template.process(model, writer);
			Main.entry("Generated", fileName, 0);
			
		}
		
	}
	
}

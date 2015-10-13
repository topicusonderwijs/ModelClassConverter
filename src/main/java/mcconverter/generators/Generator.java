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
import mcconverter.model.*;

public abstract class Generator {
	
	/* ===== Private Properties ===== */
	
	private MCPackage pack;
	
	private Configuration freemarker;
	private String date;
	
	public static final String Path = "/Users/Thomas/Desktop/Temp/";
	
	
	
	/* ===== Construction ===== */
	
	public Generator() {
		
		freemarker = new Configuration();
		
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY");
		date = format.format(new Date());
		
		try {
			freemarker.setDirectoryForTemplateLoading(new File("templates/"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public final void setPackage(MCPackage pack) {
		
		this.pack = pack;
		
	}
	
	public final MCPackage getPackage() {
		
		return pack;
		
	}
	
	
	public final boolean generate() {
		
		boolean correct = false;
		
		if ( getPackage() != null ) {

			try {
				
				FileUtils.cleanDirectory(Path);
				
				for ( String identifier : getPackage().getEntityIdentifiers() ) {
					
					MCEntity entity = getPackage().getEntity(identifier);
					
					for (String name : getTemplates(entity)) {

						Template template = freemarker.getTemplate(name);
						
						String fileName = generateFileName(entity);
						
						FileWriter writer = new FileWriter(Path + fileName);

						Map<String, Object> model = entity.getModel(this);
						
						model.put("package_name", getPackage().getName());
						
						model.put("file_name", fileName);
						model.put("file_date", date);
						
						validateModel(entity, model);
						
						template.process(model, writer);
						
					}
					
					System.out.println("-> Generated: " + entity.getIdentifier());
					
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
	
	public final String generateTypeName(MCType type, int parameter) {
		
		return generateTypeName(type.getParameter(parameter));
		
	}
	
	/**
	 * Returns the template to be used for the given entity.
	 */
	public abstract List<String> getTemplates(MCEntity entity);
	
	/**
	 * Returns the name to be used for the given property.
	 * This can be used to prevent from using names that are for instance keywords.
	 */
	public abstract String generatePropertyName(MCProperty property);
	
	public abstract String generateEnumValueName(MCEnum.MCEnumValue value);
	
	/**
	 * Returns the property syntax for representing the given type.
	 */
	public abstract String generateTypeName(MCType type);
	
	
	public abstract String generateFileName(MCEntity entity);
	
	public abstract void validateModel(MCEntity entity, Map<String, Object> model);
	
}

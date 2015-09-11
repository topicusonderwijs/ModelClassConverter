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
	
	private MCPackage pack;
	
	private Configuration freemarker;
	private String date;
	
	public static final String Path = "/Users/Thomas/Desktop/Temp/";
	
	
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
	
	public MCPackage getPackage() {
		
		return pack;
		
	}
	
	
	public final String generate(MCPackage pack) {
		
		this.pack = pack;
		
		try {
			
			FileUtils.cleanDirectory(Path);
			
			for ( String identifier : getPackage().getEntityIdentifiers() ) {
				
				MCEntity entity = getPackage().getEntity(identifier);
				
				for (String name : getTemplates(entity)) {

					Template template = freemarker.getTemplate(name);
					
					String fileName = generateFileName(entity);
					
					FileWriter writer = new FileWriter(Path + fileName);
					
					Map<String, Object> model = entity.getModel(this);
					
					model.put("file_name", fileName);
					model.put("file_date", date);
					
					validateModel(entity, model);
					
					template.process(model, writer);
					
				}
				
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} catch (TemplateException e) {
			
			e.printStackTrace();
			
		}
		
		
		return "";
		
	}
	
	/**
	 * Returns the template to be used for the given entity.
	 */
	public abstract List<String> getTemplates(MCEntity entity);
	
	public abstract String generateType(MCType type);
	
	public abstract String generateFileName(MCEntity entity);
	
	public abstract void validateModel(MCEntity entity, Map<String, Object> model);
	
}

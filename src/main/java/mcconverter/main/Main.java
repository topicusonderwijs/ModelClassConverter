package mcconverter.main;

import java.util.Map;

import org.codehaus.plexus.util.StringUtils;

import mcconverter.configuration.Configuration;
import mcconverter.configuration.ConfigurationParser;
import mcconverter.conversion.Analyzer;
import mcconverter.conversion.Loader;
import mcconverter.generators.*;
import mcconverter.model.MCPackage;


public class Main {
	
	public static final String DefaultConfigurationLocation = "conf/configuration.xml";
	
	public static void main(String[] args) {
		
		//Determine location of configuration
		String location = DefaultConfigurationLocation;
		
		if ( args.length > 0 ) {
			
			location = args[0];
			
		}
		
		//Parse configuration
		ConfigurationParser parser = new ConfigurationParser(location);
		if ( !parser.parse() ) {
			
			fatal("Could not parse configuration");
			
		} else {
			
			Configuration.setCurrent(parser.getConfiguration());
			
			//Load classes
			header("Loading");
			
			Loader loader = new Loader();
			if ( !loader.load() ) {
				
				fatal("Could not load classes");
				
			} else {
				
				Map<String, Class<?>> classes = loader.getClasses();
				if ( classes == null ) {
					
					fatal("Could not extract classes from loader");
					
				} else {
					
					//Analyze classes
					header("Analyzing");
					
					Analyzer analyzer = new Analyzer();
					MCPackage pack = analyzer.analyze(Configuration.current().getProductName(), classes);
					
					//Find generator
					Generator generator = null;
					
					try {
						
						Object gen = Class.forName(Configuration.current().getGeneratorName()).newInstance();
						
						if ( gen instanceof Generator ) {
							generator = (Generator)gen;
						}
						
					} catch (ClassNotFoundException e) {
						
						fatal("Class for generator not found");
						
					} catch (InstantiationException e) {
						
						fatal("Could not intialize generator");
						
					} catch (IllegalAccessException e) {
						
						fatal("Not allowed to initialize generator");
						
					}
					
					if ( generator == null ) {
						
						fatal("No valid class for generator found");
						
					} else {
						
						//Generate classes
						header("Generating");
						generator.setPackage(pack);
						
						if ( generator.generate() ) {
							
							header("Finished");
							info("✅ Finished generating to " + Configuration.current().getOutputLocation());
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Outputs the given title as a header.
	 */
	public static void header(String title) {
		
		info("\n\n\n ━━━━━━━━━━ " + title + " ━━━━━━━━━━ \n");
		
	}
	
	/**
	 * Outputs an entry with given name and content.
	 * The given indent is applied as well.
	 * An entry is for instance an analyzed entity.
	 */
	public static void entry(String name, String content, int indent) {
		
		info(StringUtils.repeat("\t", indent) +  "→ " + name + ": " + content);
		
	}
	
	/**
	 * Outputs the given message.
	 */
	public static void info(String message) {
		
		System.out.println(message);
		
	}
	
	/**
	 * Outputs the given message as a warning.
	 */
	public static void warning(String message) {
		
		System.err.println("Warning: " + message);
		
	}
	
	/**
	 * Outputs the given message as fatal error.
	 * The application will exit after the output is displayed.
	 */
	public static void fatal(String message) {
		
		System.err.println("❌️ Error: " + message);
		System.exit(0);
		
	}
	
}

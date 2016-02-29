package mcconverter.main;

import java.util.Map;

import org.codehaus.plexus.util.StringUtils;

import mcconverter.configuration.Configuration;
import mcconverter.configuration.ConfigurationParser;
import mcconverter.conversion.Analyzer;
import mcconverter.conversion.Loader;
import mcconverter.generators.*;
import mcconverter.main.commands.*;
import mcconverter.model.MCPackage;


public class Main {
	
	/* ===== Constants ===== */
	
	public static final String Version = "0.0.1";
	public static final String DefaultConfigurationLocation = "conf/configuration.xml";
	
	private static final Command[] Commands = {
		new FormattedCommand(),
		new HelpCommand(),
		new ConfigurationCommand()
	};
	
	
	
	/* ===== Public Properties ===== */
	
	public static String ConfigurationLocation = DefaultConfigurationLocation;
	
	
	
	/* ===== Public Functions ===== */
	
	public static void main(String[] arguments) {
		
		//Parse argument
		parseArguments(arguments);
		
		//Parse configuration
		ConfigurationParser parser = new ConfigurationParser(ConfigurationLocation);
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
							info(Format.format("✅ Finished generating to " + Configuration.current().getOutputLocation(), Format.Light, Format.Green));
							
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
		
		info(Format.format("\n\n\n ━━━━━━━━━━ " + title + " ━━━━━━━━━━ \n", Format.Dark, Format.Magenta));
		
	}
	
	/**
	 * Outputs an entry with given name and content.
	 * The given indent is applied as well.
	 * An entry is for instance an analyzed entity.
	 */
	public static void entry(String name, String content, int indent) {
		
		info(Format.format(StringUtils.repeat("\t", indent) +  "→ " + name + ": " + content, Format.Dark, Format.Blue));
		
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
		
		System.err.println(Format.format("Warning: " + message, Format.Light, Format.Black));
		
	}
	
	/**
	 * Outputs the given message as fatal error.
	 * The application will exit after the output is displayed.
	 */
	public static void fatal(String message) {
		
		System.err.println(Format.format("❌️ Error: " + message, Format.Dark, Format.Red));
		System.exit(0);
		
	}
	
	
	
	/**
	 * Exits the application.
	 */
	public static void exit() {
		
		System.exit(0);
		
	}
	
	
	
	/* ===== Private Functions ===== */
	
	private static void parseArguments(String[] arguments) {
		
		Command current = null;
		
		for ( String argument : arguments ) {
			
			if ( argument.startsWith(Command.CommandPrefix) ) {
				
				if ( current != null && current.getArgumentCount() != current.getArguments().size() ) {
					
					Main.fatal("Invalid number of argument for '" + Command.CommandPrefix + current.getName() + "' command");
					
				}
				
				current = getCommand(argument.substring(Command.CommandPrefix.length()));
				
				if ( current != null ) {
					
					current.reset();
					
				} else {
					
					Main.fatal("Invalid command: '" + argument + "'");
					
				}
				
			} else if ( current != null ) {
				
				if ( !current.addArgument(argument) ) {
					
					Main.fatal("Invalid argument for '" + Command.CommandPrefix + current.getName() + "' command");
					
				}
				
			}
			
		}
		
	}
	
	private static Command getCommand(String name) {
		
		Command command = null;
		
		for ( Command current : Commands ) {
			
			if ( current.getName().equals(name) ) {
				
				command = current;
				break;
				
			}
			
		}
		
		return command;
		
	}
	
	
}

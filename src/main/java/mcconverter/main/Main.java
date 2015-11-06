package mcconverter.main;

import java.util.Map;

import mcconverter.conversion.Analyzer;
import mcconverter.conversion.Loader;
import mcconverter.generators.*;
import mcconverter.model.MCPackage;


public class Main {
	
	public static final String PACKAGE_NAME = "Parro";
	
	public static final String[] DEPENDENCIES = {
		//"nl.topicus.digdag:digdag-service-contract:39"
		"nl.topicus.geon:geon-rest-contract:1-SNAPSHOT"
	};
	public static final String[] PACKAGES = {
		"nl.topicus.cobra.restcontract.model",
		//"nl.topicus.digdag.servicecontract.model"
		"nl.topicus.geon.restcontract.model"
	};
	public static final String[] ANNOTATIONS = {
		
	};
	public static final String[] DEEPEST_SUPERCLASSES = { "Linkable" };
	public static final String[] IGNORED_CLASSES = { "package-info", "java.lang.Object", "java.lang.Enum" };
	public static final String[] IGNORED_PROPERTIES = { "serialVersionUID" };
	
	public static final String GENERATOR_NAME = "mcconverter.generators.swift.SwiftObjectMapperParroGenerator";
	
	public static final String OUTPUT_LOCATION = "/Users/Thomas/Development/Topicus/geon/geon-ios/comm/Geon/Communication/rest/entities/generated/";
	
	
	public static void main(String[] args) {
		
		Loader loader = new Loader();
		
		loader.setDependencies(DEPENDENCIES);
		loader.setPackages(PACKAGES);
		
		System.out.println("\n\n\n ====== Loading ====== \n");
		
		if ( loader.load() ) {
			
			Map<String, Class<?>> classes = loader.getClasses();
			
			if ( classes != null ) {
				
				Analyzer analyzer = new Analyzer();
				
				System.out.println("\n\n\n ====== Analyzing ====== \n");
				
				MCPackage pack = analyzer.analyze(PACKAGE_NAME, classes);
				
				
				System.out.println("\n\n\n ====== Output ====== \n");
				
				System.out.println(pack.toString());
				
				Generator generator = null;
				
				try {
					
					Object gen = Class.forName(GENERATOR_NAME).newInstance();
					
					if ( gen instanceof Generator ) {
						generator = (Generator)gen;
					}
					
				} catch (ClassNotFoundException e) {
					System.err.println("Generator not found");
				} catch (InstantiationException e) {
					System.err.println("Could not intialize generator");
				} catch (IllegalAccessException e) {
					System.err.println("Not allowed to initialize generator");
				}
				
				if ( generator != null ) {
					
					generator.setPackage(pack);
					
					System.out.println("\n\n\n ====== Generating ====== \n");
					
					generator.generate();
					
				}
				
			}
			
		}
		
	}
	
}

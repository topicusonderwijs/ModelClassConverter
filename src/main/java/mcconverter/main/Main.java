package mcconverter.main;

import java.util.Map;

import mcconverter.conversion.Analyzer;
import mcconverter.conversion.Loader;
import mcconverter.generators.*;
import mcconverter.generators.swift.*;
import mcconverter.model.MCPackage;


public class Main {
	
	public static final String[] DEPENDENCIES = { "nl.topicus.digdag:digdag-service-contract-v37:1.0" };
	public static final String[] PACKAGES = { "nl.topicus.digdag.servicecontract.v37.model" };
	public static final String[] DEEPEST_SUPERCLASSES = { "Linkable" };
	public static final String[] IGNORED_CLASSES = { "package-info", "java.lang.Object", "java.lang.Enum" };
	public static final String[] IGNORED_PROPERTIES = { "serialVersionUID" };
	
	public static final String OUTPUT_LOCATION = "";
	
	
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
				
				MCPackage pack = analyzer.analyze(classes);
				
				
				System.out.println("\n\n\n ====== Output ====== \n");
				
				System.out.println(pack.toString());
				
				Generator gen = new SwiftRestKitGenerator();
				
				gen.setPackage(pack);
				
				System.out.println("\n\n\n ====== Generating ====== \n");
				
				gen.generate();
				
				
			}
			
		}
		
	}
	
}

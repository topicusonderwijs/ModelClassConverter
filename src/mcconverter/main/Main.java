package mcconverter.main;

import java.util.List;

import mcconverter.conversion.Analyzer;
import mcconverter.conversion.Loader;
import mcconverter.generators.*;
import mcconverter.generators.swift.*;
import mcconverter.model.MCPackage;


public class Main {
	
	public static final String[] DEPENDENCIES = { "nl.topicus.digdag:digdag-service-contract-v37:1.0" };
	public static final String[] PACKAGES = { "nl.topicus.digdag.servicecontract.v37.model" };
	public static final String OUTPUT_LOCATION = "";
	
	
	public static void main(String[] args) {
		
		Loader loader = new Loader();
		
		List<Class<?>> classes = loader.load( DEPENDENCIES , PACKAGES );
		
		if ( classes != null ) {
			
			Analyzer analyzer = new Analyzer();
			
			MCPackage pack = analyzer.analyze(classes);
			
			Generator gen = new SwiftRestKitGenerator();
			
			gen.generate(pack);
			
			
		}
		
	}
	
}

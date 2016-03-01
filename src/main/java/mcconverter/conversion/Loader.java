package mcconverter.conversion;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import mcconverter.configuration.Configuration;
import mcconverter.main.Main;

import com.tobedevoured.naether.api.Naether;
import com.tobedevoured.naether.impl.NaetherImpl;

public class Loader {
	
	/* ===== Constants ===== */
	
	private static final String InnerClassSymbol = "$";
	
	
	
	/* ===== Private Properties ===== */
	
	private URLClassLoader loader;
	private Map<String, Class<?>> classes;
	
	private boolean loaded;
	
	
	
	/* ===== Construction ===== */
	
	/**
	 * Constructs a loader.
	 */
	public Loader() {
		
		classes = new HashMap<String, Class<?>>();
		
		loaded = false;
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	/**
	 * Determines and returns whether a class with given name is loaded and should be converted.
	 */
	public final boolean hasClass(String name) {
		
		return classes.containsKey(name);
		
	}
	
	/**
	 * A map of classes and their identifiers that are loaded and should be converted.
	 */
	public final Map<String, Class<?>> getClasses() {
		
		return classes;
		
	}
	
	/**
	 * A flag indicating whether the loader is done loading all necessary classes.
	 */
	public final boolean hasLoaded() {
		
		return loaded;
		
	}
	
	/**
	 * Loads the necessary classes and determines which classes need to be converted.
	 */
	public final boolean load() {
		
		boolean correct = false;
		
		if ( !hasLoaded() ) {

			Naether n = new NaetherImpl();
			
			try {
				
				//Determine dependencies
				for ( String dependency : Configuration.current().getDependencies()) {
					
					n.addDependency(dependency);
					
				}
				
				n.resolveDependencies();
				
				
				//Determine all entries
				Map<String, String> paths = n.getDependenciesPath();
				List<URL> locations = new ArrayList<URL>();
				List<JarEntry> entries = new ArrayList<JarEntry>();
				
				
				for ( String key : paths.keySet() ) {
					
					String path = paths.get(key);
					
					try {
						
						JarFile file = new JarFile(path);
						Enumeration<JarEntry> fileEntries = file.entries();
						
						while ( fileEntries.hasMoreElements() ) {
							
							entries.add(fileEntries.nextElement());
							
						}
						
						locations.add( new URL("jar:file:" + path + "!/") );
						
						file.close();
						
					} catch ( Exception e ) {
						Main.warning("Could not load: " + path);
					}
					
				}
				
				//Load all necessary classes
				loader = URLClassLoader.newInstance( locations.toArray(new URL[locations.size()]) );
				
				for (JarEntry entry : entries) {
					
					loadClass(getClassName(entry), 0);
					
				}
				
				loaded = true;
				correct = true;
				
			} catch (Exception e) {
				correct = false;
			}
			
		}
		
		return correct;
		
	}
	
	
	/* ===== Private Functions ===== */
	
	private String getClassName(JarEntry entry) {
		
		String name = null;
		String file = entry.getName();
		
		if (file.endsWith(".class") ) {
			
			name = file.substring(0, file.length() - 6).replace('/', '.');
			
		}
		
		return name;
		
	}
	
	/**
	 * Converts and returns the given class name to its simple name.
	 */
	private String getSimpleName(String className) {
		
		String simpleName = null;
		String[] components = className.split("\\.");
		
		if ( components.length > 0 ) {
			
			simpleName = components[components.length - 1];
			
		}
		
		return simpleName;
		
	}
	
	private boolean loadClass(String className, int indent) {
		
		boolean correct = false;
		
		if ( className != null && !hasClass(className) ) {
			
			try {
				
				//Determine if class needs to be loaded
				if ( verifyClass(className, indent) ) {
					
					//Load class
					Class<?> c = loader.loadClass(className);
					
					classes.put(className, c);
					Main.entry("Loaded", className, indent);
					
				}
				
			} catch (ClassNotFoundException e) {
				
				Main.warning("Could not find: " + className);
				
			}
			
		}
		
		return correct;
		
	}
	
	private boolean verifyClass(String className, int indent) {
		
		boolean valid = false;
		
		if ( indent == 0 ) {
			
			for ( String pack : Configuration.current().getPackages() ) {
				
				valid = valid || className.startsWith( pack );
				
			}
			
		} else {
			
			valid = true;
			
		}
		
		return valid &&
				!className.contains(InnerClassSymbol) &&
				!Configuration.current().hasIgnoredClass(className) &&
				!Configuration.current().hasIgnoredClass(getSimpleName(className));
		
	}
	
}

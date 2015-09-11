package mcconverter.conversion;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import mcconverter.main.Main;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.plexus.util.StringUtils;

import com.tobedevoured.naether.DependencyException;
import com.tobedevoured.naether.URLException;
import com.tobedevoured.naether.api.Naether;
import com.tobedevoured.naether.impl.NaetherImpl;

public class Loader {
	
	/* ===== Private Properties ===== */
	
	private String[] dependencies;
	private String[] packages;
	
	private URLClassLoader loader;
	private Map<String, Class<?>> classes;
	
	private boolean loaded;
	
	
	
	/* ===== Construction ===== */
	
	public Loader() {
		
		classes = new HashMap<String, Class<?>>();
		
		loaded = false;
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public final void setDependencies(String[] dependencies) {
		
		this.dependencies = dependencies;
		
	}
	
	public final String[] getDependencies() {
		
		return dependencies;
		
	}
	
	public final void setPackages(String[] packages) {
		
		this.packages = packages;
		
	}
	
	public final String[] getPackages() {
		
		return packages;
		
	}
	
	public final boolean hasClass(String name) {
		
		return classes.containsKey(name);
		
	}
	
	public final Map<String, Class<?>> getClasses() {
		
		return classes;
		
	}
	
	public final boolean hasLoaded() {
		
		return loaded;
		
	}
	
	public final boolean load() {
		
		boolean correct = false;
		
		if ( !hasLoaded() ) {

			Naether n = new NaetherImpl();
			
			try {
				
				//Determine dependencies
				for ( String dependency : dependencies) {
					
					n.addDependency(dependency);
					
				}
				
				n.resolveDependencies();
				
				
				//Determine all entries
				Map<String, String> paths = n.getDependenciesPath();
				List<URL> locations = new ArrayList<URL>();
				List<JarEntry> entries = new ArrayList<JarEntry>();
				
				
				for ( String key : paths.keySet() ) {
					
					String path = paths.get(key);
					
					JarFile file = new JarFile(path);
					Enumeration<JarEntry> fileEntries = file.entries();
					
					while ( fileEntries.hasMoreElements() ) {
						
						entries.add(fileEntries.nextElement());
						
					}
					
					locations.add( new URL("jar:file:" + path + "!/") );
					
					file.close();
					
				}
				
				//Load all necessary classes
				loader = URLClassLoader.newInstance( locations.toArray(new URL[locations.size()]) );
				
				for (JarEntry entry : entries) {
					
					loadClass(getClassName(entry), 0);
					
				}
				
				loaded = true;
				correct = true;
				
			} catch (URLException e) {
				correct = false;
			} catch (DependencyException e) {
				correct = false;
			} catch (MalformedURLException e) {
				correct = false;
			} catch (IOException e) {
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
	
	private boolean loadClass(String name, int indent) {
		
		boolean correct = false;
		
		if ( name != null && !hasClass(name) ) {
			
			try {
				
				Class<?> c = loader.loadClass(name);
				
				
				
				//Determine if it needs conversion
				
				if ( verifyClass(c, indent) ) {
					
					classes.put(name, c);
					
					System.out.println(StringUtils.repeat("\t", indent) + "-> Loaded: " + name);
					
					Class<?> s = determineSuperClass(c);
					
					if ( s != null ) {
						
						correct = loadClass(s.getName(), indent + 1);
						
					}
					
					
				}
				
			} catch (ClassNotFoundException e) {
				
				System.err.println("Could not find: " + name);
				
			}
			
		}
		
		return correct;
		
	}
	
	private boolean verifyClass(Class<?> c, int indent) {
		
		boolean valid = false;
		
		if ( indent == 0 ) {
			
			for ( String pack : packages ) {
				
				valid = valid || c.getName().startsWith( pack ); //Also need super classes!
				
			}
			
		} else {
			
			valid = true;
			
		}
		
		return valid &&
				!ArrayUtils.contains(Main.IGNORED_CLASSES, c.getName()) &&
				!ArrayUtils.contains(Main.IGNORED_CLASSES, c.getSimpleName());
		
	}
	
	private Class<?> determineSuperClass(Class<?> c) {
		
		Class<?> s = c.getSuperclass();
		
		if ( s != null && (
			ArrayUtils.contains(Main.DEEPEST_SUPERCLASSES, c.getName())
			||
			ArrayUtils.contains(Main.DEEPEST_SUPERCLASSES, c.getSimpleName())
		)) {
			
			s = null;
			
		}
		
		return s;
		
	}
	
}

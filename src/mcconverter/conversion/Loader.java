package mcconverter.conversion;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.tobedevoured.naether.DependencyException;
import com.tobedevoured.naether.URLException;
import com.tobedevoured.naether.api.Naether;
import com.tobedevoured.naether.impl.NaetherImpl;

public class Loader {
	
	public List<Class<?>> load( String[] dependencies, String[] packages ) {
		
		List<Class<?>> classes = null;
		
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
			
			//Create loader
			URL[] l = new URL[0];
			URLClassLoader loader = URLClassLoader.newInstance( locations.toArray(l) );
			
			//Iterate over all entries
			classes = new ArrayList<Class<?>>();
			
			for (JarEntry entry : entries) {
				
				//Determine class name
				String name = getClassName(entry);
				
				if ( name != null ) {
					
					//Load class
					Class<?> c = loader.loadClass(name);
					
					//Determine if it needs conversion
					boolean add = false;
					
					for ( String pack : packages ) {
						
						if ( name.startsWith( pack ) ) { //Also need super classes!
							
							add = true;
							
						}
						
					}
					
					if ( add ) {
						
						classes.add(c);
						
					}
					
				}
				
			}
			
		} catch (URLException e) {
			classes = null;
		} catch (DependencyException e) {
			classes = null;
		} catch (MalformedURLException e) {
			classes = null;
		} catch (IOException e) {
			classes = null;
		} catch (ClassNotFoundException e) {
			classes = null;
		}
		
		return classes;
		
	}
	
	private static String getClassName(JarEntry entry) {
		
		String name = null;
		String file = entry.getName();
		
		if (file.endsWith(".class") ) {
			
			name = file.substring(0, file.length() - 6).replace('/', '.');
			
		}
		
		return name;
		
	}
	
}

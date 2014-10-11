package utils.refl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public final class ReflectionUtils {
	
	private ReflectionUtils() {} // Static Class
	
	 public static Set<Class<?>> getClasseNamesInPackage
     (String jarName, String packageName){
		 Set<Class<?>> classes = new HashSet<Class<?>>();

	   packageName = packageName.replaceAll(File.separator + "." , "/");
   
	   String[] s = jarName.split("!");
	 
	   try
	   {
		   JarInputStream jarFile = new JarInputStream (new FileInputStream (s[0].replace("jar:file:/", "")));
		   JarEntry jarEntry;

		     while(true) {
		       jarEntry=jarFile.getNextJarEntry ();
		       if(jarEntry == null){
		         break;
		       }
		       if((jarEntry.getName ().startsWith (packageName)) &&
		            (jarEntry.getName ().endsWith (".class")) ) {
		
				    String className = jarEntry.getName().replaceAll("/", File.separator + ".").replace(".class", "");
				    Class<?> clazz = null;
				    
					try 
					{
						URLClassLoader loader = URLClassLoader.newInstance(new URL[] { new URL(jarName) } , ClassLoader.getSystemClassLoader());
						clazz = loader.loadClass(className);
					} 
					catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (clazz != null)
						classes.add(clazz);  		  
		       }
		     }
	   }
	   catch (IOException e) {
		   
	   }
     
   return classes;
}
	
	/**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
	 * @throws IOException 
     */
    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException, IOException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
	
	/**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Class<?>[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }
}
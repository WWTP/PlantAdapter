package utils.refl;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import utils.colls.CollectionUtils;
import utils.refl.filters.ITypeFilter;

public class ReflectionHelper {

	private Set<String> packages;
	
	public ReflectionHelper(String... packages) {
		this.packages = new HashSet<String>();

		if (packages.length == 0 || packages == null) {
			// Aggiunge tutti i Package raggiungibile dal ClassLoader
			this.packages.addAll(CollectionUtils.getValuesCollection(Arrays.asList(Package.getPackages()), Package.class, 
					"getName", String.class));
		}
		else 
		{
			// Aggiunge tutti i Package indicati...
			this.packages.addAll(Arrays.asList(packages));
		}
	}
	
	public <TIn, TOut> Set<Class<? extends TOut>> getTypes(ITypeFilter<Object, TOut> filter) {
		
		if (filter == null)
			filter = new DummyFilter<Object, TOut>();
		
		Set<Class<? extends TOut>> classes = new HashSet<Class<? extends TOut>>();
		
		for (String packageName : this.packages) {
			// Cerco JAR con il ClassLoader
			URL jarURL = ClassLoader.getSystemResource(packageName.replace('.', '/'));
			if (jarURL != null) {
				if (jarURL.toString().startsWith("jar:")) {
					// Utilizza metodo per estrarre i .class dal JAR e aggiunge all'insieme i tipi trovati
					Set<Class<?>> packageTypes = ReflectionUtils.getClasseNamesInPackage(jarURL.toString(), packageName);
					for (Class<?> type : packageTypes) {
						if (filter.accept(type))
							classes.add((Class<? extends TOut>)type);
					}
				}
				else if (jarURL.toString().startsWith("file:"))
				{
					Class<?>[] packageTypes;
					try {
						packageTypes = ReflectionUtils.getClasses(packageName);

						for (Class<?> type : packageTypes) {
							if (filter.accept(type))
								classes.add((Class<? extends TOut>)type);
						}
					} 
					catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
			}
			// Utilizza metodo per ottenere i .class dal FileSystem e aggiunge all'insieme i tipi trovati
			else if (Package.getPackage(packageName) != null) {
				try 
				{
					Class<?>[] packageTypes = ReflectionUtils.getClasses(packageName);
					for (Class<?> type : packageTypes) {
						if (filter.accept(type))
							classes.add((Class<? extends TOut>)type);
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	
		return classes;
	}
	
	public Set<Class<?>> getTypes() {
		return this.getTypes(null);
	}
	
	/*
	
	public List<Class<?>> getSortedTypes(ITypeFilter filter, Comparator<Class<?>> comparator) {
		Set<Class<?>> types = this.getTypes(filter); // TODO
		// TODO
		return null;
	}
	
	public List<Class<?>> getSortedTypes(Comparator<Class<?>> comparator) {
		return this.getSortedTypes(null, comparator);
	}
	
	*/
	
	private static class DummyFilter<TIn, TOut> implements ITypeFilter<TIn, TOut>
	{
		@Override
		public boolean accept(Class<? extends TIn> type) {
			return true;
		}
	}
}
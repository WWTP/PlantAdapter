package utils.colls;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {
	
	public static <T, E> Collection<? extends E> getValuesCollection(Collection<T> collection, Class<T> elementType, String methodName, Class<E> methodReturnType) {
		
		Method method = null;
		List<E> values = new ArrayList<E>();
		// Ottiene il metodo da invocare (accessor senza parametri)
		try 
		{
			method = elementType.getMethod(methodName);
			
			for (T element : collection) {
				@SuppressWarnings("unchecked")
				E subElement = (E)method.invoke(element);
				values.add(subElement);
			}
		} 
		catch (Exception e) {
			return null;
		}
		
		return values;
	}
}
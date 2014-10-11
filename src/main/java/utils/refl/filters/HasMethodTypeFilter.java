package utils.refl.filters;

public class HasMethodTypeFilter extends DecoratorTypeFilter<Object, Object> {
	
	private String methodName;
	private boolean inherited;
	private Class<?>[] parameterTypes;

	public HasMethodTypeFilter(ITypeFilter<Object, ? extends Object> filter, String methodName, boolean inherited, Class<?>... parameterTypes) {
		super(filter);

		this.methodName = methodName;
		this.inherited = inherited;
		this.parameterTypes = parameterTypes.clone();
	}
	
	public HasMethodTypeFilter(String methodName, boolean inherited, Class<?>... parameterTypes) {
		this(null, methodName, inherited, parameterTypes);
	}
	
	public HasMethodTypeFilter(String methodName, Class<?>... parameterTypes) {
		this(null, methodName, true, parameterTypes);
	}

	@Override
	public boolean acceptImpl(Class<?> type) {
		try 
		{
			// Prova ad ottenere il metodo (se non è presente viene scatenata NoSuchMethodException)
			if (this.inherited)
				type.getMethod(this.methodName, parameterTypes);
			else
				type.getDeclaredMethod(this.methodName, this.parameterTypes);
			
			return true;
		}
		catch (NoSuchMethodException e) {
			return false;
		}
		catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
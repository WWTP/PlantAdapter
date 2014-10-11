package utils.refl.filters;

public class ImplementedInterfaceTypeFilter<TOut> extends AbstractTypeFilter<TOut> {

	public ImplementedInterfaceTypeFilter(ITypeFilter<Object, ? extends Object> filter, Class<TOut> typeOut) {
		super(filter, typeOut);
	}
	
	public ImplementedInterfaceTypeFilter(Class<TOut> typeOut) {
		super(null, typeOut);
	}

	@Override
	protected boolean validate(Class<? extends TOut> typeOut) {
		return true;
	}

	@Override
	protected boolean accept(Class<?> type, Class<? extends TOut> typeOut) {
		if (!type.isInterface()) // TODO
		{
			SubInterfaceTypeFilter<TOut> interfaceFilter = new SubInterfaceTypeFilter<TOut>(typeOut);

			for (Class<?> intface : type.getInterfaces()) {
				if (interfaceFilter.accept(intface))
					return true;
			}
			
			return false;
		}
		else return false;
	}
}
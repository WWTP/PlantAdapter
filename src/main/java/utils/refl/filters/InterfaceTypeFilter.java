package utils.refl.filters;

public class InterfaceTypeFilter extends DecoratorTypeFilter<Object, Object> {

	public InterfaceTypeFilter() {
		super(null);
	}
	
	public InterfaceTypeFilter(ITypeFilter<Object, ?> filter) {
		super(filter);
	}

	@Override
	public boolean acceptImpl(Class<?> type) {
		return type.isInterface();
	}
}
package utils.refl.filters;

public abstract class DecoratorTypeFilter<TIn, TOut> implements ITypeFilter<TIn, TOut> {

	private ITypeFilter<Object, ? extends TIn> filter;
	
	public DecoratorTypeFilter(ITypeFilter<Object, ? extends TIn> filter) {
		this.filter = filter;
	}
	
	public abstract boolean acceptImpl(Class<?> type);
	
	@Override
	public final boolean accept(Class<? extends TIn> type) {
		if (this.filter != null)
			return this.filter.accept(type) && this.acceptImpl(type);
		else
			return this.acceptImpl(type);
	}
}
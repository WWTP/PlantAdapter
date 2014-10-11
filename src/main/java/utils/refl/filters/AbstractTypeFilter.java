package utils.refl.filters;

public abstract class AbstractTypeFilter<TOut> extends DecoratorTypeFilter<Object, TOut> {

	private Class<? extends TOut> typeOut;
	
	public AbstractTypeFilter(ITypeFilter<Object, ?> filter, Class<? extends TOut> typeOut) {
		super(filter);
		
		if (typeOut == null)
			throw new IllegalArgumentException("typeOut == null");
		if (!this.validate(typeOut))
			throw new IllegalArgumentException("!this.validate(typeOut)");
		
		this.typeOut = typeOut;
	}
	
	protected abstract boolean validate(Class<? extends TOut> typeOut);
	protected abstract boolean accept(Class<?> type, Class<? extends TOut> typeOut);
	
	@Override
	public final boolean acceptImpl(Class<?> type) {
		return this.accept(type, this.typeOut);
	}
}
package utils.refl.filters;

/**
 * <p>Permette di ottenere le classi che estendono la classe data (compresa la classe stessa).</p>
 * @author JCC
 *
 * @param <TOut>
 */
public class SubClassTypeFilter<TOut> extends AbstractTypeFilter<TOut> {

	public SubClassTypeFilter(ITypeFilter<Object, ? extends Object> filter, Class<TOut> outType) {
		super(filter, outType);
	}
	
	public SubClassTypeFilter(Class<TOut> outType) {
		this(null, outType);
	}

	@Override
	protected boolean validate(Class<? extends TOut> typeOut) {
		return !typeOut.isInterface(); // TODO
	}

	@Override
	protected boolean accept(Class<?> type, Class<? extends TOut> typeOut) {
		if (type == null) // Object
			return typeOut.equals(Object.class);
		else if (type.equals(typeOut))
			return true;
		else
			return this.accept(type.getSuperclass(), typeOut);
	}
}
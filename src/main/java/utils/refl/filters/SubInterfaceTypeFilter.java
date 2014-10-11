package utils.refl.filters;

/**
 * <p>Permette di ottenere le interfacce che estendono l'interfaccia data (compresa l'interfaccia stessa).</p>
 * @author JCC
 *
 * @param <TOut>
 */
public class SubInterfaceTypeFilter<TOut> extends AbstractTypeFilter<TOut> {
	
	private Class<? extends TOut> typeOut;
	
	public SubInterfaceTypeFilter(ITypeFilter<Object, ?> filter, Class<? extends TOut> typeOut) {
		super(filter, typeOut);
	}
	
	public SubInterfaceTypeFilter(Class<? extends TOut> typeOut) {
		this(null, typeOut);
	}

	@Override
	protected boolean validate(Class<? extends TOut> typeOut) {
		// TOut deve essere un'interfaccia
		return typeOut.isInterface();
	}
	
    private boolean validateInterface(Class<?> type, Class<? extends TOut> typeOut) {
    	
    	if (type.getInterfaces().length > 0) {
    		for (Class<?> i : type.getInterfaces()) {
    			if (i.equals(typeOut))
    				return true;
    			
    			return validateInterface(i, typeOut);
    		}
    	}
    	return false;
    }

	@Override
	protected boolean accept(Class<?> type, Class<? extends TOut> typeOut) {
		
		if (type.isInterface())
			return this.validateInterface(type, typeOut);
		else
			return false;
	}
}
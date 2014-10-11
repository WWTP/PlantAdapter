package utils.refl.filters;

public interface ITypeFilter<TIn, TOut>
{	
	public boolean accept(Class<? extends TIn> type);
}
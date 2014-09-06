package quantities;

import javax.measure.quantity.Quantity;

public interface IAmount {
	public Class<? extends Quantity> getQuantity();
}
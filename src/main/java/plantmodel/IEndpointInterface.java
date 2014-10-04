package plantmodel;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public abstract class IEndpointInterface {
	private Class<? extends Quantity> quantity;
	private Unit<? extends Quantity> unit;
	
	public IEndpointInterface(Class<? extends Quantity> quantity, Unit<? extends Quantity> unit)
	{
		this.quantity = quantity;
		this.unit = unit;
	}
	
	public Class<? extends Quantity> getQuantity() {
		return this.quantity;
	}
	
	public Unit<? extends Quantity> getUnit() {
		return this.unit;
	}
}
package plantmodel;

import javax.measure.quantity.DataAmount;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import quantities.DigitalQuantity;

import unit.DigitalUnit;

public class DigitalEndpointInterface extends IEndpointInterface {
	
	// TODO public DigitalEndpointInterface(Class<? extends Quantity> quantity, DigitalUnit<? extends DigitalQuantity> unit) {
	public DigitalEndpointInterface(Class<? extends Quantity> quantity, Unit<? extends Quantity> unit) {
		super(quantity, unit);
	}
	
	@Override
	public String toString()
	{
		int levels;
		Unit<? extends Quantity> unit = super.getUnit();
		
		if(unit == DataAmount.UNIT)
			levels = 2;
		else if(unit instanceof DigitalUnit)
			levels = ((DigitalUnit<DigitalQuantity>)unit).getLevels();
		else
			throw new IllegalStateException();
		
		return "Digital (" + levels + " levels)";
	}
}

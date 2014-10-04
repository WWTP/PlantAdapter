package quantities;

import javax.measure.quantity.Quantity;

import org.jscience.physics.amount.Amount;

public class AnalogueAmount implements IAmount {

	private Class<? extends Quantity> quantity;
	private Amount<? extends Quantity> amount;
	
	public <T extends Quantity> AnalogueAmount(Class<? extends T> quantity, Amount<? extends T> amount) {
		this.quantity = quantity;
		this.amount = amount;
	}

	public  Amount<? extends Quantity> getAmount() {
		return this.amount;
	}
	
	// IAmount
	
	@Override
	public Class<? extends Quantity> getQuantity() {
		return this.quantity;
	}
	
	// Object
	
	@Override
	public String toString() {
		return this.amount.toString();
	}
}
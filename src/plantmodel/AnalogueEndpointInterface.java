package plantmodel;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.jscience.physics.amount.Amount;

import plantadapter.utils.ScalingTransformation;

public class AnalogueEndpointInterface extends IEndpointInterface {
	
	private Amount<? extends Quantity> minAmount;
	private Amount<? extends Quantity> maxAmount;

	private ScalingTransformation scalingTransformation;
	
	public AnalogueEndpointInterface(Class<? extends Quantity> quantity, Unit<? extends Quantity> unit, 
			Amount<? extends Quantity> minAmount, Amount<? extends Quantity> maxAmount, ScalingTransformation scalingTransformation) {
		super(quantity, unit);
		
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.scalingTransformation = scalingTransformation;
	}
	
	public ScalingTransformation getScalingTransformation() {
		return this.scalingTransformation;
	}
	
	public Amount<? extends Quantity> getMaximum() {
		return this.maxAmount;
	}
	
	public Amount<? extends Quantity> getMinimum() {
		return this.minAmount;
	}
	
	// Object
	
	@Override
	public String toString() {
		return super.getQuantity().getSimpleName() + 
				"(" + this.getUnit() + ") " + 
				"[" + this.minAmount + "," + this.maxAmount + "] " + 
				"(" + this.scalingTransformation + ")";
	}
}
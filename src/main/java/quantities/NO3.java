package quantities;

import static javax.measure.unit.NonSI.LITER;
import static javax.measure.unit.SI.GRAM;
import static javax.measure.unit.SI.MILLI;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public interface NO3 extends Quantity {
	@SuppressWarnings("unchecked")
	public static final Unit<NO3> UNIT = (Unit<NO3>) MILLI(GRAM).divide(LITER);
}
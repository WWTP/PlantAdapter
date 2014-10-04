package quantities;

import static javax.measure.unit.NonSI.LITER;
import static javax.measure.unit.SI.GRAM;
import static javax.measure.unit.SI.MILLI;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public interface NH4 extends Quantity {
	@SuppressWarnings("unchecked")
	public static final Unit<NH4> UNIT = (Unit<NH4>) MILLI(GRAM).divide(LITER);
}

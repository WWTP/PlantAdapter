package quantities;

import static javax.measure.unit.SI.GRAM;
import static javax.measure.unit.NonSI.LITER;
import static javax.measure.unit.SI.MILLI;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public interface TSS extends Quantity {
	@SuppressWarnings("unchecked")
	public static final Unit<TSS> UNIT = (Unit<TSS>) MILLI(GRAM).divide(LITER);
}
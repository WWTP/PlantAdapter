package quantities;

import static javax.measure.unit.SI.VOLT;
import static javax.measure.unit.SI.MILLI;

import javax.measure.quantity.ElectricPotential;
import javax.measure.unit.Unit;

public interface ORP extends ElectricPotential {
	public static final Unit<ElectricPotential> UNIT = MILLI(VOLT); // TODO
}
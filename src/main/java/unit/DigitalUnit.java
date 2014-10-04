package unit;

import java.util.HashMap;
import java.util.Map;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.Unit;

import quantities.IDigitalQuantity;

public class DigitalUnit<Q extends IDigitalQuantity> extends Unit<Q> {

	/// Flyweight Factory
	// TODO Valutare possibilità di ritornare DataAmount.UNIT nel caso di 2 livelli --> mappa restituisce Unit<? extends Quantity>
	private static Map<Integer, DigitalUnit<? extends IDigitalQuantity>> digitalUnitMap = new HashMap<Integer, DigitalUnit<? extends IDigitalQuantity>>();
	public static DigitalUnit<? extends IDigitalQuantity> getDigitalUnit(int levels)
	{
		if(!digitalUnitMap.containsKey(levels))
			digitalUnitMap.put(levels, new DigitalUnit<IDigitalQuantity>(levels));
		
		return digitalUnitMap.get(levels);
	}
	//\ Flyweight Factory
	
	private int levels;
	
	private DigitalUnit(int levels)
	{
		this.levels = levels;
	}
	
	public int getLevels()
	{
		return this.levels;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0.getClass().equals(DigitalUnit.class))
			return ((DigitalUnit<Q>)arg0).getLevels() == levels;
		return false;
	}

	@Override
	public Unit<? super Q> getStandardUnit() {
		return this;
	}

	@Override
	public int hashCode() {
		return levels;
	}

	@Override
	public UnitConverter toStandardUnit() {
		return UnitConverter.IDENTITY;
	}

}

package quantities;

import static javax.measure.unit.SI.BIT;

import java.util.HashMap;
import java.util.Map;

import javax.measure.quantity.DataAmount;
import javax.measure.unit.Unit;

public class DigitalQuantity implements IDigitalQuantity {
	
	private static Map<Integer, DigitalQuantity> quantityMap = new HashMap<Integer, DigitalQuantity>();
	public static DigitalQuantity getDigitalQuantity(int levels)
	{
		if(levels < 2)
			throw new IllegalArgumentException("levels < 2");
		
		if(!quantityMap.containsKey(levels))
			//if(levels == 2)
			//	quantityMap.put(2, BinaryQuantity.QUANTITY);
			//else
				quantityMap.put(levels, new DigitalQuantity(levels));
		
		return quantityMap.get(levels);
	}
	
	/**
	 * Numero di livelli che la grandezza digitale può assumere
	 */
	private int levels;
	
	private DigitalQuantity(int levels){
		this.levels = levels;
	}
	
	@Override
	public int getLevels()
	{
		return this.levels;
	}
	
	@Override
	public String toString()
	{
		return this.levels + "LevelsDigitalQuantity";
	}
	
	public static class BinaryQuantity extends DigitalQuantity {
		public static final Unit<DataAmount> UNIT = (Unit<DataAmount>) BIT;
		public static final BinaryQuantity QUANTITY = new BinaryQuantity();
		
		private BinaryQuantity() {
			super(2);
		}
		
		@Override
		public String toString()
		{
			return "BinaryQuantity";
		}
	}
}

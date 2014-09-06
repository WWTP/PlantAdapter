package plantmodel.adam;

import java.util.HashMap;
import java.util.Map;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import quantities.AnalogueAmount;
import quantities.IAmount;

public class Adam5000EngineeringUnits {
	
	public static Map<Integer, RangeEntry> ranges;
	static{
		ranges = new HashMap<Integer, RangeEntry>();
		ranges.put(Integer.parseInt("30", 16), new RangeEntry(0, 20, -3, 0.005, javax.measure.quantity.ElectricCurrent.UNIT, "vv.vvv"));
		ranges.put(Integer.parseInt("31", 16), new RangeEntry(4, 20, -3, 0.005, javax.measure.quantity.ElectricCurrent.UNIT, "vv.vvv"));
		ranges.put(Integer.parseInt("32", 16), new RangeEntry(0, 10, 0, 0.002442, javax.measure.quantity.ElectricPotential.UNIT, "vv.vvv"));
	}
	
	/**
	 * Ritorna una stringa che descrive il formato legato alla quantità passata
	 * @param q
	 * @return
	 */
	public static String getFormat(Class<? extends Quantity> q){
		// TODO implementare
		return "";
	}
	
	/**
	 * Ritorna una stringa che descrive il formato legato al RangeCode (vedi manuale)
	 * @param rangeCode
	 * @return
	 */
	public static String getFormat(int rangeCode){
		return ranges.get((Integer)rangeCode).format;
	}
	
	/**
	 * Converte in stringa secondo il formato Engineering Units specificato
	 * @param value
	 * @param q
	 * @param rangeCode
	 * @return
	 */
	public static String toString(double v, Class<? extends Quantity> q, int rangeCode){
		if(!ranges.containsKey((Integer) rangeCode))
			throw new UnsupportedOperationException();
		
		Double value = (Double) v;
		if(ranges.get((Integer) rangeCode).max < value || ranges.get((Integer) rangeCode).min > value)
			throw new IllegalArgumentException("value is out of range");
		
		int i = 0;
		String res = "";
		for(Character c : ranges.get((Integer) rangeCode).format.toCharArray()){
			i++;
			if((char) c == '.'){
				String s = String.valueOf(value).split("\\.")[0];
				if(s.length() < i){
					while(s.length() + 1 < i)
						s = "0" + s;
					res = s;
				}
		else res = s.substring(-i);
				i = 0;
			}
		}
		res += ".";
		String s = String.valueOf(value).split("\\.")[1];
		if(s.length() < i){
			while(s.length() < i)
				s += "0";
			res += s;
		}
		else res += s.substring(0, i);
		return res;
	}
	
	/**
	 * Converte in stringa secondo il formato Engineering Units ricavato
	 * @param value
	 * @param q
	 * @return
	 */
	public static String toString(double value, Class<? extends Quantity> q){
		if(q.equals(javax.measure.quantity.ElectricCurrent.class))
			return toString(value, q, Integer.parseInt("30", 16));
		if(q.equals(javax.measure.quantity.ElectricPotential.class))
			return toString(value, q, Integer.parseInt("32", 16));
		// TODO temperature
		throw new UnsupportedOperationException();
	}
	
	public static String toString(IAmount amount){
		// TODO Verificare
		return toString(((AnalogueAmount) amount).getAmount().getEstimatedValue(), amount.getQuantity());
	}
	
	// TODO Potrebbe essere utile estendere Endpoint con ADAMEndpoint a cui sia associabile una RangeEntry (vedi Engineering Units su manuale)
	private static class RangeEntry{
		// TODO vedere se implementare con Amount di JScience
		double min;
		double max;
		int prefix;
		double displayedResolution;
		Unit unit;
		String format;
		
		public RangeEntry(double min, double max, int prefix, double displayedResolution, Unit unit, String format){
			this.min = min;
			this.max = max;
			this.prefix = prefix; // prefisso rispetto all'unità; es.: milli = -3, unità = 0
			this.displayedResolution = displayedResolution; // rispetto al prefisso specificato
			this.unit = unit;
			this.format = format;
		}
	}
}

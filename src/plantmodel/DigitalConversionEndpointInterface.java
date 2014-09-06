package plantmodel;

import java.util.Map;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import quantities.IDigitalQuantity;
import quantities.IAmount;
import quantities.DigitalAmount;

public class DigitalConversionEndpointInterface extends
		DigitalEndpointInterface {

	private Map<Integer, IAmount> conversionMap;
	
	// TODO public DigitalConversionEndpointInterface(Class<? extends Quantity> quantity, DigitalUnit<? extends DigitalQuantity> unit, Map<DigitalAmount, IAmount> conversionMap) {
	public DigitalConversionEndpointInterface(IDigitalQuantity quantity, Unit<? extends Quantity> unit, Map<Integer, IAmount> conversionMap) {
			super(quantity.getClass(), unit);
		
		// TODO Controllo dimensione tabella conversione in relazione con la grandezza da convertire
		if(conversionMap.size() != quantity.getLevels())
			throw new IllegalArgumentException();
		
		this.conversionMap = conversionMap;
	}

	public IAmount convertAmount(DigitalAmount amount)
	{
		if(!this.conversionMap.containsKey(amount.getLevel()))
			throw new IllegalArgumentException();
		
		return this.conversionMap.get(amount.getLevel());
	}
}

package plantmodel;

import java.util.Map;

import quantities.DigitalQuantity.BinaryQuantity;
import quantities.IAmount;

public class BinaryConversionEndpointInterface extends
		DigitalConversionEndpointInterface {

	public BinaryConversionEndpointInterface(Map<Integer, IAmount> conversionMap) {
		super(BinaryQuantity.QUANTITY, BinaryQuantity.UNIT, conversionMap);
	}

}

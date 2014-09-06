package quantities;

import javax.measure.quantity.DataAmount;
import javax.measure.quantity.Quantity;

import plantmodel.datatypes.ASCIIString;

public class InformationAmount implements IAmount {
	
	public static InformationAmount valueOf(String s, InformationEncoding representation) {
		// TODO
		return null;
	}
	
	private byte[] bytes;
	
	public InformationAmount(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public byte[] getBytes() {
		return this.bytes;
	}
	
	@Override
	public Class<? extends Quantity> getQuantity() {
		return DataAmount.class;
	}
	
	// Object
	
	@Override
	public String toString() {
		return ASCIIString.fromByteArray(this.bytes).toString(); // TODO Encoding...
	}
	
	@Override
	public boolean equals(Object o) {
		InformationAmount informationAmount = (InformationAmount) o;
		byte[] bytes = informationAmount.getBytes();
		if(this.bytes.length != bytes.length) {
			return false;
		}
		for(int i = 0; i < this.bytes.length; i++)
			if(this.bytes[i] != bytes[i])
				return false;
		return true;
	}
}
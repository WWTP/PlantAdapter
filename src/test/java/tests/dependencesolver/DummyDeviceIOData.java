package tests.dependencesolver;

import plantmodel.IDeviceIOData;

public class DummyDeviceIOData implements IDeviceIOData {

	@Override
	public byte[] toByteArray() {
		return new byte[0];
	}
}
package tests.dependencesolver;

import plantmodel.Device;
import plantmodel.Endpoint;

public class DummyDevice extends Device {
	
	public DummyDevice(String id, Endpoint... endpoints) {
		super(id, "", "", "", "", "", endpoints, DummyDeviceIOData.class);
	}
	
	@Override
	public String toString() {
		return this.getID();
	}
}
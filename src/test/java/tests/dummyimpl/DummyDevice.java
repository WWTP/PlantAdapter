package tests.dummyimpl;

import plantmodel.Device;
import plantmodel.Endpoint;

public class DummyDevice extends Device {
	
	public static Endpoint[] setupEndpoints(String[] names)
	{
		Endpoint[] endpoints = new Endpoint[names.length];
		for (int i = 0; i < endpoints.length; i++) {
			endpoints[i] = new DummyEndpoint(names[i]);
		}
		return endpoints;
	}
	
	public static Endpoint[] setupEndpoints(int count)
	{
		Endpoint[] endpoints = new Endpoint[count];
		for (int i = 0; i < count; i++) {
			endpoints[i] = new DummyEndpoint("");
		}
		return endpoints;
	}
	
//	public DummyDevice(String id, Endpoint... endpoints) {
//		super(id, "", "", "", "", "", endpoints, DummyDeviceIOData.class);
//	}
	
	public DummyDevice(String id, String... endpointNames)
	{
		super(id, "", "", "", "", "", setupEndpoints(endpointNames), DummyDeviceIOData.class);
	}
	
	public DummyDevice(String id, int endpointCount)
	{
		super(id, "", "", "", "", "", setupEndpoints(endpointCount), DummyDeviceIOData.class);
	}
	
	@Override
	public String toString() {
		return this.getID();
	}
}
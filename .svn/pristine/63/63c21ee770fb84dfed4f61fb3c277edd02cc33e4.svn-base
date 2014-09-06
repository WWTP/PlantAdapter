package tests.model.sys.adam;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import plantadapter.excpts.EndpointNotFoundException;
import plantmodel.Endpoint;
import plantmodel.adam.Adam5000Device;
import tests.dependencesolver.DummyEndpoint;

public class Adam5000DeviceTest {

	static Adam5000Device device;
	
	@BeforeClass
	public static void setup() {
		device = new Adam5000Device("ADAM5000", "000000", 1); //TODO Slot montato: 5024
	}

	@Test
	public void testGetHWNetworkAddress() {
		assertEquals(1, device.getHWNetworkAddress());
	}

	@Test
	public void testSetHWNetworkAddress() {
		device.setHWNetworkAddress(2);
		assertEquals(2, device.getHWNetworkAddress());
		device.setHWNetworkAddress(1);
		assertEquals(1, device.getHWNetworkAddress());
	}

	@Test
	public void testIsActive() {
		Endpoint endpoint = device.getEndpointById("01S0C1Current");
		assertTrue(device.isActive(endpoint));
		
		endpoint = device.getEndpointById("01S0C1Voltage");
		assertFalse(device.isActive(endpoint));
	}

	@Test
	public void testGetSlotNumber() {
//		for(Endpoint e : device.getEndpoints())
//			System.out.println(e.getID());
//		
//		for(Endpoint e : device.getEndpoints(javax.measure.quantity.ElectricCurrent.class))
//			System.out.println(e.getID());
		
		Endpoint endpoint = device.getEndpointById("01S0C1Current");
		try {
			assertEquals(0, device.getSlotNumber(endpoint));
		} catch (EndpointNotFoundException e) {
			fail();
		}
		
		endpoint = new DummyEndpoint("DUMMY");
		
		try{
			device.getSlotNumber(endpoint);
			fail();
		} catch (EndpointNotFoundException e) {	}
	}

	@Test
	public void testGetChannelNumber() {
		Endpoint endpoint = device.getEndpointById("01S0C1Current");
		try {
			assertEquals(1, device.getChannelNumber(endpoint));
		} catch (EndpointNotFoundException e) {
			fail();
		}
		
		endpoint = new DummyEndpoint("DUMMY");
		
		try{
			device.getChannelNumber(endpoint);
			fail();
		} catch (EndpointNotFoundException e) {	}
	}

	@Test
	public void testGetActiveEndpoint() {
		Endpoint e = device.getEndpointById("01S0C0Current");
		Endpoint active = device.getActiveEndpoint(0, 0);
		
		assertEquals(e, active);
	}

}

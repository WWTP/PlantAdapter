package tests.dependencesolver;

import javax.measure.quantity.ElectricPotential;
import javax.measure.unit.SI;

import plantadapter.utils.LinearScalingTransformation;
import plantmodel.Device;
import plantmodel.Endpoint;
import plantmodel.AnalogueEndpointInterface;

public class DummyEndpoint extends Endpoint {
	
	public DummyEndpoint(String id) {
		super(id, new AnalogueEndpointInterface(ElectricPotential.class, SI.VOLT, null, null, LinearScalingTransformation.IDENTITY));
	}
   	
   public DummyEndpoint(String epId, String devId) {
		super(epId, new AnalogueEndpointInterface(ElectricPotential.class, SI.VOLT, null, null, LinearScalingTransformation.IDENTITY));
		// TODO Necessario perché altrimenti il Device associato all'Endpoint sarebbe null
		Device dev = Device.fromID(devId);
		if (dev == null) new DummyDevice(devId, this);
	}
}
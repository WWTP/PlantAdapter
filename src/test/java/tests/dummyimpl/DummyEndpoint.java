package tests.dummyimpl;

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
}
package plantmodel.dt80;

import plantmodel.DigitalEndpointInterface;

public class DT80DigitalEndpoint extends DT80Endpoint {
	
	// Constructors
	
	public DT80DigitalEndpoint(String id, DigitalEndpointInterface[] physicalInterfaces, DigitalEndpointInterface preferredInterface, int channelNumber) {
		super(id, physicalInterfaces, preferredInterface, channelNumber);
	}
	
	public DT80DigitalEndpoint(String id, DigitalEndpointInterface preferredInterface, int channelNumber) {
		this(id, new DigitalEndpointInterface[] { preferredInterface }, preferredInterface, channelNumber);
	}
	
	public DT80DigitalEndpoint(String id, DigitalEndpointInterface[] physicalInterfaces, int channelNumber) {
		this(id, physicalInterfaces, null, channelNumber);
	}
}
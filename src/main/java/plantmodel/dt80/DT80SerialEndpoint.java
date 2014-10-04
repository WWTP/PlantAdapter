package plantmodel.dt80;

import plantmodel.DataEndpointInterface;

public class DT80SerialEndpoint extends DT80Endpoint {
	
	// Constructors
	
	public DT80SerialEndpoint(String id, DataEndpointInterface[] physicalInterfaces, DataEndpointInterface preferredInterface, int channelNumber) {
		super(id, physicalInterfaces, preferredInterface, channelNumber);
	}
	
	public DT80SerialEndpoint(String id, DataEndpointInterface dataEndpointInterfaces, int channelNumber) {
		this(id, new DataEndpointInterface[] { dataEndpointInterfaces }, dataEndpointInterfaces, channelNumber);
	}
	
	public DT80SerialEndpoint(String id, DataEndpointInterface[] physicalInterfaces, int channelNumber) {
		this(id, physicalInterfaces, null, channelNumber);
	}
}
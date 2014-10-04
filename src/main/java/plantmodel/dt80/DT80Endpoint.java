package plantmodel.dt80;

import plantmodel.Endpoint;
import plantmodel.AnalogueEndpointInterface;
import plantmodel.IEndpointInterface;

public abstract class DT80Endpoint extends Endpoint {
	
	// Private Fields
	
	private int channelNumber;
	
	// Constructors
	
	public DT80Endpoint(String id, IEndpointInterface[] physicalInterfaces, IEndpointInterface preferredInterface, int channelNumber) {
		super(id, physicalInterfaces, preferredInterface);
		
		this.channelNumber = channelNumber;
	}
	
	public DT80Endpoint(String id, IEndpointInterface preferredInterface, int channelNumber) {
		this(id, new IEndpointInterface[] { preferredInterface }, preferredInterface, channelNumber);
	}
	
	public DT80Endpoint(String id, IEndpointInterface[] physicalInterfaces, int channelNumber) {
		this(id, physicalInterfaces, null, channelNumber);
	}
	
	// Accessors
	
	public int getChannelNumber() {
		return this.channelNumber;
	}
}
package plantmodel.dt80;

import plantmodel.AnalogueEndpointInterface;
import plantmodel.dt80.DT80Utils.Info.ChannelModifiers;

public class DT80AnalogueEndpoint extends DT80Endpoint {

	// Private Fields
	
	private ChannelModifiers channelModifier;
	
	// Constructors
	
	public DT80AnalogueEndpoint(String id, AnalogueEndpointInterface[] physicalInterfaces, AnalogueEndpointInterface preferredInterface, int channelNumber, ChannelModifiers channelModifier) {
		super(id, physicalInterfaces, preferredInterface, channelNumber);
		
		this.channelModifier = channelModifier;
	}
	
	public DT80AnalogueEndpoint(String id, AnalogueEndpointInterface preferredInterface, int channelNumber, ChannelModifiers channelModifier) {
		this(id, new AnalogueEndpointInterface[] { preferredInterface }, preferredInterface, channelNumber, channelModifier);
	}
	
	public DT80AnalogueEndpoint(String id, AnalogueEndpointInterface[] physicalInterfaces, int channelNumber, ChannelModifiers channelModifier) {
		this(id, physicalInterfaces, null, channelNumber, channelModifier);
	}
	
	// Accessors
	
	public ChannelModifiers getChannelModifier() {
		return this.channelModifier;
	}
}
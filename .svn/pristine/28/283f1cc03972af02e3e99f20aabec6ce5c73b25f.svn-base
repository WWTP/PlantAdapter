package plantmodel.dt80;

import plantmodel.dt80.DT80Utils.Info.ChannelTypes;

public abstract class DT80DigitalChannelDefinition extends DT80ChannelDefinition {

	public DT80DigitalChannelDefinition(int channelNumber,
			ChannelTypes channelType) {
		super(channelNumber, channelType);
		if(channelType.getChannelClass() != DT80Utils.Info.ChannelClasses.DIGITAL)
			// ChannelType passato deve essere DIGITAL
			throw new IllegalArgumentException("ChannelType != DIGITAL");
	}

}

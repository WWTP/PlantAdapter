package plantmodel.dt80;

import plantmodel.dt80.DT80Utils.Info.ChannelModifiers;
import plantmodel.dt80.DT80Utils.Info.ChannelTypes;

// TODO Una volta definite classi per tutti i sotto-tipi di channel-definition
// si potrebbe rendere astratta la classe base.

public class DT80SerialChannelDefinition extends DT80ChannelDefinition {

	public DT80SerialChannelDefinition(int channelNumber, DT80ChannelOptionList channelOptions) {
		super(channelNumber, ChannelTypes.SERIAL, ChannelModifiers.NONE, channelOptions);

		// TODO Check sulle opzioni possibili (?)
		
		if (channelNumber < 1 || channelNumber > 3)
			throw new IllegalArgumentException("channelNumber < 1 || channelNumber > 3");
	}
}
package plantmodel.dt80;

public class DT80DigitalStateInputChannelDefinition extends
		DT80DigitalChannelDefinition {

	public DT80DigitalStateInputChannelDefinition(int channelNumber) {
		super(channelNumber, DT80Utils.Info.ChannelTypes.DIGITAL_STATE_INPUT);
	}
}

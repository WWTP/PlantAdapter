package plantadapter.dcgs.impl;

import plantadapter.commands.DeviceCommand;
import plantadapter.commands.WriteCommand;
import plantadapter.dcgs.IDeviceCommandBuilder;
import plantadapter.dcgs.impl.misc.ActuableWriteCommandBuilder;

import plantmodel.misc.ActuableDevice;

public class ActuableDeviceCommandGenerator extends SequentialDeviceCommandGenerator<ActuableDevice> {

	public ActuableDeviceCommandGenerator(ActuableDevice associatedDevice) {
		super(associatedDevice);
	}
	
	@Override
	public IDeviceCommandBuilder getCommandBuilder(DeviceCommand command) {
		if(command instanceof WriteCommand)
			return new ActuableWriteCommandBuilder(super.getDevice(), (WriteCommand)command);
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean hasInputParser() {
		return false;
	}
}
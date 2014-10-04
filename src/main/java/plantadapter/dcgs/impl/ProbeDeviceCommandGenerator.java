package plantadapter.dcgs.impl;

import plantadapter.commands.DeviceCommand;
import plantadapter.commands.ReadCommand;
import plantadapter.dcgs.IDeviceCommandBuilder;
import plantadapter.dcgs.impl.misc.ProbeReadCommandBuilder;

import plantmodel.misc.ProbeDevice;

public class ProbeDeviceCommandGenerator extends
		SequentialDeviceCommandGenerator<ProbeDevice> {

	public ProbeDeviceCommandGenerator(ProbeDevice associatedDevice) {
		super(associatedDevice);
	}
	@Override
	public IDeviceCommandBuilder getCommandBuilder(DeviceCommand command) {
		if(command instanceof ReadCommand)
			return new ProbeReadCommandBuilder(super.getDevice(), (ReadCommand)command);
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean hasInputParser() {
		return false;
	}
}
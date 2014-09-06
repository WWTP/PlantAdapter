package plantadapter.cmdgen;

import plantadapter.commands.DeviceCommand;
import plantadapter.commands.PortCommand;

public class AggregatedDeviceCommandImpl implements IAggregatedDeviceCommand {

	private DeviceCommand[] sourceCommands;
	private PortCommand generatedCommand;
	
	public AggregatedDeviceCommandImpl(DeviceCommand[] sourceCommands, PortCommand generatedCommand){
		this.sourceCommands = sourceCommands;
		this.generatedCommand = generatedCommand;
	}
	
	@Override
	public PortCommand getSolvedDeviceCommand() {
		return this.generatedCommand;
	}

	@Override
	public DeviceCommand[] getAggregatedCommands() {
		return this.sourceCommands;
	}
}
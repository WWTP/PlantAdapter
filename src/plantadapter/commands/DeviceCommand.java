package plantadapter.commands;

import java.util.Date;

import plantadapter.annotations.CommandInfo;
import plantadapter.commands.dev.PlantDeviceCommandExecutor;

import plantmodel.Device;

@CommandInfo(commandExecutorClass = PlantDeviceCommandExecutor.class)
public abstract class DeviceCommand extends Command {
	
	// Instance Fields
	
	private final Device targetDevice;

	// Public Constructors

	public DeviceCommand(String commandID, Date timestamp, Device targetDevice) {
		super(commandID, timestamp);
		
		this.targetDevice = targetDevice;
	}
	
	public DeviceCommand(String commandID, Device targetDevice) {
		this(commandID, new Date(), targetDevice);
	}

	public Device getTargetDevice() {
		return this.targetDevice;
	}
	
	// Object
	
	@Override
	public String toString() {
		return "@" + this.getTargetDevice() + " " + super.toString();
	}
	
	@Override
	public boolean equals(Object o){
		DeviceCommand command = (DeviceCommand) o;
		if(super.equals(command))
			return false;
		if(!this.targetDevice.equals(command.getTargetDevice()))
			return false;
		return true;
	}
}
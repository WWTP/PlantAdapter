package plantadapter.commands;

import java.util.Date;

import plantmodel.Device;

public class StatusCommand extends DeviceCommand {

	//// PUBBLICI
	public StatusCommand(String commandID, Date timestamp, Device targetDevice) {
		super(commandID, timestamp, targetDevice);
		// TODO Auto-generated constructor stub
	}
	
	public StatusCommand(String commandID, Device targetDevice) {
		this(commandID, new Date(), targetDevice);
	}
	//\\ PUBBLICI
}
package plantadapter.commands.dev;


import plantadapter.commands.DeviceCommand;
import plantadapter.communication.ExecutionThread;
import plantadapter.communication.IMailbox;
import plantadapter.communication.ISink;

public class DeviceCommandExecutionThread extends ExecutionThread<DeviceCommand> {

	public DeviceCommandExecutionThread(IMailbox<DeviceCommand> src,
			ISink<DeviceCommand> sink) {
		super(src, sink);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void exceptionsHandler(Exception e, DeviceCommand item) {
		// TODO Auto-generated method stub
		
	}
}
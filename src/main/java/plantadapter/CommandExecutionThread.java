package plantadapter;

import plantadapter.commands.Command;
import plantadapter.communication.ExecutionThread;
import plantadapter.communication.IMailbox;
import plantadapter.communication.ISink;
import plantadapter.excpts.PlantAdapterException;


public class CommandExecutionThread extends ExecutionThread<Command> {

	private CommandLogger logger;
	
	public CommandExecutionThread(IMailbox<Command> src, ISink<Command> recog, CommandLogger logger) {
		super(src, recog);
	}

	@Override
	protected void exceptionsHandler(Exception e, Command cmd) {
		this.logger.getErrorCallbackReceiver(cmd).sendError(new PlantAdapterException() { }); // TODO Eccezioni di mailbox e sink
	}
}
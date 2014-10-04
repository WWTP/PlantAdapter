package plantadapter.commands;

import java.util.Date;

import plantadapter.annotations.CommandInfo;
import plantadapter.commands.diagn.DiagnosticCommandExecutor;

@CommandInfo(commandExecutorClass = DiagnosticCommandExecutor.class)
public class CommandDiagnosticCommand extends DiagnosticCommand {

	private Command target;
	
	public CommandDiagnosticCommand(String commandID, Command command) {
		super(commandID, DiagnosticCommand.Subject.COMMAND);
		this.target = command;
	}
	
	public CommandDiagnosticCommand(String commandID, Date timestamp, Command command) {
		super(commandID, timestamp, DiagnosticCommand.Subject.COMMAND);
		this.target = command;
	}
	
	public Command getTarget() {
		return target;
	}
}

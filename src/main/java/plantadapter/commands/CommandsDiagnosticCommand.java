package plantadapter.commands;

import java.util.Date;

import plantadapter.annotations.CommandInfo;
import plantadapter.commands.diagn.DiagnosticCommandExecutor;

@CommandInfo(commandExecutorClass = DiagnosticCommandExecutor.class)
public class CommandsDiagnosticCommand extends DiagnosticCommand {

	public CommandsDiagnosticCommand(String commandID) {
		super(commandID, DiagnosticCommand.Subject.COMMANDS);
		// TODO Auto-generated constructor stub
	}
	
	public CommandsDiagnosticCommand(String commandID, Date timestamp) {
		super(commandID, timestamp, DiagnosticCommand.Subject.COMMANDS);
		// TODO Auto-generated constructor stub
	}

}

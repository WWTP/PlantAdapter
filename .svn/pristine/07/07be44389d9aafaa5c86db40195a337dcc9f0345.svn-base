package plantadapter.results;

import plantadapter.CommandLogger.CommandInfo.CommandState;
import plantadapter.commands.Command;
import plantadapter.commands.CommandDiagnosticCommand;

public class CommandDiagnosticResult extends SingleCommandDiagnosticResult {

	private CommandDiagnosticCommand command;
	
	public CommandDiagnosticCommand getCommand() {
		return command;
	}
	
	public CommandDiagnosticResult(CommandDiagnosticCommand command, Command target, CommandState state) {
		super(target, state);
		this.command = command;
	}

}

package plantadapter.results;

import plantadapter.CommandLogger;
import plantadapter.commands.Command;

public class SingleCommandDiagnosticResult extends Result {
	private Command command;
	private CommandLogger.CommandInfo.CommandState state;
	
	public SingleCommandDiagnosticResult(Command command, CommandLogger.CommandInfo.CommandState state) {
		this.command = command;
		this.state = state;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public CommandLogger.CommandInfo.CommandState getState() {
		return state;
	}
	
	@Override
	public String toString() {
		return command.getCommandID() + ": " + state.name();
	}
}

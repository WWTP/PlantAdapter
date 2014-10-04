package plantadapter.commands;

import java.util.Date;

// @CommandInfo(commandExecutorClass = DiagnosticCommandExecutor.class)
public abstract class DiagnosticCommand extends Command {

	private Subject subject;
	
	public DiagnosticCommand(String commandID, Subject subject) {
		super(commandID);
		this.subject = subject;
	}
	
	public DiagnosticCommand(String commandID, Date timestamp, Subject subject) {
		super(commandID, timestamp);
		this.subject = subject;
	}
	
	public Subject getSubject() {
		return subject;
	}
	
	/**
	 * Indica a cosa fa riferimento il comando di diagnostica
	 */
	public static enum Subject {
		COMMANDS,
		COMMAND
	}
}

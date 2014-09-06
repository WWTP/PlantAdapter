package plantadapter.commands;

import java.util.Date;

public abstract class Command {
	
	// Instance Fields
	
	private final String commandID;
	private final Date timestamp;
	
	// Public Constructors
	
	public Command(String commandID, Date timestamp) {
		this.commandID = commandID;
		this.timestamp = timestamp;
	}
	
	public Command(String commandID) {
		this(commandID, new Date());
	}
	
	public String getCommandID() {
		return this.commandID;
	}
	
	public Date getTimestamp() {
		return this.timestamp;
	}
	
	// Object
	
	@Override
	public String toString() {
		return "(" + this.getCommandID() + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		Command command = (Command) o;
		if(!this.commandID.equals(command.getCommandID()))
			return false;
		if(!this.timestamp.equals(command.getTimestamp()))
			return false;
		return true;
	}
}
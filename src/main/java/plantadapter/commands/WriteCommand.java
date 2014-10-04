package plantadapter.commands;

import java.util.Date;

import plantmodel.Endpoint;

import quantities.IAmount;

public class WriteCommand extends PortCommand {
	
	private final IAmount value;

	// Public Constructors
	
	public WriteCommand(String commandID, Date timestamp, Endpoint targetPort, IAmount value) {
		super(commandID, timestamp, targetPort);

		this.value = value;
	}
	
	public WriteCommand(String commandID, Endpoint targetPort, IAmount value) {
		this(commandID, new Date(), targetPort, value);
	}
	
	public IAmount getValue() {
		return this.value;
	}
	
	// Object
	
	@Override
	public String toString() {
		return "Write(" + this.getValue().toString() + ") " + super.toString();
	}
	
	@Override
	public boolean equals(Object o){
		WriteCommand command = (WriteCommand) o;
		if(!super.equals(command))
			return false;
		if(!this.value.equals(command.getValue()))
			return false;
		return true;
	}
}
package plantadapter.commands;

import java.util.Date;

import javax.measure.quantity.Quantity;

import plantmodel.Endpoint;

public class ReadCommand extends PortCommand {
	
	// Instance Fields
	
	private final Class<? extends Quantity> quantity;
	
	// Public Constructors
	
	public ReadCommand(String commandID, Date timestamp, Endpoint targetPort, Class<? extends Quantity> quantity) {
		super(commandID, timestamp, targetPort);

		this.quantity = quantity;
	}
	
	public ReadCommand(String commandID, Endpoint targetPort, Class<? extends Quantity> quantity) {
		this(commandID, new Date(), targetPort, quantity);
	}
	
	public Class<? extends Quantity> getQuantity() {
		return this.quantity;
	}
	
	// Object
	
	@Override
	public String toString() {
		return "Read(" + this.getQuantity().getSimpleName() + ") " + super.toString();
	}
	
	@Override
	public boolean equals(Object o){
		ReadCommand command = (ReadCommand) o;
		if(!super.equals(command))
			return false;
		if(!this.quantity.equals(command.getQuantity()))
			return false;
		return true;
	}
}
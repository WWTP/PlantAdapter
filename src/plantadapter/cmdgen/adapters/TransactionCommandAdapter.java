package plantadapter.cmdgen.adapters;

import plantadapter.cmdgen.DeviceCommandGenerationController;
import plantadapter.commands.PortCommand;
import plantadapter.commands.TransactionCommand;
import plantmodel.Device;
import plantmodel.Endpoint;

public class TransactionCommandAdapter extends TransactionCommand implements ICommandAdapter {
	
	// usato per evitare di cambiare il livello di protezione
	// degli attributi della classe base
	private EndpointAdapter innerAdp;
	private TransactionCommand innerCmd;
	
	public TransactionCommandAdapter(TransactionCommand transactionCommand) {
		this(transactionCommand, transactionCommand.getTargetPort());
	}
	
	public TransactionCommandAdapter(TransactionCommand transactionCommand, Endpoint endpoint){
		super(
				transactionCommand.getCommandID(),
				transactionCommand.getTimestamp(),
				endpoint,
				transactionCommand.getCommands()
		);
		
		this.innerAdp = new EndpointAdapter(endpoint);
		this.innerCmd = transactionCommand;
		
		// NOTA: funziona perché l'array è restituito per riferimento e non copiato...
		// (altrimenti basterebbe memorizzare qui un secondo array).
		DeviceCommandGenerationController.adaptCommands(super.getCommands());
	}
	
	@Override
	public PortCommand getAdaptedCommand() {
		return this.innerCmd;
	}

	@Override
	public void setEndpoint(Endpoint e) {
		this.innerAdp.setEndpoint(e);
		
		for (PortCommand cmd : super.getCommands()) {
			((ICommandAdapter)cmd).setEndpoint(e);
		}
	}
	
	@Override
	public Endpoint getTargetPort() {
		return this.innerAdp.getTargetPort();
	}
	
	@Override
	public Device getTargetDevice()	{
		return this.innerAdp.getTargetDevice();
	}
}
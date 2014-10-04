package plantadapter.cmdgen.adapters;

import plantadapter.commands.PortCommand;
import plantadapter.commands.ReadCommand;
import plantmodel.Device;
import plantmodel.Endpoint;

public class ReadCommandAdapter extends ReadCommand implements ICommandAdapter {
	
	// usato per evitare di cambiare il livello di protezione
	// degli attributi della classe base
	private EndpointAdapter innerAdp;
	private ReadCommand innerCmd;
	
	public ReadCommandAdapter(ReadCommand readCommand){
		this(readCommand, readCommand.getTargetPort());
	}
	
	public ReadCommandAdapter(ReadCommand readCommand, Endpoint endpoint){
		super(
				readCommand.getCommandID(),
				readCommand.getTimestamp(),
				endpoint,
				readCommand.getQuantity()
		);
		
		this.innerAdp = new EndpointAdapter(endpoint);
		this.innerCmd = readCommand;
	}

	@Override
	public PortCommand getAdaptedCommand() {
		return this.innerCmd;
	}

	@Override
	public void setEndpoint(Endpoint e) {
		this.innerAdp.setEndpoint(e);
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
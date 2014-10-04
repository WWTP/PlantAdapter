package plantadapter.cmdgen.adapters;

import plantadapter.commands.PortCommand;
import plantmodel.Endpoint;

/**
 * INTERFACCIA MARKER
 * Identifica una classe (adapter) che estende un Command per replicarne il contenuto e cambiarne l'endpoint.
 * 
 * Viene utilizzato nel DeviceCommandGenerationController per modificare l'endpoint di destinazione di un comando una volta risolto.
 */
public interface ICommandAdapter {
	
	public PortCommand getAdaptedCommand();	
	public void setEndpoint(Endpoint e);
}
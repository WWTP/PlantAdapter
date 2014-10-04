package plantadapter;

import plantmodel.Device;

public interface IPlant {

	public Device[] getDevices();
	
	public Device[] getRootDevices();
	
	// TODO Interfacce...
	
	public ICommunicationManager getCommunicationManager();
	
	public CommandLogger getCommandLogger();
	
	public RequestMailboxFactory getRequestMailboxFactory();
}
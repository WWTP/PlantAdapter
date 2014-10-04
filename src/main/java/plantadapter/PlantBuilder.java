package plantadapter;

import java.util.LinkedList;
import java.util.List;

import plantadapter.excpts.PlantConfigurationException;

import plantmodel.Device;

public class PlantBuilder {

	private Device[] devices;
	private Device[] rootDevices;
	
	private CommunicationManager communicationManager;
	private CommandLogger commandLogger;
	private RequestMailboxFactory requestMailboxFactory;
	
	public PlantBuilder(Device[] plantDevices) {
		
		this.devices = plantDevices;
		
		List<Device> rootDevicesList = new LinkedList<Device>();
		for (Device device : this.devices) {
			// TODO Notare che questa è una convenzione (di plantadapter o di plantmodel?)
			if (device.hasHostConnections()) rootDevicesList.add(device);
		}
		
		this.rootDevices = rootDevicesList.toArray(new Device[0]);
		
		this.communicationManager = new CommunicationManager(rootDevices);
		this.commandLogger = new CommandLogger();
		this.requestMailboxFactory = new RequestMailboxFactory();
	}
	
	public PlantAdapter buildPlant() throws PlantConfigurationException {
		return new DefaultPlantAdapter(
				new Plant(
						this.devices,
						this.rootDevices,
						this.communicationManager, 
						this.commandLogger,
						this.requestMailboxFactory
					)
				);
	}
	
	private class Plant implements IPlant {
		
		private Device[] devices;
		private Device[] rootDevices;
		
		private ICommunicationManager communicationManager;
		
		private final CommandLogger commandLogger;
		private final RequestMailboxFactory requestMailboxFactory;
		
		public Plant(Device[] devices, Device[] rootDevices, ICommunicationManager communicationManager, 
				CommandLogger commandLogger, RequestMailboxFactory requestMailboxFactory) {
			
			this.devices = devices;
			this.rootDevices = rootDevices;
			
			this.communicationManager = communicationManager;
			
			this.commandLogger = commandLogger;
			this.requestMailboxFactory = requestMailboxFactory;
		}
		
		@Override
		public Device[] getDevices()
		{
			return this.devices;
		}
		
		@Override
		public Device[] getRootDevices()
		{
			return this.rootDevices;
		}
		
		// TODO
		
		@Override
		public ICommunicationManager getCommunicationManager() {
			return this.communicationManager;
		}
		
		@Override
		public CommandLogger getCommandLogger() {
			return this.commandLogger;
		}

		@Override
		public RequestMailboxFactory getRequestMailboxFactory() {
			return this.requestMailboxFactory;
		}
	}
}
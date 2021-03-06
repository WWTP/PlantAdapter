package plantadapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import plantadapter.commands.Command;

import plantadapter.excpts.CommandFailureException;
import plantadapter.excpts.PlantConfigurationException;

import plantmodel.Device;

public abstract class PlantAdapter {
	
	// Public Static Fields
	
	private static Map<String, PlantAdapter> map = new HashMap<String, PlantAdapter>();
	
	// Public Static Methods
	
	// TODO Creazione istanze specificando IPlant (ID incapsulato all'interno...).
	
	public static void newInstance(String plantId, Device[] plantDevices) throws PlantConfigurationException {
		newInstance(plantId, plantDevices, null);
	}
	
	public static PlantAdapter newInstance(String plantId, Device[] plantDevices, IPlantCallbackReceiver observer) throws PlantConfigurationException {
		
		// TODO verifica che non sia gi� presente un Plant con lo stesso ID, eventuali controlli sui dispositivi (valutare se evitare condivisioni etc)...

		PlantBuilder plantBuilder = new PlantBuilder(plantDevices);	
		map.put(plantId, plantBuilder.buildPlant());
		return getInstance(plantId, observer);
	}

	public final static PlantAdapter getInstance(String plantId, IPlantCallbackReceiver defaultCallbackReceiver) {
		PlantAdapter plantAdapter = map.get(plantId);
		if (plantAdapter == null) return null;	
		return new PlantAdapterProxy(plantAdapter, defaultCallbackReceiver);
	}

	public final static String[] getIds()
	{
		return map.keySet().toArray(new String[0]);
	}
	
	public final static String getId(PlantAdapter plantAdapter) {
		for(Entry<String, PlantAdapter> p : map.entrySet())
			if(plantAdapter.equals(p.getValue()))
				return p.getKey();
		return null;
	}
	
	protected PlantAdapter() {}
	
	// TODO Numero di observers...

	// Observers

	/*
	public void setObserver(IPlantObserver observer) {
		this.observer = observer;
	}
	
	public void setObserver(final IInputObserver inputObserver, final IErrorObserver errorObserver) {
		this.setObserver(new IPlantObserver() {
			@Override
			public void sendInput(String xml) {
				inputObserver.sendInput(xml);
			}
			@Override
			public void sendError(PlantAdapterException exception) {
				errorObserver.sendError(exception);
			}
		});
	}
	
	protected IPlantObserver getObserver()
	{
		return this.observer; // TODO
	}
	*/
			
	// Commands
	
	public abstract void sendCommand(Command command) throws CommandFailureException;

	public abstract void sendCommand(Command command, IPlantCallbackReceiver callbackReceiver) throws CommandFailureException;
	
	public abstract IPlant getPlantModel(); // TODO Vedi se puoi fornire direttamente l'implementazione...
	
	/*
	public abstract void command(String xml, IPlantObserver observer) throws CommandFailureException; // TODO Eccezione (?)
	
	public abstract void command(Document xml, IPlantObserver observer) throws CommandFailureException; // TODO Eccezione (?)
	
	public void command(String xml) throws CommandFailureException {
		if (this.observer == null) throw new IllegalStateException("this.observer == null");
		this.command(xml, this.observer);
	}
	
	public void command(Document xml) throws CommandFailureException {
		if (this.observer == null) throw new IllegalStateException("this.observer == null");
		this.command(xml, this.observer);
	}
	*/
}
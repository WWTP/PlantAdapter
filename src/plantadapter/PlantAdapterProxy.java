package plantadapter;

import plantadapter.commands.Command;
import plantadapter.excpts.CommandFailureException;

public class PlantAdapterProxy extends PlantAdapter {

	private final PlantAdapter innerAdapter;
	private final IPlantCallbackReceiver defaultCallbackReceiver;
	
	public PlantAdapterProxy(PlantAdapter innerAdapter, IPlantCallbackReceiver defaultCallbackReceiver) {
		this.innerAdapter = innerAdapter;
		this.defaultCallbackReceiver = defaultCallbackReceiver;
	}

	@Override
	public void sendCommand(Command command) throws CommandFailureException {
		this.innerAdapter.sendCommand(command, defaultCallbackReceiver);
	}

	@Override
	public void sendCommand(Command command, IPlantCallbackReceiver callbackReceiver) throws CommandFailureException {
		this.innerAdapter.sendCommand(command, callbackReceiver);
	}

	@Override
	public IPlant getPlantModel() {
		return this.innerAdapter.getPlantModel();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof PlantAdapter)
			return o == this.innerAdapter;
		
		return o == this;
	}
}
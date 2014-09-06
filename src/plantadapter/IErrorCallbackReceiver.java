package plantadapter;

import plantadapter.excpts.PlantAdapterException;

public interface IErrorCallbackReceiver extends ICallbackReceiver {
	
	public void sendError(PlantAdapterException exception);
}
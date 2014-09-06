package plantadapter;

import plantadapter.results.Result;

public interface IInputCallbackReceiver extends ICallbackReceiver {
	
	public void sendInput(Result result);
}
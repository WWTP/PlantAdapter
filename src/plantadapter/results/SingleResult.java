package plantadapter.results;

import java.util.Date;
import plantadapter.commands.DeviceCommand;
import quantities.IAmount;

public class SingleResult extends Result {
	
	private final IAmount value;
	private final DeviceCommand request; 
	
	protected SingleResult(IAmount value, DeviceCommand request, Date timestamp) {
		super(timestamp);

		this.value =  value;
		this.request = request;
	}
	
	public SingleResult(IAmount value, DeviceCommand request) {
		this(value, request, new Date());
	}
	
	public IAmount getValue() {
		return this.value;
	}
	
	public DeviceCommand getRequest() {
		return this.request;
	}
	
	// Object
	
	@Override
	public String toString() {
		return this.value.toString() + " [" + this.request + "]";
	}
}
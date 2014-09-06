package plantadapter.parsers;

import plantadapter.commands.DeviceCommand;
import plantadapter.results.SingleResult;
import plantmodel.IDeviceIOData;

public interface IParsingStrategy {
	public SingleResult parse(IDeviceIOData input, DeviceCommand command);
}
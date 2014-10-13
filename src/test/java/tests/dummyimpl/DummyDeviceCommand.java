package tests.dummyimpl;

import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import plantadapter.commands.ReadCommand;
import plantmodel.Endpoint;

public class DummyDeviceCommand extends ReadCommand {

	private DummyDevice device;
	
	public DummyDeviceCommand(DummyDevice dev, Endpoint endpoint) throws ParserConfigurationException {
		super(UUID.randomUUID().toString(), endpoint, DummyQuantity.class);
		this.device = dev;
	}
	
	@Override
	public String toString() {
		return this.device.toString();
	}
}
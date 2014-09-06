package tests.commandtreebuilder;

import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import plantadapter.commands.ReadCommand;
import tests.dependencesolver.DummyDevice;
import tests.dependencesolver.DummyEndpoint;

public class DummyDeviceCommand extends ReadCommand {

	private DummyDevice device;
	
	public DummyDeviceCommand(DummyDevice dev, String endpointID) throws ParserConfigurationException {
		super(UUID.randomUUID().toString(), new DummyEndpoint(endpointID), DummyQuantity.class);
		this.device = dev;
	}
	
	@Override
	public String toString() {
		return this.device.toString();
	}
}
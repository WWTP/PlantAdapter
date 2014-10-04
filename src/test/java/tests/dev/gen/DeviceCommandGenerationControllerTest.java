package tests.dev.gen;

import java.io.IOException;
import java.util.UUID;

import javax.measure.quantity.Quantity;

import org.junit.Test;

import plantadapter.CommandLogger;
import plantadapter.cmdgen.DeviceCommandGenerationController;
import plantadapter.cmdgen.IDeviceCommandTree;
import plantadapter.commands.DeviceCommand;
import plantadapter.commands.ReadCommand;
import plantadapter.commands.WriteCommand;
import plantadapter.excpts.PlantConfigurationException;
import plantmodel.Device;
import quantities.IAmount;
import quantities.InformationAmount;
import quantities.ORP;
import tests.model.ModelTest;
import tests.plantadapter.PlantAdapterTest;

public class DeviceCommandGenerationControllerTest {

	public static WriteCommand getWriteCommand(String targetDeviceId, String targetEndpointId, IAmount a) {
		Device targetDevice = Device.fromID(targetDeviceId);
		return  new WriteCommand(UUID.randomUUID().toString(), targetDevice.getEndpointById(targetEndpointId), a);
	}
	
	public static ReadCommand getReadCommand(String targetDeviceId, String targetEndpointId, Class<? extends Quantity> q) {
		Device targetDevice = Device.fromID(targetDeviceId);
		return new ReadCommand(UUID.randomUUID().toString(), targetDevice.getEndpointById(targetEndpointId), q);
	}
	
	@Test
	public final void testGetCommandTrees() throws PlantConfigurationException, IOException, InterruptedException {
		CommandLogger dummyLogger = new CommandLogger(); // TODO
		DeviceCommandGenerationController cmdGen = new DeviceCommandGenerationController(dummyLogger);
		
		ModelTest.setup();
		
		InformationAmount someData = new InformationAmount("/H".getBytes());
		
		DeviceCommand[] commands = new DeviceCommand[] {
		    getReadCommand("ORP_ANOX", "SOURCE", ORP.class),
			getWriteCommand("DT80", "ETHERNET_PORT", someData),
			getWriteCommand("DT80", "ETHERNET_PORT", someData)
		};
		
		IDeviceCommandTree[] trees = cmdGen.getCommandTrees(commands);
		
		for (IDeviceCommandTree tree : trees) {
			PlantAdapterTest.printNormalized(((InformationAmount)tree.getRawCommand().getValue()).getBytes());
			System.out.println("\n");
		}
	}
}
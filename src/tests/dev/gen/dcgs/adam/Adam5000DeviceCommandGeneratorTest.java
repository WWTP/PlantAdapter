package tests.dev.gen.dcgs.adam;

import static org.junit.Assert.*;

import javax.measure.quantity.ElectricCurrent;

import org.jscience.physics.amount.Amount;
import org.junit.BeforeClass;
import org.junit.Test;

import plantadapter.cmdgen.DeviceCommandGenerationList;
import plantadapter.commands.WriteCommand;
import plantadapter.dcgs.DeviceCommandGeneratorFactory;
import plantadapter.excpts.DeviceNotFoundException;
import plantadapter.excpts.InvalidCommandException;
import plantadapter.excpts.PlantConfigurationException;
import plantmodel.Device;
import plantmodel.Endpoint;
import plantmodel.adam.Adam5000Device;
import plantmodel.datatypes.ASCIIString;
import quantities.AnalogueAmount;
import quantities.InformationAmount;
import tests.model.ModelTest;

public class Adam5000DeviceCommandGeneratorTest {

	@BeforeClass
	public static void setup() throws PlantConfigurationException{
		ModelTest.setup();
	}
	
	private static DeviceCommandGeneratorFactory dcgFactory = new DeviceCommandGeneratorFactory(); 
	
	@Test
	public void testAdam5000DeviceCommandGenerator() {
//		// TEST su generazione singolo DeviceCommandGenerator
//		Adam5000Device device = new Adam5000Device("Dummy", null, 1);
//		Adam5000DeviceCommandGenerator dcg = new Adam5000DeviceCommandGenerator(device);
//		assertEquals(device,dcg.getRelatedDevice());
		
		// TEST su quanto ISTANZIATO nel SISTEMA
		for(Device d : Device.getSystemDevices()){
			try {
				if(d instanceof Adam5000Device)
					assertNotNull(dcgFactory.getDeviceCommandGenerator(d));
			} catch (Exception e) {
				fail();
			}
		}
	}

	@Test
	public void testGetNewCommands() {
		Adam5000Device device = null;
		for(Device d : Device.getSystemDevices())
			if(d instanceof Adam5000Device){
				device = (Adam5000Device) d;
				break;
			}
		if(device == null)
			fail("Adam5000Device NOT in Plant!");
		
		/*
		Device pump = Device.fromID("pump");
		if(pump == null)
			fail("pump not in plant");
		*/
		
		Endpoint adamEndpoint = device.getActiveEndpoint(0, 0);
		System.out.println(adamEndpoint.toString());
		
		@SuppressWarnings("unchecked")
		WriteCommand wc = new WriteCommand("WriteCommand", adamEndpoint, new AnalogueAmount(ElectricCurrent.class, (Amount<ElectricCurrent>)Amount.valueOf("10 mA")));
		
		DeviceCommandGenerationList dcgl = new DeviceCommandGenerationList(device);
		dcgl.add(wc);
		
		try {
			WriteCommand expectedCommand = new WriteCommand(wc.getCommandID(), wc.getTimestamp(), device.getEndpointById("RS232"), new InformationAmount(new ASCIIString("#01S0C010.000\r").toByteArray()));
			System.out.println(writeCommandToString(expectedCommand));
			System.out.println(writeCommandToString((WriteCommand)dcgFactory.getDeviceCommandGenerator(device).generateCommands(dcgl)[0].getSolvedDeviceCommand()));
			assertEquals(expectedCommand, dcgFactory.getDeviceCommandGenerator(device).generateCommands(dcgl)[0].getSolvedDeviceCommand());
		} catch (InvalidCommandException e) {
			fail("Invalid Command!");
		} catch (DeviceNotFoundException e) {
			fail("Device NOT found!");
		}
		
	}

	//TODO TMP
	private static String writeCommandToString(WriteCommand command){
		StringBuilder sb = new StringBuilder();
		for (byte b : ((InformationAmount)command.getValue()).getBytes()) {
			sb.append((char)b);
		}
		String value = sb.toString();
		return command.getCommandID() + command.getTimestamp() + " - " + command.getTargetDevice() + ":" + command.getTargetPort() + "=\"" + value + "\"" + ((InformationAmount)command.getValue()).getQuantity().getSimpleName();
	}
}
package tests.testbench;

import java.io.IOException;
import java.util.UUID;

import javax.measure.quantity.ElectricPotential;

import plantadapter.PlantAdapter;
import plantadapter.commands.ReadCommand;
import plantadapter.excpts.CommandFailureException;
import plantadapter.excpts.PlantConfigurationException;
import plantmodel.Endpoint;
import plantmodel.datatypes.ASCIIString;
import plantmodel.dt80.DT80Utils.Info.ScheduleIds;
import plants.MulePlant;
import quantities.ORP;
import quantities.PH;
import testbench.IPServerActivationThread;
import testbench.PlantAdapterTestbench;
import utils.PlantObserver;

public class Testbench {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws PlantConfigurationException 
	 * @throws InterruptedException 
	 * @throws CommandFailureException 
	 */
	public static void main(String[] args) throws IOException, PlantConfigurationException, InterruptedException, CommandFailureException {
		
		IPServerActivationThread serverActivator = new IPServerActivationThread();		
		PlantAdapterTestbench tb = new PlantAdapterTestbench(serverActivator);
		// Creates Mule Plant
		MulePlant plant = new MulePlant(serverActivator.getServerInetAddres().getHostName(), serverActivator.getServerLocalPort());
		PlantAdapter p = PlantAdapter.newInstance(plant.getId(), plant.getDevices(), new PlantObserver(System.out, System.err));
		// ReadCommand @orp_ox
		ReadCommand r = new ReadCommand(UUID.randomUUID().toString(), Endpoint.fromID("orp_ox", "SOURCE"), ORP.class);
		p.sendCommand(r);
		System.out.println(ASCIIString.fromByteArray(tb.getCommandChecker().getCommand()));
		// ReadCommand @orp_anox
		r = new ReadCommand(UUID.randomUUID().toString(), Endpoint.fromID("ph_ox", "SOURCE"), ElectricPotential.class);
		p.sendCommand(r);
		System.out.println(ASCIIString.fromByteArray(tb.getCommandChecker().getCommand()));
		
		Thread.sleep(2000);
		
		// Dummy results
		tb.getInputGenerator().input(ScheduleIds.IMMEDIATE, 10);
		tb.getInputGenerator().input(ScheduleIds.IMMEDIATE, 15);
	}
}
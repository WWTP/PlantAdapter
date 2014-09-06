package tests.commandtreebuilder;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import plantadapter.commands.DeviceCommand;
import tests.commandtreebuilder.uut.IAggregatedDeviceCommand;
import tests.dependencesolver.DummyDevice;

public class DummyADC implements IAggregatedDeviceCommand {
	
	private static Map<String, DummyDeviceCommand> cmds = new HashMap<String, DummyDeviceCommand>();
	
	private DummyDeviceCommand solver;
	private DummyDeviceCommand[] solved;
	
	/*
	public DummyADC(String solver, boolean duplicateSolver, String... solved) throws ParserConfigurationException {
		if (!cmds.containsKey(solver))
			cmds.put(solver, new DummyDeviceCommand(new DummyDevice(solver)));
		this.solver = cmds.get(solver);
		this.solved = new DummyDeviceCommand[solved.length];
		int index = 0;
		for (String devId : solved) {
			if (!cmds.containsKey(devId))
				cmds.put(devId, new DummyDeviceCommand(new DummyDevice(devId)));
			this.solved[index] = cmds.get(devId);
			index++;
		}
	}
	*/

	public DummyADC(DummyDeviceCommand solver, DummyDeviceCommand... solved) throws ParserConfigurationException {
		this.solver = solver;
		this.solved = solved;
	}

	@Override
	public DeviceCommand getSolvedDeviceCommand() {
		return this.solver;
	}

	@Override
	public DeviceCommand[] getAggregatedCommands() {
		return this.solved;
	}
}
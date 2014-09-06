package plantadapter.commands.sched;

import java.util.ArrayList;
import java.util.List;

import plantadapter.IPlant;
import plantadapter.PlantAdapter;

import plantadapter.cmdgen.DeviceCommandGenerationController;
import plantadapter.cmdgen.IDeviceCommandTree;


import plantadapter.commands.Command;
import plantadapter.commands.ScheduleDefinitionCommand;
import plantadapter.communication.ISink;

import plantadapter.schedules.ScheduledOperation;
import plantadapter.schedules.SystemScheduleDefinition;

import plantmodel.Device;

// TODO
import plantmodel.dt80.DT80Device;
import plantmodel.dt80.DT80Schedule;
import plantmodel.dt80.DT80ScheduleDefinition;
import plantmodel.dt80.DT80Utils.Info.ScheduleFrequencyUnits;

public class PlantScheduleDefinitionCommandExecutor implements ISink<Command> {
	
	private IPlant plant;
	
	public PlantScheduleDefinitionCommandExecutor(PlantAdapter plant) {
		this.plant = plant.getPlantModel();
	}

	@Override
	public void put(Command message) throws Exception {
		
		if (!(message instanceof ScheduleDefinitionCommand))
			throw new IllegalArgumentException(); // TODO
		
		ScheduleDefinitionCommand cmd = (ScheduleDefinitionCommand)message;
		
		// TODO Individua il/i dispositivo/i che devono possono materialmente la schedule (se presenti)...
		
		Device scheduleExecutor = (DT80Device)Device.fromID("DT80"); // TODO
		
		// TODO Crea delle DeviceScheduledOperation per ciascun dispositivo, o delle SystemScheduledOperation altrimenti
		
		List<ScheduledOperation> scheduledOps = new ArrayList<ScheduledOperation>();
		
		// TODO Caso specifico del DT80 (occorre renderlo trasparente, rimuovendo le dipendenze dai dispositivi particolari)
		
		DT80Device dt80 = (DT80Device)scheduleExecutor;
		
		//
		
		ScheduleFrequencyUnits unit = ScheduleFrequencyUnits.getMaximumUnit(cmd.getFrequency());
		
		DT80ScheduleDefinition dt80ScheduleDef = new DT80ScheduleDefinition(
				dt80.getAvailableScheduleId(), cmd.getScheduleId().length() > 8 ? cmd.getScheduleId().substring(0, 8) : cmd.getScheduleId(),
				ScheduleFrequencyUnits.getTimesForUnit(unit, cmd.getFrequency()), unit,
				null); // TODO
		
		DT80Schedule schedule = new DT80Schedule(dt80, dt80ScheduleDef);
		dt80.addSchedule(schedule);
		
		schedule.setScheduledCommands(cmd.getScheduledCommands());
		// Ottiene la maschera degli input associati alla Schedule
		IDeviceCommandTree[] trees = new DeviceCommandGenerationController(this.plant.getCommandLogger()).getCommandTrees(cmd.getScheduledCommands()); // TODO DCGController a livello di Plant (?)
		schedule.setScheduledInputMask(trees[0].getMask()); // TODO
		
		scheduledOps.add(schedule);
		
		// TODO Invia il comando per attuare materialmente la schedule...
		
		// Crea la Schedule di sistema
		
		SystemScheduleDefinition scheduleDefinition = new SystemScheduleDefinition(
				cmd.getScheduleId(),
				scheduledOps.toArray(new ScheduledOperation[0]),
				cmd.getStart(),
				cmd.getFrequency()
		);
		
		schedule.setSystemScheduleDefinition(scheduleDefinition); // TODO
	}
}
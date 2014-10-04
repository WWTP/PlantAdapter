package plantadapter.commands.sched;

import plantadapter.IPlant;
import plantadapter.PlantAdapter;

import plantadapter.commands.Command;
import plantadapter.commands.ScheduleSubscriptionCommand;
import plantadapter.communication.ISink;
import plantadapter.schedules.SystemScheduleDefinition;

public class PlantScheduleSubscriptionCommandExecutor implements ISink<Command> {
	
	private IPlant plant;
	
	public PlantScheduleSubscriptionCommandExecutor(PlantAdapter plant) {
		this.plant = plant.getPlantModel();
	}

	@Override
	public void put(Command message) throws Exception {
		
		if (!(message instanceof ScheduleSubscriptionCommand))
			throw new IllegalArgumentException();
		
		ScheduleSubscriptionCommand subscription = (ScheduleSubscriptionCommand)message;
		// Aggiunge chi ha inviato il comando agli Observers registrati per la Schedule
		SystemScheduleDefinition schedule = SystemScheduleDefinition.byId(subscription.getScheduleID());
		schedule.subscribe(
				plant.getCommandLogger().getInputCallbackReceiver(subscription), 
				plant.getCommandLogger().getErrorCallbackReceiver(subscription));

		plant.getCommandLogger().unlog(subscription);
	}
}
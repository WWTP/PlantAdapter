package plantadapter.commands;

import java.util.Date;

import plantadapter.annotations.CommandInfo;
import plantadapter.commands.sched.PlantScheduleSubscriptionCommandExecutor;

@CommandInfo(commandExecutorClass = PlantScheduleSubscriptionCommandExecutor.class)
public class ScheduleSubscriptionCommand extends Command { // TODO

	private final String scheduleID;
	
	public ScheduleSubscriptionCommand(String commandID, Date timestamp, String scheduleID) {
		super(commandID, timestamp);

		this.scheduleID = scheduleID;
	}
	
	public ScheduleSubscriptionCommand(String commandID, String scheduleID) {
		this(commandID, new Date(), scheduleID);
	}
	
	public String getScheduleID() {
		return this.scheduleID;
	}
}
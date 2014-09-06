package plantadapter.commands;

import java.util.Date;

import plantadapter.annotations.CommandInfo;
import plantadapter.commands.sched.PlantScheduleDefinitionCommandExecutor;

@CommandInfo(commandExecutorClass = PlantScheduleDefinitionCommandExecutor.class)
public class ScheduleDefinitionCommand extends Command {
	
	private String scheduleId;
	private DeviceCommand[] scheduledCommands;
	private Date start;
	private long frequency;

	public ScheduleDefinitionCommand(String commandID, Date timestamp,
			String scheduleId, DeviceCommand[] scheduledCommands, Date start, long frequency) {
		super(commandID, timestamp);

		this.scheduledCommands = scheduledCommands.clone();
		this.start = start;
		this.frequency = frequency;
	}

	public ScheduleDefinitionCommand(String commandID, String scheduleId, DeviceCommand[] scheduledCommands, Date start, long frequency) {
		this(commandID, new Date(), scheduleId, scheduledCommands, start, frequency);
	}
	
	public String getScheduleId() {
		return scheduleId;
	}	
	
	public DeviceCommand[] getScheduledCommands() {
		return scheduledCommands;
	}

	public long getFrequency() {
		return frequency;
	}
	
	public Date getStart() {
		return start;
	}
}
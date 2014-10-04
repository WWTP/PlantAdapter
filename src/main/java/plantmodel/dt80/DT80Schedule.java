package plantmodel.dt80;

import plantadapter.commands.DeviceCommand;
import plantadapter.commands.dev.IInputMask;
import plantadapter.schedules.DeviceScheduledOperation;
import plantadapter.schedules.ScheduleSupplierDevice;
import plantadapter.schedules.SystemScheduleDefinition;

/**
 * <p>Rappresenta una <i>schedule</i> in quanto entità attiva all'interno del <code>DT80</code></p>.
 * @author JCC
 *
 */
public class DT80Schedule implements DeviceScheduledOperation { // TODO In realtà sarebbe meglio un Adapter...

	private final DT80Device dt80;
	private final DT80ScheduleDefinition scheduleDefinition;
	
	private SystemScheduleDefinition systemScheduleDefinition; // TODO In realtà sarebbe meglio un Adapter...
	private DeviceCommand[] equivalentCommands; // TODO In realtà sarebbe meglio un Adapter...
	private IInputMask scheduledInputMask; // TODO In realtà sarebbe meglio un Adapter...
	
	public DT80Schedule(DT80Device dt80, DT80ScheduleDefinition scheduleDefinition) { // TODO Impedisci creazione con scheduleDefinition nulla (!) - Lasciato così per un Test...
		this.dt80 = dt80;
		this.scheduleDefinition = scheduleDefinition;
	}
	
	// Accessors
	
	public DT80ScheduleDefinition getDT80ScheduleDefinition() {
		return this.scheduleDefinition;
	}
	
	public void setSystemScheduleDefinition(SystemScheduleDefinition systemScheduleDefinition) {
		if (this.systemScheduleDefinition == null)
			this.systemScheduleDefinition = systemScheduleDefinition;
		else throw new IllegalStateException("SystemScheduleDefinition impostabile una sola volta.");
	}
	
	public void setScheduledCommands(DeviceCommand[] equivalentCommands) {
		if (this.equivalentCommands == null)
			this.equivalentCommands = equivalentCommands;
		else throw new IllegalStateException("EquivalentCommands impostabili una sola volta.");
	}
	
	public void setScheduledInputMask(IInputMask scheduledInputMask) {
		this.scheduledInputMask = scheduledInputMask;
	}
	
	// ScheduledOperation

	@Override
	public SystemScheduleDefinition getScheduleDefinition() {
		return this.systemScheduleDefinition;
	}

	@Override
	public DeviceCommand[] getEquivalentCommands() {
		return this.equivalentCommands;
	}
	
	// DeviceScheduledOperation
	
	@Override
	public ScheduleSupplierDevice getScheduleSupplierDevice() {
		return this.dt80;
	}

	@Override
	public IInputMask getScheduledInputMask() {
		return this.scheduledInputMask;
	}

	/* TODO Il DT80 ha nel proprio concetto di Schedules anche cose scatenate da "trigger"
	 * diversi da semplici intervalli di tempo. Attualmente queste cose esulano dal nostro dominio
	 * ma in futuro potrà essere opportuno considerarle. Notare che in questo caso il fatto che
	 * DT80Schedule estenda Schedule è improprio: ad una Schedule del DT80 PUO' corrispondere una schedule
	 * del sistema, ma non è detto (a meno di non modificare di pari passo la semantica di entrambi i concetti).
	 */
	
	/*	
		// TODO Usare l'enumeratore o direttamente il tipo delle sottoclassi (?)
		public enum ScheduleTriggerTypes {
			TimeInterval,
			ExternalEvent,
			InternalEvent,
			PollCommand; // TODO Utilizzato per la Schedule RX...
		}
		
		public interface DT80ScheduleTrigger {
			// TODO Usare l'enumeratore o direttamente il tipo delle sottoclassi (?)
			public ScheduleTriggerTypes getTriggerType();
			public <T extends DT80ScheduleTrigger> T getTrigger();
		}
	 */
	
	// TODO Altri triggers...
}

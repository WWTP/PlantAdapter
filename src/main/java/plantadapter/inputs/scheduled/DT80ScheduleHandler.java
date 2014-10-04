package plantadapter.inputs.scheduled;

import plantadapter.commands.dev.IInputMask;
import plantadapter.communication.ISink;

import plantadapter.parsers.IInputParser;
import plantadapter.parsers.InputParserFactory;

import plantadapter.schedules.DeviceScheduledOperation;

import plantmodel.datatypes.ASCIIString;

import plantmodel.dt80.DT80Device;
import plantmodel.dt80.DT80Schedule;
import plantmodel.dt80.DT80Utils;
import plantmodel.dt80.DT80Utils.Info.FixedFormatFields;
import plantmodel.dt80.DT80Utils.Info.ScheduleIds;

public class DT80ScheduleHandler implements ISink<ASCIIString> {
	
	private final DT80Device dt80;
//	// Cache delle "maschere" associate alle Schedules
//	private final Map<DT80Schedule, IInputMask> maskMap;
	
	public DT80ScheduleHandler(DT80Device dt80) {
		this.dt80 = dt80;
//		this.maskMap = new HashMap<DT80Schedule, IInputMask>();
	}
	
//	private IInputMask getMask(DT80Schedule sched) {
//		// Ottiene la maschera dalla cache se già presente...
//		if (this.maskMap.containsKey(sched))
//			return this.maskMap.get(sched);
//		else {
//			// Ottiene i comandi associati a questa ScheduledOperation
//			DeviceCommand[] cmds = sched.getEquivalentCommands();
//			IDeviceCommandTree[] trees = DeviceCommandGenerationController.getInstance().getCommandTrees(cmds);
//			// TODO Controlli consistenza albero con Schedule passata
//			IInputMask mask = trees[0].getMask();
//			this.maskMap.put(sched, mask);
//			return mask;
//		}
//	}
	
	private DT80Schedule getReceivedSchedule(ASCIIString message) {
		ScheduleIds schedId;
		if ((schedId = ScheduleIds.parse(DT80Utils.readFixedFormat(message, FixedFormatFields.SCHEDULE))) == ScheduleIds.IMMEDIATE)
			; // TODO Errore! Ricevuta lettura anziché schedule!!!
		// Verifica che la schedule sia definita in questo DT80...
		for (DeviceScheduledOperation sched : this.dt80.getDefinedSchedules()) {
			if (((DT80Schedule)sched).getDT80ScheduleDefinition().getScheduleId() == schedId)
				return (DT80Schedule)sched;
		}

		return null; // TODO Ricevuto risultato schedule non presente (viene ignorato...)
	}

	@Override
	public void put(ASCIIString message) throws Exception {
		DT80Schedule schedule = this.getReceivedSchedule(message);
		
		if (schedule != null) {
			// Ottiene la "maschera" relativa all'input ricevuto
			IInputMask mask = schedule.getScheduledInputMask(); //this.getMask(schedule);
			IInputParser parser = InputParserFactory.getInputParser(mask.asMask().getSourceDevice());
			// Invio risultato agli osservatori iscritti (in XML)
			schedule.getScheduleDefinition().scheduledOperationExecuted(schedule, parser.parse(mask, message));
		}
	}
}
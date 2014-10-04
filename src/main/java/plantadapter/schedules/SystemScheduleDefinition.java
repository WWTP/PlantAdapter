package plantadapter.schedules;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import plantadapter.IErrorCallbackReceiver;
import plantadapter.IInputCallbackReceiver;
import plantadapter.commands.DeviceCommand;
import plantadapter.results.ScheduledResult;
import plantadapter.results.SingleResult;

/* Nel caso più generale una Schedule offerta dal PlantAdapter è definita da una lista di comandi equivalenti
 * ai quali l'esecuzione della Schedule corrisponde. A tali comandi (e quindi alla definizione della Schedule)
 * corrispondono delle "azioni" (ScheduledOperation) compiute al fine di eseguire tali comandi alla scadenza prefissata:
 * queste possono essere supportate dai dispositivi oppure messe in atto dal sistema al fine di fornire il servizio
 * richiesto. Sia la parte di ricezione degli input che la parte di esecuzione delle Schedule genereranno eventi che,
 * una volta che tutte le ScheduledOperation associate ad una SystemScheduleDefinition saranno state eseguite,
 * provocheranno l'invio (a tutti gli Observer registrati) del risultato della Schedule.
 * 
 * I dispositivi in grado di eseguire autonomamente delle ScheduledOperation conterranno come propria informazione
 * di configurazione la presenza di tale ScheduledOperation. Sarà responsabilità della classe SystemScheduleConfiguration
 * reperire da questi l'informazione (che contiene anche l'ID della schedule a livello di sistema) e tradurla (eventualmente
 * unita ad altre ScheduledOperation, definite o meno da dispositivi) in parte di una SystemScheduleDefinition.
 */

public class SystemScheduleDefinition {
	
	private static Map<String, SystemScheduleDefinition> schedules = new HashMap<String, SystemScheduleDefinition>();
	
	public static SystemScheduleDefinition byId(String id) {
		return schedules.get(id);
	}
	
	public static void remove(SystemScheduleDefinition ssd) {
		schedules.remove(ssd);
	}
	
	public static void remove(String id) {
		schedules.remove(SystemScheduleDefinition.byId(id));
	}
	
	// ID di definizione di questa Schedule
	private final String schedId;
	// Operazioni schedulate che eseguono i "comandi equivalenti" a cui corrisponde la Schedule (in generale sia letture che scritture)
	private final ScheduledOperation[] schedOps;
	// Comandi equivalenti eseguiti dalle operazioni schedulate (cache)
	private final DeviceCommand[] schedCmds;
	// Istante di inizio della schedule
	private final Date schedStart;
	// Frequenza della schedules (mS)
	private final long schedFreq;
	//
	private List<IInputCallbackReceiver> inputObservers;
	private List<IErrorCallbackReceiver> errorObservers;
	//
	private final ScheduledOperationLogger scheduledOperationLogger;
	
	public SystemScheduleDefinition(String scheduleId, ScheduledOperation[] scheduledOperations,
			Date start, long frequency) {

		if (schedules.containsKey(scheduleId)) throw new IllegalArgumentException("ID Schedule deve essere univoco."); // TODO
		
		List<DeviceCommand> temp = new LinkedList<DeviceCommand>();
		this.schedId = scheduleId;
		this.schedOps = scheduledOperations;
		// Aggiunge i comandi alla cache...
		for (ScheduledOperation schedOp : this.schedOps) {
			temp.addAll(Arrays.asList(schedOp.getEquivalentCommands()));
		}
		this.schedCmds = temp.toArray(new DeviceCommand[0]);
		this.schedStart = start;
		this.schedFreq = frequency;
		//
		this.inputObservers = new LinkedList<IInputCallbackReceiver>();
		this.errorObservers = new LinkedList<IErrorCallbackReceiver>();
		//
		this.scheduledOperationLogger = new ScheduledOperationLogger();
		//
		schedules.put(this.schedId, this);
	}
	
	public SystemScheduleDefinition(String scheduleId, ScheduledOperation[] scheduledOperations, long frequency) {
		this(scheduleId, scheduledOperations, new Date(), frequency);
	}
	
	// ACCESSORS
	
	public String getScheduleId() {
		return this.schedId;
	}
	
	public DeviceCommand[] getScheduleCommands() {
		return this.schedCmds;
	}
	
	public Date getScheduleStartInstant() {
		return this.schedStart;
	}
	
	public long getScheduleFrequency() {
		return this.schedFreq;
	}
	
	/* Una SystemScheduleDefinition genera un evento ai propri "subscribers" ogni volta che
	 * tutte le ScheduledOperation ad essa associate sono state eseguite.
	 */
	
	private void sendScheduleResults(ScheduledResult result) {
		for (IInputCallbackReceiver obs : this.inputObservers) {
			obs.sendInput(result);
		}
	}
	
	/**
	 * <p>Metodo invocato dalle <code>SystemScheduledOperation</code> associate a questa <code>SystemScheduleDefinition</code>,
	 * con indicazione del mittente e del frammento di XML risultante dall'operazione.</p>
	 * @param executedOp
	 * @param result
	 */
	public void scheduledOperationExecuted(ScheduledOperation executedOp, SingleResult[] opResults) {
		if (this.scheduledOperationLogger.update(executedOp, opResults)) {
			this.sendScheduleResults(this.scheduledOperationLogger.getResult());
			this.scheduledOperationLogger.reset();
		}
	}
	
	public synchronized void subscribe(IInputCallbackReceiver inputObs, IErrorCallbackReceiver errorObs) {
		this.inputObservers.add(inputObs);
		this.errorObservers.add(errorObs);
	}
	
	public synchronized void unsubscribe(IInputCallbackReceiver inputObs, IErrorCallbackReceiver errorObs) {
		this.inputObservers.remove(inputObs);
		this.errorObservers.remove(errorObs);
	}
	
	private class ScheduledOperationLogger {
		
		private List<ScheduledOperation> executedOps = new LinkedList<ScheduledOperation>();
		private List<SingleResult> results = new LinkedList<SingleResult>();
		
		public boolean check() {
			return this.executedOps.size() == (schedOps.length) && results.size() == getScheduleCommands().length;
		}
		
		public boolean update(ScheduledOperation executedOp, SingleResult[] results) {
			if (this.executedOps.contains(executedOp))
				throw new IllegalStateException();
			// Aggiorna stato interno
			this.executedOps.add(executedOp);
			this.results.addAll(Arrays.asList(results));
			// Verifica se tutte le ScheduledOperation sono state eseguite
			return this.check();
		}
		
		public ScheduledResult getResult() {
			return new ScheduledResult(schedId, this.results.toArray(new SingleResult[0]));
		}
		
		public void reset() {
			this.executedOps.clear();
			this.results.clear();
		}
	}
}
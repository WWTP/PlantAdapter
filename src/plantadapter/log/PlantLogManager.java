package plantadapter.log;

import java.util.List;

import plantadapter.ILogCallbackReceiver;

/* TODO Inizialmente si pensava ad una gestione differenziata
 * per messaggi di log relativi a comandi e schedules in base
 * al particolare receiver, al momento si usa un log generale
 * nel senso che i messaggi di log vengono propagati a tutti
 * i receiver di comandi e schedules.
 */

public class PlantLogManager {

	private List<ILogCallbackReceiver> loggers;
	
	public void sendLog(String log) {
		for (ILogCallbackReceiver logger : loggers) {
			logger.sendLog(log);
		}
	}
}
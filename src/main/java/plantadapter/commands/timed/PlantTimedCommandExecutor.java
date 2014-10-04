package plantadapter.commands.timed;

import plantadapter.communication.ISink;

/* TODO
 * 
 * Utilizzare DelayedQueue e unico thread che legge
 * ciò che viene posto in essa. Gli inserimenti possono
 * avvenire dall'esterno oppure dal thread stesso (che
 * re-inserisce la timed facendo decrescere il "time-to-live"
 * finché non diventa 0.
 * 
 * Il thread legge dalla coda, invia il comando
 * alla coda dei DeviceCommand e poi lo re-inserisce
 * nella propria coda. Non è possibile un deadlock
 * in quanto l'inserimento è senza attesa (eccezione
 * se la coda è piena).
 */

public class PlantTimedCommandExecutor implements ISink { // TODO ISink<TimedCommand>

	@Override
	public void put(Object message) throws Exception {
		// TODO Auto-generated method stub
		
	} 
}
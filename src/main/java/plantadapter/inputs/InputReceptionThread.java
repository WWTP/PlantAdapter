package plantadapter.inputs;

import java.io.IOException;

import plantadapter.communication.ExecutionThread;
import plantadapter.communication.ISink;
import plantadapter.communication.ISource;

/* TODO Astrazione comune per i thread?
 * Funzionalità è analoga a CommandExecutionThread & co., in particolare
 * per il ruolo del "Recognizer" e degli "Handler", anche se ciò che deve
 * essere passato non è ottenuto da una Mailbox ma da un IInputSource...
 * 
 * Verificare bene la creazione da parte dei livelli superiori (anche nel
 * caso della generazione di comandi!).
 */

/**
 * <p>Un <code>InputReceptionThread</code> gestisce la ricezione di input
 * da una data sorgente e la loro elaborazione da parte di una data istanza
 * di <code>IInputRecognizer</code>.</p>
 * @author JCC
 *
 */
public class InputReceptionThread extends ExecutionThread<byte[]> {

	public InputReceptionThread(ISource<byte[]> src, ISink<byte[]> sink) {
		super(src, sink);
	}

	@Override
	protected void exceptionsHandler(Exception e, byte[] item) {
		// TODO Auto-generated method stub
		
	}
}
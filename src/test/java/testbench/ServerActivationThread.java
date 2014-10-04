package testbench;

import java.io.InputStream;
import java.io.OutputStream;

abstract class ServerActivationThread extends Thread
{
	@Override
	public void run() {

		this.acceptRequest();
		
		// Nota: notify() non necessaria perché una volta
		// terminata l'esecuzione non c'è nessun processo
		// che detiene il lock dell'oggetto (i processi
		// che hanno eseguito la wait() vengono liberati).
	}
	
	/**
	 * 
	 * @return true se si è ancora in attesa di una richiesta di connessione,
	 * false se essa è stata accettata con successo od è fallita.
	 */
	protected abstract boolean waiting();
	
	private void checkWaiting() {
		synchronized (this) 
		{
			while (this.waiting()) {
				try {
					this.wait();
				} 
				catch (InterruptedException e) {
					continue;
				}
			}
		}
	}
	
	public InputStream getInputStream() {
		this.checkWaiting();
		return this.getInputStreamImpl();
	}
	
	public OutputStream getOutputStream() {
		this.checkWaiting();
		return this.getOutputStreamImpl();
	}
	
	/**
	 * Se non è possibile accettare correttamente la richiesta,
	 * la classe derivata deve fare si che getInputStream() e
	 * getOutputStream() restituiscano entrambe null. Non deve
	 * essere sollavata alcuna eccezione.
	 */
	protected abstract void acceptRequest();
	
	/**
	 * 
	 * @return null se non è possibile ottenere lo stream.
	 */
	protected abstract InputStream getInputStreamImpl();

	/**
	 * 
	 * @return null se non è possibile ottenere lo stream.
	 */
	protected abstract OutputStream getOutputStreamImpl();
}
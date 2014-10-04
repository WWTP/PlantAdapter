package testbench;

import java.io.IOException;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import plantmodel.dt80.DT80Utils;

import tests.plantadapter.CommandChecker;
import tests.plantadapter.InputGenerator;

public class PlantAdapterTestbench {
	
	/*
	 * 1 istanzio testbench
	 * 2 un thread si mette in attesa di una connessione
	 * 3 ottengo dal testbench i dati sulla connessione, in modo
	 * da poter istanziare correttamente il modello
	 * 4 istanzio un plantadapter
	 * 5 ottengo commandchecker e input source dal testbench
	 * 
	 */
	
	private CommandChecker checker;
	private InputGenerator input;
	
	private ServerActivationThread serverActivator;
	private Lock lock;
	private Condition condition;
	
	public PlantAdapterTestbench(ServerActivationThread serverActivator) throws IOException {
		
		this.serverActivator = serverActivator;
		// usati per sincronizzare il thread che accetta la richiesta  di connessione con quello corrente
		this.lock = new ReentrantLock();
		this.condition = lock.newCondition();
		// sincronizzazione con thread attivazione e lancio
		this.serverActivator.start();
	}
	
	public void waitService() throws InterruptedException {
		// attende che il thread di servizio abbia
		// accettato la richiesta
		this.lock.lock();
		this.condition.await();
	}
	
	public CommandChecker getCommandChecker() throws InterruptedException {
		//this.waitService();
		if (this.checker == null)
			this.checker = new CommandChecker(this.serverActivator.getInputStream(), DT80Utils.Info.InputFormatDescriptor.getInstance()); // TODO Cablato su DT80...
		return this.checker;
	}
	
	public InputGenerator getInputGenerator() throws InterruptedException {
		//this.waitService();
		if (this.input == null)
			this.input = new InputGenerator(this.serverActivator.getOutputStream());
		return this.input;
	}
}
package plantadapter.results;

import java.util.Date;

import plantadapter.commands.Command;

public class ImmediateResult extends Result { // TODO
	
	// Instance Fields
	
	private Command command; // TODO Gestione relativa a Batch/Timed...
	private SingleResult[] results; // TODO

	// Constructors
	
	public ImmediateResult(Date timestamp,/* Command command,*/ SingleResult... results) {
		super(timestamp);
		
		// Inizializzazione Campi
		//this.command = command;
		// TODO
		this.results = results;
	}

	public ImmediateResult(/*Command command,*/ SingleResult... results) {
		this(new Date(),/* command,*/ results);
	}
	
//	public Command getCommand() {
//		return this.command;
//	}
	
	public SingleResult[] getResults() { // TODO Fatorizzare (?)
		return this.results; // TODO Eventualmente copia...
	}
	
	// Object
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(/* COMMAND*/ " " + super.getTimestamp() + " { ");
		for (SingleResult result : this.getResults()) {
			sb.append(result.toString() + ", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(" }");
		
		return sb.toString();
	}
}
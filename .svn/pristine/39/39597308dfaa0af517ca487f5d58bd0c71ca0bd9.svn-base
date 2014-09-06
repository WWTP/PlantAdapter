package plantadapter.commands;

import java.util.Date;

import plantmodel.Endpoint;

// TODO Valutare questo approccio, in particolare nel caso di dispositivi "radice"...x

/**
 * </p>
 * Rappresentano una sequenza di comandi diretta ad una stessa porta di uno stesso dispositivo,
 * istruendo i <code>DCG</code> del fatto che essi sono tra loro correlati nel senso che servono a mettere in pratica
 * un'interazione tra i due dispositivi (e.g. richiesta di un dato e risposta). Tali sequenze si compongono
 * tipicamente di un <code>WriteCommand</code> seguito da un <code>ReadCommand</code>, ma questo non è strettamente necessario.
 * </p>
 * <p>
 * Si ipotizza che i comandi di una <em>transaction</em> siano gestiti <u>insieme</u> da un <code>DCG</code>.
 * </p>
 * @author JCC
 *
 */
public class TransactionCommand extends PortCommand {

	private PortCommand[] commands;
	
	public TransactionCommand(String commandID, Date timestamp, Endpoint targetPort, PortCommand... commands) {
		super(commandID, timestamp, targetPort);
		
		// TODO I TransactionCommand NON POSSONO CONTENERE altri TransactionCommand...
		
		// TODO
		if (commands.length < 2) throw new IllegalArgumentException("TransactionCommand: commands.length < 2");
		
		for (PortCommand cmd : commands) {
			if (cmd.getTargetDevice() != super.getTargetDevice() || cmd.getTargetPort() != targetPort)
				throw new IllegalArgumentException("TransactionCommand: cmd.getTargetDevice() != super.getTargetDevice() || cmd.getTargetPort() != targetPort");
		}
		
		this.commands = commands;
	}

	public TransactionCommand(String commandID, Endpoint targetPort, PortCommand... commands) {
		this(commandID, new Date(), targetPort, commands);
	}
	
	public PortCommand[] getCommands() {
		return this.commands;
	}
}
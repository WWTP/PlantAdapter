package plantadapter.commands;

import java.util.Date;

import plantadapter.annotations.CommandInfo;
import plantadapter.commands.batch.PlantBatchCommandExecutor;

/**
 * Lista di comandi da eseguire in sequenza. I singoli DCG hanno l'obbligo di garantire
 * la sequenzialità ma di accorpare i comandi nel minimo numero di istruzioni loro possibile.
 * @author JCC
 *
 */
@CommandInfo(commandExecutorClass = PlantBatchCommandExecutor.class)
public class BatchCommand extends Command {

	private final Command[] commands;
	
	public BatchCommand(String commandID, Date timestamp , Command... commands) {
		super(commandID, timestamp);
		
		this.commands = commands;
	}

	public BatchCommand(String commandID, Command... commands) {
		this(commandID, new Date(), commands);
	}

	public Command[] getCommands() {
		return this.commands;
	}
}
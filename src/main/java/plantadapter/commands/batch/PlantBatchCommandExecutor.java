package plantadapter.commands.batch;

import java.util.ArrayList;
import java.util.List;

import plantadapter.PlantAdapter;
import plantadapter.commands.BatchCommand;
import plantadapter.commands.Command;
import plantadapter.commands.DeviceCommand;
import plantadapter.communication.ISink;

// TOOD

public class PlantBatchCommandExecutor implements ISink<Command> {

	private PlantAdapter plant;
	
	public PlantBatchCommandExecutor(PlantAdapter plant) {
		this.plant = plant;
	}
	
	@Override
	public void put(Command message) throws Exception {
		
		throw new UnsupportedOperationException();
		
		/*
		
		BatchCommand cmd = (BatchCommand)message;
		List<DeviceCommand> devCmdList = new ArrayList<DeviceCommand>(cmd.getCommands().length);
		
		/* TODO Qua si hanno diverse possibilità: o si accede direttamente ai singoli moduli inviando
		 * i comandi opportuni a seconda del tipo (in questo caso, valutare questo approccio anche
		 * per generare gli status command...) oppure nuovamente gli si invia al PlantAdapter (di cui
		 * ho un riferimento). In questo secondo caso però occorre avere un modo per inviare i DeviceCommand
		 * in blocco all'apposito Executor (e.g. creando un comando apposito che sia diretto a PlantDCExecutor).
		 */
		
		// Transaction --> ?
		
		/*
		for (Command c : cmd.getCommands()) {
			// TODO
		}*/
	}
}
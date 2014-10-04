package plantadapter.commands.diagn;

import plantadapter.CommandLogger;
import plantadapter.IPlant;
import plantadapter.PlantAdapter;
import plantadapter.commands.Command;
import plantadapter.commands.CommandDiagnosticCommand;
import plantadapter.commands.CommandsDiagnosticCommand;
import plantadapter.commands.DiagnosticCommand;
import plantadapter.communication.ISink;
import plantadapter.results.CommandDiagnosticResult;
import plantadapter.results.CommandsDiagnosticResult;
import plantadapter.results.SingleCommandDiagnosticResult;

public class DiagnosticCommandExecutor implements ISink<Command> {

	private IPlant plant;
	
	public DiagnosticCommandExecutor(PlantAdapter plant) {
		this.plant = plant.getPlantModel();
	}
	
	@Override
	public void put(Command message) throws Exception {
		if(! (DiagnosticCommand.class.isAssignableFrom(message.getClass()) ))
			throw new IllegalArgumentException();
		
		DiagnosticCommand cmd = (DiagnosticCommand) message;
		
		CommandLogger cl = plant.getCommandLogger();
		
		switch(cmd.getSubject()) {
			case COMMANDS: {
				if(!(message instanceof CommandsDiagnosticCommand))
					throw new IllegalArgumentException();
				
				Command[] cmds = cl.getCommands();
				SingleCommandDiagnosticResult[] cd = new SingleCommandDiagnosticResult[cmds.length];
				
				int i = 0;
				for(Command c : cmds){
					cd[i] = new SingleCommandDiagnosticResult(c, cl.getCommandInfo(c).getState());
					
					i++;
				}
				
				// invio risultato
				cl.getInputCallbackReceiver(cmd).sendInput(
						new CommandsDiagnosticResult((CommandsDiagnosticCommand) cmd, cd)
				);
				
				break;
			}
			case COMMAND: {
				if(!(cmd instanceof CommandDiagnosticCommand))
					throw new IllegalArgumentException();				
				
				cl.getInputCallbackReceiver(cmd).sendInput(
						new CommandDiagnosticResult(
								(CommandDiagnosticCommand) cmd,
								((CommandDiagnosticCommand)cmd).getTarget(),
								cl.getCommandInfo(((CommandDiagnosticCommand)cmd).getTarget()).getState()
						)
				);
				
				break;
			}
			default:
				throw new UnsupportedOperationException();
		}
		
		cl.unlog(cmd);
	}

}

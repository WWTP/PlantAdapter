package plantadapter.dcgs.impl.adam;

import plantadapter.commands.PortCommand;
import plantadapter.commands.ReadCommand;
import plantadapter.commands.TransactionCommand;
import plantadapter.commands.WriteCommand;
import plantadapter.dcgs.AbstractCommandBuilder;
import plantadapter.dcgs.IDeviceCommandBuilder;
import plantmodel.adam.Adam5000Device;

/** 
 * Per ora le transazioni possono essere inviate solo all'endpoint di controllo dell'Adam, vengono restituite
 * tale e quali per essere gestite dal dispositivo successivo (che si occupa materialente di attuare l'interazione)</p>
 * @author JCC
 *
 */
public class Adam5000TransactionCommandBuilder extends AbstractCommandBuilder<Adam5000Device, TransactionCommand> implements IDeviceCommandBuilder {

	public Adam5000TransactionCommandBuilder(Adam5000Device executorDevice, TransactionCommand originalCommand) {
		super(executorDevice, originalCommand);
	}

	@Override
	public PortCommand buildCommand() {
		// TODO Solita necessità di identificare l'endpoint di controllo...
		if (this.getOriginalCommand().getTargetPort().getID().equalsIgnoreCase("RS232")) {
			// devo "copiare" anche i singoli comandi presenti all'interno
			// TODO Meglio fattorizzare da qualche parte questo tipo di operazioni (e.g. implementazione clone())...
			PortCommand[] newCmds = new PortCommand[super.getOriginalCommand().getCommands().length];
			for (int i = 0; i < newCmds.length; i++) {
				if (super.getOriginalCommand().getCommands()[i] instanceof ReadCommand) {
					ReadCommand rdCmd = (ReadCommand)super.getOriginalCommand().getCommands()[i];
					newCmds[i] = new ReadCommand(rdCmd.getCommandID(), rdCmd.getTimestamp(), rdCmd.getTargetPort(), rdCmd.getQuantity());
				}
				else if (super.getOriginalCommand().getCommands()[i] instanceof WriteCommand) {
					WriteCommand wrCmd = (WriteCommand)super.getOriginalCommand().getCommands()[i];
					newCmds[i] = new WriteCommand(wrCmd.getCommandID(), wrCmd.getTimestamp(), wrCmd.getTargetPort(), wrCmd.getValue());;
				}
				else
					throw new IllegalStateException(); // TODO I TransactionCommand NON POSSONO CONTENERE altri TransactionCommand...
			}
			
			return new TransactionCommand(super.getOriginalCommand().getCommandID(), super.getOriginalCommand().getTimestamp(), super.getOriginalCommand().getTargetPort(), newCmds);
		}
		
		throw new UnsupportedOperationException("Adam5000TransactionCommandBuilder");
	}
}
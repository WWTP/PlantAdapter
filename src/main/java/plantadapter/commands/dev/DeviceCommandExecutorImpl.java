package plantadapter.commands.dev;

import java.io.IOException;

import plantadapter.IPlant;

import plantadapter.cmdgen.DeviceCommandGenerationController;
import plantadapter.cmdgen.IDeviceCommandTree;


import plantadapter.commands.Command;
import plantadapter.commands.DeviceCommand;
import plantadapter.commands.ReadCommand;
import plantadapter.commands.TransactionCommand;
import plantadapter.commands.WriteCommand;
import plantadapter.communication.ISink;

import plantadapter.excpts.ConnectionNotFoundException;
import plantadapter.excpts.FullMailboxException;

import plantmodel.Connection;

public class DeviceCommandExecutorImpl implements ISink<DeviceCommand> {
	
	private IPlant plant;
	
	public DeviceCommandExecutorImpl(IPlant plant) {
		this.plant = plant;
	}
	
	@Override
	public void put(DeviceCommand command) throws Exception {
		
		DeviceCommandGenerationController cmdGen = new DeviceCommandGenerationController(this.plant.getCommandLogger()); // TODO
		
		// TODO Nel caso dei comandi "multipli", qui bisognerebbe suddividerli in pi� comandi
		DeviceCommand[] commands = new DeviceCommand[] { command };

		// Ricavo gli alberi rappresentanti i comandi generati
		IDeviceCommandTree[] cmdTrees = cmdGen.getCommandTrees(commands);
		
		boolean sent = false; // TODO
		
		for (IDeviceCommandTree cmdTree : cmdTrees) {
			
			/* TODO sendMask(): cercare eventuali nodi comuni agli alberi e, in caso siano presenti, inviare solamente tante maschere
			 * quanti sono i ReadCommand presenti nel comando comune (eventualmente uno solo se si tratta di un ReadCommand, pi� di uno
			 * in caso di TransactionCommand o simili).
			 */
			
			/* TODO Nota: necessario inviare pi� maschere (e forse sapere anche a chi) in caso di pi� dispositivi radice */
			if (!sent) { // TODO
				sendMask((command instanceof ReadCommand || command instanceof TransactionCommand) ? cmdTree.getMask() : null); // TODO (!!!)
				sent = true; // TODO
			}
			sendCommand(cmdTree.getRawCommand());
			for (Command cmd : cmdTree.getFirstCommands()) {
				if (cmd instanceof WriteCommand)
					this.plant.getCommandLogger().unlog(cmd);
			}
		}
	}
	
	/**
	 * Invia la <code>IInputMask</code> alla parte di risposta
	 * @param mask <code>IInputMask</code> da inviare
	 */
	protected void sendMask(IInputMask mask) {
		try 
		{
			// Per convenzione non viene inviato nulla nel caso la maschera ritornata da IDeviceCommandTree sia null (nessun comando con risposta inviato).
			if(mask != null)
				// Inoltra la Maschera alla opportuna PendingRequestMailbox.
				this.plant.getRequestMailboxFactory().getInstance(mask.asMask().getSourceDevice()).put(mask);
		}
		catch (FullMailboxException e) {
			// TODO
		}
	}
	
	/**
	 * <p>Invia il <code>DeviceCommand</code> specificato.</p>
	 * @param rawCmd <code>DeviceCommand</code> da inviare
	 */
	protected void sendCommand(WriteCommand rawCmd) {
		try {
			this.plant.getCommunicationManager().getCommandSender(this.selectConnection(rawCmd)).put(rawCmd);
			//CommandSenderFactory.getInstance(this.selectConnection(rawCmd)).sendCommand(rawCmd); // TODO Eliminare CommandSenderFactory...
		} catch (ConnectionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO (eccezione di put)
		}
	}
	
	/**
	 * <p>Seleziona una <code>Connection</code> su cui inviare il <code>DeviceCommand</code> specificato.
	 * @param rawCmd Comando da inviare da cui successivamente sar� ricavato l'<code>IDeviceCommandSender</code> che lo invier� effettivamente.
	 * @return Connessione scelta su cui inviare il comando.
	 * @throws IllegalArgumentException Nel caso non vi siano connessioni da cui sia possibile inviare il comando.
	*/
	protected Connection selectConnection(WriteCommand rawCmd) throws IllegalArgumentException {
		for(Connection connection : rawCmd.getTargetPort().getConnectionsAsSlave())
			if(connection.getMasterEndpoint() == null)
				return connection;
		// Nel caso non vi siano connessioni da cui sia possibile inviare il comando
		throw new IllegalArgumentException();
	}
}
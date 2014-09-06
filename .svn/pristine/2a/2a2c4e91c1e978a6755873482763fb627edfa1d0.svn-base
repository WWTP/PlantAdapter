package plantadapter.commands.adam;

import java.util.UUID;

import javax.measure.quantity.DataAmount;

import plantadapter.IPlant;
import plantadapter.PlantAdapter;
import plantadapter.commands.BatchCommand;
import plantadapter.commands.Command;
import plantadapter.commands.ReadCommand;
import plantadapter.commands.TransactionCommand;
import plantadapter.commands.WriteCommand;
import plantadapter.communication.ISink;
import plantadapter.excpts.CommandFailureException;
import plantmodel.Endpoint;
import quantities.InformationAmount;

public class PlantAdamStatusCommandExecutor implements ISink<Command> {

	private PlantAdapter plant;
	
	public PlantAdamStatusCommandExecutor(PlantAdapter plant) {
		this.plant = plant;
	}
	
	@Override
	public void put(Command message) throws Exception {
		
		if (message instanceof Adam5024LastValueReadBackCommand) {
			this.handleAdam5024ChannelLastValueReadBackCommand((Adam5024LastValueReadBackCommand)message);
		}
		else if (message instanceof Adam5024StartUpOutputConfigurationCommand) {
			this.handleAdam5024StartUpOutputConfigurationCommand((Adam5024StartUpOutputConfigurationCommand)message);
		}
		else throw new UnsupportedOperationException();
	}
	
	private void handleAdam5024StartUpOutputConfigurationCommand(Adam5024StartUpOutputConfigurationCommand cmd) {
		// TODO Anche qui importante il concetto di "Endpoint di Controllo"...
		Endpoint adamControlPort = cmd.getTargetDevice().getEndpointById("RS232");
		
		WriteCommand wrCmd = new WriteCommand(UUID.randomUUID().toString(), adamControlPort, new InformationAmount(cmd.getSyntax().toByteArray()));
		
		try {
			this.plant.sendCommand(wrCmd, this.plant.getPlantModel().getCommandLogger().getPlantCallBackReceiver(cmd)); // Nota: importante il secondo parametro (implementazione DefaultPlantAdapter)
		} 
		catch (CommandFailureException e) {
			// TODO Invio fallimento al callback receiver...
		}
	}
	
	private void handleAdam5024ChannelLastValueReadBackCommand(Adam5024LastValueReadBackCommand cmd) {
		
		// TODO Anche qui importante il concetto di "Endpoint di Controllo"...		
		Endpoint adamControlPort = cmd.getTargetDevice().getEndpointById("RS232");
		
		// Necessario WriteCommand per inviare il comando all'ADAM...
		WriteCommand wrCmd = new WriteCommand(cmd.getCommandID(), adamControlPort, new InformationAmount(cmd.getSyntax().toByteArray()));
		// Necessario ReadCommand per imporre a "chi controlla l'ADAM" di leggere la risposta. L'interpretazione del dato ricevuto dovrà essere fatta (tramite la maschera) 
		// da un modulo specifico di ricezione. Nota: nel caso del DT80, i due comandi devono essere impartiti con la stessa istruzione (es. 2SERIAL).
		ReadCommand rdCmd = new ReadCommand(cmd.getCommandID(), adamControlPort, DataAmount.class);
		
		// Raggruppo i due comandi in un batch command (garanzia di sequenza, minimo numero di istruzioni da impiegare).
		// Nota: IMPORTANTISSIMO che l'ID sia diverso da quello del comando ricevuto e che NON SIA SEMPRE LO STESSO
		// (problemi relativi alle associazioni fatte dal CommandLogger).
		TransactionCommand transaction = new TransactionCommand(UUID.randomUUID().toString(), adamControlPort, wrCmd, rdCmd);
		
		// TODO Ora devo poter inviare il comando al PlantAdapter stesso...
		
		try {
			this.plant.sendCommand(transaction, this.plant.getPlantModel().getCommandLogger().getPlantCallBackReceiver(cmd)); // Nota: importante il secondo parametro (implementazione DefaultPlantAdapter)
		} 
		catch (CommandFailureException e) {
			// TODO Invio fallimento al callback receiver...
		}
	}
}
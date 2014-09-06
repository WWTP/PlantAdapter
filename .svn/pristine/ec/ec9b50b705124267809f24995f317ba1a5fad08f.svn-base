package plantadapter.commands.adam;

import java.util.Date;

import plantmodel.adam.Adam5000Device;
import plantmodel.datatypes.ASCIIString;

public class Adam5024StartUpOutputConfigurationCommand extends
		Adam5000ChannelCommand {

	public Adam5024StartUpOutputConfigurationCommand(String commandID,
			Date timestamp, Adam5000Device targetDevice, int slot, int channel) {
		super(commandID, timestamp, targetDevice, slot, channel);
	}

	public Adam5024StartUpOutputConfigurationCommand(String commandID,
			Adam5000Device targetDevice, int slot, int channel) {
		super(commandID, targetDevice, slot, channel);
	}
	
	// TODO Fattorizza in un'interfaccia...
	
	// Nota: cose di questo genere starebbero meglio assieme al modello dell'Adam (eventualmente metterle lì
	// e poi appoggiarsi su di esse per implementare questi comandi - ?).
	
	public ASCIIString getSyntax() {
		// es. $01S0C04
		return ASCIIString.fromString("$" + super.getSyntax().toString() + "4\r"); // Nota: aggiunto anche lo \r perché si tratta di un comando "concreto"...
	}
}
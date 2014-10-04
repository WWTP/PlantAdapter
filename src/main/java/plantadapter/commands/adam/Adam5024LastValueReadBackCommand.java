package plantadapter.commands.adam;

import java.util.Date;

import plantmodel.adam.Adam5000Device;
import plantmodel.datatypes.ASCIIString;

// TODO Questo comando potrebbe essere indirizzato all'Endpoint (?) -
// riflettici, anche in relazione al tipo di risultato.

// Nota: concettualmente, avrebbe senso riprodurre questa informazione come
// una "lettura di corrente" in ingresso alla pompa/uscita ADAM?

/**
 * A questo comando corrisponde come risultato la lettura di un valore elettrico, rappresentante
 * dal valore letto nel canale specificato (espresso in termini dell'Endpoint di riferimento).
 * @author JCC
 *
 */
public class Adam5024LastValueReadBackCommand extends Adam5000ChannelCommand {

	public Adam5024LastValueReadBackCommand(String commandID, Date timestamp, Adam5000Device targetDevice, int slot, int channel) {
		super(commandID, timestamp, targetDevice, slot, channel);
	}

	public Adam5024LastValueReadBackCommand(String commandID, Adam5000Device targetDevice, int slot, int channel) {
		this(commandID, new Date(), targetDevice, slot, channel);
	}
	
	@Override
	public Adam5000Device getTargetDevice() {
		return (Adam5000Device)super.getTargetDevice();
	}
	
	// TODO Fattorizza in un'interfaccia...
	
	// Nota: cose di questo genere starebbero meglio assieme al modello dell'Adam (eventualmente metterle lì
	// e poi appoggiarsi su di esse per implementare questi comandi - ?).
	
	public ASCIIString getSyntax() {
		return ASCIIString.fromString("$" + super.getSyntax().toString() + "6\r"); // Nota: aggiunto anche lo \r perché si tratta di un comando "concreto"...
	}
}
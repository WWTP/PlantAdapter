package plantadapter.commands.adam;

import java.util.Date;

import plantadapter.annotations.CommandInfo;
import plantadapter.commands.StatusCommand;

import plantmodel.adam.Adam5000Device;
import plantmodel.datatypes.ASCIIString;

// TODO Mettere la @CommandInfo direttamente in eventuale classe superiore...
@CommandInfo(commandExecutorClass = PlantAdamStatusCommandExecutor.class)
public abstract class Adam5000ChannelCommand extends StatusCommand {

	private int slot, channel;
	
	public Adam5000ChannelCommand(String commandID, Date timestamp, Adam5000Device targetDevice, int slot, int channel) {
		super(commandID, timestamp, targetDevice);

		this.slot = slot;
		this.channel = channel;
	}

	public Adam5000ChannelCommand(String commandID, Adam5000Device targetDevice, int slot, int channel) {
		this(commandID, new Date(), targetDevice, slot, channel);
	}

	public int getSlot() {
		return slot;
	}

	public int getChannel() {
		return channel;
	}
	
	// TODO Fattorizza in un'interfaccia...
	
	// Nota: cose di questo genere starebbero meglio assieme al modello dell'Adam (eventualmente metterle lì
	// e poi appoggiarsi su di esse per implementare questi comandi - ?).
	
	// Formato: aaSiCj
	public ASCIIString getSyntax() {
		int aa = ((Adam5000Device)super.getTargetDevice()).getHWNetworkAddress();
		// TODO In realtà non funziona con valori più alti dell'indirizzo dell'ADAM, perché è possibile
		// utilizzare entrambe le cifre esadecimali per indicarli
		return new ASCIIString(String.format("0%hS%1dC%1d", aa, this.slot, this.channel));
	}
}
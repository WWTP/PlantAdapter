package plantadapter.dcgs.impl.dt80;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import plantadapter.commands.PortCommand;
import plantadapter.commands.ReadCommand;
import plantadapter.commands.WriteCommand;

import plantmodel.datatypes.ASCIIString;
import plantmodel.dt80.DT80ChannelOptionList;
import plantmodel.dt80.DT80SerialChannelDefinition;
import plantmodel.dt80.DT80SerialControlString;
import plantmodel.dt80.DT80SerialEndpoint;
import plantmodel.dt80.DT80SerialInputAction;
import plantmodel.dt80.DT80SerialOutputAction;
import plantmodel.dt80.IDT80SerialIOAction;

import quantities.InformationAmount;

// TODO Modellare i comandi seriali del DT80 (Channel Definition e Channel Options apposite, compresa la possibilità
// di inserire sia input che output in unica channel options - con eventuale \e iniziale. Questo modulo deve essere
// reso generale e costruire un'istruzione seriale sulla base di un array di ReadCommand/WriteCommand (il comportamento
// "normale" può essere implementato con chiamate successive). Valutare riutilizzo dello stesso oggetto (flyweight).

// Nota: relativamente ai Flyweight, in generale noto l'impianto si sanno già i moduli che verranno riutilizzati
// => istanziare tutto all'inizio e avere sempre pronto quando serve (usando metodi "set" piuttosto che il costruttore
// per passare i parametri).

class DT80SerialChannelDefinitionBuilder /* implements IDT80ChannelDefinitionBuilder */ {
	
	// TODO Parametro Device...
	
	/*
	public static DT80SerialChannelDefinitionBuilder getReadingInstance(ReadCommand command) {
		if (!validateCommand(command))
			throw new IllegalArgumentException();
		return new DT80SerialChannelDefinitionBuilder(command);
	}
	
	public static DT80SerialChannelDefinitionBuilder getWritingInstance(WriteCommand command) {
		if (!validateCommand(command))
			throw new IllegalArgumentException();
		return new DT80SerialChannelDefinitionBuilder(command);
	}
	
	private static boolean validateCommand(PortCommand command) {
		return command.getTargetPort() instanceof DT80SerialEndpoint;
	}
	
	private PortCommand command;
	
	private DT80SerialChannelDefinitionBuilder(PortCommand command) {
		this.command = command;
	}
	*/
	
	// Inutile in quanto il channel number è memorizzato direttamente nell'Endpoint...
	
	/*
	private int getChannelNumber() {
		
		// TODO La seguente cosa sarebbe da fare esternamente, ovvero al modulo del DT80
		// dovrebbero arrivare direttamente comandi diretti ad una sua porta
		
		//// Individuazione Endpoint Output
		// TODO da eliminare quando si cambierà la politica di gestione Endpoint comandi
		Endpoint targetPort = this.command.getTargetPort();

		//\\ Individuazione Endpoint Output
		
		if (targetPort.getID().equalsIgnoreCase("SERSEN_PORT"))
			return 1; // 1SERIAL
		else if (targetPort.getID().equalsIgnoreCase("HOST_RS232_PORT"))
			return 2; // 2SERIAL
		else if (targetPort.getID().equalsIgnoreCase("USB_PORT"))
			return 3; // 3SERIAL
		else
			throw new IllegalStateException();
	}
	*/
	
	// TODO Implementare questo tipo di moduli con pattern flyweight, usando un qualche genere di astrazione
	// per il parametro da passare (stato estrinseco).
	
	
	/**
	 * Si ipotizza che tutti i comandi passati siano diretti ad uno stesso endpoint di uno stesso
	 * dispositivo. Tutti i comandi verranno aggregati in un'unica control string (per ottenere più
	 * istruzioni distinte è necessario invocare più volte questo metodo).
	 * @param commands
	 * @return
	 */
	public DT80SerialChannelDefinition buildSerialChannelDefinition(PortCommand... commands) {
		// TODO No TransactionCommand (?)
		
		if (commands.length == 0)
			throw new IllegalArgumentException("commands.length == 0");
		
		List<IDT80SerialIOAction> actions = new ArrayList<IDT80SerialIOAction>(commands.length);
		
		// TODO Per sicurezza all'inizio svuoto il buffer di ricezione...
		actions.add(DT80SerialInputAction.getEraseReceiveBufferAction());
		
		for (PortCommand command : commands) {
			if (command instanceof ReadCommand) {
				actions.add(this.buildReadCommand((ReadCommand)command));
			}
			else if (command instanceof WriteCommand) {
				actions.add(this.buildWriteCommand((WriteCommand)command));
			}
			else throw new UnsupportedOperationException("DT80SerialChannelDefinitionBuilder.buildSerialChannelDefinition()");
		}
		
		DT80SerialControlString controlString = new DT80SerialControlString(actions.toArray(new IDT80SerialIOAction[0]));
		
		return new DT80SerialChannelDefinition(
				((DT80SerialEndpoint)commands[0].getTargetPort()).getChannelNumber(),
				new DT80ChannelOptionList(controlString));
	}
	
	private DT80SerialOutputAction buildWriteCommand(WriteCommand command) {

		///// Valore da Scrivere
		byte[] outBytes = ((InformationAmount)((WriteCommand)command).getValue()).getBytes();
		
		int i = 0;
		
		// TODO Fattorizzare da qualche parte...
		
		while (i < outBytes.length) {
			// I caratteri non stambabili vengono sostituiti (es. \r -> \013)
			for (; i < outBytes.length; i++) {
				if (outBytes[i] < 31 || outBytes[i] > 254) {
					
					byte[] begin = Arrays.copyOf(outBytes, i);
					byte[] end = Arrays.copyOfRange(outBytes, i + 1, outBytes.length);
					String str = "" + (int)outBytes[i];
					
					if (str.length() == 1) str = "00" + str;
					if (str.length() == 2) str = "0" + str;
					
					byte[] newBytes = new byte[4];
					
					newBytes[0] = '\\';
					newBytes[1] = (byte)str.charAt(0);
					newBytes[2] = (byte)str.charAt(1);
					newBytes[3] = (byte)str.charAt(2);
					
					outBytes = new byte[begin.length + newBytes.length + end.length];
					
					for (int j = 0; j < outBytes.length; j++) {
						if (j < begin.length) outBytes[j] = begin[j];
						else if (j < begin.length + newBytes.length) outBytes[j] = newBytes[j - begin.length];
						else outBytes[j] = end[j - begin.length - newBytes.length];
					}
				}
			}
		} // Fine algoritmo sostituzione caratteri...
		
		return DT80SerialOutputAction.getTextAction(ASCIIString.fromByteArray(outBytes));
	}
	
	private DT80SerialInputAction buildReadCommand(ReadCommand command) {
		// TODO Teoricamente dovrebbe verificare in qualche modo lo "stato" del DT80
		// considerato (riferimento ottenibile direttamente dal comando) e utilizzare
		// solo variabili "libere" - per ora utilizzata 1$ per semplicità.
		return DT80SerialInputAction.getStringDataAction(1);
	}

	/*
	@Override
	public DT80ChannelDefinition buildChannelDefinition() {
		if (this.command instanceof WriteCommand)
			return this.buildWriteCommand();
		else if (this.command instanceof ReadCommand)
			return this.buildReadCommand();
		else
			throw new IllegalStateException();
	}
	*/
}
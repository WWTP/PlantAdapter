package plantadapter.communication.impl;

import java.io.IOException; // TODO
import java.io.OutputStream;

import plantadapter.commands.WriteCommand;
import plantadapter.communication.ISink;

import quantities.InformationAmount;

public class CommandSenderImpl implements ISink<WriteCommand> {

	private OutputStream out;
	
	public CommandSenderImpl(OutputStream out){
		this.out = out;
	}

	@Override
	public void put(WriteCommand rawCmd) throws Exception {
		// Solo WriteCommand inviabili direttamente come comandi ai dispositivi
		if (rawCmd.getValue() instanceof InformationAmount)
			this.out.write(((InformationAmount)rawCmd.getValue()).getBytes());
		else throw new IllegalArgumentException("Impossibile inviare il comando: tipo di dato non valido.");
	}
}
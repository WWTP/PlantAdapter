package plantadapter.dcgs.impl.dt80;

import plantadapter.commands.TransactionCommand;
import plantadapter.dcgs.AbstractCommandBuilder;
import plantmodel.dt80.DT80ChannelDefinition;
import plantmodel.dt80.DT80Device;

public class DT80TransactionCommandBuilder extends AbstractCommandBuilder<DT80Device, TransactionCommand> implements IDT80ChannelDefinitionBuilder {

	public DT80TransactionCommandBuilder(DT80Device executorDevice, TransactionCommand originalCommand) {
		super(executorDevice, originalCommand);
	}

	@Override
	public DT80ChannelDefinition buildChannelDefinition() {
		
		// TODO Gestiti solo comandi diretti alle porte seriali
		// (per verificare questi vincoli sarebbe utile avere delle utilities
		// per determinare il "tipo" di porta e cose simili).
		
		DT80SerialChannelDefinitionBuilder builder = new DT80SerialChannelDefinitionBuilder();
		
		return builder.buildSerialChannelDefinition(super.getOriginalCommand().getCommands());
	}
}
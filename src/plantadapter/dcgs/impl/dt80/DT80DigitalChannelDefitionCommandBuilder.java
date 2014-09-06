package plantadapter.dcgs.impl.dt80;

import plantadapter.commands.PortCommand;
import plantadapter.commands.ReadCommand;
import plantadapter.commands.WriteCommand;
import plantmodel.dt80.DT80ChannelDefinition;
import plantmodel.dt80.DT80DigitalEndpoint;
import plantmodel.dt80.DT80DigitalStateInputChannelDefinition;
import plantmodel.dt80.DT80DigitalStateOutputChannelDefinition;
import quantities.DigitalAmount;
import quantities.DigitalQuantity;

public class DT80DigitalChannelDefitionCommandBuilder implements
		IDT80ChannelDefinitionBuilder {

	public static DT80DigitalChannelDefitionCommandBuilder getReadingInstance(ReadCommand command) {
		if (!validateCommand(command))
			throw new IllegalArgumentException();
		return new DT80DigitalChannelDefitionCommandBuilder(command);
	}
	
	public static DT80DigitalChannelDefitionCommandBuilder getWritingInstance(WriteCommand command) {
		if (!validateCommand(command))
			throw new IllegalArgumentException();
		return new DT80DigitalChannelDefitionCommandBuilder(command);
	}
	
	private static boolean validateCommand(PortCommand command) {
		return command.getTargetPort() instanceof DT80DigitalEndpoint;
	}
	
	private PortCommand command;
	
	private DT80DigitalChannelDefitionCommandBuilder(PortCommand command) {
		this.command = command;
	}
	
	public DT80ChannelDefinition buildChannelDefinition() {
		// TODO Come gestire DigitalStateOutputChannelDefinition senza parametro? In realtà è una lettura...
		if (command instanceof ReadCommand) {
			// ritorna DigitalStateInputChannelDefinition
			return this.buildReadCommand((ReadCommand)command);
		}
		else if (command instanceof WriteCommand) {
			// ritorna DigitalStateOutputChannelDefinition con livello del writecommand passato (controllare quantity amount livello prima)
			return this.buildWriteCommand((WriteCommand)command);
		}
		throw new UnsupportedOperationException("DT80SerialChannelDefinitionBuilder.buildSerialChannelDefinition()");
	}
	
	private DT80ChannelDefinition buildReadCommand(ReadCommand rdCmd)
	{
		DT80DigitalEndpoint dt80Ep = (DT80DigitalEndpoint)rdCmd.getTargetPort();
		
		return new DT80DigitalStateInputChannelDefinition(dt80Ep.getChannelNumber());
	}
	
	private DT80ChannelDefinition buildWriteCommand(WriteCommand rdCmd)
	{
		if(rdCmd.getValue().getQuantity() != DigitalQuantity.class)
			throw new IllegalArgumentException();
		
		DT80DigitalEndpoint dt80Ep = (DT80DigitalEndpoint)rdCmd.getTargetPort();
		
		return new DT80DigitalStateOutputChannelDefinition(dt80Ep.getChannelNumber(), ((DigitalAmount)rdCmd.getValue()).getLevel());
	}
}

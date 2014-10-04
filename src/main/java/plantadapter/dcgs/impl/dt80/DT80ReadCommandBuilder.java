package plantadapter.dcgs.impl.dt80;

import plantadapter.commands.ReadCommand;
import plantadapter.dcgs.AbstractCommandBuilder;

import plantmodel.dt80.DT80ChannelDefinition;
import plantmodel.dt80.DT80Device;
import quantities.DigitalQuantity;

public class DT80ReadCommandBuilder extends AbstractCommandBuilder<DT80Device, ReadCommand> implements IDT80ChannelDefinitionBuilder {

	public DT80ReadCommandBuilder(DT80Device executorDevice,
			ReadCommand originalCommand) {
		super(executorDevice, originalCommand);
	}

	@Override
	public DT80ChannelDefinition buildChannelDefinition() {
		
		IDT80ChannelDefinitionBuilder builder = null;
		
		if(super.getOriginalCommand().getQuantity() == javax.measure.quantity.DataAmount.class) {
			// TODO Letture dalla seriale etc...
		}
		/// Letture DIGITALI
		if(super.getOriginalCommand().getQuantity() == DigitalQuantity.class)
		{
			builder = DT80DigitalChannelDefitionCommandBuilder.getReadingInstance(super.getOriginalCommand());
		}
		//\ Letture DIGITALI 
		// Il blocco seguente racchiude il caso di LETTURE ANALOGICHE
		else 
		{
			builder = DT80AnalogueChannelDefinitionCommandBuilder.getReadingInstance(super.getOriginalCommand());
		}
		
		return builder.buildChannelDefinition();
	}
}
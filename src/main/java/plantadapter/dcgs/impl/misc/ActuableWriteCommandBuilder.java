package plantadapter.dcgs.impl.misc;

import plantadapter.commands.PortCommand;
import plantadapter.commands.WriteCommand;
import plantadapter.dcgs.AbstractCommandBuilder;
import plantadapter.dcgs.IDeviceCommandBuilder;

import plantmodel.Endpoint;
import plantmodel.misc.ActuableDevice;

import quantities.AnalogueAmount;
import quantities.BinaryAmount;
import quantities.DigitalAmount;
import quantities.DigitalQuantity;
import quantities.IAmount;

public class ActuableWriteCommandBuilder extends AbstractCommandBuilder<ActuableDevice, WriteCommand> implements IDeviceCommandBuilder {

	public ActuableWriteCommandBuilder(ActuableDevice executorDevice,
			WriteCommand originalCommand) {
		super(executorDevice, originalCommand);
	}

	@Override
	public PortCommand buildCommand() {
		
		// TODO Aggiungere conversione tramite DeviceAmountConverter (quando saranno supportati Command con associati originalCommand e la possibilità di convertire il valore originale se trovata una EndpointInterface che la supporti nativamente)
		IAmount value = super.getOriginalCommand().getValue();
		
		if(value.getQuantity().equals(DigitalQuantity.class))
			// Conversione da Digitale ad Analogico secondo EndpointInterface digitale
		{
			// APPLY CONVERSION
			// Se Endpoint supporta nativamente DigitalEndpointInterface ritornare lo stesso valore: DCG conosce quali dei suoi Endpoint supportano nativamente comandi digitali (senza correlarli alle EndpointInterface che non sono in grado di specificarlo a meno che DigitalEndpoint non siano PhisicalEndpointInterface - vengono distinte dalle EndpointInterfaceLogiche che effettuano la conversione in Analogico)
		}
		
		return new WriteCommand(super.getOriginalCommand().getCommandID(), super.getOriginalCommand().getTimestamp(), super.getExecutorDevice().getSlaveEndpoints()[0], value);
	}
}
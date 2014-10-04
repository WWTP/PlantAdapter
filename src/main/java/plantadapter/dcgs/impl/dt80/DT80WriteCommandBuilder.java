package plantadapter.dcgs.impl.dt80;

import plantadapter.commands.WriteCommand;
import plantadapter.dcgs.AbstractCommandBuilder;

import plantmodel.dt80.DT80ChannelDefinition;
import plantmodel.dt80.DT80Device;
import quantities.DigitalQuantity;

public class DT80WriteCommandBuilder extends AbstractCommandBuilder<DT80Device, WriteCommand> implements IDT80ChannelDefinitionBuilder {

	public DT80WriteCommandBuilder(DT80Device executorDevice,
			WriteCommand originalCommand) {
		super(executorDevice, originalCommand);
	}

	@Override
	public DT80ChannelDefinition buildChannelDefinition() {
		
		//IDT80ChannelDefinitionBuilder builder = null;
		
		// TODO Aggiungere possibile distinzione in base a Endpoint (getQuantity() potrebbe NON bastare nel caso dei DataAmount).
		// TODO Attualmente unico canale digitale ammesso è SERIAL
		
		if(super.getOriginalCommand().getValue().getQuantity() == javax.measure.quantity.ElectricPotential.class){
			throw new UnsupportedOperationException(); // TODO Come si scrive un voltaggio su DT80?
		}
		else if(super.getOriginalCommand().getValue().getQuantity() == javax.measure.quantity.ElectricCurrent.class){
			throw new UnsupportedOperationException(); // TODO Come si scrive una corrente su DT80?
		}
		else if(super.getOriginalCommand().getValue().getQuantity() == DigitalQuantity.class){
			DT80DigitalChannelDefitionCommandBuilder builder = DT80DigitalChannelDefitionCommandBuilder.getWritingInstance(super.getOriginalCommand());
			
			return builder.buildChannelDefinition();
		}
		else if(super.getOriginalCommand().getValue().getQuantity() == javax.measure.quantity.DataAmount.class){
			// TODO Verifica EthernetPort e altri eventuali Endpoint che gestiscano DataAmount
			return new DT80SerialChannelDefinitionBuilder().buildSerialChannelDefinition(super.getOriginalCommand()); // TODO Riutilizza sempre stessa istanza...
		}
		else throw new UnsupportedOperationException();
		
		//return builder.buildChannelDefinition();
	}
	
	// Commenti vari estratti dal metodo precedente
	
	//// Individuazione Device successivo
	//Device nextDevice = /*port.getConnectionsAsSlave()[0].getMasterDevice()*/ this.device;
	//\\ Individuazione Device successivo
	
	//// Individuazione Quantity successiva
	// IMPLICITO IN QUANTO IAmount è un InformationAmount!!!
	// TODO Class<? extends Quantity> nextCommandQuantity = DataAmount.class;
	//\\ Individuazione Quantity successiva

	/* TODO Anche il channel number, se serve, verrà preso dai singoli moduli...
	 * 
	//// Composizione IAmount
	int channelNumber;
	// TODO Da fare meglio..
	if (command.getValue().getQuantity() != javax.measure.quantity.DataAmount.class)
		channelNumber = device.getAnalogueEndpointMapper().getChannelNumber(port);
	*/
	
	/* TODO Ha senso occuparsi degli span solamente nel caso di scritture analogiche
	 * (nei moduli ad essi dedicati)...
	if(span != null)
		// TODO DT80 può solo scrivere su Endpoint digitali? Se sì inutile gestire span
		channelOptions = new DT80ChannelOptionList(outValue, span);
	else
		channelOptions = new DT80ChannelOptionList(outValue);
    */
}
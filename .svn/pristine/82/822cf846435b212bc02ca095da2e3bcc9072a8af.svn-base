package plantadapter.dcgs.impl;

import plantadapter.cmdgen.DeviceCommandGenerationList;
import plantadapter.cmdgen.IAggregatedDeviceCommand;
import plantadapter.dcgs.IDeviceCommandGenerator;
import plantadapter.excpts.InvalidCommandException;

import plantmodel.Device;

// TODO Un DCG non dovrebbe accettare comandi che non siano diretti al dispositivo stesso
// (tale vincolo può essere verificato ogni volta, ma forse per evitare di scorrere sempre
// tutti i comandi può avere senso darlo per scontato e verificarlo solo all'esterno (magari
// nella DCGList, man mano che i comandi vengono aggiunti).

/**
 * <p>Controlli comuni a tutti i DCG effettuati da questa classe:
 * <ul>
 * <li>dispositivo a cui è riferita una DCGList uguale a quello incapsulato dal DCG;</li>
 * <li>dispositivo a cui è riferito ciascun comando della DCGList uguale a quello incapsulato dal DCG.</li>
 * </ul>
 * </p>
 * @author JCC
 *
 * @param <TDev>
 */
abstract class AbstractDeviceCommandGenerator<TDev extends Device> implements IDeviceCommandGenerator {

	private TDev device; 
	
	public AbstractDeviceCommandGenerator(TDev device) {
		this.device = device;
	}
	
	protected TDev getDevice()
	{
		return this.device;
	}
	
	@Override
	public final IAggregatedDeviceCommand[] generateCommands(DeviceCommandGenerationList commands) throws InvalidCommandException {
		
		// Controlla che il dispositivo a cui è diretta la lista sia quello incapsulato
		if(commands.getCommandGeneratorDevice() != this.device)
			throw new IllegalArgumentException();
		
		// TODO De-commentare una volta modificata l'implementazione
		/*
		for (DeviceCommand cmd : commands.getCommands()) {
			if (cmd.getTargetDevice() != this.device)
				throw new IllegalArgumentException("cmd.getTargetDevice() != this.device");
		}
		*/
		
		// TODO La parte sottostante - incluso il metodo this.generateCommandsImpl() - è da utilizzare solamente 
		// se è prevista una politica comune per la gestione dei comandi diretti agli endpoint di controllo
		
		/*
		
		// Ottiene l'Endpoint di controllo del dispositivo
		// TODO potrebbe essere opportuno ottenere questa informazione dall'esterno
		// piuttosto che dai singoli DCG
		Endpoint controlEndpoint = this.getControlEndpoint();
		
		// Comandi diretti all'Endpoint di controllo del dispositivo vengono restituiti
		// e sequenzializzati (ad N comandi ricevuti corrispondono N comandi aggregati distinti).
		for (DeviceCommand command : commands.getCommands()) {
			if (command instanceof PortCommand && ((PortCommand) command).getTargetPort() == controlEndpoint) {
				// Per loro definizione, questi comandi non possono essere aggregati, devono
				// quindi essere sequenzializzati
				
				// TODO
			}
		}
		*/
		
		return this.generateCommandsImpl(commands);
	}
	
	protected abstract IAggregatedDeviceCommand[] generateCommandsImpl(DeviceCommandGenerationList commands) throws InvalidCommandException;
	
	//protected abstract Endpoint getControlEndpoint();
}
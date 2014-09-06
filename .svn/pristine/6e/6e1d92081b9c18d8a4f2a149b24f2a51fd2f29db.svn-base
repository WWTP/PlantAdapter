package plantadapter.dcgs.impl;

import java.util.ArrayList;
import java.util.List;

import plantadapter.cmdgen.AggregatedDeviceCommandImpl;
import plantadapter.cmdgen.DeviceCommandGenerationList;
import plantadapter.cmdgen.IAggregatedDeviceCommand;
import plantadapter.commands.DeviceCommand;
import plantadapter.commands.PortCommand;
import plantadapter.commands.ReadCommand;
import plantadapter.commands.TransactionCommand;
import plantadapter.commands.WriteCommand;
import plantadapter.dcgs.IDeviceCommandGenerator;
import plantadapter.dcgs.impl.dt80.DT80ReadCommandBuilder;
import plantadapter.dcgs.impl.dt80.DT80TransactionCommandBuilder;
import plantadapter.dcgs.impl.dt80.DT80WriteCommandBuilder;
import plantadapter.dcgs.impl.dt80.IDT80ChannelDefinitionBuilder;
import plantadapter.excpts.InvalidCommandException;

import plantmodel.Connection;
import plantmodel.Device;
import plantmodel.Endpoint;
import plantmodel.datatypes.ASCIIString;
import plantmodel.dt80.DT80ChannelDefinition;
import plantmodel.dt80.DT80ChannelDefinitionList;
import plantmodel.dt80.DT80ChannelOption;
import plantmodel.dt80.DT80ChannelOptionList;
import plantmodel.dt80.DT80Command;
import plantmodel.dt80.DT80Device;
import plantmodel.dt80.DT80SerialChannelDefinition;
import plantmodel.dt80.DT80SerialControlString;
import plantmodel.dt80.DT80SerialInputAction;
import plantmodel.dt80.DT80SerialInputAction.DT80SerialStringDataInputAction;
import plantmodel.dt80.DT80Utils.Info.ChannelTypes;
import plantmodel.dt80.IDT80SerialIOAction;

import quantities.IAmount;
import quantities.InformationAmount;

// Si riferisce ad una particolare istanza di DT80
// NOTA: il Device di riferimento è passato ANCHE attraverso DeviceCommandGenerationList.
// TODO
// TODO REFACTORING CLASSE:
// TODO  - creare classe DT80DeviceReadCommandGenerator a cui passare comando/i da generare
// TODO  - creare classe DT80DeviceWriteCommandGenerator a cui passare comando/i da generare
// TODO  - classe DT80CommandGenerator usata solo per smistare comandi da generare ad opportune classi e fornire un unico gruppo di IAggregatedCommand
// TODO  - implementare DT80CommandGenerator.getNewCommands(Command[] commands) - controllo aggiuntivo su Device
// TODO  - implementare DT80CommandGenerator.getNewCommand(Command command)
public class DT80DeviceCommandGenerator implements IDeviceCommandGenerator {

	private DT80Device device;
	
	/**
	 * Costruisce un <code>DT80DeviceCommandGenerator</code> sulla base del <code>Device</code> passato
	 * @param device <code>Device</code> a cui è riferito questo <code>DT80DeviceCommandGenerator</code>
	 */
	public DT80DeviceCommandGenerator(DT80Device device){
		this.device = device;
	}
	
	// TODO Creazione di una DT80ChannelDefinition per ogni comando ricevuto e loro aggragazione in una "ScheduleDefinition" immediata
	
	@Override
	public IAggregatedDeviceCommand[] generateCommands(DeviceCommandGenerationList commands) throws InvalidCommandException {
		
		DT80Device dt80 = (DT80Device) commands.getCommandGeneratorDevice();
		
		// Sono necessarie più liste in quanto possono essere generati più comandi distinti
		// (la prima è usata normalmente, le altre vengono create all'occorrenza).
		List<DT80ChannelDefinitionList> channelDefinitionLists = new ArrayList<DT80ChannelDefinitionList>();
		channelDefinitionLists.add(new DT80ChannelDefinitionList());
		
		for(DeviceCommand command : commands)
		{
			if(command instanceof PortCommand)
			{
				// TODO Il vincolo sul fatto che la porta debba essere dello specifico DT80 in oggetto deve essere rispettato esternamente...
				// TODO Fattorizzare in un DCG più astratto la gestione di comandi diretti a EP di controllo... (?)
				if (command instanceof WriteCommand && ((PortCommand) command).getTargetPort().getID().equalsIgnoreCase("ETHERNET_PORT")) {
					// DT80ControlPortCommand extends DT80ChannelDefinition
					channelDefinitionLists.get(0).add(new DT80ControlPortCommand(((InformationAmount)((WriteCommand)command).getValue()).getBytes()));
					continue;
				}
				
				/* Da qui in poi si gestiscono solo i comandi che non siano diretti a porte di controllo */
				
				/// Controllo precondizioni su PortCommand
				if(!validatePort(((PortCommand)command).getTargetPort(), dt80))
					throw new IllegalArgumentException();
				//\ Controllo precondizioni su PortCommand
				
				IDT80ChannelDefinitionBuilder cmdBuilder = null;
				// TODO Factory...
				if(command instanceof ReadCommand) {
					cmdBuilder = new DT80ReadCommandBuilder(this.device, (ReadCommand)command);
				}
				else if(command instanceof WriteCommand) {
					cmdBuilder = new DT80WriteCommandBuilder(this.device, (WriteCommand)command);
				}
				else if (command instanceof TransactionCommand) {
					cmdBuilder = new DT80TransactionCommandBuilder(this.device, (TransactionCommand)command);
				}
				else throw new IllegalStateException("DT80DeviceCommandGenerator");
				
				DT80ChannelDefinition channelDefinition = cmdBuilder.buildChannelDefinition();
				
				channelDefinitionLists.get(0).add(channelDefinition);
				
				// Nel caso di letture dalla seriale (ed eventualmente in altri casi), un comando di lettura va
				// inteso come "leggi e restituiscimi il risultato", dunque è implicito che il DT80 debba immagazzinare
				// il dato in una sua variabile interna e restituirlo -> aggiunta di una channel definition per la
				// restituzione del valore della variabile.
				if (channelDefinition instanceof DT80SerialChannelDefinition) {
					DT80SerialChannelDefinition serialChannelDef = (DT80SerialChannelDefinition)channelDefinition;
					DT80ChannelOptionList options = serialChannelDef.getChannelOptions();
					for (DT80ChannelOption opt : options) {
						if (opt instanceof DT80SerialControlString) {
							IDT80SerialIOAction[] actions = ((DT80SerialControlString)opt).getIOActions();
							for (IDT80SerialIOAction action : actions) {
								if (action instanceof DT80SerialInputAction) {
									if (action instanceof DT80SerialStringDataInputAction) {
										DT80SerialStringDataInputAction strAct = (DT80SerialStringDataInputAction)action;
										// Variabile usata per memorizzare il dato...
										int n = strAct.getVariable();						
										// TODO In realtà dovrebbe cambiare a seconda del tipo di lettura effettuata
										// (per ora ipotizziamo si sia letta un'intera stringa, la cui elaborazione 
										// è a carico dell'InputParser).
										channelDefinitionLists.add(new DT80ChannelDefinitionList());
										// Aggiungo la nuova channel definition alla lista appena creata 
										// (così da creare un comando distinto).
										channelDefinitionLists.get(channelDefinitionLists.size() - 1).add(new DT80ChannelDefinition(n, ChannelTypes.STRING));
									}
								}
							}
						}
					}
				}
			}
		}
		
		// Creo un comando aggregato per ogni ChannelDefinitionList
		
		List<IAggregatedDeviceCommand> generatedCommands = new ArrayList<IAggregatedDeviceCommand>();
		
		for (DT80ChannelDefinitionList lst : channelDefinitionLists) {
			//\\ Composizione IAmount
			IAmount data = new InformationAmount(new DT80Command(lst).getDT80Syntax().toByteArray());

			//// Individuazione Endpoint Sorgente successivo
			Endpoint nextPort = this.device.getEndpointById("ETHERNET_PORT");
			//\\ Individuazione Endpoint Sorgente successivo
			
			//// Istanziazione Comando
			
			/* TODO Occorre una convenzione su ID e TimeStamp dei comandi multipli/legati alle schedule (per ora ne uso uno a caso
			 * poiché non caso delle Schedules non serve, ma nel caso dei comandi multipli seriverebbe!
			 */
			generatedCommands.add(
					new AggregatedDeviceCommandImpl(
							commands.getCommands(), 
							new WriteCommand
							(
									commands.getCommands()[0].getCommandID(),
									commands.getCommands()[0].getTimestamp(), 
									nextPort,
									data
							)
						)
					);
		}
		
		return generatedCommands.toArray(new IAggregatedDeviceCommand[0]);
	}

	/**
	 * Controlla se l'Endpoint è associato al Device o se l'Endpoint è connesso al device.
	 * @param port
	 * @param device
	 * @return
	 */
	// TODO far diventare metodo di istanza
	private static boolean validatePort(Endpoint port, Device device){
		if(port.getDevice() == device || checkDevice(device, port))
			return true;
		return false;
	}
	
	/** Controlla se device è collegato dall'Endpoint passato.
	 * @param device
	 * @param port
	 * @return
	 */
	// TODO far diventare metodo di istanza?
	// TODO includere metodo come utilità di Device?
	private static boolean checkDevice(Device device, Endpoint port){
		Connection[] portConnections = port.getConnectionsAsSlave();
		for(Connection conn : portConnections)
			if(conn.getMasterDevice().equals(device))
				return true;
		return false;
	}
	
	@Override
	public boolean hasInputParser() {
		return true;
	}

	/*
	@Override
	public DT80Device getAssociatedDevice() {
		return this.device;
	}*/
	
	/**
	 * Classe usata come contenitore dei comandi inviati direttamente ad una
	 * porta di controllo del DT80, che vengono aggregati agli altri comandi
	 * generati come se fossero normali channel definitions.
	 * @author JCC
	 *
	 */
	private class DT80ControlPortCommand extends DT80ChannelDefinition
	{
		private byte[] command;
		
		public DT80ControlPortCommand(byte[] command) {
			// Questi dati non sono significativi in quanto questa implementazione non li usa
			// devono comunque essere inseriti dati corretti, onde evitare malfunzionamenti
			super(2, ChannelTypes.SERIAL);
			this.command = command;
		}
		
		@Override
		public ASCIIString getDT80Syntax() {
			return ASCIIString.fromByteArray(command);
		}
	}
}
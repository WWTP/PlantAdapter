package tests.commandtreebuilder.uut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import plantadapter.commands.DeviceCommand;
import plantadapter.commands.PortCommand;
import plantadapter.commands.StatusCommand;
import plantmodel.Connection;
import plantmodel.Device;
import plantmodel.Endpoint;

/**
 * <p>Un <code>DeviceCommandGenerationList</code> è l'insieme di <code>DeviceCommand</code> accomunati allo stesso <code>Device</code> responsabile della loro risoluzione.</p>
 */
public class DeviceCommandGenerationList implements Iterable<DeviceCommand> {
	
	/// MEMBRI STATICI
	//// METODI STATICI
	/**
	 * <p>Controlla se il <code>DeviceCommand</code> passato è associabile al <code>Device</code> passato.</p>
	 * <p>Un <code>DeviceCommand</code> non è valido rispetto ad un <code>Device</code> se l'<code>Endpoint</code>
	 * a cui è diretto non contiene tale <code>Device</code> come <i>master</i> o come <i>slave</i> di una sua connessione.</p>
	 * @param cmd
	 * @param device
	 * @return
	 */
	public static boolean validate(DeviceCommand cmd, Device device) {
		if(device == null)
			return true;
		if(cmd instanceof PortCommand){
			Connection[] cnns = ((PortCommand)cmd).getTargetPort().getConnectionsAsSlave();
			for (Connection cnn : cnns) {
				/*
				 * Valido anche se Slave in quanto le richieste iniziali possono puntare ad Endpoint <i>slave</i> dei <code>Device</code>.
				 */
				if (cnn.isMaster(device) || cnn.isSlave(device)) return true;
			}
			return false;
		}
		if(cmd instanceof StatusCommand)
			return cmd.getTargetDevice().equals(device);
		throw new UnsupportedOperationException();
	}
	
	///// Factories
	/**
	 * <p>Genera una <code>DeviceCommandGenerationList</code> associata al <code>Device</code> che deve gestire il <code>DeviceCommand</code> passato.</p>
	 * @param cmd <code>DeviceCommand</code> da associare alla <code>DeviceCommandGenerationList</code> che si vuole generare.
	 * @param commandGeneratorDevice <code>Device</code> associato alla <code>DeviceCommandGenerationList</code> che si vuole ottenere.
	 * @return <code>DeviceCommandGenerationList</code> associata al <code>Device</code> contenente il <code>DeviceCommand</code> passato.
	 * @throws IllegalArgumentException Se il <code>Device</code> passato non è coerente con il <code>DeviceCommand</code>.
	 */
	public static DeviceCommandGenerationList fromCommand(DeviceCommand cmd, Device commandGeneratorDevice) throws IllegalArgumentException {
		DeviceCommandGenerationList list = new DeviceCommandGenerationList(commandGeneratorDevice);
		if (validate(cmd, commandGeneratorDevice)) {
			list.add(cmd);
			return list;
		}
		else throw new IllegalArgumentException();
	}
	
	/**
	 * <p>Genera una <code>DeviceCommandGenerationList</code> associata ai <code>DeviceCommand</code> passati e al <code>Device</code> che deve risolverli.</p>
	 * @param cmds Insieme di <code>DeviceCommand</code> da associare alla <code>DeviceCommandGenerationList</code> che si vuole generare.
	 * @param commandGeneratorDevice <code>Device</code> associato alla <code>DeviceCommandGenerationList</code> che si vuole ottenere.
	 * @return <code>DeviceCommandGenerationList</code> associata al <code>Device</code> contenente i <code>DeviceCommand</code> passati.
	 * @throws IllegalArgumentException Se il <code>Device</code> passato non è coerente con il <code>DeviceCommand</code>.
	 */
	public static DeviceCommandGenerationList fromCommands(Iterable<? extends DeviceCommand> cmds, Device commandGeneratorDevice) {
		DeviceCommandGenerationList list = new DeviceCommandGenerationList(commandGeneratorDevice);
		for (DeviceCommand cmd : cmds) {
			if (validate(cmd, commandGeneratorDevice)) {
				list.add(cmd);	
			}
			else throw new IllegalArgumentException();
		}
		return list;
	}
	//\\\ Factories
	
	// TODO Aggiornare in base al formato dei comandi (usare gli Accessors invece di XMLReader - ora eliminato)
	
	/**
	 * Passato l'insieme di <code>DeviceCommandGenerationList currCmds</code>, ne rimuovo le occorrenze i cui <code>Device</code> coincidano con quello associato al <code>DeviceCommandGenerationList</code> contenuto in cmds ed integro le altre attraverso l'elenco di Comandi <code>Document[] newCmds</code> passati.
	 * @param currCmds
	 * @param newCmds
	 * @param solvedDevices
	 */
	public static DeviceCommandGenerationList[] update(DeviceCommandGenerationList[] currCmds, IAggregatedDeviceCommand[] newCmds) {
		List<DeviceCommandGenerationList> lCurrCmds = new ArrayList<DeviceCommandGenerationList>();
		
		lCurrCmds = Arrays.asList(currCmds);
		
		// Cancello ogni newCmd.getSolvedDeviceCommand().getEndpoint().getDevice() trovato
		// Aggiorno ogni currCmd con ogni newCmd.getSolvedDeviceCommand().getEndpoint().getConnections()[0].getMasterDevice()
		
		for(IAggregatedDeviceCommand newCmd : newCmds){
			// TODO Introdurre DeviceCommand newSolvedDeviceCommand = newCmd.getSolvedDeviceCommand(); per semplificare?
			// RIMOZIONE DeviceCommandGenerationList risolte
			// TODO Migliorare prestazioni introducendo ultimo Device eliminato (o elenco Device già eliminati) in modo da non dover scorrere tutta la lista di comandi per ogni nuovo comando.
			for(DeviceCommandGenerationList currCmd : lCurrCmds){
				if(newCmd.getSolvedDeviceCommand().getTargetDevice() == currCmd.getCommandGeneratorDevice()){
					// Se più di un IAggregatedDeviceCommand si riferisce allo stesso Device (in quanto non aggregati) il DeviceCommandGenerationList a cui è associato sarà eliminato una sola volta (correttamente) in quanto poi non comparirà più nella lista di DeviceCommandGenerationList attuale.
					lCurrCmds.remove(currCmd);
					break; // Solo un DeviceCommandGenerationList può essere associato allo stesso Device
				}
			}
			// AGGIORNAMENTO e INTEGRAZIONE DeviceCommandGenerationList correnti
			
			if(newCmd.getSolvedDeviceCommand() instanceof PortCommand){
				boolean found = false;
				Endpoint newCmdTargetEndpoint = ((PortCommand)newCmd.getSolvedDeviceCommand()).getTargetPort();
				// Controllo se, per il prossimo Device che deve gestire il comando, esiste già una DeviceCommandGenerationList ad esso associato
				for(DeviceCommandGenerationList currCmd : lCurrCmds){
					if(getNextMasterDevice(newCmdTargetEndpoint) == currCmd.getCommandGeneratorDevice()){
						currCmd.add(newCmd.getSolvedDeviceCommand());
						found = true;
						break;
					}
				}
				if(!found){
					lCurrCmds.add(new DeviceCommandGenerationList(getNextMasterDevice(newCmdTargetEndpoint)));
				}
			}
			else{
				// TODO if(newCmd.getSolvedDeviceCommand() instanceof StatusCommand)
				// TODO gestione statusCommand attraverso getTargetDevice() e discriminazione attraverso
				// TODO nuovo metodo getNextMasterDevice(newCmdTargetDevice) per discriminare quale deve occuparsene
				throw new UnsupportedOperationException();
			}
		}
		
		/*
		for(Device solvedDevice : solvedDevices) {
			for(DeviceCommand newCmd : newCmds) {
				if(newCmd.getTargetDevice() == solvedDevice) {
					// rimuovi elemento da currCmds
					lCurrCmds.remove(newCmd);
				}
				else {
					 for(DeviceCommandGenerationList currCmdc : lCurrCmds) {
						 if(newCmd == currCmdc..getDeviceID())
							// aggiungi elemento a currCmdc
							currCmdc.add(newCmd);
					 }
				}
			}
		}
		*/
		return lCurrCmds.toArray(new DeviceCommandGenerationList[0]);
	}
	
	/**
	 * <p>Indica il <code>Device</code> che andrà a gestire la richiesta legata all'<code>Endpoint</code> passato.</p>
	 * <p>Questa discriminazione è necessaria in quanto un <code>Endpoint</code> potrebbe avere più <code>Connection</code> associate ad esso.</p>
	 * @param endpoint 
	 * @return
	 */
	// TODO valutare se deve essere qui o indicato altrove; potrebbe essere una entità esterna a dover gestire ciò (es.: Router)
	private static Device getNextMasterDevice(Endpoint endpoint){
		return endpoint.getMasterDevices()[0];
	}
	
	/**
	 * Raggruppa i <code>DeviceCommand</code> passati a seconda del <code>Device</code> che deve gestirli nel <code>DeviceCommandGenerationList</code> di competenza.
	 * @param commands Insieme di <code>DeviceCommand</code> da raggruppare.
	 * @return <code>DeviceCommandGenerationList</code> ottenuti dai <code>DeviceCommand</code> passati.
	 */
	public static DeviceCommandGenerationList[] define(DeviceCommand[] commands){
		List<DeviceCommandGenerationList> lcmds = new ArrayList<DeviceCommandGenerationList>();
		for(DeviceCommand command : commands){
			boolean found = false;
			for(DeviceCommandGenerationList lcmd : lcmds){
				if(lcmd.getCommandGeneratorDevice() == command.getTargetDevice()){
					lcmd.add(command);
					found = true;
					break;
				}
			}
			if(!found){
				lcmds.add(DeviceCommandGenerationList.fromCommand(command, command.getTargetDevice()));
			}
		}
		return lcmds.toArray(new DeviceCommandGenerationList[0]);
	}
	//\\ METODI STATICI
	//\ MEMBRI STATICI
	
	/// MEMBRI DI ISTANZA
	//// Attributi Membro
	private List<DeviceCommand> commands = null;
	private Device commandGeneratorDevice;
	//\\ Attributi Membro
	//// Metodi di Istanza
	///// Costruttori
	/**
	 * <p>Genera una nuova <code>DeviceCommandGenerationList</code> associata al <code>Device</code> passato.</p>
	 * @param commandGeneratorDevice <code>Device</code> a cui sarà associata la <code>DeviceCommandGenerationList</code>
	 */
	public DeviceCommandGenerationList(Device commandGeneratorDevice){
		this.commandGeneratorDevice = commandGeneratorDevice;
	}
	//\\\ Costruttori
	///// Accessors
	public DeviceCommand[] getCommands() {
		return this.commands.toArray(new DeviceCommand[0]);
	}
	
	public Device getCommandGeneratorDevice() {
		return this.commandGeneratorDevice;
	}
	//\\\ Accessors
	
	/**
	 * <p>Controlla se il <code>Device</code> associato ï¿½ presente in un gruppo di <code>Device</code> passato.</p>
	 * @param devices Insieme di <code>Device</code>
	 * @return Ritorna true se almeno un <code>Device</code> passato ï¿½ uguale al <code>Device</code> dell'oggetto, false altrimenti.
	 */
	boolean isIn(Iterable<Device> devices) {
		for(Device device : devices){
			if(device == this.commandGeneratorDevice)
				return true;
		}
		return false;
	}
	
	/**
	 * <p></p>
	 * @param deviceCommandGenerationLists
	 * @return
	 */
	/*
	 * TODO definire utilità metodo
	boolean isIn(DeviceCommandGenerationList[] deviceCommandGenerationLists) {
		// TODO creare metodo statico toDeviceIDs(SameDeviceCommandGeneratorCommandCollection[] cmdcs) : String[]
		// TODO isIn(SameDeviceCommandGeneratorCommandCollection.toDeviceIDs(cmdcs));
		for (DeviceCommandGenerationList cmdc : deviceCommandGenerationLists) {
			if(cmdc.getCommandGeneratorDevice().equals(this.commandGeneratorDevice))
				return true;
		}
		return false;
	}
	*/
	
	/**
	 * <p>Aggiunge i Comandi passati ai comandi salvati se sono associati allo stesso <code>Device</code></p>
	 * @param cmds
	 */
	// TODO utilità?
	void update(DeviceCommand[] cmds) {
		for(DeviceCommand cmd : cmds) {
			if(validate(cmd, this.commandGeneratorDevice))
				this.commands.add(cmd);
			else
				throw new IllegalArgumentException();
		}
	}
	
	/**
	 * <p>Aggiunge alla <code>DeviceCommandGenerationList</code> il <code>DeviceCommand</code> passato.</p>
	 * @param cmd <code>DeviceCommand</code> da aggiungere.
	 * @throws IllegalArgumentException se il <code>DeviceCommand</code> passato non è risolvibile direttamente da questa <code>DeviceCommandGenerationList</code>.
	 */
	public void add(DeviceCommand cmd) throws IllegalArgumentException {
		if (validate(cmd, this.commandGeneratorDevice))
			this.commands.add(cmd);
		else
			throw new IllegalArgumentException();
	}

	@Override
	public Iterator<DeviceCommand> iterator() {
		return this.commands.iterator();
	}
}
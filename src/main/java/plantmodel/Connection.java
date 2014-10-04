package plantmodel;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Un Endpoint connesso a <code>null</code> è inteso come connesso all'host del sistema. Possono esistere
 * quindi più connessioni aventi master <code>null</code>, le quali non sono reperibili usando <code>null</code> come chiave.</p>
 * @author JCC
 *
 */
public class Connection {
	
	// Static Fields
	
	// Ipotizzo che ad un Endpoint corrisponda al più una connessione
	private static Map<Endpoint, Connection> connections = new HashMap<Endpoint, Connection>();
	
	public static Connection fromEndpoint(Endpoint endpoint) {
		return connections.get(endpoint);
	}
	
	public static Connection fromEndpoint(String deviceID, String endpointID) {
		return connections.get(Endpoint.fromID(deviceID, endpointID));
	}
	
	// Instance Fields
	
	private final Endpoint masterEndpoint;
	private final Endpoint slaveEndpoint;
	
	// Public Constructors
	
	public Connection(Endpoint masterEndpoint, Endpoint slaveEndpoint) { // Ipotesi singola connessione per ciascun Endpoint
		// Controlli validità parametri
		if (slaveEndpoint == null)
			throw new IllegalArgumentException("Endpoint Slave non può essere null.");
		if (masterEndpoint == slaveEndpoint || (masterEndpoint != null && (masterEndpoint.getDevice() == slaveEndpoint.getDevice())))
			throw new IllegalArgumentException("Due Endpoint di una stessa Connessione non possono appartenere allo stesso dispositivo.");
		if (connections.containsKey(masterEndpoint) && connections.containsKey(slaveEndpoint))
			throw new IllegalArgumentException("Endpoints già parte di un'altra connessione.");
		if (masterEndpoint != null && connections.containsKey(masterEndpoint))
			throw new IllegalArgumentException("Endpoint Master già parte di un'altra connessione.");
		if (connections.containsKey(slaveEndpoint))
			throw new IllegalArgumentException("Endpoint Slave già parte di un'altra connessione.");
		// Inizializzazione Campi
		this.masterEndpoint = masterEndpoint;
		this.slaveEndpoint = slaveEndpoint;
		// Aggiunge la connessione alla mappa delle connessioni del sistema
		if (masterEndpoint != null) connections.put(masterEndpoint, this);
		connections.put(slaveEndpoint, this);
	}
	
	// Accessors
	
	public Endpoint getMasterEndpoint() {
		return this.masterEndpoint;
	}
	
	public Device getMasterDevice() {
		return this.masterEndpoint == null ? null : this.masterEndpoint.getDevice();
	}
	
	public Endpoint getSlaveEndpoint() {
		return this.slaveEndpoint;
	}
	
	public Device getSlaveDevice() {
		return this.slaveEndpoint.getDevice();
	}
	
	// Public Instance Methods
	
	/**
	 * 
	 * @param device
	 * @return l'<code>Endpoint</code> associato al <code>Device</code> passato o <code>null</code> se questo non è parte di questa connessione
	 */
	public Endpoint getEndpoint(Device device) {
		if(device.equals(this.masterEndpoint.getDevice()))
			return this.masterEndpoint;
		if(device.equals(this.slaveEndpoint.getDevice()))
			return this.slaveEndpoint;
		return null;
	}
	
	/**
	 * il<code>Device</code> associato all'<code>Endpoint</code> passato o <code>null</code> se questo non è parte di questa connessione
	 * @param endpoint
	 * @return
	 */
	public Device getDevice(Endpoint endpoint) {
		if(endpoint.equals(this.masterEndpoint))
			return this.masterEndpoint.getDevice();
		if(endpoint.equals(this.slaveEndpoint))
			return this.slaveEndpoint.getDevice();
		return null;
	}
	
	/**
	 * 
	 * @param device
	 * @return il <code>Device</code> all'altro capo di di questa connessione o <code>null</code> se il <code>Device</code> passato 
	 * non è parte di questa connessione
	 */
	public Device getOtherDevice(Device device) {
		if(device.equals(this.masterEndpoint.getDevice()))
			return this.slaveEndpoint.getDevice();
		if(device.equals(this.slaveEndpoint.getDevice()))
			return this.masterEndpoint.getDevice();
		return null;
	}
	
	/**
	 * 
	 * @param endpoint
	 * @return il <code>Device</code> all'altro capo di di questa connessione o <code>null</code> se l'<code>Endpoint</code> passato 
	 * non è parte di questa connessione
	 */
	public Device getOtherDevice(Endpoint endpoint) {
		if(endpoint.equals(this.masterEndpoint))
			return this.slaveEndpoint.getDevice();
		if(endpoint.equals(this.slaveEndpoint))
			return this.masterEndpoint.getDevice();
		return null;
	}
	
	/**
	 * 
	 * @param device
	 * @return l'<code>Endpoint</code> all'altro capo di di questa connessione o <code>null</code> se il <code>Device</code> passato 
	 * non è parte di questa connessione
	 */
	public Endpoint getOtherEndpoint(Device device) {
		if(device.equals(this.masterEndpoint.getDevice()))
			return this.slaveEndpoint;
		if(device.equals(this.slaveEndpoint.getDevice()))
			return this.masterEndpoint;
		return null;
	}
	
	/**
	 * 
	 * @param endpoint
	 * @return l'<code>Endpoint</code> all'altro capo di di questa connessione o <code>null</code> se l'<code>Endpoint</code> passato 
	 * non è parte di questa connessione
	 */
	public Endpoint getOtherEndpoint(Endpoint endpoint) {
		if(endpoint.equals(this.masterEndpoint))
			return this.slaveEndpoint;
		if(endpoint.equals(this.slaveEndpoint))
			return this.masterEndpoint;
		return null;
	}
	
	// Predicati
	
	// TODO return this.isMaster(device) || this.isSlave(device)
	
	public boolean includes(Device device){
		return device.equals(this.masterEndpoint.getDevice()) || device.equals(this.slaveEndpoint.getDevice());
	}
	
	public boolean includes(Endpoint endpoint) {
		return endpoint.equals(this.masterEndpoint) || endpoint.equals(this.slaveEndpoint);
	}
	
	public boolean isMaster(Device device) {
		return this.masterEndpoint.getDevice() == device;
	}
	
	public boolean isSlave(Device device) {
		return this.slaveEndpoint.getDevice() == device;
	}
	
	public boolean isMaster(Endpoint endpoint) {
		return this.masterEndpoint == endpoint;
	}
	
	public boolean isSlave(Endpoint endpoint) {
		return this.slaveEndpoint == endpoint;
	}
	
	public boolean isHostConnection() {
		return this.masterEndpoint == null;
	}
	
	// Object
	
	@Override
	public String toString() {
		return this.getMasterEndpoint() + " -> " + this.getSlaveEndpoint();
	}
}
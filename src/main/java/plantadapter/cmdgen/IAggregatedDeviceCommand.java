package plantadapter.cmdgen;

import plantadapter.commands.DeviceCommand;
import plantadapter.commands.PortCommand;

/**
 * <p>Contenitore di <code>DeviceCommand</code> che sono stati risolti ed aggregati in un nuovo <code>DeviceCommand</code></p>
 */
public interface IAggregatedDeviceCommand {
	/**
	 * <p>Ritorna il <code>DeviceCommand</code> risultante dall'aggregazione e risoluzione dei <code>DeviceCommand</code> a cui è associata l'istanza corrente di <code>IAggregatedDeviceCommand</code>.</p>
	 * @return <code>DeviceCommand</code> risolto.
	 */
	public PortCommand getSolvedDeviceCommand();
	
	/**
	 * <p>Ritorna i <code>DeviceCommand</code> risolti nel <code>DeviceCommand</code></p>
	 * @return Insieme di <code>DeviceCommand</code> che sono stati risolti nel <code>DeviceCommand</code> associato.
	 */
	public DeviceCommand[] getAggregatedCommands();
}
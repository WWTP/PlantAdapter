package plantadapter.dcgs;

import plantadapter.cmdgen.DeviceCommandGenerationList;
import plantadapter.cmdgen.IAggregatedDeviceCommand;

import plantadapter.excpts.InvalidCommandException;

// TODO Aggiorna Documentazione...

/**
 * Generatore di <code>IAggregatedCommand</code> a partire da <code>Command</code> ad esso associato.
 */
public interface IDeviceCommandGenerator {
	/**
	 * <p>Genera comandi elaborati e associati ai comandi passati in ingresso attraverso la <code>DeviceCommandGenerationList</code></p>
	 * @param commands <code>DeviceCommandGenerationList</code> contenente i comandi che devono essere elaborati.
	 * @return array di <code>IAggregatedCommand</code> i quali contengono: <code>Command</code> generato e un array di <code>Command</code> da cui deriva. 
	 * @throws InvalidCommandException TODO caso DeviceCommandGenerationList associata non valida per questo Device?
	 */
	public IAggregatedDeviceCommand[] generateCommands(DeviceCommandGenerationList commands) throws InvalidCommandException;
		
	/**
	 * <p>Indica se al DCG corrisponde un <code>IInputParser</code>, ovvero indica se il dispositivo a cui è associato
	 * deve comparire come <i>maschera</i> dei comandi generati.</p>
	 * @return
	 */
	public boolean hasInputParser();
	
	
	/**
	 * <p>Ritorna il dispositivo a cui è associato il DCG</p>
	 * @return
	 */
	//public T getAssociatedDevice();
}
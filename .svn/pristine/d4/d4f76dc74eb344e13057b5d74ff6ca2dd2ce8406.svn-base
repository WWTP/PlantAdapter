package tests.dependencesolver.uut;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import tests.dependencesolver.uut.DeviceCommandGenerationList;
import plantmodel.Connection;
import plantmodel.Device;

public class DependenceSolverImpl implements IDependenceSolver {
	
	private Map<Device, Set<Device>> dependences;
	
	/**
	 * <p>Navigazione ricorsiva del grafo fino alla radice, partendo dal <code>Device</code>
	 * passato e ottenendo i <i>master</i> a cui aggiungere il <code>Device</code> corrente come
	 * </i>slave</i>.</p>
	 * @param dev
	 */
	private void generateDependencies(Device dev) {
		for (Connection cnn : dev.getConnectionsAsSlave()) {
			Device master = cnn.getMasterDevice();
			// La ricorsione termina se il Master è null
			if (master != null) {
				if (!this.dependences.containsKey(master)) {
					this.dependences.put(master, new HashSet<Device>());
				}
				// Aggiunge il dispositivo visitato all'insieme di dispositivi che puntano al dispositivo master
				this.dependences.get(master).add(dev);
				// Ottiene le dipendenze del master
				this.generateDependencies(master);
			}
		}
	}
	
	// Costruttori
	
	public DependenceSolverImpl(DeviceCommandGenerationList[] cmds) {
		this.dependences = new HashMap<Device, Set<Device>>();
		// Inizializza dipendenze
		for (DeviceCommandGenerationList lst : cmds) {
			this.generateDependencies(lst.getCommandGeneratorDevice());
		}
	}
	
	// Implementazione IDependenceSolver
	
	@Override
	public boolean hasDependence(DeviceCommandGenerationList commands) {
		if (this.dependences.containsKey(commands.getCommandGeneratorDevice()))
			return this.dependences.get(commands.getCommandGeneratorDevice()).size() > 0;
		else return false;
	}

	@Override
	public void update(DeviceCommandGenerationList solvedCommands) {
		/* Elimino tutte le dipendenze rappresentate dal dispositivo associato ai comandi risolti (l'ipotesi è
		 * che le DeviceCommandGenerationList si aggiornino in modo corretto esternamente, ovvero che quando
		 * il numero di dipendenze di un dispositivo è 0 vi sia un'unica lista diretta a quel dispositivo).
		 */
		for (Set<Device> dependenceSet : this.dependences.values()) {
			dependenceSet.remove(solvedCommands.getCommandGeneratorDevice());
		}
	}
}
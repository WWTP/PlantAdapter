package plantadapter.cmdgen;

import plantadapter.commands.DeviceCommand;
import plantadapter.commands.WriteCommand;
import plantadapter.commands.dev.IInputMask;

import extree.NavigableTree;

/* TODO Esplicitare il fatto che � un albero e il perch�. Descriverne la struttura.
 * Le implementazioni devono inoltre garantire che si tratti EFFETTIVAMENTE
 * di un SINGOLO albero. Specificare il significato delle funzionalit� di base
 * che devono essere offerte e l'estendibilit� totale resa possibile da NavigableTree.
 */

/**
 * <p>Rappresenta l'albero dei comandi associato ad un <b>singolo</b> <i>raw command</i>.</p>
 * @author JCC
 *
 */
public interface IDeviceCommandTree extends NavigableTree<DeviceCommand> { // TODO Disaccoppiare questa interfaccia dalla generazione di maschere etc. (pattern Visitor all'esterno)

	public DeviceCommand[] getFirstCommands();
	
	public WriteCommand getRawCommand();

	public IInputMask getMask();
}
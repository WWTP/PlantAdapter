package tests.commandtreebuilder.uut;

import plantadapter.commands.DeviceCommand;
import plantadapter.commands.dev.IInputMask;
import utils.extree.NavigableTree;

/* TODO Esplicitare il fatto che è un albero e il perché. Descriverne la struttura.
 * Le implementazioni devono inoltre garantire che si tratti EFFETTIVAMENTE
 * di un SINGOLO albero. Specificare il significato delle funzionalità di base
 * che devono essere offerte e l'estendibilità totale resa possibile da NavigableTree.
 */

/**
 * <p>Rappresenta l'albero dei comandi associato ad un <b>singolo</b> <i>raw command</i>.</p>
 * @author JCC
 *
 */
public interface IDeviceCommandTree extends NavigableTree<DeviceCommand> {

	public DeviceCommand[] getFirstCommands();
	
	public DeviceCommand getRawCommand();

	public IInputMask getMask();
}
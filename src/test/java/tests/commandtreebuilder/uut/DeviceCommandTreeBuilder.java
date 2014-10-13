package tests.commandtreebuilder.uut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import plantadapter.commands.DeviceCommand;
import plantadapter.commands.ReadCommand;
import plantadapter.commands.dev.IInputMask;
import utils.extree.ExtensibleTree;
import utils.extree.NavigableTree;
import utils.extree.TreeNavigator;
import utils.extree.TreeNavigator.NavigationMode;
import utils.extree.TreeNode;

public class DeviceCommandTreeBuilder {
	
	private ExtensibleTree<DeviceCommand> exTree;
	
	public DeviceCommandTreeBuilder() {
		this.exTree = new ExtensibleTree<DeviceCommand>();
	}
	
	// TODO Eventualmente estrai interfaccia...
	
	/**
	 * <p>Questo metodo suppone che tutti gli <code>IAggregatedDeviceCommand</code> ottenuti mediante la risoluzione di una
	 * <code>DeviceCommandGenerationList</code> da parte di un <code>IDeviceCommandGenerator</code> gli siano passati <i>tutti</i>
	 * e <i>in sequenza</i>.</p>
	 * <p>Il passaggio di un <code>IAggregatedDeviceCommand</code> avente un <i>comando risolutore</i> recante indicazione di 
	 * un <i>target device</i> differente da quello indicato dal comando risolutore dell'ultimo <code>IAggregatedDeviceCommand</code>
	 * ricevuto è interpretato come il completamento dell'invio di tutte le informazioni relative ai comandi da risolvere per quel
	 * dispositivo. Non sarà quindi più possibile estendere l'albero con altri comandi generati dallo stesso <code>IDeviceCommandGenerator</code>.</p>
	 * @param commands
	 */
	public void add(IAggregatedDeviceCommand commands) {
		this.exTree.addBranch(commands.getSolvedDeviceCommand(), Arrays.asList(commands.getAggregatedCommands()));
	}
	
	public IDeviceCommandTree[] getTrees() {
		// Ottiene le radici dell'albero estensibile...
		List<? extends NavigableTree<DeviceCommand>> roots = this.exTree.split();
		IDeviceCommandTree[] trees = new DeviceCommandTreeImpl[roots.size()];
		int index = 0;
		/* Crea un albero di comandi per ognuno degli alberi ottenuti con exTree.split();
		 * notare che NON si tratta di ExensibleTree ma semplicemente di alberi navigabili.
		 */
		for (NavigableTree<DeviceCommand> tree : roots) {
			trees[index] = new DeviceCommandTreeImpl(tree);
			index++;
		}
		return trees;
	}
	
	private class DeviceCommandTreeImpl implements IDeviceCommandTree{
		
		private NavigableTree<DeviceCommand> innerTree;
		
		public DeviceCommandTreeImpl(NavigableTree<DeviceCommand> tree) {
			this.innerTree = tree;
		}
		
		@Override
		public void navigate(TreeNavigator<DeviceCommand> navigator) {
			this.innerTree.navigate(navigator);
		}

		@Override
		public DeviceCommand[] getFirstCommands() {
			FirstCommandsFinder finder = new FirstCommandsFinder();
			this.innerTree.navigate(finder);
			return finder.getFirstCommands();
		}

		@Override
		public DeviceCommand getRawCommand() {
			RawCommandFinder finder = new RawCommandFinder();
			this.innerTree.navigate(finder);
			return finder.getRawCommand();
		}

		@Override
		public IInputMask getMask() {
			MaskBuilder maskBuilder = new MaskBuilder();
			this.innerTree.navigate(maskBuilder);
			return maskBuilder.getMask();
		}
		
		private class FirstCommandsFinder implements TreeNavigator<DeviceCommand> {
			
			private List<DeviceCommand> firstCommands;
			
			public FirstCommandsFinder() {
				this.firstCommands = new LinkedList<DeviceCommand>();
			}
			
			public DeviceCommand[] getFirstCommands() {
				return this.firstCommands.toArray(new DeviceCommand[0]);
			}

			@Override
			public NavigationMode getChildrenNavigationMode() {
				return NavigationMode.LEFTMOST;
			}
			
			@Override
			public void preorderVisit(TreeNode<DeviceCommand> node) {
				if (node.getChildren().size() == 0) {
					// Il nodo è una foglia, dunque lo aggiungo alla lista dei primi comandi
					this.firstCommands.add(node.getElement());
				}
			}

			@Override
			public void postorderVisit(TreeNode<DeviceCommand> node) {
				/* do nothing */
			}

			@Override
			public boolean goOn() {
				return true;
			}

			@Override
			public boolean skipChilden() {
				return false;
			}
		}
		
		private class RawCommandFinder implements TreeNavigator<DeviceCommand> {

			private DeviceCommand rawCommand;
			
			public RawCommandFinder() {
				this.rawCommand = null;
			}
			
			public DeviceCommand getRawCommand() {
				return this.rawCommand;
			}
			
			@Override
			public NavigationMode getChildrenNavigationMode() {
				return NavigationMode.LEFTMOST;
			}

			@Override
			public void postorderVisit(TreeNode<DeviceCommand> node) {
				/* do nothing */
			}

			@Override
			public void preorderVisit(TreeNode<DeviceCommand> node) {
				if (this.rawCommand == null)
					this.rawCommand = node.getElement();
				
				/* TODO Qui dovrebbe semplicemente fermare la navigazione
				 * (è sufficiente che il navigator esponge un metodo "goOn()"
				 * che permetta di capire se si vuole proseguire).
				 */
			}

			@Override
			public boolean goOn() {
				return this.rawCommand == null;
			}

			@Override
			public boolean skipChilden() {
				return false;
			}
		}
		
		private class MaskBuilder implements TreeNavigator<DeviceCommand> {

			private IInputMask mask;
			private Stack<IInputMask> stack;
			
			public MaskBuilder() {
				// TODO Verificare l'accesso ai metodi dei tipi di livello superiore
				this.mask = new IInputMask.Mask(getRawCommand().getTargetDevice());
				this.stack = new Stack<IInputMask>();
			}
			
			public IInputMask getMask() {
				return this.mask;
			}
			
			@Override
			public NavigationMode getChildrenNavigationMode() {
				return NavigationMode.LEFTMOST;
			}

			@Override
			public void preorderVisit(TreeNode<DeviceCommand> node) {
				if (node.getChildren().size() > 0) { 
					// Aggiungo allo Stack prima di navigare i nodi figli
					this.stack.push(new IInputMask.Mask(node.getElement().getTargetDevice()));
				}
				else { // Comando "foglia": nella generazione della maschera devo tenere il ramo corrispondente SOLO se trovo almeno un ReadCommand...
					if (node.getElement() instanceof ReadCommand)
						this.stack.peek().asMask().add(new IInputMask.CommandMask((ReadCommand)node.getElement()));
				}
			}

			@Override
			public void postorderVisit(TreeNode<DeviceCommand> node) {
				if (node.getChildren().size() > 0) {
					if (this.stack.size() > 1) {
						IInputMask mask;
						// Aggiungo il la maschera al contenitore superiore se ho trovato almeno un ReadCommand nel sotto-albero
						if ((mask = this.stack.pop()).asMask().getSubMasksCount() > 0) {
							this.stack.peek().asMask().add(mask);
						}
					}
					else { // Sono arrivato alla radice!
						if (this.stack.peek().asMask().getSubMasksCount() > 0) {
							this.mask = this.stack.pop();
						}
						else this.mask = null;
					}
				}
				// Non fa nulla nel caso dellle foglie...
			}

			@Override
			public boolean goOn() {
				return true;
			}

			@Override
			public boolean skipChilden() {
				return false;
			}
		}
	}
}
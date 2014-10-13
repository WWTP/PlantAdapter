package plantadapter.cmdgen;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.measure.quantity.DataAmount;

import plantadapter.commands.DeviceCommand;
import plantadapter.commands.PortCommand;
import plantadapter.commands.ReadCommand;
import plantadapter.commands.TransactionCommand;
import plantadapter.commands.WriteCommand;
import plantadapter.commands.dev.IInputMask;
import utils.extree.ExtensibleTree;
import utils.extree.NavigableTree;
import utils.extree.TreeNavigator;
import utils.extree.TreeNode;

class DeviceCommandTreeBuilder {
	
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
	 * ricevuto � interpretato come il completamento dell'invio di tutte le informazioni relative ai comandi da risolvere per quel
	 * dispositivo. Non sar� quindi pi� possibile estendere l'albero con altri comandi generati dallo stesso <code>IDeviceCommandGenerator</code>.</p>
	 * @param commands
	 */
	public void add(IAggregatedDeviceCommand commands) {
		this.exTree.addBranch(commands.getSolvedDeviceCommand(), Arrays.asList(commands.getAggregatedCommands()));
	}
	
	public IDeviceCommandTree[] getTrees() {
		// Ottiene le radici dell'albero estensibile...
		List<? extends NavigableTree<DeviceCommand>> roots = this.exTree.split();
		IDeviceCommandTree[] trees = new IDeviceCommandTree[roots.size()];
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
	
	public void clear() {
		this.exTree = new ExtensibleTree<DeviceCommand>();
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
		public WriteCommand getRawCommand() {
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
					// Il nodo � una foglia, dunque lo aggiungo alla lista dei primi comandi
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

			private WriteCommand rawCommand;
			
			public RawCommandFinder() {
				this.rawCommand = null;
			}
			
			public WriteCommand getRawCommand() {
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
				if (this.rawCommand == null) {
					if (node.getElement() instanceof WriteCommand)
						this.rawCommand = (WriteCommand)node.getElement();
					else throw new IllegalStateException("Errore nella generazione dei comandi.");
				}
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
					// Aggiungo allo Stack prima di navigare i nodi figli, ma solo se non � gi� presente una maschera per la stessa istanza di Device
					// (o se lo Stack � vuoto)
					try  {
						if (this.stack.peek().asMask().getSourceDevice() != node.getElement().getTargetDevice())
							this.stack.push(new IInputMask.Mask(node.getElement().getTargetDevice()));
					}
					catch (EmptyStackException e) {
						this.stack.push(new IInputMask.Mask(node.getElement().getTargetDevice()));
					}
				}
				else 
				{
					// Se il comando � diretto ad un endpoint slave (e.g. porta di controllo di un dispositivo
					// controllato) allora devo anche aggiungere una maschera relativa a quel dispositivo (solamente 
					// se esso pu� aggiungere formato all'input, ossia se ah un'interfaccia DataAmount).
					if (((PortCommand)node.getElement()).getTargetPort().getConnectionsAsSlave().length > 0) {
						// TODO Valutare l'approccio con EndpointInterface...
						if (((PortCommand)node.getElement()).getTargetPort().getEndpointInterfacesForQuantity(DataAmount.class).length > 0)
							this.stack.push(new IInputMask.Mask(node.getElement().getTargetDevice()));
					}
					
					// Comando "foglia": nella generazione della maschera devo tenere il ramo corrispondente SOLO se trovo almeno un ReadCommand
					// (inclusi quelli trovati nei TransactionCommand).
					
					if (node.getElement() instanceof ReadCommand)
						this.stack.peek().asMask().add(new IInputMask.CommandMask((ReadCommand)node.getElement()));
					else if (node.getElement() instanceof TransactionCommand) { // TODO
						for (PortCommand rdCmd : ((TransactionCommand)node.getElement()).getCommands()) {
							if (rdCmd instanceof ReadCommand) this.stack.peek().asMask().add(new IInputMask.CommandMask((ReadCommand)rdCmd));
						}
					}
					else; // TODO Eccezione (?)
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
						try {
							if (this.stack.peek().asMask().getSubMasksCount() > 0) {
								this.mask = this.stack.pop();
							}
							else this.mask = null;
						}
						catch (EmptyStackException e) { /* do nothing */ }
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
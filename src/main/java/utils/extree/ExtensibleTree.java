package utils.extree;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/* TODO Esplicitare (e applicare) le precondizioni su eventuale presenza di nodi passati
 * nella extend() ma posti in posizioni in cui questa non sia applicabile.
 * 
 * Valutare il caso di grafi più complessi e la possibile gestione a "livelli", anche per
 * quanto riguarda le append().
 */

public class ExtensibleTree<E> implements NavigableTree<E> {
	
	// TODO Sistema la semantica di questa collezione
	private TreeNodeCollection<E> branchRoots;
	
	public ExtensibleTree() {
		this.branchRoots = new TreeNodeCollection<E>();
	}
	
	/**
	 * <p>Crea un nuovo albero avente come radice il <code>TreeNode</code> passato. Si noti
	 * che il nodo non viene copiato ma è condiviso con altri eventuali alberi di cui facesse
	 * parte prima o di cui andrà a fare parte dopo la chiamata a questo costruttore.</p>
	 * @param root
	 */
	private ExtensibleTree(TreeNode<E> root) {
		this.branchRoots = new TreeNodeCollection<E>();
		this.branchRoots.add(root);
	}
	
	/* TODO Uso l'interfaccia per non permettere estensioni successive dell'albero (problema condivisione nodi):
	 * verificare se questo approccio è corretto o se è meglio dare direttamente un ExtensibleTree;
	 * valutare se suddividere le implementazioni per evitare che il cliente possa "barare"
	 * usando un Cast (è sufficiente utilizzare un'implementazione privata di NavigableTree
	 * che sfrutti la navigate() di ExtensibleTree - così da non doverne nemmeno riscrivere il codice)...
	 * 
	 * N.B. Per ora le istanze restituite da Split possono essere solo navigate: l'albero può essere
	 * comunque esteso ma questo non influisce su queste istanze, che vengono navigate a partire dalla
	 * radice originale. Nel caso di append() - ancora da implementare - la modifica si propaga ovviamente
	 * su tutti gli alberi interessati (un nodo potrebbe anche essere "appeso" a due alberi diversi e in tal
	 * caso sarebbe visitato in ENTRAMBE le navigazioni). Eventuali MODIFICHE degli alberi fatte DURANTE la
	 * navigazione (oltre ad essere forse problematiche già problematiche per loro natura) si PROPAGANO
	 * inoltre a tutti gli alberi coinvolti (in caso di condivisione dei nodi modificati).
	 * 
	 * Può avere senso offrire la possibilità di scelta fra copia o condivisione di riferimenti...
	 */
	
	public List<? extends NavigableTree<E>> split() {
		List<ExtensibleTree<E>> trees = new ArrayList<ExtensibleTree<E>>();
		for (TreeNode<E> root : this.branchRoots) {
			trees.add(new ExtensibleTree<E>(root));
		}
		return trees;
	}
	
	/* TODO Nell'extend() sono possibili i seguenti casi:
	 * - passaggio di sole foglie (element == null). In tal caso SE NON E' GIA' PRESENTE UN NODO
	 * contenente un elemento foglia, questo viene aggiunto COME "RADICE" di un nuovo ramo
	 * (in teoria la sua presenza andrebbe verificata ricorsivamente...);
	 * - passaggio di sola radice (children == null o privo di elementi). In tal caso SE NON E' GIA'
	 * PRESETE un nodo RADICE contenente un elemento foglia, questo viene aggiunto come radice di un
	 * nuovo ramo (occorrerebbe verificare ricorsivamente la presenza all'interno dell'albero e
	 * scegliere in tal caso una politica appropriata...)
	 * - passaggio di radice e foglie. In tal caso si crea un nuovo nodo contenente l'elemento padre da
	 * aggiungersi alla collezione delle radici (o lo si RECUPERA se è già presente) e si verifica la 
	 * di nodi contenenti elementi figli all'interno della collezione stessa (in realtà occorrerebbe verificare 
	 * ricorsivamente la sua presenza) e se trovati vengono rimossi. I nodi contenenti gli elementi figli
	 * (già presenti come radici o creati ex novo) vengono "appesi" al nodo genirtore (anch'esso un nuovo
	 * nodo o un nodo già aggiunto precedentemente alla collezione delle radici.
	 * 
	 * i children passati devono essere Iterabili: l'ordine di inserimento all'interno la LISTA dei
	 * figli del nodo a cui vengono aggiunti è quello specificato dall'iteratore.
	 * 
	 * Il metodo lancia IllegalArgumentException in alcuni casi:
	 * - elemento genitore presente all'interno della lista dei figli
	 * 
	 * TODO Aggiungere overload che accetti un array (?)
	 */
	
	/**
	 * <p>Aggiunge all'istanza corrente di <code>ExtensibleTree</code> un nuovo nodo
	 * contenente l'elemento passato. Se uno degli elementi passati come figli è contenuto nella 
	 * radice di uno dei rami già presenti nell'albero, allora il nodo radice di tale ramo viene 
	 * aggiunto ai figli del nuovo nodo nella posizione in cui l'elemento figlio corrispondente 
	 * compare nella lista. In caso contrario ad ogni elemento figlio corrisponde una nuova 
	 * istanza di <code>TreeNode</code>, aggiunta ai figli del nuovo nodo nell'ordine in cui 
	 * compare l'elemento figlio corrispondente.</p>
	 * <p>In caso il nodo radice di un ramo venga aggiunto come figlio al nuovo nodo, quest'ultimo
	 * diviene la nuova radice del ramo esteso (sostituendo la radice precedente). In caso vengano
	 * solo aggiunti nuovi nodi (senza estendere alcun ramo preesistente), allora il nuovo nodo
	 * costituirà la radice di un nuovo ramo.</p>
	 * <p>I fruitori di questo metodo non dovrebbero passare riferimenti ad oggetti già presenti
	 * nell'albero ma che non siano contenuti in nodi radice di rami già presenti: in tal caso
	 * questi verrebbero trattati a tutti gli effetti come nuovi nodi (scollegati dall'albero
	 * preesistente), il che può dare luogo ad inconsistenze. Inoltre il parametro <code>element</code> 
	 * non dovrebbe mai essere già presente nell'albero.</p>
	 * @param element
	 * @param children
	 */
	public void extend(E element, Iterable<? extends E> children) {
		// Ottiene il nodo "genitore" del ramo che si sta aggiungendo (eventualmente già presente)
		TreeNode<E> parent;
		if (element != null) {
			// Se non è già presente come radice di un ramo, lo aggiungo
			if (!this.branchRoots.contains(element)) {
				parent = new TreeNode<E>(element);
				// Se non è presente, aggiungo il nuovo nodo alle radici TODO Evitabile se TreeNodeCollection fosse un "Set"
				this.branchRoots.add(parent);
			}
			// Se è già presente lo ottengo
			else parent = this.branchRoots.getNode(element);
		}
		else parent = null;
		// Se non è indicato un genitore, aggiungo semplicemente le foglie...
		if (parent == null) {
			for (E childElement : children) {
				this.extend(childElement, null); // Sfrutto ricorsivamente questa stessa funzione (!)
			}
		}
		// Se l'elemento radice è presente e sono indicati elementi figli, controlla se uno di essi è già contenuto in un nodo radice TODO Ricerca ricorsivamente...
		else if (children != null && children.iterator().hasNext()) {
			for (E childElement : children) {
				if (childElement == element) throw new IllegalArgumentException("L'elemento " + childElement + " compare sia come radice che come foglia del ramo.");
				if (this.branchRoots.contains(childElement, true)) { // Ottiene RICORSIVAMENTE il figlio...
					TreeNode<E> oldRoot = this.branchRoots.getNode(childElement, true);
					// Figlio già presente come radice di un ramo: appendo il ramo al nuovo nodo
					parent.append(oldRoot);
					// Rimuovo la vecchia radice dalla collezione di radici dei rami
					this.branchRoots.remove(oldRoot);
				}
				else {
					// Figlio non presente, lo aggiungo al ramo corrente
					parent.append(childElement);
				}
			}
		}
	}
	
	public void addElement(E element) {
		this.extend(element, null);
	}
	
	/**
	 * <p>Equivalente a <code>tree.extend(element, children);</code></p>
	 * @param element
	 * @param children
	 */
	public void addBranch(E element, List<? extends E> children) {
		// TODO Presuppone che l'utilizzatore "sappia" se l'oggetto passato è già presente nell'albero o meno (?)
		this.extend(element, children);
	}
	
	/* TODO
	 * Append: l'elemento "radice" (quello a cui aggiungere figli) deve essere già presente nell'albero. Gli elementi figli
	 * non devono essere presenti nell'albero (successivamente si potrebbe permettere di appendere "figli" contenuti in nodi
	 * già presenti purché non si violino i criteri di "direzione" già propri dell'albero (vedere se in questo può aiutare
	 * una struttura a "livelli" o se ha comunque senso metterla - probabimente non per un generico grafo diretto).
	 * 
	 * TODO: fornire metodi per inserire/rimuovere figli in una certa posizione (?)
	 * 
	 */
	
	/**
	 * <p>Operazione non supportata.</p>
	 * @param element
	 * @param children
	 */
	public void append(E element, List<E> children) {
		throw new UnsupportedOperationException();
		/* TODO
		if (this.branchRoots.contains(element)) {
			for (E child : children) 
				this.branchRoots.getNode(element).append(child);
		}*/
	}
	
	// TODO insert(E element, E children, int position), remove(E element), delete(E element, int position) ...
	
	private void navigate(TreeNode<E> node, TreeNavigator<E> navigator) {
		if (navigator.goOn()) {
			ListIterator<TreeNode<E>> iterator;
			// Visita "pre-order" del nodo
			navigator.preorderVisit(node);
			if (navigator.goOn() && !navigator.skipChilden()) {
				// Visita dei nodi figli
				switch (navigator.getChildrenNavigationMode()) {
					case LEFTMOST: {
						iterator = node.getChildren().listIterator(0);
						for (; iterator.hasNext();) {
							if (!navigator.goOn()) break;
							this.navigate(iterator.next(), navigator);
						}
						break;
					}
					case RIGHTMOST: {
						if (node.getChildren().size() > 0) {
							iterator = node.getChildren().listIterator(node.getChildren().size());
							for (; iterator.hasPrevious();) {
								if (!navigator.goOn()) break;
								this.navigate(iterator.previous(), navigator);
							}
						}
						break;
					}
					default: {
						throw new IllegalArgumentException();
					}
				}
				// Visita "post-order" del nodo
				if (navigator.goOn())
					navigator.postorderVisit(node);
			}
		}
	}
	
	@Override
	public void navigate(TreeNavigator<E> navigator) {
		for (TreeNode<E> node : this.branchRoots) {
			this.navigate(node, navigator);
		}
	}
	
	@Override
	public String toString() {
		return this.branchRoots.toString();
	}
}
package utils.extree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/* TODO Implementare una qualche interfaccia specifica di JCF (?)
 * 
 * Definire meglio la semantica di questa collezione (Lista?), in particolare
 * se contiene effettivamente solo "radici" o genericamente nodi, definire la
 * semantica delle varie operazioni in base alla semantica della collezione.
 */

public class TreeNodeCollection<E> implements Iterable<TreeNode<E>> {
	
	private List<TreeNode<E>> innerList;
	
	public TreeNodeCollection() {
		this.innerList = new ArrayList<TreeNode<E>>();
	}
	
	/**
	 * <p>Nodi <code>null</code> non vengono inseriti.</p>
	 * @param node
	 */
	public void add(TreeNode<E> node) {
		if (node != null) this.innerList.add(node);
	}
	
	// TODO Indicazioni sull'ordine e sulla differenza fra remove + add e replace
	
	public void remove(TreeNode<E> node) { // TODO Aggiungere versione con rimozione ricorsiva...
		this.innerList.remove(node);
	}
	
	private TreeNode<E> recSearch(E element, TreeNode<E> node) {
		if (node.getElement() == element) {
			return node;
		}
		else if (node.getChildren().size() > 0) {
			return recSearchAll(element, node.getChildren());
		}
		else { // Il nodo non ha figli
			return null;
		}
	}
	
	private TreeNode<E> recSearchAll(E element, TreeNodeCollection<E> cll) {
		// Memorizza i dati ottenuti nei sotto alberi
		TreeNodeCollection<E> results = new TreeNodeCollection<E>();
		for (TreeNode<E> child : cll) {
			results.add(this.recSearch(element, child)); // Nota: la lista conterrà alcuni valori null
		}
		if (results.contains(element)) return results.getNode(element);
		else return null;
	}
	
	/**
	 * 
	 * @param element
	 * @param recursion
	 * @return <code>true</code> se è stato trovato un <code>TreeNode</code> contenente l'elemento <code>element</code>,
	 * <code>false</code> altrimenti
	 */
	public boolean contains(E element, boolean recursion) {
		if (recursion) {
			return this.recSearchAll(element, this) != null;
		}
		else {
			for (TreeNode<E> node : this.innerList) {
				if (node.getElement() == element) {
					return true;
				}
			}
			return false;
		}
	}
	
	public boolean contains(E element) {
		return this.contains(element, false);
	}
	
	public boolean contains(TreeNode<E> node) {
		return this.contains(node.getElement(), false);
	}
	
	public boolean containsTreeNode(TreeNode<E> node) {
		return this.innerList.contains(node);
	}
	
	/**
	 * <p>Ricerca ricorsiva non ancora supportata.</p>
	 * @param element
	 * @param recursion
	 * @return il nodo contenente l'elemento passato o <code>null</code> se non
	 * è presente alcun nodo contenente quell'elemento
	 */
	public TreeNode<E> getNode(E element, boolean recursion) {
		if (recursion) {
			return this.recSearchAll(element, this);
		}
		else {
			for (TreeNode<E> node : this.innerList) {
				if (node.getElement() == element) {
					return node;
				}
			}
			return null;
		}
	}
	
	public TreeNode<E> getNode(E e) {
		return this.getNode(e, false);
	}

	@Override
	public Iterator<TreeNode<E>> iterator() {
		return this.innerList.iterator();
	}
	
	public ListIterator<TreeNode<E>> listIterator() {
		return this.innerList.listIterator();
	}
	
	public ListIterator<TreeNode<E>> listIterator(int index) {
		return this.innerList.listIterator(index);
	}
	
	public int size() {
		return this.innerList.size();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (TreeNode<E> root : this) {
			sb.append(root.toString());
			sb.append(", ");
		}
		if (this.size() > 0) sb.delete(sb.length() - 2, sb.length());
		sb.append("]");
		return sb.toString();
	}
}
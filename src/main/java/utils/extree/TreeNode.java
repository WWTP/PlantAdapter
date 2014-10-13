package utils.extree;

import java.util.List;

public class TreeNode<E> {
	
	// Attributes
	
	private E element;
	// TODO Utilizzare TreeNodeCollection (una volta aggiunte tutte le funzionalità necessarie) (?)
	private TreeNodeCollection<E> children;
	
	// Constructors
	
	public TreeNode(E element, List<? extends E> children) {
		this.element = element;
		if (children != null) {
			for (E child : children)
				this.children.add(new TreeNode<E>(child));
		}
		else this.children = new TreeNodeCollection<E>();
	}
	
	public TreeNode(E element) {
		this(element, null);
	}
	
	// Accessors
	
	public E getElement() {
		return this.element;
	}
	
	public TreeNodeCollection<E> getChildren() {
		return this.children;
	}
	
	// Methods
	
	/**
	 * <p>Crea un nuovo <code>TreeNode</code> contenente l'elemento passato come parametro e lo
	 * aggiunge alla lista dei <i>figli</i> del nodo corrente.</p>
	 * @param child
	 */
	public void append(E child) {
		this.children.add(new TreeNode<E>(child));
	}
	
	/**
	 * <p>Aggiunge il <code>TreeNode</code> passato come parametro alla lista dei <i>figli</i> del
	 * nodo corrente.</p>
	 * @param child
	 */
	public void append(TreeNode<E> child) {
		this.children.add(child);
	}
	
	/**
     * <p>Crea tanti <code>TreeNode</code> quanti sono gli elementi contenuti nella lista passata
     * e li aggiunge alla lista dei <i>figli</i> del nodo corrente.</p>
	 * @param children
	 */
	public void append(List<? extends E> children) {
		for (E child : children)
			this.children.add(new TreeNode<E>(child));
	}
	
	public void append(TreeNodeCollection<E> children) {
		for (TreeNode<E> child : children)
			this.children.add(child);
	}
	
	@Override
	public String toString() {
		return this.element.toString() + " -> " + this.children.toString();
	}
}
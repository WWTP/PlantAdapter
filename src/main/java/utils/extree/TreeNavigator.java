package utils.extree;

/**
 * <p>Questa interfacccia definisce i metodi che devono essere implementati da una classe le cui istanze
 * vogliano visitare i nodi di un albero. Permette di definire logiche di navigazione anche complesse,
 * in quanto la scelta fra visita <i>post-order</i>/<i>pre-order</i> e <i>left-most</i>/<i>right-most</i>
 * è applicabile al singolo nodo visitato. Viene inoltre data la possibilità di forzare la terminazione
 * della visita o di saltare specifiche parti dell'albero.</p>
 * @author JCC
 *
 * @param <E> il tipo degli elementi contenuti nell'albero che si vuole visitare
 */
public interface TreeNavigator<E> {

	/**
	 * <p>Indica se la visita dell'albero deve andare avanti. Il valore restituito da questo metodo è
	 * controllato prima di procedere con l'esecuzione dei metodi <code>preorderVisit()</code> e
	 * <code>postorderVisit()</code> o con la visita dei sotto-alberi di un nodo: se il tale valore
	 * è <code>false</code>, la visita dell'albero termina senza che nessuna delle operazioni sopra
	 * elencate venga eseguita su alcun ulteriore nodo.</p>
	 * <p>Questo metodo può essere usato, ad esempio, quando non si ha la necessità di visitare l'intero 
	 * albero ma solamente alcuni nodi, utili a perseguire un certo obiettio: una volta che tale obiettivo 
	 * viene raggiunto, il <code>TreeNavigator</code> può porre fine alla visita evitando di proseguire 
	 * inutilmente le chiamate ricorsive, guadagnando così in prestazioni.</p>
	 * @return <code>true</code> se si desidera che la visita dell'albero proceda, <code>false</code> altrimenti
	 */
	public boolean goOn();
	
	/**
	 * <p>Indica se si desidera procedere con la visita dei sotto-alberi relativi al <i>nodo corrente</i>. Il
	 * valore restituito da questo metodo viene controllato prima di iniziare la visita dei sotto-alberi e tale
	 * visita ha luogo solamente se viene restituito <code>true</code>.</p>
	 * <p>Un esempio di utilizzo si ha qualora durante l'invocazione di <code>preorderVisit()</code> il
	 * <code>TreeNavigator</code> abbia informazioni tali da inferire l'inutilità della visita dei sotto-alberi
	 * del nodo corrente: in tal caso può evitarla facendo si che questo metodo restituisca <code>true</code>.</p>
	 * @return <code>true</code> se si desidera saltare la visita dei sotto-alberi del nodo corrente, <code>false</code> altrimenti
	 */
	public boolean skipChilden();
	
	/**
	 * <p>Definisce le operazioni da eseguire non appena inizia la visita del nodo, prima di visitare i sotto-alberi
	 * del nodo medesimo. Viene invocato solamente se il metodo <code>goOn()</code> restituisce <code>true</code>.</p>
	 * @param node il nodo correntemente visitato
	 */
	public void preorderVisit(TreeNode<E> node);
	
	/**
	 * <p>Definisce le operazioni da eseguire al termine della visita del nodo, dopo aver visitato i sotto-alberi
	 * del nodo medesimo. Viene invocato solamente se il metodo <code>goOn()</code> restituisce <code>true</code>.</p>
	 * @param node il nodo correntemente visitato
	 */
	public void postorderVisit(TreeNode<E> node);
	
	/**
	 * <p>Indica come deve avvenire la navigazione dei sotto-alberi del nodo corrente, secondo le modalità definite 
	 * dall'enumeratore <code>NavigationMode</code>.</p>
	 * @return la modalità di navigazione da utilizzare per visitare i sotto-alberi del nodo corrente
	 */
	public NavigationMode getChildrenNavigationMode();
	
	/**
	 * <p>Indica le possibili modalità di navigazione dell'albero:</p>
	 * <ul>
	 * <li><code>LEFTMOST</code>: i sotto alberi devono essere visitati in ordine dal primo all'ultimo;</li>
	 * <li><code>RIGHTMOST</code>: i sotto alberi devono essere visitati in ordine dall'ultimo al primo.</li>
	 * </ul>
	 * @author JCC
	 *
	 */
	public enum NavigationMode {
		/**
		 * Indica che i sotto alberi devono essere visitati in ordine dal primo all'ultimo.
		 */
		LEFTMOST,
		/**
		 * Indica che i sotto alberi devono essere visitati in ordine dall'ultimo al primo.
		 */
		RIGHTMOST
	}
}
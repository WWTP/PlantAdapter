package plantmodel;

// TODO Aggiunta eventuali funzionalità utili anche in fase di OUTPUT

/**
 * <p>Questa interfaccia offre le informazioni di formato necessarie per leggere
 * da uno stream di <i>bytes</i>. Il metodo <code>getResponseFormatUsage()</code>
 * restituisce un di <code>IResponseFormatInformation.ResponseFormatUsage</code> 
 * che permette di determinare quali dei valori restituiti dagli altri <i>accessors</i> 
 * sono significativi al fine di determinare tale formato.</p>
 * @author JCC
 *
 */
public interface IDataFormatDescriptor {
	
	/**
	 * <p>Restituisce il numero di <i>bytes</i> che devono essere trasferiti affinché 
	 * sia possibile ottenere un dato significativo.</p>
	 * @return la lunghezza in <i>bytes</i> del dato. Il valore restituito non è 
	 * significativo se il metodo <code>getResponseFormatUsage()</code> restituisce  
	 * <code>IResponseFormatInformation.ResponseFormatUsage.DELIMITER</code>. In tal
	 * caso il valore restituito dovrebbe essere minore o uguale a 0.
	 */
	public long getDataLength();
	
	/**
	 * <p>Restituisce i possibili delimitatori, sotto forma di sequenze di <i>bytes</i>.
	 * Un dato è significativo se è seguito da uno qualunque di questi delimitatori cioè
	 * se è seguito da tutti i caratteri che, nell'ordine, compongono il delimitatore.</p>
	 * @return un array di delimitatori (a loro volta array di <i>bytes</i>). Il valore 
	 * restituito non è significativo se il metodo <code>getResponseFormatUsage()</code> 
	 * restituisce <code>IResponseFormatInformation.ResponseFormatUsage.LENGTH</code>. In
	 * tal caso il valore restituito dovrebbe essere <code>null</code>.
	 */
	public byte[][] getDataDelimiters();
	
	/**
	 * <p>Restituisce un valore di <code>IResponseFormatInformation.ResponseFormatUsage</code>
	 * che definisce la semantica dei valori restituiti dagli altri <i>accessors</i>.</p>
	 * <p>Per ogni istanza di <code>IResponseFormatInformation</code> il valore restituito 
	 * da questo metodo deve essere immutabile e sempre diverso da <code>null</code>.</p>
	 * @return
	 */
	public IDataFormatDescriptor.DataFormat getDataFormat();
	
	/**
	 * <p>Definisce la semantica delle informazioni offerte da <code>IResponseFormatInformation</code>:</p>
	 * <ul>
	 * <li><code>FIXED_LENGTH</code>: le risposte sono costituite dal numero di <i>bytes</i>
	 * indicato (hanno lunghezza fissa). Il valore restituito dal metodo <code>getResponseDelimiters()</code>
	 * di questa istanza non è significativo.</li>
	 * <li><code>DELIMITER</code>: le risposte sono fra loro separate da una delle sequenze
	 * di <i>bytes</i> indicate. Il valore restituito dal metodo <code>getResponseLength()</code>
	 * di questa istanza non è significativo.</li>
	 * <li><code>MIXED</code>: le risposte hanno la lunghezza massima indicata dal valore restituito dal
	 * metodo <code>getResponseLength()</code> ma possono essere tra loro separate da una delle sequenze 
	 * di <i>bytes</i> indicate dal metodo <code>getResponseDelimiters().</code></li>
	 * </ul>
	 * <p></p>
	 * @author JCC
	 *
	 */
	public enum DataFormat {
		FIXED_LENGTH,
		DELIMITERS,
		MIXED
	}
}
package quantities;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public interface IDigitalAmount extends IAmount {
	/**
	 * Permette di ottenera il livello digitale al quale ci si riferisce
	 * @return livello impostato nella quantit�
	 */
	int getLevel();
	
	/**
	 * Permette di ottenere il numero totale di livelli esposti dalla grandezza digitale
	 * @return numero livelli supportati
	 */
	int getLevels();
	
	/**
	 * Permette di ottenere l'unit� di misura che gestisce l'amount
	 * @return unit� di misura
	 */
	Unit<? extends Quantity> getUnit();
}

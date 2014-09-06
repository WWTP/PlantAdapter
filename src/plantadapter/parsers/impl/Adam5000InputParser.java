package plantadapter.parsers.impl;

import javax.measure.quantity.ElectricCurrent;
import javax.measure.unit.SI;

import org.jscience.physics.amount.Amount;

import plantadapter.commands.ReadCommand;
import plantadapter.commands.dev.IInputMask;
import plantadapter.results.SingleResult;
import plantmodel.Device;
import plantmodel.IDeviceIOData;
import plantmodel.adam.Adam5000Device;
import plantmodel.datatypes.ASCIIString;
import quantities.AnalogueAmount;

public class Adam5000InputParser extends AbstractInputParser {

	public Adam5000InputParser(Adam5000Device device) {
		super(device);
	}

	@Override
	protected IDeviceIOData convertToDeviceIODataType(IDeviceIOData input) {
		if (input instanceof ASCIIString) return input;
		else return ASCIIString.fromByteArray(input.toByteArray());
	}

	// TODO Sono da definire delle politiche precise relativamente alla parte sottostante...
	
	@Override
	protected SingleResult parse(ReadCommand cmd, IDeviceIOData token) {
		// TODO In realtà qui occorrerebbe sapere il comando che era stato inviato all'ADAM
		// prima che lui emettesse questo dato, in quanto il formato dipende da questo. Un modo
		// per farlo potrebbe essere quello di associare questa informazione al ReadCommand nel momento
		// in cui esso viene inviato (in pratica dal modulo che invia la Transaction) - sufficiente una
		// sotto-classe di ReadCommand che sia nota solo ai moduli specifici dell'Adam.
		
		// Nota: cablato sull'unico comando ora supportato, la cui risposta ha formato !0115.000
		// (è il "read back" dell'ultimo valore impostato, quindi nel nostro caso una corrente in mA - 
		// in realtà occorrerebbe fare opportune verifiche in base allo stato di configurazione dell'Adam).

		String input = ASCIIString.fromByteArray(token.toByteArray()).toString();
		
		// Arriva anche dell'input relativo all'esito del comando sulla seriale, in quanto anch'esso è
		// collegato ad un comando di lettura e quindi presente nella maschera (per ora lo ingoro, ma in generale
		// si dovrebbe valutare come gestire questo tipo di valori di ritorno - possibile elaborarli comunque,
		// eventualmente per restituire eventuali errori, oppure ignorarli - nel qual caso anche il comando che
		// ora viene ricevuto non dovrebbe essere incluso nella maschera).
		
		if (input.startsWith("!")) {
			double value = Double.parseDouble(input.substring(3)); // Rimuove i primi tre caratteri...
			return new SingleResult(new AnalogueAmount(ElectricCurrent.class, Amount.valueOf(value, SI.MILLI(SI.AMPERE))), cmd);
		}
		else return null; // TODO Convenzione temporanea...
	}

	@Override
	protected IDeviceIOData[] tokenizeInput(IDeviceIOData input, IInputMask mask) {
		return new IDeviceIOData[] { input }; // TODO
	}
}
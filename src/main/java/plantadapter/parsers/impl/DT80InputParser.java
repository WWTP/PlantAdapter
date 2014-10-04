package plantadapter.parsers.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.measure.quantity.DataAmount;

import org.jscience.physics.amount.Amount;

import plantadapter.commands.DeviceCommand;
import plantadapter.commands.ReadCommand;
import plantadapter.commands.dev.IInputMask;
import plantadapter.parsers.IParsingStrategy;
import plantadapter.results.SingleResult;
import plantmodel.IDeviceIOData;
import plantmodel.datatypes.ASCIIString;
import plantmodel.dt80.DT80Device;
import plantmodel.dt80.DT80Utils;
import plantmodel.dt80.DT80Utils.Info.FixedFormatFields;
import quantities.AnalogueAmount;

/* Definire se lo Scaling è nelle classi di modello o se vi è un Adapter che lo aggiunge;
 * inoltre forse sarebbe opportuno evitare questo passaggio e fare si che l'uscita dei dispositivi
 * fosse esattamente ciò che l'utilizzatore ha richiesto.
 */

/**
 * <p>Questa classe suppone che gli output del <code>DT80</code> siano in <i>fixed format</i>.</p>
 * @author JCC
 *
 */
public class DT80InputParser extends AbstractInputParser {

	public DT80InputParser(DT80Device dt80) {
		super(dt80);
	}
	
	@Override
	protected IDeviceIOData convertToDeviceIODataType(IDeviceIOData input) {
		return new ASCIIString(input.toByteArray());
	}
	
	@Override
	protected IDeviceIOData[] tokenizeInput(IDeviceIOData input, IInputMask mask) {
		ASCIIString ascii = (ASCIIString)input; // Verifica del tipo già fatta da AbstractInputParser
		List<ASCIIString> tokens = new LinkedList<ASCIIString>();
		
		Iterator<IInputMask> maskIterator = mask.asMask().iterator(); // TODO
		
		String field;
		int fieldIndex = 0;
		// Legge tutti i campi dell'input contenenti dei valori risultati da un campionamento
		while ((field = DT80Utils.readFixedFormat(ascii, FixedFormatFields.VALUE, fieldIndex)) != "") {
			IInputMask fieldMask = maskIterator.next(); // TODO
			// TODO In realtà probabilmente occorrerebbe analizzare meglio la maschera e/o disporre di altre informazioni
			// per verificare se sia opportuno effettuare questa operazione (potrebbe essere necessario avere a disposizione
			// tutto l'albero per determinare il comando usato, oppure almeno determinare che si trattava di un comando di lettura
			// sulla porta seriale in base alla connessione con il dispositivo indicato nella maschera - partendo dall'ipotesi che
			// il DCG debba mapparlo nella lettura di una stringa). Per ora suppongo di dover semplicemente rimuovere gli apici,
			// qualora presenti - il che potrebbe anche essere corretto, ma per esserne sicuri occorrerebbe vedere bene il manuale del DT80.
			if (field.startsWith("\"") && field.endsWith("\"")) {
				// Rimuove il primo e l'ultimo apice...
				field = field.substring(1, field.length()).substring(0, field.length() - 2);
			}
			
			tokens.add(new ASCIIString(field));
			fieldIndex++;
		}
		return tokens.toArray(new ASCIIString[0]);
	}
	
	protected IParsingStrategy getParsingStrategy(DeviceCommand cmd) { // TODO (per l'ADAM)...
		if (cmd instanceof ReadCommand) {
			ReadCommand rdCmd = (ReadCommand)cmd;
			if (rdCmd.getQuantity() == DataAmount.class) {
				// TODO Vedi se è sulla seriale e restituisci strategy corrispondente...
				
			}
	     	// TODO "Grandezze" digitali (?)
			else // Suppone che a parte i DataAmount esistano solo grandezze analogiche... 
			{
				return new AnalogueReadCommandParsingStrategy((DT80Device)super.getDevice());
			}
		}
		// TODO Altri tipi di comandi gestibili...
		throw new IllegalArgumentException("Comando non riconosciuto.");
	}

	@Override
	protected SingleResult parse(ReadCommand cmd, IDeviceIOData token) {
		DT80Device dt80 = (DT80Device)super.getDevice();
		// Due casi possibili: o il comando originale è rivolto ad uno degli slave del dispositivo
		// (lettura da un canale di un dispositivo collegato, e.g. una sonda) oppure è rivolto al
		// dispositivo stesso (e.g. lettura da un'uscita del dispositivo interrogato).
		// TODO: questi controlli generali dovrebbero essere fattorizzati...
		if (!(dt80.isMasterOf(cmd.getTargetDevice()) || (cmd.getTargetDevice() == dt80)))
			throw new IllegalStateException("Comando non diretto ad uno slave del dispositivo o al dispositivo stesso.");
		SingleResult result = this.getParsingStrategy(cmd).parse(token, cmd);
		if (result == null)
			throw new IllegalStateException("Errore nella lettura dell'input relativo al comando " + cmd.getCommandID());
		return result;
	}
	
	protected static class AnalogueReadCommandParsingStrategy implements IParsingStrategy {
		
		private DT80Device dt80;
		
		public AnalogueReadCommandParsingStrategy(DT80Device dt80) {
			this.dt80 = dt80;
		}
		
		@Override
		public SingleResult parse(IDeviceIOData input, DeviceCommand command) {
			ReadCommand rdCmd = (ReadCommand)command;
			double scaledValue = AbstractInputParser.getScalingTransformation(dt80, rdCmd).scale(Double.valueOf(input.toString()));
			return new SingleResult(
					new AnalogueAmount(
							rdCmd.getQuantity(), 
							Amount.valueOf(scaledValue, AbstractInputParser.getResponseMeasurementUnit(dt80, rdCmd))), rdCmd); 
		}
	}
}
package tests.parsers.dt80.readfixedformat.uut;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import plantmodel.datatypes.ASCIIString;
import plantmodel.dt80.DT80Utils.Info.FixedFormatFields;
import plantmodel.dt80.DT80Utils.Info.RecordTypes;

public class UUT {
	
	public static String readFixedFormat(ASCIIString input, FixedFormatFields field, int valueIndex) {
		if (field ==  FixedFormatFields.VALUE && valueIndex < 0)
			throw new IllegalArgumentException("valueIndex < 0");
		StringTokenizer tokenizer = new StringTokenizer(input.toString());
		String token = tokenizer.nextToken(",;");
		if (field == FixedFormatFields.RECORD_CLASS)
			return token;
		token = tokenizer.nextToken();
		if (field == FixedFormatFields.SERIAL_NUMBER)
			return token;
		token = tokenizer.nextToken();
		if (field == FixedFormatFields.JOB_NAME)
			return token;
		// Lettura TimeStamp (intero o singoli campi)
		token = tokenizer.nextToken();
		token += ",";
		token += tokenizer.nextToken();
		token += ",";
		token += tokenizer.nextToken();
		if (field == FixedFormatFields.TIMESTAMP)
			return token;
		//
		String[] dateFields = token.split(",");
		if (field == FixedFormatFields.DATE)
			return dateFields[0];
		if (field == FixedFormatFields.TIME)
			return dateFields[1];
		if (field == FixedFormatFields.SUBSECOND_TIME)
			return dateFields[2];
		token = tokenizer.nextToken();
		if (field == FixedFormatFields.RECORD_TYPE)
			return token;
		// Da qui in poi dipende dal tipo di record...
		RecordTypes recordType = RecordTypes.parse(token);
		if (recordType == RecordTypes.RealTimeData ||
				recordType == RecordTypes.NormalData) {
			// Non può essere stato chiesto il numero di righe dell'unload...
			if (field == FixedFormatFields.UNLOADED_RECORDS)
				throw new IllegalArgumentException("RecordType=" + recordType.toString());
			token = tokenizer.nextToken(); // Elimina un ';' che resta all'inizio...
			if (field == FixedFormatFields.SCHEDULE)
				return token;
			token = tokenizer.nextToken();
			if (field == FixedFormatFields.SCHEDULE_FIRSTDATA_INDEX)
				return token;
			// Devo leggere tutto fino alla fine perché non conosco il numero totale di valori...
			List<String> tokens = new ArrayList<String>();
			try {
				do {
					token = tokenizer.nextToken(); // Elimina una ',' che resta all'inizio...;
					tokens.add(token);
				}
				while(token != null);
			}
			catch (NoSuchElementException e) {
				// Ora vado avanti...
				if (field != FixedFormatFields.VALUE) {
					if (field == FixedFormatFields.CODE)
						return tokens.get(tokens.size() - 2);
					if (field == FixedFormatFields.CRC)
						return tokens.get(tokens.size() - 1);
				}
			}
			if (field == FixedFormatFields.VALUE) {
				// Convenzione: stringa vuota se il valore non è presente...
				return valueIndex >= tokens.size() - 2 ? "" : tokens.get(valueIndex);
			}
		}
		else if (recordType == RecordTypes.EndOfSchedule ||
				recordType == RecordTypes.EndOfUnload) {
			// Non può essere richiesta la schedule, né un valore di indice > 0 se è richiesto un valore...
			if (field == FixedFormatFields.SCHEDULE ||
					field == FixedFormatFields.SCHEDULE_FIRSTDATA_INDEX || (valueIndex > 0 && field == FixedFormatFields.VALUE))
				throw new IllegalArgumentException("RecordType=" + recordType.toString());
			token = tokenizer.nextToken(",").replaceFirst(";", "");; // Elimina un ';' che resta all'inizio...;
			if (field == FixedFormatFields.UNLOADED_RECORDS ||
					field == FixedFormatFields.VALUE)
				return token;
			tokenizer.nextToken(";"); // The ABCDEF01 string does not mean anything.
			token = tokenizer.nextToken(";");
			if (field == FixedFormatFields.CODE)
				return token;
			token = tokenizer.nextToken(";");
			if (field == FixedFormatFields.CRC)
				return token;
		}
		// Se non è già uscito, c'è stato qualche problema...
		throw new IllegalArgumentException("Formato non riconosciuto.");
	}
}
package plantmodel.dt80;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.measure.quantity.Quantity;

import plantmodel.IDataFormatDescriptor;
import plantmodel.datatypes.ASCIIString;
import plantmodel.dt80.DT80Utils.Info.FixedFormatFields;
import plantmodel.dt80.DT80Utils.Info.RecordTypes;

// TODO Dividi in più files (!!!)

public class DT80Utils {

	/* TODO La funzione seguente deve essere generalizzata a tutti i tipi di record disponibili,
	 * ad esempio quelli relativi agli allarmi (hanno campi diversi). Occorrerebbe usare un Tokenizer
	 * migliore (che permetta di definire un insieme di delimitatori) e definire una sorta di Pattern
	 * Strategy per i vari tipi di record possibili (eventualmente usando enumerativi diversi nei vari casi).
	 * 
	 * Elenco di altri record incontrati:
	 * 
	 * J,089404,2012/05/14,11:07:07,0.473876,2;"  MARTINA","  ADAM5024","  CONFIG","  JOB1";0085;A22C (DIRJOBS)
	 */
	
	/**
	 * <p>Nel caso venga specificato <code>FixedFormatFields.VALUE</code> questo metodo restituisce: 
	 * <ul>
	 * <li>il primo campo se non è specificato alcun indice;</li>
	 * <li>il numero di righe scaricate nel caso si tratti di uno dei <i>record</i> associati alle <i>unload</i>;</li>
	 * <li>stringa vuota se non è presente un campo avente l'indice specificato.</li>
	 * </ul>
	 * non è inoltre possibile specificare un indice diverso da <code>0</code> nel caso in cui si stia leggendo da un
	 * <i>record</i> associato ad una <i>unload</i>.</p>
	 * @param input
	 * @param field
	 * @param valueIndex
	 * @return
	 */
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
	
	public static String readFixedFormat(ASCIIString input, FixedFormatFields field) {
		return readFixedFormat(input, field, 0);
	}
	
	/* TODO Possibile mettere gli enumerativi fuori da "Info" (necessario un po' di refactoring);
	 * necessario utilizzare una classe "statica" solo se si è oltre il primo livello di innestamento dei tipi.
	 */
	
	public static class Info {
		
		public enum ChannelTypes implements IDT80Entity { // TODO Esistono anche Channel Options (!!!)
			VOLTAGE(ChannelClasses.ANALOG, "V"),
			HIGH_VOLTAGE(ChannelClasses.ANALOG, "HV"),
			CURRENT(ChannelClasses.ANALOG, "I"),
			CURRENT_LOOP(ChannelClasses.ANALOG, "L"), // 4-20 mA
			RESISTANCE(ChannelClasses.ANALOG, "R"),
			FREQUENCY(ChannelClasses.ANALOG, "F"),
			SYSTEM_VAR(ChannelClasses.SYSTEM_VARIABLE, "SV"),
			CHANNEL_VAR(ChannelClasses.CHANNEL_VARIABLE, "CV"),
			INTEGER_VAR(null, "IV"), // TODO (?)
			STRING(ChannelClasses.STRING, "$"),
			SERIAL(ChannelClasses.SERIAL, "SERIAL"),
			DIGITAL_STATE_INPUT(ChannelClasses.DIGITAL, "DS"),
			DIGITAL_NIBBLE_INPUT(ChannelClasses.DIGITAL, "DN"), // TODO Vedi opzione di default per il funzionamento base...
			DIGITAL_BYTE_INPUT(ChannelClasses.DIGITAL, "DB"), // TODO Vedi opzione di default per il funzionamento base...
			DIGITAL_STATE_OUTPUT(ChannelClasses.DIGITAL, "DSO"),
			DIGITAL_NIBBLE_OUTPUT(ChannelClasses.DIGITAL, "DNO"), // TODO Vedi opzione di default per il funzionamento base...
			DIGITAL_BYTE_OUTPUT(ChannelClasses.DIGITAL, "DBO"); // TODO Vedi opzione di default per il funzionamento base...
			// ...
			
			static ChannelTypes forQuantity(Class<? extends Quantity> quantity) {
				// TODO
				return null;
			}
			
			public static ChannelTypes getChannelTypeForQuantity(Class<? extends Quantity> q) {
				if(q.equals(javax.measure.quantity.ElectricPotential.class))
					return DT80Utils.Info.ChannelTypes.VOLTAGE;
				else if(q.equals(javax.measure.quantity.ElectricCurrent.class))
					return DT80Utils.Info.ChannelTypes.CURRENT;
				// TODO
				else throw new IllegalArgumentException(); // TODO
			}
			
			private final ChannelClasses channelClass;
			private final String symbol;
			// TODO getModifier() o altra soluzione per gli Analogue Channel Types
			
			private ChannelTypes(ChannelClasses channelClass, String symbol /*, Class<? extends Quantity> quantity*/) {
				this.channelClass = channelClass;
				this.symbol = symbol;
			}
			
			public ChannelClasses getChannelClass() {
				return this.channelClass;
			}
			
			public String getSymbol() {
				return this.symbol;
			}

			@Override
			public ASCIIString getDT80Syntax() {
				return ASCIIString.fromString(this.symbol);
			}
		}
		
		public enum ChannelModifiers implements IDT80Entity {
			PLUS("+"), MINUS("-"), ASTERISK("*"), SHARP("#"), NONE("");
			
			private final String symbol;
			
			private ChannelModifiers(String symbol) {
				this.symbol = symbol;
			}

			@Override
			public ASCIIString getDT80Syntax() {
				return ASCIIString.fromString(this.symbol);
			}
			
			@Override
			public String toString() {
				return this.symbol;
			}
		}
		
		public enum ChannelClasses {
			
			ANALOG(5, ChannelModifiers.values()),
			DIGITAL(8, ChannelModifiers.NONE),
			SERIAL(2, ChannelModifiers.NONE), // ...
			CHANNEL_VARIABLE(1000, ChannelModifiers.NONE),
			SYSTEM_VARIABLE(53, ChannelModifiers.NONE),
			STRING(50, ChannelModifiers.NONE);
						
			private int maximumChannelNumber;
			private ChannelModifiers[] applicableModifiers;
			
			private ChannelClasses(int maximumChannelNumber, ChannelModifiers... applicableModifiers) {
				this.maximumChannelNumber = maximumChannelNumber;
				if (applicableModifiers == null)
					applicableModifiers = new ChannelModifiers[0];
				this.applicableModifiers = applicableModifiers;
			}
			
			public int getMaximumChannelNumber() {
				return this.maximumChannelNumber;
			}
			
			public ChannelModifiers[] getApplicableModifiers() {
				return this.applicableModifiers;
			}
			
			public boolean isApplicableModifier(ChannelModifiers modifier) {
				for (ChannelModifiers m : this.applicableModifiers) {
					if (m == modifier) return true;
				}
				return false;
			}
		}
		
		public enum ScheduleIds implements IDT80Entity {
			RA("A"), RB("B"), RC("C"),
			RD("D"), RE("E"), RF("F"),
			RG("G"), RH("H"), RI("D"),
			RJ("J"), RK("D"), RS("S"), RX("X"),
			IMMEDIATE("*");
			
			private String symbol;
			
			private ScheduleIds(String symbol) {
				this.symbol = symbol;
			}
			
			public static ScheduleIds parse(String value) {
				for (ScheduleIds id : ScheduleIds.values()) {
					if (id.getSymbol().trim().equalsIgnoreCase(value))
						return id;
				}
				return null;
			}
			
			public String getSymbol() {
				return this.symbol;
			}

			@Override
			public ASCIIString getDT80Syntax() {
				return ASCIIString.fromString(this.symbol);
			}
		}
		
		public enum ScheduleFrequencyUnits implements IDT80Entity {
			D(60000 * 60 * 24), 
			H(60000 * 60), 
			M(60000), 
			S(1000), 
			T(1),
			// Convenzione interna: schedule continue equivalgono a frequenza < 0
			CONTINUOUS(-1);
			
			public static ScheduleFrequencyUnits getMaximumUnit(long mills) {
				if (mills % D.toMills() == 0) {
					return D;
				}
				else if (mills % H.toMills() == 0) {
					return H;
				}
				else if (mills % M.toMills() == 0) {
					return M;
				}
				else if (mills % S.toMills() == 0) {
					return S;
				}
				else return T;
			}
					
			public static int getTimesForUnit(ScheduleFrequencyUnits unit, long mills) {
				return (int)(mills / unit.toMills());
			}
			
			private long mills;
			private String symbol;
			
			private ScheduleFrequencyUnits(long mills) {
				this.mills = mills;
			}
			
			public long toMills() {
				return this.mills;
			}
			
			public String getSymbol() {
				return this.symbol;
			}

			@Override
			public ASCIIString getDT80Syntax() {
				return ASCIIString.fromString(this.symbol);
			}
		}
		
		// TODO Estendere con tutti i possibili formati...
		
		public enum FixedFormatFields {
			RECORD_CLASS, /* D=DATA, A=ALARM, E=ERROR ... */
			SERIAL_NUMBER, 
			JOB_NAME, /* es. "JOB1" n.b. la stringa include le quotes */
			TIMESTAMP, /* es. 2005/03/29,12:46:00,0.0293681 */
			DATE, /* es. 2005/03/29 */
			TIME, /* es. 12:46:00 */
			SUBSECOND_TIME, /* es. 0.0293681 */
			RECORD_TYPE, /* 0=RealTimeData, 1=NormalData, 5=EndOfSchedule, 3=EndOfUnload */
			SCHEDULE, /* A, B, ..., I, J, K */
			SCHEDULE_FIRSTDATA_INDEX,
			UNLOADED_RECORDS, /* solo per RECORD_TYPE 3 o 5 */
			VALUE, /* in numero non fissato, accesso tramite indice */
			CODE, /* SIGNIFICATO SCONOSCIUTO (0072, 0055, ...), probabilmente indica sempre il tipo di record... */
			CRC; /* hex */
		}
		
		public enum RecordClasses {
			DATA("D"), ALARM("A"), ERROR("E"); // TODO J, ...
			
			private String value;
			
			private RecordClasses(String value) {
				this.value = value;
			}
			
			public static RecordClasses parse(String value) {
				for (RecordClasses recordClass : RecordClasses.values()) {
					if (recordClass.getValue().equals(value))
						return recordClass;
				}
				return null;
			}
			
			public String getValue() {
				return this.value;
			}
		}
		
		public enum RecordTypes {
			RealTimeData("0"), 
			NormalData("1"), 
			EndOfSchedule("5"),
			EndOfUnload("3");
			
			private String value;
			
			private RecordTypes(String value) {
				this.value = value;
			}
			
			public static RecordTypes parse(String value) {
				for (RecordTypes recordType : RecordTypes.values()) {
					if (recordType.getValue().equals(value))
						return recordType;
				}
				return null;
			}
			
			public String getValue() {
				return this.value;
			}
		}
		
		/**
		 * <p>Formato dati <i>in ingresso</i> al <code>DT80</code>, ovvero <i>uscite</i> per il <code>PlantAdapter</code>.</p>
		 * @author JCC
		 *
		 */
		public static class InputFormatDescriptor implements IDataFormatDescriptor { // TODO LOL per la keyword "static" in java...

			private static InputFormatDescriptor instance;
			
			public static InputFormatDescriptor getInstance() {
				if (instance == null)
					instance = new InputFormatDescriptor();
				return instance;
			}
			
			private InputFormatDescriptor() { /* singleton */ }
			
			@Override
			public long getDataLength() {
				return 255; // TODO Verifica se il delimitatori sono esclusi o inclusi...
			}

			@Override
			public byte[][] getDataDelimiters() {
				byte[] crlf = { '\r', '\n' };
				return new byte[][] { crlf };
				
			}

			@Override
			public DataFormat getDataFormat() {
				return DataFormat.MIXED;
			}
		}
		
		/**
		 * <p>Formato dati <i>in uscita</i> al <code>DT80</code>, ovvero <i>ingressi</i> per il <code>PlantAdapter</code>.</p>
		 * @author JCC
		 *
		 */
		public static class OutputFormatDescriptor implements IDataFormatDescriptor { // TODO LOL per la keyword "static" in java...

			private static OutputFormatDescriptor instance;
			
			public static OutputFormatDescriptor getInstance() {
				if (instance == null)
					instance = new OutputFormatDescriptor();
				return instance;
			}
			
			private OutputFormatDescriptor() { /* singleton */ }
			
			@Override
			public long getDataLength() {
				return 255; // TODO Verifica se vale anche per le risposte (!)
			}

			@Override
			public byte[][] getDataDelimiters() {
				byte[] crlf = { '\r', '\n' };
				return new byte[][] { crlf };
				
			}

			@Override
			public DataFormat getDataFormat() {
				return DataFormat.DELIMITERS; // TODO Verifica se vale anche il discorso della lunghezza massima...
			}
		}
	}
}
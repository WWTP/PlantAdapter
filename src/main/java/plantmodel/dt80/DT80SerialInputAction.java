package plantmodel.dt80;

import plantmodel.datatypes.ASCIIString;

public abstract class DT80SerialInputAction implements IDT80SerialIOAction {

	public static DT80SerialInputAction getEraseReceiveBufferAction() {
		return new DT80SerialInputAction() {
			@Override
			public ASCIIString getDT80Syntax() {
				return new ASCIIString("\\e");
			}
		};
	}
	
	/**
	 * Un valore di width < 0 indica che la lunghezza non è da specificare.
	 * @param width
	 * @param type
	 * @param variable
	 * @return
	 */
	public static DT80SerialInputAction getStringDataAction(final int width, final DT80SerialInputActionFormats type, final int variable) {	
		// TODO Controllo parametri (e.g. numero variabile - eventualmente fattorizza).	
		return new DT80SerialStringDataInputAction(width, type, variable);
	}
	
	/**
	 * Lettura di stringa terminata da CR e senza specificare lunghezza massima. Il dato
	 * letto viene memorizzato nella variabile stringa indicata.<br /><br />
	 * 
	 * Equivale a <code>getStringDataAction(-1, DT80SerialInputActionFormats.STRING_CR_TERMINATED, variable)</code>.
	 * @return
	 */
	public static DT80SerialInputAction getStringDataAction(int variable) {
		return getStringDataAction(-1, DT80SerialInputActionFormats.STRING_CR_TERMINATED, variable);
	}
	
	public static class DT80SerialStringDataInputAction extends DT80SerialInputAction {
		
		private int width;
		private DT80SerialInputActionFormats type;
		private int variable;
		
		public DT80SerialStringDataInputAction(int width, DT80SerialInputActionFormats type, int variable) {
			this.width = width;
			this.type = type;
			this.variable = variable;
		}

		public boolean hasSpecifiedWidth() {
			return this.width >= 0;
		}
		
		public int getWidth() {
			return this.width;
		}

		public DT80SerialInputActionFormats getType() {
			return this.type;
		}

		public int getVariable() {
			return this.variable;
		}
		
		@Override
		public ASCIIString getDT80Syntax() {
			// TODO Generalizza formati simili...
			return ASCIIString.fromString("%" + (this.width >= 0 ? this.width : "") + this.type + "[" + this.variable + "$]");
		}
	}
	
	// TODO Da fare anche suo complementare per gli output...
	
	public enum DT80SerialInputActionFormats implements IDT80Entity {
		
		FLOATING_POINT("f"),
		DECIMAL_INTEGER("d"),
		HEXADECIMAL_INTEGER("x"),
		OCTAL_INTEGER("o"),
		DEC_HEX_OCT_INTEGER("i"),
		CHARACTER("c"),
		BINARY("b"),
		STRING_CR_TERMINATED("s"),
		STRING_WHITESPACE_TERMINATED("S");
		
		// TODO Vedere come gestire gli ultimi due casi
		// della tabella
		
		private String type;
		
		private DT80SerialInputActionFormats(String type) {
			this.type = type;
		}

		@Override
		public ASCIIString getDT80Syntax() {
			return ASCIIString.fromString(this.type);
		}
		
		@Override
		public String toString() {
			return this.getDT80Syntax().toString();
		}
	}
}
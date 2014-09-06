package plantadapter.excpts;

import org.w3c.dom.Document;

public abstract class PlantAdapterException extends Exception {
	
	// Descrizione XML dell'errore, interpretabile all'esterno in base all'ontologia
	private Document errorDescription;
	
	public PlantAdapterException() {
		super();
	}
	
	public PlantAdapterException(String message) {
		super(message);
	}
	
	public PlantAdapterException(String message, Document errorDescription) {
		super(message);
		this.errorDescription = errorDescription;
	}

	public Document getErrorDescription() {
		return errorDescription;
	}
}
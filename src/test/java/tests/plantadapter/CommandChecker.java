package tests.plantadapter;

import java.io.InputStream;

import plantadapter.communication.impl.InputSourceImpl;
import plantmodel.IDataFormatDescriptor;

public class CommandChecker {

	private InputSourceImpl is; // TODO Sto usando nel test una cosa da testare...
	private IDataFormatDescriptor inputFormatDescriptor;
	
	public CommandChecker(InputStream is, IDataFormatDescriptor inputFormatDescriptor) {
		this.is = new InputSourceImpl(is, inputFormatDescriptor);
		this.inputFormatDescriptor = inputFormatDescriptor;
	}
	
	public synchronized byte[] getCommand() {	
		try {
			return this.is.get();
		} 
		catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
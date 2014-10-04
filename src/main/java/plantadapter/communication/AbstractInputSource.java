package plantadapter.communication;

import plantmodel.IDataFormatDescriptor;

public abstract class AbstractInputSource implements ISource<byte[]> {
	
	private IDataFormatDescriptor inputFormatDescriptor;
	
	public AbstractInputSource(IDataFormatDescriptor inputFormatDescriptor) {
		this.inputFormatDescriptor = inputFormatDescriptor;
	}
	
	protected IDataFormatDescriptor getInputFormatDescriptor() {
		return this.inputFormatDescriptor;
	}
}
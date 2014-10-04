package plantmodel.dt80;

import plantmodel.datatypes.ASCIIString;

public class DT80Command implements IDT80Entity { // TODO

	private static final ASCIIString delimiters = new ASCIIString("\r\n");
	
	private IDT80Entity innerEntity;
	
	public DT80Command(IDT80Entity entity) {
		this.innerEntity = entity;
	}
	
	@Override
	public ASCIIString getDT80Syntax() {
		return ASCIIString.concat(innerEntity.getDT80Syntax(), delimiters);
	}
}
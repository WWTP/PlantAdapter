package plantmodel.dt80;

import plantmodel.datatypes.ASCIIString;

public abstract class DT80SerialOutputAction implements IDT80SerialIOAction {

	public static DT80SerialOutputAction getTextAction(ASCIIString text) {
		return new DT80SerialOutputAction(text.toString()) {};
	}
	
	private String actionText;
	
	protected DT80SerialOutputAction(String actionText) {
		this.actionText = actionText;
	}

	@Override
	public ASCIIString getDT80Syntax() {
		return ASCIIString.fromString("{" + this.actionText + "}");
	}
}
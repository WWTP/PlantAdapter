package plantmodel.dt80;

import plantmodel.datatypes.ASCIIString;

public class DT80SerialControlString extends DT80ChannelOption {

	private static String getActionsString(IDT80SerialIOAction... actions) {
		
		StringBuilder sb = new StringBuilder();
		
		for (IDT80SerialIOAction act : actions) {
			sb.append(act.getDT80Syntax().toString());
		}
		
		return sb.toString();
	}
	
	private IDT80SerialIOAction[] actions;
	
	public DT80SerialControlString(IDT80SerialIOAction... actions) {
		super(getActionsString(actions));
		
		this.actions = actions;
	}
	
	public IDT80SerialIOAction[] getIOActions() {
		return this.actions;
	}
	
	@Override
	public ASCIIString getDT80Syntax() {
		return ASCIIString.fromString("\""+ super.getDT80Syntax() + "\""); // TODO
	}
}
package plantadapter.results;

import plantadapter.commands.CommandsDiagnosticCommand;

public class CommandsDiagnosticResult extends Result {
	private CommandsDiagnosticCommand request;
	private SingleCommandDiagnosticResult[] singleResults;
	
	public CommandsDiagnosticResult(CommandsDiagnosticCommand request, SingleCommandDiagnosticResult... singleResults) {
		this.request = request;
		this.singleResults = singleResults;
	}
	
	public CommandsDiagnosticCommand getRequest() {
		return request;
	}
	
	public SingleCommandDiagnosticResult[] getResults() {
		return singleResults;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("from: " + this.getRequest().getCommandID() + System.getProperty("line.separator"));
		
		int i = 0;
		for(SingleCommandDiagnosticResult r : singleResults) {
			sb.append(r.getCommand().toString().replace('\n', ' ') + ": " + r.getState());
			if(i != singleResults.length - 1)
				sb.append(System.getProperty("line.separator"));
			else
				sb.append(System.getProperty("line.separator") + "-------------------------------------------------" + System.getProperty("line.separator"));
			i++;
		}
		return sb.toString();
	}
}

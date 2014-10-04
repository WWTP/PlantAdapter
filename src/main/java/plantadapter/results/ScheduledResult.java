package plantadapter.results;

import java.util.Date;

public class ScheduledResult extends Result {
	
	// Instance Fields
	
	private final String scheduleId;
	private final SingleResult[] results;
	
	// Constructors
	
	public ScheduledResult(Date timestamp, String scheduleId, SingleResult... results) {
		super(timestamp);

		this.scheduleId = scheduleId;
		this.results = results;
	}

	public ScheduledResult(String scheduleID, SingleResult... results) {
		this(new Date(), scheduleID, results);
	}
	
	// Accessors
	
	public String getScheduleId() {
		return this.scheduleId;
	}
	
	public SingleResult[] getResults() { // TODO Fatorizzare (?)
		return this.results; // TODO Eventualmente copia...
	}
	
	// Object
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getScheduleId() + " " + super.getTimestamp() + " { ");
		for (SingleResult result : this.getResults()) {
			sb.append(result.toString() + ", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(" }");
		
		return sb.toString();
	}
}
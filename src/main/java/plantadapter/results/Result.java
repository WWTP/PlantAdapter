package plantadapter.results;

import java.util.Date;

public abstract class Result {
	
	// Instance Fields
	
	private final Date timestamp;
	
	// Public Constructors
	
	public Result(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public Result() {
		this(new Date());
	}
	
	// Accessors
	
	public Date getTimestamp() {
		return this.timestamp;
	}
}
package plantmodel.dt80;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import plantmodel.datatypes.ASCIIString;

public class DT80ChannelOptionList implements IDT80Entity, Iterable<DT80ChannelOption> {
	
	// Public Static Fields
	
	public static final DT80ChannelOptionList EMPTY = new DT80ChannelOptionList(); // TODO Non ha senso alcuno...
	
	// Instance Fields

	private final List<DT80ChannelOption> channelOptions;
	
	// Constructors
	
	public DT80ChannelOptionList(DT80ChannelOption... channelOptions) {

		this.channelOptions = new ArrayList<DT80ChannelOption>();
		
		for (DT80ChannelOption option : channelOptions) {
			if (option == null)
				throw new IllegalArgumentException("option == null"); // TODO
			
			this.channelOptions.add(option);
		}
	}
	
	public void add(DT80ChannelOption channelOption) {
		if (channelOption != null)
			this.channelOptions.add(channelOption);
	}
	
	// Iterable<DT80ChannelOption>

	@Override
	public Iterator<DT80ChannelOption> iterator() {
		return this.channelOptions.iterator();
	}
	
	// DT80Entity
	
	@Override
	public ASCIIString getDT80Syntax() {
		
		if (this.channelOptions.size() == 0)
			return new ASCIIString();
		
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (DT80ChannelOption option : this.channelOptions) {
			sb.append(option.getDT80Syntax());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return ASCIIString.fromString(sb.toString());
	}
}
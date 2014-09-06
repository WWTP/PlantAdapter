package plantmodel.dt80;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import plantmodel.datatypes.ASCIIString;

public class DT80ChannelDefinitionList implements IDT80Entity, Iterable<DT80ChannelDefinition> {
	
	// Instance Fields

	private final List<DT80ChannelDefinition> channelDefinitions;
	
	// Constructors
	
	public DT80ChannelDefinitionList(DT80ChannelDefinition... channelDefinitions) {

		this.channelDefinitions = new ArrayList<DT80ChannelDefinition>();
		
		for (DT80ChannelDefinition channelDefinition : channelDefinitions) {
			if (channelDefinition == null)
				throw new IllegalArgumentException("channelDefinition == null"); // TODO
			
			this.channelDefinitions.add(channelDefinition);
		}
	}
	
	public void add(DT80ChannelDefinition channelDefinition) {
		if (channelDefinition != null)
			this.channelDefinitions.add(channelDefinition);
	}
	
	public void remove(DT80ChannelDefinition channelDefinition) {
		if (channelDefinition != null)
			this.channelDefinitions.remove(channelDefinition);
	}
	
	// DT80Entity
	
	@Override
	public ASCIIString getDT80Syntax() {
		StringBuilder sb = new StringBuilder();
		for (DT80ChannelDefinition option : this.channelDefinitions) {
			sb.append(option.getDT80Syntax());
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		return ASCIIString.fromString(sb.toString());
	}

	@Override
	public Iterator<DT80ChannelDefinition> iterator() {
		return this.channelDefinitions.iterator();
	}
}
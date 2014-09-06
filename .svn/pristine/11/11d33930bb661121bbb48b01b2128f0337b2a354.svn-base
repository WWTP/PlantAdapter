package plantmodel.dt80;

import plantmodel.datatypes.ASCIIString;
import plantmodel.dt80.DT80Utils.Info.ChannelClasses;
import plantmodel.dt80.DT80Utils.Info.ChannelModifiers;
import plantmodel.dt80.DT80Utils.Info.ChannelTypes;

public class DT80ChannelDefinition implements IDT80Entity {
	
	private final int channelNumber;
	private final ChannelTypes channelType;
	private final ChannelModifiers channelModifier;
	private final DT80ChannelOptionList channelOptions;
	
	// Constructors
	
	public DT80ChannelDefinition(int channelNumber, ChannelTypes channelType, ChannelModifiers channelModifier, DT80ChannelOptionList channelOptions) {
		if (!channelType.getChannelClass().isApplicableModifier(channelModifier))
			throw new IllegalArgumentException("ChannelModifier non valido per la ChannelClass " + channelType.getChannelClass());
		if (channelNumber > channelType.getChannelClass().getMaximumChannelNumber())
			throw new IllegalArgumentException("ChannelNumber superiore al massimo consentito.");
		// Inizializzazione Campi
		this.channelNumber = channelNumber;
		this.channelType = channelType;
		this.channelModifier = channelModifier;
		this.channelOptions = channelOptions;
	}
	
	public DT80ChannelDefinition(int channelNumber, ChannelTypes channelType, DT80ChannelOptionList channelOptions) {
		this(channelNumber, channelType, ChannelModifiers.NONE, channelOptions);
	}
	
	public DT80ChannelDefinition(int channelNumber, ChannelTypes channelType, ChannelModifiers channelModifier) {
		this(channelNumber, channelType, channelModifier, DT80ChannelOptionList.EMPTY);
	}
	
	public DT80ChannelDefinition(int channelNumber, ChannelTypes channelType) {
		this(channelNumber, channelType, ChannelModifiers.NONE, DT80ChannelOptionList.EMPTY);
	}
	
	// Accessors
	
	public int getChannelNumber() {
		return this.channelNumber;
	}
	
	public ChannelTypes getChannelType() {
		return this.channelType;
	}
	
	public ChannelModifiers getChannelModifier() {
		return this.channelModifier;
	}
	
	public ChannelClasses getChannelClass() {
		return this.channelType.getChannelClass();
	}
	
	public DT80ChannelOptionList getChannelOptions() {
		return this.channelOptions;
	}
	
	// DT80Entity

	@Override
	public ASCIIString getDT80Syntax() {
		if(this.channelModifier != ChannelModifiers.NONE)
			if(this.channelOptions != DT80ChannelOptionList.EMPTY)
				return ASCIIString.concat(
						ASCIIString.fromString("" + this.channelNumber),
						this.channelModifier.getDT80Syntax(),
						this.channelType.getDT80Syntax(),
						this.channelOptions.getDT80Syntax());
			else
				return ASCIIString.concat(
						ASCIIString.fromString("" + this.channelNumber),
						this.channelModifier.getDT80Syntax(),
						this.channelType.getDT80Syntax());
		else
			if(this.channelOptions != DT80ChannelOptionList.EMPTY)
				return ASCIIString.concat(
						ASCIIString.fromString("" + this.channelNumber),
						this.channelType.getDT80Syntax(),
						this.channelOptions.getDT80Syntax());
			else
				return ASCIIString.concat(
						ASCIIString.fromString("" + this.channelNumber),
						this.channelType.getDT80Syntax());
	}
	
	// Object
	
	@Override
	public String toString() {
		return this.getDT80Syntax().toString();
	}
}
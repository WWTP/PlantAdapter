package plantmodel.dt80;

import plantmodel.datatypes.ASCIIString;

public class DT80DigitalStateOutputChannelDefinition extends
		DT80DigitalChannelDefinition {

	private static int UNDEFINED = -1;
	
	/**
	 * Livello digitale da impostare
	 */
	private int outputLevel;
	
	// TODO gestire ChannelOption per Delay e inversione (R)
	// TODO gestire caso nessun livello di output impostato: LETTURA ultimo stato impostato in output (!= stato attuale in quanto è tristate se pin è in input).
	
	// Caso scrittura livello digitale su Endpoint
	public DT80DigitalStateOutputChannelDefinition(int channelNumber, int outputLevel) {
		super(channelNumber, DT80Utils.Info.ChannelTypes.DIGITAL_STATE_OUTPUT);
		
		if(outputLevel > 1 || outputLevel < 0)
			throw new IllegalArgumentException();
		
		this.outputLevel = outputLevel;
	}
	
	// Caso scrittura livello digitale su Endpoint
	public DT80DigitalStateOutputChannelDefinition(int channelNumber, boolean outputLevel) {
		this(channelNumber, outputLevel? 1 : 0);
	}
	
	// Caso nessuna scrittura (vedere pagina 230 manuale DT80)
	public DT80DigitalStateOutputChannelDefinition(int channelNumber) {
		super(channelNumber, DT80Utils.Info.ChannelTypes.DIGITAL_STATE_OUTPUT);
		this.outputLevel = UNDEFINED;
	}
	
	@Override
	public ASCIIString getDT80Syntax()
	{
		return ASCIIString.concat(super.getDT80Syntax(), ASCIIString.fromString(
				this.outputLevel != UNDEFINED ? "=" + this.outputLevel : ""
		));
	}
}

package plantmodel.dt80;

import java.util.List;

import plantmodel.datatypes.ASCIIString;

// TODO Definire bene l'utilizzo (Pattern Flyweight o simile)...

public class DT80Span extends DT80ChannelOption implements IDT80Entity {
	
	// TODO Vincolo: gli Spans sono al massimo 50 (ognuno identificato da un numero) per uno specifico DT80
	private int n;
	private double lowerSignal, lowerPhysical, upperSignal, upperPhysical;
	private String label;
	/* TODO Canali ai quali è applicato lo Span (nota: teoricamente valido solo all'interno di una determinata schedule!!! - sono quindi possibili duplicati);
	 * per lo scopo dei Parser e per non rendere necessario 
	 */
	private List<DT80ChannelDefinition> channels; // TODO

	// TODO Permetti di specificare 'n' e l'eventuale label ("units"), oltre ai CANALI ai quali è applicata
	
	public DT80Span(int n, String label, double lowerSignal, double lowerPhysical,
			double upperSignal, double upperPhysical)
	{
		super("S" + n);
		// TODO Controlli sul valore di 'n' e sulla lunghezza della 'label'
		this.n = n;
		this.label = label;
		this.lowerSignal = lowerSignal;
		this.lowerPhysical = lowerPhysical;
		this.upperSignal = upperSignal;
		this.upperPhysical = upperPhysical;
	}
	
	/// Accessors
	/**
	 * @return the n
	 */
	public int getN() {
		return n;
	}

	/**
	 * @return the lowerSignal
	 */
	public double getLowerSignal() {
		return lowerSignal;
	}

	/**
	 * @return the lowerPhysical
	 */
	public double getLowerPhysical() {
		return lowerPhysical;
	}

	/**
	 * @return the upperSignal
	 */
	public double getUpperSignal() {
		return upperSignal;
	}

	/**
	 * @return the upperPhysical
	 */
	public double getUpperPhysical() {
		return upperPhysical;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	//\ Accessors
	// TODO Altri costruttori
	
	@Override
	public ASCIIString getDT80Syntax() {
		return ASCIIString.fromString("S" + this.n);
	}
	
	@Override
	public String toString() {
		return this.getDT80Syntax().toString();
	}

	/**
	 * Aggiunge una ChannelDefinition fra quelle per cui lo Span è in uso
	 * @param channelDefinition
	 */
	public void addChannelDefinitionInUse(DT80ChannelDefinition channelDefinition) {
		this.channels.add(channelDefinition);
	}
}

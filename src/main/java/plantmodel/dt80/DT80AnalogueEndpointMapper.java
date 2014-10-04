package plantmodel.dt80;

import java.util.HashMap;
import java.util.Map;

import plantmodel.Endpoint;
import plantmodel.dt80.DT80Utils.Info.ChannelModifiers;

/**
 * Associa un Endpoint (usato in modo analogico) al canale + modificatore per poterla sfruttare.
 */
// TODO SOLO ANALOGICO? DIGITALI POSSONO AVERE MODIFIER? (NO by JCC)
// TODO Per il momento gestiti solo Endpoint Analogici quindi trascuro questa opzione per semplicità
public class DT80AnalogueEndpointMapper {
	private Map<Endpoint, AnalogueChannelEntry> map = new HashMap<Endpoint, AnalogueChannelEntry>();
	
	/**
	 * Aggiunge una associazione tra Endpoint e canale DT80
	 * @param e Endpoint da associare
	 * @param channelNumber numero della porta del DT80 a cui è associato l'Endpoint
	 */
	public void add(Endpoint e, int channelNumber){
		if(this.map.containsKey(e))
			throw new IllegalStateException("Endpoint already mapped.");
		map.put(e, new AnalogueChannelEntry(channelNumber, null));
	}
	
	/**
	 * Aggiunge una associazione tra Endpoint e canale DT80 + eventuale modificatore
	 * @param e Endpoint da associare
	 * @param channelNumber numero della porta del DT80 a cui è associato l'Endpoint
	 * @param modifier modificatore 
	 */
	public void add(Endpoint e, int channelNumber, ChannelModifiers modifier){
		if(this.map.containsKey(e))
			throw new IllegalStateException("Endpoint already mapped.");
		map.put(e, new AnalogueChannelEntry(channelNumber, modifier));
	}
	
	/**
	 * Ritorna gli <code>Endpoint</code> mappati.
	 * @return
	 */
	public Endpoint[] getAnalogueEndpoints(){
		return this.map.keySet().toArray(new Endpoint[0]);
	}
	
	/**
	 * Ritorna l'Endpoint associato all'impostazione del canale definita.
	 * @param channelNumber
	 * @param modifier
	 * @return Ritorna l'Endpoint associato all'impostazione del canale definita, null se non presente.
	 */
	public Endpoint getAnalogueEndpoint(int channelNumber, ChannelModifiers modifier){
		for(Map.Entry<Endpoint, AnalogueChannelEntry> entry : map.entrySet())
			if(entry.getValue().channelNumber == channelNumber && entry.getValue().modifier.equals(modifier))
				return entry.getKey();
		return null;
	}
	
	public int getChannelNumber(Endpoint e){
		// TODO gestione caso endpoint non sia in map
		return this.map.get(e).channelNumber;
	}
	
	public ChannelModifiers getChannelModifier(Endpoint e){
		return this.map.get(e).modifier;
	}
	
	private class AnalogueChannelEntry {
		private int channelNumber;
		private ChannelModifiers modifier;
		
		public AnalogueChannelEntry(int channelNumber, ChannelModifiers modifier){
			if(channelNumber < 1 || channelNumber > DT80Utils.Info.ChannelClasses.ANALOG.getMaximumChannelNumber() )
				throw new IllegalArgumentException("ChannelNumber NOT VALID, it must be between 1 and " + DT80Utils.Info.ChannelClasses.ANALOG.getMaximumChannelNumber());
			this.channelNumber = channelNumber;
			this.modifier = modifier == null ? ChannelModifiers.NONE : modifier;
		}
//		
//		public int getChannelNumber(){
//			return this.channelNumber;
//		}
//		
//		public ChannelModifiers getModifier(){
//			return this.modifier;
//		}
		
		@Override
		public String toString(){
			return this.channelNumber + this.modifier.toString();
		}
	}
}

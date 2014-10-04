package plantmodel.dt80;

import plantmodel.datatypes.ASCIIString;

/* Channels Options di interesse:
 * Channel Factor
 * Scaling Sn
 * (Variables es. =nCV)
 * Destination (NR, NL, ND, W(!))
 * 
 * Opzioni per SERIAL (da 244):
 * nSERIAL("{output actions}",options)
 * nSERIAL("input actions",options)
 * 
 * Nota: per effettuare una lettura seguita da scrittura
 * (i.e. TransactionCommand) occorre mischiare le due
 * sintassi, e.g.
 * 
 * 2SERIAL("\e{$01S0C06\013}%s")
 * 
 * dove \e serve per svuotare il buffer e %s indica la lettura
 * di una stringa terminata da CR (possibile indicare anche formati
 * più complessi).
 */

public class DT80ChannelOption implements IDT80Entity {
	
	private final String channelOption;
	
	public DT80ChannelOption(String channelOption) { // TODO
		this.channelOption = channelOption;
	}

	@Override
	public ASCIIString getDT80Syntax() {
		return ASCIIString.fromString(channelOption);
	}
	
	@Override
	public String toString() {
		return this.channelOption;
	}
}
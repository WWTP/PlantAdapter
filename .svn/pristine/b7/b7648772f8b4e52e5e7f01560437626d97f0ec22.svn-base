package plantadapter;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import plantadapter.commands.dev.IInputMask;
import plantadapter.communication.AbstractMailbox;
import plantmodel.Device;

public class RequestMailboxFactory {

	private static HashMap<Device,PendingRequestMailbox> instances; // TODO
	
	static {
		instances = new HashMap<Device,PendingRequestMailbox>();
		// TODO Generazione Mailbox per ogni dispositivo sorgente
	}
	
	// TODO Valutare questo approccio, meglio Mailbox creata "by need" (?)
	public PendingRequestMailbox newInstance(Device device) throws IllegalArgumentException {
		if(instances.containsKey(device))
			throw new IllegalArgumentException(); // TODO
		PendingRequestMailbox mailbox = new PendingRequestMailbox();
		instances.put(device, mailbox);
		return mailbox;
	}
	
	/**
	 * <p>Ritorna una istanza di <code>PendingRequestMailbox</code> a seconda del Device passato.</p>
	 * @param device <code>Device</code> sorgente di cui si vuole ricavare la <code>PendingRequestMailbox</code> associata.
	 * @return <code>PendingRequestMailbox</code> associata al <code>Device</code> passato.
	 * @throws IllegalArgumentException nel caso il <code>Device</code> passato non sia collegato direttamente al sistema.
	 */
	public PendingRequestMailbox getInstance(Device device) throws IllegalArgumentException {
		if(instances.containsKey(device)){
			return instances.get(device);
		}
		else 
		{
			System.out.println("");
			
			throw new IllegalArgumentException(); // TODO
		}
	}
	
	public RequestMailboxFactory() {
		// TODO
	}
	
	public static class PendingRequestMailbox extends AbstractMailbox<IInputMask> { // TODO

		/* TODO valutare se è utile aggiungere un Device a cui è riferito la PendingRequestMailbox; 
		 * in altre parole: può essere utile ricavarsi, conoscendo la PendingRequestMailbox, il Device a cui è associata?
		 */
		
		// private Device device;
		
		private PendingRequestMailbox() {
			super(new LinkedBlockingQueue<IInputMask>());
		}
	}
}
package plantadapter.communication;

import java.util.concurrent.BlockingQueue;

import plantadapter.excpts.FullMailboxException; // TODO

public class AbstractMailbox<T> implements IMailbox<T> { // TODO
	
	private BlockingQueue<T> innerQueue;
	
	/**
	 * <p>Lascia alle classi derivate la libertà di scegliere la particolare
	 * implementazione di <code>BlockingQueue</code> da utilizzare.</p>
	 * @param queue
	 */
	protected AbstractMailbox(BlockingQueue<T> queue) {
		this.innerQueue = queue;
	}

	@Override
	public void put(T message) throws FullMailboxException {
		try 
		{
			this.innerQueue.add(message);
		}
		catch (IllegalStateException e) {
			throw new FullMailboxException();
		}
	}

	@Override
	public T get() throws InterruptedException {
		return this.innerQueue.take();
	}
}
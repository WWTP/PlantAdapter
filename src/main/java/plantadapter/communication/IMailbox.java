package plantadapter.communication;

import plantadapter.excpts.FullMailboxException; // TODO Eliminare dipendenza dall'interfaccia (?)

public interface IMailbox<T> extends ISink<T>, ISource<T> {

	@Override
	public void put(T message) throws FullMailboxException;

	@Override
	public T get() throws InterruptedException;
}
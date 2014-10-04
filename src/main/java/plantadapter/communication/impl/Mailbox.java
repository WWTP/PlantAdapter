package plantadapter.communication.impl;

import java.util.concurrent.LinkedBlockingQueue;

import plantadapter.communication.AbstractMailbox;

public class Mailbox<T> extends AbstractMailbox<T> {
	
	public Mailbox() {
		super(new LinkedBlockingQueue<T>());
	}
}
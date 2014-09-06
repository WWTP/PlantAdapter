package testbench;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class IPServerActivationThread extends ServerActivationThread {

	private ServerSocket server;
	private Socket cnn;
	
	private boolean waiting;
	
	public IPServerActivationThread(int port, boolean reuseAddress) throws IOException {
		this.server = new ServerSocket(port);
		this.server.setReuseAddress(reuseAddress);
		this.waiting = true;
		this.cnn = null;
	}
	
	public IPServerActivationThread(int port) throws IOException {
		this(port, false);
	}
	
	public IPServerActivationThread() throws IOException {
		this(0);
	}
	
	public InetAddress getServerInetAddres() {
		return this.server.getInetAddress();
	}
	
	public int getServerLocalPort() {
		return this.server.getLocalPort();
	}
	
	@Override
	public void acceptRequest() {
		// attende e accetta la richiesta
		try {
			this.cnn = this.server.accept();
		} 
		catch (IOException e) {
			// this.cnn == null;
		}
		
		this.waiting = false;
	}

	@Override
	protected boolean waiting() {
		return this.waiting;
	}

	@Override
	protected InputStream getInputStreamImpl() {
		try {
			return this.cnn == null ? null : this.cnn.getInputStream();
		} 
		catch (IOException e) {
			return null;
		}
	}

	@Override
	protected OutputStream getOutputStreamImpl() {
		try {
			return this.cnn == null ? null : this.cnn.getOutputStream();
		} 
		catch (IOException e) {
			return null;
		}
	} 	
}
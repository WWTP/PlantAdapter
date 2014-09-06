package plantadapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import plantmodel.Connection;
import plantmodel.IPConnection;

public class CommunicationFactory {
	
	private static CommunicationFactory instance;

	public static CommunicationFactory getInstance() {
		if (instance == null)
			instance = new CommunicationFactory();
		return instance;
	}
	
	private static CommunicationFactory createFactory(Connection cnn) {
		
		CommunicationFactory factory = null;
		
		if (cnn instanceof IPConnection) {
			IPConnection ipCnn = (IPConnection)cnn;
			factory = new IPCommunicationFactory(ipCnn.getAddress(), ipCnn.getPort());
		}
		// TODO...
		
		return factory;
	}
	
	private Map<Connection, CommunicationFactory> factories;
	
	public CommunicationFactory() {
		this.factories = new HashMap<Connection, CommunicationFactory>();
	}
	
	public IOStreams getIOStreams(Connection cnn) {
		if (!this.factories.containsKey(cnn))
			this.factories.put(cnn, createFactory(cnn));
		return this.factories.get(cnn).getIOStreams(cnn);
	}
	
	public static class IOStreams {

		private InputStream is;
		private OutputStream os;
		
		public IOStreams(InputStream is, OutputStream os) {
			this.is = is;
			this.os = os;
		}

		public InputStream getInputStream() {
			return this.is;
		}

		public OutputStream getOutputStream() {
			return this.os;
		}
	}
	
	private static class IPCommunicationFactory extends CommunicationFactory {
		
		private IOStreams ioStreams;
		
		public IPCommunicationFactory(String address, int port) {
			
			try 
			{
				Socket socket = new Socket(address, port);	
				this.ioStreams = new IOStreams(socket.getInputStream(), socket.getOutputStream());
			} 
			catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public IOStreams getIOStreams(Connection cnn) {
			return this.ioStreams;
		}
	}
}
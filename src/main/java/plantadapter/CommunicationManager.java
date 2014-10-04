package plantadapter;

import java.util.HashMap;
import java.util.Map;

import plantadapter.commands.WriteCommand;
import plantadapter.communication.ISink;
import plantadapter.communication.ISource;
import plantadapter.communication.impl.CommandSenderImpl;
import plantadapter.communication.impl.InputSourceImpl;

import plantadapter.excpts.ConnectionNotFoundException;

import plantmodel.Connection;
import plantmodel.Device;
import plantmodel.IDataFormatDescriptor;
import plantmodel.dt80.DT80Utils;

/**
 * Gestisce comunicazioni (seguendo pattern FlyWeight) permettendo di ottenere sia ICommandSender che IInputSource
 * 
 */
class CommunicationManager implements ICommunicationManager {
	
	private Map<Connection, ConnectionInterfaces> connectionInterfaces;
	
	public CommunicationManager(Device[] rootDevices) {
		this.connectionInterfaces = new HashMap<Connection, ConnectionInterfaces>();
		for(Device rootDevice : rootDevices)
			for(Connection connection : rootDevice.getConnectionsAsSlave())
				if(connection.getMasterEndpoint() == null)
					// aggiungi Connessione a quelle gestibili da CommunicationManager
					this.connectionInterfaces.put(connection, null); // ConnectionInterfaces istanziato solo alla richiesta.
	}
	
	// TODO
	private IDataFormatDescriptor getDeviceInputFormatDescriptor(Connection cnn)
	{
		// TODO Non è assolutamente generale -> serve accesso standard a questa informazione
		// per tutti i dispositivi (eventualmente mettere in classe Device).
		return DT80Utils.Info.InputFormatDescriptor.getInstance(); // TODO !!!
	}
	
	// TODO Sistema sit due metodi quasi uguali...
	
	@Override
	public ISource<byte[]> getInputSource(Connection connection) throws ConnectionNotFoundException{
		if(this.connectionInterfaces.containsKey(connection))
			if(this.connectionInterfaces.get(connection) == null /*|| this.connectionInterfaces.get(connection).getInputSource().isActive()*/){
				this.connectionInterfaces.remove(connection);
				CommunicationFactory cf = CommunicationFactory.getInstance(); // TODO
				// InputReceiver e CommandSender sono classi concrete
				ISource<byte[]> is = new InputSourceImpl(cf.getIOStreams(connection).getInputStream(), getDeviceInputFormatDescriptor(connection));
				this.connectionInterfaces.put(connection, new ConnectionInterfaces(is, new CommandSenderImpl(cf.getIOStreams(connection).getOutputStream())));
				return is;
			}
			else{
				// CommunicationInterface già istanziata e ancora valida
				return this.connectionInterfaces.get(connection).getInputSource();
			}
		else
			throw new ConnectionNotFoundException();
	}
	
	@Override
	public ISink<WriteCommand> getCommandSender(Connection connection) throws ConnectionNotFoundException{
		if(this.connectionInterfaces.containsKey(connection))
			if(this.connectionInterfaces.get(connection) == null /*|| this.connectionInterfaces.get(connection).getInputSource().isActive()*/){
				this.connectionInterfaces.remove(connection);
				CommunicationFactory cf = CommunicationFactory.getInstance(); // TODO
				// InputReceiver e CommandSender sono classi concrete
				CommandSenderImpl cs = new CommandSenderImpl(cf.getIOStreams(connection).getOutputStream());
				this.connectionInterfaces.put(connection, new ConnectionInterfaces(new InputSourceImpl(cf.getIOStreams(connection).getInputStream(), getDeviceInputFormatDescriptor(connection)), cs));
				return cs;
			}
			else{
				// CommunicationInterface già istanziata e ancora valida
				return this.connectionInterfaces.get(connection).getCommandSender();
			}
		else
			throw new ConnectionNotFoundException();
	}
	
	class ConnectionInterfaces{
		
		ISource<byte[]> in;
		ISink<WriteCommand> out;
		
		public ConnectionInterfaces(ISource<byte[]> in, ISink<WriteCommand> out){
			this.in = in;
			this.out = out;
		}

		public ISource<byte[]> getInputSource(){
			return this.in;
		}
		
		public ISink<WriteCommand> getCommandSender(){
			return this.out;
		}
	}
	
	/*
	
	protected static interface ICommunicationFactory {
		public OutputStream getOutputStream(Connection connection);
		public InputStream getInputStream(Connection connection);
	}
	
	*/
	
	/** si occupa di istanziare e gestire le comunicazioni realmente (socket, etc...) e di tenerne traccia **/
	
	/*
 
	static class CommunicationFactory implements ICommunicationFactory{
		private static final String DT80_ADDR = "192.168.44.1";
		private static CommunicationFactory cf;
		private Socket sock;
		
		// istanzio una CommunicationFactory per ogni tipo di connessione supportata
		
		public static CommunicationFactory getInstance(Connection connection){
			if(cf == null)
				cf = new CommunicationFactory();
			return cf;
		}

		// TODO
		// Istanzia solo comunicazione diretta con DT80
		protected CommunicationFactory() {
			try {
				sock = new Socket(DT80_ADDR, 2000);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public OutputStream getOutputStream(Connection connection) {
			try {
				return sock.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		public InputStream getInputStream(Connection connection) {
			try {
				return sock.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	
	*/
}
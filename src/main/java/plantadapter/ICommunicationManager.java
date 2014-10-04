package plantadapter;


import plantadapter.commands.WriteCommand;
import plantadapter.communication.ISink;
import plantadapter.communication.ISource;

import plantadapter.excpts.ConnectionNotFoundException;

import plantmodel.Connection;

public interface ICommunicationManager {

	public ISource<byte[]> getInputSource(Connection connection) throws ConnectionNotFoundException;
	
	public ISink<WriteCommand> getCommandSender(Connection connection) throws ConnectionNotFoundException;
}
package tests.plantadapter;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import plantadapter.CommunicationFactory;
import plantadapter.ICommunicationManager;


import plantadapter.com.ISink;
import plantadapter.com.ISource;
import plantadapter.com.impl.CommandSenderImpl;
import plantadapter.com.impl.InputSourceImpl;
import plantadapter.commands.WriteCommand;

import plantadapter.excpts.ConnectionNotFoundException;

import plantmodel.Connection;
import plantmodel.dt80.DT80Utils;

public class DummyCommunicationManager implements ICommunicationManager {

	private PipedInputStream is;
	private PipedOutputStream os;
	/*
	public DummyCommunicationManager(PipedInputStream is, PipedOutputStream os) {
		this.is = is;
		this.os = os;
	}*/

	@Override
	public ISource<byte[]> getInputSource(Connection connection)
			throws ConnectionNotFoundException {
		return new InputSourceImpl(is, DT80Utils.Info.InputFormatDescriptor.getInstance()); // TODO
	}

	@Override
	public ISink<WriteCommand> getCommandSender(Connection connection)
			throws ConnectionNotFoundException {
		return new CommandSenderImpl(os); // TODO
	}
}
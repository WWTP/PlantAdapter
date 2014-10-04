package plantadapter.dcgs.impl;

import java.util.ArrayList;
import java.util.List;

import plantadapter.cmdgen.AggregatedDeviceCommandImpl;
import plantadapter.cmdgen.DeviceCommandGenerationList;
import plantadapter.cmdgen.IAggregatedDeviceCommand;
import plantadapter.commands.DeviceCommand;
import plantadapter.dcgs.IDeviceCommandBuilder;
import plantadapter.excpts.InvalidCommandException;

import plantmodel.Device;

public abstract class SequentialDeviceCommandGenerator<T extends Device> extends AbstractDeviceCommandGenerator<T> {

	public SequentialDeviceCommandGenerator(T associatedDevice){
		super(associatedDevice);
	}
	
	/**
	 * Restituisce l'istanza che si occupa di scegliere quale ISingleDeviceCommandGenerator utilizzare per gestire il comando passato
	 * @param command comando da generare
	 * @return 
	 */
	// TemplateMethod
	protected abstract IDeviceCommandBuilder getCommandBuilder(DeviceCommand command);
	
	@Override
	public final IAggregatedDeviceCommand[] generateCommandsImpl(DeviceCommandGenerationList commands) throws InvalidCommandException {

		List<IAggregatedDeviceCommand> list = new ArrayList<IAggregatedDeviceCommand>();
		
		for(DeviceCommand command : commands)
		{
			IDeviceCommandBuilder singleDCG = getCommandBuilder(command);
			list.add(new AggregatedDeviceCommandImpl(new DeviceCommand[]{command}, singleDCG.buildCommand()));
		}
		
		return list.toArray(new IAggregatedDeviceCommand[0]);
	}
	
	/*
	@Override
	public T getAssociatedDevice()
	{
		return this.associatedDevice;
	}*/
}
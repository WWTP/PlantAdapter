package plantadapter.dcgs;

import plantadapter.commands.Command;
import plantmodel.Device;

// Questa classe NON implementa IDeviceCommandBuilder di proposito, in modo da poter
// essere riutilizzata in altri casi (in particolare nelle implementazioni di DT80ChannelDefinitionBuilder)
// - le singole sottoclassi possono comunque implementare l'interfaccia.

public abstract class AbstractCommandBuilder<TDev extends Device, TCom extends Command> {

	private final TDev executorDevice;
	private final TCom originalCommand;
	
	public AbstractCommandBuilder(TDev executorDevice, TCom originalCommand) {

		this.executorDevice = executorDevice;
		this.originalCommand = originalCommand;
	}
	
	protected TDev getExecutorDevice() {
		return this.executorDevice;
	}
	
	protected TCom getOriginalCommand() {
		return this.originalCommand;
	}
}
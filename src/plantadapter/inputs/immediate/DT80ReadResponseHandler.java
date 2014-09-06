package plantadapter.inputs.immediate;

import java.util.ArrayList;
import java.util.List;

import plantadapter.IPlant;
import plantadapter.commands.Command;
import plantadapter.commands.dev.IInputMask;
import plantadapter.communication.ISink;
import plantadapter.parsers.IInputParser;
import plantadapter.parsers.InputParserFactory;
import plantadapter.results.ImmediateResult;
import plantmodel.datatypes.ASCIIString;
import plantmodel.dt80.DT80Device;

public class DT80ReadResponseHandler implements ISink<ASCIIString> {

	private DT80Device dt80;
	private IPlant plant;
	
	// TODO
	public DT80ReadResponseHandler(DT80Device dt80, IPlant plant) {
		this.plant = plant;
		this.dt80 = dt80;
	}
	
	// Private Instance Methods
	
	private void addMaskCommands(IInputMask mask, List<Command> cmds) {
		if (mask.isCommand()) {
			cmds.add(mask.asCommand());
		}
		else if (mask.isMask()) {
			for (IInputMask subMask : mask.asMask()) {
				this.addMaskCommands(subMask, cmds);
			}
		}
	}
	
	private Command[] getMaskCommands(IInputMask mask) {
		List<Command> cmds = new ArrayList<Command>();
		this.addMaskCommands(mask, cmds);
		return cmds.toArray(new Command[0]);
	}
	
	// AbstractInputHandler

	@Override
	public void put(ASCIIString message) throws Exception {
		
		ImmediateResult result;
		IInputParser parser;
		IInputMask mask;
		// Recupera la maschera relativa al comando che ha generato la risposta dalla PendingRequestMailbox
		try {
			// TODO
			mask = this.plant.getRequestMailboxFactory().getInstance(dt80).get();
			// TODO
			parser = InputParserFactory.getInputParser(mask.asMask().getSourceDevice());
			
			result = new ImmediateResult(parser.parse(mask, message));
			
			// TODO Gestione relativa a Batch/Timed...
			
			if (result != null && result.getResults().length > 0) { // TODO
				for (Command c : getMaskCommands(mask)) {
					// TODO
					this.plant.getCommandLogger().getInputCallbackReceiver(c).sendInput(result);
					this.plant.getCommandLogger().unlog(c);
				}
			}
		}
		catch (InterruptedException e) {
			/* TODO qui è teoricamente possibile che il thread si ponga in attesa, ma se questo
			 * accade significa che c'è stato un grave errore (si sta leggendo una risposta PER LA QUALE
			 * NON E' PRESENTE ALCUN COMANDO). Forse serve quindi una coda con "take" non bloccante, in modo
			 * da poter rilevare l'assenza del comando con una eccezione.
			 */
		}
	}
}
package plantadapter.parsers;

import plantadapter.commands.dev.IInputMask;
import plantadapter.inputs.MaskFormatException;
import plantadapter.results.SingleResult;
import plantmodel.IDeviceIOData;

public interface IInputParser {

	/**
	 * <p>La <i>maschera</i> passata deve essere relativa solamente all'<code>input</code> corrispondente e
	 * deve contenere come tag "root" l'indicazione di un dispositivo gestibile dall'istanza di <code>IResponseParser</code>
	 * considerata, altrimeni verrà sollevata una <code>InvalidMaskException</code>.</p>
	 * <p>E' compito del chiamante fare si che il dato XML sia consistente con l'<code>input</code> fornito.</p>
	 * @param mask
	 * @param input
	 * @return
	 * @throws MaskFormatException
	 */
	public SingleResult[] parse(IInputMask mask, IDeviceIOData input) throws MaskFormatException;
}
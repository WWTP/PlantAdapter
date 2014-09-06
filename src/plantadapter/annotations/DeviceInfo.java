package plantadapter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import plantadapter.commands.dev.IInputMask;
import plantadapter.communication.ISink;
import plantadapter.dcgs.IDeviceCommandGenerator;
import plantadapter.parsers.IInputParser;
import plantadapter.results.SingleResult;

import plantmodel.IDeviceIOData;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DeviceInfo {

	/**
	 * <p>L'implementazione deve essere dotata di un costruttore pubblico che accetti un solo parametro (del tipo del Device appropriato).</p>
	 * @return
	 */
	public Class<? extends IDeviceCommandGenerator> deviceCommandGeneratorClass();
	/**
	 * <p>L'implementazione deve essere dotata di un costruttore pubblico che accetti come parametri un IPlant e un Device (del tipo del Device).</p>
	 * @return
	 */
	public Class<? extends ISink<byte[]>> deviceInputRecognizerClass() default NullInputRecognizer.class; // TODO Marker (?)
	/**
	 * <p>L'implementazione deve essere dotata di un costruttore pubblico che accetti un solo parametro (del tipo del Device appropriato).</p>
	 * @return
	 */
	public Class<? extends IInputParser> deviceInputParserClass() default NullInputParser.class;
	
	/* Le seguenti classi esistono solo in quanto a nessuno degli sviluppattori
	 * di Java è venuto in mente che un parametro opzionale nelle Annotations potesse
	 * essere una buona idea. Le povere Factory dovranno interpretare i seguenti come
	 * indicazione del fatto che l'attributo non è stato specificato...
	 * 
	 * No, non esiste nemmeno l'ereditarietà fra le Annotations.
	 */
	
	public class NullInputParser implements IInputParser
	{
		@Override
		public SingleResult[] parse(IInputMask mask, IDeviceIOData input) {
			throw new UnsupportedOperationException();
		}
	}

	
	public class NullInputRecognizer implements ISink<byte[]>
	{
		@Override
		public void put(byte[] message) throws Exception {
			throw new UnsupportedOperationException();
		}
	}
}
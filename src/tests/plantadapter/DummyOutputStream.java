package tests.plantadapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DummyOutputStream extends OutputStream {

	private ByteArrayOutputStream os;
	private OutputReadingStrategy outputReadingStrategy;
	
	public DummyOutputStream(OutputReadingStrategy outputReadingStrategy) {
		this.os = new ByteArrayOutputStream();
		this.outputReadingStrategy = outputReadingStrategy;
	}
	
	public String getOutput() {
		// TODO
		return null;
	}

	@Override
	public synchronized void write(int b) throws IOException {
		this.os.write(b);
	}
	
	public interface OutputReadingStrategy {
		/**
		 * <p>Ad ogni carattere della stringa deve corrispondere un byte.</p>
		 * @param data
		 * @return
		 */
		public String readOutput(byte[] data);
	}
}
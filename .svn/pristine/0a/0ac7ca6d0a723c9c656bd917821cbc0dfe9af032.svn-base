package tests.plantadapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyInputStream extends InputStream {

	private byte[] data;
	
	public DummyInputStream() {
		this.data = new byte[0];
	}
	
	public synchronized void addData(byte[] data) {
		byte[] newData = new byte[this.data.length + data.length];
		for (int i = 0; i < newData.length; i++) {
			if (i < this.data.length) newData[i] = this.data[i];
			else newData[i] = data[i];
		}
		this.data = newData;
		this.notify();
	}

	@Override
	public synchronized int read() throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(this.data);
		int b = is.read();
		// Bloccante se non ci sono dati
		while (b < 0) { 
			try { this.wait(); } catch (InterruptedException e) { }
			// Quando si sveglia devono essere stati aggiunti dati...
			is = new ByteArrayInputStream(this.data);
			b = is.read();
		}
		// Rimuove il primo byte dall'array
		this.data = Arrays.copyOfRange(this.data, 1, this.data.length);
		return b;
	}
}
package tests.plantadapter;

import java.io.IOException;
import java.io.OutputStream;

import plantmodel.datatypes.ASCIIString;
import plantmodel.dt80.DT80Utils;

public class InputGenerator {

	private OutputStream os;
	
	/**
	 * 
	 * @param os <code>PipedOutputStream</code> collegato al <code>PipedInputStream</code> sul quale gli <i>input</i> devono essere ricevuti
	 * @param interval
	 */
	public InputGenerator(OutputStream os) {
		this.os = os;
	}
	
	/**
	 * Input basato sul formato del DT80 (fixed format).
	 * @param schedule
	 * @param values
	 * @throws IOException
	 */
	public synchronized void input(DT80Utils.Info.ScheduleIds schedule, Object... values) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		sb.append("D,089401,\"29_03_12\",2012/05/19,14:07:00,0.000366,0;" + schedule.getDT80Syntax() + ",0,");
		for (Object v : values) {
			sb.append(v.toString());
			sb.append(',');
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		// TODO Il codice inserito non può essere qualsiasi, è importante che sia uno di quelli
		// che il recognizer accetta come validi (e.g. 67,63,151). Nota: per ora questi codici
		// non vengono usati per determinare se il record è una schedule o meno, ma potrebbero
		// esserlo in futuro...
		sb.append(";063;4D46");
		sb.append("\r\n");
		
		this.os.write(ASCIIString.fromString((sb.toString())).toByteArray());
	}
}
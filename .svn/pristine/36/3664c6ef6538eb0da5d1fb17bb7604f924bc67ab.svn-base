package tests.inputs.sources;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import plantadapter.communication.impl.InputSourceImpl;
import plantmodel.IDataFormatDescriptor;
import plantmodel.datatypes.ASCIIString;

public class InputSourceImplTest {

	@Test
	public final void testGetData() throws Exception {
		
		String expected = "CIAO";
		
		ByteArrayInputStream is = new ByteArrayInputStream((expected + "\r\n").getBytes());
		InputSourceImpl source = new InputSourceImpl(is, new IDataFormatDescriptor() {

			@Override
			public long getDataLength() {
				return 0;
			}

			@Override
			public byte[][] getDataDelimiters() {
				return new byte[][] { new byte[] { '\r', '\n' } };
			}

			@Override
			public DataFormat getDataFormat() {
				return DataFormat.DELIMITERS;
			}
			
		});
		
		String actual = ASCIIString.fromByteArray(source.get()).toString();
		
		assertEquals(expected.length(), actual.length());
		assertEquals(expected, actual);
	}
}
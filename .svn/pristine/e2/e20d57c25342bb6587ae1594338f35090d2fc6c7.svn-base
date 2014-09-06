package tests.parsers.dt80.readfixedformat;

import static org.junit.Assert.*;

import org.junit.Test;

import plantmodel.datatypes.ASCIIString;
import plantmodel.dt80.DT80Utils.Info.FixedFormatFields;

import tests.parsers.dt80.readfixedformat.uut.UUT;

public class DT80ReadFixedFormatTest {

	@Test
	public final void testData() {
		ASCIIString input = ASCIIString.fromString(
				"D,000043,\"MANGO\",2007/01/23,03:04:20,0.000732,1;A,0,21.555113,2999.5464;0072;0707");
		assertEquals("D", UUT.readFixedFormat(input, FixedFormatFields.RECORD_CLASS, 50));
		assertEquals("000043", UUT.readFixedFormat(input, FixedFormatFields.SERIAL_NUMBER, 50));
		assertEquals("\"MANGO\"", UUT.readFixedFormat(input, FixedFormatFields.JOB_NAME, 50));
		// TimeStamp
		assertEquals("2007/01/23,03:04:20,0.000732", UUT.readFixedFormat(input, FixedFormatFields.TIMESTAMP, 50));
		assertEquals("2007/01/23", UUT.readFixedFormat(input, FixedFormatFields.DATE, 50));
		assertEquals("03:04:20", UUT.readFixedFormat(input, FixedFormatFields.TIME, 50));
		assertEquals("0.000732", UUT.readFixedFormat(input, FixedFormatFields.SUBSECOND_TIME, 50));
		//
		assertEquals("1", UUT.readFixedFormat(input, FixedFormatFields.RECORD_TYPE, 50));
		assertEquals("A", UUT.readFixedFormat(input, FixedFormatFields.SCHEDULE, 50));
		assertEquals("0", UUT.readFixedFormat(input, FixedFormatFields.SCHEDULE_FIRSTDATA_INDEX, 50));
		// Values...
		assertEquals("", UUT.readFixedFormat(input, FixedFormatFields.VALUE, 50));
		assertEquals("21.555113", UUT.readFixedFormat(input, FixedFormatFields.VALUE, 0));
		assertEquals("2999.5464", UUT.readFixedFormat(input, FixedFormatFields.VALUE, 1));
		assertEquals("", UUT.readFixedFormat(input, FixedFormatFields.VALUE, 2));
		//
		assertEquals("0072", UUT.readFixedFormat(input, FixedFormatFields.CODE, 30));
		assertEquals("0707", UUT.readFixedFormat(input, FixedFormatFields.CRC, 60));
		// Eccezioni...
		try {
			// Indice negativo in lettura di valore
			UUT.readFixedFormat(input, FixedFormatFields.VALUE, -1);
			assertTrue(false);
		}
		catch (Exception e) {
			assertTrue(true);
			System.out.println(e.getMessage());
		}
		try {
			// Tentativo lettura numero righe...
			UUT.readFixedFormat(input, FixedFormatFields.UNLOADED_RECORDS, 30);
			assertTrue(false);
		}
		catch (Exception e) {
			assertTrue(true);
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public final void testAlarm() {
		ASCIIString input = ASCIIString.fromString(
				"A,000043,\"MANGO\",2007/01/23,03:04:25,0.264648,1;B,9,1,\"Alarm! 27.0^M^J\";0072;E9E4");
		assertEquals("A", UUT.readFixedFormat(input, FixedFormatFields.RECORD_CLASS, 50));
		assertEquals("000043", UUT.readFixedFormat(input, FixedFormatFields.SERIAL_NUMBER, 50));
		assertEquals("\"MANGO\"", UUT.readFixedFormat(input, FixedFormatFields.JOB_NAME, 50));
		// TimeStamp
		assertEquals("2007/01/23,03:04:25,0.264648", UUT.readFixedFormat(input, FixedFormatFields.TIMESTAMP, 50));
		assertEquals("2007/01/23", UUT.readFixedFormat(input, FixedFormatFields.DATE, 50));
		assertEquals("03:04:25", UUT.readFixedFormat(input, FixedFormatFields.TIME, 50));
		assertEquals("0.264648", UUT.readFixedFormat(input, FixedFormatFields.SUBSECOND_TIME, 50));
		//
		assertEquals("1", UUT.readFixedFormat(input, FixedFormatFields.RECORD_TYPE, 50));
		/* TODO
		 * Verificare il formato tipico degli allarmi (campi diversi dallo standard...)
		 */
		
		// ...
	}
	
	@Test
	public final void testUnloaded() {
		ASCIIString input = ASCIIString.fromString(
				"D,000043,\"MANGO\",2007/01/23,09:10:54,0.974121,3;29,ABCDEF01;0060;8945");
		assertEquals("D", UUT.readFixedFormat(input, FixedFormatFields.RECORD_CLASS, 50));
		assertEquals("000043", UUT.readFixedFormat(input, FixedFormatFields.SERIAL_NUMBER, 50));
		assertEquals("\"MANGO\"", UUT.readFixedFormat(input, FixedFormatFields.JOB_NAME, 50));
		// TimeStamp
		assertEquals("2007/01/23,09:10:54,0.974121", UUT.readFixedFormat(input, FixedFormatFields.TIMESTAMP, 50));
		assertEquals("2007/01/23", UUT.readFixedFormat(input, FixedFormatFields.DATE, 50));
		assertEquals("09:10:54", UUT.readFixedFormat(input, FixedFormatFields.TIME, 50)); // TODO Why (?)
		assertEquals("0.974121", UUT.readFixedFormat(input, FixedFormatFields.SUBSECOND_TIME, 50));
		//
		assertEquals("3", UUT.readFixedFormat(input, FixedFormatFields.RECORD_TYPE, 50));
		//
		assertEquals("29", UUT.readFixedFormat(input, FixedFormatFields.VALUE, 0));
		assertEquals("29", UUT.readFixedFormat(input, FixedFormatFields.UNLOADED_RECORDS, 71));
		//
		assertEquals("0060", UUT.readFixedFormat(input, FixedFormatFields.CODE, 30));
		assertEquals("8945", UUT.readFixedFormat(input, FixedFormatFields.CRC, 60));
		// Eccezioni...
		try {
			// Tentativo lettura da indice > 0
			assertEquals("", UUT.readFixedFormat(input, FixedFormatFields.VALUE, 1));
			assertTrue(false);
		}
		catch (Exception e) {
			assertTrue(true);
			System.out.println(e.getMessage());
		}
		try {
			// Tentativo lettura schedule...
			assertEquals("", UUT.readFixedFormat(input, FixedFormatFields.SCHEDULE, 0));
			assertTrue(false);
		}
		catch (Exception e) {
			assertTrue(true);
			System.out.println(e.getMessage());
		}
	}
}

package plantmodel.datatypes;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import plantmodel.IDeviceIOData;

public class ASCIIString implements IDeviceIOData {

	// Private Static Fields
	
	private static CharsetEncoder asciiEncoder = 
		      Charset.forName("US-ASCII").newEncoder();
	
	// Public Static Fields
	
	public static ASCIIString fromString(String asciiString) {
		return new ASCIIString(asciiString);
	}
	
	public static ASCIIString fromByteArray(byte[] bytes) {
		return new ASCIIString(bytes);
	}
	
	public static ASCIIString concat(ASCIIString... asciiStrings) {
		StringBuilder sb = new StringBuilder();
		for (ASCIIString s : asciiStrings) {
			sb.append(s.toString());
		}
		return ASCIIString.fromString(sb.toString());
	}
	
	// Private Static Methods
	
	private static boolean checkASCII(String s) {
		return asciiEncoder.canEncode(s);
	}
	
	// Private Instance Fields

	private String str;
	
	// Constructors
	
	public ASCIIString() {
		this("");
	}

	public ASCIIString(String asciiString) {
		
		// TODO Non so perché ma a quanto pare questo controllo era cagione di tutti i problemi dell'universo,
		// sia errori di decodifica sia deadlock di thread in giro...
		
		/*
		if (!checkASCII(asciiString))
			throw new IllegalArgumentException("La stringa deve essere in formato ASCII.");
		*/
		
		this.str = asciiString;
	}
	
	public ASCIIString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append((char)b);
		}
		this.str = sb.toString();
	}
	
	// IDeviceIOData
	
	@Override
	public byte[] toByteArray() {
		return this.str.getBytes();
	}
	
	// Object
	
	@Override
	public String toString() {
		return this.str;
	}
}
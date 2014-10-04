package plantadapter.communication.impl;

import java.io.EOFException;
import java.io.InputStream;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import plantadapter.communication.AbstractInputSource;
import plantmodel.IDataFormatDescriptor;
import plantmodel.IDataFormatDescriptor.DataFormat;

public class InputSourceImpl extends AbstractInputSource {

	private InputStream in;
	
	public InputSourceImpl(InputStream in, IDataFormatDescriptor inputFormatDescriptor) {
		super(inputFormatDescriptor);
		this.in = in;
	}
	
	@Override
	public byte[] get() throws Exception {
		
		IDataFormatDescriptor ifd = super.getInputFormatDescriptor();
		
		byte[] buffer = new byte[(int)ifd.getDataLength() > 1 ? (int)ifd.getDataLength() : 255]; // TODO
		
		if (ifd.getDataFormat() == DataFormat.FIXED_LENGTH) {
			this.in.read(buffer);
			return buffer;
		}
		else {
			DelimiterSequenceCheckerSet checker = new DelimiterSequenceCheckerSet(ifd.getDataDelimiters());
			int b;
			int count = 0;
			while ((b = this.in.read()) >= 0) {
				// Espando buffer se necessario
				if (count > buffer.length) buffer = Arrays.copyOf(buffer, 2 * buffer.length);
				buffer[count] = (byte)b;
				count++;
				
				// Verifica delimitatori
				if (checker.check((byte)b)) return Arrays.copyOf(buffer, count - checker.getSequenceDelimitersCount());
				if (ifd.getDataFormat() == DataFormat.MIXED && count == ifd.getDataLength()) 
					return Arrays.copyOf(buffer, count);
			}
			throw new EOFException(); // TODO
		}
	}
	
	private static class DelimiterSequenceChecker {
		
		private String expectedSequence;
		private int ptr;

		public DelimiterSequenceChecker(byte[] sequence) {
			this.expectedSequence = "";
			this.ptr = 0;
			
			for (byte b : sequence) this.expectedSequence += (char)b;
		}
		
		public boolean update(byte b) {
			if (this.check()) this.ptr = 0;
			if (this.expectedSequence.charAt(ptr) == (char)b) ptr++;
			else ptr = 0;
			
			return this.check();
		}
		
		public boolean check() {
			return this.ptr == this.expectedSequence.length();
		}
		
		public int getDelimitersCount() {
			return this.expectedSequence.length();
		}
	}
	
	private static class DelimiterSequenceCheckerSet {
		
		private Set<DelimiterSequenceChecker> checkers;
		
		public DelimiterSequenceCheckerSet(byte[][] delimiters) {
			this.checkers = new HashSet<DelimiterSequenceChecker>();
			
			for (byte[] sequence : delimiters) {
				this.checkers.add(new DelimiterSequenceChecker(sequence));
			}
		}
		
		public boolean check(byte b) {
			for (DelimiterSequenceChecker checker : this.checkers) {
				if (checker.update(b)) return true;
			}
			return false;
		}
		
		public int getSequenceDelimitersCount() {
			for (DelimiterSequenceChecker checker : this.checkers) {
				if (checker.check()) return checker.getDelimitersCount();
			}
			throw new IllegalStateException("clearDelimiters() può essere invocato solo se check() == true");
		}
	}
}
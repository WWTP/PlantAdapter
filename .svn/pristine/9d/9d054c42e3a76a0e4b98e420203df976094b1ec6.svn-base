package plantadapter.commands.dev;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import plantadapter.commands.ReadCommand;

import plantmodel.Device;

/**
 * 
 * @author JCC
 *
 */
public interface IInputMask {
	
	public boolean isMask();
	public Mask asMask();
	public boolean isCommand();
	public ReadCommand asCommand();
	
	public class Mask implements IInputMask, Iterable<IInputMask> {
		
		private Device sourceDev;
		private List<IInputMask> content;
		
		public Mask(Device source) {
			this.sourceDev = source;
			this.content = new LinkedList<IInputMask>();
		}
		
		public Device getSourceDevice() {
			return this.sourceDev;
		}
		
		// TODO La maschera deve poter essere modificata dal ricevente (?) - eventualmente lasciare l'implementazione al "builder"
		public void add (IInputMask child) {
			this.content.add(child);
		}
		
		public int getSubMasksCount() {
			return this.content.size();
		}

		@Override
		public Iterator<IInputMask> iterator() {
			return this.content.iterator();
		}

		@Override
		public boolean isMask() {
			return true;
		}

		@Override
		public Mask asMask() {
			return this;
		}

		@Override
		public boolean isCommand() {
			return false;
		}

		@Override
		public ReadCommand asCommand() {
			throw new UnsupportedOperationException(); // TODO
		}
		
		@Override
		public String toString() {
			return this.sourceDev.toString() + " -> " + this.content.toString();
		}
	}
	
	// TODO Trova un modo per rendere privata la classe seguente...
	
	public class CommandMask implements IInputMask {

		private ReadCommand maskedCommand;
		
		public CommandMask(ReadCommand cmd) {
			this.maskedCommand = cmd;
		}
		
		public ReadCommand getMaskedCommand() {
			return this.maskedCommand;
		}

		@Override
		public boolean isMask() {
			return false;
		}

		@Override
		public Mask asMask() {
			throw new UnsupportedOperationException(); // TODO
		}

		@Override
		public boolean isCommand() {
			return true;
		}

		@Override
		public ReadCommand asCommand() {
			return this.maskedCommand;
		}
		
		@Override
		public String toString() {
			return this.maskedCommand.toString();
		}
	}
}
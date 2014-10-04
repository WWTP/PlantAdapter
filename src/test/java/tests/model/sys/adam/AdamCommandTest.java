package tests.model.sys.adam;

import static org.junit.Assert.*;

import org.junit.Test;

import plantadapter.commands.adam.Adam5000ChannelCommand;
import plantadapter.commands.adam.Adam5024LastValueReadBackCommand;
import plantmodel.adam.Adam5000Device;

public class AdamCommandTest {

	@Test
	public final void test() {
		
		Adam5000Device adam = new Adam5000Device("any", "thing", 1);
		Adam5000ChannelCommand cmd = new Adam5024LastValueReadBackCommand(null, adam, 0, 0);
		
		System.out.println(cmd.getSyntax());
	}
}
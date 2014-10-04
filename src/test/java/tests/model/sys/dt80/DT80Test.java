package tests.model.sys.dt80;

import static org.junit.Assert.*;

import org.junit.Test;

import plantmodel.dt80.DT80ChannelDefinition;
import plantmodel.dt80.DT80ChannelOption;
import plantmodel.dt80.DT80ChannelOptionList;
import plantmodel.dt80.DT80Utils.Info.ChannelTypes;

public class DT80Test {

	@Test
	public final void testChannelDefinition() {
		// DT80ChannelDefinition
		DT80ChannelDefinition cd = new DT80ChannelDefinition(1, ChannelTypes.VOLTAGE);
		assertTrue(cd.getDT80Syntax().toString().equalsIgnoreCase("1V"));
		cd = new DT80ChannelDefinition(1, ChannelTypes.RESISTANCE);
		assertTrue(cd.getDT80Syntax().toString().equalsIgnoreCase("1R"));
		// DT80ChannelOptionList
		DT80ChannelOptionList col = new DT80ChannelOptionList(new DT80ChannelOption("S1"));
		cd = new DT80ChannelDefinition(2, ChannelTypes.VOLTAGE, col);
		assertTrue(cd.getDT80Syntax().toString().equalsIgnoreCase("2V(S1)"));
		// DT80ChannelOptionList
		col = new DT80ChannelOptionList(new DT80ChannelOption("S1"), new DT80ChannelOption("\"Thierrico!\""));
		cd = new DT80ChannelDefinition(2, ChannelTypes.VOLTAGE, col);
		assertTrue(cd.getDT80Syntax().toString().equalsIgnoreCase("2V(S1,\"Thierrico!\")"));
		// Exception
		try {
			cd = new DT80ChannelDefinition(10, ChannelTypes.VOLTAGE);
			fail();
		}
		catch (IllegalArgumentException e) { }
		
	}
}
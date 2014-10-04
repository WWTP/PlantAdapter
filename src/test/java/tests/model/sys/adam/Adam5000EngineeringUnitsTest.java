package tests.model.sys.adam;

import static org.junit.Assert.*;

import javax.measure.quantity.ElectricCurrent;

import org.jscience.physics.amount.Amount;
import org.junit.Test;

import plantmodel.adam.Adam5000EngineeringUnits;
import quantities.AnalogueAmount;
import quantities.IAmount;

public class Adam5000EngineeringUnitsTest {

	@Test
	public void testGetFormatClassOfQextendsQuantity() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFormatInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStringDoubleClassOfQextendsQuantityInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStringDoubleClassOfQextendsQuantity() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStringIAmount() {
		IAmount amount = new AnalogueAmount(ElectricCurrent.class, (Amount<ElectricCurrent>)Amount.valueOf("10 mA"));
		assertEquals("10.000", Adam5000EngineeringUnits.toString(amount));
		
		amount = new AnalogueAmount(ElectricCurrent.class, (Amount<ElectricCurrent>)Amount.valueOf("10.0000 mA"));
		assertEquals("10.000", Adam5000EngineeringUnits.toString(amount));
		
		amount = new AnalogueAmount(javax.measure.quantity.ElectricCurrent.class, (Amount<ElectricCurrent>)Amount.valueOf("9.1234 mA"));
		assertEquals("09.123", Adam5000EngineeringUnits.toString(amount));
		
		amount = new AnalogueAmount(javax.measure.quantity.ElectricCurrent.class, (Amount<ElectricCurrent>)Amount.valueOf("40 mA"));
		try {
			Adam5000EngineeringUnits.toString(amount);
			fail();
		}
		catch(IllegalArgumentException e){
			
		}
		
		amount = new AnalogueAmount(ElectricCurrent.class, (Amount<ElectricCurrent>)Amount.valueOf("4 A"));
		
		try {
			Adam5000EngineeringUnits.toString(amount);
			fail(); // TODO non prende in considerazione l'unità di misura e prosegue; valutare se riconvertire in Amount le Engineering Units.
		}
		catch (IllegalArgumentException e) {
			
		}
	}

}

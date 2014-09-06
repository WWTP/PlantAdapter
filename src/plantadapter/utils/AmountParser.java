package plantadapter.utils;

import java.text.ParseException;

import javax.measure.quantity.Quantity;

import org.jscience.physics.amount.Amount;

import plantmodel.datatypes.ASCIIString;
import quantities.AnalogueAmount;
import quantities.DigitalAmount;
import quantities.DigitalQuantity;
import quantities.IAmount;
import quantities.InformationAmount;

public class AmountParser {
	public static IAmount parseAmount(String amountString) throws ParseException {
		if(amountString == null)
			throw new IllegalArgumentException("amountString == null");
		
		if(amountString == "") {
			throw new ParseException("ERRORE: valore non inserito", 0);
		}
		
		// Eliminazione spazi alla fine e all'inizio della stringa
		amountString = amountString.trim();
		
		// DataAmount: [value]
		if(amountString.length() > 1 && amountString.charAt(0) == '[' && amountString.charAt(amountString.length() - 1) == ']' )
		{
			return new InformationAmount(ASCIIString.fromString(amountString.substring(1, amountString.length() - 1)).toByteArray());
		}
		// DigitalAmount: ValuexL where Value := [0-9]+ & L := [0-9]+ (L = number of levels)
		if(amountString.contains("x"))
		{
			String[] split = amountString.split("x");
			
			// Se una sola x trovata
			if(split.length == 2)
			{
				if(split[0].length() > 0 && split[1].length() > 0) {
					try {
					int v = Integer.parseInt(split[0]);
					int l = Integer.parseInt(split[1]);
					
					if(v < l)
						return new DigitalAmount(v, DigitalQuantity.getDigitalQuantity(l));
					}
					catch (NumberFormatException e)
					{ /* NON è un digitalAmount (sottostringhe per valore e livelli non sono numeri) */ }
				}
			}
		}
		// AnalogueAmount: x(u:q) where x := [0-9]+ & u := [a-zA-z] (unità di misura) & q := [a-zA-z] (grandezza - quantity)
		if(amountString.length() > 5)
		{
			return parseAnalogueAmount(amountString);
		}
		
		throw new ParseException("ERRORE: input non riconosciuto", amountString.length());
	}
	
	public static AnalogueAmount parseAnalogueAmount(String amountString) throws ParseException
	{
		// TODO custom quantities are supported? (e.g.: RPM,...)
		try {
			String[] strs = amountString.split("\\(|:|\\)");
			
			//Amount<? extends Quantity> amount = Amount.valueOf(amountString);
			Amount<? extends Quantity> amount = Amount.valueOf(strs[0] + " " + strs[1]);
			if(amount == null)
				throw new Exception();
			
			//return new AnalogueAmount(QuantityParser.newInstance().parseQuantity(amountString), amount);
			return new AnalogueAmount(QuantityParser.newInstance().parseQuantity(strs[2]), amount);
		}
		catch (IllegalArgumentException e) {
			throw new ParseException("ERRORE: analogue quantity not identified", amountString.length() - 1);
		}
		catch (Exception e)
		{
			throw new ParseException("ERRORE: analogue amount not identified", amountString.length() - 1);
		}
	}
}

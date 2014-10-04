package plantadapter.utils;

import org.jscience.physics.amount.Amount;

import plantmodel.Endpoint;
import plantmodel.AnalogueEndpointInterface;
import quantities.AnalogueAmount;
import quantities.IAmount;

public class DeviceAmountConverter {
	/**
	 * Converte l'Amount passato in un Amount seguendo il PreferredAmount attualmente impostato per l'Endpoint passato
	 * @param deviceEndpoint
	 * @param amount
	 * @return
	 * @throws IllegalStateException se non è definita una preferred EndpointInterface per l'endpoint specificato
	 * @throws UnsupportedOperationException se non sono definite EndpointInterface che supportino l'Amount passato
	 */
	public static IAmount toDeviceAmount(Endpoint deviceEndpoint, IAmount amount){
		return convert(deviceEndpoint, amount, new IConvertionStrategy.Scale());
	}
	
	// TODO Aggiungere Overloading con IAmount originalAmount per poter fare lo scaling correttamente e non applicarlo più volte).
	
	// TODO da rivedere
	public static IAmount fromDeviceAmount(Endpoint deviceEndpoint, IAmount amount){
		return convert(deviceEndpoint, amount, new IConvertionStrategy.Invert());
	}
	
	private static IAmount convert(Endpoint deviceEndpoint, IAmount amount, IConvertionStrategy convertionStrategy){
		if(amount instanceof AnalogueAmount)
		{
			if(!deviceEndpoint.hasPreferredInterface())
				throw new IllegalStateException("Preferred Fisical Interface is not defined for endpoint " + deviceEndpoint);
			
			double value = ((AnalogueAmount)amount).getAmount().getEstimatedValue();
			
			if(!amount.getQuantity().equals(deviceEndpoint.getPreferredInterface().getQuantity())) {
				AnalogueEndpointInterface[] ei = (AnalogueEndpointInterface[]) deviceEndpoint.getLogicalInterfacesForQuantity(amount.getQuantity()); // CAST possibile in quanto la quantità passata è analogica (amount instanceof AnalogueAmount)
				
				if(ei.length == 0)
					throw new UnsupportedOperationException("There are no Logical Interfaces defined for " + amount.getQuantity());
				value = convertionStrategy.scale(ei[0], value);
				
				// Conversione dall'unità
				value = convertionStrategy.scale((AnalogueEndpointInterface)deviceEndpoint.getPreferredInterface(), value); // CAST possibile in quanto Quantity pari a quella dell'IAmount (analogico)
			}
			// else: l'Amount passato è della Quantità gestita nativamente dal Device
			
			// TODO Conversione dall'unità - non applicabile qui in quanto se valore di base è già scalato correttamente verrà ulteriormente scalato.
			//value = convertionStrategy.scale(deviceEndpoint.getPreferredInterface(), value);
			
			return new AnalogueAmount(deviceEndpoint.getPreferredInterface().getQuantity(), Amount.valueOf(value, deviceEndpoint.getPreferredInterface().getUnit()));
		}
		else
			// Nessuna conversione possibile / da effettuare
			return amount;
	}
	
	private interface IConvertionStrategy {
		public double scale(AnalogueEndpointInterface ei, double value);
		

		class Scale implements IConvertionStrategy {

			@Override
			public double scale(AnalogueEndpointInterface ei, double value) {
				return ei.getScalingTransformation().scale(value);
			}
			
		}
		
		class Invert implements IConvertionStrategy {

			@Override
			public double scale(AnalogueEndpointInterface ei, double value) {
				return ei.getScalingTransformation().invert().scale(value);
			}
			
		}
	}
}

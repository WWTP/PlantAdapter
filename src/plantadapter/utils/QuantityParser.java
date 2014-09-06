package plantadapter.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.Quantity;
import javax.xml.xpath.XPathExpressionException;

import org.jscience.physics.amount.Amount;
import org.w3c.dom.Element;

import plantadapter.Configuration;
import plantadapter.annotations.DeviceInfo;
import plantmodel.adam.Adam5000Device;
import plantmodel.datatypes.ASCIIString;
import plantmodel.dt80.DT80Device;
import plantmodel.misc.ActuableDevice;
import plantmodel.misc.ProbeDevice;
import quantities.AnalogueAmount;
import quantities.DO;
import quantities.Flow;
import quantities.IAmount;
import quantities.IDigitalQuantity;
import quantities.InformationAmount;
import quantities.NH4;
import quantities.NO3;
import quantities.ORP;
import quantities.PH;
import quantities.TSS;

import utils.refl.ReflectionHelper;
import utils.refl.filters.SubInterfaceTypeFilter;
import utils.refl.filters.InterfaceTypeFilter;

import xmld.XMLData;

public class QuantityParser {

	public static QuantityParser newInstance() {
		return new QuantityParser();
	}
	
	private QuantityParser() { }
	
	/* Lista d'appoggio in caso la reflection fallisca. */
	
	private static Iterable<Class<? extends Quantity>> quantities;
	
    static {
		
    	quantities = new ArrayList<Class<? extends Quantity>>();
			
		((List)quantities).add(NH4.class);
		((List)quantities).add(NO3.class);
		((List)quantities).add(ORP.class);
		((List)quantities).add(PH.class);
		((List)quantities).add(TSS.class);
		((List)quantities).add(Flow.class);
		((List)quantities).add(DO.class);
		((List)quantities).add(IDigitalQuantity.class);
		((List)quantities).add(ElectricCurrent.class);
		((List)quantities).add(ElectricPotential.class);
	}
	
	/* */
	
	
    // Public Instance Methods
	
	// Quantity
	
    /* Seguente metodo attualmente non utilizzato dunque non implementato uso della mappa statica... */
    
    /*
	public String[] getQuantitiesNames()
	{
		List<String> names = new ArrayList<String>();
		
		ReflectionHelper reflector = new ReflectionHelper(Configuration.QUANTITIES_PACKAGES);
		
		SubInterfaceTypeFilter<Quantity> quantityFilter = new SubInterfaceTypeFilter<Quantity>(new InterfaceTypeFilter(), Quantity.class);
		
		for (Class<? extends Quantity> c : reflector.getTypes(quantityFilter)) {
			names.add(c.getSimpleName());
		}
		
		return names.toArray(new String[0]);
	}
	*/
	
	public Class<? extends Quantity> parseQuantity(String s) {
		
		ReflectionHelper reflector = new ReflectionHelper(Configuration.QUANTITIES_PACKAGES);
		
		SubInterfaceTypeFilter<Quantity> quantityFilter = new SubInterfaceTypeFilter<Quantity>(new InterfaceTypeFilter(), Quantity.class);
		
		for (Class<? extends Quantity> c : reflector.getTypes(quantityFilter)) {
			if (c.getSimpleName().equalsIgnoreCase(s))
				return (Class<? extends Quantity>)c;
		}
		
		// Se la reflection fallisce...
		for (Class<? extends Quantity> c : quantities) {
			if (c.getSimpleName().equalsIgnoreCase(s))
				return (Class<? extends Quantity>)c;
		}
		

		//System.err.println("QuantityParser: " + s);

		return null;
	}
	
	public Class<? extends Quantity> parseQuantity(Element e) {
		// TODO
		return null;
	}
	
	public String toString(Class<? extends Quantity> q) {
		return q.getSimpleName();
	}
	
	public Element toElement(Class<? extends Quantity> q) {
		// TODO
		return null;
	}
	
	// Amount
	
	public IAmount parseAmount(String s) {
		// TODO
		return null;
	}
	
	public IAmount parseAmount(Element e) {
		
		XMLData amount = XMLData.fromNode(e);
		
		// TODO Gestione Amount non analogici...
		
		try 
		{
			// TODO Uso reflection per andare a prendere tutte le implementazioni di Quantity e fare il test su quelle...
			if (amount.dom("amount/quantity").equalsIgnoreCase("ElectricCurrent")) {
				String amountStr = amount.dom("amount/value") + " " +  amount.dom("amount/unit");
				
				@SuppressWarnings("unchecked")
				IAmount amnt = new AnalogueAmount(ElectricCurrent.class, (Amount<ElectricCurrent>)Amount.valueOf(amountStr));
				
				return amnt;
			}
			// TODO Altri casi...
			else if (amount.dom("amount/quantity").equalsIgnoreCase("DataAmount")) {
				return new InformationAmount(ASCIIString.fromString(amount.dom("amount/value")).toByteArray()); // TODO Gestione formati diversi da ASCII...
			}
		} 
		catch (XPathExpressionException e1) { throw new RuntimeException(); /* TODO */ }
		
		return null;
	}
	
	public String toString(IAmount a) {
		// TODO
		return null;
	}
	
	public Element toElement(IAmount a) {
		
		XMLData xml = null;
		
		// TODO Gestion Amount non analogici...

		try 
		{
			if (a instanceof AnalogueAmount) {
				xml = new XMLData("amount");
					
				AnalogueAmount amount = (AnalogueAmount)a;
				
				xml.dom("/amount/quantity=" + amount.getQuantity().getSimpleName());
				xml.dom("/amount/value=" + amount.getAmount().getEstimatedValue());
				xml.dom("/amount/unit=" + amount.getAmount().getUnit()); // TODO Verifica problema �C (?)
			}
			else if (a instanceof InformationAmount) { // TODO Per ora usato ASCII, vedere come fare per consentire altre codifiche...
				xml = new XMLData("amount");
				
				InformationAmount amount = (InformationAmount)a;
				
				xml.dom("/amount/quantity=" + amount.getQuantity().getSimpleName());
				xml.dom("/amount/value=" + ASCIIString.fromByteArray(amount.getBytes()));
				xml.dom("/amount/unit=" + "ASCII"); // TODO Qui 'unit' assume significato di 'rappresentazione'...
			}
		}
		catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (xml == null)
			throw new IllegalStateException("Amount non valido."); // TODO
		
		try 
		{
			return (Element)xml.toNode("amount");
		} 
		catch (XPathExpressionException e) {
			throw new RuntimeException();
		}
	}
}
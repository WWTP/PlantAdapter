package plantmodel.dt80;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.measure.quantity.Quantity;
import javax.measure.quantity.DataAmount;
import javax.measure.quantity.ElectricPotential;
import javax.measure.unit.SI;

import plantadapter.annotations.DeviceInfo;
import plantadapter.dcgs.impl.DT80DeviceCommandGenerator;
import plantadapter.inputs.recogs.DT80InputRecognizer;
import plantadapter.parsers.impl.DT80InputParser;
import plantadapter.schedules.DeviceScheduledOperation;
import plantadapter.schedules.ScheduleSupplierDevice;
import plantadapter.utils.LinearScalingTransformation;
import plantmodel.DataEndpointInterface;
import plantmodel.Device;
import plantmodel.DigitalEndpointInterface;
import plantmodel.Endpoint;
import plantmodel.AnalogueEndpointInterface;
import plantmodel.datatypes.ASCIIString;
import plantmodel.dt80.DT80Utils.Info.ChannelModifiers;
import plantmodel.dt80.DT80Utils.Info.ChannelTypes;
import plantmodel.dt80.DT80Utils.Info.ScheduleIds;

/* TODO Ci vorrebbero degli invarianti di classe per le connessioni possibili dei canali analogici:
 * +-/*# o *#/+#/-#. Come trattare le diverse modalità di utilizzo di questi Endpoint (Endpoint separati, modalità, ...)?
 */

@DeviceInfo
(
	deviceCommandGeneratorClass = DT80DeviceCommandGenerator.class,
	deviceInputRecognizerClass = DT80InputRecognizer.class,
	deviceInputParserClass = DT80InputParser.class
)
public class DT80Device extends Device implements ScheduleSupplierDevice {

	// Mapping temporaneo della configurazione, usato in quanto non è possibile configurarlo correttamente nel costruttore altrimenti.
	//private static DT80AnalogueEndpointMapper tmp = new DT80AnalogueEndpointMapper();
	
	// Private Static Methods
	
	// AnalogueEndpoints
	
	private static AnalogueEndpointInterface[] getAnalogueEndpointInterfaces() {
		return new AnalogueEndpointInterface[] {
			new AnalogueEndpointInterface(ElectricPotential.class, SI.MILLI(SI.VOLT), null, null, LinearScalingTransformation.IDENTITY)	// TODO
		};
	}
	
	private static void addAnalogueEndpoint(List<Endpoint> endpoints, int n) {
		Endpoint e = null;
		for(int i = 0; i < 4; i++){
			switch(i) {
				case 0: e = new DT80AnalogueEndpoint(n + "(*#)", getAnalogueEndpointInterfaces(), n, ChannelModifiers.ASTERISK); break; //tmp.add(e, n, ChannelModifiers.ASTERISK); break; // es. "1(*#)"
				case 1: e = new DT80AnalogueEndpoint(n + "(+#)", getAnalogueEndpointInterfaces(), n, ChannelModifiers.PLUS); break;     //tmp.add(e, n, ChannelModifiers.PLUS); break; // es. "1(+#)"
				case 2: e = new DT80AnalogueEndpoint(n + "(-#)", getAnalogueEndpointInterfaces(), n, ChannelModifiers.MINUS); break;    //tmp.add(e, n, ChannelModifiers.MINUS); break; // es. "1(-#)"
				case 3: e = new DT80AnalogueEndpoint(n + "(+-)", getAnalogueEndpointInterfaces(), n, ChannelModifiers.NONE); break;     //tmp.add(e, n, ChannelModifiers.NONE); break; // es. "1(+-)"
				// TODO SHARP
			}
			endpoints.add(e);
		}
	}
	
	private static void addAnalogueEndpoints(List<Endpoint> endpoints) {
		for (int i = 1; i <= 5; i++) addAnalogueEndpoint(endpoints, i);
	}
	
	// DigitalEndpoints
	
	private static DigitalEndpointInterface[] getDigitalEndpointInterfaces() {
		return new DigitalEndpointInterface[] {
				new DigitalEndpointInterface(DataAmount.class, DataAmount.UNIT)
			};
	}
	
	private static void addDigitalEndpoints(List<Endpoint> endpoints) {
		for (int i = 1; i <= 8; i++) endpoints.add(new DT80DigitalEndpoint(i + "D", getDigitalEndpointInterfaces(), i));
	}
	
	private static DataEndpointInterface[] getSerialEndpointInterfaces() {
		return new DataEndpointInterface[] {
				new DataEndpointInterface(DataAmount.class, DataAmount.UNIT)
		};
	}
	
	// SerialEndpoints
	
	private static void addSerialEndpoints(List<Endpoint> endpoints) {
		endpoints.add(new DT80SerialEndpoint("SERSEN_PORT", getSerialEndpointInterfaces(), 1));
		endpoints.add(new DT80SerialEndpoint("HOST_RS232_PORT", getSerialEndpointInterfaces(), 2));
		endpoints.add(new DT80SerialEndpoint("USB_PORT", getSerialEndpointInterfaces(), 3));
	}
	
	// Endpoints
	
	private static DataEndpointInterface[] getEthernetEndpointInterfaces() {
		return new DataEndpointInterface[] {
				new DataEndpointInterface(DataAmount.class, DataAmount.UNIT)
		};
	}
	
	private static Endpoint[] setupEndpoints() { // TODO Caricamento RunTime Configurazione
		List<Endpoint> endpoints = new LinkedList<Endpoint>();
		addAnalogueEndpoints(endpoints);
		addDigitalEndpoints(endpoints);
		addSerialEndpoints(endpoints);
		
		// Ethernet Port
		endpoints.add(new Endpoint("ETHERNET_PORT", getEthernetEndpointInterfaces()));
		return endpoints.toArray(new Endpoint[0]);
	}
	
	// Instance Fields
	
	private Set<DT80Schedule> activeSchedules;
	private Set<DT80Span> definedSpans;
	//private DT80AnalogueEndpointMapper analogueEndpointMapper;
	private DT80DefinedSpanMapper spanMapper;
	
	// Public Constructors
	
	public DT80Device(String id, String serialNumber) {
		// TODO da rivedere definizione endpoint e associazione in mapping logico endpoint-portaDT80
		super(id, "Data Logger", "DataTaker", "DT80", "2", serialNumber, setupEndpoints(), ASCIIString.class);
		//this.analogueEndpointMapper = tmp;
		this.spanMapper = new DT80DefinedSpanMapper();
		this.activeSchedules = new HashSet<DT80Schedule>();
		this.definedSpans = new HashSet<DT80Span>();
	}
	
	// Protected Instance Methods

	// Public Instance Methods
	/**
	 * Aggiunge uno Span HW del Sistema
	 * @param span
	 */
	public void addHwSpan(DT80Span span){
		this.definedSpans.add(span);
	}
	
	/**
	 * Aggiunge uno Span HW del sistema associandolo all'Endpoint per cui è attualmente in uso.
	 * @param endpoint
	 * @param endpointInterface
	 * @param span
	 */
	public void addHwSpan(DT80Span span, Endpoint endpoint, AnalogueEndpointInterface endpointInterface){
		this.definedSpans.add(span);
		// TODO Precondizioni su presenza EndpointInterface nel DT80 (?)
		this.spanMapper.add(endpoint, endpointInterface, span);
	}
	
	/**
	 * Aggiunge uno Span HW associato ad una ChannelDefinition permanente (es. Schedule) - correlata quindi ad un Endpoint
	 * @param endpoint
	 * @param endpointInterface
	 * @param span
	 * @param channelDefinition
	 */
	public void addHwSpan(DT80Span span, Endpoint endpoint, AnalogueEndpointInterface endpointInterface, DT80ChannelDefinition channelDefinition){
		this.definedSpans.add(span);
		// TODO Precondizioni su presenza EndpointInterface nel DT80 (?)
		this.spanMapper.add(endpoint, endpointInterface, span);
		span.addChannelDefinitionInUse(channelDefinition);
	}
	
	public ChannelTypes getChannelTypeForEndpoint(Endpoint endpoint) {
		// TODO
		return null;
	}
	
	public DT80Span[] getDefinedSpansForChannelType(ChannelTypes channelType) {
		// TODO
		return null;
	}
	
	public DT80Span[] getDefinedSpansForEndpoint(Endpoint endpoint) {
		return this.spanMapper.getSpans(endpoint);
	}
	
	/**
	 * Ritorna gli Span impostati su un Endpoint del ChannelType definito.
	 * @param endpoint
	 * @param channelType
	 * @return
	 */
	public DT80Span[] getDefinedSpans(Endpoint endpoint, ChannelTypes channelType) {
		// TODO
		return null;
	}
	
	// TODO Possibilità di integrare la gestione all'interno di DT80Device e mascherare l'uso di questo oggetto eliminandone la dipendenza
	// TODO integrare con:
	// TODO - int getChannelNumber(Endpoint e)
	// TODO - ChannelModifiers getChannelModifier(Endpoint e)
	/*
	public DT80AnalogueEndpointMapper getAnalogueEndpointMapper(){
		return this.analogueEndpointMapper;
	}
	*/
	
	// Schedules
	
	public void addSchedule(DT80Schedule schedule) {
		this.activeSchedules.add(schedule);
	}
	
	public void removeSchedule(DT80Schedule schedule) {
		this.activeSchedules.remove(schedule);
	}
	
	/**
	 * 
	 * @return <code>null</code> se non vi sono ScheduleIds <code>ScheduleIds</code> disponibili
	 */
	public ScheduleIds getAvailableScheduleId() {
		for (ScheduleIds id : ScheduleIds.values()) {
			boolean free = true;
			for (DT80Schedule schedule : this.activeSchedules) {
				if (schedule.getDT80ScheduleDefinition().getScheduleId() == id) {
					free = false;
					break;
				}
			}
			if (free) return id;
		}
		
		return null;
	}
	
	@Override
	public DeviceScheduledOperation[] getDefinedSchedules() {
		return this.activeSchedules.toArray(new DeviceScheduledOperation[0]);
	}
	
	/**
	 * Configura gli endpoint del DT80 con i relativi Channel (canali digitali) + modificatori in uso dal DT80.
	 * @param e
	 * @param channelNumber
	 * @param modifier
	 */
	/*
	public void setAnalagueEndpointMapping(Endpoint e, int channelNumber, ChannelModifiers modifier){
		this.analogueEndpointMapper.add(e, channelNumber, modifier);
	}*/

	public DT80Span getSpan(Endpoint targetPort, Class<? extends Quantity> commandQuantity) {
		return this.spanMapper.getSpan(targetPort, commandQuantity);
	}
	
	@Override
	public String toString(){
		return "DT80 " + this.getID();
	}
}
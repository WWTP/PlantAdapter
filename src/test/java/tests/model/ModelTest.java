package tests.model;

import static org.junit.Assert.*;
import static javax.measure.unit.SI.*;

import java.util.Date;

import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.Quantity;
import javax.measure.quantity.Temperature;
import javax.measure.unit.Unit;
import org.jscience.physics.amount.Amount;
import org.junit.BeforeClass;
import org.junit.Test;
import plantadapter.commands.DeviceCommand;
import plantadapter.commands.ReadCommand;
import plantadapter.commands.dev.IInputMask;

import plantadapter.schedules.ScheduledOperation;
import plantadapter.schedules.SystemScheduleDefinition;

import plantadapter.utils.LinearScalingTransformation;

import plantmodel.Connection;
import plantmodel.Device;
import plantmodel.Endpoint;
import plantmodel.AnalogueEndpointInterface;
import plantmodel.IPConnection;

import plantmodel.adam.Adam5000Device;

import plantmodel.dt80.DT80Device;
import plantmodel.dt80.DT80Schedule;
import plantmodel.dt80.DT80ScheduleDefinition;
import plantmodel.dt80.DT80Span;
import plantmodel.dt80.DT80Utils.Info.ScheduleFrequencyUnits;

import plantmodel.misc.ActuableDevice;
import plantmodel.misc.ProbeDevice;

import quantities.DO;
import quantities.Flow;
import quantities.NH4;
import quantities.NO3;
import quantities.ORP;
import quantities.PH;
import quantities.TSS;

public class ModelTest {
	
	private static Connection createConnection(
			String masterDeviceID, String masterEndpointID, 
			String slaveDeviceID, String slaveEndpointID) {
		return new Connection(
				Endpoint.fromID(masterDeviceID, masterEndpointID),
				Endpoint.fromID(slaveDeviceID, slaveEndpointID)
		);
	}
	
	// TODO
	private static ActuableDevice createActuable(String id) {
		return new ActuableDevice(id, id, id, id, id, null, null);
	}
	
	private static ProbeDevice createProbe(String id, AnalogueEndpointInterface... endpointInterfaces) {
		return new ProbeDevice(id, "", "", "", "", endpointInterfaces[0], endpointInterfaces[1]);
	}
	
	// TODO Manca indicazione Range...
	private static AnalogueEndpointInterface createEpInterface(Class<? extends Quantity> quantity,
			Unit<? extends Quantity> unit, double factor, double offset, 
			double minAmount, Unit<? extends Quantity> minAmountUnit, double maxAmount, Unit<? extends Quantity> maxAmountUnit) {
		return new AnalogueEndpointInterface(quantity, unit, Amount.valueOf(minAmount, minAmountUnit), Amount.valueOf(maxAmount, maxAmountUnit), 
				new LinearScalingTransformation(factor, offset));
	}
	
	public static void setupDT80EndpointInterfaces() {
		DT80Device dt80 = (DT80Device)Device.fromID("dt80");
		int spanIndex = 1;
		for (Connection cnn : dt80.getConnectionsAsMaster()) {
			if(cnn.getSlaveDevice() instanceof ProbeDevice){
				Endpoint dt80Ep = cnn.getMasterEndpoint();
				Endpoint probeEp = cnn.getSlaveEndpoint();
				AnalogueEndpointInterface probeEi = (AnalogueEndpointInterface) probeEp.getLogicalInterfaces()[0];
				AnalogueEndpointInterface dt80Ei = (AnalogueEndpointInterface) probeEi; // TODO Copia semplicemente l'EI (tanto quelle delle sonde non vengono mai usate)
				dt80Ep.addLogicalInterface(dt80Ei);
				// Ora usa l'interfaccia fisica delle sonde...
				probeEi = (AnalogueEndpointInterface) probeEp.getPreferredInterface();
				dt80.addHwSpan(new DT80Span(spanIndex, null, 
						probeEi.getMinimum().getEstimatedValue(),
						dt80Ei.getMinimum().getEstimatedValue(),
						probeEi.getMaximum().getEstimatedValue(),
						dt80Ei.getMaximum().getEstimatedValue()), 
						dt80Ep, dt80Ei);
				spanIndex++;
			}
		}
	}
	
	// SETUP
	
	private static boolean initialized = false;
	
	@BeforeClass
	public static final void setup() {

		if (initialized) return;
		
		AnalogueEndpointInterface physicalEp, logicalEp;
		
		// DT80
		DT80Device dt80 = new DT80Device("dt80", "");
		
		Endpoint dt80Ep = dt80.getEndpointById("1(+#)");
		dt80.setEndpointPreferredInterface(dt80Ep, dt80.getEndpointNativeInterfaces(dt80Ep, ElectricPotential.class)[0]);
		dt80Ep = dt80.getEndpointById("1(-#)");
		dt80.setEndpointPreferredInterface(dt80Ep, dt80.getEndpointNativeInterfaces(dt80Ep, ElectricPotential.class)[0]);
		dt80Ep = dt80.getEndpointById("1(*#)");
		dt80.setEndpointPreferredInterface(dt80Ep, dt80.getEndpointNativeInterfaces(dt80Ep, ElectricPotential.class)[0]);
		
		dt80Ep = dt80.getEndpointById("2(+#)");
		dt80.setEndpointPreferredInterface(dt80Ep, dt80.getEndpointNativeInterfaces(dt80Ep, ElectricPotential.class)[0]);
		dt80Ep = dt80.getEndpointById("2(-#)");
		dt80.setEndpointPreferredInterface(dt80Ep, dt80.getEndpointNativeInterfaces(dt80Ep, ElectricPotential.class)[0]);
		dt80Ep = dt80.getEndpointById("2(*#)");
		dt80.setEndpointPreferredInterface(dt80Ep, dt80.getEndpointNativeInterfaces(dt80Ep, ElectricPotential.class)[0]);
		
		dt80Ep = dt80.getEndpointById("3(*#)");
		dt80.setEndpointPreferredInterface(dt80Ep, dt80.getEndpointNativeInterfaces(dt80Ep, ElectricPotential.class)[0]);
		dt80Ep = dt80.getEndpointById("3(+-)");
		dt80.setEndpointPreferredInterface(dt80Ep, dt80.getEndpointNativeInterfaces(dt80Ep, ElectricPotential.class)[0]);
		
		dt80Ep = dt80.getEndpointById("4(*#)");
		dt80.setEndpointPreferredInterface(dt80Ep, dt80.getEndpointNativeInterfaces(dt80Ep, ElectricPotential.class)[0]);
		dt80Ep = dt80.getEndpointById("4(+-)");
		dt80.setEndpointPreferredInterface(dt80Ep, dt80.getEndpointNativeInterfaces(dt80Ep, ElectricPotential.class)[0]);
		
		dt80Ep = dt80.getEndpointById("5(*#)");
		dt80.setEndpointPreferredInterface(dt80Ep, dt80.getEndpointNativeInterfaces(dt80Ep, ElectricPotential.class)[0]);
		dt80Ep = dt80.getEndpointById("5(+-)");
		dt80.setEndpointPreferredInterface(dt80Ep, dt80.getEndpointNativeInterfaces(dt80Ep, ElectricPotential.class)[0]);

		// ADAM
		new Adam5000Device("adam", "", 1);
		
		// Actuable (PUMP)
		// TODO Aggiungere in modo coerente con i test
		// createActuable("internal_recycle_pump");

		// Sonde (ANOX)
		physicalEp = createEpInterface(ElectricPotential.class, MILLI(VOLT), 1.0, 0.0, 401.326, Unit.valueOf("mV"), 2022.401, Unit.valueOf("mV"));
		logicalEp = createEpInterface(ORP.class, ORP.UNIT, 0.617, -747.568, -500, Unit.valueOf("mV"), 500, Unit.valueOf("mV"));
		createProbe("orp_anox", physicalEp, logicalEp);
		physicalEp = createEpInterface(ElectricPotential.class, MILLI(VOLT), 1.0, 0.0, 401.6, Unit.valueOf("mV"), 2001.6, Unit.valueOf("mV"));
		logicalEp = createEpInterface(PH.class, PH.UNIT, 0.006, -0.510, 2, PH.UNIT, 12, PH.UNIT);
		createProbe("ph_anox", physicalEp, logicalEp);
		physicalEp = createEpInterface(ElectricPotential.class, MILLI(VOLT), 1.0, 0.0, 400, Unit.valueOf("mV"), 2000, Unit.valueOf("mV"));
		logicalEp = createEpInterface(NH4.class, NH4.UNIT, 0.062, -24.875, 0.1, Unit.valueOf("mg/L"), 100, Unit.valueOf("mg/L")); // Importante L maiuscola...
		createProbe("nh4_anox", physicalEp, logicalEp);
		physicalEp = createEpInterface(ElectricPotential.class, MILLI(VOLT), 1.0, 0.0, 400, Unit.valueOf("mV"), 2000, Unit.valueOf("mV"));
		logicalEp = createEpInterface(NO3.class, NH4.UNIT, 0.062, -24.875, 0.1, Unit.valueOf("mg/L"), 100, Unit.valueOf("mg/L"));
		createProbe("no3_anox", physicalEp, logicalEp);
		// Sonde (OX)
		physicalEp = createEpInterface(ElectricPotential.class, MILLI(VOLT), 1.0, 0.0, 400.815, Unit.valueOf("mV"), 2006.926, Unit.valueOf("mV"));
		logicalEp = createEpInterface(ORP.class, ORP.UNIT, 1.245, -1499.113, -1000, Unit.valueOf("mV"), 1000, Unit.valueOf("mV"));
		createProbe("orp_ox", physicalEp, logicalEp);
		physicalEp = createEpInterface(ElectricPotential.class, MILLI(VOLT), 1.0, 0.0, 400.623, Unit.valueOf("mV"), 1997.584, Unit.valueOf("mV"));
		logicalEp = createEpInterface(PH.class, PH.UNIT, 0.006, -0.509, 2, PH.UNIT, 12, PH.UNIT);
		createProbe("ph_ox", physicalEp, logicalEp);
		physicalEp = createEpInterface(ElectricPotential.class, MILLI(VOLT), 1.0, 0.0, 400, Unit.valueOf("mV"), 2000, Unit.valueOf("mV"));
		logicalEp = createEpInterface(NH4.class, NH4.UNIT, 0.062, -24.875, 0.1, Unit.valueOf("mg/L"), 100, Unit.valueOf("mg/L"));
		createProbe("nh4_ox", physicalEp, logicalEp);
		physicalEp = createEpInterface(ElectricPotential.class, MILLI(VOLT), 1.0, 0.0, 400, Unit.valueOf("mV"), 2000, Unit.valueOf("mV"));
		logicalEp = createEpInterface(NO3.class, NO3.UNIT, 0.062, -24.875, 0.1, Unit.valueOf("mg/L"), 100, Unit.valueOf("mg/L"));
		createProbe("no3_ox", physicalEp, logicalEp);
		// Sonde
		physicalEp = createEpInterface(ElectricPotential.class, MILLI(VOLT), 1.0, 0.0, 400, Unit.valueOf("mV"), 2000, Unit.valueOf("mV"));
		logicalEp = createEpInterface(TSS.class, TSS.UNIT, 2.5, -1000, 0.0, Unit.valueOf("mg/L"), 4000, Unit.valueOf("mg/L"));
		createProbe("tss", physicalEp, logicalEp);
		physicalEp = createEpInterface(ElectricPotential.class, MILLI(VOLT), 1.0, 0.0, 407.708, Unit.valueOf("mV"), 2026.152, Unit.valueOf("mV"));
		logicalEp = createEpInterface(DO.class, DO.UNIT, 0.012, -5.038, 0, Unit.valueOf("mg/L"), 20, Unit.valueOf("mg/L"));
		createProbe("oxigen", physicalEp, logicalEp);
		physicalEp = createEpInterface(ElectricPotential.class, MILLI(VOLT), 1.0, 0.0, 404.881, Unit.valueOf("mV"), 2027.685, Unit.valueOf("mV"));
		logicalEp = createEpInterface(Temperature.class, Unit.valueOf("°C"), 0.040, -21.217, -5, Unit.valueOf("°C"), 60, Unit.valueOf("°C"));
		createProbe("temperature", physicalEp, logicalEp);
		physicalEp = createEpInterface(ElectricPotential.class, MILLI(VOLT), 1.0, 0.0, 0, Unit.valueOf("V"), 10, Unit.valueOf("V"));
		logicalEp = createEpInterface(Flow.class, Flow.UNIT, 0.010, 0.0, 0, Unit.valueOf("m^3/h"), 100, Unit.valueOf("m^3/h"));
		createProbe("flow", physicalEp, logicalEp);
		
		// IRPump
		
		new ActuableDevice("pump", "Watson&Marlov", "model", "series", "serial", null, null); // TODO ID = IRPump
		
		// Connessioni
		
		new IPConnection(null, Endpoint.fromID("DT80", "ETHERNET_PORT"), "169.254.44.21", 7700); // TODO
		
		createConnection("dt80", "1(+#)", "orp_anox", "SOURCE");
		createConnection("dt80", "1(-#)", "ph_anox", "SOURCE");
		createConnection("dt80", "1(*#)", "nh4_anox", "SOURCE");
		createConnection("dt80", "2(-#)", "no3_anox", "SOURCE");
		
		createConnection("dt80", "2(*#)", "orp_ox", "SOURCE");
		createConnection("dt80", "3(*#)", "ph_ox", "SOURCE");
		createConnection("dt80", "5(*#)", "nh4_ox", "SOURCE");
		createConnection("dt80", "4(*#)", "no3_ox", "SOURCE");
		
		createConnection("dt80", "4(+-)", "tss", "SOURCE");
		createConnection("dt80", "3(+-)", "oxigen", "SOURCE");
		createConnection("dt80", "2(+#)", "temperature", "SOURCE");
		createConnection("dt80", "5(+-)", "flow", "SOURCE");
		
		// Host Connection
		createConnection("dt80", "HOST_RS232_PORT", "adam", "RS232");
		
		// Adam Slave Device (Pump) Connection
		createConnection("ADAM", "01S0C0Current", "PUMP", "CMD_PORT"); // TODO ID = IRPump
		
		// TODO aggiungere in modo coerente con i test
		// createConnection("adam", "", "internal_recycle_pump", "CMD_PORT");
		//
		setupDT80EndpointInterfaces();
		
		initialized = true;
	}

	@Test
	public final void testDevicesCreation() {
		
		assertNotNull(Device.fromID("dt80"));
		
		assertNotNull(Device.fromID("orp_anox"));
		assertNotNull(Device.fromID("ph_anox"));
		assertNotNull(Device.fromID("nh4_anox"));
		assertNotNull(Device.fromID("no3_anox"));
		
		assertNotNull(Device.fromID("orp_ox"));
		assertNotNull(Device.fromID("ph_ox"));
		assertNotNull(Device.fromID("nh4_ox"));
		assertNotNull(Device.fromID("no3_ox"));
		
		assertNotNull(Device.fromID("tss"));
		assertNotNull(Device.fromID("oxigen"));
		assertNotNull(Device.fromID("temperature"));
		assertNotNull(Device.fromID("flow"));
	}
	
	@Test
	public final void checkDT80Connections() {
		assertNotNull(Connection.fromEndpoint("dt80", "1(+#)"));
		assertNotNull(Connection.fromEndpoint("dt80", "1(-#)"));
		assertNotNull(Connection.fromEndpoint("dt80", "1(*#)"));
		assertNotNull(Connection.fromEndpoint("dt80", "2(-#)"));
		
		assertNotNull(Connection.fromEndpoint("dt80", "2(*#)"));
		assertNotNull(Connection.fromEndpoint("dt80", "3(*#)"));
		assertNotNull(Connection.fromEndpoint("dt80", "5(*#)"));
		assertNotNull(Connection.fromEndpoint("dt80", "4(*#)"));
		
		assertNotNull(Connection.fromEndpoint("dt80", "4(+-)"));
		assertNotNull(Connection.fromEndpoint("dt80", "3(+-)"));
		assertNotNull(Connection.fromEndpoint("dt80", "2(+#)"));
		assertNotNull(Connection.fromEndpoint("dt80", "5(+-)"));
	}
	
	@Test
	public final void checkProbesConnections() {
		assertNotNull(Connection.fromEndpoint("orp_anox", "SOURCE"));
		assertNotNull(Connection.fromEndpoint("ph_anox", "SOURCE"));
		assertNotNull(Connection.fromEndpoint("nh4_anox", "SOURCE"));
		assertNotNull(Connection.fromEndpoint("no3_anox", "SOURCE"));
		
		assertNotNull(Connection.fromEndpoint("orp_ox", "SOURCE"));
		assertNotNull(Connection.fromEndpoint("ph_ox", "SOURCE"));
		assertNotNull(Connection.fromEndpoint("nh4_ox", "SOURCE"));
		assertNotNull(Connection.fromEndpoint("no3_ox", "SOURCE"));
		
		assertNotNull(Connection.fromEndpoint("tss", "SOURCE"));
		assertNotNull(Connection.fromEndpoint("oxigen", "SOURCE"));
		assertNotNull(Connection.fromEndpoint("temperature", "SOURCE"));
		assertNotNull(Connection.fromEndpoint("flow", "SOURCE"));
	}
	
	@Test
	public final void checkConnectionsConsistence() {
		assertTrue(Connection.fromEndpoint("orp_anox", "SOURCE") == Connection.fromEndpoint("dt80", "1(+#)"));
		assertTrue(Connection.fromEndpoint("ph_anox", "SOURCE") == Connection.fromEndpoint("dt80", "1(-#)"));
		assertTrue(Connection.fromEndpoint("nh4_anox", "SOURCE") == Connection.fromEndpoint("dt80", "1(*#)"));
		assertTrue(Connection.fromEndpoint("no3_anox", "SOURCE") == Connection.fromEndpoint("dt80", "2(-#)"));
		
		assertTrue(Connection.fromEndpoint("orp_ox", "SOURCE") == Connection.fromEndpoint("dt80", "2(*#)"));
		assertTrue(Connection.fromEndpoint("ph_ox", "SOURCE") == Connection.fromEndpoint("dt80", "3(*#)"));
		assertTrue(Connection.fromEndpoint("nh4_ox", "SOURCE") == Connection.fromEndpoint("dt80", "5(*#)"));
		assertTrue(Connection.fromEndpoint("no3_ox", "SOURCE") == Connection.fromEndpoint("dt80", "4(*#)"));
		
		assertTrue(Connection.fromEndpoint("tss", "SOURCE") == Connection.fromEndpoint("dt80", "4(+-)"));
		assertTrue(Connection.fromEndpoint("oxigen", "SOURCE") == Connection.fromEndpoint("dt80", "3(+-)"));
		assertTrue(Connection.fromEndpoint("temperature", "SOURCE") == Connection.fromEndpoint("dt80", "2(+#)"));
		assertTrue(Connection.fromEndpoint("flow", "SOURCE") == Connection.fromEndpoint("dt80", "5(+-)"));
	}

	/*
	@Test
	public final void testWriteCommand() throws XPathExpressionException {
		XMLData wcXML = new XMLData("writeCommand");
		// Command
		wcXML.dom("writeCommand/commandID=WC1");
		wcXML.dom("writeCommand/timestamp=" + new Date().getTime());
		// DeviceCommand
		wcXML.dom("writeCommand/tgtDeviceID=orp_anox"); // TODO
		// PortCommand
		wcXML.dom("writeCommand/tgtPortID=SOURCE");
		// Amount
		XMLData amount = new XMLData("amount");
		amount.dom("amount/quantity=ElectricCurrent");
		amount.dom("amount/unit=mA");
		amount.dom("amount/value=100");
		//WriteCommand
		wcXML.append("writeCommand", (Element)amount.toDocument().getFirstChild()); // TODO Modifiche a XMLData...
		
		WriteCommand wc = WriteCommand.fromXML(wcXML.toDocument());
		
		// TODO "serialize() in XMLData"...
	}
	*/
	
	/*
	@Test
	public final void testReadCommand() throws XPathExpressionException {
		XMLData rcXML = new XMLData("readCommand");
		// Command
		rcXML.dom("readCommand/commandID=WC1");
		rcXML.dom("readCommand/timestamp=" + new Date().getTime());
		// DeviceCommand
		rcXML.dom("readCommand/tgtDeviceID=orp_anox");
		// PortCommand
		rcXML.dom("readCommand/tgtPortID=SOURCE");
		// ReadCommand
		rcXML.dom("readCommand/quantity=ElectricCurrent");
		
		ReadCommand wc = ReadCommand.fromXML(rcXML.toDocument());
		
		// TODO "serialize() in XMLData"...
	}
	*/
	
	@Test
	public final void testDeviceCreation() {
		Device dev;
		// DT80
		assertNotNull(dev = Device.fromID("dt80"));
		// Connessioni DT80
		assertTrue(dev.getConnections().length == 14);
		assertTrue(dev.getDeviceConnections().length == 13);
		assertTrue(dev.getConnectionsAsMaster().length == 13);
		assertTrue(dev.getHostConnections().length == 1);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
		// Controllo Connessione con Host
		assertTrue(Device.getRootDevices().length == 1 && dev == Device.getRootDevices()[0]);
		
		// orp_anox
		assertNotNull(dev = Device.fromID("orp_anox"));
		assertTrue(dev.getConnectionsAsMaster().length == 0);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
		// ph_anox
		assertNotNull(dev = Device.fromID("ph_anox"));
		assertTrue(dev.getConnectionsAsMaster().length == 0);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
		// nh4_anox
		assertNotNull(dev = Device.fromID("nh4_anox"));
		assertTrue(dev.getConnectionsAsMaster().length == 0);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
		// no3_anox
		assertNotNull(dev = Device.fromID("no3_anox"));
		assertTrue(dev.getConnectionsAsMaster().length == 0);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
		
		// orp_ox
		assertNotNull(dev = Device.fromID("orp_ox"));
		assertTrue(dev.getConnectionsAsMaster().length == 0);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
		// ph_ox
		assertNotNull(dev = Device.fromID("ph_ox"));
		assertTrue(dev.getConnectionsAsMaster().length == 0);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
		// nh4_ox
		assertNotNull(dev = Device.fromID("nh4_ox"));
		assertTrue(dev.getConnectionsAsMaster().length == 0);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
		assertNotNull(dev = Device.fromID("no3_ox"));
		// no3_ox
		assertTrue(dev.getConnectionsAsMaster().length == 0);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
		
		// tss
		assertNotNull(dev = Device.fromID("tss"));
		assertTrue(dev.getConnectionsAsMaster().length == 0);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
		// oxigen
		assertNotNull(dev = Device.fromID("oxigen"));
		assertTrue(dev.getConnectionsAsMaster().length == 0);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
		// temperature
		assertNotNull(dev = Device.fromID("temperature"));
		assertTrue(dev.getConnectionsAsMaster().length == 0);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
		// flow
		assertNotNull(dev = Device.fromID("flow"));
		assertTrue(dev.getConnectionsAsMaster().length == 0);
		assertTrue(dev.getConnectionsAsSlave().length == 1);
	}
	
	// TODO SystemScheduleDefinition/ScheduledOperation
	
	@Test
	public final void testScheduleDefinition() {
		
		ReadCommand rdCmd = new ReadCommand("RD_orp_anox", new Date(),
				Endpoint.fromID("orp_anox", "source"), ORP.class); // TODO Meglio specificare solo Endpoint (?)
		
		// TODO Qui dovrebbe avvenire la generazione dei comandi e l'impostazione della Schedule...
		
		DT80Schedule dt80Schedule = new DT80Schedule((DT80Device)Device.fromID("dt80"), null);
		dt80Schedule.setScheduledCommands(new DeviceCommand[] { rdCmd });
		
		SystemScheduleDefinition systemSchedule = new SystemScheduleDefinition("testScheduleDefinitionID", 
				new ScheduledOperation[] { dt80Schedule }, new Date(), 60000);
		dt80Schedule.setSystemScheduleDefinition(systemSchedule);

		assertTrue(dt80Schedule.getScheduleDefinition() == systemSchedule);
		assertTrue(dt80Schedule.getScheduleSupplierDevice() == Device.fromID("DT80"));
		assertTrue(systemSchedule.getScheduleCommands().length == 1 && systemSchedule.getScheduleCommands()[0] == rdCmd);
		
		// TODO Gestisci meglio la situazione...
		
		SystemScheduleDefinition.remove(systemSchedule);
	}
	
	@Test
	public final void testDT80Spans() {
		DT80Device dt80 = (DT80Device)Device.fromID("dt80");
		
		DT80Span span = dt80.getSpan(dt80.getEndpointById("1(+#)"), ORP.class);
		assertEquals(401.326, span.getLowerSignal(), 0.001);
		assertEquals(-500, span.getLowerPhysical(), 0.001);
		assertEquals(2022.401, span.getUpperSignal(), 0.001);
		assertEquals(500, span.getUpperPhysical(), 0.001);
		span = dt80.getSpan(dt80.getEndpointById("1(-#)"), PH.class);
		assertEquals(401.6, span.getLowerSignal(), 0.001);
		assertEquals(2, span.getLowerPhysical(), 0.001);
		assertEquals(2001.6, span.getUpperSignal(), 0.001);
		assertEquals(12, span.getUpperPhysical(), 0.001);
		span = dt80.getSpan(dt80.getEndpointById("1(*#)"), NH4.class);
		assertEquals(400, span.getLowerSignal(), 0.001);
		assertEquals(0.1, span.getLowerPhysical(), 0.001);
		assertEquals(2000, span.getUpperSignal(), 0.001);
		assertEquals(100, span.getUpperPhysical(), 0.001);
		span = dt80.getSpan(dt80.getEndpointById("2(-#)"), NO3.class);
		assertEquals(400, span.getLowerSignal(), 0.001);
		assertEquals(0.1, span.getLowerPhysical(), 0.001);
		assertEquals(2000, span.getUpperSignal(), 0.001);
		assertEquals(100, span.getUpperPhysical(), 0.001);
		
		span = dt80.getSpan(dt80.getEndpointById("2(*#)"), ORP.class);
		assertEquals(400.815, span.getLowerSignal(), 0.001);
		assertEquals(-1000, span.getLowerPhysical(), 0.001);
		assertEquals(2006.926, span.getUpperSignal(), 0.001);
		assertEquals(1000, span.getUpperPhysical(), 0.001);
		span = dt80.getSpan(dt80.getEndpointById("3(*#)"), PH.class);
		assertEquals(400.623, span.getLowerSignal(), 0.001);
		assertEquals(2, span.getLowerPhysical(), 0.001);
		assertEquals(1997.584, span.getUpperSignal(), 0.001);
		assertEquals(12, span.getUpperPhysical(), 0.001);
		span = dt80.getSpan(dt80.getEndpointById("5(*#)"), NH4.class);
		assertEquals(400, span.getLowerSignal(), 0.001);
		assertEquals(0.1, span.getLowerPhysical(), 0.001);
		assertEquals(2000, span.getUpperSignal(), 0.001);
		assertEquals(100, span.getUpperPhysical(), 0.001);
		span = dt80.getSpan(dt80.getEndpointById("4(*#)"), NO3.class);
		assertEquals(400, span.getLowerSignal(), 0.001);
		assertEquals(0.1, span.getLowerPhysical(), 0.001);
		assertEquals(2000, span.getUpperSignal(), 0.001);
		assertEquals(100, span.getUpperPhysical(), 0.001);
		
		span = dt80.getSpan(dt80.getEndpointById("4(+-)"), TSS.class);
		assertEquals(400, span.getLowerSignal(), 0.001);
		assertEquals(0, span.getLowerPhysical(), 0.001);
		assertEquals(2000, span.getUpperSignal(), 0.001);
		assertEquals(4000, span.getUpperPhysical(), 0.001);
		span = dt80.getSpan(dt80.getEndpointById("3(+-)"), DO.class);
		assertEquals(407.708, span.getLowerSignal(), 0.001);
		assertEquals(0, span.getLowerPhysical(), 0.001);
		assertEquals(2026.152, span.getUpperSignal(), 0.001);
		assertEquals(20, span.getUpperPhysical(), 0.001);
		span = dt80.getSpan(dt80.getEndpointById("2(+#)"), Temperature.class);
		assertEquals(404.881, span.getLowerSignal(), 0.001);
		assertEquals(-5, span.getLowerPhysical(), 0.001);
		assertEquals(2027.685, span.getUpperSignal(), 0.001);
		assertEquals(60, span.getUpperPhysical(), 0.001);
		span = dt80.getSpan(dt80.getEndpointById("5(+-)"), Flow.class);
		assertEquals(0, span.getLowerSignal(), 0.001);
		assertEquals(0, span.getLowerPhysical(), 0.001);
		assertEquals(10, span.getUpperSignal(), 0.001);
		assertEquals(100, span.getUpperPhysical(), 0.001);
	}
	
	// TODO
	
	public static ReadCommand[] setupScheduleCommands() {
		ReadCommand[] commands = new ReadCommand[12];
		commands[0] = new ReadCommand("ORP_Anox",
				Endpoint.fromID("ORP_Anox", "source"), ORP.class);
		commands[1] = new ReadCommand("pH_Anox",
				Endpoint.fromID("pH_Anox", "source"), PH.class);
		commands[2] = new ReadCommand("NH4_Anox",
				Endpoint.fromID("NH4_Anox", "source"), NH4.class);
		commands[3] = new ReadCommand("Temperature",
				Endpoint.fromID("Temperature", "source"), Temperature.class);
		commands[4] = new ReadCommand("NO3_Anox",
				Endpoint.fromID("NO3_Anox", "source"), NO3.class);
		commands[5] = new ReadCommand("ORP_Ox",
				Endpoint.fromID("ORP_Ox", "source"), ORP.class);
		commands[6] = new ReadCommand("Oxigen",
				Endpoint.fromID("Oxigen", "source"), DO.class);
		commands[7] = new ReadCommand("pH_Ox",
				Endpoint.fromID("pH_Ox", "source"), PH.class);
		commands[8] = new ReadCommand("TSS",
				Endpoint.fromID("TSS", "source"), TSS.class);
		commands[9] = new ReadCommand("NO3_Ox",
				Endpoint.fromID("NO3_Ox", "source"), NO3.class);
		commands[10] = new ReadCommand("Flow",
				Endpoint.fromID("Flow", "source"), Flow.class);
		commands[11] = new ReadCommand("NH4_Ox",
				Endpoint.fromID("NH4_Ox", "source"), NH4.class);
		return commands;
	}
	
	public static void fillMask(IInputMask.Mask mask, ReadCommand[] commands) {
		mask.add(new IInputMask.CommandMask(commands[0]));
		mask.add(new IInputMask.CommandMask(commands[1]));
		mask.add(new IInputMask.CommandMask(commands[2]));
		mask.add(new IInputMask.CommandMask(commands[3]));
		mask.add(new IInputMask.CommandMask(commands[4]));
		mask.add(new IInputMask.CommandMask(commands[5]));
		mask.add(new IInputMask.CommandMask(commands[6]));
		mask.add(new IInputMask.CommandMask(commands[7]));
		mask.add(new IInputMask.CommandMask(commands[8]));
		mask.add(new IInputMask.CommandMask(commands[9]));
		mask.add(new IInputMask.CommandMask(commands[10]));
		mask.add(new IInputMask.CommandMask(commands[11]));
	}
	
	/*
	public static DummySystemScheduleDefinition createDummySchedule() {
		// Simulo le azioni da eseguire all'atto di un comando di DEFINIZIONE
		// di una Schedule. Suppongo di ricavare da tale comando i "comandi equivalenti"
		// a cui la Schedule corrisponde (ipotizzo siano quelli attualmente attuati nell'impianto)...
		//
		ReadCommand[] commands = setupScheduleCommands();
		// Ora il sistema dovrebbe CAPIRE come implementare la schedule e in questo caso
		// particolare decidere di utilizzare le Schedule definibili nel DT80. Una volta
		// fatta questa scelta un modulo speifico si occuperà di definire una Schedule
		// all'interno del DT80, collegandola alla SystemScheduleDefinition richiesta
		// dall'utilizzatore...
		
		// TODO Usare anche qui comando definizione schedule...
		DT80Device dt80 = (DT80Device)Device.fromID("DT80");
		
		DT80Schedule dt80Schedule = new DT80Schedule(dt80,
				new DT80ScheduleDefinition(dt80.getAvailableScheduleId(), "Misure", 60, ScheduleFrequencyUnits.S, null)); // TODO Manca ChannelDefinition...
		dt80Schedule.setScheduledCommands(commands);
		// Il modulo stesso dovrà definire la ChannelDefinitionList per la schedule (utilizzando
		// i CommandGenerator per ottenerla) e definiendo inoltre la Maschera da associare agli input
		// relativi a tale Schedule:
		//
		IInputMask mask = new IInputMask.Mask((DT80Device)Device.fromID("DT80"));
		fillMask(mask.asMask(), commands);
		dt80Schedule.setScheduledInputMask(mask);
		// Generata la configurazione necessaria, il modulo dovrà associare la Schedule del
		// DT80 alla SystemScheduleDefinition del sistema (usata versione "Dummy" per poter
		// testare i risultati).

		((DT80Device)Device.fromID("DT80")).addSchedule(dt80Schedule);
		
		DummySystemScheduleDefinition systemSchedule = new DummySystemScheduleDefinition("ModelTest", 
				new ScheduledOperation[] { dt80Schedule }, new Date(), 60000);

		dt80Schedule.setSystemScheduleDefinition(systemSchedule);
		// Una volta creata la configurazione, il modulo dovrà anche ovviamente occuparsi
		// di INVIARE il comando di definizione della Schedule (potrà essere uno StatusCommand
		// per il DT80 o semplicemente un WriteCommand diretto sulla sua porta ETHERNET.

		return systemSchedule;
	}
	*/
	
	public static SystemScheduleDefinition createSchedule() {
		/* Simulo le azioni da eseguire all'atto di un comando di DEFINIZIONE
		 * di una Schedule. Suppongo di ricavare da tale comando i "comandi equivalenti"
		 * a cui la Schedule corrisponde (ipotizzo siano quelli attualmente attuati nell'impianto)...
		 */
		ReadCommand[] commands = setupScheduleCommands();
		/* Ora il sistema dovrebbe CAPIRE come implementare la schedule e in questo caso
		 * particolare decidere di utilizzare le Schedule definibili nel DT80. Una volta
		 * fatta questa scelta un modulo speifico si occuperà di definire una Schedule
		 * all'interno del DT80, collegandola alla SystemScheduleDefinition richiesta
		 * dall'utilizzatore...
		 */
		
		// TODO Usare anche qui comando definizione schedule...
		DT80Device dt80 = (DT80Device)Device.fromID("DT80");
		
		DT80Schedule dt80Schedule = new DT80Schedule(dt80,
				new DT80ScheduleDefinition(dt80.getAvailableScheduleId(), "Misure", 60, ScheduleFrequencyUnits.S, null)); // TODO Manca ChannelDefinition...
		dt80Schedule.setScheduledCommands(commands);
		/* Il modulo stesso dovrà definire la ChannelDefinitionList per la schedule (utilizzando
		 * i CommandGenerator per ottenerla) e definiendo inoltre la Maschera da associare agli input
		 * relativi a tale Schedule:
		 */
		IInputMask mask = new IInputMask.Mask((DT80Device)Device.fromID("DT80"));
		fillMask(mask.asMask(), commands);
		dt80Schedule.setScheduledInputMask(mask);
		/* Generata la configurazione necessaria, il modulo dovrà associare la Schedule del
		 * DT80 alla SystemScheduleDefinition del sistema (usata versione "Dummy" per poter
		 * testare i risultati).
		 */
		((DT80Device)Device.fromID("DT80")).addSchedule(dt80Schedule);
		
		SystemScheduleDefinition systemSchedule = new SystemScheduleDefinition("ModelTest_2", 
				new ScheduledOperation[] { dt80Schedule }, new Date(), 60000);

		dt80Schedule.setSystemScheduleDefinition(systemSchedule);
		/* Una volta creata la configurazione, il modulo dovrà anche ovviamente occuparsi
		 * di INVIARE il comando di definizione della Schedule (potrà essere uno StatusCommand
		 * per il DT80 o semplicemente un WriteCommand diretto sulla sua porta ETHERNET.
		 */
		
		return systemSchedule;
	}
	
	/*
	@Test
	public final void testSchedules() {
		
		DummySystemScheduleDefinition systemSchedule = createDummySchedule();
		
		// DT80InputRecognizer
		DT80InputRecognizer recog = new DT80InputRecognizer((DT80Device)Device.fromID("dt80"));
		String input = "D,089401,\"29_03_12\",2012/05/19,14:07:00,0.000366,0;A,0," +
				"-309.67612," +
				"7.690946," +
				"17.947476," +
				"42.686884," +
				"10.574842," +
				"118.18726," +
				"1.370179," +
				"7.176635," +
				"2.860431," +
				"21.44785," +
				"19.853794," +
				"2.174477" +
				";0170;4D46";
		
		// Operazioni svolte da InputReceptionThread
		IInputHandler handler = recog.getInputHandler(input.getBytes());
		handler.handleInput();
		
		SingleResult[] results = systemSchedule.getResults();
		
		assertEquals(-309.67612, ((AnalogueAmount)results[0].getValue()).getAmount().getEstimatedValue(), 0.001);
		assertEquals(7.690946, ((AnalogueAmount)results[1].getValue()).getAmount().getEstimatedValue(), 0.001);
		assertEquals(17.947476, ((AnalogueAmount)results[2].getValue()).getAmount().getEstimatedValue(), 0.001);
		assertEquals(42.686884, ((AnalogueAmount)results[3].getValue()).getAmount().getEstimatedValue(), 0.001);
		assertEquals(10.574842, ((AnalogueAmount)results[4].getValue()).getAmount().getEstimatedValue(), 0.001);
		assertEquals(118.18726, ((AnalogueAmount)results[5].getValue()).getAmount().getEstimatedValue(), 0.001);
		assertEquals(1.370179, ((AnalogueAmount)results[6].getValue()).getAmount().getEstimatedValue(), 0.001);
		assertEquals(7.176635, ((AnalogueAmount)results[7].getValue()).getAmount().getEstimatedValue(), 0.001);
		assertEquals(2.860431, ((AnalogueAmount)results[8].getValue()).getAmount().getEstimatedValue(), 0.001);
		assertEquals(21.44785, ((AnalogueAmount)results[9].getValue()).getAmount().getEstimatedValue(), 0.001);
		assertEquals(19.853794, ((AnalogueAmount)results[10].getValue()).getAmount().getEstimatedValue(), 0.001);
		assertEquals(2.174477, ((AnalogueAmount)results[11].getValue()).getAmount().getEstimatedValue(), 0.001);
		
		// TODO Gestisci meglio la situazione...
		
		SystemScheduleDefinition.remove(systemSchedule);
		((DT80Device)Device.fromID("DT80")).removeSchedule((DT80Schedule)((DT80Device)Device.fromID("DT80")).getDefinedSchedules()[0]);
	}
	*/
}
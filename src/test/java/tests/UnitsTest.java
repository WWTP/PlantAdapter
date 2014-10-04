package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

// Test delle singole unità prese singolarmente, basati sulla configurazione fatta dal metodo
// ModelTest.setup().

@RunWith(Suite.class)
@SuiteClasses({
	// Generazione comandi
	//tests.commandtreebuilder.CommandTreeBuilderTest.class,
	//tests.dependencesolver.DependenceSolverTest.class,
	tests.dev.gen.dcgs.adam.Adam5000DeviceCommandGeneratorTest.class,
	tests.dev.gen.dcgs.dt80.DT80DeviceCommandGeneratorTest.class,
	tests.dev.gen.DeviceCommandGenerationControllerTest.class,
	// Ricezione input
	//tests.inputs.dt80.recog.DT80InputRecognizerTest.class, // TODO
	//tests.inputs.dt80.recog.DT80ScheduleHandlerTest.class, // TODO
	tests.inputs.sources.InputSourceImplTest.class,
	// Modello (dispositivi)
	tests.model.sys.adam.Adam5000DeviceTest.class,
	//tests.model.sys.adam.Adam5000EngineeringUnitsTest.class,
	tests.model.sys.dt80.DT80Test.class,
	// Modello
	tests.model.ModelTest.class,
	// Parsers // TODO
	tests.parsers.dt80.readfixedformat.DT80ReadFixedFormatTest.class,
})
public class UnitsTest {}
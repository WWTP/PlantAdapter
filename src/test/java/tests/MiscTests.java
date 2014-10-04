package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

// Test vari che operano sulle singole unità prese singolarmente ma che
// NON si basano su ModelTest.setup() - si tratta dei primi test fatti, basati
// su piccoli modelli costruiti ad hoc (eventualmente da reimplementare per un modello più ampio).

@RunWith(Suite.class)
@SuiteClasses({
	tests.dependencesolver.DependenceSolverTest.class,
	// TODO Il seguente test non va a buon fine a causa delle classi "Dummy" da esso utilizzate
	// che non sono più consistenti con il funzionamento delle classi del modello. Occorre riscrivere
	// il test una volta che queste classi saranno state risistemate (farlo funzionare adesso può
	// rivelarsi complesso ed inutile).
	tests.commandtreebuilder.CommandTreeBuilderTest.class,
	tests.model.sys.adam.AdamCommandTest.class
})
public class MiscTests {

}
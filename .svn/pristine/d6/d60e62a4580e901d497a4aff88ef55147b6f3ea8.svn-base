package tests.commandtreebuilder;

import static org.junit.Assert.*;

import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import plantadapter.commands.ReadCommand;
import plantadapter.commands.dev.IInputMask;

import tests.commandtreebuilder.uut.DeviceCommandTreeBuilder;
import tests.commandtreebuilder.uut.IDeviceCommandTree;
import tests.dependencesolver.DummyDevice;
import tests.dependencesolver.DummyEndpoint;

// TODO Il seguente test non va a buon fine a causa delle classi "Dummy" da esso utilizzate
// che non sono più consistenti con il funzionamento delle classi del modello. Occorre riscrivere
// il test una volta che queste classi saranno state risistemate (farlo funzionare adesso può
// rivelarsi complesso ed inutile).

public class CommandTreeBuilderTest {

	@Test
	public final void testAdd() throws ParserConfigurationException { // TODO Ha senso come test dell'albero ma non nel contesto dei comandi...
		DeviceCommandTreeBuilder builder = new DeviceCommandTreeBuilder();
		// Dispositivi
		DummyDevice 
				dev1a = new DummyDevice("dev1a", new DummyEndpoint("")), 
				dev2 = new DummyDevice("dev2", new DummyEndpoint("")), 
				dev3 = new DummyDevice("dev3", new DummyEndpoint("")), 
				dev0 = new DummyDevice("dev0", new DummyEndpoint("")), 
				dev1b = new DummyDevice("dev1b", new DummyEndpoint("")), 
				dev_ = new DummyDevice("dev_", new DummyEndpoint("")), 
				dev1c = new DummyDevice("dev1c", new DummyEndpoint("")),
				dev_root = new DummyDevice("dev_root", new DummyEndpoint(""));
		// Aggiunta ADC
		DummyDeviceCommand dev1a_cmd = new DummyDeviceCommand(dev1a, "");
		// dev1a risolve dev2 e dev3
		builder.add(new DummyADC(dev1a_cmd, new DummyDeviceCommand(dev2, ""), new DummyDeviceCommand(dev3, "")));
		assertEquals(1, builder.getTrees().length);
		// dev0 risolve UNA PARTE di dev1a (oltre a dev1b)
		builder.add(new DummyADC(new DummyDeviceCommand(dev0, ""), dev1a_cmd, new DummyDeviceCommand(dev1b, "")));
		assertEquals(1, builder.getTrees().length);
		// dev_ risolvere l'altra parte di dev1a (oltre a dev1c)
		DummyDeviceCommand dev_cmd = new DummyDeviceCommand(dev_, "");
		builder.add(new DummyADC(dev_cmd, dev1a_cmd, new DummyDeviceCommand(dev1c, "")));
		assertEquals(2, builder.getTrees().length);
		// dev_root risolve dev_ (in totale ci sono due raw command da inviare!)
		builder.add(new DummyADC(new DummyDeviceCommand(dev_root, ""), dev_cmd));
		assertEquals(2, builder.getTrees().length);
	}
	
	@Test
	public final void testGetTrees() throws ParserConfigurationException {
		DeviceCommandTreeBuilder builder = new DeviceCommandTreeBuilder();
		// Dispositivi
		DummyDevice 
			dev1 = new DummyDevice("dev1'", new DummyEndpoint("")), 
			dev2 = new DummyDevice("dev2'", new DummyEndpoint("")), 
			dev3 = new DummyDevice("dev3'", new DummyEndpoint("")), 
			dev4 = new DummyDevice("dev4'", new DummyEndpoint("")), 
			dev5 = new DummyDevice("dev5'", new DummyEndpoint("")), 
			dev6 = new DummyDevice("dev6'", new DummyEndpoint(""));
		/* Suppongo che venga passato un comando a ciascuno dei DCG di dev4, dev5 e dev6 (collegati a dev3)
		 * e che quindi vengano ottenuti 3 diversi ADC recanti come "risolutore" un comando diretto sempre
		 * ai medesimi dispotivi:
		 */
		DummyDeviceCommand cmd_dev4_0 = new DummyDeviceCommand(dev4, ""), cmd_dev5_0 = new DummyDeviceCommand(dev5, ""), cmd_dev6_0 = new DummyDeviceCommand(dev6, ""),
				cmd_dev4_1 = new DummyDeviceCommand(dev4, ""), cmd_dev5_1 = new DummyDeviceCommand(dev5, ""), cmd_dev6_1 = new DummyDeviceCommand(dev6, "");
		// Aggiugo i 3 ADC:
		builder.add(new DummyADC(cmd_dev4_1, cmd_dev4_0));
		builder.add(new DummyADC(cmd_dev5_1, cmd_dev5_0));
		builder.add(new DummyADC(cmd_dev6_1, cmd_dev6_0));
		assertEquals(3, builder.getTrees().length); // Ora ho 3 "alberi" distinti
		/* Suppongo che i tre nuovi comandi (cmd_devX_1) siano quindi passati al DCG di dev3 dal DCE (in quanto
		 * facenti parte di una stessa DCGL) e suppongo che questo risponda generando due comandi (diretti ovviamente a dev3):
		 * cmd_dev3_a che risolve cmd_dev4_1 e cmd_dev6_1; cmd_dev3_b che risolve cmd_dev5_1...
		 */
		DummyDeviceCommand cmd_dev3_a = new DummyDeviceCommand(dev3, ""), cmd_dev3_b = new DummyDeviceCommand(dev3, "");
		builder.add(new DummyADC(cmd_dev3_a, cmd_dev4_1, cmd_dev6_1));
		builder.add(new DummyADC(cmd_dev3_b, cmd_dev5_1));
		assertEquals(2, builder.getTrees().length); // Ora ho 2 "alberi" (in quanto ho due diversi comandi "aggregatori" che puntano a dev3)
		/* Ottimo: ora suppongo che cmd_dev3_a e cmd_dev3_b siano risolti rispettivamente da cmd_dev1 e cmd_dev2,
		 * diretti ai dispositivi indicati (questi saranno i raw-commands, trattandosi di dispositivi radice...).
		 */
		DummyDeviceCommand cmd_dev1 = new DummyDeviceCommand(dev1, ""), cmd_dev2 = new DummyDeviceCommand(dev2, "");
		builder.add(new DummyADC(cmd_dev1, cmd_dev3_a));
		assertEquals(2, builder.getTrees().length);
		builder.add(new DummyADC(cmd_dev2, cmd_dev3_b));
		assertEquals(2, builder.getTrees().length); // Sempre 2 alberi...
		// Divido i due alberi...
		IDeviceCommandTree[] trees = builder.getTrees();
		// Verifica RawCommands
		assertTrue(trees[0].getRawCommand() == cmd_dev1);
		assertTrue(trees[1].getRawCommand() == cmd_dev2);
		// Verifica FirstCommands (erano tre originariamente...)
		assertEquals(2, trees[0].getFirstCommands().length);
		assertEquals(1, trees[1].getFirstCommands().length);
		assertTrue(trees[0].getFirstCommands()[0] == cmd_dev4_0);
		assertTrue(trees[0].getFirstCommands()[1] == cmd_dev6_0);
		assertTrue(trees[1].getFirstCommands()[0] == cmd_dev5_0);
		// Verifica MASCHERE
		IInputMask mask = trees[0].getMask(); // Maschera comando generato da dev1
		assertTrue(mask.asMask().getSourceDevice() == dev1);
		assertEquals(1, mask.asMask().getSubMasksCount());
		// Maschera comando cmd_dev3_a (generato da dev_3). Nota: ha due sotto-maschere (relative a cmd_dev4_1 e cmd_dev6_1)...
		mask = mask.asMask().iterator().next();
		assertTrue(mask.asMask().getSourceDevice() == dev3);
		assertEquals(2, mask.asMask().getSubMasksCount());
		// maschera associata a cmd_dev4_1 (contiene il comando cmd_dev4_0)
		Iterator<IInputMask> iterator = mask.asMask().iterator(); // Iteratore per ottenere tutti i comandi allo stesso livello...
		mask = iterator.next();
		assertTrue(mask.asMask().getSourceDevice() == dev4);
		assertEquals(1, mask.asMask().getSubMasksCount());
		// comando cmd_dev4_0
		mask = mask.asMask().iterator().next();
		assertTrue(mask.asCommand().getTargetDevice() == dev4);
		assertTrue(mask.asCommand() instanceof ReadCommand);
		// maschera associata a cmd_dev6_1 (contiene il comando cmd_dev6_0)
		mask = iterator.next();
		assertTrue(mask.asMask().getSourceDevice() == dev6);
		assertEquals(1, mask.asMask().getSubMasksCount());
		// comando cmd_dev6_0
		mask = mask.asMask().iterator().next();
		assertTrue(mask.asCommand().getTargetDevice() == dev6);
		assertTrue(mask.asCommand() instanceof ReadCommand);
		// ANALOGO PER L'ALTRO ALBERO
		mask = trees[1].getMask(); // Maschera comando generato da dev1
		assertTrue(mask.asMask().getSourceDevice() == dev2);
		assertEquals(1, mask.asMask().getSubMasksCount());
		// Maschera comando cmd_dev3_b (generato da dev_3). Nota: ha una sola sotto-maschera (relativa a cmd_dev5_1)...
		mask = mask.asMask().iterator().next();
		assertTrue(mask.asMask().getSourceDevice() == dev3);
		assertEquals(1, mask.asMask().getSubMasksCount());
		// maschera associata a cmd_dev5_1 (contiene il comando cmd_dev5_0)
		mask =  mask.asMask().iterator().next();
		assertTrue(mask.asMask().getSourceDevice() == dev5);
		assertEquals(1, mask.asMask().getSubMasksCount());
		// comando cmd_dev5_0
		mask = mask.asMask().iterator().next();
		assertTrue(mask.asCommand().getTargetDevice() == dev5);
		assertTrue(mask.asCommand() instanceof ReadCommand);
		/* HELL YEAH! */
	}
}
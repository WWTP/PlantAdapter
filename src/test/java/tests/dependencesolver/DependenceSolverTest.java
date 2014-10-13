package tests.dependencesolver;

import static org.junit.Assert.*;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import plantmodel.Connection;
import plantmodel.Endpoint;
import tests.dependencesolver.uut.DependenceSolverImpl;
import tests.dependencesolver.uut.DeviceCommandGenerationList;
import tests.dependencesolver.uut.IDependenceSolver;
import tests.dummyimpl.DummyDevice;
import tests.dummyimpl.DummyEndpoint;

public class DependenceSolverTest {
	
	@Test
	public final void testConstuctor() throws ParserConfigurationException {
		DummyDevice s1, s2, s3, d1, d2;
		// Devices
		s1 = new DummyDevice("S1", "SLAVE");
		s2 = new DummyDevice("S2", "SLAVE");
		s3 = new DummyDevice("S3", "SLAVE");
		d1 = new DummyDevice("D1", "MASTER_S1", "MASTER_D2", "SLAVE");
		d2 = new DummyDevice("D2", "MASTER_S2", "MASTER_S3", "SLAVE");
		// Connections (si associano automaticamente ai Devices)
		new Connection(
				Endpoint.fromID("D1", "MASTER_S1"), 
				Endpoint.fromID("S1", "SLAVE"));
		new Connection(
				Endpoint.fromID("D2", "MASTER_S2"), 
				Endpoint.fromID("S2", "SLAVE"));
		new Connection(
				Endpoint.fromID("D2", "MASTER_S3"), 
				Endpoint.fromID("S3", "SLAVE"));
		new Connection(
				Endpoint.fromID("D1", "MASTER_D2"), 
				Endpoint.fromID("D2", "SLAVE"));
		// Connessione a null
		new Connection(
				null, 
				Endpoint.fromID("D1", "SLAVE"));
		// First Commands
		DeviceCommandGenerationList cmdS1 = new DeviceCommandGenerationList(s1);
		DeviceCommandGenerationList cmdS2 = new DeviceCommandGenerationList(s2);
		DeviceCommandGenerationList cmdS3 =new DeviceCommandGenerationList(s3);
		DeviceCommandGenerationList[] cmds = new DeviceCommandGenerationList[] {
			cmdS1, cmdS2, cmdS3
		};
		//
		IDependenceSolver solver = new DependenceSolverImpl(cmds);
		// Dispositivi con dipendenze (ipotizzo di avere un comando da inviare)
		assertTrue(solver.hasDependence(new DeviceCommandGenerationList(d2)));
		assertTrue(solver.hasDependence(new DeviceCommandGenerationList(d1)));
		// Dispositivi senza dipendenze (ipotizzo di avere un comando da inviare)
		assertFalse(solver.hasDependence(new DeviceCommandGenerationList(s1)));
		assertFalse(solver.hasDependence(new DeviceCommandGenerationList(s2)));
		assertFalse(solver.hasDependence(new DeviceCommandGenerationList(s2)));
	}

	@Test
	public final void testUpdate() throws ParserConfigurationException {
		DummyDevice s1, s2, s3, d1, d2;
		// Devices
		s1 = new DummyDevice("S1'", "SLAVE");
		s2 = new DummyDevice("S2'", "SLAVE");
		s3 = new DummyDevice("S3'", "SLAVE");
		d1 = new DummyDevice("D1'", "MASTER_S1", "MASTER_D2", "SLAVE");
		d2 = new DummyDevice("D2'", "MASTER_S2", "MASTER_S3", "SLAVE");
		// Connections (si associano automaticamente ai Devices)
		new Connection(
				Endpoint.fromID("D1'", "MASTER_S1"), 
				Endpoint.fromID("S1'", "SLAVE"));
		new Connection(
				Endpoint.fromID("D2'", "MASTER_S2"), 
				Endpoint.fromID("S2'", "SLAVE"));
		new Connection(
				Endpoint.fromID("D2'", "MASTER_S3"), 
				Endpoint.fromID("S3'", "SLAVE"));
		new Connection(
				Endpoint.fromID("D1'", "MASTER_D2"), 
				Endpoint.fromID("D2'", "SLAVE"));
		// Connessione a null
		new Connection(
				null, 
				Endpoint.fromID("D1'", "SLAVE"));
		// First Commands
		DeviceCommandGenerationList cmdS1 = new DeviceCommandGenerationList(s1);
		DeviceCommandGenerationList cmdS2 = new DeviceCommandGenerationList(s2);
		DeviceCommandGenerationList cmdS3 =new DeviceCommandGenerationList(s3);
		DeviceCommandGenerationList[] cmds = new DeviceCommandGenerationList[] {
			cmdS1, cmdS2, cmdS3
		};
		//
		IDependenceSolver solver = new DependenceSolverImpl(cmds);
		/* Risolvo cmdS1 e ottengo un nuovo comando DA DIRIGERE a D1;
		 * non posso però inviarlo perché D1 ha ancora dipendenze (nonostante
		 * io qui faccia update(), dall'altra parte c'è D2 che attende di essere risolto)!
		 */
		DeviceCommandGenerationList cmdD1_S1 = new DeviceCommandGenerationList(d1); // Ottenuto dal DCG di S1
		solver.update(cmdS1);
		// Verifica dipendenze comando appena ottenuto
		assertTrue(solver.hasDependence(cmdD1_S1));
		// Altre verifiche per sicurezza...
		assertTrue(solver.hasDependence(new DeviceCommandGenerationList(d2)));
		assertTrue(solver.hasDependence(new DeviceCommandGenerationList(d1)));
		/*
		 * Risolvo cmdS2, ottengo un comando diretto a D2 ma non posso inviarlo
		 * perché vi è ancora una dipendenza (cmdS3).
		 */
		DeviceCommandGenerationList cmdD2_S2 = new DeviceCommandGenerationList(d2); // Ottenuto dal DCG di S2
		solver.update(cmdS2);
		// Verifica dipendenze comando appena ottenuto
		assertTrue(solver.hasDependence(cmdD2_S2));
		// Altre verifiche per sicurezza...
		assertTrue(solver.hasDependence(new DeviceCommandGenerationList(d2)));
		assertTrue(solver.hasDependence(new DeviceCommandGenerationList(d1)));
		/* 
		 * Risolvo anche cmdS3, ora il comando ottenuto non ha altre dipendenze
		 * e potrei inviare il comando ottenuto.
		 */
		DeviceCommandGenerationList cmdD2_S3 = new DeviceCommandGenerationList(d2); // Ottenuto dal DCG di S3
		solver.update(cmdS3);
		// Verifica dipendenze comando appena ottenuto
		assertFalse(solver.hasDependence(cmdD2_S3));
		// Altre verifiche per sicurezza...
		assertFalse(solver.hasDependence(new DeviceCommandGenerationList(d2)));
		assertTrue(solver.hasDependence(new DeviceCommandGenerationList(d1)));
		/*
		 * Ottimo: ora suppongo di aggregare cmdD2_S2 e cmdD2_S3
		 * in un unica lista cmdD2_S2_S3 (l'ipotesi è che questa lista contenga
		 * tutti i comandi per i quali sono state risolte dipendenze relative
		 * al dispositivo associato alla lista - comunque la cosa non interessa al DepencenceSolver).
		 */
		DeviceCommandGenerationList cmdD2_S2_S3 = new DeviceCommandGenerationList(d2); // Semplice unione, NON ricevuta da un DCG
		// Posso inviarlo in quanto (come già visto):
		assertFalse(solver.hasDependence(cmdD2_S2_S3));
		// Eseguo quindi update():
		solver.update(cmdD2_S2_S3);
		/* Ho risolto la dipendenza su S1 dovuta a D2! Suppongo quindi di avere ottenuto un comando 
		 * cmdD1_D2_S2_S3 da inviare a D1, il quale non ha più dipendenze (all'inizio ho risolto
		 * quella dovuta a S1, ora quella dovuta a D2)...
		 */
		DeviceCommandGenerationList cmdD1_D2_S2_S3 = new DeviceCommandGenerationList(d1); // Ottenuto dal DCG di D2
		// Ora potrei inviare direttamente i comandi!
		assertFalse(solver.hasDependence(cmdD1_D2_S2_S3));
		assertFalse(solver.hasDependence(cmdD1_S1));
		// Ovviamente per inviarli devo prima creare la lista:
		DeviceCommandGenerationList cmdD1 = new DeviceCommandGenerationList(d1); // Semplice unione, NON ricevuta da un DCG
		assertFalse(solver.hasDependence(cmdD1));
		/*
		 * In questo momento ho inviato l'ultimo comando al DCG di D1 e ho ottenuto i RawCommands corrispondenti
		 * (in realtà l'Executor creerà man mano anche l'albero delle dipendenze).
		 */
	}
}
package plantadapter.dcgs;

import plantadapter.commands.PortCommand;
import plantmodel.Device;
import plantmodel.Endpoint;

// TODO Kill me please! I'm unuseful!

public class DCGUtils {
	
	// TODO L'invocazione del seguente metodo dovrebbe essere svolta da DCGController e non dai singoli DCG (che dovrebbero
	// essere astratti dai dispositivi che li circondano, ricevendo solamente comandi diretti a loro stessi - in questo modo
	// sar� possibile risolvere esternamente eventuali problematiche di "routing").
	
	/**
	 * <p>Ad un DCG pu� giungere un comando diretto al proprio <code>Device</code> di riferimento
	 * oppure ad uno <i>slave</i> di questo. Nel primo caso l'<code>Endpoint</code> da considerare
	 * � quello specificato dal comando, altrimenti � il suo <i>master</i>.</p>
	 * @param command
	 * @param device
	 * @return
	 */
	public static Endpoint getCommandTargetPort(PortCommand command, Device device) {
		return command.getTargetDevice() == device ? command.getTargetPort() : command.getTargetPort().getConnectionsAsSlave()[0].getMasterEndpoint();
	}
}
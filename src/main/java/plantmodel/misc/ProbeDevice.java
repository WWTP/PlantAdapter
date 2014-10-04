package plantmodel.misc;

import plantadapter.annotations.DeviceInfo;
import plantadapter.dcgs.impl.ProbeDeviceCommandGenerator;
import plantmodel.Device;
import plantmodel.Endpoint;
import plantmodel.AnalogueEndpointInterface;

/**
 * <p>Modella una sonda con un'unica uscita, il cui <code>ID</code> è <code>SOURCE</code>.</p>
 * @author JCC
 *
 */
@DeviceInfo
(
		deviceCommandGeneratorClass = ProbeDeviceCommandGenerator.class
)
public class ProbeDevice extends Device {

	private static Endpoint[] setupEndpoint(AnalogueEndpointInterface physicalQuantityInterface, AnalogueEndpointInterface logicalQuantityInterface) {
		Endpoint source = new Endpoint("SOURCE", physicalQuantityInterface);
		source.addLogicalInterface(logicalQuantityInterface);
		return new Endpoint[] { source };
	}
	
	public ProbeDevice(String id, String manufacturer, String model, String series, String serialNumber,
			AnalogueEndpointInterface physicalQuantityInterface, AnalogueEndpointInterface logicalQuantityInterface) {
		super(id, "Probe", manufacturer, model, series, serialNumber, setupEndpoint(physicalQuantityInterface, logicalQuantityInterface), null);
	}
	
	// TODO
}
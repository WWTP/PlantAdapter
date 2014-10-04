package plantadapter.cmdgen.adapters;

import plantmodel.Device;
import plantmodel.Endpoint;

class EndpointAdapter {
	
	private Endpoint adaptedEndpoint;
	
	public EndpointAdapter(Endpoint endpoint)
	{
		this.adaptedEndpoint = endpoint;
	}

	public void setEndpoint(Endpoint e) {
		this.adaptedEndpoint = e;
	}
	
	public Endpoint getTargetPort() {
		return this.adaptedEndpoint;
	}
	
	public Device getTargetDevice()	{
		return this.adaptedEndpoint == null ? null : this.adaptedEndpoint.getDevice();
	}
}
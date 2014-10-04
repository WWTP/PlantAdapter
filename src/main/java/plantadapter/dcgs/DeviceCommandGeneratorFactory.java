package plantadapter.dcgs;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import plantadapter.Configuration;
import plantadapter.annotations.DeviceInfo;
import plantadapter.excpts.DeviceNotFoundException;

import plantmodel.Device;
import plantmodel.adam.Adam5000Device;
import plantmodel.dt80.DT80Device;
import plantmodel.misc.ActuableDevice;
import plantmodel.misc.ProbeDevice;

import utils.refl.ReflectionHelper;
import utils.refl.filters.SubClassTypeFilter;

public class DeviceCommandGeneratorFactory { // TODO Togliere la dipendenza da IPlant e spostare solo ai livelli di competenza...
	
	private static Map<Class<? extends Device>, Class<? extends IDeviceCommandGenerator>> typesMap = 
			new HashMap<Class<? extends Device>, Class<? extends IDeviceCommandGenerator>>();
	
	/* Mappa d'appoggio in caso la reflection fallisca e relativo metodo di accesso */
	
	private static Map<Class<?>, DeviceInfo> devClassesInfoMap;
	
	private static void buildClassesInfoMap(Class<?> c) {
		
		devClassesInfoMap = new HashMap<Class<?>, DeviceInfo>();
			
		devClassesInfoMap.put(DT80Device.class, DT80Device.class.getAnnotation(DeviceInfo.class));
		devClassesInfoMap.put(Adam5000Device.class, Adam5000Device.class.getAnnotation(DeviceInfo.class));
		devClassesInfoMap.put(ActuableDevice.class, ActuableDevice.class.getAnnotation(DeviceInfo.class));
		devClassesInfoMap.put(ProbeDevice.class, ProbeDevice.class.getAnnotation(DeviceInfo.class));
	}
	
	private Class<?> getDCGClassForDev(Class<?> c) {
		// Classe del DCG!
		Class<?> i = typesMap.get(c);
		
		if (i == null) {
			if (devClassesInfoMap == null) buildClassesInfoMap(c);
			i = devClassesInfoMap.get(c).deviceCommandGeneratorClass();
		}
		
		//System.err.println("DeviceCommandGeneratorFactory: " + c + " " + i);
		
		return i; 
	}
	
	/* */
	
	static
	{
		ReflectionHelper deviceReflector = new ReflectionHelper(Configuration.DEVICES_PACKAGES);
		
		for (Class<? extends Device> devType : deviceReflector.getTypes(new SubClassTypeFilter<Device>(Device.class))) {
			if (!Modifier.isAbstract(devType.getModifiers())) { // TODO AbstractClassTypeFilter...
				DeviceInfo devInfo = devType.getAnnotation(DeviceInfo.class);
				if (devInfo != null)
					typesMap.put(devType, devInfo.deviceCommandGeneratorClass());
			}
		}
	}
	
	private Map<Device, IDeviceCommandGenerator> instancesMap;
	
	public DeviceCommandGeneratorFactory() {
		this.instancesMap = new HashMap<Device, IDeviceCommandGenerator>();
	}
	
	public IDeviceCommandGenerator getDeviceCommandGenerator(Device device) throws DeviceNotFoundException {

		if (this.instancesMap.containsKey(device))
			return this.instancesMap.get(device);
		else
		{
			try 
			{
				// TODO Check cast...
				IDeviceCommandGenerator dcg = (IDeviceCommandGenerator) getDCGClassForDev(device.getClass()).getConstructor(device.getClass()).newInstance(device);
				this.instancesMap.put(device, dcg);
				return dcg;
			} 
			catch (Exception e) {
				throw new RuntimeException(e); // TODO
			}
		}
	}
}
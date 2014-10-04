package plantadapter.inputs;

import java.lang.reflect.Modifier;

import java.util.HashMap;
import java.util.Map;

import plantadapter.Configuration;
import plantadapter.IPlant;
import plantadapter.PlantAdapter;
import plantadapter.annotations.DeviceInfo;
import plantadapter.communication.ISink;

import plantmodel.Device;
import plantmodel.adam.Adam5000Device;
import plantmodel.dt80.DT80Device;
import plantmodel.misc.ActuableDevice;
import plantmodel.misc.ProbeDevice;

import utils.refl.ReflectionHelper;
import utils.refl.filters.SubClassTypeFilter;

public class InputRecognizerFactory {

	private static Map<Class<? extends Device>, Class<? extends ISink<byte[]>>> typesMap = 
			new HashMap<Class<? extends Device>, Class<? extends ISink<byte[]>>>();
	
	/* Mappa d'appoggio in caso la reflection fallisca e relativo metodo di accesso */
	
	private static Map<Class<?>, DeviceInfo> devClassesInfoMap;
	
	private static void buildClassesInfoMap(Class<?> c) {
		
		devClassesInfoMap = new HashMap<Class<?>, DeviceInfo>();
			
		devClassesInfoMap.put(DT80Device.class, DT80Device.class.getAnnotation(DeviceInfo.class));
		devClassesInfoMap.put(Adam5000Device.class, Adam5000Device.class.getAnnotation(DeviceInfo.class));
		devClassesInfoMap.put(ActuableDevice.class, ActuableDevice.class.getAnnotation(DeviceInfo.class));
		devClassesInfoMap.put(ProbeDevice.class, ProbeDevice.class.getAnnotation(DeviceInfo.class));
	}
	
	private Class<?> getInputRecogClassForDev(Class<?> c) {
		// Classe del DCG!
		Class<?> i = typesMap.get(c);
		
		if (i == null) {
			if (devClassesInfoMap == null) buildClassesInfoMap(c);
			i = devClassesInfoMap.get(c).deviceInputRecognizerClass();
		}
		
		//System.err.println("InputRecognizerFactory: " + c + " " + i);
		
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
					typesMap.put(devType, devInfo.deviceInputRecognizerClass());
			}
		}
	}

	private final Map<Device, ISink<byte[]>> instancesMap;
	private final IPlant plant;
	
	public InputRecognizerFactory(PlantAdapter plant) {
		this.instancesMap = new HashMap<Device, ISink<byte[]>>();
		this.plant = plant.getPlantModel();
	}
	
	public ISink<byte[]> getInputRecognized(Device device) {
		if (this.instancesMap.containsKey(device))
			return this.instancesMap.get(device);
		else
		{
			try 
			{
				// TODO Check cast...
				ISink<byte[]> ir = (ISink<byte[]>) getInputRecogClassForDev(device.getClass()).getConstructor(IPlant.class, device.getClass()).newInstance(this.plant, device);
				this.instancesMap.put(device, ir);
				return ir;
			} 
			catch (Exception e) {
				throw new RuntimeException(e); // TODO
			}
		}
	}
}
package plantadapter.parsers;

import java.lang.reflect.Modifier;

import java.util.HashMap;
import java.util.Map;

import plantadapter.Configuration;
import plantadapter.annotations.DeviceInfo;
import plantmodel.Device;
import plantmodel.adam.Adam5000Device;
import plantmodel.dt80.DT80Device;
import plantmodel.misc.ActuableDevice;
import plantmodel.misc.ProbeDevice;

import utils.refl.ReflectionHelper;
import utils.refl.filters.SubClassTypeFilter;

public class InputParserFactory {
	
	private static Map<Class<? extends Device>, Class<? extends IInputParser>> typesMap = 
			new HashMap<Class<? extends Device>, Class<? extends IInputParser>>();
	
	/* Mappa d'appoggio in caso la reflection fallisca e relativo metodo di accesso */
	
	private static Map<Class<?>, DeviceInfo> devClassesInfoMap;
	
	private static void buildClassesInfoMap(Class<?> c) {
		
		devClassesInfoMap = new HashMap<Class<?>, DeviceInfo>();
			
		devClassesInfoMap.put(DT80Device.class, DT80Device.class.getAnnotation(DeviceInfo.class));
		devClassesInfoMap.put(Adam5000Device.class, Adam5000Device.class.getAnnotation(DeviceInfo.class));
		devClassesInfoMap.put(ActuableDevice.class, ActuableDevice.class.getAnnotation(DeviceInfo.class));
		devClassesInfoMap.put(ProbeDevice.class, ProbeDevice.class.getAnnotation(DeviceInfo.class));
	}
	
	private static Class<?> getInputParserClassForDev(Class<?> c) {
		// Classe del DCG!
		Class<?> i = typesMap.get(c);
		
		if (i == null) {
			if (devClassesInfoMap == null) buildClassesInfoMap(c);
			i = devClassesInfoMap.get(c).deviceInputParserClass();
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
					typesMap.put(devType, devInfo.deviceInputParserClass());
			}
		}
	}

	private InputParserFactory() {/* static class */ }
	
	public static IInputParser getInputParser(Device device) {
		try 
		{
			return (IInputParser) getInputParserClassForDev(device.getClass()).getConstructor(device.getClass()).newInstance(device);
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
package plantadapter.schedules;


// TODO Anche questo concetto introduce una dipendenza ciclica
// => fare un adapter

public interface ScheduleSupplierDevice {
	public DeviceScheduledOperation[] getDefinedSchedules();
}
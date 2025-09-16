public class SmartHomeDeviceDemo {
	public static void main(String[] args) {
		Device[] devices = new Device[] {
			new SmartLight(),
			new SmartFan(),
			new SmartThermostat()
		};

		for (Device device : devices) {
			System.out.println("Turning on " + device.getName());
			device.turnOn();
		}

		for (Device device : devices) {
			System.out.println("Turning off " + device.getName());
			device.turnOff();
		}
	}
}



public class SmartThermostat implements Device {
	private boolean on;
	private double temperature = 24.0;

	@Override
	public void turnOn() {
		on = true;
		System.out.println("SmartThermostat is ON at " + temperature + "°C");
	}

	@Override
	public void turnOff() {
		on = false;
		System.out.println("SmartThermostat is OFF");
	}
}



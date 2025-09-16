public class SmartLight implements Device {
	private boolean on;

	@Override
	public void turnOn() {
		on = true;
		System.out.println("SmartLight is ON");
	}

	@Override
	public void turnOff() {
		on = false;
		System.out.println("SmartLight is OFF");
	}
}



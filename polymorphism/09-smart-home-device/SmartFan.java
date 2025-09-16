public class SmartFan implements Device {
	private boolean on;
	private int speed = 0;

	@Override
	public void turnOn() {
		on = true;
		speed = 1;
		System.out.println("SmartFan is ON at speed " + speed);
	}

	@Override
	public void turnOff() {
		on = false;
		speed = 0;
		System.out.println("SmartFan is OFF");
	}
}



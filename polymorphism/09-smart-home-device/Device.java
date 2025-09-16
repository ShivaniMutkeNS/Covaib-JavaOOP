public interface Device {
	void turnOn();
	void turnOff();

	default String getName() {
		return getClass().getSimpleName();
	}
}



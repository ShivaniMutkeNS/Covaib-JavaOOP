public class CarNavigator implements Navigator {
	@Override
	public String route(String source, String destination) {
		return "Car route from " + source + " to " + destination + " via highways";
	}
}



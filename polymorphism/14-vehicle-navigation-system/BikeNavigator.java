public class BikeNavigator implements Navigator {
	@Override
	public String route(String source, String destination) {
		return "Bike route from " + source + " to " + destination + " via local streets";
	}
}



public class TruckNavigator implements Navigator {
	@Override
	public String route(String source, String destination) {
		return "Truck route from " + source + " to " + destination + " avoiding low bridges";
	}
}



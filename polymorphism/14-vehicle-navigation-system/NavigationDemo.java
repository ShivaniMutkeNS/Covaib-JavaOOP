public class NavigationDemo {
	public static void main(String[] args) {
		Navigator[] navigators = new Navigator[] {
			new CarNavigator(),
			new BikeNavigator(),
			new TruckNavigator()
		};

		String src = "Station";
		String dst = "Airport";
		for (Navigator n : navigators) {
			System.out.println(n.getName() + ": " + n.route(src, dst));
		}
	}
}



public class RidePricingDemo {
	public static void main(String[] args) {
		RidePricingStrategy[] strategies = new RidePricingStrategy[] {
			new CityRidePricing(30.0, 12.0, 2.0),
			new AirportRidePricing(50.0, 15.0, 120.0),
			new PremiumRidePricing(60.0, 20.0, 3.0, 40.0)
		};

		double distanceKm = 12.5;
		double durationMinutes = 28.0;
		double surgeMultiplier = 1.3;

		for (RidePricingStrategy strategy : strategies) {
			double price = strategy.calculatePrice(distanceKm, durationMinutes, surgeMultiplier);
			System.out.println(strategy.getName() + " price: Rs. " + String.format("%.2f", price));
		}
	}
}



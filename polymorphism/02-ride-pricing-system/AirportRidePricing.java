public class AirportRidePricing implements RidePricingStrategy {
	private final double flatPickupFee;
	private final double perKmRate;
	private final double minimumFare;

	public AirportRidePricing(double flatPickupFee, double perKmRate, double minimumFare) {
		this.flatPickupFee = flatPickupFee;
		this.perKmRate = perKmRate;
		this.minimumFare = minimumFare;
	}

	@Override
	public double calculatePrice(double distanceKm, double durationMinutes, double surgeMultiplier) {
		double cost = flatPickupFee + (perKmRate * distanceKm);
		cost = Math.max(cost, minimumFare);
		return Math.max(0.0, cost * surgeMultiplier);
	}
}



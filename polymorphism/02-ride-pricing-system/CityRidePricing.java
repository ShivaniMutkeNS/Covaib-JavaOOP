public class CityRidePricing implements RidePricingStrategy {
	private final double baseFare;
	private final double perKmRate;
	private final double perMinuteRate;

	public CityRidePricing(double baseFare, double perKmRate, double perMinuteRate) {
		this.baseFare = baseFare;
		this.perKmRate = perKmRate;
		this.perMinuteRate = perMinuteRate;
	}

	@Override
	public double calculatePrice(double distanceKm, double durationMinutes, double surgeMultiplier) {
		double cost = baseFare + (perKmRate * distanceKm) + (perMinuteRate * durationMinutes);
		return Math.max(0.0, cost * surgeMultiplier);
	}
}



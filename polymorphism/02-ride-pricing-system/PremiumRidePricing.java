public class PremiumRidePricing implements RidePricingStrategy {
	private final double baseFare;
	private final double perKmRate;
	private final double perMinuteRate;
	private final double luxuryFee;

	public PremiumRidePricing(double baseFare, double perKmRate, double perMinuteRate, double luxuryFee) {
		this.baseFare = baseFare;
		this.perKmRate = perKmRate;
		this.perMinuteRate = perMinuteRate;
		this.luxuryFee = luxuryFee;
	}

	@Override
	public double calculatePrice(double distanceKm, double durationMinutes, double surgeMultiplier) {
		double cost = baseFare + (perKmRate * distanceKm) + (perMinuteRate * durationMinutes) + luxuryFee;
		return Math.max(0.0, cost * surgeMultiplier);
	}
}



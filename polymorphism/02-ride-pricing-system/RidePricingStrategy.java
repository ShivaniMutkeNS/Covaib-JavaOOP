public interface RidePricingStrategy {
	/**
	 * Calculate price for a ride using distance (km), duration (minutes) and surge multiplier.
	 */
	double calculatePrice(double distanceKm, double durationMinutes, double surgeMultiplier);

	default String getName() {
		return getClass().getSimpleName();
	}
}



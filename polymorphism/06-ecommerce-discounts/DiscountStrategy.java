public interface DiscountStrategy {
	/**
	 * Calculate discounted price for a given original price and quantity.
	 */
	double apply(double originalPrice, int quantity);

	default String getName() {
		return getClass().getSimpleName();
	}
}



public class PercentageDiscount implements DiscountStrategy {
	private final double percent; // 0..100

	public PercentageDiscount(double percent) {
		this.percent = Math.max(0.0, Math.min(100.0, percent));
	}

	@Override
	public double apply(double originalPrice, int quantity) {
		double discounted = originalPrice * (1.0 - (percent / 100.0));
		return Math.max(0.0, discounted);
	}
}



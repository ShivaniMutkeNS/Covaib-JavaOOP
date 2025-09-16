public class NoDiscount implements DiscountStrategy {
	@Override
	public double apply(double originalPrice, int quantity) {
		return Math.max(0.0, originalPrice);
	}
}



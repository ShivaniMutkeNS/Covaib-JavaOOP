public class BulkDiscount implements DiscountStrategy {
	private final int thresholdQuantity;
	private final double perUnitDiscount;

	public BulkDiscount(int thresholdQuantity, double perUnitDiscount) {
		this.thresholdQuantity = Math.max(1, thresholdQuantity);
		this.perUnitDiscount = Math.max(0.0, perUnitDiscount);
	}

	@Override
	public double apply(double originalPrice, int quantity) {
		if (quantity >= thresholdQuantity) {
			return Math.max(0.0, originalPrice - (perUnitDiscount * quantity));
		}
		return Math.max(0.0, originalPrice);
	}
}



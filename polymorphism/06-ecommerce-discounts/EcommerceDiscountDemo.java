public class EcommerceDiscountDemo {
	public static void main(String[] args) {
		DiscountStrategy[] strategies = new DiscountStrategy[] {
			new NoDiscount(),
			new PercentageDiscount(15.0),
			new BulkDiscount(5, 10.0)
		};

		double price = 1000.0;
		int quantity = 7;
		for (DiscountStrategy strategy : strategies) {
			double finalPrice = strategy.apply(price, quantity);
			System.out.println(strategy.getName() + " => Rs. " + String.format("%.2f", finalPrice));
		}
	}
}


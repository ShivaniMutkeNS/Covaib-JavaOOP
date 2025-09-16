public class MaxAggregator implements Aggregator {
	private Double max = null;

	@Override
	public void add(double value) {
		if (max == null || value > max) {
			max = value;
		}
	}

	@Override
	public double result() {
		return max == null ? Double.NEGATIVE_INFINITY : max;
	}
}



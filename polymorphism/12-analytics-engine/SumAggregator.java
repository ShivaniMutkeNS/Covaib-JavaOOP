public class SumAggregator implements Aggregator {
	private double sum = 0.0;

	@Override
	public void add(double value) {
		sum += value;
	}

	@Override
	public double result() {
		return sum;
	}
}



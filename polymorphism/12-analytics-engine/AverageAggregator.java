public class AverageAggregator implements Aggregator {
	private double sum = 0.0;
	private int count = 0;

	@Override
	public void add(double value) {
		sum += value;
		count++;
	}

	@Override
	public double result() {
		return count == 0 ? 0.0 : sum / count;
	}
}



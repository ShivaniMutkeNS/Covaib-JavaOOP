public class AnalyticsDemo {
	public static void main(String[] args) {
		double[] values = new double[] {12.5, 9.3, 15.2, 7.1, 10.0};

		Aggregator[] aggregators = new Aggregator[] {
			new SumAggregator(),
			new AverageAggregator(),
			new MaxAggregator()
		};

		for (Aggregator agg : aggregators) {
			for (double v : values) agg.add(v);
			System.out.println(agg.getName() + ": " + String.format("%.2f", agg.result()));
		}
	}
}



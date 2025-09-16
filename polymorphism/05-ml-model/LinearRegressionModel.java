public class LinearRegressionModel implements MLModel {
	private final double[] weights;
	private final double bias;

	public LinearRegressionModel(double[] weights, double bias) {
		this.weights = weights.clone();
		this.bias = bias;
	}

	@Override
	public double predict(double[] features) {
		double sum = bias;
		int len = Math.min(weights.length, features.length);
		for (int i = 0; i < len; i++) {
			sum += weights[i] * features[i];
		}
		return sum;
	}
}



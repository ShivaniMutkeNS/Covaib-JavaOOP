public class NeuralNetworkModel implements MLModel {
	private final int hiddenUnits;

	public NeuralNetworkModel(int hiddenUnits) {
		this.hiddenUnits = Math.max(1, hiddenUnits);
	}

	@Override
	public double predict(double[] features) {
		double sum = 0.0;
		for (double f : features) {
			double activated = 1.0 / (1.0 + Math.exp(-f));
			sum += activated;
		}
		return sum / hiddenUnits;
	}
}



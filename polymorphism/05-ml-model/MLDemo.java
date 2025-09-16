public class MLDemo {
	public static void main(String[] args) {
		MLModel[] models = new MLModel[] {
			new LinearRegressionModel(new double[] {0.5, -0.2, 1.0}, 0.3),
			new RandomForestModel(5, 42L),
			new NeuralNetworkModel(8)
		};

		double[] features = new double[] {1.2, 0.7, -0.3};
		for (MLModel model : models) {
			double pred = model.predict(features);
			System.out.println(model.getName() + " prediction: " + String.format("%.4f", pred));
		}
	}
}



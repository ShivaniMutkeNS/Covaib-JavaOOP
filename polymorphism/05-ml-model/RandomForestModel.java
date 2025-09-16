import java.util.Random;

public class RandomForestModel implements MLModel {
	private final int numTrees;
	private final long seed;

	public RandomForestModel(int numTrees, long seed) {
		this.numTrees = Math.max(1, numTrees);
		this.seed = seed;
	}

	@Override
	public double predict(double[] features) {
		Random random = new Random(seed);
		double sum = 0.0;
		for (int t = 0; t < numTrees; t++) {
			double vote = 0.0;
			for (double f : features) {
				vote += (random.nextDouble() - 0.5) * f;
			}
			sum += vote;
		}
		return sum / numTrees;
	}
}



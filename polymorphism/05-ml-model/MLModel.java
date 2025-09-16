public interface MLModel {
	double predict(double[] features);

	default String getName() {
		return getClass().getSimpleName();
	}
}



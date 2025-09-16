public interface Aggregator {
	void add(double value);
	double result();

	default String getName() {
		return getClass().getSimpleName();
	}
}



public interface Logger {
	void log(String message);

	default String getName() {
		return getClass().getSimpleName();
	}
}



public interface Navigator {
	String route(String source, String destination);

	default String getName() {
		return getClass().getSimpleName();
	}
}



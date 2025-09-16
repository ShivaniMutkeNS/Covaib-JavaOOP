public interface DocumentExporter {
	String export(String content);

	default String getName() {
		return getClass().getSimpleName();
	}
}



public interface AssessmentGrader {
	double grade(String submission);

	default String getName() {
		return getClass().getSimpleName();
	}
}



public class EssayGrader implements AssessmentGrader {
	@Override
	public double grade(String submission) {
		int words = submission.trim().isEmpty() ? 0 : submission.trim().split("\\s+").length;
		return Math.min(100.0, words);
	}
}



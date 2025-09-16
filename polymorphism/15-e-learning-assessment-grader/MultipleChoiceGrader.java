public class MultipleChoiceGrader implements AssessmentGrader {
	@Override
	public double grade(String submission) {
		int correct = (int) submission.chars().filter(c -> c == 'T').count();
		int total = submission.length();
		return total == 0 ? 0.0 : (100.0 * correct / total);
	}
}



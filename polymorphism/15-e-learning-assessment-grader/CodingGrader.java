public class CodingGrader implements AssessmentGrader {
	@Override
	public double grade(String submission) {
		boolean compiles = submission.contains("main(");
		int lines = submission.split("\n").length;
		double base = compiles ? 60.0 : 30.0;
		return Math.min(100.0, base + Math.min(40.0, lines));
	}
}



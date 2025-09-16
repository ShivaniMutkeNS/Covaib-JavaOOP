public class AssessmentGraderDemo {
	public static void main(String[] args) {
		AssessmentGrader[] graders = new AssessmentGrader[] {
			new MultipleChoiceGrader(),
			new EssayGrader(),
			new CodingGrader()
		};

		String mc = "TTFTFT";
		String essay = "This is a concise essay with enough words to pass";
		String code = "public class X { public static void main(String[] args){} }\nSystem.out.println(1);";

		System.out.println("MC: " + graders[0].grade(mc));
		System.out.println("Essay: " + graders[1].grade(essay));
		System.out.println("Code: " + graders[2].grade(code));
	}
}



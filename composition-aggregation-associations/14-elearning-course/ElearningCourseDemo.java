package composition.elearning;

import java.util.*;

/**
 * Comprehensive demo for the E-learning Course Management System
 * Demonstrates: Strategy Pattern, Observer Pattern, State Pattern, Command Pattern
 */
public class ElearningCourseDemo {
    
    public static void main(String[] args) {
        System.out.println("=== E-learning Course Management System Demo ===\n");
        
        // Create course
        Course javaCourse = new Course("COURSE001", "Advanced Java Programming", 
                                     "Comprehensive Java course covering OOP, design patterns, and best practices",
                                     "Dr. Smith", CourseCategory.PROGRAMMING, CourseDifficulty.INTERMEDIATE);
        
        // Add event listener
        CourseEventListener logger = new CourseEventLogger();
        javaCourse.addEventListener(logger);
        
        System.out.println("1. COURSE CREATION AND SETUP");
        System.out.println("=============================");
        System.out.printf("Created course: %s by %s\n", javaCourse.getTitle(), javaCourse.getInstructor());
        System.out.printf("Category: %s, Difficulty: %s\n", 
                         javaCourse.getCategory().getDisplayName(), 
                         javaCourse.getDifficulty().getDisplayName());
        System.out.printf("Current state: %s\n\n", javaCourse.getState().getDisplayName());
        
        System.out.println("2. MODULE AND CONTENT CREATION");
        System.out.println("===============================");
        
        // Create modules
        Module module1 = createModule1();
        Module module2 = createModule2();
        Module module3 = createModule3();
        
        // Add modules to course
        javaCourse.addModule(module1);
        javaCourse.addModule(module2);
        javaCourse.addModule(module3);
        
        System.out.printf("Added %d modules to the course\n", javaCourse.getModules().size());
        for (Module module : javaCourse.getModules()) {
            System.out.printf("  - %s (%d contents, %d assessments)\n", 
                             module.getTitle(), module.getTotalContentCount(), module.getTotalAssessmentCount());
        }
        
        System.out.println("\n3. COURSE PUBLISHING");
        System.out.println("====================");
        
        CourseStateResult publishResult = javaCourse.publishCourse();
        System.out.println("Publish result: " + publishResult.getMessage());
        System.out.println("New state: " + publishResult.getNewState().getDisplayName());
        
        System.out.println("\n4. STUDENT ENROLLMENT");
        System.out.println("=====================");
        
        // Create students
        Student alice = new Student("STU001", "Alice Johnson", "alice@example.com");
        Student bob = new Student("STU002", "Bob Smith", "bob@example.com");
        Student charlie = new Student("STU003", "Charlie Brown", "charlie@example.com");
        
        // Configure student preferences
        alice.getPreferences().setLearningStyle("Visual");
        bob.getPreferences().setLearningStyle("Auditory");
        charlie.getPreferences().setLearningStyle("Kinesthetic");
        
        // Enroll students
        EnrollmentResult aliceEnrollment = javaCourse.enrollStudent(alice);
        EnrollmentResult bobEnrollment = javaCourse.enrollStudent(bob);
        EnrollmentResult charlieEnrollment = javaCourse.enrollStudent(charlie);
        
        System.out.println("Alice enrollment: " + aliceEnrollment.getMessage());
        System.out.println("Bob enrollment: " + bobEnrollment.getMessage());
        System.out.println("Charlie enrollment: " + charlieEnrollment.getMessage());
        System.out.printf("Total enrolled students: %d\n", javaCourse.getEnrolledStudents().size());
        
        System.out.println("\n5. CONTENT DELIVERY STRATEGIES");
        System.out.println("===============================");
        
        // Test different content delivery strategies
        System.out.println("Testing Sequential Content Delivery:");
        testContentDelivery(javaCourse, alice, "Sequential");
        
        System.out.println("\nSwitching to Adaptive Content Delivery:");
        javaCourse.setContentDeliveryStrategy(new AdaptiveContentDeliveryStrategy());
        testContentDelivery(javaCourse, bob, "Adaptive");
        
        System.out.println("\nSwitching to Flexible Content Delivery:");
        javaCourse.setContentDeliveryStrategy(new FlexibleContentDeliveryStrategy());
        testContentDelivery(javaCourse, charlie, "Flexible");
        
        System.out.println("\n6. ASSESSMENT STRATEGIES");
        System.out.println("=========================");
        
        // Test different assessment strategies
        System.out.println("Testing Standard Assessment Strategy:");
        javaCourse.setAssessmentStrategy(new StandardAssessmentStrategy());
        testAssessments(javaCourse, alice);
        
        System.out.println("\nSwitching to Adaptive Assessment Strategy:");
        javaCourse.setAssessmentStrategy(new AdaptiveAssessmentStrategy());
        testAssessments(javaCourse, bob);
        
        System.out.println("\nSwitching to Peer Review Assessment Strategy:");
        javaCourse.setAssessmentStrategy(new PeerReviewAssessmentStrategy());
        testAssessments(javaCourse, charlie);
        
        System.out.println("\n7. PROGRESS TRACKING STRATEGIES");
        System.out.println("================================");
        
        // Test different progress tracking strategies
        System.out.println("Testing Detailed Progress Tracking:");
        javaCourse.setProgressTrackingStrategy(new DetailedProgressTrackingStrategy());
        testProgressTracking(javaCourse, alice);
        
        System.out.println("\nSwitching to Gamified Progress Tracking:");
        javaCourse.setProgressTrackingStrategy(new GamifiedProgressTrackingStrategy());
        testProgressTracking(javaCourse, bob);
        
        System.out.println("\nSwitching to Simple Progress Tracking:");
        javaCourse.setProgressTrackingStrategy(new SimpleProgressTrackingStrategy());
        testProgressTracking(javaCourse, charlie);
        
        System.out.println("\n8. COURSE COMPLETION AND CERTIFICATES");
        System.out.println("======================================");
        
        // Simulate course completion
        simulateStudentProgress(javaCourse, alice);
        simulateStudentProgress(javaCourse, bob);
        
        CourseCompletionResult aliceCompletion = javaCourse.checkCourseCompletion(alice.getStudentId());
        CourseCompletionResult bobCompletion = javaCourse.checkCourseCompletion(bob.getStudentId());
        
        System.out.println("Alice completion: " + aliceCompletion.getMessage());
        if (aliceCompletion.isCompleted() && aliceCompletion.getCertificate() != null) {
            System.out.println("Certificate issued: " + aliceCompletion.getCertificate());
        }
        
        System.out.println("Bob completion: " + bobCompletion.getMessage());
        if (bobCompletion.isCompleted() && bobCompletion.getCertificate() != null) {
            System.out.println("Certificate issued: " + bobCompletion.getCertificate());
        }
        
        System.out.println("\n9. ANALYTICS AND REPORTING");
        System.out.println("===========================");
        
        CourseAnalytics analytics = javaCourse.getAnalytics();
        System.out.println("Course Analytics:");
        System.out.println("  Total Enrollments: " + analytics.getTotalEnrollments());
        System.out.println("  Total Completions: " + analytics.getTotalCompletions());
        System.out.println("  Modules Added: " + analytics.getModulesAdded());
        
        EngagementMetrics engagement = javaCourse.getEngagementMetrics();
        System.out.println("\nEngagement Metrics:");
        System.out.println("  " + engagement);
        
        System.out.println("\nProgress Reports:");
        List<StudentProgress> progressReports = javaCourse.getProgressReport();
        for (StudentProgress progress : progressReports) {
            System.out.println("  " + progress);
        }
        
        System.out.println("\n10. COURSE MANAGEMENT");
        System.out.println("=====================");
        
        // Test course settings
        CourseSettings settings = javaCourse.getSettings();
        settings.setMaxStudents(50);
        settings.setRequireSequentialProgress(true);
        settings.setCertificateThreshold(80);
        
        System.out.println("Updated course settings:");
        System.out.println("  Max Students: " + settings.getMaxStudents());
        System.out.println("  Sequential Progress Required: " + settings.isRequireSequentialProgress());
        System.out.println("  Certificate Threshold: " + settings.getCertificateThreshold() + "%");
        
        // Archive course
        CourseStateResult archiveResult = javaCourse.archiveCourse();
        System.out.println("\nArchive result: " + archiveResult.getMessage());
        System.out.println("Final state: " + archiveResult.getNewState().getDisplayName());
        
        System.out.println("\n=== Demo Complete ===");
        
        // Cleanup
        javaCourse.shutdown();
    }
    
    private static void testContentDelivery(Course course, Student student, String strategyName) {
        ContentDeliveryResult result = course.deliverContent(student.getStudentId(), 
                                                           course.getModules().get(0).getModuleId());
        System.out.printf("  %s delivery for %s: %s\n", 
                         strategyName, student.getName(), result.getMessage());
        if (result.getContent() != null) {
            System.out.printf("    Content: %s (%s)\n", 
                             result.getContent().getTitle(), result.getDeliveryNote());
        }
    }
    
    private static void testAssessments(Course course, Student student) {
        Module firstModule = course.getModules().get(0);
        if (!firstModule.getAssessments().isEmpty()) {
            Assessment assessment = firstModule.getAssessments().get(0);
            AssessmentResult result = course.conductAssessment(student.getStudentId(), 
                                                             assessment.getAssessmentId());
            System.out.printf("  Assessment for %s: %s (Score: %.1f)\n", 
                             student.getName(), result.getMessage(), result.getScore());
            if (result.getFeedback() != null) {
                System.out.printf("    Feedback: %s\n", result.getFeedback());
            }
        }
    }
    
    private static void testProgressTracking(Course course, Student student) {
        ProgressResult result = course.getStudentProgress(student.getStudentId());
        if (result.isSuccess() && result.getSummary() != null) {
            System.out.printf("  Progress for %s: %s\n", student.getName(), result.getSummary());
        }
    }
    
    private static void simulateStudentProgress(Course course, Student student) {
        // Simulate completing content and assessments
        for (Module module : course.getModules()) {
            for (Content content : module.getContents()) {
                StudentProgress progress = course.getProgressReport().stream()
                        .filter(p -> p.getStudentId().equals(student.getStudentId()))
                        .findFirst()
                        .orElse(null);
                
                if (progress != null) {
                    progress.recordContentAccess(content.getContentId());
                    progress.recordContentCompletion(content.getContentId());
                }
            }
            
            for (Assessment assessment : module.getAssessments()) {
                course.conductAssessment(student.getStudentId(), assessment.getAssessmentId());
            }
        }
    }
    
    // Helper methods to create modules with content and assessments
    private static Module createModule1() {
        Module module = new Module("MOD001", "Object-Oriented Programming Fundamentals", 
                                 "Introduction to OOP concepts in Java", 1);
        
        // Add content
        module.addContent(new Content("CONT001", "Introduction to Classes and Objects", 
                                    "Learn the basics of classes and objects", ContentType.VIDEO, 
                                    "video/intro-classes.mp4", 30, 1));
        module.addContent(new Content("CONT002", "Inheritance and Polymorphism", 
                                    "Understanding inheritance hierarchies", ContentType.TEXT, 
                                    "text/inheritance.html", 45, 2));
        module.addContent(new Content("CONT003", "Encapsulation and Abstraction", 
                                    "Data hiding and abstraction principles", ContentType.INTERACTIVE, 
                                    "interactive/encapsulation.html", 25, 3));
        
        // Add assessments
        Assessment quiz1 = new Assessment("ASSESS001", "OOP Fundamentals Quiz", 
                                        "Test your understanding of OOP basics", 
                                        AssessmentType.QUIZ, 100, 70, 15);
        
        // Add questions to assessment
        quiz1.addQuestion(new Question("Q001", "What is encapsulation?", "multiple-choice",
                                     Arrays.asList("Data hiding", "Inheritance", "Polymorphism", "Abstraction"),
                                     "Data hiding", 10));
        quiz1.addQuestion(new Question("Q002", "Which keyword is used for inheritance in Java?", "multiple-choice",
                                     Arrays.asList("extends", "implements", "inherits", "super"),
                                     "extends", 10));
        
        module.addAssessment(quiz1);
        
        return module;
    }
    
    private static Module createModule2() {
        Module module = new Module("MOD002", "Design Patterns", 
                                 "Common design patterns in Java", 2);
        
        // Add content
        module.addContent(new Content("CONT004", "Singleton Pattern", 
                                    "Implementing the singleton pattern", ContentType.VIDEO, 
                                    "video/singleton.mp4", 20, 1));
        module.addContent(new Content("CONT005", "Factory Pattern", 
                                    "Creating objects with factory pattern", ContentType.TEXT, 
                                    "text/factory.html", 35, 2));
        module.addContent(new Content("CONT006", "Observer Pattern", 
                                    "Event-driven programming with observer", ContentType.INTERACTIVE, 
                                    "interactive/observer.html", 40, 3));
        
        // Add assessment
        Assessment assignment = new Assessment("ASSESS002", "Design Patterns Implementation", 
                                             "Implement three design patterns", 
                                             AssessmentType.ASSIGNMENT, 100, 75, 120);
        
        assignment.addQuestion(new Question("Q003", "Implement a Singleton class", "coding",
                                          Arrays.asList(), "public class Singleton", 30));
        assignment.addQuestion(new Question("Q004", "Create a Factory method", "coding",
                                          Arrays.asList(), "public static Object create", 35));
        
        module.addAssessment(assignment);
        
        return module;
    }
    
    private static Module createModule3() {
        Module module = new Module("MOD003", "Advanced Java Concepts", 
                                 "Concurrency, streams, and modern Java features", 3);
        
        // Add content
        module.addContent(new Content("CONT007", "Java Streams API", 
                                    "Functional programming with streams", ContentType.VIDEO, 
                                    "video/streams.mp4", 50, 1));
        module.addContent(new Content("CONT008", "Concurrency and Threading", 
                                    "Multi-threaded programming in Java", ContentType.TEXT, 
                                    "text/concurrency.html", 60, 2));
        module.addContent(new Content("CONT009", "Lambda Expressions", 
                                    "Functional interfaces and lambdas", ContentType.INTERACTIVE, 
                                    "interactive/lambdas.html", 30, 3));
        
        // Add final exam
        Assessment exam = new Assessment("ASSESS003", "Final Exam", 
                                       "Comprehensive exam covering all topics", 
                                       AssessmentType.EXAM, 100, 80, 90);
        
        exam.addQuestion(new Question("Q005", "What is a lambda expression?", "essay",
                                    Arrays.asList(), "A lambda expression is", 20));
        exam.addQuestion(new Question("Q006", "Explain the benefits of streams", "essay",
                                    Arrays.asList(), "Streams provide", 25));
        
        module.addAssessment(exam);
        
        return module;
    }
}

/**
 * Event listener implementation for logging course events
 */
class CourseEventLogger implements CourseEventListener {
    @Override
    public void onCourseEvent(String courseId, String eventMessage) {
        System.out.println("[COURSE EVENT] " + courseId + ": " + eventMessage);
    }
    
    @Override
    public void onStudentEnrolled(String courseId, String studentId) {
        System.out.println("[ENROLLMENT] Student " + studentId + " enrolled in course " + courseId);
    }
    
    @Override
    public void onStudentCompleted(String courseId, String studentId) {
        System.out.println("[COMPLETION] Student " + studentId + " completed course " + courseId);
    }
    
    @Override
    public void onModuleAdded(String courseId, String moduleId) {
        System.out.println("[MODULE] Module " + moduleId + " added to course " + courseId);
    }
    
    @Override
    public void onAssessmentCompleted(String courseId, String studentId, String assessmentId, double score) {
        System.out.println("[ASSESSMENT] Student " + studentId + " completed assessment " + assessmentId + 
                         " with score " + score);
    }
}

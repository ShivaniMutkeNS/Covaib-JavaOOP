package composition.elearning;

import java.util.*;
import java.util.concurrent.*;

/**
 * MAANG-Level E-learning Course Management System using Composition
 * Demonstrates: Strategy Pattern, Observer Pattern, State Pattern, Command Pattern
 */
public class Course {
    private final String courseId;
    private final String title;
    private final String description;
    private final String instructor;
    private final CourseCategory category;
    private final CourseDifficulty difficulty;
    private final List<Module> modules;
    private final List<Student> enrolledStudents;
    private final List<CourseEventListener> listeners;
    private final Map<String, StudentProgress> studentProgress;
    private final CourseSettings settings;
    private final CourseAnalytics analytics;
    private final ExecutorService eventExecutor;
    private CourseState state;
    private AssessmentStrategy assessmentStrategy;
    private ContentDeliveryStrategy contentDeliveryStrategy;
    private ProgressTrackingStrategy progressTrackingStrategy;
    private final long createdAt;
    private long lastModified;
    
    public Course(String courseId, String title, String description, String instructor, 
                 CourseCategory category, CourseDifficulty difficulty) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructor = instructor;
        this.category = category;
        this.difficulty = difficulty;
        this.modules = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.studentProgress = new ConcurrentHashMap<>();
        this.settings = new CourseSettings();
        this.analytics = new CourseAnalytics();
        this.eventExecutor = Executors.newFixedThreadPool(3);
        this.state = CourseState.DRAFT;
        this.assessmentStrategy = new StandardAssessmentStrategy();
        this.contentDeliveryStrategy = new SequentialContentDeliveryStrategy();
        this.progressTrackingStrategy = new DetailedProgressTrackingStrategy();
        this.createdAt = System.currentTimeMillis();
        this.lastModified = createdAt;
    }
    
    // Runtime strategy swapping - core composition flexibility
    public void setAssessmentStrategy(AssessmentStrategy strategy) {
        this.assessmentStrategy = strategy;
        this.lastModified = System.currentTimeMillis();
        notifyListeners("Assessment strategy updated: " + strategy.getStrategyName());
    }
    
    public void setContentDeliveryStrategy(ContentDeliveryStrategy strategy) {
        this.contentDeliveryStrategy = strategy;
        this.lastModified = System.currentTimeMillis();
        notifyListeners("Content delivery strategy updated: " + strategy.getStrategyName());
    }
    
    public void setProgressTrackingStrategy(ProgressTrackingStrategy strategy) {
        this.progressTrackingStrategy = strategy;
        this.lastModified = System.currentTimeMillis();
        notifyListeners("Progress tracking strategy updated: " + strategy.getStrategyName());
    }
    
    // Course lifecycle management
    public CourseStateResult publishCourse() {
        if (state != CourseState.DRAFT) {
            return new CourseStateResult(false, "Course can only be published from draft state", state);
        }
        
        if (modules.isEmpty()) {
            return new CourseStateResult(false, "Cannot publish course without modules", state);
        }
        
        state = CourseState.PUBLISHED;
        lastModified = System.currentTimeMillis();
        analytics.recordStateChange(state);
        notifyListeners("Course published successfully");
        
        return new CourseStateResult(true, "Course published successfully", state);
    }
    
    public CourseStateResult archiveCourse() {
        if (state == CourseState.ARCHIVED) {
            return new CourseStateResult(false, "Course is already archived", state);
        }
        
        state = CourseState.ARCHIVED;
        lastModified = System.currentTimeMillis();
        analytics.recordStateChange(state);
        notifyListeners("Course archived");
        
        return new CourseStateResult(true, "Course archived successfully", state);
    }
    
    // Module management
    public ModuleResult addModule(Module module) {
        if (state == CourseState.ARCHIVED) {
            return new ModuleResult(false, "Cannot add modules to archived course", null);
        }
        
        modules.add(module);
        module.setCourse(this);
        lastModified = System.currentTimeMillis();
        analytics.incrementModulesAdded();
        
        notifyListeners("Module added: " + module.getTitle());
        
        return new ModuleResult(true, "Module added successfully", module);
    }
    
    public ModuleResult removeModule(String moduleId) {
        Module module = findModule(moduleId);
        if (module == null) {
            return new ModuleResult(false, "Module not found", null);
        }
        
        modules.remove(module);
        lastModified = System.currentTimeMillis();
        notifyListeners("Module removed: " + module.getTitle());
        
        return new ModuleResult(true, "Module removed successfully", module);
    }
    
    // Student enrollment management
    public EnrollmentResult enrollStudent(Student student) {
        if (state != CourseState.PUBLISHED) {
            return new EnrollmentResult(false, "Course must be published for enrollment", null);
        }
        
        if (enrolledStudents.contains(student)) {
            return new EnrollmentResult(false, "Student already enrolled", null);
        }
        
        if (settings.getMaxStudents() > 0 && enrolledStudents.size() >= settings.getMaxStudents()) {
            return new EnrollmentResult(false, "Course is full", null);
        }
        
        enrolledStudents.add(student);
        StudentProgress progress = new StudentProgress(student.getStudentId(), courseId);
        studentProgress.put(student.getStudentId(), progress);
        
        analytics.incrementEnrollments();
        notifyListeners("Student enrolled: " + student.getName());
        
        return new EnrollmentResult(true, "Student enrolled successfully", progress);
    }
    
    public EnrollmentResult unenrollStudent(String studentId) {
        Student student = findStudent(studentId);
        if (student == null) {
            return new EnrollmentResult(false, "Student not found", null);
        }
        
        enrolledStudents.remove(student);
        StudentProgress progress = studentProgress.remove(studentId);
        
        analytics.incrementUnenrollments();
        notifyListeners("Student unenrolled: " + student.getName());
        
        return new EnrollmentResult(true, "Student unenrolled successfully", progress);
    }
    
    // Content delivery
    public ContentDeliveryResult deliverContent(String studentId, String moduleId) {
        Student student = findStudent(studentId);
        if (student == null) {
            return new ContentDeliveryResult(false, "Student not enrolled", null, null);
        }
        
        Module module = findModule(moduleId);
        if (module == null) {
            return new ContentDeliveryResult(false, "Module not found", null, null);
        }
        
        StudentProgress progress = studentProgress.get(studentId);
        return contentDeliveryStrategy.deliverContent(student, module, progress);
    }
    
    // Assessment management
    public AssessmentResult conductAssessment(String studentId, String assessmentId) {
        Student student = findStudent(studentId);
        if (student == null) {
            return new AssessmentResult(false, "Student not enrolled", 0, null);
        }
        
        Assessment assessment = findAssessment(assessmentId);
        if (assessment == null) {
            return new AssessmentResult(false, "Assessment not found", 0, null);
        }
        
        StudentProgress progress = studentProgress.get(studentId);
        AssessmentResult result = assessmentStrategy.conductAssessment(student, assessment, progress);
        
        if (result.isSuccess()) {
            progress.recordAssessmentResult(assessmentId, result.getScore());
            progressTrackingStrategy.updateProgress(progress, assessment);
            analytics.recordAssessmentCompletion(result.getScore());
        }
        
        return result;
    }
    
    // Progress tracking
    public ProgressResult getStudentProgress(String studentId) {
        StudentProgress progress = studentProgress.get(studentId);
        if (progress == null) {
            return new ProgressResult(false, "Student not found", null);
        }
        
        ProgressSummary summary = progressTrackingStrategy.calculateProgress(progress, modules);
        return new ProgressResult(true, "Progress retrieved successfully", summary);
    }
    
    public CourseCompletionResult checkCourseCompletion(String studentId) {
        StudentProgress progress = studentProgress.get(studentId);
        if (progress == null) {
            return new CourseCompletionResult(false, "Student not found", false, null);
        }
        
        boolean isCompleted = progressTrackingStrategy.isCourseCompleted(progress, modules);
        Certificate certificate = null;
        
        if (isCompleted && !progress.isCompleted()) {
            progress.markCompleted();
            certificate = generateCertificate(studentId);
            analytics.incrementCompletions();
            notifyListeners("Course completed by student: " + studentId);
        }
        
        return new CourseCompletionResult(true, "Completion status checked", isCompleted, certificate);
    }
    
    // Analytics and reporting
    public CourseAnalytics getAnalytics() {
        analytics.updateCurrentMetrics(enrolledStudents.size(), modules.size(), 
                                      calculateAverageProgress(), calculateCompletionRate());
        return analytics;
    }
    
    public List<StudentProgress> getProgressReport() {
        return new ArrayList<>(studentProgress.values());
    }
    
    public EngagementMetrics getEngagementMetrics() {
        return analytics.calculateEngagementMetrics(studentProgress.values());
    }
    
    // Helper methods
    private Module findModule(String moduleId) {
        return modules.stream()
                .filter(module -> module.getModuleId().equals(moduleId))
                .findFirst()
                .orElse(null);
    }
    
    private Student findStudent(String studentId) {
        return enrolledStudents.stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }
    
    private Assessment findAssessment(String assessmentId) {
        return modules.stream()
                .flatMap(module -> module.getAssessments().stream())
                .filter(assessment -> assessment.getAssessmentId().equals(assessmentId))
                .findFirst()
                .orElse(null);
    }
    
    private Certificate generateCertificate(String studentId) {
        Student student = findStudent(studentId);
        return new Certificate(UUID.randomUUID().toString(), student, this, 
                             System.currentTimeMillis(), calculateFinalGrade(studentId));
    }
    
    private double calculateAverageProgress() {
        if (studentProgress.isEmpty()) return 0.0;
        
        return studentProgress.values().stream()
                .mapToDouble(progress -> progressTrackingStrategy.calculateProgress(progress, modules).getCompletionPercentage())
                .average()
                .orElse(0.0);
    }
    
    private double calculateCompletionRate() {
        if (enrolledStudents.isEmpty()) return 0.0;
        
        long completedCount = studentProgress.values().stream()
                .mapToLong(progress -> progress.isCompleted() ? 1 : 0)
                .sum();
        
        return (double) completedCount / enrolledStudents.size() * 100.0;
    }
    
    private double calculateFinalGrade(String studentId) {
        StudentProgress progress = studentProgress.get(studentId);
        if (progress == null) return 0.0;
        
        return progress.getAssessmentResults().values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
    
    // Event handling
    public void addEventListener(CourseEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeEventListener(CourseEventListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners(String message) {
        for (CourseEventListener listener : listeners) {
            eventExecutor.submit(() -> listener.onCourseEvent(courseId, message));
        }
    }
    
    public void shutdown() {
        eventExecutor.shutdown();
        try {
            if (!eventExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                eventExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            eventExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    // Getters
    public String getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getInstructor() { return instructor; }
    public CourseCategory getCategory() { return category; }
    public CourseDifficulty getDifficulty() { return difficulty; }
    public List<Module> getModules() { return new ArrayList<>(modules); }
    public List<Student> getEnrolledStudents() { return new ArrayList<>(enrolledStudents); }
    public CourseState getState() { return state; }
    public CourseSettings getSettings() { return settings; }
    public long getCreatedAt() { return createdAt; }
    public long getLastModified() { return lastModified; }
}

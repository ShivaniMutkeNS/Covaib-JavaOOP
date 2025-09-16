package composition.elearning;

import java.util.*;

/**
 * Student class representing a learner in the e-learning system
 */
public class Student {
    private final String studentId;
    private final String name;
    private final String email;
    private final Map<String, Object> profile;
    private final List<String> enrolledCourses;
    private final Map<String, Double> courseGrades;
    private final StudentPreferences preferences;
    private final long registeredAt;
    
    public Student(String studentId, String name, String email) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.profile = new HashMap<>();
        this.enrolledCourses = new ArrayList<>();
        this.courseGrades = new HashMap<>();
        this.preferences = new StudentPreferences();
        this.registeredAt = System.currentTimeMillis();
    }
    
    public void enrollInCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
        }
    }
    
    public void unenrollFromCourse(String courseId) {
        enrolledCourses.remove(courseId);
        courseGrades.remove(courseId);
    }
    
    public void recordCourseGrade(String courseId, double grade) {
        courseGrades.put(courseId, grade);
    }
    
    public boolean isEnrolledInCourse(String courseId) {
        return enrolledCourses.contains(courseId);
    }
    
    public double getCourseGrade(String courseId) {
        return courseGrades.getOrDefault(courseId, 0.0);
    }
    
    public double getOverallGPA() {
        if (courseGrades.isEmpty()) return 0.0;
        
        return courseGrades.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
    
    // Getters
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Map<String, Object> getProfile() { return new HashMap<>(profile); }
    public List<String> getEnrolledCourses() { return new ArrayList<>(enrolledCourses); }
    public Map<String, Double> getCourseGrades() { return new HashMap<>(courseGrades); }
    public StudentPreferences getPreferences() { return preferences; }
    public long getRegisteredAt() { return registeredAt; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return Objects.equals(studentId, student.studentId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
    
    @Override
    public String toString() {
        return String.format("Student[%s]: %s (%s)", studentId, name, email);
    }
}

/**
 * Student preferences for personalized learning
 */
class StudentPreferences {
    private String preferredLanguage;
    private String learningStyle;
    private boolean emailNotifications;
    private boolean pushNotifications;
    private int dailyStudyGoalMinutes;
    private String timezone;
    
    public StudentPreferences() {
        this.preferredLanguage = "English";
        this.learningStyle = "Visual";
        this.emailNotifications = true;
        this.pushNotifications = false;
        this.dailyStudyGoalMinutes = 60;
        this.timezone = "UTC";
    }
    
    // Getters and setters
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String language) { this.preferredLanguage = language; }
    public String getLearningStyle() { return learningStyle; }
    public void setLearningStyle(String style) { this.learningStyle = style; }
    public boolean isEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(boolean enabled) { this.emailNotifications = enabled; }
    public boolean isPushNotifications() { return pushNotifications; }
    public void setPushNotifications(boolean enabled) { this.pushNotifications = enabled; }
    public int getDailyStudyGoalMinutes() { return dailyStudyGoalMinutes; }
    public void setDailyStudyGoalMinutes(int minutes) { this.dailyStudyGoalMinutes = minutes; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}

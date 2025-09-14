/**
 * Student class extending Person
 * Demonstrates hierarchy, shared vs specialized behavior
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class Student extends Person {
    private String studentId;
    private String major;
    private String year;
    private double gpa;
    private int creditsCompleted;
    private int creditsRequired;
    private String advisor;
    private String enrollmentStatus;
    private String graduationDate;
    private boolean isGraduated;
    private String[] courses;
    private int courseCount;
    
    /**
     * Constructor for Student
     * @param id Unique identifier
     * @param name Full name
     * @param email Email address
     * @param phone Phone number
     * @param address Address
     * @param department Department
     * @param studentId Student ID
     * @param major Major field of study
     * @param year Academic year
     * @param advisor Academic advisor
     */
    public Student(String id, String name, String email, String phone, String address, 
                   String department, String studentId, String major, String year, String advisor) {
        super(id, name, email, phone, address, "Student", department, 0.0); // Students don't have salary
        this.studentId = studentId;
        this.major = major;
        this.year = year;
        this.gpa = 0.0;
        this.creditsCompleted = 0;
        this.creditsRequired = 120; // Default credits required for graduation
        this.advisor = advisor;
        this.enrollmentStatus = "Enrolled";
        this.graduationDate = "";
        this.isGraduated = false;
        this.courses = new String[10]; // Maximum 10 courses
        this.courseCount = 0;
    }
    
    /**
     * Override performTask method with student-specific tasks
     * @param task Task to perform
     * @return True if task completed successfully
     */
    @Override
    public boolean performTask(String task) {
        switch (task.toLowerCase()) {
            case "take exam":
                return takeExam();
            case "submit assignment":
                return submitAssignment();
            case "attend class":
                return attendClass();
            case "meet advisor":
                return meetAdvisor();
            case "register course":
                return registerCourse();
            case "check grades":
                return checkGrades();
            default:
                System.out.println("Unknown task: " + task);
                return false;
        }
    }
    
    /**
     * Override getResponsibilities method with student responsibilities
     * @return String description of student responsibilities
     */
    @Override
    public String getResponsibilities() {
        return "Student Responsibilities: " +
               "Attend classes regularly, " +
               "Complete assignments on time, " +
               "Take exams and quizzes, " +
               "Maintain good academic standing, " +
               "Meet with academic advisor, " +
               "Follow university policies, " +
               "Participate in academic activities";
    }
    
    /**
     * Override getRoleFeatures method with student features
     * @return String description of student features
     */
    @Override
    public String getRoleFeatures() {
        return "Student Features: " +
               "Student ID: " + studentId + ", " +
               "Major: " + major + ", " +
               "Year: " + year + ", " +
               "GPA: " + String.format("%.2f", gpa) + ", " +
               "Credits: " + creditsCompleted + "/" + creditsRequired + ", " +
               "Advisor: " + advisor + ", " +
               "Status: " + enrollmentStatus + ", " +
               "Graduated: " + (isGraduated ? "Yes" : "No") + ", " +
               "Courses: " + courseCount;
    }
    
    /**
     * Student-specific method to take exam
     * @return True if exam taken successfully
     */
    public boolean takeExam() {
        if (!isActive) {
            System.out.println("Student is not active. Cannot take exam.");
            return false;
        }
        
        System.out.println("Student " + name + " is taking an exam...");
        System.out.println("Exam completed successfully!");
        return true;
    }
    
    /**
     * Student-specific method to submit assignment
     * @return True if assignment submitted successfully
     */
    public boolean submitAssignment() {
        if (!isActive) {
            System.out.println("Student is not active. Cannot submit assignment.");
            return false;
        }
        
        System.out.println("Student " + name + " is submitting an assignment...");
        System.out.println("Assignment submitted successfully!");
        return true;
    }
    
    /**
     * Student-specific method to attend class
     * @return True if class attended successfully
     */
    public boolean attendClass() {
        if (!isActive) {
            System.out.println("Student is not active. Cannot attend class.");
            return false;
        }
        
        System.out.println("Student " + name + " is attending class...");
        System.out.println("Class attended successfully!");
        return true;
    }
    
    /**
     * Student-specific method to meet advisor
     * @return True if meeting scheduled successfully
     */
    public boolean meetAdvisor() {
        if (!isActive) {
            System.out.println("Student is not active. Cannot meet advisor.");
            return false;
        }
        
        System.out.println("Student " + name + " is meeting with advisor " + advisor + "...");
        System.out.println("Meeting completed successfully!");
        return true;
    }
    
    /**
     * Student-specific method to register for course
     * @return True if course registered successfully
     */
    public boolean registerCourse() {
        if (!isActive) {
            System.out.println("Student is not active. Cannot register for course.");
            return false;
        }
        
        if (courseCount >= courses.length) {
            System.out.println("Maximum courses reached. Cannot register for more courses.");
            return false;
        }
        
        System.out.println("Student " + name + " is registering for a course...");
        System.out.println("Course registered successfully!");
        return true;
    }
    
    /**
     * Student-specific method to check grades
     * @return True if grades checked successfully
     */
    public boolean checkGrades() {
        if (!isActive) {
            System.out.println("Student is not active. Cannot check grades.");
            return false;
        }
        
        System.out.println("Student " + name + " is checking grades...");
        System.out.println("Current GPA: " + String.format("%.2f", gpa));
        System.out.println("Grades checked successfully!");
        return true;
    }
    
    /**
     * Student-specific method to add course
     * @param courseName Name of the course
     * @return True if course added successfully
     */
    public boolean addCourse(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            System.out.println("Error: Course name cannot be empty");
            return false;
        }
        
        if (courseCount >= courses.length) {
            System.out.println("Maximum courses reached. Cannot add more courses.");
            return false;
        }
        
        courses[courseCount] = courseName;
        courseCount++;
        System.out.println("Course added: " + courseName);
        return true;
    }
    
    /**
     * Student-specific method to update GPA
     * @param newGpa New GPA value
     * @return True if GPA updated successfully
     */
    public boolean updateGPA(double newGpa) {
        if (newGpa < 0.0 || newGpa > 4.0) {
            System.out.println("Error: GPA must be between 0.0 and 4.0");
            return false;
        }
        
        this.gpa = newGpa;
        System.out.println("GPA updated to: " + String.format("%.2f", newGpa));
        return true;
    }
    
    /**
     * Student-specific method to add credits
     * @param credits Number of credits to add
     * @return True if credits added successfully
     */
    public boolean addCredits(int credits) {
        if (credits <= 0) {
            System.out.println("Error: Credits must be positive");
            return false;
        }
        
        creditsCompleted += credits;
        System.out.println("Credits added: " + credits + ", Total: " + creditsCompleted);
        
        // Check if student is eligible for graduation
        if (creditsCompleted >= creditsRequired && !isGraduated) {
            isGraduated = true;
            graduationDate = java.time.LocalDate.now().toString();
            System.out.println("Congratulations! Student is now eligible for graduation!");
        }
        
        return true;
    }
    
    /**
     * Student-specific method to get academic progress
     * @return String with academic progress
     */
    public String getAcademicProgress() {
        double progressPercentage = (double) creditsCompleted / creditsRequired * 100;
        return String.format("Academic Progress: %.1f%% (%d/%d credits), GPA: %.2f, Status: %s", 
                           progressPercentage, creditsCompleted, creditsRequired, gpa, enrollmentStatus);
    }
    
    /**
     * Student-specific method to get course list
     * @return String with course list
     */
    public String getCourseList() {
        if (courseCount == 0) {
            return "No courses registered";
        }
        
        StringBuilder courseList = new StringBuilder("Courses: ");
        for (int i = 0; i < courseCount; i++) {
            courseList.append(courses[i]);
            if (i < courseCount - 1) {
                courseList.append(", ");
            }
        }
        
        return courseList.toString();
    }
    
    /**
     * Student-specific method to check graduation eligibility
     * @return True if student is eligible for graduation
     */
    public boolean isEligibleForGraduation() {
        return creditsCompleted >= creditsRequired && gpa >= 2.0;
    }
    
    /**
     * Getter for student ID
     * @return The student ID
     */
    public String getStudentId() {
        return studentId;
    }
    
    /**
     * Getter for major
     * @return The major
     */
    public String getMajor() {
        return major;
    }
    
    /**
     * Getter for year
     * @return The academic year
     */
    public String getYear() {
        return year;
    }
    
    /**
     * Getter for GPA
     * @return The GPA
     */
    public double getGpa() {
        return gpa;
    }
    
    /**
     * Getter for credits completed
     * @return The credits completed
     */
    public int getCreditsCompleted() {
        return creditsCompleted;
    }
    
    /**
     * Getter for credits required
     * @return The credits required
     */
    public int getCreditsRequired() {
        return creditsRequired;
    }
    
    /**
     * Getter for advisor
     * @return The advisor
     */
    public String getAdvisor() {
        return advisor;
    }
    
    /**
     * Getter for enrollment status
     * @return The enrollment status
     */
    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }
    
    /**
     * Getter for graduation date
     * @return The graduation date
     */
    public String getGraduationDate() {
        return graduationDate;
    }
    
    /**
     * Getter for graduated status
     * @return True if student is graduated
     */
    public boolean isGraduated() {
        return isGraduated;
    }
    
    /**
     * Getter for course count
     * @return The course count
     */
    public int getCourseCount() {
        return courseCount;
    }
    
    /**
     * Override toString to include student-specific details
     * @return String representation of the student
     */
    @Override
    public String toString() {
        return super.toString() + " [Student ID: " + studentId + ", Major: " + major + ", Year: " + year + ", GPA: " + String.format("%.2f", gpa) + "]";
    }
}

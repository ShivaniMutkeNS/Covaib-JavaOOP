/**
 * Professor class extending Person
 * Demonstrates hierarchy, shared vs specialized behavior
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class Professor extends Person {
    private String professorId;
    private String title;
    private String specialization;
    private String[] coursesTeaching;
    private int courseCount;
    private int maxCourses;
    private String researchArea;
    private String[] publications;
    private int publicationCount;
    private int maxPublications;
    private String officeLocation;
    private String officeHours;
    private String[] advisees;
    private int adviseeCount;
    private int maxAdvisees;
    private String tenureStatus;
    private boolean isTenured;
    private String[] committees;
    private int committeeCount;
    private int maxCommittees;
    
    /**
     * Constructor for Professor
     * @param id Unique identifier
     * @param name Full name
     * @param email Email address
     * @param phone Phone number
     * @param address Address
     * @param department Department
     * @param salary Salary
     * @param professorId Professor ID
     * @param title Academic title
     * @param specialization Area of specialization
     * @param researchArea Research area
     * @param officeLocation Office location
     * @param officeHours Office hours
     * @param tenureStatus Tenure status
     */
    public Professor(String id, String name, String email, String phone, String address, 
                    String department, double salary, String professorId, String title, 
                    String specialization, String researchArea, String officeLocation, 
                    String officeHours, String tenureStatus) {
        super(id, name, email, phone, address, "Professor", department, salary);
        this.professorId = professorId;
        this.title = title;
        this.specialization = specialization;
        this.coursesTeaching = new String[10]; // Maximum 10 courses
        this.courseCount = 0;
        this.maxCourses = 10;
        this.researchArea = researchArea;
        this.publications = new String[50]; // Maximum 50 publications
        this.publicationCount = 0;
        this.maxPublications = 50;
        this.officeLocation = officeLocation;
        this.officeHours = officeHours;
        this.advisees = new String[20]; // Maximum 20 advisees
        this.adviseeCount = 0;
        this.maxAdvisees = 20;
        this.tenureStatus = tenureStatus;
        this.isTenured = tenureStatus.equals("Tenured");
        this.committees = new String[10]; // Maximum 10 committees
        this.committeeCount = 0;
        this.maxCommittees = 10;
    }
    
    /**
     * Override performTask method with professor-specific tasks
     * @param task Task to perform
     * @return True if task completed successfully
     */
    @Override
    public boolean performTask(String task) {
        switch (task.toLowerCase()) {
            case "teach class":
                return teachClass();
            case "grade papers":
                return gradePapers();
            case "conduct research":
                return conductResearch();
            case "meet students":
                return meetStudents();
            case "attend meeting":
                return attendMeeting();
            case "publish paper":
                return publishPaper();
            default:
                System.out.println("Unknown task: " + task);
                return false;
        }
    }
    
    /**
     * Override getResponsibilities method with professor responsibilities
     * @return String description of professor responsibilities
     */
    @Override
    public String getResponsibilities() {
        return "Professor Responsibilities: " +
               "Teach courses and conduct lectures, " +
               "Grade assignments and exams, " +
               "Conduct research and publish papers, " +
               "Advise students and supervise thesis, " +
               "Attend department meetings, " +
               "Serve on academic committees, " +
               "Maintain office hours, " +
               "Contribute to academic community";
    }
    
    /**
     * Override getRoleFeatures method with professor features
     * @return String description of professor features
     */
    @Override
    public String getRoleFeatures() {
        return "Professor Features: " +
               "Professor ID: " + professorId + ", " +
               "Title: " + title + ", " +
               "Specialization: " + specialization + ", " +
               "Research Area: " + researchArea + ", " +
               "Office: " + officeLocation + ", " +
               "Office Hours: " + officeHours + ", " +
               "Tenure Status: " + tenureStatus + ", " +
               "Courses: " + courseCount + "/" + maxCourses + ", " +
               "Publications: " + publicationCount + "/" + maxPublications + ", " +
               "Advisees: " + adviseeCount + "/" + maxAdvisees + ", " +
               "Committees: " + committeeCount + "/" + maxCommittees;
    }
    
    /**
     * Professor-specific method to teach class
     * @return True if class taught successfully
     */
    public boolean teachClass() {
        if (!isActive) {
            System.out.println("Professor is not active. Cannot teach class.");
            return false;
        }
        
        System.out.println("Professor " + name + " is teaching a class...");
        System.out.println("Class taught successfully!");
        return true;
    }
    
    /**
     * Professor-specific method to grade papers
     * @return True if papers graded successfully
     */
    public boolean gradePapers() {
        if (!isActive) {
            System.out.println("Professor is not active. Cannot grade papers.");
            return false;
        }
        
        System.out.println("Professor " + name + " is grading papers...");
        System.out.println("Papers graded successfully!");
        return true;
    }
    
    /**
     * Professor-specific method to conduct research
     * @return True if research conducted successfully
     */
    public boolean conductResearch() {
        if (!isActive) {
            System.out.println("Professor is not active. Cannot conduct research.");
            return false;
        }
        
        System.out.println("Professor " + name + " is conducting research in " + researchArea + "...");
        System.out.println("Research conducted successfully!");
        return true;
    }
    
    /**
     * Professor-specific method to meet students
     * @return True if meeting conducted successfully
     */
    public boolean meetStudents() {
        if (!isActive) {
            System.out.println("Professor is not active. Cannot meet students.");
            return false;
        }
        
        System.out.println("Professor " + name + " is meeting with students...");
        System.out.println("Meeting conducted successfully!");
        return true;
    }
    
    /**
     * Professor-specific method to attend meeting
     * @return True if meeting attended successfully
     */
    public boolean attendMeeting() {
        if (!isActive) {
            System.out.println("Professor is not active. Cannot attend meeting.");
            return false;
        }
        
        System.out.println("Professor " + name + " is attending a meeting...");
        System.out.println("Meeting attended successfully!");
        return true;
    }
    
    /**
     * Professor-specific method to publish paper
     * @return True if paper published successfully
     */
    public boolean publishPaper() {
        if (!isActive) {
            System.out.println("Professor is not active. Cannot publish paper.");
            return false;
        }
        
        System.out.println("Professor " + name + " is publishing a paper...");
        System.out.println("Paper published successfully!");
        return true;
    }
    
    /**
     * Professor-specific method to add course
     * @param courseName Name of the course
     * @return True if course added successfully
     */
    public boolean addCourse(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            System.out.println("Error: Course name cannot be empty");
            return false;
        }
        
        if (courseCount >= maxCourses) {
            System.out.println("Maximum courses reached. Cannot add more courses.");
            return false;
        }
        
        coursesTeaching[courseCount] = courseName;
        courseCount++;
        System.out.println("Course added: " + courseName);
        return true;
    }
    
    /**
     * Professor-specific method to add publication
     * @param publicationTitle Title of the publication
     * @return True if publication added successfully
     */
    public boolean addPublication(String publicationTitle) {
        if (publicationTitle == null || publicationTitle.trim().isEmpty()) {
            System.out.println("Error: Publication title cannot be empty");
            return false;
        }
        
        if (publicationCount >= maxPublications) {
            System.out.println("Maximum publications reached. Cannot add more publications.");
            return false;
        }
        
        publications[publicationCount] = publicationTitle;
        publicationCount++;
        System.out.println("Publication added: " + publicationTitle);
        return true;
    }
    
    /**
     * Professor-specific method to add advisee
     * @param adviseeName Name of the advisee
     * @return True if advisee added successfully
     */
    public boolean addAdvisee(String adviseeName) {
        if (adviseeName == null || adviseeName.trim().isEmpty()) {
            System.out.println("Error: Advisee name cannot be empty");
            return false;
        }
        
        if (adviseeCount >= maxAdvisees) {
            System.out.println("Maximum advisees reached. Cannot add more advisees.");
            return false;
        }
        
        advisees[adviseeCount] = adviseeName;
        adviseeCount++;
        System.out.println("Advisee added: " + adviseeName);
        return true;
    }
    
    /**
     * Professor-specific method to add committee
     * @param committeeName Name of the committee
     * @return True if committee added successfully
     */
    public boolean addCommittee(String committeeName) {
        if (committeeName == null || committeeName.trim().isEmpty()) {
            System.out.println("Error: Committee name cannot be empty");
            return false;
        }
        
        if (committeeCount >= maxCommittees) {
            System.out.println("Maximum committees reached. Cannot add more committees.");
            return false;
        }
        
        committees[committeeCount] = committeeName;
        committeeCount++;
        System.out.println("Committee added: " + committeeName);
        return true;
    }
    
    /**
     * Professor-specific method to get teaching load
     * @return String with teaching load information
     */
    public String getTeachingLoad() {
        return String.format("Teaching Load: %d/%d courses, Specialization: %s, Research Area: %s", 
                           courseCount, maxCourses, specialization, researchArea);
    }
    
    /**
     * Professor-specific method to get research profile
     * @return String with research profile
     */
    public String getResearchProfile() {
        return String.format("Research Profile: %d publications, Area: %s, Tenure: %s", 
                           publicationCount, researchArea, tenureStatus);
    }
    
    /**
     * Professor-specific method to get advising load
     * @return String with advising load information
     */
    public String getAdvisingLoad() {
        return String.format("Advising Load: %d/%d advisees, Office: %s, Hours: %s", 
                           adviseeCount, maxAdvisees, officeLocation, officeHours);
    }
    
    /**
     * Professor-specific method to get committee involvement
     * @return String with committee involvement
     */
    public String getCommitteeInvolvement() {
        return String.format("Committee Involvement: %d/%d committees", committeeCount, maxCommittees);
    }
    
    /**
     * Professor-specific method to check if professor is overloaded
     * @return True if professor is overloaded
     */
    public boolean isOverloaded() {
        return courseCount >= maxCourses * 0.8 || adviseeCount >= maxAdvisees * 0.8 || 
               committeeCount >= maxCommittees * 0.8;
    }
    
    /**
     * Professor-specific method to check if professor is eligible for tenure
     * @return True if professor is eligible for tenure
     */
    public boolean isEligibleForTenure() {
        return !isTenured && getYearsOfService() >= 6 && publicationCount >= 10 && 
               courseCount >= 5 && adviseeCount >= 5;
    }
    
    /**
     * Getter for professor ID
     * @return The professor ID
     */
    public String getProfessorId() {
        return professorId;
    }
    
    /**
     * Getter for title
     * @return The academic title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Getter for specialization
     * @return The specialization
     */
    public String getSpecialization() {
        return specialization;
    }
    
    /**
     * Getter for research area
     * @return The research area
     */
    public String getResearchArea() {
        return researchArea;
    }
    
    /**
     * Getter for office location
     * @return The office location
     */
    public String getOfficeLocation() {
        return officeLocation;
    }
    
    /**
     * Getter for office hours
     * @return The office hours
     */
    public String getOfficeHours() {
        return officeHours;
    }
    
    /**
     * Getter for tenure status
     * @return The tenure status
     */
    public String getTenureStatus() {
        return tenureStatus;
    }
    
    /**
     * Getter for tenured status
     * @return True if professor is tenured
     */
    public boolean isTenured() {
        return isTenured;
    }
    
    /**
     * Getter for course count
     * @return The course count
     */
    public int getCourseCount() {
        return courseCount;
    }
    
    /**
     * Getter for publication count
     * @return The publication count
     */
    public int getPublicationCount() {
        return publicationCount;
    }
    
    /**
     * Getter for advisee count
     * @return The advisee count
     */
    public int getAdviseeCount() {
        return adviseeCount;
    }
    
    /**
     * Getter for committee count
     * @return The committee count
     */
    public int getCommitteeCount() {
        return committeeCount;
    }
    
    /**
     * Override toString to include professor-specific details
     * @return String representation of the professor
     */
    @Override
    public String toString() {
        return super.toString() + " [Professor ID: " + professorId + ", Title: " + title + ", Specialization: " + specialization + ", Tenure: " + tenureStatus + "]";
    }
}

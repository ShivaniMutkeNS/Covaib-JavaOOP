/**
 * Admin Staff class extending Person
 * Demonstrates hierarchy, shared vs specialized behavior
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class AdminStaff extends Person {
    private String staffId;
    private String position;
    private String[] responsibilities;
    private int responsibilityCount;
    private int maxResponsibilities;
    private String[] tasks;
    private int taskCount;
    private int maxTasks;
    private String supervisor;
    private String[] reports;
    private int reportCount;
    private int maxReports;
    private String workLocation;
    private String workHours;
    private String[] skills;
    private int skillCount;
    private int maxSkills;
    private String performanceRating;
    private boolean isEligibleForPromotion;
    private String[] certifications;
    private int certificationCount;
    private int maxCertifications;
    
    /**
     * Constructor for AdminStaff
     * @param id Unique identifier
     * @param name Full name
     * @param email Email address
     * @param phone Phone number
     * @param address Address
     * @param department Department
     * @param salary Salary
     * @param staffId Staff ID
     * @param position Position title
     * @param supervisor Supervisor name
     * @param workLocation Work location
     * @param workHours Work hours
     */
    public AdminStaff(String id, String name, String email, String phone, String address, 
                     String department, double salary, String staffId, String position, 
                     String supervisor, String workLocation, String workHours) {
        super(id, name, email, phone, address, "Admin Staff", department, salary);
        this.staffId = staffId;
        this.position = position;
        this.responsibilities = new String[20]; // Maximum 20 responsibilities
        this.responsibilityCount = 0;
        this.maxResponsibilities = 20;
        this.tasks = new String[50]; // Maximum 50 tasks
        this.taskCount = 0;
        this.maxTasks = 50;
        this.supervisor = supervisor;
        this.reports = new String[10]; // Maximum 10 reports
        this.reportCount = 0;
        this.maxReports = 10;
        this.workLocation = workLocation;
        this.workHours = workHours;
        this.skills = new String[15]; // Maximum 15 skills
        this.skillCount = 0;
        this.maxSkills = 15;
        this.performanceRating = "Good";
        this.isEligibleForPromotion = false;
        this.certifications = new String[10]; // Maximum 10 certifications
        this.certificationCount = 0;
        this.maxCertifications = 10;
    }
    
    /**
     * Override performTask method with admin staff-specific tasks
     * @param task Task to perform
     * @return True if task completed successfully
     */
    @Override
    public boolean performTask(String task) {
        switch (task.toLowerCase()) {
            case "manage schedule":
                return manageSchedule();
            case "process paperwork":
                return processPaperwork();
            case "coordinate events":
                return coordinateEvents();
            case "handle inquiries":
                return handleInquiries();
            case "update records":
                return updateRecords();
            case "prepare reports":
                return prepareReports();
            default:
                System.out.println("Unknown task: " + task);
                return false;
        }
    }
    
    /**
     * Override getResponsibilities method with admin staff responsibilities
     * @return String description of admin staff responsibilities
     */
    @Override
    public String getResponsibilities() {
        return "Admin Staff Responsibilities: " +
               "Manage schedules and appointments, " +
               "Process paperwork and documentation, " +
               "Coordinate events and meetings, " +
               "Handle inquiries and communications, " +
               "Update records and databases, " +
               "Prepare reports and presentations, " +
               "Support department operations, " +
               "Maintain office organization";
    }
    
    /**
     * Override getRoleFeatures method with admin staff features
     * @return String description of admin staff features
     */
    @Override
    public String getRoleFeatures() {
        return "Admin Staff Features: " +
               "Staff ID: " + staffId + ", " +
               "Position: " + position + ", " +
               "Supervisor: " + supervisor + ", " +
               "Work Location: " + workLocation + ", " +
               "Work Hours: " + workHours + ", " +
               "Performance Rating: " + performanceRating + ", " +
               "Promotion Eligible: " + (isEligibleForPromotion ? "Yes" : "No") + ", " +
               "Responsibilities: " + responsibilityCount + "/" + maxResponsibilities + ", " +
               "Tasks: " + taskCount + "/" + maxTasks + ", " +
               "Reports: " + reportCount + "/" + maxReports + ", " +
               "Skills: " + skillCount + "/" + maxSkills + ", " +
               "Certifications: " + certificationCount + "/" + maxCertifications;
    }
    
    /**
     * Admin staff-specific method to manage schedule
     * @return True if schedule managed successfully
     */
    public boolean manageSchedule() {
        if (!isActive) {
            System.out.println("Admin staff is not active. Cannot manage schedule.");
            return false;
        }
        
        System.out.println("Admin staff " + name + " is managing schedule...");
        System.out.println("Schedule managed successfully!");
        return true;
    }
    
    /**
     * Admin staff-specific method to process paperwork
     * @return True if paperwork processed successfully
     */
    public boolean processPaperwork() {
        if (!isActive) {
            System.out.println("Admin staff is not active. Cannot process paperwork.");
            return false;
        }
        
        System.out.println("Admin staff " + name + " is processing paperwork...");
        System.out.println("Paperwork processed successfully!");
        return true;
    }
    
    /**
     * Admin staff-specific method to coordinate events
     * @return True if event coordinated successfully
     */
    public boolean coordinateEvents() {
        if (!isActive) {
            System.out.println("Admin staff is not active. Cannot coordinate events.");
            return false;
        }
        
        System.out.println("Admin staff " + name + " is coordinating events...");
        System.out.println("Event coordinated successfully!");
        return true;
    }
    
    /**
     * Admin staff-specific method to handle inquiries
     * @return True if inquiry handled successfully
     */
    public boolean handleInquiries() {
        if (!isActive) {
            System.out.println("Admin staff is not active. Cannot handle inquiries.");
            return false;
        }
        
        System.out.println("Admin staff " + name + " is handling inquiries...");
        System.out.println("Inquiry handled successfully!");
        return true;
    }
    
    /**
     * Admin staff-specific method to update records
     * @return True if records updated successfully
     */
    public boolean updateRecords() {
        if (!isActive) {
            System.out.println("Admin staff is not active. Cannot update records.");
            return false;
        }
        
        System.out.println("Admin staff " + name + " is updating records...");
        System.out.println("Records updated successfully!");
        return true;
    }
    
    /**
     * Admin staff-specific method to prepare reports
     * @return True if report prepared successfully
     */
    public boolean prepareReports() {
        if (!isActive) {
            System.out.println("Admin staff is not active. Cannot prepare reports.");
            return false;
        }
        
        System.out.println("Admin staff " + name + " is preparing reports...");
        System.out.println("Report prepared successfully!");
        return true;
    }
    
    /**
     * Admin staff-specific method to add responsibility
     * @param responsibility Responsibility description
     * @return True if responsibility added successfully
     */
    public boolean addResponsibility(String responsibility) {
        if (responsibility == null || responsibility.trim().isEmpty()) {
            System.out.println("Error: Responsibility cannot be empty");
            return false;
        }
        
        if (responsibilityCount >= maxResponsibilities) {
            System.out.println("Maximum responsibilities reached. Cannot add more responsibilities.");
            return false;
        }
        
        responsibilities[responsibilityCount] = responsibility;
        responsibilityCount++;
        System.out.println("Responsibility added: " + responsibility);
        return true;
    }
    
    /**
     * Admin staff-specific method to add task
     * @param task Task description
     * @return True if task added successfully
     */
    public boolean addTask(String task) {
        if (task == null || task.trim().isEmpty()) {
            System.out.println("Error: Task cannot be empty");
            return false;
        }
        
        if (taskCount >= maxTasks) {
            System.out.println("Maximum tasks reached. Cannot add more tasks.");
            return false;
        }
        
        tasks[taskCount] = task;
        taskCount++;
        System.out.println("Task added: " + task);
        return true;
    }
    
    /**
     * Admin staff-specific method to add skill
     * @param skill Skill description
     * @return True if skill added successfully
     */
    public boolean addSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            System.out.println("Error: Skill cannot be empty");
            return false;
        }
        
        if (skillCount >= maxSkills) {
            System.out.println("Maximum skills reached. Cannot add more skills.");
            return false;
        }
        
        skills[skillCount] = skill;
        skillCount++;
        System.out.println("Skill added: " + skill);
        return true;
    }
    
    /**
     * Admin staff-specific method to add certification
     * @param certification Certification description
     * @return True if certification added successfully
     */
    public boolean addCertification(String certification) {
        if (certification == null || certification.trim().isEmpty()) {
            System.out.println("Error: Certification cannot be empty");
            return false;
        }
        
        if (certificationCount >= maxCertifications) {
            System.out.println("Maximum certifications reached. Cannot add more certifications.");
            return false;
        }
        
        certifications[certificationCount] = certification;
        certificationCount++;
        System.out.println("Certification added: " + certification);
        return true;
    }
    
    /**
     * Admin staff-specific method to update performance rating
     * @param rating New performance rating
     * @return True if rating updated successfully
     */
    public boolean updatePerformanceRating(String rating) {
        if (rating == null || rating.trim().isEmpty()) {
            System.out.println("Error: Performance rating cannot be empty");
            return false;
        }
        
        this.performanceRating = rating;
        this.isEligibleForPromotion = rating.equals("Excellent") || rating.equals("Outstanding");
        
        System.out.println("Performance rating updated to: " + rating);
        System.out.println("Promotion eligible: " + (isEligibleForPromotion ? "Yes" : "No"));
        return true;
    }
    
    /**
     * Admin staff-specific method to get workload summary
     * @return String with workload summary
     */
    public String getWorkloadSummary() {
        return String.format("Workload Summary: %d responsibilities, %d tasks, %d reports, Rating: %s", 
                           responsibilityCount, taskCount, reportCount, performanceRating);
    }
    
    /**
     * Admin staff-specific method to get skill profile
     * @return String with skill profile
     */
    public String getSkillProfile() {
        return String.format("Skill Profile: %d skills, %d certifications, Position: %s", 
                           skillCount, certificationCount, position);
    }
    
    /**
     * Admin staff-specific method to get work details
     * @return String with work details
     */
    public String getWorkDetails() {
        return String.format("Work Details: Location: %s, Hours: %s, Supervisor: %s", 
                           workLocation, workHours, supervisor);
    }
    
    /**
     * Admin staff-specific method to check if staff is overloaded
     * @return True if staff is overloaded
     */
    public boolean isOverloaded() {
        return taskCount >= maxTasks * 0.8 || responsibilityCount >= maxResponsibilities * 0.8;
    }
    
    /**
     * Admin staff-specific method to check if staff is eligible for promotion
     * @return True if staff is eligible for promotion
     */
    public boolean isEligibleForPromotion() {
        return isEligibleForPromotion && getYearsOfService() >= 3 && 
               skillCount >= 5 && certificationCount >= 2;
    }
    
    /**
     * Getter for staff ID
     * @return The staff ID
     */
    public String getStaffId() {
        return staffId;
    }
    
    /**
     * Getter for position
     * @return The position
     */
    public String getPosition() {
        return position;
    }
    
    /**
     * Getter for supervisor
     * @return The supervisor
     */
    public String getSupervisor() {
        return supervisor;
    }
    
    /**
     * Getter for work location
     * @return The work location
     */
    public String getWorkLocation() {
        return workLocation;
    }
    
    /**
     * Getter for work hours
     * @return The work hours
     */
    public String getWorkHours() {
        return workHours;
    }
    
    /**
     * Getter for performance rating
     * @return The performance rating
     */
    public String getPerformanceRating() {
        return performanceRating;
    }
    
    /**
     * Getter for promotion eligibility
     * @return True if staff is eligible for promotion
     */
    public boolean isEligibleForPromotion() {
        return isEligibleForPromotion;
    }
    
    /**
     * Getter for responsibility count
     * @return The responsibility count
     */
    public int getResponsibilityCount() {
        return responsibilityCount;
    }
    
    /**
     * Getter for task count
     * @return The task count
     */
    public int getTaskCount() {
        return taskCount;
    }
    
    /**
     * Getter for report count
     * @return The report count
     */
    public int getReportCount() {
        return reportCount;
    }
    
    /**
     * Getter for skill count
     * @return The skill count
     */
    public int getSkillCount() {
        return skillCount;
    }
    
    /**
     * Getter for certification count
     * @return The certification count
     */
    public int getCertificationCount() {
        return certificationCount;
    }
    
    /**
     * Override toString to include admin staff-specific details
     * @return String representation of the admin staff
     */
    @Override
    public String toString() {
        return super.toString() + " [Staff ID: " + staffId + ", Position: " + position + ", Rating: " + performanceRating + "]";
    }
}

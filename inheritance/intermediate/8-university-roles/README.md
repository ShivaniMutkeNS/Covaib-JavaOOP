# 🎓 University Roles System - Hierarchy & Shared vs Specialized Behavior

## Problem Statement
Create a university management system with different roles. Implement inheritance with base class `Person` and subclasses `Student`, `Professor`, and `AdminStaff`. Each role should override the abstract methods `performTask()`, `getResponsibilities()`, and `getRoleFeatures()` with their specific behaviors while sharing common functionality.

## Learning Objectives
- **Hierarchy**: Understanding inheritance hierarchies and relationships
- **Shared vs Specialized Behavior**: Common functionality in base class, specific behavior in subclasses
- **Method Overriding**: Each role has different task performance and responsibilities
- **Real-world Modeling**: University organizational structure and roles

## Class Hierarchy

```
Person (Abstract)
├── Student
├── Professor
└── AdminStaff
```

## Key Concepts Demonstrated

### 1. Hierarchy
- **Person**: Base class with common attributes and behaviors
- **Student**: Academic role with course management and academic progress
- **Professor**: Teaching role with research and advising responsibilities
- **AdminStaff**: Administrative role with operational and support tasks

### 2. Shared vs Specialized Behavior
- **Shared Behavior**: Contact management, salary updates, status tracking, years of service
- **Specialized Behavior**: Role-specific tasks, responsibilities, and features
- **Common Interface**: Same method calls produce different results based on role

### 3. Method Overriding
- Each role provides its own implementation of abstract methods
- Different task performance logic for each role
- Different responsibilities and features for each role

## Code Structure

### Person.java (Abstract Base Class)
```java
public abstract class Person {
    protected String id;
    protected String name;
    protected String email;
    protected String phone;
    protected String address;
    protected String role;
    protected String department;
    protected String joinDate;
    protected boolean isActive;
    protected double salary;
    protected String status;
    
    // Abstract methods - must be implemented by subclasses
    public abstract boolean performTask(String task);
    public abstract String getResponsibilities();
    public abstract String getRoleFeatures();
    
    // Concrete methods - shared by all roles
    public boolean updateContactInfo(String email, String phone, String address) { ... }
    public boolean setActiveStatus(boolean isActive) { ... }
    public boolean updateSalary(double newSalary) { ... }
    public int getYearsOfService() { ... }
    public boolean isEligibleForPromotion() { ... }
}
```

### Student.java (Concrete Subclass)
```java
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
    
    @Override
    public boolean performTask(String task) {
        switch (task.toLowerCase()) {
            case "take exam": return takeExam();
            case "submit assignment": return submitAssignment();
            case "attend class": return attendClass();
            case "meet advisor": return meetAdvisor();
            case "register course": return registerCourse();
            case "check grades": return checkGrades();
            default: return false;
        }
    }
    
    @Override
    public String getResponsibilities() {
        return "Student Responsibilities: Attend classes regularly, " +
               "Complete assignments on time, Take exams and quizzes, " +
               "Maintain good academic standing, Meet with academic advisor, " +
               "Follow university policies, Participate in academic activities";
    }
    
    @Override
    public String getRoleFeatures() {
        return "Student Features: Student ID: " + studentId + ", Major: " + major + 
               ", Year: " + year + ", GPA: " + String.format("%.2f", gpa) + 
               ", Credits: " + creditsCompleted + "/" + creditsRequired + 
               ", Advisor: " + advisor + ", Status: " + enrollmentStatus + 
               ", Graduated: " + (isGraduated ? "Yes" : "No") + 
               ", Courses: " + courseCount;
    }
}
```

### Professor.java (Concrete Subclass)
```java
public class Professor extends Person {
    private String professorId;
    private String title;
    private String specialization;
    private String[] coursesTeaching;
    private int courseCount;
    private String researchArea;
    private String[] publications;
    private int publicationCount;
    private String officeLocation;
    private String officeHours;
    private String[] advisees;
    private int adviseeCount;
    private String tenureStatus;
    private boolean isTenured;
    private String[] committees;
    private int committeeCount;
    
    @Override
    public boolean performTask(String task) {
        switch (task.toLowerCase()) {
            case "teach class": return teachClass();
            case "grade papers": return gradePapers();
            case "conduct research": return conductResearch();
            case "meet students": return meetStudents();
            case "attend meeting": return attendMeeting();
            case "publish paper": return publishPaper();
            default: return false;
        }
    }
    
    @Override
    public String getResponsibilities() {
        return "Professor Responsibilities: Teach courses and conduct lectures, " +
               "Grade assignments and exams, Conduct research and publish papers, " +
               "Advise students and supervise thesis, Attend department meetings, " +
               "Serve on academic committees, Maintain office hours, " +
               "Contribute to academic community";
    }
    
    @Override
    public String getRoleFeatures() {
        return "Professor Features: Professor ID: " + professorId + 
               ", Title: " + title + ", Specialization: " + specialization + 
               ", Research Area: " + researchArea + ", Office: " + officeLocation + 
               ", Office Hours: " + officeHours + ", Tenure Status: " + tenureStatus + 
               ", Courses: " + courseCount + "/" + maxCourses + 
               ", Publications: " + publicationCount + "/" + maxPublications + 
               ", Advisees: " + adviseeCount + "/" + maxAdvisees + 
               ", Committees: " + committeeCount + "/" + maxCommittees;
    }
}
```

### AdminStaff.java (Concrete Subclass)
```java
public class AdminStaff extends Person {
    private String staffId;
    private String position;
    private String[] responsibilities;
    private int responsibilityCount;
    private String[] tasks;
    private int taskCount;
    private String supervisor;
    private String[] reports;
    private int reportCount;
    private String workLocation;
    private String workHours;
    private String[] skills;
    private int skillCount;
    private String performanceRating;
    private boolean isEligibleForPromotion;
    private String[] certifications;
    private int certificationCount;
    
    @Override
    public boolean performTask(String task) {
        switch (task.toLowerCase()) {
            case "manage schedule": return manageSchedule();
            case "process paperwork": return processPaperwork();
            case "coordinate events": return coordinateEvents();
            case "handle inquiries": return handleInquiries();
            case "update records": return updateRecords();
            case "prepare reports": return prepareReports();
            default: return false;
        }
    }
    
    @Override
    public String getResponsibilities() {
        return "Admin Staff Responsibilities: Manage schedules and appointments, " +
               "Process paperwork and documentation, Coordinate events and meetings, " +
               "Handle inquiries and communications, Update records and databases, " +
               "Prepare reports and presentations, Support department operations, " +
               "Maintain office organization";
    }
    
    @Override
    public String getRoleFeatures() {
        return "Admin Staff Features: Staff ID: " + staffId + 
               ", Position: " + position + ", Supervisor: " + supervisor + 
               ", Work Location: " + workLocation + ", Work Hours: " + workHours + 
               ", Performance Rating: " + performanceRating + 
               ", Promotion Eligible: " + (isEligibleForPromotion ? "Yes" : "No") + 
               ", Responsibilities: " + responsibilityCount + "/" + maxResponsibilities + 
               ", Tasks: " + taskCount + "/" + maxTasks + 
               ", Reports: " + reportCount + "/" + maxReports + 
               ", Skills: " + skillCount + "/" + maxSkills + 
               ", Certifications: " + certificationCount + "/" + maxCertifications;
    }
}
```

## How to Run

1. Compile all Java files:
```bash
javac *.java
```

2. Run the demo:
```bash
java UniversityDemo
```

## Expected Output

```
🎓 UNIVERSITY ROLES SYSTEM 🎓
==================================================

📋 PERSONNEL INFORMATION:
--------------------------------------------------
ID: STU001, Name: Alice Johnson, Role: Student, Department: Computer Science, Status: Active, Salary: $0.00
ID: STU002, Name: Bob Wilson, Role: Student, Department: Mathematics, Status: Active, Salary: $0.00
ID: PROF001, Name: Dr. Carol Davis, Role: Professor, Department: Computer Science, Status: Active, Salary: $75000.00
ID: PROF002, Name: Dr. David Miller, Role: Professor, Department: Mathematics, Status: Active, Salary: $85000.00
ID: ADM001, Name: Eva Rodriguez, Role: Admin Staff, Department: Registrar's Office, Status: Active, Salary: $45000.00
ID: ADM002, Name: Frank Chen, Role: Admin Staff, Department: IT Department, Status: Active, Salary: $50000.00

🎯 TASK PERFORMANCE DEMONSTRATION:
--------------------------------------------------

Student - Alice Johnson:
Student Alice Johnson is taking an exam...
Exam completed successfully!

Professor - Dr. Carol Davis:
Professor Dr. Carol Davis is teaching a class...
Class taught successfully!

Admin Staff - Eva Rodriguez:
Admin staff Eva Rodriguez is managing schedule...
Schedule managed successfully!

Student - Bob Wilson:
Student Bob Wilson is submitting an assignment...
Assignment submitted successfully!

Professor - Dr. David Miller:
Professor Dr. David Miller is conducting research in Data Science...
Research conducted successfully!

Admin Staff - Frank Chen:
Admin staff Frank Chen is processing paperwork...
Paperwork processed successfully!

📝 ROLE RESPONSIBILITIES:
--------------------------------------------------

Student - Alice Johnson:
Student Responsibilities: Attend classes regularly, Complete assignments on time, Take exams and quizzes, Maintain good academic standing, Meet with academic advisor, Follow university policies, Participate in academic activities

Professor - Dr. Carol Davis:
Professor Responsibilities: Teach courses and conduct lectures, Grade assignments and exams, Conduct research and publish papers, Advise students and supervise thesis, Attend department meetings, Serve on academic committees, Maintain office hours, Contribute to academic community

Admin Staff - Eva Rodriguez:
Admin Staff Responsibilities: Manage schedules and appointments, Process paperwork and documentation, Coordinate events and meetings, Handle inquiries and communications, Update records and databases, Prepare reports and presentations, Support department operations, Maintain office organization

Student - Bob Wilson:
Student Responsibilities: Attend classes regularly, Complete assignments on time, Take exams and quizzes, Maintain good academic standing, Meet with academic advisor, Follow university policies, Participate in academic activities

Professor - Dr. David Miller:
Professor Responsibilities: Teach courses and conduct lectures, Grade assignments and exams, Conduct research and publish papers, Advise students and supervise thesis, Attend department meetings, Serve on academic committees, Maintain office hours, Contribute to academic community

Admin Staff - Frank Chen:
Admin Staff Responsibilities: Manage schedules and appointments, Process paperwork and documentation, Coordinate events and meetings, Handle inquiries and communications, Update records and databases, Prepare reports and presentations, Support department operations, Maintain office organization

🔧 ROLE FEATURES:
--------------------------------------------------

Student - Alice Johnson:
Student Features: Student ID: S12345, Major: Computer Science, Year: Junior, GPA: 0.00, Credits: 0/120, Advisor: Dr. Smith, Status: Enrolled, Graduated: No, Courses: 0

Professor - Dr. Carol Davis:
Professor Features: Professor ID: P12345, Title: Associate Professor, Specialization: Machine Learning, Research Area: Artificial Intelligence, Office: Office 101, Office Hours: Mon-Fri 10AM-12PM, Tenure Status: Tenure Track, Courses: 0/10, Publications: 0/50, Advisees: 0/20, Committees: 0/10

Admin Staff - Eva Rodriguez:
Admin Staff Features: Staff ID: A12345, Position: Administrative Coordinator, Supervisor: Dr. Johnson, Work Location: Admin Building, Work Hours: Mon-Fri 8AM-5PM, Performance Rating: Good, Promotion Eligible: No, Responsibilities: 0/20, Tasks: 0/50, Reports: 0/10, Skills: 0/15, Certifications: 0/10

Student - Bob Wilson:
Student Features: Student ID: S67890, Major: Mathematics, Year: Senior, GPA: 0.00, Credits: 0/120, Advisor: Dr. Brown, Status: Enrolled, Graduated: No, Courses: 0

Professor - Dr. David Miller:
Professor Features: Professor ID: P67890, Title: Full Professor, Specialization: Statistics, Research Area: Data Science, Office: Office 201, Office Hours: Mon-Fri 2PM-4PM, Tenure Status: Tenured, Courses: 0/10, Publications: 0/50, Advisees: 0/20, Committees: 0/10

Admin Staff - Frank Chen:
Admin Staff Features: Staff ID: A67890, Position: IT Support Specialist, Supervisor: Dr. Williams, Work Location: IT Building, Work Hours: Mon-Fri 9AM-6PM, Performance Rating: Good, Promotion Eligible: No, Responsibilities: 0/20, Tasks: 0/50, Reports: 0/10, Skills: 0/15, Certifications: 0/10

👥 ROLE-SPECIFIC BEHAVIORS:
--------------------------------------------------
Student Behaviors:
Academic Progress: 0.0% (0/120 credits), GPA: 0.00, Status: Enrolled
No courses registered
Course added: Biology 101
Course added: Chemistry 201
GPA updated to: 3.50
Credits added: 15, Total: 15
Academic Progress: 12.5% (15/120 credits), GPA: 3.50, Status: Enrolled
Courses: Biology 101, Chemistry 201
Graduation eligible: false

Professor Behaviors:
Teaching Load: 0/10 courses, Specialization: Quantum Physics, Research Area: Quantum Computing
Research Profile: 0 publications, Area: Quantum Computing, Tenure: Tenure Track
Advising Load: 0/20 advisees, Office: Office 301, Hours: Mon-Fri 1PM-3PM
Committee Involvement: 0/10 committees
Course added: Physics 101
Course added: Quantum Mechanics
Publication added: Quantum Computing Applications
Advisee added: John Doe
Committee added: Curriculum Committee
Teaching Load: 2/10 courses, Specialization: Quantum Physics, Research Area: Quantum Computing
Research Profile: 1 publications, Area: Quantum Computing, Tenure: Tenure Track
Overloaded: false
Tenure eligible: false

Admin Staff Behaviors:
Workload Summary: 0 responsibilities, 0 tasks, 0 reports, Rating: Good
Skill Profile: 0 skills, 0 certifications, Position: HR Assistant
Work Details: Location: HR Building, Hours: Mon-Fri 8AM-4PM, Supervisor: Dr. Taylor
Responsibility added: Manage employee records
Task added: Process new hire paperwork
Skill added: Database management
Certification added: HR Professional
Performance rating updated to: Excellent
Promotion eligible: Yes
Workload Summary: 1 responsibilities, 1 tasks, 0 reports, Rating: Excellent
Skill Profile: 1 skills, 1 certifications, Position: HR Assistant
Overloaded: false
Promotion eligible: true

📞 CONTACT INFORMATION UPDATES:
--------------------------------------------------
Student - Alice Johnson:
Contact information updated successfully!

Professor - Dr. Carol Davis:
Contact information updated successfully!

Admin Staff - Eva Rodriguez:
Contact information updated successfully!

Student - Bob Wilson:
Contact information updated successfully!

Professor - Dr. David Miller:
Contact information updated successfully!

Admin Staff - Frank Chen:
Contact information updated successfully!

💰 SALARY UPDATES:
--------------------------------------------------
Professor - Dr. Carol Davis:
Salary updated to: $78750.00

Professor - Dr. David Miller:
Salary updated to: $89250.00

Admin Staff - Eva Rodriguez:
Salary updated to: $47250.00

Admin Staff - Frank Chen:
Salary updated to: $52500.00

⏰ YEARS OF SERVICE:
--------------------------------------------------
Student - Alice Johnson:
Years of service: 0
Promotion eligible: false

Professor - Dr. Carol Davis:
Years of service: 0
Promotion eligible: false

Admin Staff - Eva Rodriguez:
Years of service: 0
Promotion eligible: false

Student - Bob Wilson:
Years of service: 0
Promotion eligible: false

Professor - Dr. David Miller:
Years of service: 0
Promotion eligible: false

Admin Staff - Frank Chen:
Years of service: 0
Promotion eligible: false

📊 STATUS UPDATES:
--------------------------------------------------
Student - Alice Johnson:
Status updated to: Inactive
Status updated to: Active

Professor - Dr. Carol Davis:
Status updated to: Inactive
Status updated to: Active

Admin Staff - Eva Rodriguez:
Status updated to: Inactive
Status updated to: Active

Student - Bob Wilson:
Status updated to: Inactive
Status updated to: Active

Professor - Dr. David Miller:
Status updated to: Inactive
Status updated to: Active

Admin Staff - Frank Chen:
Status updated to: Inactive
Status updated to: Active

🔄 POLYMORPHISM DEMONSTRATION:
--------------------------------------------------
Processing personnel using polymorphism:
1. Student - Alice Johnson
   Department: Computer Science
   Responsibilities: Student Responsibilities: Attend classes regularly, Complete assignments on time, Take exams and quizzes, Maintain good academic standing, Meet with academic advisor, Follow university policies, Participate in academic activities
   Features: Student Features: Student ID: S12345, Major: Computer Science, Year: Junior, GPA: 0.00, Credits: 0/120, Advisor: Dr. Smith, Status: Enrolled, Graduated: No, Courses: 0

2. Professor - Dr. Carol Davis
   Department: Computer Science
   Responsibilities: Professor Responsibilities: Teach courses and conduct lectures, Grade assignments and exams, Conduct research and publish papers, Advise students and supervise thesis, Attend department meetings, Serve on academic committees, Maintain office hours, Contribute to academic community
   Features: Professor Features: Professor ID: P12345, Title: Associate Professor, Specialization: Machine Learning, Research Area: Artificial Intelligence, Office: Office 101, Office Hours: Mon-Fri 10AM-12PM, Tenure Status: Tenure Track, Courses: 0/10, Publications: 0/50, Advisees: 0/20, Committees: 0/10

3. Admin Staff - Eva Rodriguez
   Department: Registrar's Office
   Responsibilities: Admin Staff Responsibilities: Manage schedules and appointments, Process paperwork and documentation, Coordinate events and meetings, Handle inquiries and communications, Update records and databases, Prepare reports and presentations, Support department operations, Maintain office organization
   Features: Admin Staff Features: Staff ID: A12345, Position: Administrative Coordinator, Supervisor: Dr. Johnson, Work Location: Admin Building, Work Hours: Mon-Fri 8AM-5PM, Performance Rating: Good, Promotion Eligible: No, Responsibilities: 0/20, Tasks: 0/50, Reports: 0/10, Skills: 0/15, Certifications: 0/10

4. Student - Bob Wilson
   Department: Mathematics
   Responsibilities: Student Responsibilities: Attend classes regularly, Complete assignments on time, Take exams and quizzes, Maintain good academic standing, Meet with academic advisor, Follow university policies, Participate in academic activities
   Features: Student Features: Student ID: S67890, Major: Mathematics, Year: Senior, GPA: 0.00, Credits: 0/120, Advisor: Dr. Brown, Status: Enrolled, Graduated: No, Courses: 0

5. Professor - Dr. David Miller
   Department: Mathematics
   Responsibilities: Professor Responsibilities: Teach courses and conduct lectures, Grade assignments and exams, Conduct research and publish papers, Advise students and supervise thesis, Attend department meetings, Serve on academic committees, Maintain office hours, Contribute to academic community
   Features: Professor Features: Professor ID: P67890, Title: Full Professor, Specialization: Statistics, Research Area: Data Science, Office: Office 201, Office Hours: Mon-Fri 2PM-4PM, Tenure Status: Tenured, Courses: 0/10, Publications: 0/50, Advisees: 0/20, Committees: 0/10

6. Admin Staff - Frank Chen
   Department: IT Department
   Responsibilities: Admin Staff Responsibilities: Manage schedules and appointments, Process paperwork and documentation, Coordinate events and meetings, Handle inquiries and communications, Update records and databases, Prepare reports and presentations, Support department operations, Maintain office organization
   Features: Admin Staff Features: Staff ID: A67890, Position: IT Support Specialist, Supervisor: Dr. Williams, Work Location: IT Building, Work Hours: Mon-Fri 9AM-6PM, Performance Rating: Good, Promotion Eligible: No, Responsibilities: 0/20, Tasks: 0/50, Reports: 0/10, Skills: 0/15, Certifications: 0/10

🏗️ HIERARCHY BENEFITS:
--------------------------------------------------
Shared Behavior (Inherited from Person):
- Contact information management
- Salary and status updates
- Years of service calculation
- Promotion eligibility checking

Specialized Behavior (Role-specific):
- Students: Academic progress, course management, graduation eligibility
- Professors: Teaching load, research profile, tenure eligibility
- Admin Staff: Workload management, skill development, performance tracking

Polymorphic Behavior:
- Same method call (performTask) produces different results based on role
- Same method call (getResponsibilities) returns role-specific information
- Same method call (getRoleFeatures) shows role-specific features

✨ DEMONSTRATION COMPLETE! ✨
```

## Key Learning Points

1. **Hierarchy**: Understanding inheritance hierarchies and relationships
2. **Shared vs Specialized Behavior**: Common functionality in base class, specific behavior in subclasses
3. **Method Overriding**: Each role provides its own implementation
4. **Real-world Modeling**: University organizational structure and roles
5. **Polymorphism**: Same method call produces different results based on object type
6. **Code Reuse**: Shared functionality reduces code duplication

## Advanced Concepts Demonstrated

- **Runtime Polymorphism**: Method calls resolved at runtime based on actual object type
- **Abstract Class Design**: Cannot instantiate abstract classes directly
- **Method Overriding vs Overloading**: Different concepts with different purposes
- **Access Modifiers**: Protected fields accessible to subclasses
- **String Formatting**: Professional output formatting
- **Real-world Modeling**: University organizational structure and roles
- **Code Reuse**: Shared functionality reduces code duplication

## Design Patterns Used

1. **Template Method Pattern**: Abstract base class defines the structure, subclasses implement specific details
2. **Strategy Pattern**: Different task performance strategies for different roles
3. **Polymorphism**: Single interface for different role types

This example demonstrates the fundamental concepts of inheritance and method overriding in Java, providing a solid foundation for understanding more complex object-oriented programming patterns in real-world applications.

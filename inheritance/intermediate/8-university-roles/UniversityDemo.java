/**
 * Demo class to showcase university roles system
 * Demonstrates hierarchy, shared vs specialized behavior
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class UniversityDemo {
    public static void main(String[] args) {
        System.out.println("🎓 UNIVERSITY ROLES SYSTEM 🎓");
        System.out.println("=" .repeat(50));
        
        // Create different types of university personnel
        Person[] personnel = {
            new Student("STU001", "Alice Johnson", "alice@university.edu", "+1-555-0123", "123 Student St, Campus", "Computer Science", "S12345", "Computer Science", "Junior", "Dr. Smith"),
            new Student("STU002", "Bob Wilson", "bob@university.edu", "+1-555-0456", "456 Dorm Ave, Campus", "Mathematics", "S67890", "Mathematics", "Senior", "Dr. Brown"),
            new Professor("PROF001", "Dr. Carol Davis", "carol@university.edu", "+1-555-0789", "789 Faculty Dr, Campus", "Computer Science", 75000.0, "P12345", "Associate Professor", "Machine Learning", "Artificial Intelligence", "Office 101", "Mon-Fri 10AM-12PM", "Tenure Track"),
            new Professor("PROF002", "Dr. David Miller", "david@university.edu", "+1-555-0321", "321 Faculty Ln, Campus", "Mathematics", 85000.0, "P67890", "Full Professor", "Statistics", "Data Science", "Office 201", "Mon-Fri 2PM-4PM", "Tenured"),
            new AdminStaff("ADM001", "Eva Rodriguez", "eva@university.edu", "+1-555-0654", "654 Admin Blvd, Campus", "Registrar's Office", 45000.0, "A12345", "Administrative Coordinator", "Dr. Johnson", "Admin Building", "Mon-Fri 8AM-5PM"),
            new AdminStaff("ADM002", "Frank Chen", "frank@university.edu", "+1-555-0987", "987 Admin St, Campus", "IT Department", 50000.0, "A67890", "IT Support Specialist", "Dr. Williams", "IT Building", "Mon-Fri 9AM-6PM")
        };
        
        // Display personnel information
        System.out.println("\n📋 PERSONNEL INFORMATION:");
        System.out.println("-".repeat(50));
        for (Person person : personnel) {
            System.out.println(person.getPersonInfo());
        }
        
        // Demonstrate task performance
        System.out.println("\n🎯 TASK PERFORMANCE DEMONSTRATION:");
        System.out.println("-".repeat(50));
        String[] tasks = {"take exam", "teach class", "manage schedule", "submit assignment", "conduct research", "process paperwork"};
        
        for (int i = 0; i < personnel.length; i++) {
            System.out.println("\n" + personnel[i].getRole() + " - " + personnel[i].getName() + ":");
            personnel[i].performTask(tasks[i % tasks.length]);
        }
        
        // Display role responsibilities
        System.out.println("\n📝 ROLE RESPONSIBILITIES:");
        System.out.println("-".repeat(50));
        for (Person person : personnel) {
            System.out.println("\n" + person.getRole() + " - " + person.getName() + ":");
            System.out.println(person.getResponsibilities());
        }
        
        // Display role features
        System.out.println("\n🔧 ROLE FEATURES:");
        System.out.println("-".repeat(50));
        for (Person person : personnel) {
            System.out.println("\n" + person.getRole() + " - " + person.getName() + ":");
            System.out.println(person.getRoleFeatures());
        }
        
        // Demonstrate role-specific behaviors
        System.out.println("\n👥 ROLE-SPECIFIC BEHAVIORS:");
        System.out.println("-".repeat(50));
        
        // Student specific behaviors
        Student student = new Student("STU003", "Grace Lee", "grace@university.edu", "+1-555-0147", "147 Student Ave, Campus", "Biology", "S11111", "Biology", "Sophomore", "Dr. Wilson");
        System.out.println("Student Behaviors:");
        System.out.println(student.getAcademicProgress());
        System.out.println(student.getCourseList());
        student.addCourse("Biology 101");
        student.addCourse("Chemistry 201");
        student.updateGPA(3.5);
        student.addCredits(15);
        System.out.println(student.getAcademicProgress());
        System.out.println(student.getCourseList());
        System.out.println("Graduation eligible: " + student.isEligibleForGraduation());
        System.out.println();
        
        // Professor specific behaviors
        Professor professor = new Professor("PROF003", "Dr. Henry Kim", "henry@university.edu", "+1-555-0258", "258 Faculty Way, Campus", "Physics", 80000.0, "P22222", "Assistant Professor", "Quantum Physics", "Quantum Computing", "Office 301", "Mon-Fri 1PM-3PM", "Tenure Track");
        System.out.println("Professor Behaviors:");
        System.out.println(professor.getTeachingLoad());
        System.out.println(professor.getResearchProfile());
        System.out.println(professor.getAdvisingLoad());
        System.out.println(professor.getCommitteeInvolvement());
        professor.addCourse("Physics 101");
        professor.addCourse("Quantum Mechanics");
        professor.addPublication("Quantum Computing Applications");
        professor.addAdvisee("John Doe");
        professor.addCommittee("Curriculum Committee");
        System.out.println(professor.getTeachingLoad());
        System.out.println(professor.getResearchProfile());
        System.out.println("Overloaded: " + professor.isOverloaded());
        System.out.println("Tenure eligible: " + professor.isEligibleForTenure());
        System.out.println();
        
        // Admin Staff specific behaviors
        AdminStaff adminStaff = new AdminStaff("ADM003", "Ivy Patel", "ivy@university.edu", "+1-555-0369", "369 Admin Rd, Campus", "Human Resources", 42000.0, "A33333", "HR Assistant", "Dr. Taylor", "HR Building", "Mon-Fri 8AM-4PM");
        System.out.println("Admin Staff Behaviors:");
        System.out.println(adminStaff.getWorkloadSummary());
        System.out.println(adminStaff.getSkillProfile());
        System.out.println(adminStaff.getWorkDetails());
        adminStaff.addResponsibility("Manage employee records");
        adminStaff.addTask("Process new hire paperwork");
        adminStaff.addSkill("Database management");
        adminStaff.addCertification("HR Professional");
        adminStaff.updatePerformanceRating("Excellent");
        System.out.println(adminStaff.getWorkloadSummary());
        System.out.println(adminStaff.getSkillProfile());
        System.out.println("Overloaded: " + adminStaff.isOverloaded());
        System.out.println("Promotion eligible: " + adminStaff.isEligibleForPromotion());
        System.out.println();
        
        // Demonstrate contact information updates
        System.out.println("\n📞 CONTACT INFORMATION UPDATES:");
        System.out.println("-".repeat(50));
        for (Person person : personnel) {
            System.out.println(person.getRole() + " - " + person.getName() + ":");
            person.updateContactInfo("newemail@university.edu", "+1-555-9999", "New Address, Campus");
            System.out.println();
        }
        
        // Demonstrate salary updates
        System.out.println("\n💰 SALARY UPDATES:");
        System.out.println("-".repeat(50));
        for (Person person : personnel) {
            if (person.getSalary() > 0) { // Only update salary for non-students
                System.out.println(person.getRole() + " - " + person.getName() + ":");
                person.updateSalary(person.getSalary() * 1.05); // 5% raise
                System.out.println();
            }
        }
        
        // Demonstrate years of service
        System.out.println("\n⏰ YEARS OF SERVICE:");
        System.out.println("-".repeat(50));
        for (Person person : personnel) {
            System.out.println(person.getRole() + " - " + person.getName() + ":");
            System.out.println("Years of service: " + person.getYearsOfService());
            System.out.println("Promotion eligible: " + person.isEligibleForPromotion());
            System.out.println();
        }
        
        // Demonstrate status updates
        System.out.println("\n📊 STATUS UPDATES:");
        System.out.println("-".repeat(50));
        for (Person person : personnel) {
            System.out.println(person.getRole() + " - " + person.getName() + ":");
            person.setActiveStatus(false);
            person.setActiveStatus(true);
            System.out.println();
        }
        
        // Demonstrate polymorphism
        System.out.println("\n🔄 POLYMORPHISM DEMONSTRATION:");
        System.out.println("-".repeat(50));
        demonstratePolymorphism(personnel);
        
        // Demonstrate hierarchy benefits
        System.out.println("\n🏗️ HIERARCHY BENEFITS:");
        System.out.println("-".repeat(50));
        System.out.println("Shared Behavior (Inherited from Person):");
        System.out.println("- Contact information management");
        System.out.println("- Salary and status updates");
        System.out.println("- Years of service calculation");
        System.out.println("- Promotion eligibility checking");
        System.out.println();
        System.out.println("Specialized Behavior (Role-specific):");
        System.out.println("- Students: Academic progress, course management, graduation eligibility");
        System.out.println("- Professors: Teaching load, research profile, tenure eligibility");
        System.out.println("- Admin Staff: Workload management, skill development, performance tracking");
        System.out.println();
        System.out.println("Polymorphic Behavior:");
        System.out.println("- Same method call (performTask) produces different results based on role");
        System.out.println("- Same method call (getResponsibilities) returns role-specific information");
        System.out.println("- Same method call (getRoleFeatures) shows role-specific features");
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
    
    /**
     * Demonstrates runtime polymorphism
     * @param personnel Array of Person objects
     */
    public static void demonstratePolymorphism(Person[] personnel) {
        System.out.println("Processing personnel using polymorphism:");
        for (int i = 0; i < personnel.length; i++) {
            Person person = personnel[i];
            System.out.println((i + 1) + ". " + person.getRole() + " - " + person.getName());
            System.out.println("   Department: " + person.getDepartment());
            System.out.println("   Responsibilities: " + person.getResponsibilities());
            System.out.println("   Features: " + person.getRoleFeatures());
            System.out.println();
        }
    }
}

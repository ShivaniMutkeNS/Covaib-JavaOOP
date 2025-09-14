/**
 * Abstract base class for all university roles
 * Demonstrates hierarchy, shared vs specialized behavior
 * 
 * @author Expert Developer
 * @version 1.0
 */
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
    
    /**
     * Constructor for Person
     * @param id Unique identifier
     * @param name Full name
     * @param email Email address
     * @param phone Phone number
     * @param address Address
     * @param role Role in university
     * @param department Department
     * @param salary Salary
     */
    public Person(String id, String name, String email, String phone, String address, 
                  String role, String department, double salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.department = department;
        this.salary = salary;
        this.joinDate = java.time.LocalDate.now().toString();
        this.isActive = true;
        this.status = "Active";
    }
    
    /**
     * Abstract method to perform role-specific task
     * Each role has different tasks
     * @param task Task to perform
     * @return True if task completed successfully
     */
    public abstract boolean performTask(String task);
    
    /**
     * Abstract method to get role responsibilities
     * Each role has different responsibilities
     * @return String description of responsibilities
     */
    public abstract String getResponsibilities();
    
    /**
     * Abstract method to get role features
     * Each role has different features
     * @return String description of features
     */
    public abstract String getRoleFeatures();
    
    /**
     * Concrete method to get person information
     * @return String with person details
     */
    public String getPersonInfo() {
        return String.format("ID: %s, Name: %s, Role: %s, Department: %s, Status: %s, Salary: $%.2f", 
                           id, name, role, department, status, salary);
    }
    
    /**
     * Concrete method to update contact information
     * @param email New email address
     * @param phone New phone number
     * @param address New address
     * @return True if information updated successfully
     */
    public boolean updateContactInfo(String email, String phone, String address) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Error: Email cannot be empty");
            return false;
        }
        
        if (phone == null || phone.trim().isEmpty()) {
            System.out.println("Error: Phone cannot be empty");
            return false;
        }
        
        if (address == null || address.trim().isEmpty()) {
            System.out.println("Error: Address cannot be empty");
            return false;
        }
        
        this.email = email;
        this.phone = phone;
        this.address = address;
        
        System.out.println("Contact information updated successfully!");
        return true;
    }
    
    /**
     * Concrete method to activate/deactivate person
     * @param isActive Whether person is active
     * @return True if status updated successfully
     */
    public boolean setActiveStatus(boolean isActive) {
        this.isActive = isActive;
        this.status = isActive ? "Active" : "Inactive";
        
        System.out.println("Status updated to: " + status);
        return true;
    }
    
    /**
     * Concrete method to update salary
     * @param newSalary New salary amount
     * @return True if salary updated successfully
     */
    public boolean updateSalary(double newSalary) {
        if (newSalary < 0) {
            System.out.println("Error: Salary cannot be negative");
            return false;
        }
        
        this.salary = newSalary;
        System.out.println("Salary updated to: $" + String.format("%.2f", newSalary));
        return true;
    }
    
    /**
     * Concrete method to get years of service
     * @return Number of years of service
     */
    public int getYearsOfService() {
        java.time.LocalDate joinDate = java.time.LocalDate.parse(this.joinDate);
        java.time.LocalDate currentDate = java.time.LocalDate.now();
        return java.time.temporal.ChronoUnit.YEARS.between(joinDate, currentDate);
    }
    
    /**
     * Concrete method to check if person is eligible for promotion
     * @return True if person is eligible for promotion
     */
    public boolean isEligibleForPromotion() {
        return isActive && getYearsOfService() >= 2;
    }
    
    /**
     * Getter for ID
     * @return The ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Getter for name
     * @return The name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Getter for email
     * @return The email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Getter for phone
     * @return The phone
     */
    public String getPhone() {
        return phone;
    }
    
    /**
     * Getter for address
     * @return The address
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * Getter for role
     * @return The role
     */
    public String getRole() {
        return role;
    }
    
    /**
     * Getter for department
     * @return The department
     */
    public String getDepartment() {
        return department;
    }
    
    /**
     * Getter for join date
     * @return The join date
     */
    public String getJoinDate() {
        return joinDate;
    }
    
    /**
     * Getter for active status
     * @return True if person is active
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Getter for salary
     * @return The salary
     */
    public double getSalary() {
        return salary;
    }
    
    /**
     * Getter for status
     * @return The status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Override toString method
     * @return String representation of the person
     */
    @Override
    public String toString() {
        return getPersonInfo();
    }
}

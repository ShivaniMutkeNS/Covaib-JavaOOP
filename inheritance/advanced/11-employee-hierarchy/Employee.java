public abstract class Employee {
    protected String id;
    protected String name;
    protected String department;
    protected double salary;
    protected String role;
    
    public Employee(String id, String name, String department, double salary, String role) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.role = role;
    }
    
    public abstract boolean assignTask(String task);
    public abstract double calculateBonus();
    public abstract String getRoleFeatures();
    
    public String getEmployeeInfo() {
        return String.format("ID: %s, Name: %s, Role: %s, Department: %s, Salary: $%.2f", 
                           id, name, role, department, salary);
    }
}

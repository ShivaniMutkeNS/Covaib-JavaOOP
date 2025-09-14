public class Engineer extends Employee {
    private String specialization;
    private String[] projects;
    private int projectCount;
    
    public Engineer(String id, String name, String department, double salary, String specialization) {
        super(id, name, department, salary, "Engineer");
        this.specialization = specialization;
        this.projects = new String[10];
        this.projectCount = 0;
    }
    
    @Override
    public boolean assignTask(String task) {
        System.out.println("Engineer " + name + " working on: " + task);
        return true;
    }
    
    @Override
    public double calculateBonus() {
        return salary * 0.10 + (projectCount * 500);
    }
    
    @Override
    public String getRoleFeatures() {
        return "Engineer Features: Specialization: " + specialization + 
               ", Projects: " + projectCount + ", Bonus: $" + String.format("%.2f", calculateBonus());
    }
}

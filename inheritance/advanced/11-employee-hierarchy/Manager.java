public class Manager extends Employee {
    private int teamSize;
    private String[] reports;
    private int reportCount;
    
    public Manager(String id, String name, String department, double salary, int teamSize) {
        super(id, name, department, salary, "Manager");
        this.teamSize = teamSize;
        this.reports = new String[20];
        this.reportCount = 0;
    }
    
    @Override
    public boolean assignTask(String task) {
        System.out.println("Manager " + name + " assigning task: " + task);
        return true;
    }
    
    @Override
    public double calculateBonus() {
        return salary * 0.15 + (teamSize * 1000);
    }
    
    @Override
    public String getRoleFeatures() {
        return "Manager Features: Team Size: " + teamSize + ", Reports: " + reportCount + 
               ", Bonus: $" + String.format("%.2f", calculateBonus());
    }
}

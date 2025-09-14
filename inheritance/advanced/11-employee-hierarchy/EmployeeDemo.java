public class EmployeeDemo {
    public static void main(String[] args) {
        System.out.println("👥 EMPLOYEE HIERARCHY 👥");
        System.out.println("=" .repeat(50));
        
        Employee[] employees = {
            new Manager("MGR001", "Alice Johnson", "Engineering", 120000.0, 8),
            new Engineer("ENG001", "Bob Smith", "Engineering", 90000.0, "Java")
        };
        
        System.out.println("\n📋 EMPLOYEE INFORMATION:");
        for (Employee employee : employees) {
            System.out.println(employee.getEmployeeInfo());
        }
        
        System.out.println("\n🎯 TASK ASSIGNMENT:");
        for (Employee employee : employees) {
            employee.assignTask("Complete project milestone");
        }
        
        System.out.println("\n💰 BONUS CALCULATION:");
        for (Employee employee : employees) {
            System.out.println(employee.getRoleFeatures());
        }
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
}

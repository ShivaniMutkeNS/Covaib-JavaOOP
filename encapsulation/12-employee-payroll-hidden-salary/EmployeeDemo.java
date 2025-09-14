
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Demo class to demonstrate Employee Payroll with Hidden Salary
 */
public class EmployeeDemo {
    public static void main(String[] args) {
        System.out.println("=== Employee Payroll with Hidden Salary Demo ===\n");
        
        // Test different role access levels
        testRoleAccessLevels();
        
        // Test salary information access
        testSalaryInformationAccess();
        
        // Test salary updates
        testSalaryUpdates();
        
        // Test unauthorized access
        testUnauthorizedAccess();
        
        // Test employee summary for different roles
        testEmployeeSummary();
        
        // Test salary calculations
        testSalaryCalculations();
    }
    
    private static void testRoleAccessLevels() {
        System.out.println("=== Role Access Levels Test ===");
        
        Employee employee = new Employee(
            "EMP001", "John", "Doe", "Engineering", "Software Engineer", 
            LocalDate.of(2020, 1, 15), Employee.EmployeeRole.EMPLOYEE, 
            new BigDecimal("75000")
        );
        
        // Test employee access
        System.out.println("=== Employee Access ===");
        BigDecimal netSalary = employee.getNetSalary(Employee.EmployeeRole.EMPLOYEE);
        System.out.println("Net salary (employee): " + (netSalary != null ? netSalary : "ACCESS DENIED"));
        
        BigDecimal grossSalary = employee.getGrossSalary(Employee.EmployeeRole.EMPLOYEE);
        System.out.println("Gross salary (employee): " + (grossSalary != null ? grossSalary : "ACCESS DENIED"));
        
        // Test manager access
        System.out.println("\n=== Manager Access ===");
        netSalary = employee.getNetSalary(Employee.EmployeeRole.MANAGER);
        System.out.println("Net salary (manager): " + (netSalary != null ? netSalary : "ACCESS DENIED"));
        
        grossSalary = employee.getGrossSalary(Employee.EmployeeRole.MANAGER);
        System.out.println("Gross salary (manager): " + (grossSalary != null ? grossSalary : "ACCESS DENIED"));
        
        // Test HR access
        System.out.println("\n=== HR Access ===");
        netSalary = employee.getNetSalary(Employee.EmployeeRole.HR);
        System.out.println("Net salary (HR): " + (netSalary != null ? netSalary : "ACCESS DENIED"));
        
        grossSalary = employee.getGrossSalary(Employee.EmployeeRole.HR);
        System.out.println("Gross salary (HR): " + (grossSalary != null ? grossSalary : "ACCESS DENIED"));
        
        System.out.println();
    }
    
    private static void testSalaryInformationAccess() {
        System.out.println("=== Salary Information Access Test ===");
        
        Employee employee = new Employee(
            "EMP002", "Jane", "Smith", "HR", "HR Manager", 
            LocalDate.of(2019, 3, 10), Employee.EmployeeRole.HR, 
            new BigDecimal("90000")
        );
        
        // Test HR access to salary breakdown
        Employee.SalaryBreakdown breakdown = employee.getSalaryBreakdown(Employee.EmployeeRole.HR);
        if (breakdown != null) {
            System.out.println("Salary Breakdown (HR):");
            System.out.println("  Base Salary: $" + breakdown.getBaseSalary());
            System.out.println("  Allowances: $" + breakdown.getAllowances());
            System.out.println("  Bonuses: $" + breakdown.getBonuses());
            System.out.println("  Overtime Pay: $" + breakdown.getOvertimePay());
            System.out.println("  Gross Salary: $" + breakdown.getGrossSalary());
            System.out.println("  Tax Deduction: $" + breakdown.getTaxDeduction());
            System.out.println("  Insurance Deduction: $" + breakdown.getInsuranceDeduction());
            System.out.println("  Retirement Deduction: $" + breakdown.getRetirementDeduction());
            System.out.println("  Other Deductions: $" + breakdown.getOtherDeductions());
            System.out.println("  Net Salary: $" + breakdown.getNetSalary());
        } else {
            System.out.println("Salary breakdown access denied");
        }
        
        // Test HR access to deduction details
        Employee.DeductionDetails deductions = employee.getDeductionDetails(Employee.EmployeeRole.HR);
        if (deductions != null) {
            System.out.println("\nDeduction Details (HR):");
            System.out.println("  Tax Deduction: $" + deductions.getTaxDeduction());
            System.out.println("  Insurance Deduction: $" + deductions.getInsuranceDeduction());
            System.out.println("  Retirement Deduction: $" + deductions.getRetirementDeduction());
            System.out.println("  Other Deductions: $" + deductions.getOtherDeductions());
            System.out.println("  Total Deductions: $" + deductions.getTotalDeductions());
        } else {
            System.out.println("Deduction details access denied");
        }
        
        System.out.println();
    }
    
    private static void testSalaryUpdates() {
        System.out.println("=== Salary Updates Test ===");
        
        Employee employee = new Employee(
            "EMP003", "Bob", "Johnson", "Finance", "Accountant", 
            LocalDate.of(2021, 6, 1), Employee.EmployeeRole.EMPLOYEE, 
            new BigDecimal("60000")
        );
        
        // Test salary update by HR
        boolean updated = employee.updateSalary(new BigDecimal("65000"), Employee.EmployeeRole.HR);
        System.out.println("Salary updated by HR: " + (updated ? "SUCCESS" : "FAILED"));
        
        // Test salary update by manager
        updated = employee.updateSalary(new BigDecimal("70000"), Employee.EmployeeRole.MANAGER);
        System.out.println("Salary updated by manager: " + (updated ? "SUCCESS" : "FAILED"));
        
        // Test salary update by employee
        updated = employee.updateSalary(new BigDecimal("80000"), Employee.EmployeeRole.EMPLOYEE);
        System.out.println("Salary updated by employee: " + (updated ? "SUCCESS" : "FAILED"));
        
        // Test bonus addition by HR
        boolean bonusAdded = employee.addBonus(new BigDecimal("5000"), Employee.EmployeeRole.HR);
        System.out.println("Bonus added by HR: " + (bonusAdded ? "SUCCESS" : "FAILED"));
        
        // Test bonus addition by manager
        bonusAdded = employee.addBonus(new BigDecimal("3000"), Employee.EmployeeRole.MANAGER);
        System.out.println("Bonus added by manager: " + (bonusAdded ? "SUCCESS" : "FAILED"));
        
        // Test bonus addition by employee
        bonusAdded = employee.addBonus(new BigDecimal("2000"), Employee.EmployeeRole.EMPLOYEE);
        System.out.println("Bonus added by employee: " + (bonusAdded ? "SUCCESS" : "FAILED"));
        
        System.out.println();
    }
    
    private static void testUnauthorizedAccess() {
        System.out.println("=== Unauthorized Access Test ===");
        
        Employee employee = new Employee(
            "EMP004", "Alice", "Brown", "Marketing", "Marketing Specialist", 
            LocalDate.of(2022, 2, 14), Employee.EmployeeRole.EMPLOYEE, 
            new BigDecimal("55000")
        );
        
        // Test unauthorized salary breakdown access
        Employee.SalaryBreakdown breakdown = employee.getSalaryBreakdown(Employee.EmployeeRole.EMPLOYEE);
        System.out.println("Salary breakdown (employee): " + (breakdown != null ? "ACCESS GRANTED" : "ACCESS DENIED"));
        
        breakdown = employee.getSalaryBreakdown(Employee.EmployeeRole.MANAGER);
        System.out.println("Salary breakdown (manager): " + (breakdown != null ? "ACCESS GRANTED" : "ACCESS DENIED"));
        
        // Test unauthorized deduction details access
        Employee.DeductionDetails deductions = employee.getDeductionDetails(Employee.EmployeeRole.EMPLOYEE);
        System.out.println("Deduction details (employee): " + (deductions != null ? "ACCESS GRANTED" : "ACCESS DENIED"));
        
        deductions = employee.getDeductionDetails(Employee.EmployeeRole.MANAGER);
        System.out.println("Deduction details (manager): " + (deductions != null ? "ACCESS GRANTED" : "ACCESS DENIED"));
        
        // Test unauthorized gross salary access
        BigDecimal grossSalary = employee.getGrossSalary(Employee.EmployeeRole.EMPLOYEE);
        System.out.println("Gross salary (employee): " + (grossSalary != null ? "ACCESS GRANTED" : "ACCESS DENIED"));
        
        grossSalary = employee.getGrossSalary(Employee.EmployeeRole.MANAGER);
        System.out.println("Gross salary (manager): " + (grossSalary != null ? "ACCESS GRANTED" : "ACCESS DENIED"));
        
        System.out.println();
    }
    
    /**
     * Test employee summary for different roles
     */
    private static void testEmployeeSummary() {
        System.out.println("=== Employee Summary Test ===");
        
        Employee employee = new Employee(
            "EMP005", "Charlie", "Wilson", "Sales", "Sales Representative", 
            LocalDate.of(2020, 8, 20), Employee.EmployeeRole.EMPLOYEE, 
            new BigDecimal("70000")
        );
        
        // Test summary for different roles
        Employee.EmployeeRole[] roles = {
            Employee.EmployeeRole.EMPLOYEE,
            Employee.EmployeeRole.MANAGER,
            Employee.EmployeeRole.HR,
            Employee.EmployeeRole.ADMIN
        };
        
        for (Employee.EmployeeRole role : roles) {
            System.out.println("=== Summary for " + role + " ===");
            String summary = employee.getEmployeeSummary(role);
            System.out.println(summary);
        }
        
        System.out.println();
    }
    
    /**
     * Test salary calculations
     */
    private static void testSalaryCalculations() {
        System.out.println("=== Salary Calculations Test ===");
        
        Employee employee = new Employee(
            "EMP006", "David", "Lee", "Engineering", "Senior Developer", 
            LocalDate.of(2018, 11, 5), Employee.EmployeeRole.EMPLOYEE, 
            new BigDecimal("100000")
        );
        
        // Test salary breakdown
        Employee.SalaryBreakdown breakdown = employee.getSalaryBreakdown(Employee.EmployeeRole.HR);
        if (breakdown != null) {
            System.out.println("Salary Calculations:");
            System.out.println("  Base Salary: $" + breakdown.getBaseSalary());
            System.out.println("  Allowances (10%): $" + breakdown.getAllowances());
            System.out.println("  Bonuses: $" + breakdown.getBonuses());
            System.out.println("  Overtime Pay: $" + breakdown.getOvertimePay());
            System.out.println("  Gross Salary: $" + breakdown.getGrossSalary());
            System.out.println("  Tax Deduction (25%): $" + breakdown.getTaxDeduction());
            System.out.println("  Insurance Deduction (5%): $" + breakdown.getInsuranceDeduction());
            System.out.println("  Retirement Deduction (3%): $" + breakdown.getRetirementDeduction());
            System.out.println("  Other Deductions: $" + breakdown.getOtherDeductions());
            System.out.println("  Net Salary: $" + breakdown.getNetSalary());
        }
        
        System.out.println();
    }
}


import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Employee Payroll with Hidden Salary
 * 
 * This class demonstrates encapsulation by:
 * 1. Encapsulating salary details in Employee
 * 2. Exposing only calculated net salary
 * 3. HR can view gross + deductions, but managers can only see net
 * 4. Implementing role-based access control for salary information
 */
public class Employee {
    // Encapsulated employee data
    private final String employeeId;
    private final String firstName;
    private final String lastName;
    private final String department;
    private final String position;
    private final LocalDate hireDate;
    private final EmployeeRole role;
    
    // Encapsulated salary information
    private final BigDecimal grossSalary;
    private final BigDecimal baseSalary;
    private final BigDecimal allowances;
    private final BigDecimal bonuses;
    private final BigDecimal overtimePay;
    
    // Encapsulated deduction information
    private final BigDecimal taxDeduction;
    private final BigDecimal insuranceDeduction;
    private final BigDecimal retirementDeduction;
    private final BigDecimal otherDeductions;
    
    // Access control
    private final SalaryAccessController accessController;
    
    /**
     * Constructor
     */
    public Employee(String employeeId, String firstName, String lastName, 
                   String department, String position, LocalDate hireDate, 
                   EmployeeRole role, BigDecimal baseSalary) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.position = position;
        this.hireDate = hireDate;
        this.role = role;
        this.baseSalary = baseSalary;
        
        // Calculate salary components
        this.allowances = calculateAllowances(baseSalary);
        this.bonuses = BigDecimal.ZERO; // Will be set separately
        this.overtimePay = BigDecimal.ZERO; // Will be set separately
        this.grossSalary = baseSalary.add(allowances).add(bonuses).add(overtimePay);
        
        // Calculate deductions
        this.taxDeduction = calculateTaxDeduction(grossSalary);
        this.insuranceDeduction = calculateInsuranceDeduction(grossSalary);
        this.retirementDeduction = calculateRetirementDeduction(grossSalary);
        this.otherDeductions = BigDecimal.ZERO; // Will be set separately
        
        this.accessController = new SalaryAccessController();
    }
    
    /**
     * Get basic employee information (accessible to all)
     * @return Basic employee info
     */
    public BasicEmployeeInfo getBasicInfo() {
        return new BasicEmployeeInfo(employeeId, firstName, lastName, department, position, hireDate, role);
    }
    
    /**
     * Get net salary (accessible to managers and HR)
     * @param requesterRole Role of the person requesting
     * @return Net salary or null if not authorized
     */
    public BigDecimal getNetSalary(EmployeeRole requesterRole) {
        if (!accessController.canViewNetSalary(requesterRole)) {
            return null;
        }
        
        return calculateNetSalary();
    }
    
    /**
     * Get gross salary (accessible only to HR)
     * @param requesterRole Role of the person requesting
     * @return Gross salary or null if not authorized
     */
    public BigDecimal getGrossSalary(EmployeeRole requesterRole) {
        if (!accessController.canViewGrossSalary(requesterRole)) {
            return null;
        }
        
        return grossSalary;
    }
    
    /**
     * Get salary breakdown (accessible only to HR)
     * @param requesterRole Role of the person requesting
     * @return Salary breakdown or null if not authorized
     */
    public SalaryBreakdown getSalaryBreakdown(EmployeeRole requesterRole) {
        if (!accessController.canViewSalaryBreakdown(requesterRole)) {
            return null;
        }
        
        return new SalaryBreakdown(
            baseSalary, allowances, bonuses, overtimePay, grossSalary,
            taxDeduction, insuranceDeduction, retirementDeduction, otherDeductions,
            calculateNetSalary()
        );
    }
    
    /**
     * Get deduction details (accessible only to HR)
     * @param requesterRole Role of the person requesting
     * @return Deduction details or null if not authorized
     */
    public DeductionDetails getDeductionDetails(EmployeeRole requesterRole) {
        if (!accessController.canViewDeductionDetails(requesterRole)) {
            return null;
        }
        
        return new DeductionDetails(
            taxDeduction, insuranceDeduction, retirementDeduction, otherDeductions,
            calculateTotalDeductions()
        );
    }
    
    /**
     * Update salary (accessible only to HR)
     * @param newBaseSalary New base salary
     * @param requesterRole Role of the person requesting
     * @return true if salary was updated successfully
     */
    public boolean updateSalary(BigDecimal newBaseSalary, EmployeeRole requesterRole) {
        if (!accessController.canUpdateSalary(requesterRole)) {
            return false;
        }
        
        if (newBaseSalary == null || newBaseSalary.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        // In a real implementation, this would update the salary
        // For this demo, we'll just return true
        return true;
    }
    
    /**
     * Add bonus (accessible only to HR)
     * @param bonusAmount Bonus amount
     * @param requesterRole Role of the person requesting
     * @return true if bonus was added successfully
     */
    public boolean addBonus(BigDecimal bonusAmount, EmployeeRole requesterRole) {
        if (!accessController.canUpdateSalary(requesterRole)) {
            return false;
        }
        
        if (bonusAmount == null || bonusAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        // In a real implementation, this would update the bonus
        // For this demo, we'll just return true
        return true;
    }
    
    /**
     * Get employee summary based on role
     * @param requesterRole Role of the person requesting
     * @return Employee summary
     */
    public String getEmployeeSummary(EmployeeRole requesterRole) {
        StringBuilder summary = new StringBuilder();
        BasicEmployeeInfo basicInfo = getBasicInfo();
        
        summary.append("Employee ID: ").append(basicInfo.getEmployeeId()).append("\n");
        summary.append("Name: ").append(basicInfo.getFirstName()).append(" ").append(basicInfo.getLastName()).append("\n");
        summary.append("Department: ").append(basicInfo.getDepartment()).append("\n");
        summary.append("Position: ").append(basicInfo.getPosition()).append("\n");
        summary.append("Hire Date: ").append(basicInfo.getHireDate()).append("\n");
        summary.append("Role: ").append(basicInfo.getRole()).append("\n");
        
        if (accessController.canViewNetSalary(requesterRole)) {
            BigDecimal netSalary = getNetSalary(requesterRole);
            if (netSalary != null) {
                summary.append("Net Salary: $").append(netSalary).append("\n");
            }
        }
        
        if (accessController.canViewGrossSalary(requesterRole)) {
            BigDecimal grossSalary = getGrossSalary(requesterRole);
            if (grossSalary != null) {
                summary.append("Gross Salary: $").append(grossSalary).append("\n");
            }
        }
        
        return summary.toString();
    }
    
    /**
     * Calculate net salary
     * @return Net salary
     */
    private BigDecimal calculateNetSalary() {
        BigDecimal totalDeductions = calculateTotalDeductions();
        return grossSalary.subtract(totalDeductions);
    }
    
    /**
     * Calculate total deductions
     * @return Total deductions
     */
    private BigDecimal calculateTotalDeductions() {
        return taxDeduction.add(insuranceDeduction).add(retirementDeduction).add(otherDeductions);
    }
    
    /**
     * Calculate allowances based on base salary
     * @param baseSalary Base salary
     * @return Allowances
     */
    private BigDecimal calculateAllowances(BigDecimal baseSalary) {
        // 10% of base salary as allowances
        return baseSalary.multiply(new BigDecimal("0.10"));
    }
    
    /**
     * Calculate tax deduction
     * @param grossSalary Gross salary
     * @return Tax deduction
     */
    private BigDecimal calculateTaxDeduction(BigDecimal grossSalary) {
        // Simplified tax calculation
        if (grossSalary.compareTo(new BigDecimal("50000")) <= 0) {
            return grossSalary.multiply(new BigDecimal("0.15"));
        } else if (grossSalary.compareTo(new BigDecimal("100000")) <= 0) {
            return grossSalary.multiply(new BigDecimal("0.25"));
        } else {
            return grossSalary.multiply(new BigDecimal("0.35"));
        }
    }
    
    /**
     * Calculate insurance deduction
     * @param grossSalary Gross salary
     * @return Insurance deduction
     */
    private BigDecimal calculateInsuranceDeduction(BigDecimal grossSalary) {
        // 5% of gross salary for insurance
        return grossSalary.multiply(new BigDecimal("0.05"));
    }
    
    /**
     * Calculate retirement deduction
     * @param grossSalary Gross salary
     * @return Retirement deduction
     */
    private BigDecimal calculateRetirementDeduction(BigDecimal grossSalary) {
        // 3% of gross salary for retirement
        return grossSalary.multiply(new BigDecimal("0.03"));
    }
    
    /**
     * Employee roles
     */
    public enum EmployeeRole {
        EMPLOYEE,    // Can only view basic info
        MANAGER,     // Can view basic info and net salary
        HR,          // Can view all salary information
        ADMIN        // Can view and modify all information
    }
    
    /**
     * Basic employee information
     */
    public static class BasicEmployeeInfo {
        private final String employeeId;
        private final String firstName;
        private final String lastName;
        private final String department;
        private final String position;
        private final LocalDate hireDate;
        private final EmployeeRole role;
        
        public BasicEmployeeInfo(String employeeId, String firstName, String lastName, 
                               String department, String position, LocalDate hireDate, EmployeeRole role) {
            this.employeeId = employeeId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.department = department;
            this.position = position;
            this.hireDate = hireDate;
            this.role = role;
        }
        
        public String getEmployeeId() { return employeeId; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getDepartment() { return department; }
        public String getPosition() { return position; }
        public LocalDate getHireDate() { return hireDate; }
        public EmployeeRole getRole() { return role; }
    }
    
    /**
     * Salary breakdown
     */
    public static class SalaryBreakdown {
        private final BigDecimal baseSalary;
        private final BigDecimal allowances;
        private final BigDecimal bonuses;
        private final BigDecimal overtimePay;
        private final BigDecimal grossSalary;
        private final BigDecimal taxDeduction;
        private final BigDecimal insuranceDeduction;
        private final BigDecimal retirementDeduction;
        private final BigDecimal otherDeductions;
        private final BigDecimal netSalary;
        
        public SalaryBreakdown(BigDecimal baseSalary, BigDecimal allowances, BigDecimal bonuses, 
                             BigDecimal overtimePay, BigDecimal grossSalary, BigDecimal taxDeduction, 
                             BigDecimal insuranceDeduction, BigDecimal retirementDeduction, 
                             BigDecimal otherDeductions, BigDecimal netSalary) {
            this.baseSalary = baseSalary;
            this.allowances = allowances;
            this.bonuses = bonuses;
            this.overtimePay = overtimePay;
            this.grossSalary = grossSalary;
            this.taxDeduction = taxDeduction;
            this.insuranceDeduction = insuranceDeduction;
            this.retirementDeduction = retirementDeduction;
            this.otherDeductions = otherDeductions;
            this.netSalary = netSalary;
        }
        
        public BigDecimal getBaseSalary() { return baseSalary; }
        public BigDecimal getAllowances() { return allowances; }
        public BigDecimal getBonuses() { return bonuses; }
        public BigDecimal getOvertimePay() { return overtimePay; }
        public BigDecimal getGrossSalary() { return grossSalary; }
        public BigDecimal getTaxDeduction() { return taxDeduction; }
        public BigDecimal getInsuranceDeduction() { return insuranceDeduction; }
        public BigDecimal getRetirementDeduction() { return retirementDeduction; }
        public BigDecimal getOtherDeductions() { return otherDeductions; }
        public BigDecimal getNetSalary() { return netSalary; }
    }
    
    /**
     * Deduction details
     */
    public static class DeductionDetails {
        private final BigDecimal taxDeduction;
        private final BigDecimal insuranceDeduction;
        private final BigDecimal retirementDeduction;
        private final BigDecimal otherDeductions;
        private final BigDecimal totalDeductions;
        
        public DeductionDetails(BigDecimal taxDeduction, BigDecimal insuranceDeduction, 
                              BigDecimal retirementDeduction, BigDecimal otherDeductions, 
                              BigDecimal totalDeductions) {
            this.taxDeduction = taxDeduction;
            this.insuranceDeduction = insuranceDeduction;
            this.retirementDeduction = retirementDeduction;
            this.otherDeductions = otherDeductions;
            this.totalDeductions = totalDeductions;
        }
        
        public BigDecimal getTaxDeduction() { return taxDeduction; }
        public BigDecimal getInsuranceDeduction() { return insuranceDeduction; }
        public BigDecimal getRetirementDeduction() { return retirementDeduction; }
        public BigDecimal getOtherDeductions() { return otherDeductions; }
        public BigDecimal getTotalDeductions() { return totalDeductions; }
    }
    
    /**
     * Salary access controller
     */
    private static class SalaryAccessController {
        public boolean canViewNetSalary(EmployeeRole requesterRole) {
            return requesterRole == EmployeeRole.MANAGER || 
                   requesterRole == EmployeeRole.HR || 
                   requesterRole == EmployeeRole.ADMIN;
        }
        
        public boolean canViewGrossSalary(EmployeeRole requesterRole) {
            return requesterRole == EmployeeRole.HR || 
                   requesterRole == EmployeeRole.ADMIN;
        }
        
        public boolean canViewSalaryBreakdown(EmployeeRole requesterRole) {
            return requesterRole == EmployeeRole.HR || 
                   requesterRole == EmployeeRole.ADMIN;
        }
        
        public boolean canViewDeductionDetails(EmployeeRole requesterRole) {
            return requesterRole == EmployeeRole.HR || 
                   requesterRole == EmployeeRole.ADMIN;
        }
        
        public boolean canUpdateSalary(EmployeeRole requesterRole) {
            return requesterRole == EmployeeRole.HR || 
                   requesterRole == EmployeeRole.ADMIN;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Employee{id='%s', name='%s %s', dept='%s', role=%s}", 
            employeeId, firstName, lastName, department, role);
    }
}

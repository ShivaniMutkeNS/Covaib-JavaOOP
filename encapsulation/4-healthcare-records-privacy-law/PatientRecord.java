
import java.time.LocalDate;
import java.util.*;

/**
 * Healthcare Records with Privacy Law Enforcement
 * 
 * This class demonstrates encapsulation by:
 * 1. Encapsulating patient data (disease history, prescriptions)
 * 2. Enforcing role-based access control (doctors vs insurance agents)
 * 3. Preventing unauthorized access to sensitive data
 * 4. Implementing privacy law compliance
 */
public class PatientRecord {
    // Encapsulated patient data
    private final String patientId;
    private final String patientName;
    private final LocalDate dateOfBirth;
    private final String ssn;
    
    // Medical data - only accessible by doctors
    private final List<DiseaseHistory> diseaseHistory;
    private final List<Prescription> prescriptions;
    private final List<MedicalTest> medicalTests;
    
    // Billing data - accessible by insurance agents
    private final List<BillingRecord> billingRecords;
    private final InsuranceInfo insuranceInfo;
    
    // Access control
    private final AccessController accessController;
    
    /**
     * Constructor
     */
    public PatientRecord(String patientId, String patientName, LocalDate dateOfBirth, String ssn) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.dateOfBirth = dateOfBirth;
        this.ssn = ssn;
        this.diseaseHistory = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.medicalTests = new ArrayList<>();
        this.billingRecords = new ArrayList<>();
        this.insuranceInfo = new InsuranceInfo();
        this.accessController = new AccessController();
    }
    
    /**
     * Get basic patient information (accessible to all authorized users)
     */
    public BasicPatientInfo getBasicInfo(UserRole role) {
        if (!accessController.hasAccess(role, AccessLevel.BASIC)) {
            throw new SecurityException("Access denied: Insufficient permissions for basic info");
        }
        
        return new BasicPatientInfo(patientId, patientName, dateOfBirth);
    }
    
    /**
     * Get disease history (only accessible by doctors)
     */
    public List<DiseaseHistory> getDiseaseHistory(UserRole role) {
        if (!accessController.hasAccess(role, AccessLevel.MEDICAL)) {
            throw new SecurityException("Access denied: Only doctors can access disease history");
        }
        
        return Collections.unmodifiableList(diseaseHistory);
    }
    
    /**
     * Get prescriptions (only accessible by doctors)
     */
    public List<Prescription> getPrescriptions(UserRole role) {
        if (!accessController.hasAccess(role, AccessLevel.MEDICAL)) {
            throw new SecurityException("Access denied: Only doctors can access prescriptions");
        }
        
        return Collections.unmodifiableList(prescriptions);
    }
    
    /**
     * Get medical tests (only accessible by doctors)
     */
    public List<MedicalTest> getMedicalTests(UserRole role) {
        if (!accessController.hasAccess(role, AccessLevel.MEDICAL)) {
            throw new SecurityException("Access denied: Only doctors can access medical tests");
        }
        
        return Collections.unmodifiableList(medicalTests);
    }
    
    /**
     * Get billing records (accessible by insurance agents and doctors)
     */
    public List<BillingRecord> getBillingRecords(UserRole role) {
        if (!accessController.hasAccess(role, AccessLevel.BILLING)) {
            throw new SecurityException("Access denied: Insufficient permissions for billing records");
        }
        
        return Collections.unmodifiableList(billingRecords);
    }
    
    /**
     * Get insurance information (accessible by insurance agents and doctors)
     */
    public InsuranceInfo getInsuranceInfo(UserRole role) {
        if (!accessController.hasAccess(role, AccessLevel.BILLING)) {
            throw new SecurityException("Access denied: Insufficient permissions for insurance info");
        }
        
        return insuranceInfo;
    }
    
    /**
     * Get SSN (accessible by doctors and admin only)
     */
    public String getSSN(UserRole role) {
        if (!accessController.hasAccess(role, AccessLevel.MEDICAL)) {
            throw new SecurityException("Access denied: Insufficient permissions for SSN");
        }
        
        return ssn;
    }
    
    /**
     * Add disease history (only doctors can add)
     */
    public void addDiseaseHistory(DiseaseHistory disease, UserRole role) {
        if (!accessController.hasAccess(role, AccessLevel.MEDICAL)) {
            throw new SecurityException("Access denied: Only doctors can add disease history");
        }
        
        diseaseHistory.add(disease);
        accessController.logAccess(role, "ADD_DISEASE_HISTORY", disease.getDiseaseName());
    }
    
    /**
     * Add prescription (only doctors can add)
     */
    public void addPrescription(Prescription prescription, UserRole role) {
        if (!accessController.hasAccess(role, AccessLevel.MEDICAL)) {
            throw new SecurityException("Access denied: Only doctors can add prescriptions");
        }
        
        prescriptions.add(prescription);
        accessController.logAccess(role, "ADD_PRESCRIPTION", prescription.getMedicationName());
    }
    
    /**
     * Add medical test (only doctors can add)
     */
    public void addMedicalTest(MedicalTest test, UserRole role) {
        if (!accessController.hasAccess(role, AccessLevel.MEDICAL)) {
            throw new SecurityException("Access denied: Only doctors can add medical tests");
        }
        
        medicalTests.add(test);
        accessController.logAccess(role, "ADD_MEDICAL_TEST", test.getTestName());
    }
    
    /**
     * Add billing record (insurance agents and doctors can add)
     */
    public void addBillingRecord(BillingRecord record, UserRole role) {
        if (!accessController.hasAccess(role, AccessLevel.BILLING)) {
            throw new SecurityException("Access denied: Insufficient permissions for billing records");
        }
        
        billingRecords.add(record);
        accessController.logAccess(role, "ADD_BILLING_RECORD", record.getServiceName());
    }
    
    /**
     * Get access log for audit purposes
     */
    public String getAccessLog(UserRole role) {
        if (!accessController.hasAccess(role, AccessLevel.ADMIN)) {
            throw new SecurityException("Access denied: Only administrators can access audit logs");
        }
        
        return accessController.getAccessLog();
    }
    
    /**
     * Check if user has access to specific data type
     */
    public boolean hasAccess(UserRole role, DataType dataType) {
        return accessController.hasAccess(role, dataType.getAccessLevel());
    }
    
    /**
     * Get patient summary based on role
     */
    public String getPatientSummary(UserRole role) {
        StringBuilder summary = new StringBuilder();
        summary.append("Patient ID: ").append(patientId).append("\n");
        summary.append("Name: ").append(patientName).append("\n");
        summary.append("Date of Birth: ").append(dateOfBirth).append("\n");
        
        if (hasAccess(role, DataType.DISEASE_HISTORY)) {
            summary.append("Disease History: ").append(diseaseHistory.size()).append(" records\n");
            summary.append("Prescriptions: ").append(prescriptions.size()).append(" records\n");
            summary.append("Medical Tests: ").append(medicalTests.size()).append(" records\n");
        }
        
        if (hasAccess(role, DataType.BILLING_RECORDS)) {
            summary.append("Billing Records: ").append(billingRecords.size()).append(" records\n");
            summary.append("Insurance: ").append(insuranceInfo.getProviderName()).append("\n");
        }
        
        return summary.toString();
    }
    
    /**
     * User roles for access control
     */
    public enum UserRole {
        DOCTOR,           // Can access all medical and billing data
        INSURANCE_AGENT,  // Can access billing data only
        ADMIN,            // Can access all data and audit logs
        NURSE,            // Can access basic info and some medical data
        PATIENT           // Can access basic info only
    }
    
    /**
     * Access levels
     */
    public enum AccessLevel {
        BASIC,    // Basic patient information
        MEDICAL,  // Medical records, prescriptions, tests
        BILLING,  // Billing and insurance information
        ADMIN     // Administrative access including audit logs
    }
    
    /**
     * Data types for access control
     */
    public enum DataType {
        BASIC_INFO(AccessLevel.BASIC),
        DISEASE_HISTORY(AccessLevel.MEDICAL),
        PRESCRIPTIONS(AccessLevel.MEDICAL),
        MEDICAL_TESTS(AccessLevel.MEDICAL),
        BILLING_RECORDS(AccessLevel.BILLING),
        INSURANCE_INFO(AccessLevel.BILLING),
        AUDIT_LOG(AccessLevel.ADMIN);
        
        private final AccessLevel accessLevel;
        
        DataType(AccessLevel accessLevel) {
            this.accessLevel = accessLevel;
        }
        
        public AccessLevel getAccessLevel() {
            return accessLevel;
        }
    }
    
    /**
     * Access controller for role-based access control
     */
    private static class AccessController {
        private final Map<UserRole, Set<AccessLevel>> rolePermissions;
        private final List<AccessLogEntry> accessLog;
        
        public AccessController() {
            this.rolePermissions = new HashMap<>();
            this.accessLog = new ArrayList<>();
            
            // Initialize role permissions
            initializePermissions();
        }
        
        private void initializePermissions() {
            // Doctor permissions
            rolePermissions.put(UserRole.DOCTOR, EnumSet.of(
                AccessLevel.BASIC, AccessLevel.MEDICAL, AccessLevel.BILLING
            ));
            
            // Insurance agent permissions
            rolePermissions.put(UserRole.INSURANCE_AGENT, EnumSet.of(
                AccessLevel.BASIC, AccessLevel.BILLING
            ));
            
            // Admin permissions
            rolePermissions.put(UserRole.ADMIN, EnumSet.of(
                AccessLevel.BASIC, AccessLevel.MEDICAL, AccessLevel.BILLING, AccessLevel.ADMIN
            ));
            
            // Nurse permissions
            rolePermissions.put(UserRole.NURSE, EnumSet.of(
                AccessLevel.BASIC, AccessLevel.MEDICAL
            ));
            
            // Patient permissions
            rolePermissions.put(UserRole.PATIENT, EnumSet.of(
                AccessLevel.BASIC
            ));
        }
        
        public boolean hasAccess(UserRole role, AccessLevel level) {
            Set<AccessLevel> permissions = rolePermissions.get(role);
            return permissions != null && permissions.contains(level);
        }
        
        public void logAccess(UserRole role, String action, String details) {
            accessLog.add(new AccessLogEntry(role, action, details, System.currentTimeMillis()));
        }
        
        public String getAccessLog() {
            StringBuilder log = new StringBuilder();
            for (AccessLogEntry entry : accessLog) {
                log.append(entry.toString()).append("\n");
            }
            return log.toString();
        }
    }
    
    /**
     * Access log entry
     */
    private static class AccessLogEntry {
        private final UserRole role;
        private final String action;
        private final String details;
        private final long timestamp;
        
        public AccessLogEntry(UserRole role, String action, String details, long timestamp) {
            this.role = role;
            this.action = action;
            this.details = details;
            this.timestamp = timestamp;
        }
        
        @Override
        public String toString() {
            return String.format("[%d] %s: %s - %s", timestamp, role, action, details);
        }
    }
    
    /**
     * Basic patient information
     */
    public static class BasicPatientInfo {
        private final String patientId;
        private final String patientName;
        private final LocalDate dateOfBirth;
        
        public BasicPatientInfo(String patientId, String patientName, LocalDate dateOfBirth) {
            this.patientId = patientId;
            this.patientName = patientName;
            this.dateOfBirth = dateOfBirth;
        }
        
        public String getPatientId() { return patientId; }
        public String getPatientName() { return patientName; }
        public LocalDate getDateOfBirth() { return dateOfBirth; }
    }
    
    /**
     * Disease history record
     */
    public static class DiseaseHistory {
        private final String diseaseName;
        private final LocalDate diagnosisDate;
        private final String severity;
        private final String notes;
        
        public DiseaseHistory(String diseaseName, LocalDate diagnosisDate, String severity, String notes) {
            this.diseaseName = diseaseName;
            this.diagnosisDate = diagnosisDate;
            this.severity = severity;
            this.notes = notes;
        }
        
        public String getDiseaseName() { return diseaseName; }
        public LocalDate getDiagnosisDate() { return diagnosisDate; }
        public String getSeverity() { return severity; }
        public String getNotes() { return notes; }
    }
    
    /**
     * Prescription record
     */
    public static class Prescription {
        private final String medicationName;
        private final String dosage;
        private final LocalDate prescribedDate;
        private final String doctorName;
        
        public Prescription(String medicationName, String dosage, LocalDate prescribedDate, String doctorName) {
            this.medicationName = medicationName;
            this.dosage = dosage;
            this.prescribedDate = prescribedDate;
            this.doctorName = doctorName;
        }
        
        public String getMedicationName() { return medicationName; }
        public String getDosage() { return dosage; }
        public LocalDate getPrescribedDate() { return prescribedDate; }
        public String getDoctorName() { return doctorName; }
    }
    
    /**
     * Medical test record
     */
    public static class MedicalTest {
        private final String testName;
        private final LocalDate testDate;
        private final String result;
        private final String labName;
        
        public MedicalTest(String testName, LocalDate testDate, String result, String labName) {
            this.testName = testName;
            this.testDate = testDate;
            this.result = result;
            this.labName = labName;
        }
        
        public String getTestName() { return testName; }
        public LocalDate getTestDate() { return testDate; }
        public String getResult() { return result; }
        public String getLabName() { return labName; }
    }
    
    /**
     * Billing record
     */
    public static class BillingRecord {
        private final String serviceName;
        private final double amount;
        private final LocalDate serviceDate;
        private final String status;
        
        public BillingRecord(String serviceName, double amount, LocalDate serviceDate, String status) {
            this.serviceName = serviceName;
            this.amount = amount;
            this.serviceDate = serviceDate;
            this.status = status;
        }
        
        public String getServiceName() { return serviceName; }
        public double getAmount() { return amount; }
        public LocalDate getServiceDate() { return serviceDate; }
        public String getStatus() { return status; }
    }
    
    /**
     * Insurance information
     */
    public static class InsuranceInfo {
        private final String providerName;
        private final String policyNumber;
        private final LocalDate expirationDate;
        
        public InsuranceInfo() {
            this.providerName = "Default Insurance";
            this.policyNumber = "POL-123456";
            this.expirationDate = LocalDate.now().plusYears(1);
        }
        
        public String getProviderName() { return providerName; }
        public String getPolicyNumber() { return policyNumber; }
        public LocalDate getExpirationDate() { return expirationDate; }
    }
}

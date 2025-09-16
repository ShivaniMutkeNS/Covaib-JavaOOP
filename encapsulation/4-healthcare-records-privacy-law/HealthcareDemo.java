
import java.time.LocalDate;
import java.util.List;

/**
 * Demo class to demonstrate Healthcare Records with Privacy Law Enforcement
 */
public class HealthcareDemo {
    public static void main(String[] args) {
        System.out.println("=== Healthcare Records with Privacy Law Enforcement Demo ===\n");
        
        // Create patient record
        PatientRecord patient = new PatientRecord("P001", "John Doe", LocalDate.of(1980, 5, 15), "123-45-6789");
        
        // Test different user roles and their access levels
        testDoctorAccess(patient);
        testInsuranceAgentAccess(patient);
        testNurseAccess(patient);
        testPatientAccess(patient);
        testAdminAccess(patient);
        
        // Test unauthorized access attempts
        testUnauthorizedAccess(patient);
        
        // Test data addition with proper authorization
        testDataAddition(patient);
        
        // Display access logs
        displayAccessLogs(patient);
        
        // Test patient summary for different roles
        testPatientSummary(patient);
    }
    
    private static void testDoctorAccess(PatientRecord patient) {
        System.out.println("=== Doctor Access Test ===");
        
        try {
            // Doctor can access all data
            PatientRecord.BasicPatientInfo basicInfo = patient.getBasicInfo(PatientRecord.UserRole.DOCTOR);
            System.out.println("Basic Info: " + basicInfo.getPatientName() + " (ID: " + basicInfo.getPatientId() + ")");
            
            List<PatientRecord.DiseaseHistory> diseases = patient.getDiseaseHistory(PatientRecord.UserRole.DOCTOR);
            System.out.println("Disease History: " + diseases.size() + " records");
            
            List<PatientRecord.Prescription> prescriptions = patient.getPrescriptions(PatientRecord.UserRole.DOCTOR);
            System.out.println("Prescriptions: " + prescriptions.size() + " records");
            
            List<PatientRecord.MedicalTest> tests = patient.getMedicalTests(PatientRecord.UserRole.DOCTOR);
            System.out.println("Medical Tests: " + tests.size() + " records");
            
            List<PatientRecord.BillingRecord> billing = patient.getBillingRecords(PatientRecord.UserRole.DOCTOR);
            System.out.println("Billing Records: " + billing.size() + " records");
            
            PatientRecord.InsuranceInfo insurance = patient.getInsuranceInfo(PatientRecord.UserRole.DOCTOR);
            System.out.println("Insurance: " + insurance.getProviderName());
            
            System.out.println("Doctor access: SUCCESS\n");
            
        } catch (SecurityException e) {
            System.out.println("Doctor access: FAILED - " + e.getMessage() + "\n");
        }
    }
    
    private static void testInsuranceAgentAccess(PatientRecord patient) {
        System.out.println("=== Insurance Agent Access Test ===");
        
        try {
            // Insurance agent can access basic info and billing data
            PatientRecord.BasicPatientInfo basicInfo = patient.getBasicInfo(PatientRecord.UserRole.INSURANCE_AGENT);
            System.out.println("Basic Info: " + basicInfo.getPatientName() + " (ID: " + basicInfo.getPatientId() + ")");
            
            List<PatientRecord.BillingRecord> billing = patient.getBillingRecords(PatientRecord.UserRole.INSURANCE_AGENT);
            System.out.println("Billing Records: " + billing.size() + " records");
            
            PatientRecord.InsuranceInfo insurance = patient.getInsuranceInfo(PatientRecord.UserRole.INSURANCE_AGENT);
            System.out.println("Insurance: " + insurance.getProviderName());
            
            // Try to access medical data (should fail)
            try {
                patient.getDiseaseHistory(PatientRecord.UserRole.INSURANCE_AGENT);
                System.out.println("ERROR: Should not have access to disease history");
            } catch (SecurityException e) {
                System.out.println("✓ Correctly denied access to disease history: " + e.getMessage());
            }
            
            System.out.println("Insurance agent access: SUCCESS\n");
            
        } catch (SecurityException e) {
            System.out.println("Insurance agent access: FAILED - " + e.getMessage() + "\n");
        }
    }
    
    private static void testNurseAccess(PatientRecord patient) {
        System.out.println("=== Nurse Access Test ===");
        
        try {
            // Nurse can access basic info and some medical data
            PatientRecord.BasicPatientInfo basicInfo = patient.getBasicInfo(PatientRecord.UserRole.NURSE);
            System.out.println("Basic Info: " + basicInfo.getPatientName() + " (ID: " + basicInfo.getPatientId() + ")");
            
            List<PatientRecord.DiseaseHistory> diseases = patient.getDiseaseHistory(PatientRecord.UserRole.NURSE);
            System.out.println("Disease History: " + diseases.size() + " records");
            
            List<PatientRecord.Prescription> prescriptions = patient.getPrescriptions(PatientRecord.UserRole.NURSE);
            System.out.println("Prescriptions: " + prescriptions.size() + " records");
            
            // Try to access billing data (should fail)
            try {
                patient.getBillingRecords(PatientRecord.UserRole.NURSE);
                System.out.println("ERROR: Should not have access to billing records");
            } catch (SecurityException e) {
                System.out.println("✓ Correctly denied access to billing records: " + e.getMessage());
            }
            
            System.out.println("Nurse access: SUCCESS\n");
            
        } catch (SecurityException e) {
            System.out.println("Nurse access: FAILED - " + e.getMessage() + "\n");
        }
    }
    
    private static void testPatientAccess(PatientRecord patient) {
        System.out.println("=== Patient Access Test ===");
        
        try {
            // Patient can only access basic info
            PatientRecord.BasicPatientInfo basicInfo = patient.getBasicInfo(PatientRecord.UserRole.PATIENT);
            System.out.println("Basic Info: " + basicInfo.getPatientName() + " (ID: " + basicInfo.getPatientId() + ")");
            
            // Try to access medical data (should fail)
            try {
                patient.getDiseaseHistory(PatientRecord.UserRole.PATIENT);
                System.out.println("ERROR: Should not have access to disease history");
            } catch (SecurityException e) {
                System.out.println("✓ Correctly denied access to disease history: " + e.getMessage());
            }
            
            // Try to access billing data (should fail)
            try {
                patient.getBillingRecords(PatientRecord.UserRole.PATIENT);
                System.out.println("ERROR: Should not have access to billing records");
            } catch (SecurityException e) {
                System.out.println("✓ Correctly denied access to billing records: " + e.getMessage());
            }
            
            System.out.println("Patient access: SUCCESS\n");
            
        } catch (SecurityException e) {
            System.out.println("Patient access: FAILED - " + e.getMessage() + "\n");
        }
    }
    
    private static void testAdminAccess(PatientRecord patient) {
        System.out.println("=== Admin Access Test ===");
        
        try {
            // Admin can access everything including audit logs
            PatientRecord.BasicPatientInfo basicInfo = patient.getBasicInfo(PatientRecord.UserRole.ADMIN);
            System.out.println("Basic Info: " + basicInfo.getPatientName() + " (ID: " + basicInfo.getPatientId() + ")");
            
            List<PatientRecord.DiseaseHistory> diseases = patient.getDiseaseHistory(PatientRecord.UserRole.ADMIN);
            System.out.println("Disease History: " + diseases.size() + " records");
            
            List<PatientRecord.BillingRecord> billing = patient.getBillingRecords(PatientRecord.UserRole.ADMIN);
            System.out.println("Billing Records: " + billing.size() + " records");
            
            String accessLog = patient.getAccessLog(PatientRecord.UserRole.ADMIN);
            System.out.println("Access Log: " + (accessLog.isEmpty() ? "No entries" : "Available"));
            
            System.out.println("Admin access: SUCCESS\n");
            
        } catch (SecurityException e) {
            System.out.println("Admin access: FAILED - " + e.getMessage() + "\n");
        }
    }
    
    private static void testUnauthorizedAccess(PatientRecord patient) {
        System.out.println("=== Unauthorized Access Test ===");
        
        // Test access without proper role
        try {
            patient.getDiseaseHistory(PatientRecord.UserRole.INSURANCE_AGENT);
            System.out.println("ERROR: Should not have access to disease history");
        } catch (SecurityException e) {
            System.out.println("✓ Correctly denied access: " + e.getMessage());
        }
        
        // Test access to audit logs without admin role
        try {
            patient.getAccessLog(PatientRecord.UserRole.DOCTOR);
            System.out.println("ERROR: Should not have access to audit logs");
        } catch (SecurityException e) {
            System.out.println("✓ Correctly denied access to audit logs: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void testDataAddition(PatientRecord patient) {
        System.out.println("=== Data Addition Test ===");
        
        // Doctor adds medical data
        try {
            PatientRecord.DiseaseHistory disease = new PatientRecord.DiseaseHistory(
                "Hypertension", LocalDate.now(), "Moderate", "Controlled with medication"
            );
            patient.addDiseaseHistory(disease, PatientRecord.UserRole.DOCTOR);
            System.out.println("✓ Doctor added disease history");
            
            PatientRecord.Prescription prescription = new PatientRecord.Prescription(
                "Lisinopril", "10mg daily", LocalDate.now(), "Dr. Smith"
            );
            patient.addPrescription(prescription, PatientRecord.UserRole.DOCTOR);
            System.out.println("✓ Doctor added prescription");
            
        } catch (SecurityException e) {
            System.out.println("✗ Doctor data addition failed: " + e.getMessage());
        }
        
        // Insurance agent adds billing data
        try {
            PatientRecord.BillingRecord billing = new PatientRecord.BillingRecord(
                "Consultation", 150.00, LocalDate.now(), "Paid"
            );
            patient.addBillingRecord(billing, PatientRecord.UserRole.INSURANCE_AGENT);
            System.out.println("✓ Insurance agent added billing record");
            
        } catch (SecurityException e) {
            System.out.println("✗ Insurance agent data addition failed: " + e.getMessage());
        }
        
        // Try to add medical data as insurance agent (should fail)
        try {
            PatientRecord.DiseaseHistory disease = new PatientRecord.DiseaseHistory(
                "Diabetes", LocalDate.now(), "Mild", "New diagnosis"
            );
            patient.addDiseaseHistory(disease, PatientRecord.UserRole.INSURANCE_AGENT);
            System.out.println("ERROR: Should not be able to add disease history");
        } catch (SecurityException e) {
            System.out.println("✓ Correctly denied disease history addition: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void displayAccessLogs(PatientRecord patient) {
        System.out.println("=== Access Logs ===");
        
        try {
            String accessLog = patient.getAccessLog(PatientRecord.UserRole.ADMIN);
            if (accessLog.isEmpty()) {
                System.out.println("No access logs available");
            } else {
                System.out.println(accessLog);
            }
        } catch (SecurityException e) {
            System.out.println("Access denied to audit logs: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Test patient summary for different roles
     */
    private static void testPatientSummary(PatientRecord patient) {
        System.out.println("=== Patient Summary Test ===");
        
        // Test summary for different roles
        PatientRecord.UserRole[] roles = {
            PatientRecord.UserRole.DOCTOR,
            PatientRecord.UserRole.INSURANCE_AGENT,
            PatientRecord.UserRole.NURSE,
            PatientRecord.UserRole.PATIENT
        };
        
        for (PatientRecord.UserRole role : roles) {
            try {
                String summary = patient.getPatientSummary(role);
                System.out.println("Summary for " + role + ":");
                System.out.println(summary);
            } catch (Exception e) {
                System.out.println("Error getting summary for " + role + ": " + e.getMessage());
            }
        }
        
        System.out.println();
    }
}

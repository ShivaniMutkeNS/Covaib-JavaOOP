# Healthcare Records with Privacy Law Enforcement

## Problem Statement
Patient data (disease history, prescriptions) must be encapsulated. Only doctors can view sensitive data, insurance agents can only view billing details. Enforce access control using encapsulation.

## Solution Overview

### Key Features
1. **Role-Based Access Control**: Different access levels for different user types
2. **Data Encapsulation**: Sensitive data is private and protected
3. **Privacy Law Compliance**: Enforces HIPAA-like privacy regulations
4. **Audit Trail**: Complete logging of all access attempts
5. **Granular Permissions**: Fine-grained control over data access

### Encapsulation Principles Demonstrated

#### 1. Data Hiding
- Patient medical data is private and encapsulated
- No direct access to sensitive information
- All access through controlled methods

#### 2. Access Control
- Role-based permissions system
- Different access levels for different user types
- No way to bypass security checks

#### 3. Privacy Protection
- Medical data only accessible by doctors
- Billing data accessible by insurance agents
- Basic info accessible by all authorized users

#### 4. Audit Compliance
- All access attempts are logged
- Non-repudiation for compliance
- Complete audit trail maintained

## User Roles and Permissions

### Doctor
- **Access Level**: Medical + Billing + Basic
- **Can View**: All patient data including medical records
- **Can Add**: Medical data (diseases, prescriptions, tests)
- **Cannot Access**: Audit logs (unless admin)

### Insurance Agent
- **Access Level**: Billing + Basic
- **Can View**: Basic info and billing records only
- **Can Add**: Billing records
- **Cannot Access**: Medical data, audit logs

### Nurse
- **Access Level**: Medical + Basic
- **Can View**: Basic info and medical records
- **Can Add**: Medical data (limited)
- **Cannot Access**: Billing data, audit logs

### Patient
- **Access Level**: Basic only
- **Can View**: Basic patient information only
- **Cannot Access**: Medical data, billing data, audit logs

### Admin
- **Access Level**: All levels
- **Can View**: All data including audit logs
- **Can Add**: All types of data
- **Special Access**: Audit logs and system administration

## Class Structure

### PatientRecord Class
```java
public class PatientRecord {
    // Encapsulated data
    private final String patientId;
    private final String patientName;
    private final LocalDate dateOfBirth;
    private final String ssn;
    private final List<DiseaseHistory> diseaseHistory;
    private final List<Prescription> prescriptions;
    private final List<MedicalTest> medicalTests;
    private final List<BillingRecord> billingRecords;
    private final InsuranceInfo insuranceInfo;
    
    // Access control
    private final AccessController accessController;
    
    // Public methods with role-based access
    public BasicPatientInfo getBasicInfo(UserRole role)
    public List<DiseaseHistory> getDiseaseHistory(UserRole role)
    public List<Prescription> getPrescriptions(UserRole role)
    public List<MedicalTest> getMedicalTests(UserRole role)
    public List<BillingRecord> getBillingRecords(UserRole role)
    public InsuranceInfo getInsuranceInfo(UserRole role)
    public String getAccessLog(UserRole role)
}
```

## Data Types and Access Levels

### Basic Information
- **Access Level**: Basic
- **Data**: Patient ID, name, date of birth
- **Accessible By**: All authorized users

### Medical Data
- **Access Level**: Medical
- **Data**: Disease history, prescriptions, medical tests
- **Accessible By**: Doctors, nurses, admins

### Billing Data
- **Access Level**: Billing
- **Data**: Billing records, insurance information
- **Accessible By**: Insurance agents, doctors, admins

### Audit Logs
- **Access Level**: Admin
- **Data**: Access logs, system events
- **Accessible By**: Administrators only

## Usage Examples

### Doctor Access
```java
// Doctor can access all medical data
PatientRecord patient = new PatientRecord("P001", "John Doe", LocalDate.of(1980, 5, 15), "123-45-6789");

// Access medical records
List<DiseaseHistory> diseases = patient.getDiseaseHistory(UserRole.DOCTOR);
List<Prescription> prescriptions = patient.getPrescriptions(UserRole.DOCTOR);
List<MedicalTest> tests = patient.getMedicalTests(UserRole.DOCTOR);

// Add new medical data
patient.addDiseaseHistory(new DiseaseHistory("Hypertension", LocalDate.now(), "Moderate", "Controlled"), UserRole.DOCTOR);
patient.addPrescription(new Prescription("Lisinopril", "10mg daily", LocalDate.now(), "Dr. Smith"), UserRole.DOCTOR);
```

### Insurance Agent Access
```java
// Insurance agent can only access billing data
PatientRecord.BasicPatientInfo basicInfo = patient.getBasicInfo(UserRole.INSURANCE_AGENT);
List<BillingRecord> billing = patient.getBillingRecords(UserRole.INSURANCE_AGENT);
PatientRecord.InsuranceInfo insurance = patient.getInsuranceInfo(UserRole.INSURANCE_AGENT);

// Add billing records
patient.addBillingRecord(new BillingRecord("Consultation", 150.00, LocalDate.now(), "Paid"), UserRole.INSURANCE_AGENT);

// This will throw SecurityException
// patient.getDiseaseHistory(UserRole.INSURANCE_AGENT);
```

### Access Control Validation
```java
// Check if user has access to specific data type
boolean hasMedicalAccess = patient.hasAccess(UserRole.DOCTOR, DataType.MEDICAL);
boolean hasBillingAccess = patient.hasAccess(UserRole.INSURANCE_AGENT, DataType.BILLING);

// Get patient summary based on role
String summary = patient.getPatientSummary(UserRole.DOCTOR);
```

## Security Features

### 1. Role-Based Access Control
- **Granular Permissions**: Different access levels for different roles
- **No Privilege Escalation**: Users cannot access data beyond their role
- **Fail-Safe Design**: Access denied by default

### 2. Data Encapsulation
- **Private Fields**: All sensitive data is private
- **Controlled Access**: Only through authorized methods
- **Immutable Collections**: Returned collections are unmodifiable

### 3. Audit Trail
- **Complete Logging**: All access attempts are logged
- **Non-Repudiation**: Cannot deny access attempts
- **Compliance**: Meets healthcare privacy requirements

### 4. Input Validation
- **Role Validation**: All methods validate user role
- **Data Validation**: Input data is validated before processing
- **Error Handling**: Proper exception handling for security violations

## Privacy Law Compliance

### HIPAA Compliance
- **Minimum Necessary**: Users only access data they need
- **Access Controls**: Role-based access control implemented
- **Audit Logs**: Complete audit trail maintained
- **Data Protection**: Sensitive data is encapsulated and protected

### GDPR Compliance
- **Data Minimization**: Only necessary data is accessible
- **Purpose Limitation**: Data access is limited by purpose
- **Transparency**: Clear access control and logging
- **Accountability**: Complete audit trail for compliance

## Error Handling

### Security Exceptions
- **Access Denied**: When user lacks required permissions
- **Invalid Role**: When user role is not recognized
- **Data Protection**: When attempting unauthorized access

### Validation Errors
- **Invalid Data**: When input data is invalid
- **Missing Information**: When required data is missing
- **Format Errors**: When data format is incorrect

## Performance Considerations

### Access Control Overhead
- **Role Validation**: Minimal overhead for permission checks
- **Audit Logging**: Minimal overhead for logging
- **Data Retrieval**: Efficient data access patterns

### Memory Usage
- **Data Storage**: Efficient storage of patient data
- **Audit Logs**: Consider log rotation for large systems
- **Caching**: Consider caching for frequently accessed data

## Best Practices

1. **Use Role-Based Access Control** for all sensitive data
2. **Implement Complete Audit Logging** for compliance
3. **Validate All Inputs** before processing
4. **Use Immutable Collections** for returned data
5. **Handle Security Exceptions** properly
6. **Document Access Patterns** clearly
7. **Test Security Thoroughly** with different roles

## Anti-Patterns Avoided

- ❌ Exposing sensitive data directly
- ❌ Allowing privilege escalation
- ❌ Not logging access attempts
- ❌ Using mutable collections for returned data
- ❌ Not validating user roles
- ❌ Allowing data modification without authorization
- ❌ Not handling security exceptions
- ❌ Exposing internal implementation details

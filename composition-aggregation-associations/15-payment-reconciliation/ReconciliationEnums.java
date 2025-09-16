package composition.reconciliation;

/**
 * Enums for the Payment Reconciliation System
 */

public enum ReconciliationState {
    IDLE("Idle"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    ERROR("Error"),
    PAUSED("Paused");
    
    private final String displayName;
    
    ReconciliationState(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

public enum DiscrepancyType {
    AMOUNT_MISMATCH("Amount Mismatch"),
    DATE_MISMATCH("Date Mismatch"),
    CURRENCY_MISMATCH("Currency Mismatch"),
    STATUS_MISMATCH("Status Mismatch"),
    MISSING_INTERNAL("Missing Internal Record"),
    MISSING_EXTERNAL("Missing External Record"),
    DUPLICATE_RECORD("Duplicate Record"),
    INVALID_DATA("Invalid Data"),
    REFERENCE_MISMATCH("Reference Mismatch");
    
    private final String displayName;
    
    DiscrepancyType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

public enum DiscrepancySeverity {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    CRITICAL("Critical", 4);
    
    private final String displayName;
    private final int level;
    
    DiscrepancySeverity(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getLevel() {
        return level;
    }
}

public enum PaymentStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded"),
    DISPUTED("Disputed");
    
    private final String displayName;
    
    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

public enum PaymentMethod {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    BANK_TRANSFER("Bank Transfer"),
    DIGITAL_WALLET("Digital Wallet"),
    CRYPTOCURRENCY("Cryptocurrency"),
    CHECK("Check"),
    CASH("Cash"),
    OTHER("Other");
    
    private final String displayName;
    
    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

public enum RecordSource {
    INTERNAL_SYSTEM("Internal System"),
    BANK_STATEMENT("Bank Statement"),
    PAYMENT_GATEWAY("Payment Gateway"),
    THIRD_PARTY_PROCESSOR("Third Party Processor"),
    MANUAL_ENTRY("Manual Entry"),
    API_INTEGRATION("API Integration"),
    FILE_IMPORT("File Import");
    
    private final String displayName;
    
    RecordSource(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

public enum ReportType {
    SUMMARY("Summary Report"),
    DETAILED("Detailed Report"),
    DISCREPANCY("Discrepancy Report"),
    EXCEPTION("Exception Report"),
    TREND_ANALYSIS("Trend Analysis"),
    AUDIT_TRAIL("Audit Trail"),
    PERFORMANCE("Performance Report");
    
    private final String displayName;
    
    ReportType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

public enum ResolutionAction {
    AUTO_RESOLVED("Automatically Resolved"),
    MANUAL_REVIEW("Requires Manual Review"),
    SYSTEM_CORRECTION("System Correction Applied"),
    ESCALATED("Escalated to Supervisor"),
    IGNORED("Ignored - Within Tolerance"),
    PENDING_APPROVAL("Pending Approval"),
    REJECTED("Resolution Rejected");
    
    private final String displayName;
    
    ResolutionAction(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

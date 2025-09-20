/**
 * Enumeration representing different types of banking transactions
 */
public enum TransactionType {
    DEPOSIT("Deposit", true, false),
    WITHDRAWAL("Withdrawal", false, true),
    TRANSFER_IN("Transfer In", true, false),
    TRANSFER_OUT("Transfer Out", false, true),
    INTEREST_CREDIT("Interest Credit", true, false),
    FEE_DEBIT("Fee Debit", false, false),
    ATM_WITHDRAWAL("ATM Withdrawal", false, true),
    ONLINE_PURCHASE("Online Purchase", false, true),
    DIRECT_DEPOSIT("Direct Deposit", true, false),
    CHECK_DEPOSIT("Check Deposit", true, false),
    WIRE_TRANSFER_IN("Wire Transfer In", true, false),
    WIRE_TRANSFER_OUT("Wire Transfer Out", false, true),
    OVERDRAFT_FEE("Overdraft Fee", false, false),
    MAINTENANCE_FEE("Maintenance Fee", false, false);
    
    private final String displayName;
    private final boolean isCredit; // Increases balance
    private final boolean countsTowardLimit; // Counts against monthly transaction limit
    
    TransactionType(String displayName, boolean isCredit, boolean countsTowardLimit) {
        this.displayName = displayName;
        this.isCredit = isCredit;
        this.countsTowardLimit = countsTowardLimit;
    }
    
    public String getDisplayName() { return displayName; }
    public boolean isCredit() { return isCredit; }
    public boolean isDebit() { return !isCredit; }
    public boolean countsTowardLimit() { return countsTowardLimit; }
    
    @Override
    public String toString() {
        return displayName + (isCredit ? " (+)" : " (-)");
    }
}

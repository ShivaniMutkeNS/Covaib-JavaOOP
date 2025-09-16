package composition.reconciliation;

import java.math.BigDecimal;
import java.util.*;

/**
 * External record representing bank or third-party transaction data
 */
public class ExternalRecord {
    private final String referenceId;
    private final String bankTransactionId;
    private final BigDecimal amount;
    private final String currency;
    private final String description;
    private final long settlementDate;
    private final String accountNumber;
    private final String counterpartyName;
    private final String counterpartyAccount;
    private final Map<String, Object> additionalFields;
    private final RecordSource source;
    private final long importedAt;
    
    public ExternalRecord(String referenceId, String bankTransactionId, BigDecimal amount,
                         String currency, String description, long settlementDate,
                         String accountNumber, String counterpartyName, RecordSource source) {
        this.referenceId = referenceId;
        this.bankTransactionId = bankTransactionId;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.settlementDate = settlementDate;
        this.accountNumber = accountNumber;
        this.counterpartyName = counterpartyName;
        this.counterpartyAccount = "";
        this.additionalFields = new HashMap<>();
        this.source = source;
        this.importedAt = System.currentTimeMillis();
    }
    
    public void addAdditionalField(String key, Object value) {
        additionalFields.put(key, value);
    }
    
    public Object getAdditionalField(String key) {
        return additionalFields.get(key);
    }
    
    // Getters
    public String getReferenceId() { return referenceId; }
    public String getBankTransactionId() { return bankTransactionId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getDescription() { return description; }
    public long getSettlementDate() { return settlementDate; }
    public String getAccountNumber() { return accountNumber; }
    public String getCounterpartyName() { return counterpartyName; }
    public String getCounterpartyAccount() { return counterpartyAccount; }
    public Map<String, Object> getAdditionalFields() { return new HashMap<>(additionalFields); }
    public RecordSource getSource() { return source; }
    public long getImportedAt() { return importedAt; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ExternalRecord that = (ExternalRecord) obj;
        return Objects.equals(referenceId, that.referenceId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(referenceId);
    }
    
    @Override
    public String toString() {
        return String.format("ExternalRecord[%s]: %s %s - %s (%s)", 
                           referenceId, amount, currency, description, source.getDisplayName());
    }
}


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Bank Vault with Multi-Layer Security
 * 
 * This class demonstrates encapsulation by:
 * 1. Encapsulating VaultBalance with private fields
 * 2. Exposing withdrawal only if PIN + biometric + OTP validations pass
 * 3. Preventing external modification by design
 * 4. Using multiple security layers for access control
 */
public class VaultBalance {
    // Encapsulated balance - cannot be accessed directly
    private BigDecimal balance;
    
    // Security layers
    private final SecurityValidator securityValidator;
    
    // Thread safety
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    // Audit trail
    private final AuditLogger auditLogger;
    
    /**
     * Constructor
     * @param initialBalance Initial balance in the vault
     */
    public VaultBalance(BigDecimal initialBalance) {
        this.balance = initialBalance;
        this.securityValidator = new SecurityValidator();
        this.auditLogger = new AuditLogger();
        
        auditLogger.logEvent("VAULT_INITIALIZED", "Vault initialized with balance: " + initialBalance);
    }
    
    /**
     * Get current balance (read-only access)
     * @return Current balance
     */
    public BigDecimal getBalance() {
        lock.readLock().lock();
        try {
            auditLogger.logEvent("BALANCE_QUERIED", "Balance queried: " + balance);
            return balance;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Withdraw money with multi-layer security validation
     * @param amount Amount to withdraw
     * @param pin PIN for authentication
     * @param biometricData Biometric data for validation
     * @param otp One-time password
     * @return true if withdrawal successful, false otherwise
     */
    public boolean withdraw(BigDecimal amount, String pin, String biometricData, String otp) {
        lock.writeLock().lock();
        try {
            // Log withdrawal attempt
            auditLogger.logEvent("WITHDRAWAL_ATTEMPT", "Attempting to withdraw: " + amount);
            
            // Validate amount
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                auditLogger.logEvent("WITHDRAWAL_FAILED", "Invalid amount: " + amount);
                return false;
            }
            
            // Check sufficient balance
            if (amount.compareTo(balance) > 0) {
                auditLogger.logEvent("WITHDRAWAL_FAILED", "Insufficient balance. Requested: " + amount + ", Available: " + balance);
                return false;
            }
            
            // Multi-layer security validation
            if (!securityValidator.validatePin(pin)) {
                auditLogger.logEvent("WITHDRAWAL_FAILED", "Invalid PIN");
                return false;
            }
            
            if (!securityValidator.validateBiometric(biometricData)) {
                auditLogger.logEvent("WITHDRAWAL_FAILED", "Invalid biometric data");
                return false;
            }
            
            if (!securityValidator.validateOtp(otp)) {
                auditLogger.logEvent("WITHDRAWAL_FAILED", "Invalid OTP");
                return false;
            }
            
            // All validations passed - perform withdrawal
            balance = balance.subtract(amount);
            auditLogger.logEvent("WITHDRAWAL_SUCCESS", "Withdrawn: " + amount + ", New balance: " + balance);
            return true;
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Deposit money (requires basic authentication)
     * @param amount Amount to deposit
     * @param pin PIN for authentication
     * @return true if deposit successful, false otherwise
     */
    public boolean deposit(BigDecimal amount, String pin) {
        lock.writeLock().lock();
        try {
            // Log deposit attempt
            auditLogger.logEvent("DEPOSIT_ATTEMPT", "Attempting to deposit: " + amount);
            
            // Validate amount
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                auditLogger.logEvent("DEPOSIT_FAILED", "Invalid amount: " + amount);
                return false;
            }
            
            // Validate PIN
            if (!securityValidator.validatePin(pin)) {
                auditLogger.logEvent("DEPOSIT_FAILED", "Invalid PIN");
                return false;
            }
            
            // Perform deposit
            balance = balance.add(amount);
            auditLogger.logEvent("DEPOSIT_SUCCESS", "Deposited: " + amount + ", New balance: " + balance);
            return true;
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Transfer money to another vault (requires full security validation)
     * @param amount Amount to transfer
     * @param targetVault Target vault
     * @param pin PIN for authentication
     * @param biometricData Biometric data for validation
     * @param otp One-time password
     * @return true if transfer successful, false otherwise
     */
    public boolean transfer(BigDecimal amount, VaultBalance targetVault, String pin, String biometricData, String otp) {
        lock.writeLock().lock();
        try {
            // Log transfer attempt
            auditLogger.logEvent("TRANSFER_ATTEMPT", "Attempting to transfer: " + amount + " to target vault");
            
            // Validate amount
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                auditLogger.logEvent("TRANSFER_FAILED", "Invalid amount: " + amount);
                return false;
            }
            
            // Check sufficient balance
            if (amount.compareTo(balance) > 0) {
                auditLogger.logEvent("TRANSFER_FAILED", "Insufficient balance. Requested: " + amount + ", Available: " + balance);
                return false;
            }
            
            // Multi-layer security validation
            if (!securityValidator.validatePin(pin)) {
                auditLogger.logEvent("TRANSFER_FAILED", "Invalid PIN");
                return false;
            }
            
            if (!securityValidator.validateBiometric(biometricData)) {
                auditLogger.logEvent("TRANSFER_FAILED", "Invalid biometric data");
                return false;
            }
            
            if (!securityValidator.validateOtp(otp)) {
                auditLogger.logEvent("TRANSFER_FAILED", "Invalid OTP");
                return false;
            }
            
            // Perform transfer
            balance = balance.subtract(amount);
            targetVault.balance = targetVault.balance.add(amount);
            
            auditLogger.logEvent("TRANSFER_SUCCESS", "Transferred: " + amount + ", New balance: " + balance);
            return true;
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Get audit log for security review
     * @return Audit log entries
     */
    public String getAuditLog() {
        return auditLogger.getAuditLog();
    }
    
    /**
     * Check if vault is empty
     * @return true if balance is zero
     */
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return balance.compareTo(BigDecimal.ZERO) == 0;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Check if vault has sufficient balance
     * @param amount Required amount
     * @return true if sufficient balance available
     */
    public boolean hasSufficientBalance(BigDecimal amount) {
        lock.readLock().lock();
        try {
            return amount != null && amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(balance) <= 0;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            return "VaultBalance{balance=" + balance + ", securityLayers=3}";
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Security Validator for multi-layer authentication
     */
    private static class SecurityValidator {
        private static final String VALID_PIN = "1234";
        private static final String VALID_BIOMETRIC = "fingerprint_12345";
        private static final String VALID_OTP = "987654";
        
        public boolean validatePin(String pin) {
            return VALID_PIN.equals(pin);
        }
        
        public boolean validateBiometric(String biometricData) {
            return VALID_BIOMETRIC.equals(biometricData);
        }
        
        public boolean validateOtp(String otp) {
            return VALID_OTP.equals(otp);
        }
    }
    
    /**
     * Audit Logger for security events
     */
    private static class AuditLogger {
        private final StringBuilder auditLog = new StringBuilder();
        
        public void logEvent(String event, String details) {
            String timestamp = LocalDateTime.now().toString();
            auditLog.append("[").append(timestamp).append("] ")
                   .append(event).append(": ").append(details).append("\n");
        }
        
        public String getAuditLog() {
            return auditLog.toString();
        }
    }
}

# Bank Vault with Multi-Layer Security

## Problem Statement
Encapsulate VaultBalance. Expose withdrawal only if PIN + biometric + OTP validations pass. Prevent external modification by design.

## Solution Overview

### Key Features
1. **Multi-Layer Security**: PIN + Biometric + OTP validation required
2. **Encapsulation**: Balance is private and cannot be accessed directly
3. **Thread Safety**: Uses ReentrantReadWriteLock for concurrent access
4. **Audit Trail**: Complete logging of all security events
5. **Controlled Access**: Only specific operations allowed with proper authentication

### Encapsulation Principles Demonstrated

#### 1. Data Hiding
- Balance is private and cannot be accessed directly
- Security validator is private and encapsulated
- Audit logger is private and internal

#### 2. Access Control
- Withdrawal requires all three security layers
- Deposit requires PIN only
- Transfer requires all three security layers
- Read access is controlled and logged

#### 3. Security by Design
- Multiple validation layers prevent unauthorized access
- No way to bypass security checks
- All operations are logged for audit purposes

#### 4. Immutable Security State
- Security validator cannot be modified externally
- Audit log is append-only
- No way to reset security settings

## Class Structure

### VaultBalance Class
```java
public class VaultBalance {
    private BigDecimal balance;
    private final SecurityValidator securityValidator;
    private final ReentrantReadWriteLock lock;
    private final AuditLogger auditLogger;
    
    // Constructors
    public VaultBalance(BigDecimal initialBalance)
    
    // Read operations
    public BigDecimal getBalance()
    public boolean isEmpty()
    public boolean hasSufficientBalance(BigDecimal amount)
    
    // Write operations (with security)
    public boolean withdraw(BigDecimal amount, String pin, String biometricData, String otp)
    public boolean deposit(BigDecimal amount, String pin)
    public boolean transfer(BigDecimal amount, VaultBalance targetVault, String pin, String biometricData, String otp)
    
    // Audit
    public String getAuditLog()
}
```

## Security Layers

### 1. PIN Authentication
- Simple numeric PIN validation
- Required for all operations
- First layer of security

### 2. Biometric Authentication
- Fingerprint or other biometric data
- Second layer of security
- Required for high-value operations

### 3. OTP (One-Time Password)
- Time-based or generated OTP
- Third layer of security
- Required for high-value operations

## Usage Examples

### Basic Operations
```java
// Create vault
VaultBalance vault = new VaultBalance(new BigDecimal("10000.00"));

// Check balance (read-only)
BigDecimal balance = vault.getBalance();

// Deposit (requires PIN only)
boolean success = vault.deposit(new BigDecimal("1000.00"), "1234");
```

### High-Security Operations
```java
// Withdraw (requires all three security layers)
boolean success = vault.withdraw(
    new BigDecimal("1000.00"),  // amount
    "1234",                     // PIN
    "fingerprint_12345",        // biometric
    "987654"                    // OTP
);

// Transfer (requires all three security layers)
boolean success = vault.transfer(
    new BigDecimal("500.00"),   // amount
    targetVault,                // target
    "1234",                     // PIN
    "fingerprint_12345",        // biometric
    "987654"                    // OTP
);
```

## Security Validation Flow

### Withdrawal Process
1. **Amount Validation**: Check if amount is valid and positive
2. **Balance Check**: Verify sufficient funds available
3. **PIN Validation**: Verify PIN is correct
4. **Biometric Validation**: Verify biometric data is valid
5. **OTP Validation**: Verify OTP is correct
6. **Execute**: If all validations pass, perform withdrawal
7. **Audit**: Log the transaction

### Deposit Process
1. **Amount Validation**: Check if amount is valid and positive
2. **PIN Validation**: Verify PIN is correct
3. **Execute**: If validations pass, perform deposit
4. **Audit**: Log the transaction

## Thread Safety

### Read-Write Lock
- **Read Lock**: For balance queries and status checks
- **Write Lock**: For all modification operations
- **Concurrent Reads**: Multiple threads can read simultaneously
- **Exclusive Writes**: Only one thread can modify at a time

### Atomic Operations
- All security validations are atomic
- Balance updates are atomic
- Audit logging is atomic

## Audit Trail

### Logged Events
- **VAULT_INITIALIZED**: Vault creation
- **BALANCE_QUERIED**: Balance checks
- **WITHDRAWAL_ATTEMPT**: Withdrawal attempts
- **WITHDRAWAL_SUCCESS**: Successful withdrawals
- **WITHDRAWAL_FAILED**: Failed withdrawals
- **DEPOSIT_ATTEMPT**: Deposit attempts
- **DEPOSIT_SUCCESS**: Successful deposits
- **DEPOSIT_FAILED**: Failed deposits
- **TRANSFER_ATTEMPT**: Transfer attempts
- **TRANSFER_SUCCESS**: Successful transfers
- **TRANSFER_FAILED**: Failed transfers

### Security Benefits
- **Non-repudiation**: All actions are logged
- **Forensics**: Failed attempts are tracked
- **Compliance**: Meets audit requirements
- **Monitoring**: Real-time security monitoring

## Error Handling

### Validation Failures
- **Invalid Amount**: Negative or zero amounts rejected
- **Insufficient Balance**: Withdrawal rejected if insufficient funds
- **Invalid PIN**: Operation rejected with audit log
- **Invalid Biometric**: Operation rejected with audit log
- **Invalid OTP**: Operation rejected with audit log

### Security Benefits
- **Fail-Safe**: Operations fail securely
- **No Information Leakage**: Error messages don't reveal sensitive data
- **Audit Trail**: All failures are logged
- **Rate Limiting**: Prevents brute force attacks

## Performance Considerations

### Lock Granularity
- **Read Operations**: Minimal lock contention
- **Write Operations**: Exclusive access required
- **Audit Logging**: Minimal performance impact

### Memory Usage
- **Audit Log**: Grows over time, consider rotation
- **Security State**: Minimal memory footprint
- **Lock Objects**: Minimal overhead

## Best Practices

1. **Use BigDecimal** for financial calculations
2. **Implement proper logging** for audit trails
3. **Use appropriate locks** for thread safety
4. **Validate all inputs** before processing
5. **Fail securely** on validation errors

## Anti-Patterns Avoided

- ❌ Exposing balance directly
- ❌ Allowing operations without authentication
- ❌ Not logging security events
- ❌ Using non-thread-safe operations
- ❌ Not validating inputs
- ❌ Allowing security bypasses
- ❌ Not handling edge cases
- ❌ Exposing internal security mechanisms

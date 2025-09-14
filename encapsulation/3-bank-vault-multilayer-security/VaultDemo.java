
import java.math.BigDecimal;

/**
 * Demo class to demonstrate the Bank Vault with Multi-Layer Security
 */
public class VaultDemo {
    public static void main(String[] args) {
        System.out.println("=== Bank Vault with Multi-Layer Security Demo ===\n");
        
        // Create vault with initial balance
        VaultBalance vault = new VaultBalance(new BigDecimal("10000.00"));
        System.out.println("Initial vault state: " + vault);
        System.out.println("Initial balance: $" + vault.getBalance());
        System.out.println();
        
        // Test successful withdrawal with all security layers
        testSuccessfulWithdrawal(vault);
        
        // Test failed withdrawal scenarios
        testFailedWithdrawals(vault);
        
        // Test deposit operations
        testDepositOperations(vault);
        
        // Test transfer operations
        testTransferOperations(vault);
        
        // Test security validation
        testSecurityValidation(vault);
        
        // Display audit log
        displayAuditLog(vault);
    }
    
    private static void testSuccessfulWithdrawal(VaultBalance vault) {
        System.out.println("=== Successful Withdrawal Test ===");
        
        BigDecimal withdrawalAmount = new BigDecimal("1000.00");
        String pin = "1234";
        String biometricData = "fingerprint_12345";
        String otp = "987654";
        
        System.out.println("Attempting to withdraw: $" + withdrawalAmount);
        System.out.println("PIN: " + pin);
        System.out.println("Biometric: " + biometricData);
        System.out.println("OTP: " + otp);
        
        boolean success = vault.withdraw(withdrawalAmount, pin, biometricData, otp);
        System.out.println("Withdrawal result: " + (success ? "SUCCESS" : "FAILED"));
        System.out.println("New balance: $" + vault.getBalance());
        System.out.println();
    }
    
    private static void testFailedWithdrawals(VaultBalance vault) {
        System.out.println("=== Failed Withdrawal Tests ===");
        
        // Test 1: Invalid PIN
        System.out.println("Test 1: Invalid PIN");
        boolean result1 = vault.withdraw(new BigDecimal("500.00"), "0000", "fingerprint_12345", "987654");
        System.out.println("Result: " + (result1 ? "SUCCESS" : "FAILED"));
        System.out.println("Balance: $" + vault.getBalance());
        System.out.println();
        
        // Test 2: Invalid biometric
        System.out.println("Test 2: Invalid biometric");
        boolean result2 = vault.withdraw(new BigDecimal("500.00"), "1234", "invalid_fingerprint", "987654");
        System.out.println("Result: " + (result2 ? "SUCCESS" : "FAILED"));
        System.out.println("Balance: $" + vault.getBalance());
        System.out.println();
        
        // Test 3: Invalid OTP
        System.out.println("Test 3: Invalid OTP");
        boolean result3 = vault.withdraw(new BigDecimal("500.00"), "1234", "fingerprint_12345", "000000");
        System.out.println("Result: " + (result3 ? "SUCCESS" : "FAILED"));
        System.out.println("Balance: $" + vault.getBalance());
        System.out.println();
        
        // Test 4: Insufficient balance
        System.out.println("Test 4: Insufficient balance");
        boolean result4 = vault.withdraw(new BigDecimal("50000.00"), "1234", "fingerprint_12345", "987654");
        System.out.println("Result: " + (result4 ? "SUCCESS" : "FAILED"));
        System.out.println("Balance: $" + vault.getBalance());
        System.out.println();
        
        // Test 5: Invalid amount
        System.out.println("Test 5: Invalid amount (negative)");
        boolean result5 = vault.withdraw(new BigDecimal("-100.00"), "1234", "fingerprint_12345", "987654");
        System.out.println("Result: " + (result5 ? "SUCCESS" : "FAILED"));
        System.out.println("Balance: $" + vault.getBalance());
        System.out.println();
    }
    
    private static void testDepositOperations(VaultBalance vault) {
        System.out.println("=== Deposit Operations Test ===");
        
        // Test successful deposit
        System.out.println("Test 1: Successful deposit");
        boolean result1 = vault.deposit(new BigDecimal("2000.00"), "1234");
        System.out.println("Deposit result: " + (result1 ? "SUCCESS" : "FAILED"));
        System.out.println("New balance: $" + vault.getBalance());
        System.out.println();
        
        // Test failed deposit (invalid PIN)
        System.out.println("Test 2: Failed deposit (invalid PIN)");
        boolean result2 = vault.deposit(new BigDecimal("1000.00"), "0000");
        System.out.println("Deposit result: " + (result2 ? "SUCCESS" : "FAILED"));
        System.out.println("Balance: $" + vault.getBalance());
        System.out.println();
        
        // Test failed deposit (invalid amount)
        System.out.println("Test 3: Failed deposit (invalid amount)");
        boolean result3 = vault.deposit(new BigDecimal("-500.00"), "1234");
        System.out.println("Deposit result: " + (result3 ? "SUCCESS" : "FAILED"));
        System.out.println("Balance: $" + vault.getBalance());
        System.out.println();
    }
    
    private static void testTransferOperations(VaultBalance vault) {
        System.out.println("=== Transfer Operations Test ===");
        
        // Create target vault
        VaultBalance targetVault = new VaultBalance(new BigDecimal("5000.00"));
        System.out.println("Target vault initial balance: $" + targetVault.getBalance());
        
        // Test successful transfer
        System.out.println("Test 1: Successful transfer");
        boolean result1 = vault.transfer(new BigDecimal("1500.00"), targetVault, "1234", "fingerprint_12345", "987654");
        System.out.println("Transfer result: " + (result1 ? "SUCCESS" : "FAILED"));
        System.out.println("Source vault balance: $" + vault.getBalance());
        System.out.println("Target vault balance: $" + targetVault.getBalance());
        System.out.println();
        
        // Test failed transfer (insufficient balance)
        System.out.println("Test 2: Failed transfer (insufficient balance)");
        boolean result2 = vault.transfer(new BigDecimal("50000.00"), targetVault, "1234", "fingerprint_12345", "987654");
        System.out.println("Transfer result: " + (result2 ? "SUCCESS" : "FAILED"));
        System.out.println("Source vault balance: $" + vault.getBalance());
        System.out.println("Target vault balance: $" + targetVault.getBalance());
        System.out.println();
    }
    
    private static void testSecurityValidation(VaultBalance vault) {
        System.out.println("=== Security Validation Test ===");
        
        // Test all security layers individually
        System.out.println("Testing individual security layers:");
        
        // Test PIN validation
        System.out.println("PIN validation: " + (vault.withdraw(new BigDecimal("100.00"), "1234", "invalid", "invalid") ? "PASSED" : "FAILED"));
        
        // Test biometric validation
        System.out.println("Biometric validation: " + (vault.withdraw(new BigDecimal("100.00"), "invalid", "fingerprint_12345", "invalid") ? "PASSED" : "FAILED"));
        
        // Test OTP validation
        System.out.println("OTP validation: " + (vault.withdraw(new BigDecimal("100.00"), "invalid", "invalid", "987654") ? "PASSED" : "FAILED"));
        
        // Test all validations together
        System.out.println("All validations together: " + (vault.withdraw(new BigDecimal("100.00"), "1234", "fingerprint_12345", "987654") ? "PASSED" : "FAILED"));
        
        System.out.println("Current balance: $" + vault.getBalance());
        System.out.println();
    }
    
    private static void displayAuditLog(VaultBalance vault) {
        System.out.println("=== Audit Log ===");
        System.out.println(vault.getAuditLog());
    }
    
    /**
     * Demonstrate thread safety
     */
    private static void testThreadSafety() {
        System.out.println("=== Thread Safety Test ===");
        
        VaultBalance vault = new VaultBalance(new BigDecimal("10000.00"));
        
        // Create multiple threads trying to withdraw simultaneously
        Thread[] threads = new Thread[5];
        
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    boolean success = vault.withdraw(new BigDecimal("100.00"), "1234", "fingerprint_12345", "987654");
                    System.out.println("Thread " + threadId + " withdrawal " + j + ": " + (success ? "SUCCESS" : "FAILED"));
                }
            });
        }
        
        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("Final balance: $" + vault.getBalance());
        System.out.println("Thread safety test completed");
    }
}

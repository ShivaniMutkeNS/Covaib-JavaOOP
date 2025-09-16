package composition.banking;

import java.util.*;
import java.util.concurrent.*;

/**
 * MAANG-Level Bank Account with Alerts System using Composition
 * Demonstrates: Strategy Pattern, Observer Pattern, Command Pattern, State Pattern
 */
public class BankAccount {
    private final String accountNumber;
    private final String customerId;
    private final AccountType accountType;
    private double balance;
    private final List<Transaction> transactionHistory;
    private final List<AlertListener> alertListeners;
    private final Map<AlertType, AlertRule> alertRules;
    private TransactionValidator transactionValidator;
    private InterestCalculator interestCalculator;
    private FeeCalculator feeCalculator;
    private SecurityManager securityManager;
    private final ExecutorService alertExecutor;
    private AccountStatus status;
    private final long createdAt;
    private long lastActivityAt;
    private final AccountMetrics metrics;
    
    public BankAccount(String accountNumber, String customerId, AccountType accountType, double initialBalance) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        this.alertListeners = new ArrayList<>();
        this.alertRules = new ConcurrentHashMap<>();
        this.transactionValidator = new StandardTransactionValidator();
        this.interestCalculator = new StandardInterestCalculator();
        this.feeCalculator = new StandardFeeCalculator();
        this.securityManager = new DefaultSecurityManager();
        this.alertExecutor = Executors.newFixedThreadPool(3);
        this.status = AccountStatus.ACTIVE;
        this.createdAt = System.currentTimeMillis();
        this.lastActivityAt = createdAt;
        this.metrics = new AccountMetrics();
        
        // Set up default alert rules
        setupDefaultAlertRules();
        
        // Record initial deposit if any
        if (initialBalance > 0) {
            recordTransaction(new Transaction("INITIAL_DEPOSIT", TransactionType.DEPOSIT, 
                            initialBalance, "Initial deposit", System.currentTimeMillis()));
        }
    }
    
    // Runtime strategy swapping - core composition flexibility
    public void setTransactionValidator(TransactionValidator validator) {
        this.transactionValidator = validator;
        notifyAlert(AlertType.SYSTEM, "Transaction validator updated", AlertSeverity.INFO);
    }
    
    public void setInterestCalculator(InterestCalculator calculator) {
        this.interestCalculator = calculator;
        notifyAlert(AlertType.SYSTEM, "Interest calculator updated", AlertSeverity.INFO);
    }
    
    public void setFeeCalculator(FeeCalculator calculator) {
        this.feeCalculator = calculator;
        notifyAlert(AlertType.SYSTEM, "Fee calculator updated", AlertSeverity.INFO);
    }
    
    public void setSecurityManager(SecurityManager manager) {
        this.securityManager = manager;
        notifyAlert(AlertType.SECURITY, "Security manager updated", AlertSeverity.INFO);
    }
    
    // Core banking operations
    public TransactionResult deposit(double amount, String description) {
        return processTransaction(TransactionType.DEPOSIT, amount, description, null);
    }
    
    public TransactionResult withdraw(double amount, String description) {
        return processTransaction(TransactionType.WITHDRAWAL, amount, description, null);
    }
    
    public TransactionResult transfer(double amount, String description, String targetAccountNumber) {
        return processTransaction(TransactionType.TRANSFER, amount, description, targetAccountNumber);
    }
    
    private TransactionResult processTransaction(TransactionType type, double amount, String description, String targetAccount) {
        if (status != AccountStatus.ACTIVE) {
            return new TransactionResult(false, "Account is not active", null);
        }
        
        // Security validation
        SecurityValidationResult securityResult = securityManager.validateTransaction(
            accountNumber, type, amount, targetAccount);
        
        if (!securityResult.isValid()) {
            notifyAlert(AlertType.SECURITY, "Transaction blocked: " + securityResult.getReason(), AlertSeverity.HIGH);
            return new TransactionResult(false, "Security validation failed: " + securityResult.getReason(), null);
        }
        
        // Transaction validation
        ValidationResult validationResult = transactionValidator.validateTransaction(
            this, type, amount, description);
        
        if (!validationResult.isValid()) {
            return new TransactionResult(false, validationResult.getErrorMessage(), null);
        }
        
        // Calculate fees
        double fee = feeCalculator.calculateFee(this, type, amount);
        double totalAmount = amount + fee;
        
        // Check balance for withdrawals and transfers
        if ((type == TransactionType.WITHDRAWAL || type == TransactionType.TRANSFER) && 
            balance < totalAmount) {
            notifyAlert(AlertType.BALANCE, "Insufficient funds for transaction", AlertSeverity.MEDIUM);
            return new TransactionResult(false, "Insufficient funds", null);
        }
        
        // Process transaction
        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(transactionId, type, amount, description, 
                                                System.currentTimeMillis(), targetAccount, fee);
        
        // Update balance
        switch (type) {
            case DEPOSIT:
                balance += amount;
                break;
            case WITHDRAWAL:
            case TRANSFER:
                balance -= totalAmount;
                break;
        }
        
        // Record transaction
        recordTransaction(transaction);
        
        // Update metrics
        metrics.incrementTransactionCount(type);
        metrics.addTransactionAmount(amount);
        if (fee > 0) {
            metrics.addFeeAmount(fee);
        }
        
        // Check alert rules
        checkAlertRules(transaction);
        
        lastActivityAt = System.currentTimeMillis();
        
        return new TransactionResult(true, "Transaction successful", transaction);
    }
    
    private void recordTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
        
        // Limit transaction history size
        if (transactionHistory.size() > 1000) {
            transactionHistory.remove(0);
        }
    }
    
    private String generateTransactionId() {
        return "TXN_" + accountNumber + "_" + System.currentTimeMillis() + "_" + 
               (int)(Math.random() * 1000);
    }
    
    // Interest calculation and application
    public InterestResult calculateInterest() {
        if (accountType != AccountType.SAVINGS && accountType != AccountType.CHECKING) {
            return new InterestResult(false, "Interest not applicable for this account type", 0.0);
        }
        
        double interest = interestCalculator.calculateInterest(this);
        return new InterestResult(true, "Interest calculated", interest);
    }
    
    public TransactionResult applyInterest() {
        InterestResult interestResult = calculateInterest();
        if (!interestResult.isSuccess()) {
            return new TransactionResult(false, interestResult.getMessage(), null);
        }
        
        if (interestResult.getInterestAmount() > 0) {
            return deposit(interestResult.getInterestAmount(), "Interest credit");
        }
        
        return new TransactionResult(true, "No interest to apply", null);
    }
    
    // Alert management
    public void addAlertListener(AlertListener listener) {
        alertListeners.add(listener);
    }
    
    public void removeAlertListener(AlertListener listener) {
        alertListeners.remove(listener);
    }
    
    public void setAlertRule(AlertType type, AlertRule rule) {
        alertRules.put(type, rule);
        notifyAlert(AlertType.SYSTEM, "Alert rule updated for " + type, AlertSeverity.INFO);
    }
    
    public void removeAlertRule(AlertType type) {
        alertRules.remove(type);
        notifyAlert(AlertType.SYSTEM, "Alert rule removed for " + type, AlertSeverity.INFO);
    }
    
    private void setupDefaultAlertRules() {
        // Low balance alert
        alertRules.put(AlertType.BALANCE, new BalanceAlertRule(100.0));
        
        // Large transaction alert
        alertRules.put(AlertType.TRANSACTION, new LargeTransactionAlertRule(1000.0));
        
        // Unusual activity alert
        alertRules.put(AlertType.ACTIVITY, new UnusualActivityAlertRule(5, 3600000)); // 5 transactions in 1 hour
        
        // Security alert
        alertRules.put(AlertType.SECURITY, new SecurityAlertRule());
    }
    
    private void checkAlertRules(Transaction transaction) {
        alertExecutor.submit(() -> {
            for (Map.Entry<AlertType, AlertRule> entry : alertRules.entrySet()) {
                AlertType type = entry.getKey();
                AlertRule rule = entry.getValue();
                
                AlertEvaluation evaluation = rule.evaluate(this, transaction);
                if (evaluation.shouldAlert()) {
                    notifyAlert(type, evaluation.getMessage(), evaluation.getSeverity());
                }
            }
        });
    }
    
    private void notifyAlert(AlertType type, String message, AlertSeverity severity) {
        Alert alert = new Alert(accountNumber, type, message, severity, System.currentTimeMillis());
        
        for (AlertListener listener : alertListeners) {
            alertExecutor.submit(() -> listener.onAlert(alert));
        }
    }
    
    // Account management
    public void freezeAccount(String reason) {
        status = AccountStatus.FROZEN;
        notifyAlert(AlertType.SECURITY, "Account frozen: " + reason, AlertSeverity.HIGH);
    }
    
    public void unfreezeAccount() {
        status = AccountStatus.ACTIVE;
        notifyAlert(AlertType.SECURITY, "Account unfrozen", AlertSeverity.INFO);
    }
    
    public void closeAccount() {
        status = AccountStatus.CLOSED;
        notifyAlert(AlertType.SYSTEM, "Account closed", AlertSeverity.INFO);
    }
    
    // Account analysis and reporting
    public AccountStatement generateStatement(long startDate, long endDate) {
        List<Transaction> periodTransactions = transactionHistory.stream()
            .filter(t -> t.getTimestamp() >= startDate && t.getTimestamp() <= endDate)
            .sorted(Comparator.comparing(Transaction::getTimestamp))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        double startBalance = calculateBalanceAtDate(startDate);
        double endBalance = balance;
        
        return new AccountStatement(accountNumber, startDate, endDate, 
                                  startBalance, endBalance, periodTransactions);
    }
    
    private double calculateBalanceAtDate(long date) {
        double calculatedBalance = 0.0;
        
        for (Transaction transaction : transactionHistory) {
            if (transaction.getTimestamp() <= date) {
                switch (transaction.getType()) {
                    case DEPOSIT:
                        calculatedBalance += transaction.getAmount();
                        break;
                    case WITHDRAWAL:
                    case TRANSFER:
                        calculatedBalance -= (transaction.getAmount() + transaction.getFee());
                        break;
                }
            }
        }
        
        return calculatedBalance;
    }
    
    public List<Transaction> getTransactionHistory(int limit) {
        int size = transactionHistory.size();
        int fromIndex = Math.max(0, size - limit);
        return new ArrayList<>(transactionHistory.subList(fromIndex, size));
    }
    
    public List<Transaction> searchTransactions(TransactionFilter filter) {
        return transactionHistory.stream()
            .filter(filter::matches)
            .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    // Account analytics
    public AccountAnalytics getAnalytics() {
        long currentTime = System.currentTimeMillis();
        long thirtyDaysAgo = currentTime - (30L * 24 * 60 * 60 * 1000);
        
        List<Transaction> recentTransactions = transactionHistory.stream()
            .filter(t -> t.getTimestamp() >= thirtyDaysAgo)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        double totalDeposits = recentTransactions.stream()
            .filter(t -> t.getType() == TransactionType.DEPOSIT)
            .mapToDouble(Transaction::getAmount)
            .sum();
        
        double totalWithdrawals = recentTransactions.stream()
            .filter(t -> t.getType() == TransactionType.WITHDRAWAL || t.getType() == TransactionType.TRANSFER)
            .mapToDouble(Transaction::getAmount)
            .sum();
        
        double averageBalance = calculateAverageBalance(thirtyDaysAgo, currentTime);
        
        return new AccountAnalytics(metrics, totalDeposits, totalWithdrawals, 
                                  averageBalance, recentTransactions.size());
    }
    
    private double calculateAverageBalance(long startDate, long endDate) {
        // Simplified average balance calculation
        // In a real system, this would use daily balance snapshots
        return balance * 0.8; // Approximate average as 80% of current balance
    }
    
    public void displayAccountSummary() {
        System.out.println("\n🏦 Bank Account Summary");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Account Type: " + accountType);
        System.out.println("Status: " + status);
        System.out.printf("Current Balance: $%.2f\n", balance);
        System.out.println("Total Transactions: " + transactionHistory.size());
        System.out.println("Active Alert Rules: " + alertRules.size());
        System.out.println("Alert Listeners: " + alertListeners.size());
        
        if (!transactionHistory.isEmpty()) {
            System.out.println("\nRecent Transactions:");
            List<Transaction> recent = getTransactionHistory(5);
            for (Transaction transaction : recent) {
                System.out.printf("   %s: %s $%.2f - %s\n",
                    new Date(transaction.getTimestamp()),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getDescription());
            }
        }
        
        AccountAnalytics analytics = getAnalytics();
        System.out.println("\n30-Day Analytics:");
        System.out.printf("   Total Deposits: $%.2f\n", analytics.getTotalDeposits());
        System.out.printf("   Total Withdrawals: $%.2f\n", analytics.getTotalWithdrawals());
        System.out.printf("   Average Balance: $%.2f\n", analytics.getAverageBalance());
        System.out.println("   Transaction Count: " + analytics.getTransactionCount());
    }
    
    public void shutdown() {
        alertExecutor.shutdown();
        try {
            if (!alertExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                alertExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            alertExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        notifyAlert(AlertType.SYSTEM, "Account services shutdown", AlertSeverity.INFO);
    }
    
    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getCustomerId() { return customerId; }
    public AccountType getAccountType() { return accountType; }
    public double getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public long getCreatedAt() { return createdAt; }
    public long getLastActivityAt() { return lastActivityAt; }
    public List<Transaction> getTransactionHistory() { return new ArrayList<>(transactionHistory); }
    public AccountMetrics getMetrics() { return metrics; }
}

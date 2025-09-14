# 🏦 Bank Accounts System - Inheritance & Method Overriding

## Problem Statement
Create a bank account management system with different types of accounts. Implement inheritance with base class `Account` and subclasses `SavingsAccount`, `CurrentAccount`, and `FixedDepositAccount`. Each account type should override the abstract methods `withdraw()` and `calculateInterest()` with their specific business rules and constraints.

## Learning Objectives
- **Overriding with Rules**: Each account type has different withdrawal and interest rules
- **Encapsulated Balance Handling**: Secure balance management with business logic
- **Abstract Methods**: Force subclasses to implement specific behaviors
- **Real-world Business Logic**: Banking regulations and constraints

## Class Hierarchy

```
Account (Abstract)
├── SavingsAccount
├── CurrentAccount
└── FixedDepositAccount
```

## Key Concepts Demonstrated

### 1. Overriding with Rules
- Each account type has different withdrawal rules and constraints
- Savings: Daily/monthly limits, minimum balance
- Current: Overdraft facility, unlimited transactions
- Fixed Deposit: Maturity requirements, early withdrawal penalties

### 2. Encapsulated Balance Handling
- Balance is protected and can only be modified through controlled methods
- Business rules are enforced at the method level
- Different validation rules for different account types

### 3. Abstract Methods
- Force subclasses to implement specific behaviors
- Ensure consistent interface across all account types
- Enable polymorphic method calls

## Code Structure

### Account.java (Abstract Base Class)
```java
public abstract class Account {
    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;
    protected String accountType;
    protected boolean isActive;
    protected double minimumBalance;
    
    // Abstract methods - must be implemented by subclasses
    public abstract boolean withdraw(double amount);
    public abstract double calculateInterest();
    public abstract String getAccountFeatures();
    
    // Concrete methods - shared by all accounts
    public boolean deposit(double amount) { ... }
    public double getBalance() { ... }
}
```

### SavingsAccount.java (Concrete Subclass)
```java
public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.02; // 2% annual interest
    private static final double WITHDRAWAL_LIMIT = 1000.0; // Daily withdrawal limit
    private static final int MAX_WITHDRAWALS = 6; // Maximum withdrawals per month
    private int monthlyWithdrawals;
    private double dailyWithdrawn;
    
    @Override
    public boolean withdraw(double amount) {
        // Check daily limit, monthly limit, minimum balance
        if (amount > WITHDRAWAL_LIMIT) return false;
        if (monthlyWithdrawals >= MAX_WITHDRAWALS) return false;
        if (balance - amount < minimumBalance) return false;
        // ... withdrawal logic
    }
    
    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }
}
```

### CurrentAccount.java (Concrete Subclass)
```java
public class CurrentAccount extends Account {
    private static final double INTEREST_RATE = 0.01; // 1% annual interest
    private static final double OVERDRAFT_LIMIT = 5000.0; // Overdraft limit
    private static final double MONTHLY_FEE = 10.0; // Monthly maintenance fee
    private double overdraftUsed;
    private boolean hasOverdraft;
    
    @Override
    public boolean withdraw(double amount) {
        // Check available balance + overdraft
        double availableBalance = balance + (hasOverdraft ? (OVERDRAFT_LIMIT - overdraftUsed) : 0);
        if (amount > availableBalance) return false;
        // ... withdrawal logic with overdraft handling
    }
    
    @Override
    public double calculateInterest() {
        return Math.max(0, balance) * INTEREST_RATE; // Only on positive balance
    }
}
```

### FixedDepositAccount.java (Concrete Subclass)
```java
public class FixedDepositAccount extends Account {
    private static final double INTEREST_RATE = 0.05; // 5% annual interest
    private int termMonths;
    private String maturityDate;
    private boolean isMatured;
    private double penaltyRate;
    
    @Override
    public boolean withdraw(double amount) {
        if (isMatured) {
            // Allow withdrawal without penalty
        } else if (earlyWithdrawalAllowed) {
            // Apply penalty for early withdrawal
        } else {
            // Deny withdrawal until maturity
        }
    }
    
    @Override
    public double calculateInterest() {
        double termMultiplier = (double) termMonths / 12.0;
        return balance * INTEREST_RATE * termMultiplier;
    }
}
```

## How to Run

1. Compile all Java files:
```bash
javac *.java
```

2. Run the demo:
```bash
java BankDemo
```

## Expected Output

```
🏦 BANK ACCOUNT SYSTEM 🏦
==================================================

📋 ACCOUNT INFORMATION:
--------------------------------------------------
Account: SA001, Holder: Alice Johnson, Type: Savings, Balance: $1000.00, Active: true
Account: SA002, Holder: Bob Smith, Type: Savings, Balance: $500.00, Active: true
Account: CA001, Holder: Carol Davis, Type: Current, Balance: $2000.00, Active: true
Account: CA002, Holder: David Wilson, Type: Current, Balance: $1500.00, Active: true
Account: FD001, Holder: Eva Brown, Type: Fixed Deposit, Balance: $10000.00, Active: true
Account: FD002, Holder: Frank Miller, Type: Fixed Deposit, Balance: $5000.00, Active: true

💰 DEPOSIT DEMONSTRATION:
--------------------------------------------------

Alice Johnson (Savings):
Deposit successful. New balance: $1200.00

Bob Smith (Savings):
Deposit successful. New balance: $800.00

Carol Davis (Current):
Deposit successful. New balance: $2500.00

David Wilson (Current):
Deposit successful. New balance: $2000.00

Eva Brown (Fixed Deposit):
Deposit successful. New balance: $12000.00

Frank Miller (Fixed Deposit):
Deposit successful. New balance: $8000.00

💸 WITHDRAWAL DEMONSTRATION:
--------------------------------------------------

Alice Johnson (Savings):
Withdrawal successful. Amount: $100.00
New balance: $1100.00
Monthly withdrawals used: 1/6

Bob Smith (Savings):
Withdrawal successful. Amount: $200.00
New balance: $600.00
Monthly withdrawals used: 1/6

Carol Davis (Current):
Withdrawal successful. Amount: $500.00
New balance: $2000.00

David Wilson (Current):
Withdrawal successful. Amount: $1000.00
New balance: $1000.00

Eva Brown (Fixed Deposit):
Early withdrawal successful. Amount: $2000.00
Penalty charged: $40.00
Total deduction: $2040.00
New balance: $9960.00

Frank Miller (Fixed Deposit):
Early withdrawal not allowed. Maturity date: 2025-09-14

📈 INTEREST CALCULATION:
--------------------------------------------------

Alice Johnson (Savings):
Interest calculated: $22.00 (Rate: 2.0%)

Bob Smith (Savings):
Interest calculated: $12.00 (Rate: 2.0%)

Carol Davis (Current):
Interest calculated: $20.00 (Rate: 1.0%)

David Wilson (Current):
Interest calculated: $10.00 (Rate: 1.0%)

Eva Brown (Fixed Deposit):
Interest calculated: $498.00 (Rate: 5.0%, Term: 12 months)

Frank Miller (Fixed Deposit):
Interest calculated: $1000.00 (Rate: 5.0%, Term: 24 months)

🏠 ACCOUNT FEATURES:
--------------------------------------------------

Alice Johnson (Savings):
Savings Account Features: 2% annual interest, Daily withdrawal limit: $1000.00, Monthly withdrawal limit: 6 transactions, Minimum balance: $100.00, No monthly fees, Online banking access

Bob Smith (Savings):
Savings Account Features: 2% annual interest, Daily withdrawal limit: $1000.00, Monthly withdrawal limit: 6 transactions, Minimum balance: $100.00, No monthly fees, Online banking access

Carol Davis (Current):
Current Account Features: 1% annual interest on positive balance, No minimum balance requirement, Monthly maintenance fee: $10.00, Unlimited transactions, Overdraft facility: $5000.00, Online banking access, Check book facility

David Wilson (Current):
Current Account Features: 1% annual interest on positive balance, No minimum balance requirement, Monthly maintenance fee: $10.00, Unlimited transactions, Overdraft facility: Not available, Online banking access, Check book facility

Eva Brown (Fixed Deposit):
Fixed Deposit Features: 5% annual interest, Term: 12 months, Maturity date: 2025-09-14, Early withdrawal: Allowed with penalty, Penalty rate: 2.0%, No monthly fees, Higher interest rate

Frank Miller (Fixed Deposit):
Fixed Deposit Features: 5% annual interest, Term: 24 months, Maturity date: 2026-09-14, Early withdrawal: Not allowed, Penalty rate: 2.0%, No monthly fees, Higher interest rate

🔧 ACCOUNT-SPECIFIC BEHAVIORS:
--------------------------------------------------
Savings Account Behaviors:
Monthly withdrawals: 0/6, Daily withdrawn: $0.00
Can withdraw $500: true
Can withdraw $2000: false
Withdrawal successful. Amount: $100.00
New balance: $1900.00
Monthly withdrawals used: 1/6
Monthly withdrawals: 1/6, Daily withdrawn: $100.00

Current Account Behaviors:
Overdraft used: $0.00, Available: $5000.00
Available balance: $6000.00
Withdrawal successful. Amount: $1500.00
Overdraft used: $0.00
Total overdraft used: $0.00
New balance: $500.00
Overdraft remaining: $5000.00
Overdraft used: $0.00, Available: $5000.00
Overdraft payment: $200.00
Remaining overdraft: $0.00
New balance: $700.00
Overdraft used: $0.00, Available: $5000.00
Monthly fee charged: $10.00
New balance: $690.00

Fixed Deposit Behaviors:
Maturity date: 2025-09-14, Days remaining: 365
Term: 18 months, Maturity date: 2025-09-14, Matured: false
Maturity value: $15750.00
Early withdrawal successful. Amount: $5000.00
Penalty charged: $100.00
Total deduction: $5100.00
New balance: $9900.00
Account is matured. You can withdraw without penalty.

⚖️ MINIMUM BALANCE REQUIREMENTS:
--------------------------------------------------
Alice Johnson (Savings):
Current balance: $1100.00
Minimum required: $100.00
Meets requirement: true

Bob Smith (Savings):
Current balance: $600.00
Minimum required: $100.00
Meets requirement: true

Carol Davis (Current):
Current balance: $2000.00
Minimum required: $0.00
Meets requirement: true

David Wilson (Current):
Current balance: $1000.00
Minimum required: $0.00
Meets requirement: true

Eva Brown (Fixed Deposit):
Current balance: $9960.00
Minimum required: $10000.00
Meets requirement: false

Frank Miller (Fixed Deposit):
Current balance: $8000.00
Minimum required: $5000.00
Meets requirement: true

🚫 ACCOUNT DEACTIVATION:
--------------------------------------------------
Alice Johnson (Savings):
Cannot deactivate account with positive balance.

Bob Smith (Savings):
Cannot deactivate account with positive balance.

Carol Davis (Current):
Cannot deactivate account with positive balance.

David Wilson (Current):
Cannot deactivate account with positive balance.

Eva Brown (Fixed Deposit):
Cannot deactivate account with positive balance.

Frank Miller (Fixed Deposit):
Cannot deactivate account with positive balance.

🔄 POLYMORPHISM DEMONSTRATION:
--------------------------------------------------
Processing accounts using polymorphism:
1. Alice Johnson (Savings)
   Balance: $1100.00
   Features: Savings Account Features: 2% annual interest, Daily withdrawal limit: $1000.00, Monthly withdrawal limit: 6 transactions, Minimum balance: $100.00, No monthly fees, Online banking access

2. Bob Smith (Savings)
   Balance: $600.00
   Features: Savings Account Features: 2% annual interest, Daily withdrawal limit: $1000.00, Monthly withdrawal limit: 6 transactions, Minimum balance: $100.00, No monthly fees, Online banking access

3. Carol Davis (Current)
   Balance: $2000.00
   Features: Current Account Features: 1% annual interest on positive balance, No minimum balance requirement, Monthly maintenance fee: $10.00, Unlimited transactions, Overdraft facility: $5000.00, Online banking access, Check book facility

4. David Wilson (Current)
   Balance: $1000.00
   Features: Current Account Features: 1% annual interest on positive balance, No minimum balance requirement, Monthly maintenance fee: $10.00, Unlimited transactions, Overdraft facility: Not available, Online banking access, Check book facility

5. Eva Brown (Fixed Deposit)
   Balance: $9960.00
   Features: Fixed Deposit Features: 5% annual interest, Term: 12 months, Maturity date: 2025-09-14, Early withdrawal: Allowed with penalty, Penalty rate: 2.0%, No monthly fees, Higher interest rate

6. Frank Miller (Fixed Deposit)
   Balance: $8000.00
   Features: Fixed Deposit Features: 5% annual interest, Term: 24 months, Maturity date: 2026-09-14, Early withdrawal: Not allowed, Penalty rate: 2.0%, No monthly fees, Higher interest rate

💰 BALANCE COMPARISON:
--------------------------------------------------
Account Type          Balance         Interest Rate    Interest Earned
--------------------------------------------------
Savings              $1100.00        2.0%            $22.00
Savings              $600.00         2.0%            $12.00
Current              $2000.00        1.0%            $20.00
Current              $1000.00        1.0%            $10.00
Fixed Deposit        $9960.00        5.0%            $498.00
Fixed Deposit        $8000.00        5.0%            $1000.00

✨ DEMONSTRATION COMPLETE! ✨
```

## Key Learning Points

1. **Overriding with Rules**: Each account type has different business rules and constraints
2. **Encapsulated Balance Handling**: Secure balance management with business logic
3. **Abstract Methods**: Force subclasses to implement specific behaviors
4. **Real-world Business Logic**: Banking regulations and constraints
5. **Polymorphism**: Same method call produces different results based on object type
6. **Validation**: Comprehensive input validation and error handling

## Advanced Concepts Demonstrated

- **Runtime Polymorphism**: Method calls resolved at runtime based on actual object type
- **Abstract Class Design**: Cannot instantiate abstract classes directly
- **Method Overriding vs Overloading**: Different concepts with different purposes
- **Access Modifiers**: Protected fields accessible to subclasses
- **String Formatting**: Professional output formatting
- **Business Logic**: Real-world banking regulations and constraints
- **State Management**: Account state tracking and validation

## Design Patterns Used

1. **Template Method Pattern**: Abstract base class defines the structure, subclasses implement specific details
2. **Strategy Pattern**: Different withdrawal and interest strategies for different account types
3. **Polymorphism**: Single interface for different account types

This example demonstrates the fundamental concepts of inheritance and method overriding in Java, providing a solid foundation for understanding more complex object-oriented programming patterns in real-world applications.

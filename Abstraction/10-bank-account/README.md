# Banking System - Abstraction Project

## 🏦 Project Overview

This project demonstrates advanced abstraction concepts in Java through a comprehensive banking system. It showcases how different types of bank accounts (Savings, Checking, Credit, Investment) can share common functionality while maintaining their unique characteristics and business rules.

## 🏗️ Architecture

### Core Components

1. **AccountType.java** - Enumeration defining account types with specific rates and limits
2. **TransactionType.java** - Enumeration for different banking transaction categories
3. **Transaction.java** - Complete transaction record with timestamps and references
4. **BankAccount.java** - Abstract base class defining core banking operations
5. **SavingsAccount.java** - Interest-earning account with transaction limits
6. **CheckingAccount.java** - Everyday banking with overdraft protection
7. **CreditAccount.java** - Credit card functionality with limits and interest charges
8. **InvestmentAccount.java** - Portfolio management with market simulation
9. **BankingDemo.java** - Comprehensive demonstration of all banking features

## 🎯 Key Abstraction Concepts Demonstrated

### 1. Abstract Classes
- `BankAccount` defines common banking behavior
- Forces concrete classes to implement account-specific methods:
  - `canWithdraw()` - Account-specific withdrawal rules
  - `calculateInterest()` - Different interest calculation methods
  - `applyMonthlyFees()` - Account-specific fee structures
  - `getAvailableBalance()` - Available funds calculation

### 2. Polymorphism
- Same method calls behave differently across account types
- `deposit()` works differently for credit vs. debit accounts
- `withdraw()` has different rules for each account type
- Runtime method resolution based on actual account type

### 3. Encapsulation
- Account-specific data protected within concrete classes
- Transaction history managed internally
- Balance calculations hidden from external access
- Controlled access through getter/setter methods

### 4. Inheritance
- All account types inherit from `BankAccount`
- Shared functionality with specialized behavior
- Code reuse through inherited methods
- Override capabilities for account-specific needs

## 🚀 Account Types & Features

### 💰 Savings Account
- **Interest Rate**: 2.0% APR
- **Transaction Limit**: 6 per month
- **Minimum Balance**: $1,000
- **Features**:
  - Compound interest calculation
  - Low balance fees
  - Interest projections
  - Transaction limit enforcement

### 📝 Checking Account
- **Interest Rate**: 0.1% APR (on balances > $1,000)
- **Monthly Fee**: $2.50
- **Overdraft Protection**: Available
- **Features**:
  - Unlimited transactions (25 free, then $1.00 each)
  - Check writing capability
  - ATM withdrawals
  - Online purchases
  - Overdraft protection up to $500

### 💳 Credit Account
- **Interest Rate**: 18.0% APR
- **Credit Limits**: Customizable
- **Grace Period**: 25 days
- **Features**:
  - Purchase charges
  - Cash advances (with 3% fee)
  - Minimum payment calculation
  - Interest charge simulation
  - Credit utilization tracking
  - Annual fees for premium cards

### 📊 Investment Account
- **Base Return**: 5.0% APR (variable based on market simulation)
- **Management Fee**: 0.75% annually
- **Minimum Balance**: $5,000
- **Features**:
  - Market volatility simulation
  - Risk level selection (Conservative/Moderate/Aggressive)
  - Portfolio rebalancing
  - Dividend reinvestment options
  - Performance reporting
  - Investment purchase tracking

## 💼 Transaction System

### Transaction Types
- **Deposits**: Direct deposits, check deposits, transfers in
- **Withdrawals**: ATM, checks, online purchases, transfers out
- **Fees**: Overdraft, maintenance, low balance, management
- **Interest**: Credits and charges based on account type
- **Specialized**: Wire transfers, cash advances, investment purchases

### Transaction Features
- Unique transaction IDs and references
- Timestamp tracking
- Balance-after recording
- Transaction categorization
- History management
- Limit enforcement

## 🔧 How to Compile and Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command line access

### Compilation
```bash
# Navigate to the project directory
cd "c:\Users\Shivani Mutke\Documents\Covaib-JavaOOP\abstraction\10-bank-account"

# Compile all Java files
javac *.java

# Run the banking demonstration
java BankingDemo
```

### Alternative IDEs
- **IntelliJ IDEA**: Open folder and run BankingDemo.java
- **Eclipse**: Import as Java project and run main method
- **VS Code**: Use Java Extension Pack and run the main class

## 📈 Expected Output

The demonstration will show:

1. **Polymorphism Demo** - Same methods, different behaviors
2. **Account-Specific Features** - Unique capabilities per account type
3. **Monthly Processing** - Interest, fees, and maintenance
4. **Transfer Operations** - Money movement between accounts
5. **Real-World Scenarios** - Complex banking situations:
   - Overdraft protection testing
   - Credit card payment cycles
   - Investment market volatility
   - Transaction limit enforcement

## 🎓 Learning Objectives

After studying this project, you should understand:

- How abstraction simplifies complex financial systems
- Polymorphic behavior in real-world applications
- Inheritance hierarchies in banking domain
- Encapsulation for sensitive financial data
- Enum usage for type-safe banking constants
- Transaction management and audit trails
- Interest calculation algorithms
- Fee structure implementation
- Risk management in different account types

## 🔍 Code Analysis Points

### Abstract vs Concrete Methods
- **Abstract**: `canWithdraw()`, `calculateInterest()`, `applyMonthlyFees()`
- **Concrete**: `deposit()`, `withdraw()`, `transfer()`, `printStatement()`

### Account Differentiation
- **Savings**: Focus on growth with restrictions
- **Checking**: Everyday banking with flexibility
- **Credit**: Borrowing with interest charges
- **Investment**: Growth with market risk

### Design Patterns Used
- **Template Method**: BankAccount defines transaction flow
- **Strategy Pattern**: Different interest calculation strategies
- **Factory Pattern**: Could be extended for account creation
- **Observer Pattern**: Transaction history tracking

## 🚀 Extension Ideas

1. Add more account types (Business, Student, Joint accounts)
2. Implement user authentication and security
3. Add loan and mortgage account types
4. Create a GUI banking interface
5. Add real-time interest rate feeds
6. Implement fraud detection algorithms
7. Add mobile banking features
8. Create account statements and reports
9. Add multi-currency support
10. Implement regulatory compliance features

## 💡 Advanced Features Demonstrated

### Interest Calculations
- Simple interest for checking accounts
- Compound interest for savings accounts
- Variable returns for investment accounts
- Interest charges for credit accounts

### Fee Management
- Monthly maintenance fees
- Transaction-based fees
- Overdraft penalties
- Management fees for investments
- Low balance charges

### Risk Management
- Overdraft limits and protection
- Credit limits and utilization
- Investment risk levels
- Transaction limits and monitoring

### Audit and Compliance
- Complete transaction history
- Reference number generation
- Timestamp tracking
- Balance verification
- Monthly statement generation

---

*This project demonstrates enterprise-level Java abstraction concepts through a practical banking system that mirrors real-world financial institutions' account management systems.*

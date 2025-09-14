# 💳 Payment Gateway System - Abstract Base + Real Polymorphism

## Problem Statement
Create a payment gateway system with different payment methods. Implement inheritance with base class `Payment` and subclasses `CreditCardPayment`, `UPIPayment`, `PayPalPayment`, and `CryptoPayment`. Each payment method should override the abstract methods `processPayment()`, `calculateProcessingFee()`, `getPaymentFeatures()`, and `validatePayment()` with their specific business logic and retry policies.

## Learning Objectives
- **Abstract Base + Real Polymorphism**: Different payment methods with different processing logic
- **Method Overriding**: Each payment method has different fee structures and validation rules
- **Real-world Business Logic**: Payment processing, fraud detection, retry policies
- **Complex Inheritance**: Multiple payment types with shared and specialized behavior

## Class Hierarchy

```
Payment (Abstract)
├── CreditCardPayment
├── UPIPayment
├── PayPalPayment
└── CryptoPayment
```

## Key Concepts Demonstrated

### 1. Abstract Base + Real Polymorphism
- **Payment**: Abstract base class with common payment functionality
- **CreditCardPayment**: Card processing with fraud detection and 3D Secure
- **UPIPayment**: UPI-based payments with bank integration
- **PayPalPayment**: PayPal integration with international support
- **CryptoPayment**: Cryptocurrency payments with blockchain integration

### 2. Method Overriding
- Each payment method provides its own implementation of abstract methods
- Different processing logic for each payment type
- Different fee structures and validation rules

### 3. Real-world Business Logic
- Payment processing with retry policies
- Fraud detection and risk assessment
- Fee calculation based on payment method
- Transaction tracking and status management

## Code Structure

### Payment.java (Abstract Base Class)
```java
public abstract class Payment {
    protected String paymentId;
    protected String customerId;
    protected double amount;
    protected String currency;
    protected String status;
    protected String paymentMethod;
    protected double processingFee;
    protected String transactionId;
    protected boolean isProcessed;
    protected String failureReason;
    protected int retryCount;
    protected int maxRetries;
    
    // Abstract methods - must be implemented by subclasses
    public abstract boolean processPayment();
    public abstract double calculateProcessingFee();
    public abstract String getPaymentFeatures();
    public abstract boolean validatePayment();
    
    // Concrete methods - shared by all payments
    public boolean retryPayment() { ... }
    public boolean updateStatus(String newStatus, String transactionId, String failureReason) { ... }
    public boolean isSuccessful() { ... }
    public boolean canRetry() { ... }
}
```

### CreditCardPayment.java (Concrete Subclass)
```java
public class CreditCardPayment extends Payment {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    private String cardType;
    private String bankName;
    private boolean isInternational;
    private double exchangeRate;
    private String billingAddress;
    private String zipCode;
    private String country;
    private boolean isVerified;
    private String verificationMethod;
    private String riskScore;
    private boolean isHighRisk;
    private String fraudCheckStatus;
    
    @Override
    public boolean processPayment() {
        // Credit card specific processing logic
        // 1. Validate payment
        // 2. Perform fraud check
        // 3. Verify card
        // 4. Calculate processing fee
        // 5. Process payment
    }
    
    @Override
    public double calculateProcessingFee() {
        // Credit card specific fee calculation
        // Base fee based on card type
        // International fee
        // High-risk fee
        // Minimum fee
    }
    
    @Override
    public String getPaymentFeatures() {
        return "Credit Card Features: Card Type: " + cardType + 
               ", Bank: " + bankName + ", International: " + isInternational + 
               ", Risk Score: " + riskScore + ", Verified: " + isVerified;
    }
    
    @Override
    public boolean validatePayment() {
        // Credit card specific validation
        // Card number format
        // Expiry date format
        // CVV format
        // Card holder name
        // Billing address
    }
}
```

### UPIPayment.java (Concrete Subclass)
```java
public class UPIPayment extends Payment {
    private String upiId;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String pin;
    private boolean isVerified;
    private String verificationMethod;
    
    @Override
    public boolean processPayment() {
        // UPI specific processing logic
        // 1. Validate UPI ID
        // 2. Verify bank details
        // 3. Process UPI payment
    }
    
    @Override
    public double calculateProcessingFee() {
        // UPI specific fee calculation
        // 0.5% or minimum fee
    }
    
    @Override
    public String getPaymentFeatures() {
        return "UPI Features: UPI ID: " + upiId + ", Bank: " + bankName + 
               ", Account: " + accountNumber + ", IFSC: " + ifscCode;
    }
    
    @Override
    public boolean validatePayment() {
        // UPI specific validation
        // UPI ID format
        // Bank name
        // Account number
        // IFSC code
    }
}
```

### PayPalPayment.java (Concrete Subclass)
```java
public class PayPalPayment extends Payment {
    private String paypalEmail;
    private String paypalId;
    private String country;
    private String currency;
    private boolean isVerified;
    private String verificationStatus;
    
    @Override
    public boolean processPayment() {
        // PayPal specific processing logic
        // 1. Validate PayPal account
        // 2. Process PayPal payment
    }
    
    @Override
    public double calculateProcessingFee() {
        // PayPal specific fee calculation
        // 3.4% + fixed fee based on country
    }
    
    @Override
    public String getPaymentFeatures() {
        return "PayPal Features: Email: " + paypalEmail + ", Country: " + country + 
               ", Verified: " + isVerified + ", Status: " + verificationStatus;
    }
    
    @Override
    public boolean validatePayment() {
        // PayPal specific validation
        // Email format
        // PayPal ID
        // Country
    }
}
```

### CryptoPayment.java (Concrete Subclass)
```java
public class CryptoPayment extends Payment {
    private String cryptoType;
    private String walletAddress;
    private String privateKey;
    private double cryptoAmount;
    private double exchangeRate;
    private String network;
    private boolean isVerified;
    private String verificationMethod;
    private double gasFee;
    private String transactionHash;
    
    @Override
    public boolean processPayment() {
        // Crypto specific processing logic
        // 1. Validate wallet address
        // 2. Calculate crypto amount
        // 3. Process blockchain transaction
    }
    
    @Override
    public double calculateProcessingFee() {
        // Crypto specific fee calculation
        // Gas fee based on crypto type
        // Network fee
    }
    
    @Override
    public String getPaymentFeatures() {
        return "Crypto Features: Type: " + cryptoType + ", Network: " + network + 
               ", Exchange Rate: " + exchangeRate + ", Gas Fee: " + gasFee;
    }
    
    @Override
    public boolean validatePayment() {
        // Crypto specific validation
        // Crypto type
        // Wallet address format
        // Network
        // Amount
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
java PaymentDemo
```

## Expected Output

```
💳 PAYMENT GATEWAY SYSTEM 💳
==================================================

📋 PAYMENT INFORMATION:
Payment: PAY001, Method: Credit Card, Amount: USD100.00, Status: Pending, Fee: USD0.00
Payment: PAY002, Method: UPI, Amount: INR50.00, Status: Pending, Fee: INR0.00
Payment: PAY003, Method: PayPal, Amount: EUR75.00, Status: Pending, Fee: EUR0.00
Payment: PAY004, Method: Crypto, Amount: USD200.00, Status: Pending, Fee: USD0.00

💳 PAYMENT PROCESSING:
Credit Card Payment:
Processing credit card payment...
Card: 4111 **** **** 1111 (Visa)
Holder: John Doe
Amount: USD100.00
Bank: Chase Bank
Credit card payment validation passed
Performing fraud check...
Fraud check completed. Risk score: 15, Status: Low Risk
Verifying credit card...
Card verification successful using 3D Secure
✅ Credit card payment processed successfully!
Transaction ID: TXN_PAY001_1234567890
Processing fee: USD2.90

UPI Payment:
Processing UPI payment...
UPI ID: john@paytm
Bank: HDFC Bank
Amount: INR50.00
✅ UPI payment processed successfully!

PayPal Payment:
Processing PayPal payment...
PayPal Email: jane@paypal.com
Amount: EUR75.00
✅ PayPal payment processed successfully!

Crypto Payment:
Processing Crypto payment...
Crypto Type: Bitcoin
Amount: 0.004000 Bitcoin
Network: Bitcoin
✅ Crypto payment processed successfully!
Transaction Hash: 0xCRYPTO_PAY004_1234567890

🔧 PAYMENT FEATURES:
Credit Card: Credit Card Features: Card Type: Visa, Bank: Chase Bank, International: No, Exchange Rate: 1.00, Verified: Yes, Risk Score: 15, High Risk: No, Fraud Check: Low Risk, Billing Address: 123 Main St, Country: US
UPI: UPI Features: UPI ID: john@paytm, Bank: HDFC Bank, Account: 1234567890, IFSC: HDFC0001234, Verified: No
PayPal: PayPal Features: Email: jane@paypal.com, Country: UK, Verified: No, Status: Pending
Crypto: Crypto Features: Type: Bitcoin, Network: Bitcoin, Exchange Rate: 50000.00, Gas Fee: 0.000100 Bitcoin, Verified: No

💰 FEE COMPARISON:
Payment Method          Amount          Fee             Total
--------------------------------------------------
Credit Card            USD100.00       USD2.90         USD102.90
UPI                    INR50.00        INR0.50         INR50.50
PayPal                 EUR75.00        EUR2.85         EUR77.85
Crypto                 USD200.00       USD5.00         USD205.00

✨ DEMONSTRATION COMPLETE! ✨
```

## Key Learning Points

1. **Abstract Base + Real Polymorphism**: Different payment methods with different processing logic
2. **Method Overriding**: Each payment method provides its own implementation
3. **Real-world Business Logic**: Payment processing, fraud detection, retry policies
4. **Complex Inheritance**: Multiple payment types with shared and specialized behavior
5. **Polymorphism**: Same method call produces different results based on payment type
6. **Business Rules**: Different fee structures and validation rules for each payment method

## Advanced Concepts Demonstrated

- **Runtime Polymorphism**: Method calls resolved at runtime based on actual object type
- **Abstract Class Design**: Cannot instantiate abstract classes directly
- **Method Overriding vs Overloading**: Different concepts with different purposes
- **Access Modifiers**: Protected fields accessible to subclasses
- **String Formatting**: Professional output formatting
- **Real-world Business Logic**: Payment processing, fraud detection, retry policies
- **Complex Inheritance**: Multiple payment types with shared and specialized behavior

## Design Patterns Used

1. **Template Method Pattern**: Abstract base class defines the structure, subclasses implement specific details
2. **Strategy Pattern**: Different payment processing strategies for different payment types
3. **Polymorphism**: Single interface for different payment types

This example demonstrates the fundamental concepts of inheritance and method overriding in Java, providing a solid foundation for understanding more complex object-oriented programming patterns in real-world applications.

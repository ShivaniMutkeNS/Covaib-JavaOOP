/**
 * Credit Card Payment class extending Payment
 * Demonstrates abstract base + real polymorphism
 * 
 * @author Expert Developer
 * @version 1.0
 */
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
    
    /**
     * Constructor for CreditCardPayment
     * @param paymentId Unique payment identifier
     * @param customerId Customer identifier
     * @param amount Payment amount
     * @param currency Currency code
     * @param description Payment description
     * @param merchantId Merchant identifier
     * @param orderId Order identifier
     * @param cardNumber Credit card number
     * @param cardHolderName Card holder name
     * @param expiryDate Card expiry date
     * @param cvv Card CVV
     * @param billingAddress Billing address
     * @param zipCode ZIP code
     * @param country Country
     */
    public CreditCardPayment(String paymentId, String customerId, double amount, String currency, 
                            String description, String merchantId, String orderId, String cardNumber, 
                            String cardHolderName, String expiryDate, String cvv, String billingAddress, 
                            String zipCode, String country) {
        super(paymentId, customerId, amount, currency, description, "Credit Card", merchantId, orderId);
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.billingAddress = billingAddress;
        this.zipCode = zipCode;
        this.country = country;
        this.isInternational = !country.equals("US");
        this.exchangeRate = isInternational ? 1.2 : 1.0;
        this.isVerified = false;
        this.verificationMethod = "";
        this.riskScore = "0";
        this.isHighRisk = false;
        this.fraudCheckStatus = "Pending";
        
        // Determine card type based on card number
        this.cardType = determineCardType(cardNumber);
        this.bankName = determineBankName(cardNumber);
    }
    
    /**
     * Override processPayment method with credit card processing logic
     * @return True if payment processed successfully
     */
    @Override
    public boolean processPayment() {
        System.out.println("Processing credit card payment...");
        System.out.println("Card: " + maskCardNumber(cardNumber) + " (" + cardType + ")");
        System.out.println("Holder: " + cardHolderName);
        System.out.println("Amount: " + currency + String.format("%.2f", amount));
        System.out.println("Bank: " + bankName);
        
        // Validate payment
        if (!validatePayment()) {
            updateStatus("Failed", "", "Payment validation failed");
            return false;
        }
        
        // Perform fraud check
        if (!performFraudCheck()) {
            updateStatus("Failed", "", "Fraud check failed");
            return false;
        }
        
        // Verify card
        if (!verifyCard()) {
            updateStatus("Failed", "", "Card verification failed");
            return false;
        }
        
        // Calculate processing fee
        processingFee = calculateProcessingFee();
        
        // Simulate payment processing
        try {
            Thread.sleep(2000); // Simulate processing delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate success/failure based on risk score and amount
        boolean success = !isHighRisk && amount <= 10000.0;
        
        if (success) {
            String transactionId = "TXN_" + paymentId + "_" + System.currentTimeMillis();
            updateStatus("Completed", transactionId, "");
            System.out.println("✅ Credit card payment processed successfully!");
            System.out.println("Transaction ID: " + transactionId);
            System.out.println("Processing fee: " + currency + String.format("%.2f", processingFee));
        } else {
            String reason = isHighRisk ? "High risk transaction" : "Amount exceeds limit";
            updateStatus("Failed", "", reason);
            System.out.println("❌ Credit card payment failed: " + reason);
        }
        
        return success;
    }
    
    /**
     * Override calculateProcessingFee method with credit card fee structure
     * @return The calculated processing fee
     */
    @Override
    public double calculateProcessingFee() {
        double baseFee = 0.0;
        
        // Base fee based on card type
        switch (cardType.toLowerCase()) {
            case "visa":
            case "mastercard":
                baseFee = amount * 0.029; // 2.9%
                break;
            case "amex":
                baseFee = amount * 0.035; // 3.5%
                break;
            case "discover":
                baseFee = amount * 0.032; // 3.2%
                break;
            default:
                baseFee = amount * 0.030; // 3.0%
        }
        
        // Add international fee
        if (isInternational) {
            baseFee += amount * 0.01; // 1% international fee
        }
        
        // Add high-risk fee
        if (isHighRisk) {
            baseFee += amount * 0.02; // 2% high-risk fee
        }
        
        // Minimum fee
        baseFee = Math.max(baseFee, 0.30);
        
        return Math.round(baseFee * 100.0) / 100.0; // Round to 2 decimal places
    }
    
    /**
     * Override getPaymentFeatures method with credit card features
     * @return String description of credit card features
     */
    @Override
    public String getPaymentFeatures() {
        return "Credit Card Features: " +
               "Card Type: " + cardType + ", " +
               "Bank: " + bankName + ", " +
               "International: " + (isInternational ? "Yes" : "No") + ", " +
               "Exchange Rate: " + String.format("%.2f", exchangeRate) + ", " +
               "Verified: " + (isVerified ? "Yes" : "No") + ", " +
               "Risk Score: " + riskScore + ", " +
               "High Risk: " + (isHighRisk ? "Yes" : "No") + ", " +
               "Fraud Check: " + fraudCheckStatus + ", " +
               "Billing Address: " + billingAddress + ", " +
               "Country: " + country;
    }
    
    /**
     * Override validatePayment method with credit card validation rules
     * @return True if payment is valid
     */
    @Override
    public boolean validatePayment() {
        // Validate card number
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            System.out.println("Error: Invalid card number length");
            return false;
        }
        
        // Validate expiry date
        if (expiryDate == null || !expiryDate.matches("\\d{2}/\\d{2}")) {
            System.out.println("Error: Invalid expiry date format (MM/YY)");
            return false;
        }
        
        // Validate CVV
        if (cvv == null || !cvv.matches("\\d{3,4}")) {
            System.out.println("Error: Invalid CVV format");
            return false;
        }
        
        // Validate card holder name
        if (cardHolderName == null || cardHolderName.trim().isEmpty()) {
            System.out.println("Error: Card holder name cannot be empty");
            return false;
        }
        
        // Validate amount
        if (amount <= 0) {
            System.out.println("Error: Payment amount must be positive");
            return false;
        }
        
        // Validate billing address
        if (billingAddress == null || billingAddress.trim().isEmpty()) {
            System.out.println("Error: Billing address cannot be empty");
            return false;
        }
        
        System.out.println("Credit card payment validation passed");
        return true;
    }
    
    /**
     * Credit card specific method to perform fraud check
     * @return True if fraud check passed
     */
    private boolean performFraudCheck() {
        System.out.println("Performing fraud check...");
        
        // Simulate fraud check
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Calculate risk score based on various factors
        int riskScore = 0;
        
        // Amount-based risk
        if (amount > 5000) riskScore += 30;
        if (amount > 10000) riskScore += 50;
        
        // International transaction risk
        if (isInternational) riskScore += 20;
        
        // Card type risk
        if (cardType.equals("Amex")) riskScore += 10;
        
        // Time-based risk (simplified)
        if (java.time.LocalTime.now().getHour() < 6 || java.time.LocalTime.now().getHour() > 22) {
            riskScore += 15;
        }
        
        this.riskScore = String.valueOf(riskScore);
        this.isHighRisk = riskScore > 50;
        this.fraudCheckStatus = isHighRisk ? "High Risk" : "Low Risk";
        
        System.out.println("Fraud check completed. Risk score: " + riskScore + ", Status: " + fraudCheckStatus);
        
        return !isHighRisk;
    }
    
    /**
     * Credit card specific method to verify card
     * @return True if card verification passed
     */
    private boolean verifyCard() {
        System.out.println("Verifying credit card...");
        
        // Simulate card verification
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate verification success/failure
        boolean verified = !cardNumber.endsWith("0") && !cardNumber.endsWith("1");
        
        if (verified) {
            this.isVerified = true;
            this.verificationMethod = "3D Secure";
            System.out.println("Card verification successful using " + verificationMethod);
        } else {
            System.out.println("Card verification failed");
        }
        
        return verified;
    }
    
    /**
     * Credit card specific method to determine card type
     * @param cardNumber Card number
     * @return Card type
     */
    private String determineCardType(String cardNumber) {
        if (cardNumber.startsWith("4")) return "Visa";
        if (cardNumber.startsWith("5")) return "Mastercard";
        if (cardNumber.startsWith("3")) return "Amex";
        if (cardNumber.startsWith("6")) return "Discover";
        return "Unknown";
    }
    
    /**
     * Credit card specific method to determine bank name
     * @param cardNumber Card number
     * @return Bank name
     */
    private String determineBankName(String cardNumber) {
        // Simplified bank determination
        if (cardNumber.startsWith("4")) return "Chase Bank";
        if (cardNumber.startsWith("5")) return "Bank of America";
        if (cardNumber.startsWith("3")) return "American Express";
        if (cardNumber.startsWith("6")) return "Discover Bank";
        return "Unknown Bank";
    }
    
    /**
     * Credit card specific method to mask card number
     * @param cardNumber Card number
     * @return Masked card number
     */
    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() < 8) return "****";
        return cardNumber.substring(0, 4) + " **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
    
    /**
     * Credit card specific method to get card details
     * @return String with card details
     */
    public String getCardDetails() {
        return String.format("Card Details: %s (%s) - %s, Expires: %s, Billing: %s, %s", 
                           maskCardNumber(cardNumber), cardType, cardHolderName, expiryDate, billingAddress, country);
    }
    
    /**
     * Credit card specific method to check if card is expired
     * @return True if card is expired
     */
    public boolean isCardExpired() {
        try {
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000;
            
            java.time.LocalDate expiry = java.time.LocalDate.of(year, month, 1);
            java.time.LocalDate now = java.time.LocalDate.now();
            
            return expiry.isBefore(now);
        } catch (Exception e) {
            return true; // Assume expired if parsing fails
        }
    }
    
    /**
     * Getter for card number
     * @return The card number
     */
    public String getCardNumber() {
        return cardNumber;
    }
    
    /**
     * Getter for card holder name
     * @return The card holder name
     */
    public String getCardHolderName() {
        return cardHolderName;
    }
    
    /**
     * Getter for expiry date
     * @return The expiry date
     */
    public String getExpiryDate() {
        return expiryDate;
    }
    
    /**
     * Getter for CVV
     * @return The CVV
     */
    public String getCvv() {
        return cvv;
    }
    
    /**
     * Getter for card type
     * @return The card type
     */
    public String getCardType() {
        return cardType;
    }
    
    /**
     * Getter for bank name
     * @return The bank name
     */
    public String getBankName() {
        return bankName;
    }
    
    /**
     * Getter for international status
     * @return True if card is international
     */
    public boolean isInternational() {
        return isInternational;
    }
    
    /**
     * Getter for exchange rate
     * @return The exchange rate
     */
    public double getExchangeRate() {
        return exchangeRate;
    }
    
    /**
     * Getter for billing address
     * @return The billing address
     */
    public String getBillingAddress() {
        return billingAddress;
    }
    
    /**
     * Getter for ZIP code
     * @return The ZIP code
     */
    public String getZipCode() {
        return zipCode;
    }
    
    /**
     * Getter for country
     * @return The country
     */
    public String getCountry() {
        return country;
    }
    
    /**
     * Getter for verified status
     * @return True if card is verified
     */
    public boolean isVerified() {
        return isVerified;
    }
    
    /**
     * Getter for verification method
     * @return The verification method
     */
    public String getVerificationMethod() {
        return verificationMethod;
    }
    
    /**
     * Getter for risk score
     * @return The risk score
     */
    public String getRiskScore() {
        return riskScore;
    }
    
    /**
     * Getter for high risk status
     * @return True if transaction is high risk
     */
    public boolean isHighRisk() {
        return isHighRisk;
    }
    
    /**
     * Getter for fraud check status
     * @return The fraud check status
     */
    public String getFraudCheckStatus() {
        return fraudCheckStatus;
    }
    
    /**
     * Override toString to include credit card specific details
     * @return String representation of the credit card payment
     */
    @Override
    public String toString() {
        return super.toString() + " [Card: " + maskCardNumber(cardNumber) + ", Type: " + cardType + ", Risk: " + riskScore + "]";
    }
}

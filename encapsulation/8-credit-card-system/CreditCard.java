
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Credit Card System
 * 
 * This class demonstrates encapsulation by:
 * 1. Encapsulating card number, CVV
 * 2. Providing masked getter (e.g., **** **** **** 1234)
 * 3. Full details accessible only to PaymentProcessor
 * 4. Preventing external modification by design
 */
public class CreditCard {
    // Encapsulated card details
    private final String cardNumber;
    private final String cvv;
    private final String cardholderName;
    private final LocalDate expiryDate;
    private final String cardType;
    
    // Security and validation
    private final CardValidator validator;
    private final MaskingService maskingService;
    
    /**
     * Constructor
     */
    public CreditCard(String cardNumber, String cvv, String cardholderName, LocalDate expiryDate) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.cardholderName = cardholderName;
        this.expiryDate = expiryDate;
        this.cardType = determineCardType(cardNumber);
        this.validator = new CardValidator();
        this.maskingService = new MaskingService();
        
        // Validate card details
        if (!validator.isValidCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Invalid card number");
        }
        if (!validator.isValidCVV(cvv)) {
            throw new IllegalArgumentException("Invalid CVV");
        }
        if (cardholderName == null || cardholderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Cardholder name cannot be empty");
        }
        if (expiryDate == null || expiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid or expired card");
        }
    }
    
    /**
     * Get masked card number for display
     * @return Masked card number (e.g., **** **** **** 1234)
     */
    public String getMaskedCardNumber() {
        return maskingService.maskCardNumber(cardNumber);
    }
    
    /**
     * Get masked CVV for display
     * @return Masked CVV (e.g., ***)
     */
    public String getMaskedCVV() {
        return maskingService.maskCVV();
    }
    
    /**
     * Get cardholder name
     * @return Cardholder name
     */
    public String getCardholderName() {
        return cardholderName;
    }
    
    /**
     * Get expiry date
     * @return Expiry date
     */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    /**
     * Get card type
     * @return Card type (VISA, MASTERCARD, etc.)
     */
    public String getCardType() {
        return cardType;
    }
    
    /**
     * Check if card is expired
     * @return true if card is expired
     */
    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }
    
    /**
     * Check if card is valid
     * @return true if card is valid
     */
    public boolean isValid() {
        return validator.isValidCardNumber(cardNumber) && 
               validator.isValidCVV(cvv) && 
               !isExpired() &&
               cardholderName != null && !cardholderName.trim().isEmpty();
    }
    
    /**
     * Get last 4 digits of card number
     * @return Last 4 digits
     */
    public String getLastFourDigits() {
        if (cardNumber.length() >= 4) {
            return cardNumber.substring(cardNumber.length() - 4);
        }
        return "****";
    }
    
    /**
     * Get formatted expiry date
     * @return Formatted expiry date (MM/YY)
     */
    public String getFormattedExpiryDate() {
        return expiryDate.format(DateTimeFormatter.ofPattern("MM/yy"));
    }
    
    /**
     * Get card summary for display
     * @return Card summary
     */
    public String getCardSummary() {
        return String.format("%s ending in %s (expires %s)", 
            cardType, getLastFourDigits(), getFormattedExpiryDate());
    }
    
    /**
     * Get full card details (only for PaymentProcessor)
     * @param processor PaymentProcessor instance
     * @return Full card details or null if not authorized
     */
    public FullCardDetails getFullDetails(PaymentProcessor processor) {
        if (processor == null || !processor.isAuthorized()) {
            return null;
        }
        
        return new FullCardDetails(cardNumber, cvv, cardholderName, expiryDate, cardType);
    }
    
    /**
     * Validate card for payment
     * @param processor PaymentProcessor instance
     * @return true if card is valid for payment
     */
    public boolean validateForPayment(PaymentProcessor processor) {
        if (processor == null || !processor.isAuthorized()) {
            return false;
        }
        
        return isValid() && !isExpired();
    }
    
    /**
     * Process payment
     * @param amount Payment amount
     * @param processor PaymentProcessor instance
     * @return Payment result
     */
    public PaymentResult processPayment(double amount, PaymentProcessor processor) {
        if (processor == null || !processor.isAuthorized()) {
            return new PaymentResult(false, "Unauthorized processor");
        }
        
        if (!isValid()) {
            return new PaymentResult(false, "Invalid card");
        }
        
        if (isExpired()) {
            return new PaymentResult(false, "Card expired");
        }
        
        if (amount <= 0) {
            return new PaymentResult(false, "Invalid amount");
        }
        
        // Simulate payment processing
        return processor.processPayment(this, amount);
    }
    
    /**
     * Determine card type from card number
     * @param cardNumber Card number
     * @return Card type
     */
    private String determineCardType(String cardNumber) {
        if (cardNumber.startsWith("4")) {
            return "VISA";
        } else if (cardNumber.startsWith("5") || cardNumber.startsWith("2")) {
            return "MASTERCARD";
        } else if (cardNumber.startsWith("3")) {
            return "AMEX";
        } else if (cardNumber.startsWith("6")) {
            return "DISCOVER";
        } else {
            return "UNKNOWN";
        }
    }
    
    /**
     * Card validator for validation logic
     */
    private static class CardValidator {
        public boolean isValidCardNumber(String cardNumber) {
            if (cardNumber == null || cardNumber.trim().isEmpty()) {
                return false;
            }
            
            // Remove spaces and non-digits
            String cleanNumber = cardNumber.replaceAll("[^0-9]", "");
            
            // Check length
            if (cleanNumber.length() < 13 || cleanNumber.length() > 19) {
                return false;
            }
            
            // Luhn algorithm validation
            return isValidLuhn(cleanNumber);
        }
        
        public boolean isValidCVV(String cvv) {
            if (cvv == null || cvv.trim().isEmpty()) {
                return false;
            }
            
            String cleanCVV = cvv.trim();
            return cleanCVV.matches("^[0-9]{3,4}$");
        }
        
        private boolean isValidLuhn(String cardNumber) {
            int sum = 0;
            boolean alternate = false;
            
            for (int i = cardNumber.length() - 1; i >= 0; i--) {
                int digit = Character.getNumericValue(cardNumber.charAt(i));
                
                if (alternate) {
                    digit *= 2;
                    if (digit > 9) {
                        digit = (digit % 10) + 1;
                    }
                }
                
                sum += digit;
                alternate = !alternate;
            }
            
            return (sum % 10) == 0;
        }
    }
    
    /**
     * Masking service for hiding sensitive data
     */
    private static class MaskingService {
        public String maskCardNumber(String cardNumber) {
            if (cardNumber == null || cardNumber.length() < 4) {
                return "****";
            }
            
            String cleanNumber = cardNumber.replaceAll("[^0-9]", "");
            if (cleanNumber.length() < 4) {
                return "****";
            }
            
            String lastFour = cleanNumber.substring(cleanNumber.length() - 4);
            return "**** **** **** " + lastFour;
        }
        
        public String maskCVV() {
            return "***";
        }
    }
    
    /**
     * Full card details (only accessible by PaymentProcessor)
     */
    public static class FullCardDetails {
        private final String cardNumber;
        private final String cvv;
        private final String cardholderName;
        private final LocalDate expiryDate;
        private final String cardType;
        
        public FullCardDetails(String cardNumber, String cvv, String cardholderName, 
                             LocalDate expiryDate, String cardType) {
            this.cardNumber = cardNumber;
            this.cvv = cvv;
            this.cardholderName = cardholderName;
            this.expiryDate = expiryDate;
            this.cardType = cardType;
        }
        
        public String getCardNumber() { return cardNumber; }
        public String getCVV() { return cvv; }
        public String getCardholderName() { return cardholderName; }
        public LocalDate getExpiryDate() { return expiryDate; }
        public String getCardType() { return cardType; }
        
        @Override
        public String toString() {
            return String.format("FullCardDetails{type=%s, number=%s, cvv=%s, name=%s, expiry=%s}", 
                cardType, cardNumber, cvv, cardholderName, expiryDate);
        }
    }
    
    /**
     * Payment result
     */
    public static class PaymentResult {
        private final boolean success;
        private final String message;
        private final String transactionId;
        
        public PaymentResult(boolean success, String message) {
            this.success = success;
            this.message = message;
            this.transactionId = success ? generateTransactionId() : null;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getTransactionId() { return transactionId; }
        
        private String generateTransactionId() {
            return "TXN" + System.currentTimeMillis();
        }
        
        @Override
        public String toString() {
            return String.format("PaymentResult{success=%s, message='%s', transactionId='%s'}", 
                success, message, transactionId);
        }
    }
    
    /**
     * Payment processor with authorization
     */
    public static class PaymentProcessor {
        private final String processorId;
        private final String authorizationKey;
        private final boolean authorized;
        
        public PaymentProcessor(String processorId, String authorizationKey) {
            this.processorId = processorId;
            this.authorizationKey = authorizationKey;
            this.authorized = validateAuthorization(processorId, authorizationKey);
        }
        
        private boolean validateAuthorization(String processorId, String authorizationKey) {
            return processorId != null && 
                   !processorId.trim().isEmpty() && 
                   "PAYMENT_AUTH_2024".equals(authorizationKey);
        }
        
        public boolean isAuthorized() {
            return authorized;
        }
        
        public String getProcessorId() {
            return processorId;
        }
        
        public String getAuthorizationKey() {
            return authorizationKey;
        }
        
        public PaymentResult processPayment(CreditCard card, double amount) {
            if (!isAuthorized()) {
                return new PaymentResult(false, "Unauthorized processor");
            }
            
            // Simulate payment processing
            try {
                Thread.sleep(100); // Simulate processing time
                return new PaymentResult(true, "Payment processed successfully");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new PaymentResult(false, "Payment processing interrupted");
            }
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CreditCard that = (CreditCard) obj;
        return Objects.equals(cardNumber, that.cardNumber) &&
               Objects.equals(cvv, that.cvv) &&
               Objects.equals(cardholderName, that.cardholderName) &&
               Objects.equals(expiryDate, that.expiryDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, cvv, cardholderName, expiryDate);
    }
    
    @Override
    public String toString() {
        return String.format("CreditCard{type=%s, masked=%s, name=%s, expiry=%s}", 
            cardType, getMaskedCardNumber(), cardholderName, getFormattedExpiryDate());
    }
}

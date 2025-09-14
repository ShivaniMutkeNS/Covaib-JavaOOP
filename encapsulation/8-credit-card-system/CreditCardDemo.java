
import java.time.LocalDate;

/**
 * Demo class to demonstrate Credit Card System
 */
public class CreditCardDemo {
    public static void main(String[] args) {
        System.out.println("=== Credit Card System Demo ===\n");
        
        // Test valid credit card
        testValidCreditCard();
        
        // Test invalid credit card
        testInvalidCreditCard();
        
        // Test payment processing
        testPaymentProcessing();
        
        // Test unauthorized access
        testUnauthorizedAccess();
        
        // Test card validation
        testCardValidation();
    }
    
    private static void testValidCreditCard() {
        System.out.println("=== Valid Credit Card Test ===");
        
        try {
            CreditCard card = new CreditCard(
                "4532015112830366", // Valid VISA card number
                "123",
                "John Doe",
                LocalDate.now().plusYears(2)
            );
            
            System.out.println("Card created successfully: " + card);
            System.out.println("Card type: " + card.getCardType());
            System.out.println("Masked number: " + card.getMaskedCardNumber());
            System.out.println("Masked CVV: " + card.getMaskedCVV());
            System.out.println("Last 4 digits: " + card.getLastFourDigits());
            System.out.println("Expiry date: " + card.getFormattedExpiryDate());
            System.out.println("Card summary: " + card.getCardSummary());
            System.out.println("Is valid: " + card.isValid());
            System.out.println("Is expired: " + card.isExpired());
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating card: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void testInvalidCreditCard() {
        System.out.println("=== Invalid Credit Card Test ===");
        
        // Test invalid card number
        try {
            CreditCard card = new CreditCard("1234567890123456", "123", "Jane Doe", LocalDate.now().plusYears(1));
            System.out.println("ERROR: Should not create invalid card");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly rejected invalid card number: " + e.getMessage());
        }
        
        // Test invalid CVV
        try {
            CreditCard card = new CreditCard("4532015112830366", "12", "Jane Doe", LocalDate.now().plusYears(1));
            System.out.println("ERROR: Should not create invalid CVV");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly rejected invalid CVV: " + e.getMessage());
        }
        
        // Test expired card
        try {
            CreditCard card = new CreditCard("4532015112830366", "123", "Jane Doe", LocalDate.now().minusDays(1));
            System.out.println("ERROR: Should not create expired card");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly rejected expired card: " + e.getMessage());
        }
        
        // Test empty cardholder name
        try {
            CreditCard card = new CreditCard("4532015112830366", "123", "", LocalDate.now().plusYears(1));
            System.out.println("ERROR: Should not create card with empty name");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly rejected empty cardholder name: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void testPaymentProcessing() {
        System.out.println("=== Payment Processing Test ===");
        
        CreditCard card = new CreditCard(
            "4532015112830366",
            "123",
            "John Doe",
            LocalDate.now().plusYears(2)
        );
        
        // Create authorized payment processor
        CreditCard.PaymentProcessor processor = new CreditCard.PaymentProcessor("PROC001", "PAYMENT_AUTH_2024");
        System.out.println("Processor created: " + processor.getProcessorId());
        System.out.println("Is authorized: " + processor.isAuthorized());
        
        // Test payment processing
        CreditCard.PaymentResult result1 = card.processPayment(100.50, processor);
        System.out.println("Payment result: " + result1);
        
        // Test payment with invalid amount
        CreditCard.PaymentResult result2 = card.processPayment(-50.0, processor);
        System.out.println("Invalid amount payment: " + result2);
        
        // Test payment with zero amount
        CreditCard.PaymentResult result3 = card.processPayment(0.0, processor);
        System.out.println("Zero amount payment: " + result3);
        
        System.out.println();
    }
    
    private static void testUnauthorizedAccess() {
        System.out.println("=== Unauthorized Access Test ===");
        
        CreditCard card = new CreditCard(
            "4532015112830366",
            "123",
            "John Doe",
            LocalDate.now().plusYears(2)
        );
        
        // Test unauthorized processor
        CreditCard.PaymentProcessor unauthorizedProcessor = new CreditCard.PaymentProcessor("PROC002", "WRONG_AUTH");
        System.out.println("Unauthorized processor: " + unauthorizedProcessor.getProcessorId());
        System.out.println("Is authorized: " + unauthorizedProcessor.isAuthorized());
        
        // Try to get full details with unauthorized processor
        CreditCard.FullCardDetails details = card.getFullDetails(unauthorizedProcessor);
        System.out.println("Full details with unauthorized processor: " + (details != null ? "SUCCESS" : "ACCESS DENIED"));
        
        // Try to process payment with unauthorized processor
        CreditCard.PaymentResult result = card.processPayment(100.0, unauthorizedProcessor);
        System.out.println("Payment with unauthorized processor: " + result);
        
        // Try to get full details with null processor
        CreditCard.FullCardDetails details2 = card.getFullDetails(null);
        System.out.println("Full details with null processor: " + (details2 != null ? "SUCCESS" : "ACCESS DENIED"));
        
        System.out.println();
    }
    
    private static void testCardValidation() {
        System.out.println("=== Card Validation Test ===");
        
        // Test different card types
        String[] cardNumbers = {
            "4532015112830366", // VISA
            "5555555555554444", // MASTERCARD
            "378282246310005",  // AMEX
            "6011111111111117"  // DISCOVER
        };
        
        String[] cardTypes = {"VISA", "MASTERCARD", "AMEX", "DISCOVER"};
        
        for (int i = 0; i < cardNumbers.length; i++) {
            try {
                CreditCard card = new CreditCard(
                    cardNumbers[i],
                    "123",
                    "Test User",
                    LocalDate.now().plusYears(1)
                );
                System.out.println("Card " + (i + 1) + ": " + card.getCardType() + " - " + card.getMaskedCardNumber());
            } catch (IllegalArgumentException e) {
                System.out.println("Card " + (i + 1) + " (" + cardTypes[i] + "): " + e.getMessage());
            }
        }
        
        // Test Luhn algorithm validation
        System.out.println("\nLuhn Algorithm Test:");
        String[] testNumbers = {
            "4532015112830366", // Valid
            "1234567890123456", // Invalid
            "4111111111111111", // Valid
            "5555555555554444"  // Valid
        };
        
        for (String number : testNumbers) {
            try {
                CreditCard card = new CreditCard(number, "123", "Test User", LocalDate.now().plusYears(1));
                System.out.println(number + ": VALID");
            } catch (IllegalArgumentException e) {
                System.out.println(number + ": INVALID - " + e.getMessage());
            }
        }
        
        System.out.println();
    }
    
    /**
     * Test card expiration scenarios
     */
    private static void testCardExpiration() {
        System.out.println("=== Card Expiration Test ===");
        
        // Test expired card
        CreditCard expiredCard = new CreditCard(
            "4532015112830366",
            "123",
            "John Doe",
            LocalDate.now().minusDays(1)
        );
        
        System.out.println("Expired card: " + expiredCard);
        System.out.println("Is expired: " + expiredCard.isExpired());
        System.out.println("Is valid: " + expiredCard.isValid());
        
        // Test card expiring soon
        CreditCard expiringSoonCard = new CreditCard(
            "4532015112830366",
            "123",
            "John Doe",
            LocalDate.now().plusDays(30)
        );
        
        System.out.println("Expiring soon card: " + expiringSoonCard);
        System.out.println("Is expired: " + expiringSoonCard.isExpired());
        System.out.println("Is valid: " + expiringSoonCard.isValid());
        
        System.out.println();
    }
    
    /**
     * Test full card details access
     */
    private static void testFullCardDetails() {
        System.out.println("=== Full Card Details Test ===");
        
        CreditCard card = new CreditCard(
            "4532015112830366",
            "123",
            "John Doe",
            LocalDate.now().plusYears(2)
        );
        
        // Create authorized processor
        CreditCard.PaymentProcessor processor = new CreditCard.PaymentProcessor("PROC001", "PAYMENT_AUTH_2024");
        
        // Get full details
        CreditCard.FullCardDetails details = card.getFullDetails(processor);
        if (details != null) {
            System.out.println("Full card details:");
            System.out.println("Card Number: " + details.getCardNumber());
            System.out.println("CVV: " + details.getCVV());
            System.out.println("Cardholder: " + details.getCardholderName());
            System.out.println("Expiry: " + details.getExpiryDate());
            System.out.println("Type: " + details.getCardType());
        } else {
            System.out.println("Access denied to full card details");
        }
        
        System.out.println();
    }
}

package abstraction.paymentprocessor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class showcasing polymorphic usage of different payment processors
 * Demonstrates how client code remains unchanged regardless of processor implementation
 */
public class PaymentProcessorDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Payment Processor Abstraction Demo ===\n");
        
        // Create different payment processors
        PaymentProcessor[] processors = createPaymentProcessors();
        
        // Create sample payment request
        PaymentRequest paymentRequest = createSamplePaymentRequest();
        
        // Test each processor polymorphically
        for (PaymentProcessor processor : processors) {
            System.out.println("Testing processor: " + processor.getClass().getSimpleName());
            System.out.println("Processor ID: " + processor.getProcessorId());
            
            try {
                // Create appropriate credentials for each processor
                AuthCredentials credentials = createCredentialsForProcessor(processor);
                
                // Process payment using template method (same for all processors)
                PaymentResult result = processor.processPaymentWorkflow(paymentRequest, credentials);
                
                // Display result
                displayPaymentResult(result);
                
                // Test refund if payment was successful
                if (result.isSuccess()) {
                    testRefund(processor, result);
                }
                
            } catch (Exception e) {
                System.err.println("Error processing payment: " + e.getMessage());
            }
            
            System.out.println("-".repeat(50));
        }
        
        System.out.println("\n=== Demo completed ===");
    }
    
    private static PaymentProcessor[] createPaymentProcessors() {
        // Stripe configuration
        Map<String, Object> stripeConfig = new HashMap<>();
        stripeConfig.put("webhook_secret", "whsec_test_secret");
        
        // Razorpay configuration
        Map<String, Object> razorpayConfig = new HashMap<>();
        razorpayConfig.put("key_id", "rzp_test_key");
        razorpayConfig.put("key_secret", "rzp_test_secret");
        razorpayConfig.put("requires_otp", true);
        
        // Crypto configuration
        Map<String, Object> cryptoConfig = new HashMap<>();
        cryptoConfig.put("wallet_address", "0x742d35Cc6634C0532925a3b8D404d3aAB8c3f1e");
        cryptoConfig.put("private_key", "a1b2c3d4e5f6789012345678901234567890abcdef1234567890abcdef123456");
        cryptoConfig.put("network", "ETHEREUM");
        cryptoConfig.put("multi_sig", false);
        
        return new PaymentProcessor[] {
            new StripeProcessor("stripe_001", stripeConfig),
            new RazorpayProcessor("razorpay_001", razorpayConfig),
            new CryptoProcessor("crypto_001", cryptoConfig)
        };
    }
    
    private static PaymentRequest createSamplePaymentRequest() {
        Map<String, String> cardDetails = new HashMap<>();
        cardDetails.put("card_number", "4111111111111111");
        cardDetails.put("expiry", "12/25");
        cardDetails.put("cvv", "123");
        
        PaymentMethod paymentMethod = new PaymentMethod("CARD", cardDetails);
        
        return new PaymentRequest(
            "req_" + System.currentTimeMillis(),
            new BigDecimal("100.00"),
            "USD",
            "merchant_123",
            "customer_456",
            paymentMethod
        );
    }
    
    private static AuthCredentials createCredentialsForProcessor(PaymentProcessor processor) {
        if (processor instanceof StripeProcessor) {
            return new AuthCredentials("sk_test_1234567890abcdef");
        } else if (processor instanceof RazorpayProcessor) {
            // Test multi-factor auth for Razorpay
            Map<String, String> additionalParams = new HashMap<>();
            additionalParams.put("otp", "123456");
            additionalParams.put("device_id", "device_12345");
            return new AuthCredentials("rzp_test_key", "rzp_test_secret", additionalParams);
        } else if (processor instanceof CryptoProcessor) {
            return new AuthCredentials("a1b2c3d4e5f6789012345678901234567890abcdef1234567890abcdef123456");
        }
        
        throw new IllegalArgumentException("Unknown processor type");
    }
    
    private static void displayPaymentResult(PaymentResult result) {
        System.out.println("Payment Result:");
        System.out.println("  Status: " + result.getStatus());
        System.out.println("  Transaction ID: " + result.getTransactionId());
        System.out.println("  Request ID: " + result.getRequestId());
        
        if (result.isSuccess()) {
            System.out.println("  Amount: " + result.getProcessedAmount() + " " + result.getCurrency());
            System.out.println("  Gateway Reference: " + result.getGatewayReference());
        } else {
            System.out.println("  Error: " + result.getErrorMessage());
        }
        
        System.out.println("  Timestamp: " + result.getTimestamp());
    }
    
    private static void testRefund(PaymentProcessor processor, PaymentResult paymentResult) {
        System.out.println("\nTesting refund...");
        
        try {
            RefundRequest refundRequest = new RefundRequest(
                "refund_" + System.currentTimeMillis(),
                paymentResult.getTransactionId(),
                new BigDecimal("50.00"), // Partial refund
                "Customer requested refund"
            );
            
            RefundResult refundResult = processor.refund(refundRequest);
            
            System.out.println("Refund Result:");
            System.out.println("  Status: " + refundResult.getStatus());
            System.out.println("  Refund Transaction ID: " + refundResult.getRefundTransactionId());
            System.out.println("  Refunded Amount: " + refundResult.getRefundedAmount());
            
        } catch (Exception e) {
            System.err.println("Refund failed: " + e.getMessage());
        }
    }
}

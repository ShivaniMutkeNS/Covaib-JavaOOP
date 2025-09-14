public class PayPalPayment extends Payment {
    private String paypalEmail;
    private String paypalId;
    private String country;
    private String currency;
    private boolean isVerified;
    private String verificationStatus;
    
    public PayPalPayment(String paymentId, String customerId, double amount, String currency, 
                        String description, String merchantId, String orderId, String paypalEmail, 
                        String paypalId, String country) {
        super(paymentId, customerId, amount, currency, description, "PayPal", merchantId, orderId);
        this.paypalEmail = paypalEmail;
        this.paypalId = paypalId;
        this.country = country;
        this.isVerified = false;
        this.verificationStatus = "Pending";
    }
    
    @Override
    public boolean processPayment() {
        System.out.println("Processing PayPal payment...");
        System.out.println("PayPal Email: " + paypalEmail);
        System.out.println("Amount: " + currency + String.format("%.2f", amount));
        
        if (!validatePayment()) {
            updateStatus("Failed", "", "PayPal validation failed");
            return false;
        }
        
        processingFee = calculateProcessingFee();
        
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        
        boolean success = !paypalEmail.endsWith("0");
        if (success) {
            String transactionId = "PP_" + paymentId + "_" + System.currentTimeMillis();
            updateStatus("Completed", transactionId, "");
            System.out.println("✅ PayPal payment processed successfully!");
        } else {
            updateStatus("Failed", "", "PayPal processing failed");
            System.out.println("❌ PayPal payment failed");
        }
        return success;
    }
    
    @Override
    public double calculateProcessingFee() {
        double fee = amount * 0.034; // 3.4%
        if (country.equals("US")) fee += 0.30;
        else fee += 0.35;
        return Math.round(fee * 100.0) / 100.0;
    }
    
    @Override
    public String getPaymentFeatures() {
        return "PayPal Features: Email: " + paypalEmail + ", Country: " + country + 
               ", Verified: " + (isVerified ? "Yes" : "No") + ", Status: " + verificationStatus;
    }
    
    @Override
    public boolean validatePayment() {
        return paypalEmail != null && paypalEmail.contains("@") && 
               paypalId != null && !paypalId.trim().isEmpty() &&
               country != null && !country.trim().isEmpty();
    }
}

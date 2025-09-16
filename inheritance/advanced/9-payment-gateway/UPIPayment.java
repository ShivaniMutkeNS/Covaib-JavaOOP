public class UPIPayment extends Payment {
    private String upiId;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private boolean isVerified;
    
    public UPIPayment(String paymentId, String customerId, double amount, String currency, 
                     String description, String merchantId, String orderId, String upiId, 
                     String bankName, String accountNumber, String ifscCode) {
        super(paymentId, customerId, amount, currency, description, "UPI", merchantId, orderId);
        this.upiId = upiId;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.isVerified = false;
    }
    
    @Override
    public boolean processPayment() {
        System.out.println("Processing UPI payment...");
        System.out.println("UPI ID: " + upiId);
        System.out.println("Bank: " + bankName);
        System.out.println("Amount: " + currency + String.format("%.2f", amount));
        
        if (!validatePayment()) {
            updateStatus("Failed", "", "UPI validation failed");
            return false;
        }
        
        processingFee = calculateProcessingFee();
        
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        
        boolean success = !upiId.endsWith("0");
        if (success) {
            String transactionId = "UPI_" + paymentId + "_" + System.currentTimeMillis();
            updateStatus("Completed", transactionId, "");
            System.out.println("✅ UPI payment processed successfully!");
        } else {
            updateStatus("Failed", "", "UPI processing failed");
            System.out.println("❌ UPI payment failed");
        }
        return success;
    }
    
    @Override
    public double calculateProcessingFee() {
        return Math.max(amount * 0.005, 0.50); // 0.5% or $0.50 minimum
    }
    
    @Override
    public String getPaymentFeatures() {
        return "UPI Features: UPI ID: " + upiId + ", Bank: " + bankName + 
               ", Account: " + accountNumber + ", IFSC: " + ifscCode + 
               ", Verified: " + (isVerified ? "Yes" : "No");
    }
    
    @Override
    public boolean validatePayment() {
        return upiId != null && upiId.contains("@") && 
               bankName != null && !bankName.trim().isEmpty() &&
               accountNumber != null && accountNumber.length() >= 10 &&
               ifscCode != null && ifscCode.length() == 11;
    }
}

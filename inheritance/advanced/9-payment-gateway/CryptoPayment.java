public class CryptoPayment extends Payment {
    private String cryptoType;
    private String walletAddress;
    private double cryptoAmount;
    private double exchangeRate;
    private String network;
    private boolean isVerified;
    private double gasFee;
    private String transactionHash;
    
    public CryptoPayment(String paymentId, String customerId, double amount, String currency, 
                        String description, String merchantId, String orderId, String cryptoType, 
                        String walletAddress, String network) {
        super(paymentId, customerId, amount, currency, description, "Crypto", merchantId, orderId);
        this.cryptoType = cryptoType;
        this.walletAddress = walletAddress;
        this.network = network;
        this.exchangeRate = getExchangeRate(cryptoType);
        this.cryptoAmount = amount / exchangeRate;
        this.isVerified = false;
        this.gasFee = 0.0;
        this.transactionHash = "";
    }
    
    @Override
    public boolean processPayment() {
        System.out.println("Processing Crypto payment...");
        System.out.println("Crypto Type: " + cryptoType);
        System.out.println("Amount: " + String.format("%.6f", cryptoAmount) + " " + cryptoType);
        System.out.println("Network: " + network);
        
        if (!validatePayment()) {
            updateStatus("Failed", "", "Crypto validation failed");
            return false;
        }
        
        processingFee = calculateProcessingFee();
        
        try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        
        boolean success = !walletAddress.endsWith("0");
        if (success) {
            String transactionId = "CRYPTO_" + paymentId + "_" + System.currentTimeMillis();
            this.transactionHash = "0x" + transactionId;
            updateStatus("Completed", transactionId, "");
            System.out.println("✅ Crypto payment processed successfully!");
            System.out.println("Transaction Hash: " + transactionHash);
        } else {
            updateStatus("Failed", "", "Crypto processing failed");
            System.out.println("❌ Crypto payment failed");
        }
        return success;
    }
    
    @Override
    public double calculateProcessingFee() {
        double baseFee = 0.0;
        switch (cryptoType.toLowerCase()) {
            case "bitcoin": baseFee = 0.0001; break;
            case "ethereum": baseFee = 0.001; break;
            case "litecoin": baseFee = 0.0001; break;
            default: baseFee = 0.0005;
        }
        this.gasFee = baseFee;
        return baseFee * exchangeRate;
    }
    
    @Override
    public String getPaymentFeatures() {
        return "Crypto Features: Type: " + cryptoType + ", Network: " + network + 
               ", Exchange Rate: " + String.format("%.2f", exchangeRate) + 
               ", Gas Fee: " + String.format("%.6f", gasFee) + " " + cryptoType + 
               ", Verified: " + (isVerified ? "Yes" : "No");
    }
    
    @Override
    public boolean validatePayment() {
        return cryptoType != null && !cryptoType.trim().isEmpty() &&
               walletAddress != null && walletAddress.length() >= 26 &&
               network != null && !network.trim().isEmpty() &&
               cryptoAmount > 0;
    }
    
    private double getExchangeRate(String cryptoType) {
        switch (cryptoType.toLowerCase()) {
            case "bitcoin": return 50000.0;
            case "ethereum": return 3000.0;
            case "litecoin": return 200.0;
            default: return 1.0;
        }
    }
}

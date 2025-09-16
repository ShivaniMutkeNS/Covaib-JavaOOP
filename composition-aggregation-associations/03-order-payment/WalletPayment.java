package composition.order;

import java.util.UUID;

/**
 * Digital Wallet Payment Implementation
 */
public class WalletPayment implements PaymentMethod {
    private final String walletId;
    private double balance;
    private final String walletProvider;
    
    public WalletPayment(String walletId, double balance, String walletProvider) {
        this.walletId = walletId;
        this.balance = balance;
        this.walletProvider = walletProvider;
    }
    
    @Override
    public PaymentResult processPayment(double amount, String orderId) {
        if (!validatePaymentDetails()) {
            return new PaymentResult(false, "Invalid wallet details", null);
        }
        
        double totalAmount = amount + getTransactionFee(amount);
        
        if (balance < totalAmount) {
            return new PaymentResult(false, "Insufficient wallet balance", null);
        }
        
        // Simulate wallet payment processing
        System.out.println("Processing wallet payment...");
        System.out.println("Wallet: " + walletProvider + " (" + walletId + ")");
        System.out.println("Amount: $" + String.format("%.2f", totalAmount));
        System.out.println("Remaining balance: $" + String.format("%.2f", balance - totalAmount));
        
        // Instant processing for wallet
        balance -= totalAmount;
        
        String transactionId = "WALLET_" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Wallet payment successful. Transaction ID: " + transactionId);
        return new PaymentResult(true, null, transactionId);
    }
    
    @Override
    public boolean validatePaymentDetails() {
        return walletId != null && !walletId.trim().isEmpty() &&
               balance >= 0 &&
               walletProvider != null && !walletProvider.trim().isEmpty();
    }
    
    @Override
    public String getType() {
        return walletProvider + " Wallet";
    }
    
    @Override
    public double getTransactionFee(double amount) {
        return amount * 0.01; // 1% transaction fee
    }
    
    public void addBalance(double amount) {
        balance += amount;
        System.out.println("Wallet recharged: $" + String.format("%.2f", amount));
    }
    
    public double getBalance() {
        return balance;
    }
}

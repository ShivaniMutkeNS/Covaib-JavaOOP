public class CryptoPayment extends Payment {
    private final String walletAddress;

    public CryptoPayment(double amount, String currency, String walletAddress) {
        super(amount, currency);
        this.walletAddress = walletAddress;
    }

    @Override
    public boolean processPayment() {
        System.out.println("[CRYPTO] Transferred " + amount + " " + currency + " to " + walletAddress);
        return true;
    }
}



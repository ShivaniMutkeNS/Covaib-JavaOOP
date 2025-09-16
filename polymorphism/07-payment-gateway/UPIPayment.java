public class UPIPayment extends Payment {
    private final String upiId;

    public UPIPayment(double amount, String currency, String upiId) {
        super(amount, currency);
        this.upiId = upiId;
    }

    @Override
    public boolean processPayment() {
        System.out.println("[UPI] Collected " + amount + " " + currency + " from " + upiId);
        return true;
    }
}



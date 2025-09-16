public class PayPalPayment extends Payment {
    private final String accountEmail;

    public PayPalPayment(double amount, String currency, String accountEmail) {
        super(amount, currency);
        this.accountEmail = accountEmail;
    }

    @Override
    public boolean processPayment() {
        System.out.println("[PAYPAL] Received " + amount + " " + currency + " from " + accountEmail);
        return true;
    }
}



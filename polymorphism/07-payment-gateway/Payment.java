public abstract class Payment {
    protected final double amount;
    protected final String currency;

    public Payment(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public abstract boolean processPayment();
}



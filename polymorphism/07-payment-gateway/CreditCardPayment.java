public class CreditCardPayment extends Payment {
    private final String maskedCard;

    public CreditCardPayment(double amount, String currency, String maskedCard) {
        super(amount, currency);
        this.maskedCard = maskedCard;
    }

    @Override
    public boolean processPayment() {
        System.out.println("[CARD] Charged " + amount + " " + currency + " to card " + maskedCard);
        return true;
    }
}



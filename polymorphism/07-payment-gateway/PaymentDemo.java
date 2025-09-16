package polymorphism.payment;
public class PaymentDemo {
    public static void main(String[] args) {
        Payment[] payments = new Payment[] {
            new CreditCardPayment(199.99, "USD", "**** **** **** 4242"),
            new UPIPayment(499.00, "INR", "alice@upi"),
            new PayPalPayment(59.00, "EUR", "bob@example.com"),
            new CryptoPayment(0.015, "BTC", "bc1qxyz...")
        };
        for (Payment p : payments) {
            p.processPayment();
        }
    }
}



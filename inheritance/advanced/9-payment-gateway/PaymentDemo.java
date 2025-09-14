public class PaymentDemo {
    public static void main(String[] args) {
        System.out.println("💳 PAYMENT GATEWAY SYSTEM 💳");
        System.out.println("==================================================");
        
        Payment[] payments = {
            new CreditCardPayment("PAY001", "CUST001", 100.0, "USD", "Online Purchase", "MERCH001", "ORD001", 
                                "4111111111111111", "John Doe", "12/25", "123", "123 Main St", "12345", "US"),
            new UPIPayment("PAY002", "CUST002", 50.0, "INR", "Mobile Recharge", "MERCH002", "ORD002", 
                          "john@paytm", "HDFC Bank", "1234567890", "HDFC0001234"),
            new PayPalPayment("PAY003", "CUST003", 75.0, "EUR", "Software License", "MERCH003", "ORD003", 
                             "jane@paypal.com", "PP123456789", "UK"),
            new CryptoPayment("PAY004", "CUST004", 200.0, "USD", "NFT Purchase", "MERCH004", "ORD004", 
                             "Bitcoin", "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa", "Bitcoin")
        };
        
        System.out.println("\n📋 PAYMENT INFORMATION:");
        for (Payment payment : payments) {
            System.out.println(payment.getPaymentInfo());
        }
        
        System.out.println("\n💳 PAYMENT PROCESSING:");
        for (Payment payment : payments) {
            System.out.println("\n" + payment.getPaymentMethod() + " Payment:");
            payment.processPayment();
        }
        
        System.out.println("\n🔧 PAYMENT FEATURES:");
        for (Payment payment : payments) {
            System.out.println(payment.getPaymentMethod() + ": " + payment.getPaymentFeatures());
        }
        
        System.out.println("\n💰 FEE COMPARISON:");
        System.out.println("Payment Method\t\tAmount\t\tFee\t\tTotal");
        System.out.println("--------------------------------------------------");
        for (Payment payment : payments) {
            System.out.printf("%-15s\t%s%.2f\t\t%s%.2f\t\t%s%.2f%n", 
                            payment.getPaymentMethod(), payment.getCurrency(), payment.getAmount(),
                            payment.getCurrency(), payment.getProcessingFee(),
                            payment.getCurrency(), payment.getAmount() + payment.getProcessingFee());
        }
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
}

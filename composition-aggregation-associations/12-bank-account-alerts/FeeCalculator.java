package composition.banking;

/**
 * Fee Calculator interface for calculating transaction fees
 */
public interface FeeCalculator {
    double calculateFee(BankAccount account, TransactionType type, double amount);
    String getCalculatorName();
}

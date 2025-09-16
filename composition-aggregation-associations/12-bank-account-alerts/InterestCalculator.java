package composition.banking;

/**
 * Interest Calculator interface for calculating account interest
 */
public interface InterestCalculator {
    double calculateInterest(BankAccount account);
    String getCalculatorName();
}

package composition.banking;

/**
 * Alert Rule interface for defining alert conditions
 */
public interface AlertRule {
    AlertEvaluation evaluate(BankAccount account, Transaction transaction);
    String getRuleName();
}

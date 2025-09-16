package composition.banking;

/**
 * Transaction Filter interface for filtering transactions
 */
public interface TransactionFilter {
    boolean matches(Transaction transaction);
}

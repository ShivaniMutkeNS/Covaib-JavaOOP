package composition.banking;

/**
 * Interest Result data class
 */
public class InterestResult {
    private final boolean success;
    private final String message;
    private final double interestAmount;
    
    public InterestResult(boolean success, String message, double interestAmount) {
        this.success = success;
        this.message = message;
        this.interestAmount = interestAmount;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public double getInterestAmount() { return interestAmount; }
}

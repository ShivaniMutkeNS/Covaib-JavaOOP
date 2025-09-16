package composition.banking;

/**
 * Standard Interest Calculator implementation
 */
public class StandardInterestCalculator implements InterestCalculator {
    private static final double SAVINGS_RATE = 0.02; // 2% annual
    private static final double CHECKING_RATE = 0.001; // 0.1% annual
    private static final double BUSINESS_RATE = 0.015; // 1.5% annual
    
    @Override
    public double calculateInterest(BankAccount account) {
        double annualRate = getAnnualRate(account.getAccountType());
        double dailyRate = annualRate / 365;
        
        // Calculate interest based on current balance
        return account.getBalance() * dailyRate;
    }
    
    private double getAnnualRate(AccountType accountType) {
        switch (accountType) {
            case SAVINGS:
                return SAVINGS_RATE;
            case CHECKING:
                return CHECKING_RATE;
            case BUSINESS:
                return BUSINESS_RATE;
            default:
                return 0.0;
        }
    }
    
    @Override
    public String getCalculatorName() {
        return "Standard Interest Calculator";
    }
}

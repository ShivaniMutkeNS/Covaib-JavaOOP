package composition.banking;

/**
 * Standard Fee Calculator implementation
 */
public class StandardFeeCalculator implements FeeCalculator {
    private static final double WITHDRAWAL_FEE = 2.50;
    private static final double TRANSFER_FEE = 1.00;
    private static final double OVERDRAFT_FEE = 35.00;
    
    @Override
    public double calculateFee(BankAccount account, TransactionType type, double amount) {
        switch (type) {
            case WITHDRAWAL:
                return calculateWithdrawalFee(account, amount);
            case TRANSFER:
                return calculateTransferFee(account, amount);
            case DEPOSIT:
                return 0.0; // No fee for deposits
            default:
                return 0.0;
        }
    }
    
    private double calculateWithdrawalFee(BankAccount account, double amount) {
        // Free withdrawals for business accounts
        if (account.getAccountType() == AccountType.BUSINESS) {
            return 0.0;
        }
        
        // Free withdrawals for amounts under $100
        if (amount < 100.0) {
            return 0.0;
        }
        
        return WITHDRAWAL_FEE;
    }
    
    private double calculateTransferFee(BankAccount account, double amount) {
        // Free transfers for savings accounts
        if (account.getAccountType() == AccountType.SAVINGS) {
            return 0.0;
        }
        
        return TRANSFER_FEE;
    }
    
    @Override
    public String getCalculatorName() {
        return "Standard Fee Calculator";
    }
}

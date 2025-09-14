/**
 * Investment Account implementation with portfolio management and higher returns
 */
public class InvestmentAccount extends BankAccount {
    private double portfolioValue;
    private double totalGainsLosses;
    private double managementFeeRate;
    private boolean autoReinvestDividends;
    private String riskLevel;
    private double yearToDateReturn;
    
    public InvestmentAccount(String accountNumber, String accountHolderName, double initialDeposit) {
        super(accountNumber, accountHolderName, AccountType.INVESTMENT, initialDeposit);
        this.portfolioValue = initialDeposit;
        this.totalGainsLosses = 0.0;
        this.managementFeeRate = 0.0075; // 0.75% annual management fee
        this.autoReinvestDividends = true;
        this.riskLevel = "Moderate";
        this.yearToDateReturn = 0.0;
    }
    
    @Override
    public boolean canWithdraw(double amount) {
        // Investment accounts require minimum balance and may have withdrawal restrictions
        double afterWithdrawal = balance - amount;
        return afterWithdrawal >= accountType.getMinimumBalance();
    }
    
    @Override
    public void calculateInterest() {
        // Investment accounts have variable returns based on market performance
        double marketReturn = simulateMarketReturn();
        double monthlyReturn = balance * (marketReturn / 12.0);
        
        balance += monthlyReturn;
        portfolioValue = balance;
        totalGainsLosses += monthlyReturn;
        yearToDateReturn += marketReturn;
        
        String returnType = monthlyReturn >= 0 ? "Investment gain" : "Investment loss";
        TransactionType transactionType = monthlyReturn >= 0 ? 
            TransactionType.INTEREST_CREDIT : TransactionType.FEE_DEBIT;
        
        Transaction returnTransaction = new Transaction(transactionType, 
            Math.abs(monthlyReturn), returnType, balance);
        transactionHistory.add(returnTransaction);
        
        System.out.println((monthlyReturn >= 0 ? "📈" : "📉") + " Monthly return: " + 
                         (monthlyReturn >= 0 ? "+" : "") + String.format("%.2f", monthlyReturn) + 
                         " (" + String.format("%.2f%%", marketReturn * 100) + ")");
    }
    
    private double simulateMarketReturn() {
        // Simulate market volatility based on risk level
        double baseReturn = accountType.getMonthlyInterestRate();
        double volatility;
        
        switch (riskLevel) {
            case "Conservative":
                volatility = 0.02; // 2% monthly volatility
                break;
            case "Aggressive":
                volatility = 0.08; // 8% monthly volatility
                break;
            default: // Moderate
                volatility = 0.05; // 5% monthly volatility
        }
        
        // Random return between -volatility and +volatility around base return
        double randomFactor = (Math.random() - 0.5) * 2; // -1 to 1
        return baseReturn + (randomFactor * volatility);
    }
    
    @Override
    public void applyMonthlyFees() {
        // Apply management fee
        double monthlyManagementFee = balance * (managementFeeRate / 12.0);
        balance -= monthlyManagementFee;
        portfolioValue = balance;
        
        Transaction feeTransaction = new Transaction(TransactionType.MAINTENANCE_FEE, 
            monthlyManagementFee, "Investment management fee", balance);
        transactionHistory.add(feeTransaction);
        
        System.out.println("💼 Management fee: $" + String.format("%.2f", monthlyManagementFee));
        
        // Apply account maintenance fee if below minimum
        if (balance < accountType.getMinimumBalance()) {
            double lowBalanceFee = 25.0;
            balance -= lowBalanceFee;
            portfolioValue = balance;
            
            Transaction lowBalanceTransaction = new Transaction(TransactionType.MAINTENANCE_FEE, 
                lowBalanceFee, "Low balance maintenance fee", balance);
            transactionHistory.add(lowBalanceTransaction);
            
            System.out.println("💸 Low balance fee: $" + String.format("%.2f", lowBalanceFee));
        }
    }
    
    @Override
    public double getAvailableBalance() {
        // Only amount above minimum balance is available for withdrawal
        return Math.max(0, balance - accountType.getMinimumBalance());
    }
    
    public boolean buyInvestment(double amount, String investmentName) {
        if (amount <= 0) {
            System.out.println("❌ Investment amount must be positive");
            return false;
        }
        
        if (amount > getAvailableBalance()) {
            System.out.println("❌ Insufficient funds for investment");
            return false;
        }
        
        // Simulate investment purchase (no actual balance change as it's still invested)
        Transaction investmentTransaction = new Transaction(TransactionType.WITHDRAWAL, 
            amount, "Investment purchase - " + investmentName, balance);
        transactionHistory.add(investmentTransaction);
        
        System.out.println("📊 Invested $" + String.format("%.2f", amount) + " in " + investmentName);
        return true;
    }
    
    public void rebalancePortfolio() {
        System.out.println("⚖️ Rebalancing investment portfolio...");
        
        // Simulate rebalancing fee
        double rebalancingFee = 15.0;
        balance -= rebalancingFee;
        portfolioValue = balance;
        
        Transaction rebalanceTransaction = new Transaction(TransactionType.FEE_DEBIT, 
            rebalancingFee, "Portfolio rebalancing fee", balance);
        transactionHistory.add(rebalanceTransaction);
        
        System.out.println("✅ Portfolio rebalanced (Fee: $" + String.format("%.2f", rebalancingFee) + ")");
    }
    
    public void setRiskLevel(String riskLevel) {
        if (riskLevel.equals("Conservative") || riskLevel.equals("Moderate") || riskLevel.equals("Aggressive")) {
            this.riskLevel = riskLevel;
            System.out.println("⚖️ Risk level set to: " + riskLevel);
        } else {
            System.out.println("❌ Invalid risk level. Use: Conservative, Moderate, or Aggressive");
        }
    }
    
    public void setAutoReinvestDividends(boolean autoReinvest) {
        this.autoReinvestDividends = autoReinvest;
        System.out.println("🔄 Dividend reinvestment " + (autoReinvest ? "enabled" : "disabled"));
    }
    
    public double getPortfolioPerformance() {
        if (portfolioValue == 0) return 0.0;
        return ((portfolioValue - (portfolioValue - totalGainsLosses)) / (portfolioValue - totalGainsLosses)) * 100.0;
    }
    
    public void generatePerformanceReport() {
        System.out.println("\n📊 INVESTMENT PERFORMANCE REPORT");
        System.out.println("=".repeat(50));
        System.out.println("Current Portfolio Value: $" + String.format("%.2f", portfolioValue));
        System.out.println("Total Gains/Losses: $" + String.format("%.2f", totalGainsLosses));
        System.out.println("Year-to-Date Return: " + String.format("%.2f%%", yearToDateReturn * 100));
        System.out.println("Risk Level: " + riskLevel);
        System.out.println("Management Fee Rate: " + String.format("%.2f%%", managementFeeRate * 100));
        System.out.println("Auto-Reinvest Dividends: " + (autoReinvestDividends ? "Yes" : "No"));
        System.out.println("=".repeat(50));
    }
    
    // Getters
    public double getPortfolioValue() { return portfolioValue; }
    public double getTotalGainsLosses() { return totalGainsLosses; }
    public double getManagementFeeRate() { return managementFeeRate; }
    public boolean isAutoReinvestDividends() { return autoReinvestDividends; }
    public String getRiskLevel() { return riskLevel; }
    public double getYearToDateReturn() { return yearToDateReturn; }
    
    @Override
    public void printStatement() {
        super.printStatement();
        System.out.println("📊 Portfolio Value: $" + String.format("%.2f", portfolioValue));
        System.out.println("📈 Total Gains/Losses: $" + String.format("%.2f", totalGainsLosses));
        System.out.println("📅 YTD Return: " + String.format("%.2f%%", yearToDateReturn * 100));
        System.out.println("⚖️ Risk Level: " + riskLevel);
        System.out.println("🔄 Auto-Reinvest: " + (autoReinvestDividends ? "Yes" : "No"));
    }
}

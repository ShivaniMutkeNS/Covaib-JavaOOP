
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Demo class to demonstrate Stock Trading System
 */
public class PortfolioDemo {
    public static void main(String[] args) {
        System.out.println("=== Stock Trading System Demo ===\n");
        
        // Test portfolio creation and trading
        testPortfolioCreationAndTrading();
        
        // Test stock buying
        testStockBuying();
        
        // Test stock selling
        testStockSelling();
        
        // Test portfolio management
        testPortfolioManagement();
        
        // Test unauthorized access
        testUnauthorizedAccess();
    }
    
    private static void testPortfolioCreationAndTrading() {
        System.out.println("=== Portfolio Creation and Trading Test ===");
        
        Portfolio portfolio = new Portfolio("PORT_001", "USER_001");
        System.out.println("Portfolio created: " + portfolio);
        
        // Test buying stock
        Portfolio.TransactionResult result1 = portfolio.buyStock("AAPL", 100, new BigDecimal("150.00"), "USER_001");
        System.out.println("Bought AAPL: " + result1);
        
        // Test buying more of the same stock
        Portfolio.TransactionResult result2 = portfolio.buyStock("AAPL", 50, new BigDecimal("155.00"), "USER_001");
        System.out.println("Bought more AAPL: " + result2);
        
        // Test buying different stock
        Portfolio.TransactionResult result3 = portfolio.buyStock("GOOGL", 25, new BigDecimal("2800.00"), "USER_001");
        System.out.println("Bought GOOGL: " + result3);
        
        // Test selling stock
        Portfolio.TransactionResult result4 = portfolio.sellStock("AAPL", 75, new BigDecimal("160.00"), "USER_001");
        System.out.println("Sold AAPL: " + result4);
        
        System.out.println();
    }
    
    private static void testStockBuying() {
        System.out.println("=== Stock Buying Test ===");
        
        Portfolio portfolio = new Portfolio("PORT_002", "USER_002");
        
        // Test valid stock purchase
        Portfolio.TransactionResult result1 = portfolio.buyStock("MSFT", 200, new BigDecimal("300.00"), "USER_002");
        System.out.println("Bought MSFT: " + result1);
        
        // Test invalid stock symbol
        Portfolio.TransactionResult result2 = portfolio.buyStock("", 100, new BigDecimal("50.00"), "USER_002");
        System.out.println("Bought with empty symbol: " + result2);
        
        // Test invalid quantity
        Portfolio.TransactionResult result3 = portfolio.buyStock("TSLA", 0, new BigDecimal("800.00"), "USER_002");
        System.out.println("Bought with zero quantity: " + result3);
        
        // Test invalid price
        Portfolio.TransactionResult result4 = portfolio.buyStock("TSLA", 100, new BigDecimal("-800.00"), "USER_002");
        System.out.println("Bought with negative price: " + result4);
        
        // Test insufficient funds
        Portfolio.TransactionResult result5 = portfolio.buyStock("AMZN", 1000, new BigDecimal("3000.00"), "USER_002");
        System.out.println("Bought with insufficient funds: " + result5);
        
        System.out.println();
    }
    
    private static void testStockSelling() {
        System.out.println("=== Stock Selling Test ===");
        
        Portfolio portfolio = new Portfolio("PORT_003", "USER_003");
        
        // First buy some stock
        portfolio.buyStock("NVDA", 100, new BigDecimal("400.00"), "USER_003");
        System.out.println("Bought NVDA: SUCCESS");
        
        // Test valid stock sale
        Portfolio.TransactionResult result1 = portfolio.sellStock("NVDA", 50, new BigDecimal("420.00"), "USER_003");
        System.out.println("Sold NVDA: " + result1);
        
        // Test selling more than owned
        Portfolio.TransactionResult result2 = portfolio.sellStock("NVDA", 100, new BigDecimal("430.00"), "USER_003");
        System.out.println("Sold more than owned: " + result2);
        
        // Test selling non-existent stock
        Portfolio.TransactionResult result3 = portfolio.sellStock("INTC", 50, new BigDecimal("50.00"), "USER_003");
        System.out.println("Sold non-existent stock: " + result3);
        
        // Test selling remaining shares
        Portfolio.TransactionResult result4 = portfolio.sellStock("NVDA", 50, new BigDecimal("440.00"), "USER_003");
        System.out.println("Sold remaining NVDA: " + result4);
        
        System.out.println();
    }
    
    private static void testPortfolioManagement() {
        System.out.println("=== Portfolio Management Test ===");
        
        Portfolio portfolio = new Portfolio("PORT_004", "USER_004");
        
        // Add some holdings
        portfolio.buyStock("AAPL", 100, new BigDecimal("150.00"), "USER_004");
        portfolio.buyStock("GOOGL", 50, new BigDecimal("2800.00"), "USER_004");
        portfolio.buyStock("MSFT", 75, new BigDecimal("300.00"), "USER_004");
        
        // Get portfolio holdings
        Map<String, Portfolio.StockHolding> holdings = portfolio.getHoldings("USER_004");
        if (holdings != null) {
            System.out.println("Portfolio holdings:");
            for (Map.Entry<String, Portfolio.StockHolding> entry : holdings.entrySet()) {
                System.out.println("  " + entry.getValue());
            }
        } else {
            System.out.println("Holdings access denied");
        }
        
        // Get portfolio transactions
        Map<String, Portfolio.Transaction> transactions = portfolio.getTransactions("USER_004");
        if (transactions != null) {
            System.out.println("Portfolio transactions:");
            for (Portfolio.Transaction transaction : transactions.values()) {
                System.out.println("  " + transaction);
            }
        } else {
            System.out.println("Transactions access denied");
        }
        
        // Get portfolio value
        BigDecimal portfolioValue = portfolio.getPortfolioValue("USER_004");
        if (portfolioValue != null) {
            System.out.println("Portfolio value: $" + portfolioValue);
        } else {
            System.out.println("Portfolio value access denied");
        }
        
        // Get available cash
        BigDecimal availableCash = portfolio.getAvailableCash("USER_004");
        if (availableCash != null) {
            System.out.println("Available cash: $" + availableCash);
        } else {
            System.out.println("Available cash access denied");
        }
        
        // Get portfolio summary
        String summary = portfolio.getPortfolioSummary("USER_004");
        if (summary != null) {
            System.out.println("Portfolio summary:");
            System.out.println(summary);
        } else {
            System.out.println("Portfolio summary access denied");
        }
        
        System.out.println();
    }
    
    private static void testUnauthorizedAccess() {
        System.out.println("=== Unauthorized Access Test ===");
        
        Portfolio portfolio = new Portfolio("PORT_005", "USER_005");
        
        // Add some holdings
        portfolio.buyStock("AAPL", 100, new BigDecimal("150.00"), "USER_005");
        
        // Test unauthorized trading
        Portfolio.TransactionResult result1 = portfolio.buyStock("GOOGL", 50, new BigDecimal("2800.00"), "USER_006");
        System.out.println("Bought stock with wrong user: " + result1);
        
        Portfolio.TransactionResult result2 = portfolio.sellStock("AAPL", 50, new BigDecimal("160.00"), "USER_006");
        System.out.println("Sold stock with wrong user: " + result2);
        
        // Test unauthorized viewing
        Map<String, Portfolio.StockHolding> holdings = portfolio.getHoldings("USER_006");
        System.out.println("Holdings with wrong user: " + (holdings != null ? "ACCESS GRANTED" : "ACCESS DENIED"));
        
        Map<String, Portfolio.Transaction> transactions = portfolio.getTransactions("USER_006");
        System.out.println("Transactions with wrong user: " + (transactions != null ? "ACCESS GRANTED" : "ACCESS DENIED"));
        
        BigDecimal portfolioValue = portfolio.getPortfolioValue("USER_006");
        System.out.println("Portfolio value with wrong user: " + (portfolioValue != null ? "ACCESS GRANTED" : "ACCESS DENIED"));
        
        String summary = portfolio.getPortfolioSummary("USER_006");
        System.out.println("Portfolio summary with wrong user: " + (summary != null ? "ACCESS GRANTED" : "ACCESS DENIED"));
        
        System.out.println();
    }
    
    /**
     * Test transaction filtering
     */
    private static void testTransactionFiltering() {
        System.out.println("=== Transaction Filtering Test ===");
        
        Portfolio portfolio = new Portfolio("PORT_006", "USER_006");
        
        // Add some transactions
        portfolio.buyStock("AAPL", 100, new BigDecimal("150.00"), "USER_006");
        portfolio.sellStock("AAPL", 50, new BigDecimal("160.00"), "USER_006");
        portfolio.buyStock("GOOGL", 25, new BigDecimal("2800.00"), "USER_006");
        portfolio.buyStock("AAPL", 75, new BigDecimal("155.00"), "USER_006");
        
        // Get transactions by symbol
        List<Portfolio.Transaction> aaplTransactions = portfolio.getTransactionsBySymbol("AAPL", "USER_006");
        System.out.println("AAPL transactions: " + aaplTransactions.size());
        for (Portfolio.Transaction transaction : aaplTransactions) {
            System.out.println("  " + transaction);
        }
        
        List<Portfolio.Transaction> googlTransactions = portfolio.getTransactionsBySymbol("GOOGL", "USER_006");
        System.out.println("GOOGL transactions: " + googlTransactions.size());
        for (Portfolio.Transaction transaction : googlTransactions) {
            System.out.println("  " + transaction);
        }
        
        System.out.println();
    }
    
    /**
     * Test portfolio with many transactions
     */
    private static void testManyTransactions() {
        System.out.println("=== Many Transactions Test ===");
        
        Portfolio portfolio = new Portfolio("PORT_007", "USER_007");
        
        // Add many transactions
        String[] symbols = {"AAPL", "GOOGL", "MSFT", "TSLA", "AMZN"};
        BigDecimal[] prices = {new BigDecimal("150.00"), new BigDecimal("2800.00"), 
                              new BigDecimal("300.00"), new BigDecimal("800.00"), new BigDecimal("3000.00")};
        
        for (int i = 0; i < 20; i++) {
            String symbol = symbols[i % symbols.length];
            BigDecimal price = prices[i % prices.length];
            int quantity = 10 + (i % 50);
            
            if (i % 2 == 0) {
                portfolio.buyStock(symbol, quantity, price, "USER_007");
            } else {
                portfolio.sellStock(symbol, quantity, price, "USER_007");
            }
        }
        
        System.out.println("Added 20 transactions");
        
        // Get portfolio summary
        String summary = portfolio.getPortfolioSummary("USER_007");
        if (summary != null) {
            System.out.println("Portfolio summary:");
            System.out.println(summary);
        }
        
        System.out.println();
    }
}


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stock Trading System
 * 
 * This class demonstrates encapsulation by:
 * 1. Encapsulating Portfolio with private stock holdings
 * 2. Exposing only controlled methods like buyStock(), sellStock()
 * 3. Preventing external classes from directly modifying holdings
 * 4. Implementing proper validation and state management
 */
public class Portfolio {
    // Encapsulated portfolio data
    private final String portfolioId;
    private final String ownerId;
    private final LocalDateTime createdTime;
    
    // Encapsulated stock holdings
    private final Map<String, StockHolding> holdings;
    private final Map<String, Transaction> transactions;
    
    // Portfolio configuration
    private final PortfolioConfig config;
    
    // Access control
    private final AccessController accessController;
    
    /**
     * Constructor
     */
    public Portfolio(String portfolioId, String ownerId) {
        this.portfolioId = portfolioId;
        this.ownerId = ownerId;
        this.createdTime = LocalDateTime.now();
        this.holdings = new ConcurrentHashMap<>();
        this.transactions = new ConcurrentHashMap<>();
        this.config = new PortfolioConfig();
        this.accessController = new AccessController();
    }
    
    /**
     * Buy stock
     * @param symbol Stock symbol
     * @param quantity Quantity to buy
     * @param price Price per share
     * @param requesterId ID of the person making the request
     * @return Transaction result
     */
    public TransactionResult buyStock(String symbol, int quantity, BigDecimal price, String requesterId) {
        if (!accessController.canTrade(requesterId, ownerId)) {
            return new TransactionResult(false, "Unauthorized access", null);
        }
        
        if (symbol == null || symbol.trim().isEmpty()) {
            return new TransactionResult(false, "Invalid stock symbol", null);
        }
        
        if (quantity <= 0) {
            return new TransactionResult(false, "Invalid quantity", null);
        }
        
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            return new TransactionResult(false, "Invalid price", null);
        }
        
        // Check if portfolio has enough cash
        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(quantity));
        if (getAvailableCash(requesterId).compareTo(totalCost) < 0) {
            return new TransactionResult(false, "Insufficient funds", null);
        }
        
        // Check position size limit
        if (totalCost.compareTo(config.getMaxPositionSize()) > 0) {
            return new TransactionResult(false, "Position size exceeds maximum limit", null);
        }
        
        // Check maximum holdings limit
        if (holdings.size() >= config.getMaxHoldings() && !holdings.containsKey(symbol)) {
            return new TransactionResult(false, "Maximum number of holdings reached", null);
        }
        
        // Create transaction
        Transaction transaction = new Transaction(
            generateTransactionId(),
            TransactionType.BUY,
            symbol,
            quantity,
            price,
            LocalDateTime.now(),
            requesterId
        );
        
        // Update holdings
        StockHolding existingHolding = holdings.get(symbol);
        if (existingHolding != null) {
            // Update existing holding
            StockHolding updatedHolding = new StockHolding(
                symbol,
                existingHolding.getQuantity() + quantity,
                existingHolding.getAveragePrice(),
                existingHolding.getTotalCost().add(totalCost)
            );
            holdings.put(symbol, updatedHolding);
        } else {
            // Create new holding
            StockHolding newHolding = new StockHolding(
                symbol,
                quantity,
                price,
                totalCost
            );
            holdings.put(symbol, newHolding);
        }
        
        // Record transaction
        transactions.put(transaction.getTransactionId(), transaction);
        
        return new TransactionResult(true, "Stock purchased successfully", transaction);
    }
    
    /**
     * Sell stock
     * @param symbol Stock symbol
     * @param quantity Quantity to sell
     * @param price Price per share
     * @param requesterId ID of the person making the request
     * @return Transaction result
     */
    public TransactionResult sellStock(String symbol, int quantity, BigDecimal price, String requesterId) {
        if (!accessController.canTrade(requesterId, ownerId)) {
            return new TransactionResult(false, "Unauthorized access", null);
        }
        
        if (symbol == null || symbol.trim().isEmpty()) {
            return new TransactionResult(false, "Invalid stock symbol", null);
        }
        
        if (quantity <= 0) {
            return new TransactionResult(false, "Invalid quantity", null);
        }
        
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            return new TransactionResult(false, "Invalid price", null);
        }
        
        // Check if portfolio has enough shares
        StockHolding holding = holdings.get(symbol);
        if (holding == null || holding.getQuantity() < quantity) {
            return new TransactionResult(false, "Insufficient shares", null);
        }
        
        // Create transaction
        Transaction transaction = new Transaction(
            generateTransactionId(),
            TransactionType.SELL,
            symbol,
            quantity,
            price,
            LocalDateTime.now(),
            requesterId
        );
        
        // Update holdings
        if (holding.getQuantity() == quantity) {
            // Remove holding completely
            holdings.remove(symbol);
        } else {
            // Update existing holding
            StockHolding updatedHolding = new StockHolding(
                symbol,
                holding.getQuantity() - quantity,
                holding.getAveragePrice(),
                holding.getTotalCost().subtract(price.multiply(BigDecimal.valueOf(quantity)))
            );
            holdings.put(symbol, updatedHolding);
        }
        
        // Record transaction
        transactions.put(transaction.getTransactionId(), transaction);
        
        return new TransactionResult(true, "Stock sold successfully", transaction);
    }
    
    /**
     * Get portfolio holdings (read-only)
     * @param requesterId ID of the person requesting
     * @return Unmodifiable map of holdings or null if unauthorized
     */
    public Map<String, StockHolding> getHoldings(String requesterId) {
        if (!accessController.canViewHoldings(requesterId, ownerId)) {
            return null;
        }
        
        return Collections.unmodifiableMap(holdings);
    }
    
    /**
     * Get portfolio transactions (read-only)
     * @param requesterId ID of the person requesting
     * @return Unmodifiable map of transactions or null if unauthorized
     */
    public Map<String, Transaction> getTransactions(String requesterId) {
        if (!accessController.canViewTransactions(requesterId, ownerId)) {
            return null;
        }
        
        return Collections.unmodifiableMap(transactions);
    }
    
    /**
     * Get portfolio value
     * @param requesterId ID of the person requesting
     * @return Portfolio value or null if unauthorized
     */
    public BigDecimal getPortfolioValue(String requesterId) {
        if (!accessController.canViewHoldings(requesterId, ownerId)) {
            return null;
        }
        
        return holdings.values().stream()
                .map(StockHolding::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Get portfolio configuration
     * @return Portfolio configuration
     */
    public PortfolioConfig getConfig() {
        return config;
    }
    
    /**
     * Get available cash
     * @param requesterId ID of the person requesting
     * @return Available cash or null if unauthorized
     */
    public BigDecimal getAvailableCash(String requesterId) {
        if (!accessController.canViewHoldings(requesterId, ownerId)) {
            return null;
        }
        
        // In a real implementation, this would come from a cash account
        // For this demo, we'll use a fixed amount
        return new BigDecimal("100000");
    }
    
    /**
     * Get portfolio summary
     * @param requesterId ID of the person requesting
     * @return Portfolio summary or null if unauthorized
     */
    public String getPortfolioSummary(String requesterId) {
        if (!accessController.canViewHoldings(requesterId, ownerId)) {
            return null;
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("Portfolio ID: ").append(portfolioId).append("\n");
        summary.append("Owner: ").append(ownerId).append("\n");
        summary.append("Created: ").append(createdTime).append("\n");
        summary.append("Holdings: ").append(holdings.size()).append(" stocks\n");
        summary.append("Transactions: ").append(transactions.size()).append(" total\n");
        summary.append("Portfolio Value: $").append(getPortfolioValue(requesterId)).append("\n");
        summary.append("Available Cash: $").append(getAvailableCash(requesterId)).append("\n");
        
        return summary.toString();
    }
    
    /**
     * Get holdings by symbol
     * @param symbol Stock symbol
     * @param requesterId ID of the person requesting
     * @return Stock holding or null if not found or unauthorized
     */
    public StockHolding getHolding(String symbol, String requesterId) {
        if (!accessController.canViewHoldings(requesterId, ownerId)) {
            return null;
        }
        
        return holdings.get(symbol);
    }
    
    /**
     * Get transaction by ID
     * @param transactionId Transaction ID
     * @param requesterId ID of the person requesting
     * @return Transaction or null if not found or unauthorized
     */
    public Transaction getTransaction(String transactionId, String requesterId) {
        if (!accessController.canViewTransactions(requesterId, ownerId)) {
            return null;
        }
        
        return transactions.get(transactionId);
    }
    
    /**
     * Get transactions by symbol
     * @param symbol Stock symbol
     * @param requesterId ID of the person requesting
     * @return List of transactions for the symbol
     */
    public List<Transaction> getTransactionsBySymbol(String symbol, String requesterId) {
        if (!accessController.canViewTransactions(requesterId, ownerId)) {
            return Collections.emptyList();
        }
        
        return transactions.values().stream()
                .filter(transaction -> symbol.equals(transaction.getSymbol()))
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Generate unique transaction ID
     * @return Transaction ID
     */
    private String generateTransactionId() {
        return "TXN_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    // Getters
    public String getPortfolioId() { return portfolioId; }
    public String getOwnerId() { return ownerId; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    
    /**
     * Stock holding (immutable)
     */
    public static class StockHolding {
        private final String symbol;
        private final int quantity;
        private final BigDecimal averagePrice;
        private final BigDecimal totalCost;
        
        public StockHolding(String symbol, int quantity, BigDecimal averagePrice, BigDecimal totalCost) {
            this.symbol = symbol;
            this.quantity = quantity;
            this.averagePrice = averagePrice;
            this.totalCost = totalCost;
        }
        
        public String getSymbol() { return symbol; }
        public int getQuantity() { return quantity; }
        public BigDecimal getAveragePrice() { return averagePrice; }
        public BigDecimal getTotalCost() { return totalCost; }
        
        public BigDecimal getTotalValue() {
            return averagePrice.multiply(BigDecimal.valueOf(quantity));
        }
        
        @Override
        public String toString() {
            return String.format("StockHolding{symbol='%s', quantity=%d, avgPrice=%s, totalCost=%s}", 
                symbol, quantity, averagePrice, totalCost);
        }
    }
    
    /**
     * Transaction (immutable)
     */
    public static class Transaction {
        private final String transactionId;
        private final TransactionType type;
        private final String symbol;
        private final int quantity;
        private final BigDecimal price;
        private final LocalDateTime timestamp;
        private final String requesterId;
        
        public Transaction(String transactionId, TransactionType type, String symbol, 
                         int quantity, BigDecimal price, LocalDateTime timestamp, String requesterId) {
            this.transactionId = transactionId;
            this.type = type;
            this.symbol = symbol;
            this.quantity = quantity;
            this.price = price;
            this.timestamp = timestamp;
            this.requesterId = requesterId;
        }
        
        public String getTransactionId() { return transactionId; }
        public TransactionType getType() { return type; }
        public String getSymbol() { return symbol; }
        public int getQuantity() { return quantity; }
        public BigDecimal getPrice() { return price; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getRequesterId() { return requesterId; }
        
        public BigDecimal getTotalAmount() {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
        
        @Override
        public String toString() {
            return String.format("Transaction{id='%s', type=%s, symbol='%s', qty=%d, price=%s, time=%s}", 
                transactionId, type, symbol, quantity, price, timestamp);
        }
    }
    
    /**
     * Transaction result
     */
    public static class TransactionResult {
        private final boolean success;
        private final String message;
        private final Transaction transaction;
        
        public TransactionResult(boolean success, String message, Transaction transaction) {
            this.success = success;
            this.message = message;
            this.transaction = transaction;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Transaction getTransaction() { return transaction; }
        
        @Override
        public String toString() {
            return String.format("TransactionResult{success=%s, message='%s', transaction=%s}", 
                success, message, transaction);
        }
    }
    
    /**
     * Transaction types
     */
    public enum TransactionType {
        BUY, SELL
    }
    
    /**
     * Portfolio configuration
     */
    private static class PortfolioConfig {
        private final BigDecimal maxPositionSize;
        private final int maxHoldings;
        
        public PortfolioConfig() {
            this.maxPositionSize = new BigDecimal("1000000");
            this.maxHoldings = 100;
        }
        
        public BigDecimal getMaxPositionSize() { return maxPositionSize; }
        public int getMaxHoldings() { return maxHoldings; }
    }
    
    /**
     * Access controller for portfolio operations
     */
    private static class AccessController {
        public boolean canTrade(String requesterId, String ownerId) {
            return requesterId != null && requesterId.equals(ownerId);
        }
        
        public boolean canViewHoldings(String requesterId, String ownerId) {
            return requesterId != null && requesterId.equals(ownerId);
        }
        
        public boolean canViewTransactions(String requesterId, String ownerId) {
            return requesterId != null && requesterId.equals(ownerId);
        }
    }
    
    @Override
    public String toString() {
        return String.format("Portfolio{id='%s', owner='%s', holdings=%d, transactions=%d}", 
            portfolioId, ownerId, holdings.size(), transactions.size());
    }
}

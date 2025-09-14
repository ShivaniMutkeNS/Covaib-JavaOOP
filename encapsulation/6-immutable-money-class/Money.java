
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Immutable Money Class
 * 
 * This class demonstrates encapsulation by:
 * 1. Creating an immutable Money class that represents currency + amount
 * 2. Once created, cannot change value
 * 3. Support operations like add(), subtract() that return new objects
 * 4. Proper currency handling and validation
 */
public final class Money {
    // Encapsulated immutable fields
    private final BigDecimal amount;
    private final Currency currency;
    
    // Constants for common currencies
    public static final Currency USD = Currency.getInstance("USD");
    public static final Currency EUR = Currency.getInstance("EUR");
    public static final Currency GBP = Currency.getInstance("GBP");
    public static final Currency JPY = Currency.getInstance("JPY");
    public static final Currency INR = Currency.getInstance("INR");
    
    // Default scale for monetary calculations
    private static final int DEFAULT_SCALE = 2;
    
    /**
     * Constructor with amount and currency
     * @param amount Monetary amount
     * @param currency Currency
     * @throws IllegalArgumentException if amount is null or currency is null
     */
    public Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        
        // Round to appropriate scale for the currency
        int scale = getScaleForCurrency(currency);
        this.amount = amount.setScale(scale, RoundingMode.HALF_UP);
        this.currency = currency;
    }
    
    /**
     * Constructor with amount and currency code
     * @param amount Monetary amount
     * @param currencyCode Currency code (e.g., "USD", "EUR")
     * @throws IllegalArgumentException if amount is null or currency code is invalid
     */
    public Money(BigDecimal amount, String currencyCode) {
        this(amount, Currency.getInstance(currencyCode));
    }
    
    /**
     * Constructor with double amount and currency
     * @param amount Monetary amount
     * @param currency Currency
     * @throws IllegalArgumentException if currency is null
     */
    public Money(double amount, Currency currency) {
        this(BigDecimal.valueOf(amount), currency);
    }
    
    /**
     * Constructor with double amount and currency code
     * @param amount Monetary amount
     * @param currencyCode Currency code
     * @throws IllegalArgumentException if currency code is invalid
     */
    public Money(double amount, String currencyCode) {
        this(BigDecimal.valueOf(amount), currencyCode);
    }
    
    /**
     * Get the amount
     * @return Monetary amount
     */
    public BigDecimal getAmount() {
        return amount;
    }
    
    /**
     * Get the currency
     * @return Currency
     */
    public Currency getCurrency() {
        return currency;
    }
    
    /**
     * Add another money amount
     * @param other Money to add
     * @return New Money object with sum
     * @throws IllegalArgumentException if currencies don't match
     */
    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }
    
    /**
     * Subtract another money amount
     * @param other Money to subtract
     * @return New Money object with difference
     * @throws IllegalArgumentException if currencies don't match
     */
    public Money subtract(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }
    
    /**
     * Multiply by a factor
     * @param factor Multiplication factor
     * @return New Money object with multiplied amount
     * @throws IllegalArgumentException if factor is null
     */
    public Money multiply(BigDecimal factor) {
        if (factor == null) {
            throw new IllegalArgumentException("Factor cannot be null");
        }
        return new Money(this.amount.multiply(factor), this.currency);
    }
    
    /**
     * Multiply by a factor
     * @param factor Multiplication factor
     * @return New Money object with multiplied amount
     */
    public Money multiply(double factor) {
        return multiply(BigDecimal.valueOf(factor));
    }
    
    /**
     * Divide by a factor
     * @param factor Division factor
     * @return New Money object with divided amount
     * @throws IllegalArgumentException if factor is null or zero
     */
    public Money divide(BigDecimal factor) {
        if (factor == null) {
            throw new IllegalArgumentException("Factor cannot be null");
        }
        if (factor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return new Money(this.amount.divide(factor, RoundingMode.HALF_UP), this.currency);
    }
    
    /**
     * Divide by a factor
     * @param factor Division factor
     * @return New Money object with divided amount
     * @throws IllegalArgumentException if factor is zero
     */
    public Money divide(double factor) {
        if (factor == 0.0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return divide(BigDecimal.valueOf(factor));
    }
    
    /**
     * Check if this money is greater than another
     * @param other Money to compare
     * @return true if this amount is greater
     * @throws IllegalArgumentException if currencies don't match
     */
    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }
    
    /**
     * Check if this money is less than another
     * @param other Money to compare
     * @return true if this amount is less
     * @throws IllegalArgumentException if currencies don't match
     */
    public boolean isLessThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }
    
    /**
     * Check if this money is equal to another
     * @param other Money to compare
     * @return true if amounts are equal
     * @throws IllegalArgumentException if currencies don't match
     */
    public boolean isEqualTo(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) == 0;
    }
    
    /**
     * Check if this money is zero
     * @return true if amount is zero
     */
    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Check if this money is positive
     * @return true if amount is positive
     */
    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Check if this money is negative
     * @return true if amount is negative
     */
    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * Get absolute value
     * @return New Money object with absolute amount
     */
    public Money abs() {
        return new Money(this.amount.abs(), this.currency);
    }
    
    /**
     * Get negative value
     * @return New Money object with negative amount
     */
    public Money negate() {
        return new Money(this.amount.negate(), this.currency);
    }
    
    /**
     * Convert to different currency (simplified conversion)
     * @param targetCurrency Target currency
     * @param exchangeRate Exchange rate from current currency to target
     * @return New Money object in target currency
     * @throws IllegalArgumentException if target currency or exchange rate is null
     */
    public Money convertTo(Currency targetCurrency, BigDecimal exchangeRate) {
        if (targetCurrency == null) {
            throw new IllegalArgumentException("Target currency cannot be null");
        }
        if (exchangeRate == null) {
            throw new IllegalArgumentException("Exchange rate cannot be null");
        }
        if (exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive");
        }
        
        BigDecimal convertedAmount = this.amount.multiply(exchangeRate);
        return new Money(convertedAmount, targetCurrency);
    }
    
    /**
     * Convert to different currency (simplified conversion)
     * @param targetCurrency Target currency
     * @param exchangeRate Exchange rate from current currency to target
     * @return New Money object in target currency
     * @throws IllegalArgumentException if target currency is null or exchange rate is invalid
     */
    public Money convertTo(Currency targetCurrency, double exchangeRate) {
        if (exchangeRate <= 0.0) {
            throw new IllegalArgumentException("Exchange rate must be positive");
        }
        return convertTo(targetCurrency, BigDecimal.valueOf(exchangeRate));
    }
    
    /**
     * Get formatted string representation
     * @return Formatted money string
     */
    public String getFormattedString() {
        return String.format("%s %s", 
            currency.getSymbol(), 
            amount.toPlainString()
        );
    }
    
    /**
     * Get detailed string representation
     * @return Detailed money string
     */
    public String getDetailedString() {
        return String.format("%s %s (%s)", 
            amount.toPlainString(),
            currency.getCurrencyCode(),
            currency.getDisplayName()
        );
    }
    
    /**
     * Validate that two money objects have the same currency
     * @param other Money to validate
     * @throws IllegalArgumentException if currencies don't match
     */
    private void validateSameCurrency(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("Money cannot be null");
        }
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                String.format("Currency mismatch: %s vs %s", 
                    this.currency.getCurrencyCode(), 
                    other.currency.getCurrencyCode())
            );
        }
    }
    
    /**
     * Get appropriate scale for currency
     * @param currency Currency
     * @return Scale for the currency
     */
    private static int getScaleForCurrency(Currency currency) {
        // Most currencies use 2 decimal places
        // Some currencies like JPY don't use decimal places
        if (JPY.equals(currency)) {
            return 0;
        }
        return DEFAULT_SCALE;
    }
    
    /**
     * Create zero money in specified currency
     * @param currency Currency
     * @return Zero money object
     */
    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }
    
    /**
     * Create zero money in specified currency code
     * @param currencyCode Currency code
     * @return Zero money object
     */
    public static Money zero(String currencyCode) {
        return new Money(BigDecimal.ZERO, currencyCode);
    }
    
    /**
     * Create money from string representation
     * @param amountString Amount as string
     * @param currency Currency
     * @return Money object
     * @throws NumberFormatException if amount string is invalid
     */
    public static Money valueOf(String amountString, Currency currency) {
        return new Money(new BigDecimal(amountString), currency);
    }
    
    /**
     * Create money from string representation
     * @param amountString Amount as string
     * @param currencyCode Currency code
     * @return Money object
     * @throws NumberFormatException if amount string is invalid
     */
    public static Money valueOf(String amountString, String currencyCode) {
        return new Money(new BigDecimal(amountString), currencyCode);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        return Objects.equals(amount, money.amount) && 
               Objects.equals(currency, money.currency);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
    
    @Override
    public String toString() {
        return String.format("Money{amount=%s, currency=%s}", 
            amount.toPlainString(), 
            currency.getCurrencyCode()
        );
    }
}

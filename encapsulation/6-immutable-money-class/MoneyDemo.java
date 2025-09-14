
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Demo class to demonstrate Immutable Money Class
 */
public class MoneyDemo {
    public static void main(String[] args) {
        System.out.println("=== Immutable Money Class Demo ===\n");
        
        // Test basic money creation
        testBasicMoneyCreation();
        
        // Test money operations
        testMoneyOperations();
        
        // Test currency handling
        testCurrencyHandling();
        
        // Test money comparisons
        testMoneyComparisons();
        
        // Test money conversions
        testMoneyConversions();
        
        // Test immutability
        testImmutability();
        
        // Test edge cases
        testEdgeCases();
    }
    
    private static void testBasicMoneyCreation() {
        System.out.println("=== Basic Money Creation Test ===");
        
        // Create money with BigDecimal and Currency
        Money money1 = new Money(new BigDecimal("100.50"), Money.USD);
        System.out.println("Money 1: " + money1);
        System.out.println("Formatted: " + money1.getFormattedString());
        System.out.println("Detailed: " + money1.getDetailedString());
        
        // Create money with double and Currency
        Money money2 = new Money(250.75, Money.EUR);
        System.out.println("Money 2: " + money2);
        System.out.println("Formatted: " + money2.getFormattedString());
        
        // Create money with BigDecimal and currency code
        Money money3 = new Money(new BigDecimal("1000"), "JPY");
        System.out.println("Money 3: " + money3);
        System.out.println("Formatted: " + money3.getFormattedString());
        
        // Create money with double and currency code
        Money money4 = new Money(500.25, "GBP");
        System.out.println("Money 4: " + money4);
        System.out.println("Formatted: " + money4.getFormattedString());
        
        // Create zero money
        Money zeroUsd = Money.zero(Money.USD);
        System.out.println("Zero USD: " + zeroUsd);
        
        // Create money from string
        Money money5 = Money.valueOf("123.45", "INR");
        System.out.println("Money from string: " + money5);
        
        System.out.println();
    }
    
    private static void testMoneyOperations() {
        System.out.println("=== Money Operations Test ===");
        
        Money money1 = new Money(100.50, Money.USD);
        Money money2 = new Money(25.25, Money.USD);
        
        System.out.println("Money 1: " + money1);
        System.out.println("Money 2: " + money2);
        
        // Addition
        Money sum = money1.add(money2);
        System.out.println("Sum: " + sum);
        
        // Subtraction
        Money difference = money1.subtract(money2);
        System.out.println("Difference: " + difference);
        
        // Multiplication
        Money multiplied = money1.multiply(2.5);
        System.out.println("Multiplied by 2.5: " + multiplied);
        
        Money multipliedBigDecimal = money1.multiply(new BigDecimal("1.5"));
        System.out.println("Multiplied by 1.5: " + multipliedBigDecimal);
        
        // Division
        Money divided = money1.divide(2.0);
        System.out.println("Divided by 2.0: " + divided);
        
        Money dividedBigDecimal = money1.divide(new BigDecimal("4"));
        System.out.println("Divided by 4: " + dividedBigDecimal);
        
        // Absolute value
        Money negative = new Money(-50.25, Money.USD);
        Money absolute = negative.abs();
        System.out.println("Negative: " + negative);
        System.out.println("Absolute: " + absolute);
        
        // Negate
        Money negated = money1.negate();
        System.out.println("Negated: " + negated);
        
        System.out.println();
    }
    
    private static void testCurrencyHandling() {
        System.out.println("=== Currency Handling Test ===");
        
        Money usdMoney = new Money(100.50, Money.USD);
        Money eurMoney = new Money(85.25, Money.EUR);
        Money jpyMoney = new Money(10000, Money.JPY);
        
        System.out.println("USD Money: " + usdMoney);
        System.out.println("EUR Money: " + eurMoney);
        System.out.println("JPY Money: " + jpyMoney);
        
        // Test currency validation
        try {
            usdMoney.add(eurMoney);
            System.out.println("ERROR: Should not allow different currencies");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly prevented different currency addition: " + e.getMessage());
        }
        
        try {
            usdMoney.subtract(eurMoney);
            System.out.println("ERROR: Should not allow different currencies");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly prevented different currency subtraction: " + e.getMessage());
        }
        
        try {
            usdMoney.isGreaterThan(eurMoney);
            System.out.println("ERROR: Should not allow different currency comparison");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly prevented different currency comparison: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void testMoneyComparisons() {
        System.out.println("=== Money Comparisons Test ===");
        
        Money money1 = new Money(100.50, Money.USD);
        Money money2 = new Money(75.25, Money.USD);
        Money money3 = new Money(100.50, Money.USD);
        
        System.out.println("Money 1: " + money1);
        System.out.println("Money 2: " + money2);
        System.out.println("Money 3: " + money3);
        
        // Greater than
        System.out.println("Money 1 > Money 2: " + money1.isGreaterThan(money2));
        System.out.println("Money 2 > Money 1: " + money2.isGreaterThan(money1));
        
        // Less than
        System.out.println("Money 1 < Money 2: " + money1.isLessThan(money2));
        System.out.println("Money 2 < Money 1: " + money2.isLessThan(money1));
        
        // Equal to
        System.out.println("Money 1 == Money 3: " + money1.isEqualTo(money3));
        System.out.println("Money 1 == Money 2: " + money1.isEqualTo(money2));
        
        // Zero, positive, negative
        Money zero = Money.zero(Money.USD);
        Money negative = new Money(-25.50, Money.USD);
        
        System.out.println("Zero is zero: " + zero.isZero());
        System.out.println("Zero is positive: " + zero.isPositive());
        System.out.println("Zero is negative: " + zero.isNegative());
        
        System.out.println("Money 1 is positive: " + money1.isPositive());
        System.out.println("Negative is negative: " + negative.isNegative());
        
        System.out.println();
    }
    
    private static void testMoneyConversions() {
        System.out.println("=== Money Conversions Test ===");
        
        Money usdMoney = new Money(100.00, Money.USD);
        System.out.println("Original USD: " + usdMoney);
        
        // Convert to EUR (1 USD = 0.85 EUR)
        Money eurMoney = usdMoney.convertTo(Money.EUR, 0.85);
        System.out.println("Converted to EUR: " + eurMoney);
        
        // Convert to JPY (1 USD = 110 JPY)
        Money jpyMoney = usdMoney.convertTo(Money.JPY, 110.0);
        System.out.println("Converted to JPY: " + jpyMoney);
        
        // Convert to GBP (1 USD = 0.75 GBP)
        Money gbpMoney = usdMoney.convertTo(Money.GBP, new BigDecimal("0.75"));
        System.out.println("Converted to GBP: " + gbpMoney);
        
        // Test invalid conversion
        try {
            Money invalid = usdMoney.convertTo(Money.EUR, -0.85);
            System.out.println("ERROR: Should not allow negative exchange rate");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly prevented negative exchange rate: " + e.getMessage());
        }
        
        try {
            Money invalid = usdMoney.convertTo(Money.EUR, 0.0);
            System.out.println("ERROR: Should not allow zero exchange rate");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly prevented zero exchange rate: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void testImmutability() {
        System.out.println("=== Immutability Test ===");
        
        Money original = new Money(100.50, Money.USD);
        System.out.println("Original: " + original);
        
        // Perform operations
        Money added = original.add(new Money(25.25, Money.USD));
        Money multiplied = original.multiply(2.0);
        Money negated = original.negate();
        
        System.out.println("After operations:");
        System.out.println("Original: " + original);
        System.out.println("Added: " + added);
        System.out.println("Multiplied: " + multiplied);
        System.out.println("Negated: " + negated);
        
        // Verify original is unchanged
        System.out.println("Original unchanged: " + (original.getAmount().equals(new BigDecimal("100.50"))));
        
        // Test that we cannot modify the amount directly
        BigDecimal amount = original.getAmount();
        System.out.println("Amount from getter: " + amount);
        
        // The BigDecimal returned is also immutable, but let's test
        try {
            // This would throw UnsupportedOperationException if BigDecimal was mutable
            // amount.add(new BigDecimal("10")); // This creates a new BigDecimal
            BigDecimal newAmount = amount.add(new BigDecimal("10"));
            System.out.println("New amount (doesn't affect original): " + newAmount);
            System.out.println("Original amount still: " + original.getAmount());
        } catch (Exception e) {
            System.out.println("Exception during amount modification: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void testEdgeCases() {
        System.out.println("=== Edge Cases Test ===");
        
        // Test null inputs
        try {
            new Money((BigDecimal) null, Money.USD);
            System.out.println("ERROR: Should not allow null amount");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly prevented null amount: " + e.getMessage());
        }
        
        try {
            new Money(new BigDecimal("100"), (Currency) null);
            System.out.println("ERROR: Should not allow null currency");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly prevented null currency: " + e.getMessage());
        }
        
        try {
            new Money((BigDecimal) null, (Currency) null);
            System.out.println("ERROR: Should not allow null amount and currency");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly prevented null amount and currency: " + e.getMessage());
        }
        
        // Test division by zero
        Money money = new Money(100.50, Money.USD);
        try {
            money.divide(0.0);
            System.out.println("ERROR: Should not allow division by zero");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly prevented division by zero: " + e.getMessage());
        }
        
        try {
            money.divide(BigDecimal.ZERO);
            System.out.println("ERROR: Should not allow division by zero");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly prevented division by zero: " + e.getMessage());
        }
        
        // Test invalid currency code
        try {
            new Money(100.50, "INVALID");
            System.out.println("ERROR: Should not allow invalid currency code");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly prevented invalid currency code: " + e.getMessage());
        }
        
        // Test very large amounts
        Money largeAmount = new Money(new BigDecimal("999999999.99"), Money.USD);
        System.out.println("Large amount: " + largeAmount);
        
        // Test very small amounts
        Money smallAmount = new Money(new BigDecimal("0.01"), Money.USD);
        System.out.println("Small amount: " + smallAmount);
        
        // Test precision
        Money preciseAmount = new Money(new BigDecimal("123.456789"), Money.USD);
        System.out.println("Precise amount (rounded): " + preciseAmount);
        
        System.out.println();
    }
    
    /**
     * Test complex money calculations
     */
    private static void testComplexCalculations() {
        System.out.println("=== Complex Calculations Test ===");
        
        // Create a shopping cart scenario
        Money item1 = new Money(29.99, Money.USD);
        Money item2 = new Money(15.50, Money.USD);
        Money item3 = new Money(8.75, Money.USD);
        
        System.out.println("Item 1: " + item1);
        System.out.println("Item 2: " + item2);
        System.out.println("Item 3: " + item3);
        
        // Calculate subtotal
        Money subtotal = item1.add(item2).add(item3);
        System.out.println("Subtotal: " + subtotal);
        
        // Calculate tax (8.5%)
        Money tax = subtotal.multiply(0.085);
        System.out.println("Tax (8.5%): " + tax);
        
        // Calculate total
        Money total = subtotal.add(tax);
        System.out.println("Total: " + total);
        
        // Apply discount (10%)
        Money discount = total.multiply(0.10);
        Money finalTotal = total.subtract(discount);
        System.out.println("Discount (10%): " + discount);
        System.out.println("Final Total: " + finalTotal);
        
        // Convert to different currencies
        Money eurTotal = finalTotal.convertTo(Money.EUR, 0.85);
        Money jpyTotal = finalTotal.convertTo(Money.JPY, 110.0);
        
        System.out.println("Final Total in EUR: " + eurTotal);
        System.out.println("Final Total in JPY: " + jpyTotal);
        
        System.out.println();
    }
}

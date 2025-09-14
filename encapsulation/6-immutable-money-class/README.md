# Immutable Money Class

## Problem Statement
Create a Money class that represents currency + amount. Immutable → once created, cannot change value. Support operations like add(), subtract() that return new objects.

## Solution Overview

### Key Features
1. **Immutability**: Once created, Money objects cannot be modified
2. **Currency Support**: Full support for different currencies
3. **Precision**: Uses BigDecimal for precise monetary calculations
4. **Operations**: All operations return new Money objects
5. **Validation**: Comprehensive input validation and error handling

### Encapsulation Principles Demonstrated

#### 1. Data Hiding
- Amount and currency are private and final
- No direct access to internal state
- All access through controlled methods

#### 2. Immutability
- All fields are final
- No setter methods provided
- All operations return new objects

#### 3. Validation
- Input validation in constructors
- Currency validation for operations
- Error handling for invalid inputs

#### 4. Encapsulation
- Internal implementation details are hidden
- Public interface is clean and simple
- No way to modify state after creation

## Class Structure

### Money Class
```java
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;
    
    // Constructors
    public Money(BigDecimal amount, Currency currency)
    public Money(BigDecimal amount, String currencyCode)
    public Money(double amount, Currency currency)
    public Money(double amount, String currencyCode)
    
    // Getters
    public BigDecimal getAmount()
    public Currency getCurrency()
    
    // Operations (return new objects)
    public Money add(Money other)
    public Money subtract(Money other)
    public Money multiply(BigDecimal factor)
    public Money multiply(double factor)
    public Money divide(BigDecimal factor)
    public Money divide(double factor)
    
    // Comparisons
    public boolean isGreaterThan(Money other)
    public boolean isLessThan(Money other)
    public boolean isEqualTo(Money other)
    public boolean isZero()
    public boolean isPositive()
    public boolean isNegative()
    
    // Utility methods
    public Money abs()
    public Money negate()
    public Money convertTo(Currency targetCurrency, BigDecimal exchangeRate)
    
    // Static factory methods
    public static Money zero(Currency currency)
    public static Money valueOf(String amountString, Currency currency)
}
```

## Usage Examples

### Basic Money Creation
```java
// Create money with BigDecimal and Currency
Money money1 = new Money(new BigDecimal("100.50"), Money.USD);

// Create money with double and Currency
Money money2 = new Money(250.75, Money.EUR);

// Create money with currency code
Money money3 = new Money(1000, "JPY");

// Create zero money
Money zero = Money.zero(Money.USD);

// Create from string
Money money4 = Money.valueOf("123.45", "INR");
```

### Money Operations
```java
Money money1 = new Money(100.50, Money.USD);
Money money2 = new Money(25.25, Money.USD);

// Addition
Money sum = money1.add(money2);

// Subtraction
Money difference = money1.subtract(money2);

// Multiplication
Money multiplied = money1.multiply(2.5);

// Division
Money divided = money1.divide(2.0);

// Absolute value
Money absolute = money1.abs();

// Negate
Money negated = money1.negate();
```

### Money Comparisons
```java
Money money1 = new Money(100.50, Money.USD);
Money money2 = new Money(75.25, Money.USD);

// Comparisons
boolean isGreater = money1.isGreaterThan(money2);
boolean isLess = money1.isLessThan(money2);
boolean isEqual = money1.isEqualTo(money2);

// Status checks
boolean isZero = money1.isZero();
boolean isPositive = money1.isPositive();
boolean isNegative = money1.isNegative();
```

### Currency Conversions
```java
Money usdMoney = new Money(100.00, Money.USD);

// Convert to different currencies
Money eurMoney = usdMoney.convertTo(Money.EUR, 0.85);
Money jpyMoney = usdMoney.convertTo(Money.JPY, 110.0);
Money gbpMoney = usdMoney.convertTo(Money.GBP, new BigDecimal("0.75"));
```

## Immutability Features

### 1. Final Fields
- All fields are declared as final
- Cannot be modified after construction
- Thread-safe by design

### 2. No Setters
- No setter methods provided
- No way to modify state after creation
- All changes through operations

### 3. New Object Creation
- All operations return new Money objects
- Original objects remain unchanged
- No side effects

### 4. Immutable Dependencies
- Uses BigDecimal (immutable) for amounts
- Uses Currency (immutable) for currency
- All dependencies are immutable

## Currency Support

### Supported Currencies
- **USD**: US Dollar
- **EUR**: Euro
- **GBP**: British Pound
- **JPY**: Japanese Yen
- **INR**: Indian Rupee
- **And many more**: Any valid ISO currency code

### Currency Validation
- Validates currency codes
- Handles currency-specific precision
- Prevents operations between different currencies

### Precision Handling
- **Most currencies**: 2 decimal places
- **JPY**: 0 decimal places (no cents)
- **Customizable**: Based on currency requirements

## Error Handling

### Input Validation
- **Null checks**: Prevents null amounts and currencies
- **Format validation**: Validates currency codes
- **Range validation**: Prevents invalid exchange rates

### Operation Validation
- **Currency matching**: Operations only between same currencies
- **Division by zero**: Prevents division by zero
- **Invalid conversions**: Prevents invalid currency conversions

### Exception Types
- **IllegalArgumentException**: For invalid inputs
- **NumberFormatException**: For invalid number formats
- **ArithmeticException**: For mathematical errors

## Performance Considerations

### Memory Usage
- **Immutable objects**: No defensive copying needed
- **Shared references**: Safe to share between threads
- **Garbage collection**: Old objects can be garbage collected

### Calculation Performance
- **BigDecimal operations**: Precise but slower than double
- **Object creation**: New objects for each operation
- **Caching**: Consider caching for frequently used values

## Best Practices

1. **Use BigDecimal** for monetary calculations
2. **Validate inputs** in constructors
3. **Handle exceptions** properly
4. **Use appropriate precision** for currencies
5. **Consider performance** for high-frequency operations
6. **Document currency requirements** clearly
7. **Test edge cases** thoroughly

## Anti-Patterns Avoided

- ❌ Using double for monetary calculations
- ❌ Allowing mutable state
- ❌ Not validating inputs
- ❌ Mixing different currencies in operations
- ❌ Not handling precision correctly
- ❌ Exposing internal implementation details
- ❌ Not providing proper error messages
- ❌ Allowing division by zero

## Use Cases

### Financial Applications
- **Banking systems**: Account balances, transactions
- **E-commerce**: Product prices, order totals
- **Trading systems**: Stock prices, portfolio values
- **Accounting**: Ledger entries, financial reports

### International Applications
- **Multi-currency support**: Different currencies
- **Exchange rate handling**: Currency conversions
- **Localization**: Currency-specific formatting
- **Compliance**: Financial regulations

### Scientific Applications
- **Precision calculations**: High-precision arithmetic
- **Research**: Financial modeling
- **Simulations**: Economic scenarios
- **Analysis**: Statistical calculations

## Thread Safety

### Immutable Design
- **Thread-safe by design**: No mutable state
- **No synchronization needed**: No shared mutable state
- **Safe sharing**: Can be shared between threads
- **No race conditions**: No concurrent modifications

### Concurrent Usage
- **Safe for concurrent access**: Multiple threads can use same object
- **No locking required**: No synchronization overhead
- **Predictable behavior**: Consistent results across threads
- **Performance**: No contention or blocking

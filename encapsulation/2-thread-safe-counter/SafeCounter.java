
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-Safe Counter
 * 
 * This class demonstrates encapsulation by:
 * 1. Encapsulating internal count with private field
 * 2. Ensuring thread-safe increments/decrements using AtomicInteger
 * 3. Preventing direct access to raw integer
 * 4. Providing controlled interface for counter operations
 */
public class SafeCounter {
    // Encapsulated counter using AtomicInteger for thread safety
    private final AtomicInteger count;
    
    // Optional: Track maximum value reached
    private final AtomicInteger maxValue;
    
    // Optional: Track minimum value reached
    private final AtomicInteger minValue;
    
    /**
     * Constructor with initial value
     * @param initialValue Starting value for the counter
     */
    public SafeCounter(int initialValue) {
        this.count = new AtomicInteger(initialValue);
        this.maxValue = new AtomicInteger(initialValue);
        this.minValue = new AtomicInteger(initialValue);
    }
    
    /**
     * Default constructor starting at 0
     */
    public SafeCounter() {
        this(0);
    }
    
    /**
     * Increment counter by 1
     * @return New value after increment
     */
    public int increment() {
        int newValue = count.incrementAndGet();
        updateMinMax(newValue);
        return newValue;
    }
    
    /**
     * Decrement counter by 1
     * @return New value after decrement
     */
    public int decrement() {
        int newValue = count.decrementAndGet();
        updateMinMax(newValue);
        return newValue;
    }
    
    /**
     * Add specified value to counter
     * @param value Value to add (can be negative)
     * @return New value after addition
     */
    public int add(int value) {
        int newValue = count.addAndGet(value);
        updateMinMax(newValue);
        return newValue;
    }
    
    /**
     * Subtract specified value from counter
     * @param value Value to subtract
     * @return New value after subtraction
     */
    public int subtract(int value) {
        return add(-value);
    }
    
    /**
     * Get current counter value
     * @return Current value
     */
    public int getValue() {
        return count.get();
    }
    
    /**
     * Set counter to specific value
     * @param value New value to set
     * @return Previous value
     */
    public int setValue(int value) {
        int previousValue = count.getAndSet(value);
        updateMinMax(value);
        return previousValue;
    }
    
    /**
     * Reset counter to 0
     * @return Previous value
     */
    public int reset() {
        return setValue(0);
    }
    
    /**
     * Check if counter is zero
     * @return true if counter is 0
     */
    public boolean isZero() {
        return count.get() == 0;
    }
    
    /**
     * Check if counter is positive
     * @return true if counter > 0
     */
    public boolean isPositive() {
        return count.get() > 0;
    }
    
    /**
     * Check if counter is negative
     * @return true if counter < 0
     */
    public boolean isNegative() {
        return count.get() < 0;
    }
    
    /**
     * Get maximum value reached
     * @return Maximum value ever reached
     */
    public int getMaxValue() {
        return maxValue.get();
    }
    
    /**
     * Get minimum value reached
     * @return Minimum value ever reached
     */
    public int getMinValue() {
        return minValue.get();
    }
    
    /**
     * Compare and set value atomically
     * @param expected Expected current value
     * @param newValue New value to set
     * @return true if value was set successfully
     */
    public boolean compareAndSet(int expected, int newValue) {
        boolean success = count.compareAndSet(expected, newValue);
        if (success) {
            updateMinMax(newValue);
        }
        return success;
    }
    
    /**
     * Get value and increment atomically
     * @return Value before increment
     */
    public int getAndIncrement() {
        int value = count.getAndIncrement();
        updateMinMax(value + 1);
        return value;
    }
    
    /**
     * Get value and decrement atomically
     * @return Value before decrement
     */
    public int getAndDecrement() {
        int value = count.getAndDecrement();
        updateMinMax(value - 1);
        return value;
    }
    
    /**
     * Update min/max values if necessary
     * @param newValue New value to check
     */
    private void updateMinMax(int newValue) {
        // Update maximum value
        int currentMax = maxValue.get();
        while (newValue > currentMax && !maxValue.compareAndSet(currentMax, newValue)) {
            currentMax = maxValue.get();
        }
        
        // Update minimum value
        int currentMin = minValue.get();
        while (newValue < currentMin && !minValue.compareAndSet(currentMin, newValue)) {
            currentMin = minValue.get();
        }
    }
    
    @Override
    public String toString() {
        return "SafeCounter{value=" + count.get() + 
               ", max=" + maxValue.get() + 
               ", min=" + minValue.get() + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SafeCounter that = (SafeCounter) obj;
        return count.get() == that.count.get();
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(count.get());
    }
}

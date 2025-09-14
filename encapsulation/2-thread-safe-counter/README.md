# Thread-Safe Counter

## Problem Statement
Implement a SafeCounter that encapsulates internal count. Ensure increments/decrements are thread-safe using synchronization or atomic variables. Prevent direct access to raw integer.

## Solution Overview

### Key Features
1. **Thread Safety**: Uses `AtomicInteger` for lock-free thread safety
2. **Encapsulation**: Private counter field with controlled access
3. **Atomic Operations**: All operations are atomic and thread-safe
4. **Min/Max Tracking**: Tracks minimum and maximum values reached
5. **Rich Interface**: Comprehensive set of counter operations

### Encapsulation Principles Demonstrated

#### 1. Data Hiding
- Counter value is private and cannot be accessed directly
- Internal state is completely encapsulated
- No public fields exposed

#### 2. Controlled Access
- Only getter methods provide read access
- All modifications through controlled methods
- No direct field manipulation possible

#### 3. Thread Safety
- Uses `AtomicInteger` for lock-free thread safety
- All operations are atomic
- No race conditions possible

#### 4. Immutable State Tracking
- Min/max values are tracked but not modifiable externally
- Historical information preserved
- No way to reset min/max values

## Class Structure

### SafeCounter Class
```java
public class SafeCounter {
    private final AtomicInteger count;
    private final AtomicInteger maxValue;
    private final AtomicInteger minValue;
    
    // Constructors
    public SafeCounter(int initialValue)
    public SafeCounter()
    
    // Basic operations
    public int increment()
    public int decrement()
    public int add(int value)
    public int subtract(int value)
    
    // Value access
    public int getValue()
    public int setValue(int value)
    public int reset()
    
    // Status checks
    public boolean isZero()
    public boolean isPositive()
    public boolean isNegative()
    
    // Min/Max tracking
    public int getMaxValue()
    public int getMinValue()
    
    // Atomic operations
    public boolean compareAndSet(int expected, int newValue)
    public int getAndIncrement()
    public int getAndDecrement()
}
```

## Usage Examples

### Basic Operations
```java
SafeCounter counter = new SafeCounter(0);

// Increment and decrement
counter.increment();        // Returns 1
counter.decrement();        // Returns 0

// Add and subtract
counter.add(10);            // Returns 10
counter.subtract(3);        // Returns 7

// Set and reset
counter.setValue(100);      // Returns 7
counter.reset();            // Returns 100, counter becomes 0
```

### Thread-Safe Operations
```java
SafeCounter counter = new SafeCounter(0);

// Multiple threads can safely operate on the same counter
ExecutorService executor = Executors.newFixedThreadPool(4);
for (int i = 0; i < 4; i++) {
    executor.submit(() -> {
        for (int j = 0; j < 1000; j++) {
            counter.increment();
        }
    });
}
// Final value will be exactly 4000
```

### Atomic Operations
```java
SafeCounter counter = new SafeCounter(10);

// Compare and set
boolean success = counter.compareAndSet(10, 20);  // true
boolean failed = counter.compareAndSet(10, 30);   // false

// Get and operations
int oldValue = counter.getAndIncrement();  // Returns 20, counter becomes 21
int newValue = counter.getAndDecrement();  // Returns 21, counter becomes 20
```

## Thread Safety Mechanisms

### 1. AtomicInteger
- Lock-free implementation
- Uses compare-and-swap (CAS) operations
- No blocking or waiting
- High performance under contention

### 2. Atomic Operations
- All counter operations are atomic
- No intermediate states visible to other threads
- Consistent state maintained at all times

### 3. Min/Max Tracking
- Uses atomic operations for thread-safe updates
- No lost updates possible
- Historical data preserved correctly

## Performance Characteristics

### Advantages
- **Lock-free**: No blocking or waiting
- **Scalable**: Performance doesn't degrade with contention
- **Consistent**: Always returns correct values
- **Efficient**: Minimal overhead compared to synchronized methods

### Use Cases
- **Counters**: Event counting, metrics collection
- **Resource Management**: Connection pools, rate limiting
- **Coordination**: Thread coordination, barriers
- **Statistics**: Performance monitoring, analytics

## Comparison with Synchronized Approach

### Synchronized Counter (Not Implemented)
```java
public class SynchronizedCounter {
    private int count = 0;
    
    public synchronized int increment() {
        return ++count;
    }
    
    public synchronized int decrement() {
        return --count;
    }
}
```

### Atomic Counter (Our Implementation)
```java
public class SafeCounter {
    private final AtomicInteger count = new AtomicInteger(0);
    
    public int increment() {
        return count.incrementAndGet();
    }
    
    public int decrement() {
        return count.decrementAndGet();
    }
}
```

### Performance Comparison
- **AtomicInteger**: Better performance under high contention
- **Synchronized**: Better for simple cases with low contention
- **Memory**: AtomicInteger uses more memory
- **Complexity**: AtomicInteger is simpler to use correctly

## Best Practices

1. **Use AtomicInteger** for high-performance counters
2. **Avoid unnecessary operations** - batch when possible
3. **Consider memory ordering** for complex scenarios
4. **Test thoroughly** with multiple threads
5. **Document thread safety** in your API

## Anti-Patterns Avoided

- ❌ Exposing raw integer fields
- ❌ Using non-atomic operations in multi-threaded environment
- ❌ Allowing direct field modification
- ❌ Not handling overflow/underflow cases
- ❌ Mixing synchronized and atomic operations

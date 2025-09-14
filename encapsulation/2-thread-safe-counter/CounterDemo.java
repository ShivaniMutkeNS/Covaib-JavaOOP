
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demo class to demonstrate the Thread-Safe Counter
 */
public class CounterDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread-Safe Counter Demo ===\n");
        
        // Test basic operations
        testBasicOperations();
        
        // Test thread safety
        testThreadSafety();
        
        // Test concurrent access
        testConcurrentAccess();
        
        // Test min/max tracking
        testMinMaxTracking();
    }
    
    private static void testBasicOperations() {
        System.out.println("=== Basic Operations Test ===");
        
        SafeCounter counter = new SafeCounter(10);
        System.out.println("Initial value: " + counter.getValue());
        
        // Test increment
        System.out.println("After increment: " + counter.increment());
        System.out.println("After increment: " + counter.increment());
        
        // Test decrement
        System.out.println("After decrement: " + counter.decrement());
        
        // Test add
        System.out.println("After adding 5: " + counter.add(5));
        
        // Test subtract
        System.out.println("After subtracting 3: " + counter.subtract(3));
        
        // Test set value
        System.out.println("After setting to 20: " + counter.setValue(20));
        
        // Test reset
        System.out.println("After reset: " + counter.reset());
        
        // Test boolean methods
        System.out.println("Is zero: " + counter.isZero());
        System.out.println("Is positive: " + counter.isPositive());
        System.out.println("Is negative: " + counter.isNegative());
        
        System.out.println("Final counter: " + counter);
        System.out.println();
    }
    
    private static void testThreadSafety() throws InterruptedException {
        System.out.println("=== Thread Safety Test ===");
        
        SafeCounter counter = new SafeCounter(0);
        int numThreads = 10;
        int operationsPerThread = 1000;
        
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        // Each thread will increment the counter 1000 times
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        counter.increment();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // Wait for all threads to complete
        latch.await();
        executor.shutdown();
        
        int expectedValue = numThreads * operationsPerThread;
        int actualValue = counter.getValue();
        
        System.out.println("Expected value: " + expectedValue);
        System.out.println("Actual value: " + actualValue);
        System.out.println("Thread safety test: " + (expectedValue == actualValue ? "PASSED" : "FAILED"));
        System.out.println();
    }
    
    private static void testConcurrentAccess() throws InterruptedException {
        System.out.println("=== Concurrent Access Test ===");
        
        SafeCounter counter = new SafeCounter(0);
        int numThreads = 5;
        int operationsPerThread = 200;
        
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        // Mix of increment and decrement operations
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        if (threadId % 2 == 0) {
                            counter.increment();
                        } else {
                            counter.decrement();
                        }
                        
                        // Random add/subtract operations
                        if (j % 10 == 0) {
                            counter.add(threadId);
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // Wait for all threads to complete
        latch.await();
        executor.shutdown();
        
        System.out.println("Final counter value: " + counter.getValue());
        System.out.println("Max value reached: " + counter.getMaxValue());
        System.out.println("Min value reached: " + counter.getMinValue());
        System.out.println();
    }
    
    private static void testMinMaxTracking() {
        System.out.println("=== Min/Max Tracking Test ===");
        
        SafeCounter counter = new SafeCounter(0);
        
        // Test various operations
        counter.add(100);
        counter.subtract(50);
        counter.add(200);
        counter.subtract(300);
        counter.add(75);
        
        System.out.println("Current value: " + counter.getValue());
        System.out.println("Max value reached: " + counter.getMaxValue());
        System.out.println("Min value reached: " + counter.getMinValue());
        
        // Test compare and set
        System.out.println("\n=== Compare and Set Test ===");
        boolean success1 = counter.compareAndSet(25, 50);
        System.out.println("CAS(25, 50): " + success1 + ", value: " + counter.getValue());
        
        boolean success2 = counter.compareAndSet(50, 100);
        System.out.println("CAS(50, 100): " + success2 + ", value: " + counter.getValue());
        
        // Test get and increment/decrement
        System.out.println("\n=== Get and Operations Test ===");
        System.out.println("Get and increment: " + counter.getAndIncrement() + ", new value: " + counter.getValue());
        System.out.println("Get and decrement: " + counter.getAndDecrement() + ", new value: " + counter.getValue());
        
        System.out.println();
    }
    
    /**
     * Demonstrate the difference between thread-safe and non-thread-safe counter
     */
    private static void demonstrateThreadSafetyDifference() throws InterruptedException {
        System.out.println("=== Thread Safety Comparison ===");
        
        // Thread-safe counter
        SafeCounter safeCounter = new SafeCounter(0);
        
        // Non-thread-safe counter (for comparison)
        AtomicInteger unsafeCounter = new AtomicInteger(0);
        
        int numThreads = 4;
        int operationsPerThread = 10000;
        
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads * 2);
        
        // Test safe counter
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        safeCounter.increment();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // Test unsafe counter (simulating unsafe operations)
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        // This is actually safe because AtomicInteger is thread-safe
                        // But we're demonstrating the concept
                        unsafeCounter.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        
        int expectedValue = numThreads * operationsPerThread;
        
        System.out.println("Expected value: " + expectedValue);
        System.out.println("Safe counter value: " + safeCounter.getValue());
        System.out.println("Unsafe counter value: " + unsafeCounter.get());
        System.out.println("Safe counter test: " + (safeCounter.getValue() == expectedValue ? "PASSED" : "FAILED"));
        System.out.println("Unsafe counter test: " + (unsafeCounter.get() == expectedValue ? "PASSED" : "FAILED"));
    }
}

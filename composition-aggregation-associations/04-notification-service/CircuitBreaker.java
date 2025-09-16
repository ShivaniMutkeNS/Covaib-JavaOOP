package composition.notification;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Circuit Breaker implementation for notification channels
 */
public class CircuitBreaker {
    private final String channelType;
    private CircuitBreakerState state;
    private int failureCount;
    private LocalDateTime lastFailureTime;
    private final int failureThreshold;
    private final long timeoutDuration; // seconds
    
    public CircuitBreaker(String channelType) {
        this.channelType = channelType;
        this.state = CircuitBreakerState.CLOSED;
        this.failureCount = 0;
        this.failureThreshold = 5;
        this.timeoutDuration = 60; // 1 minute
    }
    
    public boolean canExecute() {
        if (state == CircuitBreakerState.CLOSED) {
            return true;
        }
        
        if (state == CircuitBreakerState.OPEN) {
            if (shouldAttemptReset()) {
                state = CircuitBreakerState.HALF_OPEN;
                System.out.println("Circuit breaker for " + channelType + " moved to HALF_OPEN");
                return true;
            }
            return false;
        }
        
        // HALF_OPEN state
        return true;
    }
    
    public void recordSuccess() {
        failureCount = 0;
        if (state == CircuitBreakerState.HALF_OPEN) {
            state = CircuitBreakerState.CLOSED;
            System.out.println("Circuit breaker for " + channelType + " CLOSED after successful call");
        }
    }
    
    public void recordFailure() {
        failureCount++;
        lastFailureTime = LocalDateTime.now();
        
        if (state == CircuitBreakerState.HALF_OPEN || failureCount >= failureThreshold) {
            state = CircuitBreakerState.OPEN;
            System.out.println("Circuit breaker for " + channelType + " OPENED after " + failureCount + " failures");
        }
    }
    
    private boolean shouldAttemptReset() {
        return lastFailureTime != null && 
               ChronoUnit.SECONDS.between(lastFailureTime, LocalDateTime.now()) >= timeoutDuration;
    }
    
    public CircuitBreakerState getState() {
        return state;
    }
    
    public enum CircuitBreakerState {
        CLOSED, OPEN, HALF_OPEN
    }
}

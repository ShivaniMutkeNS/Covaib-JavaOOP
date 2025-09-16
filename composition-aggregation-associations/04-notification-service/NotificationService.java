package composition.notification;

import java.util.*;
import java.util.concurrent.*;

/**
 * MAANG-Level Notification Service using Composition
 * Demonstrates: Strategy Pattern, Composite Pattern, Async Processing, Circuit Breaker
 */
public class NotificationService {
    private final List<NotificationChannel> channels;
    private final ExecutorService executorService;
    private final Map<String, CircuitBreaker> circuitBreakers;
    private final NotificationMetrics metrics;
    
    public NotificationService() {
        this.channels = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(10);
        this.circuitBreakers = new HashMap<>();
        this.metrics = new NotificationMetrics();
    }
    
    public void addChannel(NotificationChannel channel) {
        channels.add(channel);
        circuitBreakers.put(channel.getChannelType(), new CircuitBreaker(channel.getChannelType()));
        System.out.println("Added notification channel: " + channel.getChannelType());
    }
    
    public void removeChannel(NotificationChannel channel) {
        channels.remove(channel);
        circuitBreakers.remove(channel.getChannelType());
        System.out.println("Removed notification channel: " + channel.getChannelType());
    }
    
    // Send message through all available channels
    public CompletableFuture<NotificationResult> sendToAll(NotificationMessage message) {
        List<CompletableFuture<ChannelResult>> futures = new ArrayList<>();
        
        for (NotificationChannel channel : channels) {
            CircuitBreaker circuitBreaker = circuitBreakers.get(channel.getChannelType());
            
            if (circuitBreaker.canExecute()) {
                CompletableFuture<ChannelResult> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        boolean success = channel.sendMessage(message);
                        circuitBreaker.recordSuccess();
                        metrics.recordAttempt(channel.getChannelType(), success);
                        return new ChannelResult(channel.getChannelType(), success, null);
                    } catch (Exception e) {
                        circuitBreaker.recordFailure();
                        metrics.recordAttempt(channel.getChannelType(), false);
                        return new ChannelResult(channel.getChannelType(), false, e.getMessage());
                    }
                }, executorService);
                
                futures.add(future);
            } else {
                System.out.println("Circuit breaker OPEN for " + channel.getChannelType() + " - skipping");
                futures.add(CompletableFuture.completedFuture(
                    new ChannelResult(channel.getChannelType(), false, "Circuit breaker open")));
            }
        }
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                List<ChannelResult> results = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
                
                return new NotificationResult(message.getMessageId(), results);
            });
    }
    
    // Send through specific channel types
    public CompletableFuture<ChannelResult> sendViaChannel(NotificationMessage message, String channelType) {
        return channels.stream()
            .filter(channel -> channel.getChannelType().equals(channelType))
            .findFirst()
            .map(channel -> {
                CircuitBreaker circuitBreaker = circuitBreakers.get(channelType);
                
                if (!circuitBreaker.canExecute()) {
                    return CompletableFuture.completedFuture(
                        new ChannelResult(channelType, false, "Circuit breaker open"));
                }
                
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        boolean success = channel.sendMessage(message);
                        circuitBreaker.recordSuccess();
                        metrics.recordAttempt(channelType, success);
                        return new ChannelResult(channelType, success, null);
                    } catch (Exception e) {
                        circuitBreaker.recordFailure();
                        metrics.recordAttempt(channelType, false);
                        return new ChannelResult(channelType, false, e.getMessage());
                    }
                }, executorService);
            })
            .orElse(CompletableFuture.completedFuture(
                new ChannelResult(channelType, false, "Channel not found")));
    }
    
    // Send with fallback strategy
    public CompletableFuture<ChannelResult> sendWithFallback(NotificationMessage message, 
                                                           List<String> channelPriority) {
        return sendWithFallbackRecursive(message, channelPriority, 0);
    }
    
    private CompletableFuture<ChannelResult> sendWithFallbackRecursive(NotificationMessage message, 
                                                                      List<String> channelPriority, 
                                                                      int index) {
        if (index >= channelPriority.size()) {
            return CompletableFuture.completedFuture(
                new ChannelResult("NONE", false, "All channels failed"));
        }
        
        String channelType = channelPriority.get(index);
        return sendViaChannel(message, channelType)
            .thenCompose(result -> {
                if (result.isSuccess()) {
                    return CompletableFuture.completedFuture(result);
                } else {
                    System.out.println("Channel " + channelType + " failed, trying next...");
                    return sendWithFallbackRecursive(message, channelPriority, index + 1);
                }
            });
    }
    
    public void displayMetrics() {
        System.out.println("\n=== Notification Service Metrics ===");
        System.out.println("Active Channels: " + channels.size());
        
        for (NotificationChannel channel : channels) {
            String type = channel.getChannelType();
            CircuitBreaker cb = circuitBreakers.get(type);
            System.out.printf("%s - Success Rate: %.1f%%, Circuit Breaker: %s\n", 
                            type, metrics.getSuccessRate(type), cb.getState());
        }
        
        System.out.println("Total Messages Sent: " + metrics.getTotalAttempts());
        System.out.println("Overall Success Rate: " + String.format("%.1f%%", metrics.getOverallSuccessRate()));
    }
    
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

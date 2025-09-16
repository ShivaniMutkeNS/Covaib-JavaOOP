package abstraction.loggerframework;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPOutputStream;

/**
 * Cloud Logger Implementation with HTTP endpoints, compression, and circuit breaker
 */
public class CloudLogger extends Logger {
    
    private String endpoint;
    private String apiKey;
    private boolean enableCompression;
    private boolean enableCircuitBreaker;
    private int maxConcurrentRequests;
    private int timeoutMs;
    private int retryDelayMs;
    
    // Circuit breaker state
    private AtomicBoolean circuitOpen = new AtomicBoolean(false);
    private volatile long lastFailureTime = 0;
    private volatile int consecutiveFailures = 0;
    private final int failureThreshold = 5;
    private final long circuitResetTimeMs = 60000; // 1 minute
    
    // Async processing
    private final ExecutorService executorService;
    private final Semaphore requestSemaphore;
    private final BlockingQueue<LogEntry> failedLogsQueue;
    
    public CloudLogger(String loggerId, LogLevel threshold, Map<String, Object> config) {
        super(loggerId, threshold, config);
        
        this.endpoint = (String) config.get("endpoint");
        this.apiKey = (String) config.get("api_key");
        this.enableCompression = (Boolean) config.getOrDefault("enable_compression", true);
        this.enableCircuitBreaker = (Boolean) config.getOrDefault("enable_circuit_breaker", true);
        this.maxConcurrentRequests = ((Number) config.getOrDefault("max_concurrent_requests", 10)).intValue();
        this.timeoutMs = ((Number) config.getOrDefault("timeout_ms", 5000)).intValue();
        this.retryDelayMs = ((Number) config.getOrDefault("retry_delay_ms", 1000)).intValue();
        
        this.executorService = Executors.newFixedThreadPool(maxConcurrentRequests, 
            r -> new Thread(r, "CloudLogger-" + loggerId + "-Worker"));
        this.requestSemaphore = new Semaphore(maxConcurrentRequests);
        this.failedLogsQueue = new LinkedBlockingQueue<>();
        
        // Start retry processor for failed logs
        startRetryProcessor();
        
        // Set JSON formatter for cloud logging
        setFormatter(new JsonLogFormatter());
    }
    
    @Override
    protected void writeLog(LogEntry logEntry) throws LoggingException {
        if (enableCircuitBreaker && isCircuitOpen()) {
            // Circuit is open, queue for retry later
            failedLogsQueue.offer(logEntry);
            throw new LogWriteException("Circuit breaker is open, queuing log for retry");
        }
        
        if (asyncMode) {
            // Async processing
            CompletableFuture.runAsync(() -> {
                try {
                    sendLogToCloud(logEntry);
                } catch (LoggingException e) {
                    handleCloudLoggingFailure(logEntry, e);
                }
            }, executorService);
        } else {
            // Synchronous processing
            sendLogToCloud(logEntry);
        }
    }
    
    private void sendLogToCloud(LogEntry logEntry) throws LoggingException {
        try {
            // Acquire semaphore to limit concurrent requests
            if (!requestSemaphore.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS)) {
                throw new LogWriteException("Too many concurrent requests, request timed out");
            }
            
            try {
                HttpURLConnection connection = createConnection();
                
                // Prepare payload
                String payload = logEntry.getFormattedMessage();
                byte[] payloadBytes = payload.getBytes("UTF-8");
                
                // Compress if enabled
                if (enableCompression) {
                    payloadBytes = compressPayload(payloadBytes);
                    connection.setRequestProperty("Content-Encoding", "gzip");
                }
                
                connection.setRequestProperty("Content-Length", String.valueOf(payloadBytes.length));
                
                // Send request
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(payloadBytes);
                    os.flush();
                }
                
                // Check response
                int responseCode = connection.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    // Success
                    recordSuccess();
                } else {
                    String errorMessage = readErrorResponse(connection);
                    throw new LogWriteException("Cloud logging failed with HTTP " + responseCode + ": " + errorMessage);
                }
                
            } finally {
                requestSemaphore.release();
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LogWriteException("Cloud logging interrupted", e);
        } catch (IOException e) {
            throw new LogWriteException("Cloud logging I/O error", e);
        }
    }
    
    private HttpURLConnection createConnection() throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("User-Agent", "CloudLogger/" + loggerId);
        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setDoOutput(true);
        
        return connection;
    }
    
    private byte[] compressPayload(byte[] payload) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOut = new GZIPOutputStream(baos)) {
            gzipOut.write(payload);
        }
        return baos.toByteArray();
    }
    
    private String readErrorResponse(HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (Exception e) {
            return "Unable to read error response: " + e.getMessage();
        }
    }
    
    private void handleCloudLoggingFailure(LogEntry logEntry, LoggingException e) {
        recordFailure();
        
        // Queue for retry
        failedLogsQueue.offer(logEntry);
        
        // Fallback logging to system err
        System.err.println("Cloud logging failed for " + loggerId + ": " + e.getMessage());
        System.err.println("Original log: " + logEntry.getMessage());
    }
    
    private void recordSuccess() {
        if (enableCircuitBreaker) {
            consecutiveFailures = 0;
            if (circuitOpen.get()) {
                circuitOpen.set(false);
                System.out.println("CloudLogger circuit breaker closed for " + loggerId);
            }
        }
    }
    
    private void recordFailure() {
        if (enableCircuitBreaker) {
            consecutiveFailures++;
            lastFailureTime = System.currentTimeMillis();
            
            if (consecutiveFailures >= failureThreshold && !circuitOpen.get()) {
                circuitOpen.set(true);
                System.err.println("CloudLogger circuit breaker opened for " + loggerId + 
                                 " after " + consecutiveFailures + " consecutive failures");
            }
        }
    }
    
    private boolean isCircuitOpen() {
        if (!circuitOpen.get()) {
            return false;
        }
        
        // Check if circuit should be reset
        if (System.currentTimeMillis() - lastFailureTime > circuitResetTimeMs) {
            circuitOpen.set(false);
            consecutiveFailures = 0;
            System.out.println("CloudLogger circuit breaker reset for " + loggerId);
            return false;
        }
        
        return true;
    }
    
    private void startRetryProcessor() {
        Thread retryProcessor = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    LogEntry failedLog = failedLogsQueue.poll(1, TimeUnit.SECONDS);
                    if (failedLog != null) {
                        // Wait before retry
                        Thread.sleep(retryDelayMs);
                        
                        // Only retry if circuit is not open
                        if (!enableCircuitBreaker || !isCircuitOpen()) {
                            try {
                                sendLogToCloud(failedLog);
                            } catch (LoggingException e) {
                                // If retry fails, put it back in queue (with limit to prevent infinite retries)
                                if (failedLogsQueue.size() < 1000) { // Limit queue size
                                    failedLogsQueue.offer(failedLog);
                                }
                            }
                        } else {
                            // Circuit is open, put back in queue
                            failedLogsQueue.offer(failedLog);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "CloudLogger-RetryProcessor-" + loggerId);
        
        retryProcessor.setDaemon(true);
        retryProcessor.start();
    }
    
    @Override
    public void flush() throws LoggingException {
        // Wait for all pending async operations to complete
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                System.err.println("CloudLogger flush timeout for " + loggerId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LoggingException("Flush interrupted", e);
        }
        
        // Recreate executor for future operations
        // Note: In a real implementation, you might want to handle this differently
    }
    
    @Override
    public void close() throws LoggingException {
        // Shutdown executor service
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executorService.shutdownNow();
        }
        
        // Log remaining failed logs to system err as fallback
        if (!failedLogsQueue.isEmpty()) {
            System.err.println("CloudLogger " + loggerId + " closing with " + 
                             failedLogsQueue.size() + " unprocessed failed logs");
        }
    }
    
    // Cloud-specific methods
    public int getFailedLogsQueueSize() {
        return failedLogsQueue.size();
    }
    
    public boolean isCircuitBreakerOpen() {
        return circuitOpen.get();
    }
    
    public int getConsecutiveFailures() {
        return consecutiveFailures;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public boolean isCompressionEnabled() {
        return enableCompression;
    }
}

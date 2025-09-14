package abstraction.loggerframework;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract Logger Framework with enterprise-level features
 * Supports multiple log levels, structured logging, async processing, and filtering
 */
public abstract class Logger {
    
    public enum LogLevel {
        TRACE(0), DEBUG(1), INFO(2), WARN(3), ERROR(4), FATAL(5);
        
        private final int priority;
        
        LogLevel(int priority) {
            this.priority = priority;
        }
        
        public int getPriority() { return priority; }
        
        public boolean isEnabled(LogLevel threshold) {
            return this.priority >= threshold.priority;
        }
    }
    
    protected String loggerId;
    protected LogLevel threshold;
    protected LogFormatter formatter;
    protected LogFilter filter;
    protected boolean asyncMode;
    protected Map<String, Object> configuration;
    
    public Logger(String loggerId, LogLevel threshold, Map<String, Object> config) {
        this.loggerId = loggerId;
        this.threshold = threshold;
        this.configuration = config;
        this.asyncMode = (Boolean) config.getOrDefault("async_mode", false);
        this.formatter = createDefaultFormatter();
        this.filter = createDefaultFilter();
    }
    
    /**
     * Abstract method to write log entry to specific destination
     * @param logEntry The formatted log entry to write
     * @throws LoggingException if writing fails
     */
    protected abstract void writeLog(LogEntry logEntry) throws LoggingException;
    
    /**
     * Abstract method to flush any buffered logs
     * @throws LoggingException if flushing fails
     */
    public abstract void flush() throws LoggingException;
    
    /**
     * Abstract method to close logger and cleanup resources
     * @throws LoggingException if cleanup fails
     */
    public abstract void close() throws LoggingException;
    
    // Public logging methods
    public void trace(String message) {
        log(LogLevel.TRACE, message, null, null);
    }
    
    public void trace(String message, Object... args) {
        log(LogLevel.TRACE, formatMessage(message, args), null, null);
    }
    
    public void debug(String message) {
        log(LogLevel.DEBUG, message, null, null);
    }
    
    public void debug(String message, Object... args) {
        log(LogLevel.DEBUG, formatMessage(message, args), null, null);
    }
    
    public void info(String message) {
        log(LogLevel.INFO, message, null, null);
    }
    
    public void info(String message, Object... args) {
        log(LogLevel.INFO, formatMessage(message, args), null, null);
    }
    
    public void warn(String message) {
        log(LogLevel.WARN, message, null, null);
    }
    
    public void warn(String message, Throwable throwable) {
        log(LogLevel.WARN, message, throwable, null);
    }
    
    public void error(String message) {
        log(LogLevel.ERROR, message, null, null);
    }
    
    public void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable, null);
    }
    
    public void fatal(String message) {
        log(LogLevel.FATAL, message, null, null);
    }
    
    public void fatal(String message, Throwable throwable) {
        log(LogLevel.FATAL, message, throwable, null);
    }
    
    // Structured logging with context
    public void logWithContext(LogLevel level, String message, Map<String, Object> context) {
        log(level, message, null, context);
    }
    
    public void logWithContext(LogLevel level, String message, Throwable throwable, Map<String, Object> context) {
        log(level, message, throwable, context);
    }
    
    /**
     * Template method for logging workflow
     */
    public final void log(LogLevel level, String message, Throwable throwable, Map<String, Object> context) {
        // Step 1: Check if logging is enabled for this level
        if (!isLoggingEnabled(level)) {
            return;
        }
        
        // Step 2: Create log entry
        LogEntry logEntry = createLogEntry(level, message, throwable, context);
        
        // Step 3: Apply filters
        if (!shouldLog(logEntry)) {
            return;
        }
        
        // Step 4: Format log entry
        logEntry = formatter.format(logEntry);
        
        // Step 5: Write log (sync or async)
        if (asyncMode) {
            writeLogAsync(logEntry);
        } else {
            writeLogSync(logEntry);
        }
    }
    
    private boolean isLoggingEnabled(LogLevel level) {
        return level.isEnabled(threshold);
    }
    
    private LogEntry createLogEntry(LogLevel level, String message, Throwable throwable, Map<String, Object> context) {
        return new LogEntry(
            loggerId,
            level,
            message,
            throwable,
            context,
            LocalDateTime.now(),
            Thread.currentThread().getName(),
            Thread.currentThread().getId()
        );
    }
    
    private boolean shouldLog(LogEntry logEntry) {
        return filter == null || filter.shouldLog(logEntry);
    }
    
    private void writeLogSync(LogEntry logEntry) {
        try {
            writeLog(logEntry);
        } catch (LoggingException e) {
            handleLoggingError(e, logEntry);
        }
    }
    
    private void writeLogAsync(LogEntry logEntry) {
        CompletableFuture.runAsync(() -> {
            try {
                writeLog(logEntry);
            } catch (LoggingException e) {
                handleLoggingError(e, logEntry);
            }
        });
    }
    
    private void handleLoggingError(LoggingException e, LogEntry logEntry) {
        // Fallback error handling - could write to system err or another logger
        System.err.println("Logging failed for logger " + loggerId + ": " + e.getMessage());
        System.err.println("Original log message: " + logEntry.getMessage());
    }
    
    private String formatMessage(String message, Object... args) {
        if (args == null || args.length == 0) {
            return message;
        }
        
        try {
            return String.format(message, args);
        } catch (Exception e) {
            return message + " [FORMATTING_ERROR: " + e.getMessage() + "]";
        }
    }
    
    // Factory methods for default components
    protected LogFormatter createDefaultFormatter() {
        return new DefaultLogFormatter();
    }
    
    protected LogFilter createDefaultFilter() {
        return null; // No filtering by default
    }
    
    // Getters and setters
    public String getLoggerId() { return loggerId; }
    public LogLevel getThreshold() { return threshold; }
    public void setThreshold(LogLevel threshold) { this.threshold = threshold; }
    public void setFormatter(LogFormatter formatter) { this.formatter = formatter; }
    public void setFilter(LogFilter filter) { this.filter = filter; }
    public boolean isAsyncMode() { return asyncMode; }
    public void setAsyncMode(boolean asyncMode) { this.asyncMode = asyncMode; }
}

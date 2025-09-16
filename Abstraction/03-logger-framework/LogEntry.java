package abstraction.loggerframework;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Log entry data structure containing all log information
 */
public class LogEntry {
    private String loggerId;
    private Logger.LogLevel level;
    private String message;
    private Throwable throwable;
    private Map<String, Object> context;
    private LocalDateTime timestamp;
    private String threadName;
    private long threadId;
    private String formattedMessage;
    
    public LogEntry(String loggerId, Logger.LogLevel level, String message, 
                   Throwable throwable, Map<String, Object> context, 
                   LocalDateTime timestamp, String threadName, long threadId) {
        this.loggerId = loggerId;
        this.level = level;
        this.message = message;
        this.throwable = throwable;
        this.context = context;
        this.timestamp = timestamp;
        this.threadName = threadName;
        this.threadId = threadId;
    }
    
    // Getters and setters
    public String getLoggerId() { return loggerId; }
    public Logger.LogLevel getLevel() { return level; }
    public String getMessage() { return message; }
    public Throwable getThrowable() { return throwable; }
    public Map<String, Object> getContext() { return context; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getThreadName() { return threadName; }
    public long getThreadId() { return threadId; }
    public String getFormattedMessage() { return formattedMessage; }
    
    public void setFormattedMessage(String formattedMessage) { 
        this.formattedMessage = formattedMessage; 
    }
}

/**
 * Log formatter interface for customizing log output format
 */
interface LogFormatter {
    LogEntry format(LogEntry logEntry);
}

/**
 * Default log formatter with standard format
 */
class DefaultLogFormatter implements LogFormatter {
    @Override
    public LogEntry format(LogEntry logEntry) {
        StringBuilder sb = new StringBuilder();
        
        // Timestamp
        sb.append("[").append(logEntry.getTimestamp()).append("] ");
        
        // Log level
        sb.append("[").append(logEntry.getLevel()).append("] ");
        
        // Logger ID
        sb.append("[").append(logEntry.getLoggerId()).append("] ");
        
        // Thread info
        sb.append("[").append(logEntry.getThreadName()).append("-").append(logEntry.getThreadId()).append("] ");
        
        // Message
        sb.append(logEntry.getMessage());
        
        // Context if present
        if (logEntry.getContext() != null && !logEntry.getContext().isEmpty()) {
            sb.append(" | Context: ").append(logEntry.getContext());
        }
        
        // Exception if present
        if (logEntry.getThrowable() != null) {
            sb.append("\nException: ").append(logEntry.getThrowable().getClass().getSimpleName());
            sb.append(": ").append(logEntry.getThrowable().getMessage());
        }
        
        logEntry.setFormattedMessage(sb.toString());
        return logEntry;
    }
}

/**
 * JSON log formatter for structured logging
 */
class JsonLogFormatter implements LogFormatter {
    @Override
    public LogEntry format(LogEntry logEntry) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"timestamp\":\"").append(logEntry.getTimestamp()).append("\",");
        json.append("\"level\":\"").append(logEntry.getLevel()).append("\",");
        json.append("\"logger\":\"").append(logEntry.getLoggerId()).append("\",");
        json.append("\"thread\":\"").append(logEntry.getThreadName()).append("\",");
        json.append("\"message\":\"").append(escapeJson(logEntry.getMessage())).append("\"");
        
        if (logEntry.getContext() != null && !logEntry.getContext().isEmpty()) {
            json.append(",\"context\":").append(contextToJson(logEntry.getContext()));
        }
        
        if (logEntry.getThrowable() != null) {
            json.append(",\"exception\":{");
            json.append("\"class\":\"").append(logEntry.getThrowable().getClass().getName()).append("\",");
            json.append("\"message\":\"").append(escapeJson(logEntry.getThrowable().getMessage())).append("\"");
            json.append("}");
        }
        
        json.append("}");
        
        logEntry.setFormattedMessage(json.toString());
        return logEntry;
    }
    
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
    
    private String contextToJson(Map<String, Object> context) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            if (!first) json.append(",");
            json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
            first = false;
        }
        json.append("}");
        return json.toString();
    }
}

/**
 * Log filter interface for conditional logging
 */
interface LogFilter {
    boolean shouldLog(LogEntry logEntry);
}

/**
 * Level-based log filter
 */
class LevelFilter implements LogFilter {
    private Logger.LogLevel minLevel;
    
    public LevelFilter(Logger.LogLevel minLevel) {
        this.minLevel = minLevel;
    }
    
    @Override
    public boolean shouldLog(LogEntry logEntry) {
        return logEntry.getLevel().getPriority() >= minLevel.getPriority();
    }
}

/**
 * Context-based log filter
 */
class ContextFilter implements LogFilter {
    private String contextKey;
    private String contextValue;
    
    public ContextFilter(String contextKey, String contextValue) {
        this.contextKey = contextKey;
        this.contextValue = contextValue;
    }
    
    @Override
    public boolean shouldLog(LogEntry logEntry) {
        if (logEntry.getContext() == null) return false;
        Object value = logEntry.getContext().get(contextKey);
        return contextValue.equals(String.valueOf(value));
    }
}

/**
 * Composite filter that combines multiple filters
 */
class CompositeFilter implements LogFilter {
    private LogFilter[] filters;
    private boolean requireAll; // true for AND, false for OR
    
    public CompositeFilter(boolean requireAll, LogFilter... filters) {
        this.requireAll = requireAll;
        this.filters = filters;
    }
    
    @Override
    public boolean shouldLog(LogEntry logEntry) {
        if (filters == null || filters.length == 0) return true;
        
        for (LogFilter filter : filters) {
            boolean result = filter.shouldLog(logEntry);
            if (requireAll && !result) return false;
            if (!requireAll && result) return true;
        }
        
        return requireAll; // All passed if requireAll, none passed if OR
    }
}

/**
 * Custom logging exceptions
 */
class LoggingException extends Exception {
    public LoggingException(String message) { super(message); }
    public LoggingException(String message, Throwable cause) { super(message, cause); }
}

class LoggerConfigurationException extends LoggingException {
    public LoggerConfigurationException(String message) { super(message); }
}

class LogWriteException extends LoggingException {
    public LogWriteException(String message) { super(message); }
    public LogWriteException(String message, Throwable cause) { super(message, cause); }
}

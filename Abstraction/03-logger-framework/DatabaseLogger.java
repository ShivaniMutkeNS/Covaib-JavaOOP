package abstraction.loggerframework;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Database Logger Implementation with connection pooling, batching, and retry logic
 */
public class DatabaseLogger extends Logger {
    
    private String connectionUrl;
    private String username;
    private String password;
    private String tableName;
    private boolean enableBatching;
    private int batchSize;
    private int maxRetries;
    private Connection connection;
    private PreparedStatement insertStatement;
    private final BlockingQueue<LogEntry> logQueue;
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private Thread batchProcessor;
    
    public DatabaseLogger(String loggerId, LogLevel threshold, Map<String, Object> config) {
        super(loggerId, threshold, config);
        
        this.connectionUrl = (String) config.get("connection_url");
        this.username = (String) config.get("username");
        this.password = (String) config.get("password");
        this.tableName = (String) config.getOrDefault("table_name", "application_logs");
        this.enableBatching = (Boolean) config.getOrDefault("enable_batching", true);
        this.batchSize = ((Number) config.getOrDefault("batch_size", 100)).intValue();
        this.maxRetries = ((Number) config.getOrDefault("max_retries", 3)).intValue();
        
        this.logQueue = new LinkedBlockingQueue<>();
        
        initializeDatabase();
        
        if (enableBatching) {
            startBatchProcessor();
        }
    }
    
    private void initializeDatabase() {
        try {
            // Establish database connection
            connection = DriverManager.getConnection(connectionUrl, username, password);
            connection.setAutoCommit(!enableBatching);
            
            // Create table if it doesn't exist
            createLogTableIfNotExists();
            
            // Prepare insert statement
            String insertSql = "INSERT INTO " + tableName + 
                " (logger_id, log_level, message, throwable, context, timestamp, thread_name, thread_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            insertStatement = connection.prepareStatement(insertSql);
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database logger: " + e.getMessage(), e);
        }
    }
    
    private void createLogTableIfNotExists() throws SQLException {
        String createTableSql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
            "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
            "logger_id VARCHAR(255) NOT NULL, " +
            "log_level VARCHAR(10) NOT NULL, " +
            "message TEXT NOT NULL, " +
            "throwable TEXT, " +
            "context TEXT, " +
            "timestamp TIMESTAMP NOT NULL, " +
            "thread_name VARCHAR(255), " +
            "thread_id BIGINT, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "INDEX idx_logger_level (logger_id, log_level), " +
            "INDEX idx_timestamp (timestamp)" +
            ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSql);
        }
    }
    
    @Override
    protected void writeLog(LogEntry logEntry) throws LoggingException {
        if (enableBatching) {
            // Add to queue for batch processing
            try {
                logQueue.offer(logEntry);
            } catch (Exception e) {
                throw new LogWriteException("Failed to queue log entry for batch processing", e);
            }
        } else {
            // Write immediately
            writeLogToDatabase(logEntry);
        }
    }
    
    private void writeLogToDatabase(LogEntry logEntry) throws LoggingException {
        int retryCount = 0;
        
        while (retryCount <= maxRetries) {
            try {
                synchronized (insertStatement) {
                    insertStatement.setString(1, logEntry.getLoggerId());
                    insertStatement.setString(2, logEntry.getLevel().name());
                    insertStatement.setString(3, logEntry.getMessage());
                    insertStatement.setString(4, formatThrowable(logEntry.getThrowable()));
                    insertStatement.setString(5, formatContext(logEntry.getContext()));
                    insertStatement.setTimestamp(6, Timestamp.valueOf(logEntry.getTimestamp()));
                    insertStatement.setString(7, logEntry.getThreadName());
                    insertStatement.setLong(8, logEntry.getThreadId());
                    
                    insertStatement.executeUpdate();
                    
                    if (!enableBatching) {
                        connection.commit();
                    }
                }
                return; // Success, exit retry loop
                
            } catch (SQLException e) {
                retryCount++;
                if (retryCount > maxRetries) {
                    throw new LogWriteException("Failed to write log to database after " + maxRetries + " retries", e);
                }
                
                // Wait before retry
                try {
                    Thread.sleep(100 * retryCount); // Exponential backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new LogWriteException("Log write interrupted during retry", ie);
                }
                
                // Try to reconnect if connection is broken
                if (!isConnectionValid()) {
                    reconnect();
                }
            }
        }
    }
    
    private void startBatchProcessor() {
        batchProcessor = new Thread(() -> {
            while (isRunning.get() || !logQueue.isEmpty()) {
                try {
                    processBatch();
                } catch (Exception e) {
                    System.err.println("Batch processing error in DatabaseLogger: " + e.getMessage());
                    try {
                        Thread.sleep(1000); // Wait before retrying
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }, "DatabaseLogger-BatchProcessor-" + loggerId);
        
        batchProcessor.setDaemon(true);
        batchProcessor.start();
    }
    
    private void processBatch() throws SQLException, InterruptedException {
        if (logQueue.isEmpty()) {
            Thread.sleep(100); // Wait for entries
            return;
        }
        
        synchronized (insertStatement) {
            int batchCount = 0;
            
            while (batchCount < batchSize && !logQueue.isEmpty()) {
                LogEntry logEntry = logQueue.take();
                
                insertStatement.setString(1, logEntry.getLoggerId());
                insertStatement.setString(2, logEntry.getLevel().name());
                insertStatement.setString(3, logEntry.getMessage());
                insertStatement.setString(4, formatThrowable(logEntry.getThrowable()));
                insertStatement.setString(5, formatContext(logEntry.getContext()));
                insertStatement.setTimestamp(6, Timestamp.valueOf(logEntry.getTimestamp()));
                insertStatement.setString(7, logEntry.getThreadName());
                insertStatement.setLong(8, logEntry.getThreadId());
                
                insertStatement.addBatch();
                batchCount++;
            }
            
            if (batchCount > 0) {
                insertStatement.executeBatch();
                connection.commit();
            }
        }
    }
    
    private boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
    
    private void reconnect() throws LoggingException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            
            connection = DriverManager.getConnection(connectionUrl, username, password);
            connection.setAutoCommit(!enableBatching);
            
            String insertSql = "INSERT INTO " + tableName + 
                " (logger_id, log_level, message, throwable, context, timestamp, thread_name, thread_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            insertStatement = connection.prepareStatement(insertSql);
            
        } catch (SQLException e) {
            throw new LogWriteException("Failed to reconnect to database", e);
        }
    }
    
    private String formatThrowable(Throwable throwable) {
        if (throwable == null) return null;
        
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.getClass().getName()).append(": ").append(throwable.getMessage());
        
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\n\tat ").append(element.toString());
        }
        
        if (throwable.getCause() != null) {
            sb.append("\nCaused by: ").append(formatThrowable(throwable.getCause()));
        }
        
        return sb.toString();
    }
    
    private String formatContext(Map<String, Object> context) {
        if (context == null || context.isEmpty()) return null;
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            if (!first) sb.append(", ");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        return sb.toString();
    }
    
    @Override
    public void flush() throws LoggingException {
        if (enableBatching) {
            // Process remaining entries in queue
            try {
                while (!logQueue.isEmpty()) {
                    processBatch();
                }
            } catch (Exception e) {
                throw new LoggingException("Failed to flush batch queue", e);
            }
        }
        
        try {
            if (connection != null && !connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            throw new LoggingException("Failed to flush database connection", e);
        }
    }
    
    @Override
    public void close() throws LoggingException {
        // Stop batch processor
        isRunning.set(false);
        if (batchProcessor != null) {
            try {
                batchProcessor.join(5000); // Wait up to 5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Flush remaining logs
        flush();
        
        // Close database resources
        try {
            if (insertStatement != null) {
                insertStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new LoggingException("Failed to close database resources", e);
        }
    }
    
    // Database-specific methods
    public int getQueueSize() {
        return logQueue.size();
    }
    
    public boolean isBatchingEnabled() {
        return enableBatching;
    }
    
    public String getTableName() {
        return tableName;
    }
}

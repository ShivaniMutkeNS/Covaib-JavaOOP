package abstraction.loggerframework;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * File Logger Implementation with rotation, buffering, and thread safety
 */
public class FileLogger extends Logger {
    
    private String logFilePath;
    private boolean enableRotation;
    private long maxFileSize;
    private int maxBackupFiles;
    private boolean enableBuffering;
    private int bufferSize;
    private PrintWriter writer;
    private long currentFileSize;
    private final ReentrantLock writeLock = new ReentrantLock();
    
    public FileLogger(String loggerId, LogLevel threshold, Map<String, Object> config) {
        super(loggerId, threshold, config);
        
        this.logFilePath = (String) config.getOrDefault("log_file_path", "logs/application.log");
        this.enableRotation = (Boolean) config.getOrDefault("enable_rotation", true);
        this.maxFileSize = ((Number) config.getOrDefault("max_file_size", 10 * 1024 * 1024)).longValue(); // 10MB
        this.maxBackupFiles = ((Number) config.getOrDefault("max_backup_files", 5)).intValue();
        this.enableBuffering = (Boolean) config.getOrDefault("enable_buffering", true);
        this.bufferSize = ((Number) config.getOrDefault("buffer_size", 8192)).intValue();
        
        initializeLogger();
    }
    
    private void initializeLogger() {
        try {
            // Create log directory if it doesn't exist
            Path logPath = Paths.get(logFilePath);
            Path parentDir = logPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            // Initialize writer
            FileOutputStream fos = new FileOutputStream(logFilePath, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            
            if (enableBuffering) {
                BufferedWriter bw = new BufferedWriter(osw, bufferSize);
                writer = new PrintWriter(bw);
            } else {
                writer = new PrintWriter(osw);
            }
            
            // Get current file size
            if (Files.exists(logPath)) {
                currentFileSize = Files.size(logPath);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize file logger: " + e.getMessage(), e);
        }
    }
    
    @Override
    protected void writeLog(LogEntry logEntry) throws LoggingException {
        writeLock.lock();
        try {
            // Check if rotation is needed
            if (enableRotation && shouldRotate()) {
                rotateLogFile();
            }
            
            String logLine = logEntry.getFormattedMessage();
            writer.println(logLine);
            
            // Update file size estimate
            currentFileSize += logLine.getBytes("UTF-8").length + System.lineSeparator().length();
            
            // Flush if not buffering or if it's an error/fatal level
            if (!enableBuffering || logEntry.getLevel().getPriority() >= LogLevel.ERROR.getPriority()) {
                writer.flush();
            }
            
        } catch (IOException e) {
            throw new LogWriteException("Failed to write log to file", e);
        } finally {
            writeLock.unlock();
        }
    }
    
    private boolean shouldRotate() {
        return currentFileSize >= maxFileSize;
    }
    
    private void rotateLogFile() throws IOException {
        // Close current writer
        if (writer != null) {
            writer.close();
        }
        
        // Rotate existing backup files
        for (int i = maxBackupFiles - 1; i >= 1; i--) {
            Path oldFile = Paths.get(logFilePath + "." + i);
            Path newFile = Paths.get(logFilePath + "." + (i + 1));
            
            if (Files.exists(oldFile)) {
                if (i == maxBackupFiles - 1) {
                    Files.deleteIfExists(newFile); // Delete oldest backup
                }
                Files.move(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        
        // Move current log file to .1
        Path currentFile = Paths.get(logFilePath);
        Path backupFile = Paths.get(logFilePath + ".1");
        if (Files.exists(currentFile)) {
            Files.move(currentFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
        }
        
        // Reinitialize logger with new file
        currentFileSize = 0;
        initializeLogger();
        
        // Log rotation event
        LogEntry rotationEntry = new LogEntry(
            loggerId, LogLevel.INFO, 
            "Log file rotated. New file started.", 
            null, null, LocalDateTime.now(), 
            Thread.currentThread().getName(), 
            Thread.currentThread().getId()
        );
        formatter.format(rotationEntry);
        writer.println(rotationEntry.getFormattedMessage());
        writer.flush();
    }
    
    @Override
    public void flush() throws LoggingException {
        writeLock.lock();
        try {
            if (writer != null) {
                writer.flush();
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public void close() throws LoggingException {
        writeLock.lock();
        try {
            if (writer != null) {
                writer.flush();
                writer.close();
                writer = null;
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    // File-specific methods
    public long getCurrentFileSize() {
        return currentFileSize;
    }
    
    public String getLogFilePath() {
        return logFilePath;
    }
    
    public boolean isRotationEnabled() {
        return enableRotation;
    }
}

package abstraction.filestorage;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract File Storage Service with enterprise-level features
 * Handles different backend storage systems with unified API
 */
public abstract class StorageService {
    
    protected String serviceId;
    protected Map<String, Object> configuration;
    protected boolean isConnected;
    protected StorageMetrics metrics;
    
    public StorageService(String serviceId, Map<String, Object> config) {
        this.serviceId = serviceId;
        this.configuration = config;
        this.isConnected = false;
        this.metrics = new StorageMetrics();
    }
    
    /**
     * Initialize connection to storage backend
     * @throws StorageException if connection fails
     */
    public abstract void connect() throws StorageException;
    
    /**
     * Upload file with metadata and progress tracking
     * @param uploadRequest Upload details including file, path, metadata
     * @return CompletableFuture for async upload with progress callbacks
     * @throws StorageException if upload fails
     */
    public abstract CompletableFuture<UploadResult> upload(UploadRequest uploadRequest) 
            throws StorageException;
    
    /**
     * Download file with streaming support
     * @param downloadRequest Download details including path, range
     * @return DownloadResult with stream and metadata
     * @throws StorageException if download fails
     */
    public abstract DownloadResult download(DownloadRequest downloadRequest) 
            throws StorageException;
    
    /**
     * Delete file or directory with soft delete support
     * @param deleteRequest Delete details including path, permanent flag
     * @return DeleteResult with operation status
     * @throws StorageException if deletion fails
     */
    public abstract DeleteResult delete(DeleteRequest deleteRequest) 
            throws StorageException;
    
    /**
     * List files and directories with pagination
     * @param listRequest List parameters including path, filters, pagination
     * @return FileListResult with files and metadata
     * @throws StorageException if listing fails
     */
    public abstract FileListResult listFiles(ListRequest listRequest) 
            throws StorageException;
    
    /**
     * Get file metadata without downloading content
     * @param path File path
     * @return FileMetadata with size, timestamps, etc.
     * @throws StorageException if metadata retrieval fails
     */
    public abstract FileMetadata getMetadata(String path) throws StorageException;
    
    /**
     * Template method for complete file operation workflow
     */
    public final OperationResult performFileOperation(FileOperation operation) {
        try {
            // Step 1: Validate connection
            if (!isConnected) {
                connect();
            }
            
            // Step 2: Pre-operation hooks
            preOperationHook(operation);
            
            // Step 3: Execute operation based on type
            OperationResult result = executeOperation(operation);
            
            // Step 4: Update metrics
            updateMetrics(operation, result);
            
            // Step 5: Post-operation hooks
            postOperationHook(operation, result);
            
            return result;
            
        } catch (Exception e) {
            return handleOperationError(e, operation);
        }
    }
    
    // Template method implementation
    private OperationResult executeOperation(FileOperation operation) throws StorageException {
        switch (operation.getType()) {
            case UPLOAD:
                CompletableFuture<UploadResult> uploadFuture = upload(operation.getUploadRequest());
                try {
                    UploadResult uploadResult = uploadFuture.get();
                    return OperationResult.success(operation.getOperationId(), uploadResult);
                } catch (Exception e) {
                    throw new StorageException("Upload operation failed", e);
                }
                
            case DOWNLOAD:
                DownloadResult downloadResult = download(operation.getDownloadRequest());
                return OperationResult.success(operation.getOperationId(), downloadResult);
                
            case DELETE:
                DeleteResult deleteResult = delete(operation.getDeleteRequest());
                return OperationResult.success(operation.getOperationId(), deleteResult);
                
            case LIST:
                FileListResult listResult = listFiles(operation.getListRequest());
                return OperationResult.success(operation.getOperationId(), listResult);
                
            default:
                throw new StorageException("Unsupported operation type: " + operation.getType());
        }
    }
    
    // Hook methods for customization
    protected void preOperationHook(FileOperation operation) {
        System.out.println("Pre-operation: " + operation.getType() + " on " + serviceId);
    }
    
    protected void postOperationHook(FileOperation operation, OperationResult result) {
        System.out.println("Post-operation: " + operation.getType() + " completed with status: " + 
                         result.isSuccess());
    }
    
    private void updateMetrics(FileOperation operation, OperationResult result) {
        metrics.incrementOperation(operation.getType());
        if (result.isSuccess()) {
            metrics.incrementSuccessCount();
        } else {
            metrics.incrementErrorCount();
        }
    }
    
    private OperationResult handleOperationError(Exception e, FileOperation operation) {
        System.err.println("Operation failed for " + serviceId + ": " + e.getMessage());
        metrics.incrementErrorCount();
        return OperationResult.failure(operation.getOperationId(), e.getMessage());
    }
    
    // Utility methods
    public String getServiceId() { return serviceId; }
    public boolean isConnected() { return isConnected; }
    public StorageMetrics getMetrics() { return metrics; }
    
    public void disconnect() {
        isConnected = false;
        System.out.println("Disconnected from storage service: " + serviceId);
    }
}

package abstraction.filestorage;

/**
 * File operation wrapper for template method pattern
 */
public class FileOperation {
    
    public enum OperationType {
        UPLOAD, DOWNLOAD, DELETE, LIST
    }
    
    private String operationId;
    private OperationType type;
    private UploadRequest uploadRequest;
    private DownloadRequest downloadRequest;
    private DeleteRequest deleteRequest;
    private ListRequest listRequest;
    
    // Constructor for upload operation
    public FileOperation(String operationId, UploadRequest uploadRequest) {
        this.operationId = operationId;
        this.type = OperationType.UPLOAD;
        this.uploadRequest = uploadRequest;
    }
    
    // Constructor for download operation
    public FileOperation(String operationId, DownloadRequest downloadRequest) {
        this.operationId = operationId;
        this.type = OperationType.DOWNLOAD;
        this.downloadRequest = downloadRequest;
    }
    
    // Constructor for delete operation
    public FileOperation(String operationId, DeleteRequest deleteRequest) {
        this.operationId = operationId;
        this.type = OperationType.DELETE;
        this.deleteRequest = deleteRequest;
    }
    
    // Constructor for list operation
    public FileOperation(String operationId, ListRequest listRequest) {
        this.operationId = operationId;
        this.type = OperationType.LIST;
        this.listRequest = listRequest;
    }
    
    // Getters
    public String getOperationId() { return operationId; }
    public OperationType getType() { return type; }
    public UploadRequest getUploadRequest() { return uploadRequest; }
    public DownloadRequest getDownloadRequest() { return downloadRequest; }
    public DeleteRequest getDeleteRequest() { return deleteRequest; }
    public ListRequest getListRequest() { return listRequest; }
}

/**
 * Generic operation result
 */
class OperationResult {
    private String operationId;
    private boolean success;
    private String errorMessage;
    private Object result;
    
    private OperationResult(String operationId, boolean success, String errorMessage, Object result) {
        this.operationId = operationId;
        this.success = success;
        this.errorMessage = errorMessage;
        this.result = result;
    }
    
    public static OperationResult success(String operationId, Object result) {
        return new OperationResult(operationId, true, null, result);
    }
    
    public static OperationResult failure(String operationId, String errorMessage) {
        return new OperationResult(operationId, false, errorMessage, null);
    }
    
    // Getters
    public String getOperationId() { return operationId; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
    public Object getResult() { return result; }
}

/**
 * Storage exception hierarchy
 */
class StorageException extends Exception {
    private String errorCode;
    private boolean retryable;
    
    public StorageException(String message) {
        super(message);
        this.retryable = false;
    }
    
    public StorageException(String message, String errorCode, boolean retryable) {
        super(message);
        this.errorCode = errorCode;
        this.retryable = retryable;
    }
    
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getErrorCode() { return errorCode; }
    public boolean isRetryable() { return retryable; }
}

class ConnectionException extends StorageException {
    public ConnectionException(String message) { super(message); }
    public ConnectionException(String message, Throwable cause) { super(message, cause); }
}

class FileNotFoundException extends StorageException {
    public FileNotFoundException(String message) { super(message); }
}

class InsufficientPermissionsException extends StorageException {
    public InsufficientPermissionsException(String message) { super(message); }
}

class QuotaExceededException extends StorageException {
    public QuotaExceededException(String message) { super(message); }
}

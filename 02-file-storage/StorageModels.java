package abstraction.filestorage;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Data models for file storage operations
 */

// Upload Request
class UploadRequest {
    private String filePath;
    private InputStream fileStream;
    private long fileSize;
    private String contentType;
    private Map<String, String> metadata;
    private boolean overwrite;
    private ProgressCallback progressCallback;
    
    public UploadRequest(String filePath, InputStream fileStream, long fileSize, String contentType) {
        this.filePath = filePath;
        this.fileStream = fileStream;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.overwrite = false;
    }
    
    // Getters and setters
    public String getFilePath() { return filePath; }
    public InputStream getFileStream() { return fileStream; }
    public long getFileSize() { return fileSize; }
    public String getContentType() { return contentType; }
    public Map<String, String> getMetadata() { return metadata; }
    public boolean isOverwrite() { return overwrite; }
    public ProgressCallback getProgressCallback() { return progressCallback; }
    
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }
    public void setOverwrite(boolean overwrite) { this.overwrite = overwrite; }
    public void setProgressCallback(ProgressCallback callback) { this.progressCallback = callback; }
}

// Upload Result
class UploadResult {
    private String fileId;
    private String filePath;
    private long uploadedSize;
    private String etag;
    private String versionId;
    private LocalDateTime uploadTime;
    private boolean success;
    private String errorMessage;
    
    private UploadResult(String fileId, String filePath, long uploadedSize, String etag, 
                        String versionId, boolean success, String errorMessage) {
        this.fileId = fileId;
        this.filePath = filePath;
        this.uploadedSize = uploadedSize;
        this.etag = etag;
        this.versionId = versionId;
        this.success = success;
        this.errorMessage = errorMessage;
        this.uploadTime = LocalDateTime.now();
    }
    
    public static UploadResult success(String fileId, String filePath, long size, String etag, String versionId) {
        return new UploadResult(fileId, filePath, size, etag, versionId, true, null);
    }
    
    public static UploadResult failure(String filePath, String errorMessage) {
        return new UploadResult(null, filePath, 0, null, null, false, errorMessage);
    }
    
    // Getters
    public String getFileId() { return fileId; }
    public String getFilePath() { return filePath; }
    public long getUploadedSize() { return uploadedSize; }
    public String getEtag() { return etag; }
    public String getVersionId() { return versionId; }
    public LocalDateTime getUploadTime() { return uploadTime; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
}

// Download Request
class DownloadRequest {
    private String filePath;
    private long rangeStart;
    private long rangeEnd;
    private String versionId;
    private boolean streamingMode;
    
    public DownloadRequest(String filePath) {
        this.filePath = filePath;
        this.rangeStart = -1;
        this.rangeEnd = -1;
        this.streamingMode = true;
    }
    
    public DownloadRequest(String filePath, long rangeStart, long rangeEnd) {
        this.filePath = filePath;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.streamingMode = true;
    }
    
    // Getters and setters
    public String getFilePath() { return filePath; }
    public long getRangeStart() { return rangeStart; }
    public long getRangeEnd() { return rangeEnd; }
    public String getVersionId() { return versionId; }
    public boolean isStreamingMode() { return streamingMode; }
    
    public void setVersionId(String versionId) { this.versionId = versionId; }
    public void setStreamingMode(boolean streamingMode) { this.streamingMode = streamingMode; }
}

// Download Result
class DownloadResult {
    private InputStream contentStream;
    private FileMetadata metadata;
    private long contentLength;
    private boolean success;
    private String errorMessage;
    
    private DownloadResult(InputStream contentStream, FileMetadata metadata, long contentLength, 
                          boolean success, String errorMessage) {
        this.contentStream = contentStream;
        this.metadata = metadata;
        this.contentLength = contentLength;
        this.success = success;
        this.errorMessage = errorMessage;
    }
    
    public static DownloadResult success(InputStream stream, FileMetadata metadata, long contentLength) {
        return new DownloadResult(stream, metadata, contentLength, true, null);
    }
    
    public static DownloadResult failure(String errorMessage) {
        return new DownloadResult(null, null, 0, false, errorMessage);
    }
    
    // Getters
    public InputStream getContentStream() { return contentStream; }
    public FileMetadata getMetadata() { return metadata; }
    public long getContentLength() { return contentLength; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
}

// Delete Request
class DeleteRequest {
    private String filePath;
    private boolean permanent;
    private boolean recursive;
    private String versionId;
    
    public DeleteRequest(String filePath, boolean permanent) {
        this.filePath = filePath;
        this.permanent = permanent;
        this.recursive = false;
    }
    
    // Getters and setters
    public String getFilePath() { return filePath; }
    public boolean isPermanent() { return permanent; }
    public boolean isRecursive() { return recursive; }
    public String getVersionId() { return versionId; }
    
    public void setRecursive(boolean recursive) { this.recursive = recursive; }
    public void setVersionId(String versionId) { this.versionId = versionId; }
}

// Delete Result
class DeleteResult {
    private String filePath;
    private boolean success;
    private String errorMessage;
    private int deletedCount;
    private LocalDateTime deletionTime;
    
    private DeleteResult(String filePath, boolean success, String errorMessage, int deletedCount) {
        this.filePath = filePath;
        this.success = success;
        this.errorMessage = errorMessage;
        this.deletedCount = deletedCount;
        this.deletionTime = LocalDateTime.now();
    }
    
    public static DeleteResult success(String filePath, int deletedCount) {
        return new DeleteResult(filePath, true, null, deletedCount);
    }
    
    public static DeleteResult failure(String filePath, String errorMessage) {
        return new DeleteResult(filePath, false, errorMessage, 0);
    }
    
    // Getters
    public String getFilePath() { return filePath; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
    public int getDeletedCount() { return deletedCount; }
    public LocalDateTime getDeletionTime() { return deletionTime; }
}

// List Request
class ListRequest {
    private String directoryPath;
    private String prefix;
    private int maxResults;
    private String continuationToken;
    private boolean recursive;
    private Map<String, String> filters;
    
    public ListRequest(String directoryPath) {
        this.directoryPath = directoryPath;
        this.maxResults = 1000;
        this.recursive = false;
    }
    
    // Getters and setters
    public String getDirectoryPath() { return directoryPath; }
    public String getPrefix() { return prefix; }
    public int getMaxResults() { return maxResults; }
    public String getContinuationToken() { return continuationToken; }
    public boolean isRecursive() { return recursive; }
    public Map<String, String> getFilters() { return filters; }
    
    public void setPrefix(String prefix) { this.prefix = prefix; }
    public void setMaxResults(int maxResults) { this.maxResults = maxResults; }
    public void setContinuationToken(String token) { this.continuationToken = token; }
    public void setRecursive(boolean recursive) { this.recursive = recursive; }
    public void setFilters(Map<String, String> filters) { this.filters = filters; }
}

// File List Result
class FileListResult {
    private List<FileInfo> files;
    private String nextContinuationToken;
    private boolean hasMore;
    private int totalCount;
    private boolean success;
    private String errorMessage;
    
    private FileListResult(List<FileInfo> files, String nextToken, boolean hasMore, 
                          int totalCount, boolean success, String errorMessage) {
        this.files = files;
        this.nextContinuationToken = nextToken;
        this.hasMore = hasMore;
        this.totalCount = totalCount;
        this.success = success;
        this.errorMessage = errorMessage;
    }
    
    public static FileListResult success(List<FileInfo> files, String nextToken, boolean hasMore, int totalCount) {
        return new FileListResult(files, nextToken, hasMore, totalCount, true, null);
    }
    
    public static FileListResult failure(String errorMessage) {
        return new FileListResult(null, null, false, 0, false, errorMessage);
    }
    
    // Getters
    public List<FileInfo> getFiles() { return files; }
    public String getNextContinuationToken() { return nextContinuationToken; }
    public boolean hasMore() { return hasMore; }
    public int getTotalCount() { return totalCount; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
}

// File Metadata
class FileMetadata {
    private String fileName;
    private String filePath;
    private long size;
    private String contentType;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private String etag;
    private String versionId;
    private Map<String, String> customMetadata;
    
    public FileMetadata(String fileName, String filePath, long size, String contentType) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.size = size;
        this.contentType = contentType;
        this.createdTime = LocalDateTime.now();
        this.modifiedTime = LocalDateTime.now();
    }
    
    // Getters and setters
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public long getSize() { return size; }
    public String getContentType() { return contentType; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getModifiedTime() { return modifiedTime; }
    public String getEtag() { return etag; }
    public String getVersionId() { return versionId; }
    public Map<String, String> getCustomMetadata() { return customMetadata; }
    
    public void setEtag(String etag) { this.etag = etag; }
    public void setVersionId(String versionId) { this.versionId = versionId; }
    public void setCustomMetadata(Map<String, String> metadata) { this.customMetadata = metadata; }
    public void setModifiedTime(LocalDateTime modifiedTime) { this.modifiedTime = modifiedTime; }
}

// File Info (for listing)
class FileInfo {
    private String name;
    private String path;
    private long size;
    private boolean isDirectory;
    private LocalDateTime lastModified;
    private String etag;
    
    public FileInfo(String name, String path, long size, boolean isDirectory, LocalDateTime lastModified) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.isDirectory = isDirectory;
        this.lastModified = lastModified;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public String getPath() { return path; }
    public long getSize() { return size; }
    public boolean isDirectory() { return isDirectory; }
    public LocalDateTime getLastModified() { return lastModified; }
    public String getEtag() { return etag; }
    
    public void setEtag(String etag) { this.etag = etag; }
}

// Progress Callback Interface
interface ProgressCallback {
    void onProgress(long bytesTransferred, long totalBytes);
    void onComplete();
    void onError(String errorMessage);
}

// Storage Metrics
class StorageMetrics {
    private long uploadCount;
    private long downloadCount;
    private long deleteCount;
    private long listCount;
    private long successCount;
    private long errorCount;
    private long totalBytesTransferred;
    
    public void incrementOperation(FileOperation.OperationType type) {
        switch (type) {
            case UPLOAD: uploadCount++; break;
            case DOWNLOAD: downloadCount++; break;
            case DELETE: deleteCount++; break;
            case LIST: listCount++; break;
        }
    }
    
    public void incrementSuccessCount() { successCount++; }
    public void incrementErrorCount() { errorCount++; }
    public void addBytesTransferred(long bytes) { totalBytesTransferred += bytes; }
    
    // Getters
    public long getUploadCount() { return uploadCount; }
    public long getDownloadCount() { return downloadCount; }
    public long getDeleteCount() { return deleteCount; }
    public long getListCount() { return listCount; }
    public long getSuccessCount() { return successCount; }
    public long getErrorCount() { return errorCount; }
    public long getTotalBytesTransferred() { return totalBytesTransferred; }
    
    public double getSuccessRate() {
        long total = successCount + errorCount;
        return total > 0 ? (double) successCount / total * 100 : 0;
    }
}

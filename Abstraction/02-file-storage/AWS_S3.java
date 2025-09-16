package abstraction.filestorage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * AWS S3 Storage Service Implementation
 * Features: Multi-part uploads, versioning, lifecycle policies, encryption
 */
public class AWS_S3 extends StorageService {
    
    private String bucketName;
    private String region;
    private String accessKey;
    private String secretKey;
    private boolean versioningEnabled;
    private String encryptionType;
    
    public AWS_S3(String serviceId, Map<String, Object> config) {
        super(serviceId, config);
        this.bucketName = (String) config.get("bucket_name");
        this.region = (String) config.getOrDefault("region", "us-east-1");
        this.accessKey = (String) config.get("access_key");
        this.secretKey = (String) config.get("secret_key");
        this.versioningEnabled = (Boolean) config.getOrDefault("versioning", true);
        this.encryptionType = (String) config.getOrDefault("encryption", "AES256");
    }
    
    @Override
    public void connect() throws StorageException {
        try {
            // Simulate AWS S3 connection
            Thread.sleep(100);
            
            if (accessKey == null || secretKey == null) {
                throw new ConnectionException("AWS credentials not provided");
            }
            
            if (bucketName == null) {
                throw new ConnectionException("S3 bucket name not specified");
            }
            
            // Simulate credential validation
            if (accessKey.length() < 16 || secretKey.length() < 32) {
                throw new ConnectionException("Invalid AWS credentials format");
            }
            
            // Simulate bucket access test
            if (bucketName.contains("invalid")) {
                throw new InsufficientPermissionsException("Access denied to bucket: " + bucketName);
            }
            
            isConnected = true;
            System.out.println("Connected to AWS S3 bucket: " + bucketName + " in region: " + region);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConnectionException("Connection interrupted", e);
        }
    }
    
    @Override
    public CompletableFuture<UploadResult> upload(UploadRequest uploadRequest) throws StorageException {
        if (!isConnected) {
            throw new StorageException("Not connected to S3");
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Handle large file uploads with multipart
                if (uploadRequest.getFileSize() > 100 * 1024 * 1024) { // 100MB
                    return performMultipartUpload(uploadRequest);
                } else {
                    return performSinglePartUpload(uploadRequest);
                }
                
            } catch (Exception e) {
                return UploadResult.failure(uploadRequest.getFilePath(), e.getMessage());
            }
        });
    }
    
    private UploadResult performSinglePartUpload(UploadRequest request) throws StorageException {
        try {
            // Simulate S3 upload
            Thread.sleep(200);
            
            // Check for file existence if overwrite is false
            if (!request.isOverwrite() && fileExists(request.getFilePath())) {
                throw new StorageException("File already exists: " + request.getFilePath(), 
                                         "FILE_EXISTS", false);
            }
            
            // Simulate progress reporting
            ProgressCallback callback = request.getProgressCallback();
            if (callback != null) {
                for (int i = 0; i <= 100; i += 20) {
                    callback.onProgress(request.getFileSize() * i / 100, request.getFileSize());
                    Thread.sleep(50);
                }
                callback.onComplete();
            }
            
            // Generate S3-specific identifiers
            String fileId = "s3_" + UUID.randomUUID().toString();
            String etag = "\"" + UUID.randomUUID().toString().replace("-", "") + "\"";
            String versionId = versioningEnabled ? "v_" + System.currentTimeMillis() : null;
            
            metrics.addBytesTransferred(request.getFileSize());
            
            return UploadResult.success(fileId, request.getFilePath(), request.getFileSize(), etag, versionId);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StorageException("Upload interrupted", "INTERRUPTED", true);
        }
    }
    
    private UploadResult performMultipartUpload(UploadRequest request) throws StorageException {
        try {
            System.out.println("S3: Initiating multipart upload for large file: " + request.getFilePath());
            
            // Simulate multipart upload initialization
            String uploadId = "multipart_" + UUID.randomUUID().toString();
            
            // Simulate uploading parts (5MB chunks)
            long partSize = 5 * 1024 * 1024; // 5MB
            long totalParts = (request.getFileSize() + partSize - 1) / partSize;
            
            ProgressCallback callback = request.getProgressCallback();
            
            for (int partNumber = 1; partNumber <= totalParts; partNumber++) {
                Thread.sleep(100); // Simulate part upload time
                
                if (callback != null) {
                    long uploadedBytes = Math.min(partNumber * partSize, request.getFileSize());
                    callback.onProgress(uploadedBytes, request.getFileSize());
                }
            }
            
            // Simulate completing multipart upload
            Thread.sleep(50);
            
            if (callback != null) {
                callback.onComplete();
            }
            
            String fileId = "s3_multipart_" + UUID.randomUUID().toString();
            String etag = "\"" + UUID.randomUUID().toString().replace("-", "") + "-" + totalParts + "\"";
            String versionId = versioningEnabled ? "v_" + System.currentTimeMillis() : null;
            
            metrics.addBytesTransferred(request.getFileSize());
            
            return UploadResult.success(fileId, request.getFilePath(), request.getFileSize(), etag, versionId);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StorageException("Multipart upload interrupted", "INTERRUPTED", true);
        }
    }
    
    @Override
    public DownloadResult download(DownloadRequest downloadRequest) throws StorageException {
        if (!isConnected) {
            throw new StorageException("Not connected to S3");
        }
        
        try {
            Thread.sleep(150);
            
            // Check if file exists
            if (!fileExists(downloadRequest.getFilePath())) {
                throw new FileNotFoundException("File not found: " + downloadRequest.getFilePath());
            }
            
            // Simulate getting file metadata
            FileMetadata metadata = getMetadata(downloadRequest.getFilePath());
            
            // Handle range requests
            long contentLength = metadata.getSize();
            if (downloadRequest.getRangeStart() >= 0 && downloadRequest.getRangeEnd() >= 0) {
                contentLength = downloadRequest.getRangeEnd() - downloadRequest.getRangeStart() + 1;
            }
            
            // Create simulated content stream
            byte[] content = generateSampleContent((int) contentLength);
            InputStream contentStream = new ByteArrayInputStream(content);
            
            metrics.addBytesTransferred(contentLength);
            
            return DownloadResult.success(contentStream, metadata, contentLength);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StorageException("Download interrupted", "INTERRUPTED", true);
        }
    }
    
    @Override
    public DeleteResult delete(DeleteRequest deleteRequest) throws StorageException {
        if (!isConnected) {
            throw new StorageException("Not connected to S3");
        }
        
        try {
            Thread.sleep(100);
            
            String filePath = deleteRequest.getFilePath();
            
            // Check if file exists
            if (!fileExists(filePath)) {
                throw new FileNotFoundException("File not found: " + filePath);
            }
            
            int deletedCount = 1;
            
            // Handle directory deletion
            if (deleteRequest.isRecursive() && filePath.endsWith("/")) {
                // Simulate deleting multiple objects in directory
                deletedCount = (int) (Math.random() * 10) + 1;
                Thread.sleep(deletedCount * 50); // More time for multiple deletions
            }
            
            // Handle versioned deletion
            if (versioningEnabled && !deleteRequest.isPermanent()) {
                System.out.println("S3: Adding delete marker for versioned object: " + filePath);
            } else {
                System.out.println("S3: Permanently deleting object: " + filePath);
            }
            
            return DeleteResult.success(filePath, deletedCount);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StorageException("Delete interrupted", "INTERRUPTED", true);
        }
    }
    
    @Override
    public FileListResult listFiles(ListRequest listRequest) throws StorageException {
        if (!isConnected) {
            throw new StorageException("Not connected to S3");
        }
        
        try {
            Thread.sleep(100);
            
            List<FileInfo> files = new ArrayList<>();
            
            // Simulate listing S3 objects
            int maxResults = Math.min(listRequest.getMaxResults(), 1000);
            
            for (int i = 0; i < maxResults; i++) {
                String fileName = "file_" + i + ".txt";
                String fullPath = listRequest.getDirectoryPath() + "/" + fileName;
                long size = (long) (Math.random() * 1024 * 1024); // Random size up to 1MB
                LocalDateTime lastModified = LocalDateTime.now().minusDays((long) (Math.random() * 30));
                
                FileInfo fileInfo = new FileInfo(fileName, fullPath, size, false, lastModified);
                fileInfo.setEtag("\"" + UUID.randomUUID().toString().replace("-", "") + "\"");
                files.add(fileInfo);
            }
            
            // Simulate pagination
            boolean hasMore = files.size() == maxResults;
            String nextToken = hasMore ? "token_" + UUID.randomUUID().toString() : null;
            
            return FileListResult.success(files, nextToken, hasMore, files.size());
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StorageException("List operation interrupted", "INTERRUPTED", true);
        }
    }
    
    @Override
    public FileMetadata getMetadata(String path) throws StorageException {
        if (!isConnected) {
            throw new StorageException("Not connected to S3");
        }
        
        try {
            Thread.sleep(50);
            
            if (!fileExists(path)) {
                throw new FileNotFoundException("File not found: " + path);
            }
            
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            long size = (long) (Math.random() * 10 * 1024 * 1024); // Random size up to 10MB
            String contentType = determineContentType(fileName);
            
            FileMetadata metadata = new FileMetadata(fileName, path, size, contentType);
            metadata.setEtag("\"" + UUID.randomUUID().toString().replace("-", "") + "\"");
            
            if (versioningEnabled) {
                metadata.setVersionId("v_" + System.currentTimeMillis());
            }
            
            // Add S3-specific metadata
            Map<String, String> customMetadata = new HashMap<>();
            customMetadata.put("storage-class", "STANDARD");
            customMetadata.put("server-side-encryption", encryptionType);
            metadata.setCustomMetadata(customMetadata);
            
            return metadata;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StorageException("Metadata retrieval interrupted", "INTERRUPTED", true);
        }
    }
    
    @Override
    protected void preOperationHook(FileOperation operation) {
        super.preOperationHook(operation);
        System.out.println("S3: Validating IAM permissions and bucket policies");
    }
    
    @Override
    protected void postOperationHook(FileOperation operation, OperationResult result) {
        super.postOperationHook(operation, result);
        if (result.isSuccess()) {
            System.out.println("S3: Updating CloudWatch metrics and access logs");
        }
    }
    
    // Utility methods
    private boolean fileExists(String path) {
        // Simulate file existence check
        return !path.contains("nonexistent");
    }
    
    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "txt": return "text/plain";
            case "json": return "application/json";
            case "jpg": case "jpeg": return "image/jpeg";
            case "png": return "image/png";
            case "pdf": return "application/pdf";
            default: return "application/octet-stream";
        }
    }
    
    private byte[] generateSampleContent(int size) {
        byte[] content = new byte[size];
        Arrays.fill(content, (byte) 'A');
        return content;
    }
}

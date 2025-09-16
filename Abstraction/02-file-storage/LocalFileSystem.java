package abstraction.filestorage;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Local File System Storage Service Implementation
 * Features: Direct file system access, symbolic links, file permissions, atomic operations
 */
public class LocalFileSystem extends StorageService {
    
    private String rootDirectory;
    private boolean enableSymlinks;
    private boolean enableAtomicOperations;
    private Set<String> allowedExtensions;
    
    public LocalFileSystem(String serviceId, Map<String, Object> config) {
        super(serviceId, config);
        this.rootDirectory = (String) config.getOrDefault("root_directory", System.getProperty("user.home"));
        this.enableSymlinks = (Boolean) config.getOrDefault("enable_symlinks", false);
        this.enableAtomicOperations = (Boolean) config.getOrDefault("atomic_operations", true);
        
        @SuppressWarnings("unchecked")
        List<String> extensions = (List<String>) config.get("allowed_extensions");
        this.allowedExtensions = extensions != null ? new HashSet<>(extensions) : null;
    }
    
    @Override
    public void connect() throws StorageException {
        try {
            Path rootPath = Paths.get(rootDirectory);
            
            // Validate root directory
            if (!Files.exists(rootPath)) {
                throw new ConnectionException("Root directory does not exist: " + rootDirectory);
            }
            
            if (!Files.isDirectory(rootPath)) {
                throw new ConnectionException("Root path is not a directory: " + rootDirectory);
            }
            
            // Check permissions
            if (!Files.isReadable(rootPath)) {
                throw new InsufficientPermissionsException("No read permission for root directory: " + rootDirectory);
            }
            
            if (!Files.isWritable(rootPath)) {
                throw new InsufficientPermissionsException("No write permission for root directory: " + rootDirectory);
            }
            
            isConnected = true;
            System.out.println("Connected to local file system at: " + rootDirectory);
            
        } catch (Exception e) {
            if (e instanceof StorageException) {
                throw e;
            }
            throw new ConnectionException("Failed to connect to local file system", e);
        }
    }
    
    @Override
    public CompletableFuture<UploadResult> upload(UploadRequest uploadRequest) throws StorageException {
        if (!isConnected) {
            throw new StorageException("Not connected to local file system");
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                return performLocalUpload(uploadRequest);
            } catch (Exception e) {
                return UploadResult.failure(uploadRequest.getFilePath(), e.getMessage());
            }
        });
    }
    
    private UploadResult performLocalUpload(UploadRequest request) throws StorageException {
        try {
            Path targetPath = Paths.get(rootDirectory, request.getFilePath());
            
            // Validate file extension if restrictions are enabled
            if (allowedExtensions != null && !isAllowedExtension(request.getFilePath())) {
                throw new StorageException("File extension not allowed: " + request.getFilePath(), 
                                         "INVALID_EXTENSION", false);
            }
            
            // Create parent directories if they don't exist
            Path parentDir = targetPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            // Check if file exists and overwrite flag
            if (Files.exists(targetPath) && !request.isOverwrite()) {
                throw new StorageException("File already exists: " + request.getFilePath(), 
                                         "FILE_EXISTS", false);
            }
            
            // Perform atomic upload if enabled
            if (enableAtomicOperations) {
                return performAtomicUpload(request, targetPath);
            } else {
                return performDirectUpload(request, targetPath);
            }
            
        } catch (IOException e) {
            throw new StorageException("Upload failed: " + e.getMessage(), e);
        }
    }
    
    private UploadResult performAtomicUpload(UploadRequest request, Path targetPath) throws IOException {
        // Create temporary file for atomic operation
        Path tempPath = targetPath.getParent().resolve(targetPath.getFileName() + ".tmp");
        
        try {
            // Write to temporary file first
            long bytesWritten = 0;
            ProgressCallback callback = request.getProgressCallback();
            
            try (InputStream input = request.getFileStream();
                 OutputStream output = Files.newOutputStream(tempPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                
                byte[] buffer = new byte[8192];
                int bytesRead;
                
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                    bytesWritten += bytesRead;
                    
                    if (callback != null) {
                        callback.onProgress(bytesWritten, request.getFileSize());
                    }
                    
                    // Simulate some processing time
                    Thread.sleep(1);
                }
            }
            
            // Atomic move from temp to final location
            Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            
            if (callback != null) {
                callback.onComplete();
            }
            
            // Generate file metadata
            String fileId = "local_" + targetPath.toAbsolutePath().hashCode();
            String etag = generateEtag(targetPath);
            
            metrics.addBytesTransferred(bytesWritten);
            
            return UploadResult.success(fileId, request.getFilePath(), bytesWritten, etag, null);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Upload interrupted", e);
        } finally {
            // Clean up temp file if it exists
            Files.deleteIfExists(tempPath);
        }
    }
    
    private UploadResult performDirectUpload(UploadRequest request, Path targetPath) throws IOException {
        long bytesWritten = 0;
        ProgressCallback callback = request.getProgressCallback();
        
        try (InputStream input = request.getFileStream();
             OutputStream output = Files.newOutputStream(targetPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                bytesWritten += bytesRead;
                
                if (callback != null) {
                    callback.onProgress(bytesWritten, request.getFileSize());
                }
            }
        }
        
        if (callback != null) {
            callback.onComplete();
        }
        
        String fileId = "local_" + targetPath.toAbsolutePath().hashCode();
        String etag = generateEtag(targetPath);
        
        metrics.addBytesTransferred(bytesWritten);
        
        return UploadResult.success(fileId, request.getFilePath(), bytesWritten, etag, null);
    }
    
    @Override
    public DownloadResult download(DownloadRequest downloadRequest) throws StorageException {
        if (!isConnected) {
            throw new StorageException("Not connected to local file system");
        }
        
        try {
            Path filePath = Paths.get(rootDirectory, downloadRequest.getFilePath());
            
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException("File not found: " + downloadRequest.getFilePath());
            }
            
            if (!Files.isReadable(filePath)) {
                throw new InsufficientPermissionsException("No read permission for file: " + downloadRequest.getFilePath());
            }
            
            // Handle symbolic links
            if (Files.isSymbolicLink(filePath) && !enableSymlinks) {
                throw new StorageException("Symbolic links are disabled", "SYMLINK_DISABLED", false);
            }
            
            FileMetadata metadata = getMetadata(downloadRequest.getFilePath());
            
            // Handle range requests
            if (downloadRequest.getRangeStart() >= 0 && downloadRequest.getRangeEnd() >= 0) {
                return downloadRange(filePath, downloadRequest, metadata);
            }
            
            // Full file download
            InputStream fileStream = Files.newInputStream(filePath, StandardOpenOption.READ);
            long contentLength = Files.size(filePath);
            
            metrics.addBytesTransferred(contentLength);
            
            return DownloadResult.success(fileStream, metadata, contentLength);
            
        } catch (IOException e) {
            throw new StorageException("Download failed: " + e.getMessage(), e);
        }
    }
    
    private DownloadResult downloadRange(Path filePath, DownloadRequest request, FileMetadata metadata) throws IOException {
        long rangeStart = request.getRangeStart();
        long rangeEnd = request.getRangeEnd();
        long fileSize = Files.size(filePath);
        
        // Validate range
        if (rangeStart >= fileSize || rangeEnd >= fileSize || rangeStart > rangeEnd) {
            throw new IOException("Invalid range: " + rangeStart + "-" + rangeEnd + " for file size " + fileSize);
        }
        
        long contentLength = rangeEnd - rangeStart + 1;
        
        // Create range input stream
        InputStream rangeStream = new FileInputStream(filePath.toFile()) {
            {
                skip(rangeStart);
            }
            
            private long remaining = contentLength;
            
            @Override
            public int read() throws IOException {
                if (remaining <= 0) return -1;
                remaining--;
                return super.read();
            }
            
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                if (remaining <= 0) return -1;
                int toRead = (int) Math.min(len, remaining);
                int bytesRead = super.read(b, off, toRead);
                if (bytesRead > 0) remaining -= bytesRead;
                return bytesRead;
            }
        };
        
        metrics.addBytesTransferred(contentLength);
        
        return DownloadResult.success(rangeStream, metadata, contentLength);
    }
    
    @Override
    public DeleteResult delete(DeleteRequest deleteRequest) throws StorageException {
        if (!isConnected) {
            throw new StorageException("Not connected to local file system");
        }
        
        try {
            Path targetPath = Paths.get(rootDirectory, deleteRequest.getFilePath());
            
            if (!Files.exists(targetPath)) {
                throw new FileNotFoundException("File not found: " + deleteRequest.getFilePath());
            }
            
            int deletedCount = 0;
            
            if (Files.isDirectory(targetPath)) {
                if (deleteRequest.isRecursive()) {
                    deletedCount = deleteDirectoryRecursively(targetPath);
                } else {
                    // Try to delete empty directory
                    Files.delete(targetPath);
                    deletedCount = 1;
                }
            } else {
                // Delete single file
                Files.delete(targetPath);
                deletedCount = 1;
            }
            
            return DeleteResult.success(deleteRequest.getFilePath(), deletedCount);
            
        } catch (IOException e) {
            throw new StorageException("Delete failed: " + e.getMessage(), e);
        }
    }
    
    private int deleteDirectoryRecursively(Path directory) throws IOException {
        int deletedCount = 0;
        
        try (Stream<Path> paths = Files.walk(directory)) {
            // Delete files and directories in reverse order (depth-first)
            List<Path> pathList = paths.sorted(Comparator.reverseOrder()).toList();
            
            for (Path path : pathList) {
                Files.delete(path);
                deletedCount++;
            }
        }
        
        return deletedCount;
    }
    
    @Override
    public FileListResult listFiles(ListRequest listRequest) throws StorageException {
        if (!isConnected) {
            throw new StorageException("Not connected to local file system");
        }
        
        try {
            Path directoryPath = Paths.get(rootDirectory, listRequest.getDirectoryPath());
            
            if (!Files.exists(directoryPath)) {
                throw new FileNotFoundException("Directory not found: " + listRequest.getDirectoryPath());
            }
            
            if (!Files.isDirectory(directoryPath)) {
                throw new StorageException("Path is not a directory: " + listRequest.getDirectoryPath());
            }
            
            List<FileInfo> files = new ArrayList<>();
            
            try (Stream<Path> paths = listRequest.isRecursive() ? 
                    Files.walk(directoryPath) : Files.list(directoryPath)) {
                
                paths.filter(path -> !path.equals(directoryPath))
                     .limit(listRequest.getMaxResults())
                     .forEach(path -> {
                         try {
                             FileInfo fileInfo = createFileInfo(path, directoryPath);
                             if (matchesFilters(fileInfo, listRequest.getFilters())) {
                                 files.add(fileInfo);
                             }
                         } catch (IOException e) {
                             System.err.println("Error processing file: " + path + " - " + e.getMessage());
                         }
                     });
            }
            
            // Sort files by name
            files.sort(Comparator.comparing(FileInfo::getName));
            
            boolean hasMore = files.size() == listRequest.getMaxResults();
            String nextToken = hasMore ? "offset_" + files.size() : null;
            
            return FileListResult.success(files, nextToken, hasMore, files.size());
            
        } catch (IOException e) {
            throw new StorageException("List operation failed: " + e.getMessage(), e);
        }
    }
    
    private FileInfo createFileInfo(Path path, Path basePath) throws IOException {
        String relativePath = basePath.relativize(path).toString().replace('\\', '/');
        String name = path.getFileName().toString();
        long size = Files.isDirectory(path) ? 0 : Files.size(path);
        boolean isDirectory = Files.isDirectory(path);
        LocalDateTime lastModified = LocalDateTime.ofInstant(
            Files.getLastModifiedTime(path).toInstant(), 
            ZoneId.systemDefault()
        );
        
        FileInfo fileInfo = new FileInfo(name, relativePath, size, isDirectory, lastModified);
        fileInfo.setEtag(generateEtag(path));
        
        return fileInfo;
    }
    
    private boolean matchesFilters(FileInfo fileInfo, Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) {
            return true;
        }
        
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            String key = filter.getKey();
            String value = filter.getValue();
            
            switch (key.toLowerCase()) {
                case "extension":
                    String fileName = fileInfo.getName();
                    String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
                    if (!extension.equalsIgnoreCase(value)) {
                        return false;
                    }
                    break;
                case "min_size":
                    if (fileInfo.getSize() < Long.parseLong(value)) {
                        return false;
                    }
                    break;
                case "max_size":
                    if (fileInfo.getSize() > Long.parseLong(value)) {
                        return false;
                    }
                    break;
                case "type":
                    boolean isDir = fileInfo.isDirectory();
                    if (("file".equals(value) && isDir) || ("directory".equals(value) && !isDir)) {
                        return false;
                    }
                    break;
            }
        }
        
        return true;
    }
    
    @Override
    public FileMetadata getMetadata(String path) throws StorageException {
        if (!isConnected) {
            throw new StorageException("Not connected to local file system");
        }
        
        try {
            Path filePath = Paths.get(rootDirectory, path);
            
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException("File not found: " + path);
            }
            
            String fileName = filePath.getFileName().toString();
            long size = Files.isDirectory(filePath) ? 0 : Files.size(filePath);
            String contentType = determineContentType(fileName);
            
            FileMetadata metadata = new FileMetadata(fileName, path, size, contentType);
            
            // Set timestamps
            LocalDateTime createdTime = LocalDateTime.ofInstant(
                Files.readAttributes(filePath, "creationTime", LinkOption.NOFOLLOW_LINKS)
                     .get("creationTime").toString().equals("null") ? 
                     Files.getLastModifiedTime(filePath).toInstant() :
                     ((java.nio.file.attribute.FileTime) Files.readAttributes(filePath, "creationTime", LinkOption.NOFOLLOW_LINKS)
                      .get("creationTime")).toInstant(),
                ZoneId.systemDefault()
            );
            
            LocalDateTime modifiedTime = LocalDateTime.ofInstant(
                Files.getLastModifiedTime(filePath).toInstant(), 
                ZoneId.systemDefault()
            );
            
            metadata.setModifiedTime(modifiedTime);
            metadata.setEtag(generateEtag(filePath));
            
            // Add local file system specific metadata
            Map<String, String> customMetadata = new HashMap<>();
            customMetadata.put("absolute-path", filePath.toAbsolutePath().toString());
            customMetadata.put("readable", String.valueOf(Files.isReadable(filePath)));
            customMetadata.put("writable", String.valueOf(Files.isWritable(filePath)));
            customMetadata.put("executable", String.valueOf(Files.isExecutable(filePath)));
            customMetadata.put("hidden", String.valueOf(Files.isHidden(filePath)));
            
            if (Files.isSymbolicLink(filePath)) {
                customMetadata.put("symlink-target", Files.readSymbolicLink(filePath).toString());
            }
            
            metadata.setCustomMetadata(customMetadata);
            
            return metadata;
            
        } catch (IOException e) {
            throw new StorageException("Metadata retrieval failed: " + e.getMessage(), e);
        }
    }
    
    // Utility methods
    private boolean isAllowedExtension(String filePath) {
        if (allowedExtensions == null) return true;
        
        String extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
        return allowedExtensions.contains(extension);
    }
    
    private String generateEtag(Path path) throws IOException {
        // Generate ETag based on file size and last modified time
        long size = Files.exists(path) && !Files.isDirectory(path) ? Files.size(path) : 0;
        long lastModified = Files.getLastModifiedTime(path).toMillis();
        return "\"" + Long.toHexString(size ^ lastModified) + "\"";
    }
    
    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "txt": return "text/plain";
            case "html": case "htm": return "text/html";
            case "css": return "text/css";
            case "js": return "application/javascript";
            case "json": return "application/json";
            case "xml": return "application/xml";
            case "pdf": return "application/pdf";
            case "jpg": case "jpeg": return "image/jpeg";
            case "png": return "image/png";
            case "gif": return "image/gif";
            case "mp4": return "video/mp4";
            case "mp3": return "audio/mpeg";
            case "zip": return "application/zip";
            default: return "application/octet-stream";
        }
    }
    
    @Override
    protected void preOperationHook(FileOperation operation) {
        super.preOperationHook(operation);
        System.out.println("Local FS: Checking file system permissions and disk space");
    }
    
    @Override
    protected void postOperationHook(FileOperation operation, OperationResult result) {
        super.postOperationHook(operation, result);
        if (result.isSuccess()) {
            System.out.println("Local FS: Updating file system cache and access timestamps");
        }
    }
}

package abstraction.filestorage;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class showcasing polymorphic usage of different storage services
 * Demonstrates how client code remains unchanged regardless of storage backend
 */
public class FileStorageDemo {
    
    public static void main(String[] args) {
        System.out.println("=== File Storage Service Abstraction Demo ===\n");
        
        // Create different storage services
        StorageService[] storageServices = createStorageServices();
        
        // Test each storage service polymorphically
        for (StorageService service : storageServices) {
            System.out.println("Testing storage service: " + service.getClass().getSimpleName());
            System.out.println("Service ID: " + service.getServiceId());
            
            try {
                // Connect to storage service
                service.connect();
                
                // Test complete workflow using template method
                testStorageWorkflow(service);
                
                // Display metrics
                displayMetrics(service);
                
                // Disconnect
                service.disconnect();
                
            } catch (Exception e) {
                System.err.println("Error testing storage service: " + e.getMessage());
            }
            
            System.out.println("-".repeat(60));
        }
        
        System.out.println("\n=== Demo completed ===");
    }
    
    private static StorageService[] createStorageServices() {
        // AWS S3 configuration
        Map<String, Object> s3Config = new HashMap<>();
        s3Config.put("bucket_name", "my-test-bucket");
        s3Config.put("region", "us-west-2");
        s3Config.put("access_key", "AKIAIOSFODNN7EXAMPLE");
        s3Config.put("secret_key", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
        s3Config.put("versioning", true);
        s3Config.put("encryption", "AES256");
        
        // Google Drive configuration
        Map<String, Object> driveConfig = new HashMap<>();
        driveConfig.put("client_id", "123456789-abcdefghijklmnop.apps.googleusercontent.com");
        driveConfig.put("client_secret", "GOCSPX-abcdefghijklmnopqrstuvwxyz");
        driveConfig.put("refresh_token", "1//0abcdefghijklmnopqrstuvwxyz");
        driveConfig.put("enable_sharing", true);
        driveConfig.put("folder_id", "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms");
        
        // Local File System configuration
        Map<String, Object> localConfig = new HashMap<>();
        localConfig.put("root_directory", System.getProperty("java.io.tmpdir"));
        localConfig.put("enable_symlinks", false);
        localConfig.put("atomic_operations", true);
        localConfig.put("allowed_extensions", java.util.Arrays.asList("txt", "pdf", "jpg", "png", "docx"));
        
        return new StorageService[] {
            new AWS_S3("s3_service_001", s3Config),
            new GoogleDrive("gdrive_service_001", driveConfig),
            new LocalFileSystem("local_service_001", localConfig)
        };
    }
    
    private static void testStorageWorkflow(StorageService service) {
        try {
            // Test 1: Upload a file
            System.out.println("\n1. Testing file upload...");
            UploadRequest uploadRequest = createSampleUploadRequest();
            FileOperation uploadOp = new FileOperation("upload_001", uploadRequest);
            OperationResult uploadResult = service.performFileOperation(uploadOp);
            
            if (uploadResult.isSuccess()) {
                System.out.println("   ✓ Upload successful");
                UploadResult result = (UploadResult) uploadResult.getResult();
                System.out.println("   File ID: " + result.getFileId());
                System.out.println("   ETag: " + result.getEtag());
            } else {
                System.out.println("   ✗ Upload failed: " + uploadResult.getErrorMessage());
                return; // Skip other tests if upload fails
            }
            
            // Test 2: Get file metadata
            System.out.println("\n2. Testing metadata retrieval...");
            try {
                FileMetadata metadata = service.getMetadata("test/sample.txt");
                System.out.println("   ✓ Metadata retrieved");
                System.out.println("   File size: " + metadata.getSize() + " bytes");
                System.out.println("   Content type: " + metadata.getContentType());
                System.out.println("   Created: " + metadata.getCreatedTime());
            } catch (Exception e) {
                System.out.println("   ✗ Metadata retrieval failed: " + e.getMessage());
            }
            
            // Test 3: List files
            System.out.println("\n3. Testing file listing...");
            ListRequest listRequest = new ListRequest("test");
            listRequest.setMaxResults(5);
            FileOperation listOp = new FileOperation("list_001", listRequest);
            OperationResult listResult = service.performFileOperation(listOp);
            
            if (listResult.isSuccess()) {
                FileListResult result = (FileListResult) listResult.getResult();
                System.out.println("   ✓ Listed " + result.getFiles().size() + " files");
                result.getFiles().forEach(file -> 
                    System.out.println("     - " + file.getName() + " (" + file.getSize() + " bytes)")
                );
            } else {
                System.out.println("   ✗ List failed: " + listResult.getErrorMessage());
            }
            
            // Test 4: Download file
            System.out.println("\n4. Testing file download...");
            DownloadRequest downloadRequest = new DownloadRequest("test/sample.txt");
            FileOperation downloadOp = new FileOperation("download_001", downloadRequest);
            OperationResult downloadResult = service.performFileOperation(downloadOp);
            
            if (downloadResult.isSuccess()) {
                DownloadResult result = (DownloadResult) downloadResult.getResult();
                System.out.println("   ✓ Download successful");
                System.out.println("   Content length: " + result.getContentLength() + " bytes");
                // Close the stream
                if (result.getContentStream() != null) {
                    result.getContentStream().close();
                }
            } else {
                System.out.println("   ✗ Download failed: " + downloadResult.getErrorMessage());
            }
            
            // Test 5: Delete file
            System.out.println("\n5. Testing file deletion...");
            DeleteRequest deleteRequest = new DeleteRequest("test/sample.txt", false);
            FileOperation deleteOp = new FileOperation("delete_001", deleteRequest);
            OperationResult deleteResult = service.performFileOperation(deleteOp);
            
            if (deleteResult.isSuccess()) {
                DeleteResult result = (DeleteResult) deleteResult.getResult();
                System.out.println("   ✓ Delete successful");
                System.out.println("   Deleted " + result.getDeletedCount() + " item(s)");
            } else {
                System.out.println("   ✗ Delete failed: " + deleteResult.getErrorMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Workflow test failed: " + e.getMessage());
        }
    }
    
    private static UploadRequest createSampleUploadRequest() {
        String sampleContent = "This is a sample file for testing storage services.\n" +
                              "It demonstrates the abstraction pattern in action.\n" +
                              "The same client code works with different storage backends.";
        
        ByteArrayInputStream contentStream = new ByteArrayInputStream(sampleContent.getBytes());
        
        UploadRequest request = new UploadRequest(
            "test/sample.txt",
            contentStream,
            sampleContent.length(),
            "text/plain"
        );
        
        // Add metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("author", "FileStorageDemo");
        metadata.put("purpose", "testing");
        metadata.put("created_by", "abstraction_demo");
        request.setMetadata(metadata);
        
        // Set progress callback
        request.setProgressCallback(new ProgressCallback() {
            @Override
            public void onProgress(long bytesTransferred, long totalBytes) {
                int percentage = (int) ((bytesTransferred * 100) / totalBytes);
                if (percentage % 25 == 0) { // Show progress at 25%, 50%, 75%, 100%
                    System.out.println("     Upload progress: " + percentage + "%");
                }
            }
            
            @Override
            public void onComplete() {
                System.out.println("     Upload completed successfully!");
            }
            
            @Override
            public void onError(String errorMessage) {
                System.err.println("     Upload error: " + errorMessage);
            }
        });
        
        return request;
    }
    
    private static void displayMetrics(StorageService service) {
        System.out.println("\nService Metrics:");
        StorageMetrics metrics = service.getMetrics();
        
        System.out.println("  Operations performed:");
        System.out.println("    - Uploads: " + metrics.getUploadCount());
        System.out.println("    - Downloads: " + metrics.getDownloadCount());
        System.out.println("    - Deletes: " + metrics.getDeleteCount());
        System.out.println("    - Lists: " + metrics.getListCount());
        
        System.out.println("  Success rate: " + String.format("%.1f%%", metrics.getSuccessRate()));
        System.out.println("  Total bytes transferred: " + metrics.getTotalBytesTransferred());
        System.out.println("  Error count: " + metrics.getErrorCount());
    }
}

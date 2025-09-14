
import java.time.LocalDateTime;
import java.util.List;

/**
 * Demo class to demonstrate Versioned Document
 */
public class VersionedDocumentDemo {
    public static void main(String[] args) {
        System.out.println("=== Versioned Document Demo ===\n");
        
        // Test document creation and versioning
        testDocumentCreationAndVersioning();
        
        // Test version history
        testVersionHistory();
        
        // Test version comparison
        testVersionComparison();
        
        // Test version filtering
        testVersionFiltering();
        
        // Test document metadata
        testDocumentMetadata();
    }
    
    private static void testDocumentCreationAndVersioning() {
        System.out.println("=== Document Creation and Versioning Test ===");
        
        VersionedDocument document = new VersionedDocument(
            "DOC_001", "Project Requirements", "Initial project requirements document", "Alice"
        );
        
        System.out.println("Document created: " + document);
        System.out.println("Current version: " + document.getCurrentVersionNumber());
        System.out.println("Current content: " + document.getCurrentContent());
        
        // Create new version
        int version2 = document.createNewVersion(
            "Updated project requirements with new features", 
            "Bob", 
            "Added new feature requirements"
        );
        System.out.println("Created version 2: " + (version2 > 0 ? "SUCCESS" : "FAILED"));
        
        // Create another version
        int version3 = document.createNewVersion(
            "Final project requirements with all features and constraints", 
            "Charlie", 
            "Added constraints and finalized requirements"
        );
        System.out.println("Created version 3: " + (version3 > 0 ? "SUCCESS" : "FAILED"));
        
        System.out.println("Current version: " + document.getCurrentVersionNumber());
        System.out.println("Current content: " + document.getCurrentContent());
        
        System.out.println();
    }
    
    private static void testVersionHistory() {
        System.out.println("=== Version History Test ===");
        
        VersionedDocument document = new VersionedDocument(
            "DOC_002", "API Documentation", "Initial API documentation", "Developer1"
        );
        
        // Create multiple versions
        document.createNewVersion("API documentation with authentication", "Developer2", "Added auth section");
        document.createNewVersion("API documentation with all endpoints", "Developer3", "Added all endpoints");
        document.createNewVersion("API documentation with examples", "Developer1", "Added code examples");
        
        // Get version history
        List<VersionedDocument.DocumentVersion> history = document.getHistory();
        System.out.println("Version history (" + history.size() + " versions):");
        
        for (VersionedDocument.DocumentVersion version : history) {
            System.out.println("  " + version);
        }
        
        // Get specific version content
        String version1Content = document.getVersionContent(1);
        System.out.println("Version 1 content: " + version1Content);
        
        String version2Content = document.getVersionContent(2);
        System.out.println("Version 2 content: " + version2Content);
        
        System.out.println();
    }
    
    private static void testVersionComparison() {
        System.out.println("=== Version Comparison Test ===");
        
        VersionedDocument document = new VersionedDocument(
            "DOC_003", "User Manual", "Basic user manual", "Writer1"
        );
        
        // Create versions with different content lengths
        document.createNewVersion("Comprehensive user manual with detailed instructions", "Writer2", "Added detailed instructions");
        document.createNewVersion("User manual with troubleshooting section", "Writer3", "Added troubleshooting");
        
        // Compare versions
        VersionedDocument.VersionComparison comparison = document.compareVersions(1, 2);
        if (comparison != null) {
            System.out.println("Comparison v1 vs v2: " + comparison);
            System.out.println("Content changed: " + comparison.isContentChanged());
            System.out.println("Length difference: " + comparison.getContentLengthDifference());
        }
        
        comparison = document.compareVersions(2, 3);
        if (comparison != null) {
            System.out.println("Comparison v2 vs v3: " + comparison);
            System.out.println("Content changed: " + comparison.isContentChanged());
            System.out.println("Length difference: " + comparison.getContentLengthDifference());
        }
        
        System.out.println();
    }
    
    private static void testVersionFiltering() {
        System.out.println("=== Version Filtering Test ===");
        
        VersionedDocument document = new VersionedDocument(
            "DOC_004", "Technical Spec", "Initial technical specification", "Engineer1"
        );
        
        // Create versions with different users
        document.createNewVersion("Technical spec with architecture", "Engineer2", "Added architecture");
        document.createNewVersion("Technical spec with database design", "Engineer1", "Added database design");
        document.createNewVersion("Technical spec with security considerations", "Engineer3", "Added security");
        
        // Get versions by user
        List<VersionedDocument.DocumentVersion> engineer1Versions = document.getVersionsByUser("Engineer1");
        System.out.println("Versions by Engineer1: " + engineer1Versions.size());
        
        List<VersionedDocument.DocumentVersion> engineer2Versions = document.getVersionsByUser("Engineer2");
        System.out.println("Versions by Engineer2: " + engineer2Versions.size());
        
        // Get versions in time range
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        List<VersionedDocument.DocumentVersion> timeRangeVersions = document.getVersionsInTimeRange(startTime, endTime);
        System.out.println("Versions in time range: " + timeRangeVersions.size());
        
        System.out.println();
    }
    
    private static void testDocumentMetadata() {
        System.out.println("=== Document Metadata Test ===");
        
        VersionedDocument document = new VersionedDocument(
            "DOC_005", "Project Plan", "Initial project plan", "Manager1"
        );
        
        // Create some versions
        document.createNewVersion("Updated project plan with timeline", "Manager2", "Added timeline");
        document.createNewVersion("Final project plan with resources", "Manager1", "Added resources");
        
        // Get document metadata
        VersionedDocument.DocumentMetadata metadata = document.getMetadata();
        System.out.println("Document metadata: " + metadata);
        
        // Get document summary
        String summary = document.getDocumentSummary();
        System.out.println("Document summary:");
        System.out.println(summary);
        
        // Test version existence
        System.out.println("Has version 1: " + document.hasVersion(1));
        System.out.println("Has version 2: " + document.hasVersion(2));
        System.out.println("Has version 5: " + document.hasVersion(5));
        
        System.out.println();
    }
    
    /**
     * Test document with many versions
     */
    private static void testManyVersions() {
        System.out.println("=== Many Versions Test ===");
        
        VersionedDocument document = new VersionedDocument(
            "DOC_006", "Code Review", "Initial code review", "Reviewer1"
        );
        
        // Create many versions
        for (int i = 2; i <= 10; i++) {
            document.createNewVersion(
                "Code review version " + i + " with additional comments", 
                "Reviewer" + (i % 3 + 1), 
                "Added comments for version " + i
            );
        }
        
        System.out.println("Created 10 versions");
        System.out.println("Total versions: " + document.getTotalVersions());
        System.out.println("Current version: " + document.getCurrentVersionNumber());
        
        // Get all versions
        List<VersionedDocument.DocumentVersion> allVersions = document.getHistory();
        System.out.println("All versions:");
        for (VersionedDocument.DocumentVersion version : allVersions) {
            System.out.println("  " + version);
        }
        
        System.out.println();
    }
}

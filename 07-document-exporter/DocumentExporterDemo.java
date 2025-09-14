package abstraction.documentexporter;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Demo class showcasing polymorphic usage of different document exporters
 * Demonstrates how client code remains unchanged regardless of export format
 */
public class DocumentExporterDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Document Exporter Abstraction Demo ===\n");
        
        // Create different document exporters
        DocumentExporter[] exporters = createDocumentExporters();
        
        // Create sample documents for testing
        List<Document> sampleDocuments = createSampleDocuments();
        
        // Test each exporter polymorphically
        for (DocumentExporter exporter : exporters) {
            System.out.println("Testing Document Exporter: " + exporter.getClass().getSimpleName());
            System.out.println("Exporter ID: " + exporter.getExporterId());
            System.out.println("Exporter Name: " + exporter.getExporterName());
            System.out.println("Export Format: " + exporter.getExportFormat());
            System.out.println("Current State: " + exporter.getCurrentState());
            
            try {
                // Test complete export workflow using template method
                testExportWorkflow(exporter, sampleDocuments);
                
                // Test exporter-specific features
                testExporterSpecificFeatures(exporter);
                
                // Display exporter status
                displayExporterStatus(exporter);
                
            } catch (Exception e) {
                System.err.println("Error testing exporter: " + e.getMessage());
            }
            
            System.out.println("-".repeat(80));
        }
        
        System.out.println("\n=== Demo completed ===");
    }
    
    private static DocumentExporter[] createDocumentExporters() {
        // PDF Exporter configuration
        Map<String, Object> pdfConfig = new HashMap<>();
        pdfConfig.put("default_font", "Arial");
        pdfConfig.put("default_font_size", 12);
        pdfConfig.put("page_orientation", "PORTRAIT");
        pdfConfig.put("enable_bookmarks", true);
        pdfConfig.put("enable_hyperlinks", true);
        
        // HTML Exporter configuration
        Map<String, Object> htmlConfig = new HashMap<>();
        htmlConfig.put("html_version", "HTML5");
        htmlConfig.put("include_css", true);
        htmlConfig.put("include_javascript", false);
        htmlConfig.put("responsive_design", true);
        htmlConfig.put("css_framework", "bootstrap");
        
        // JSON Exporter configuration
        Map<String, Object> jsonConfig = new HashMap<>();
        jsonConfig.put("pretty_print", true);
        jsonConfig.put("include_schema", false);
        jsonConfig.put("validate_output", true);
        jsonConfig.put("date_format", "ISO_8601");
        jsonConfig.put("indent_size", 2);
        
        return new DocumentExporter[] {
            new PDFExporter("pdf_001", "Technical Report PDF Exporter", pdfConfig),
            new HTMLExporter("html_001", "Web Document HTML Exporter", htmlConfig),
            new JSONExporter("json_001", "Structured Data JSON Exporter", jsonConfig)
        };
    }
    
    private static List<Document> createSampleDocuments() {
        List<Document> documents = new ArrayList<>();
        
        // Technical Report Document
        Document techReport = new Document("doc_001", "System Architecture Report", 
            "This document outlines the comprehensive system architecture for our enterprise application. " +
            "The architecture follows microservices patterns with event-driven communication between services. " +
            "Key components include API Gateway, Service Registry, Configuration Server, and Message Broker.",
            DocumentType.REPORT);
        
        techReport.addMetadata("author", "System Architect");
        techReport.addMetadata("version", "1.0");
        techReport.addMetadata("classification", "internal");
        
        // Add sections to technical report
        techReport.addSection(new DocumentSection("sec_001", "Introduction", 
            "The system architecture defines the structural design of our application ecosystem. " +
            "This includes service boundaries, data flow, and integration patterns.",
            1, SectionType.PARAGRAPH));
        
        techReport.addSection(new DocumentSection("sec_002", "Core Components", 
            "API Gateway,Service Registry,Configuration Server,Message Broker,Database Cluster",
            2, SectionType.LIST));
        
        techReport.addSection(new DocumentSection("sec_003", "Service Communication", 
            "Service,Protocol,Port,Description\n" +
            "User Service,HTTP,8080,Manages user accounts\n" +
            "Order Service,HTTP,8081,Processes orders\n" +
            "Payment Service,HTTPS,8082,Handles payments\n" +
            "Notification Service,AMQP,5672,Sends notifications",
            3, SectionType.TABLE));
        
        techReport.addSection(new DocumentSection("sec_004", "Implementation Code", 
            "@RestController\n" +
            "public class UserController {\n" +
            "    @Autowired\n" +
            "    private UserService userService;\n" +
            "    \n" +
            "    @GetMapping(\"/users/{id}\")\n" +
            "    public ResponseEntity<User> getUser(@PathVariable Long id) {\n" +
            "        User user = userService.findById(id);\n" +
            "        return ResponseEntity.ok(user);\n" +
            "    }\n" +
            "}",
            4, SectionType.CODE));
        
        documents.add(techReport);
        
        // User Manual Document
        Document userManual = new Document("doc_002", "Application User Guide", 
            "Welcome to the comprehensive user guide for our enterprise application. " +
            "This guide will walk you through all the features and functionalities available.",
            DocumentType.MANUAL);
        
        userManual.addMetadata("target_audience", "end_users");
        userManual.addMetadata("difficulty_level", "beginner");
        userManual.addMetadata("estimated_reading_time", "30 minutes");
        
        userManual.addSection(new DocumentSection("man_001", "Getting Started", 
            "To begin using the application, first log in with your credentials. " +
            "Navigate to the dashboard to access all available features.",
            1, SectionType.PARAGRAPH));
        
        userManual.addSection(new DocumentSection("man_002", "Key Features", 
            "User Management,Document Processing,Report Generation,Data Analytics,System Administration",
            2, SectionType.LIST));
        
        userManual.addSection(new DocumentSection("man_003", "Important Note", 
            "Always save your work frequently to prevent data loss. " +
            "The system automatically creates backups every 15 minutes.",
            3, SectionType.QUOTE));
        
        documents.add(userManual);
        
        // Research Article Document
        Document researchArticle = new Document("doc_003", "Machine Learning in Document Processing", 
            "This research explores the application of machine learning techniques in automated document processing. " +
            "We examine various algorithms and their effectiveness in different document types.",
            DocumentType.ARTICLE);
        
        researchArticle.addMetadata("authors", "Dr. Smith, Prof. Johnson");
        researchArticle.addMetadata("publication_date", "2024-01-15");
        researchArticle.addMetadata("keywords", "machine learning, document processing, automation");
        
        researchArticle.addSection(new DocumentSection("art_001", "Abstract", 
            "Document processing has evolved significantly with the integration of machine learning. " +
            "This study presents novel approaches to automated content extraction and classification.",
            1, SectionType.PARAGRAPH));
        
        researchArticle.addSection(new DocumentSection("art_002", "Methodology", 
            "Data Collection,Feature Extraction,Model Training,Validation,Performance Evaluation",
            2, SectionType.LIST));
        
        documents.add(researchArticle);
        
        return documents;
    }
    
    private static void testExportWorkflow(DocumentExporter exporter, List<Document> documents) {
        try {
            System.out.println("\n1. Testing export workflow...");
            
            for (Document document : documents) {
                // Create export request
                ExportRequest exportRequest = createExportRequest(exporter, document);
                
                // Export document using template method
                System.out.println("   Exporting: " + document.getTitle());
                var exportFuture = exporter.exportDocument(exportRequest);
                
                ExportResult exportResult = exportFuture.get();
                
                if (exportResult.isSuccess()) {
                    System.out.println("   ✓ Export completed successfully");
                    System.out.println("   File: " + exportResult.getFileName());
                    System.out.println("   Size: " + exportResult.getFileSize() + " bytes");
                    System.out.println("   MIME Type: " + exportResult.getMimeType());
                    
                    // Display metadata
                    if (!exportResult.getMetadata().isEmpty()) {
                        System.out.println("   Metadata:");
                        exportResult.getMetadata().entrySet().stream()
                            .limit(3) // Show only first 3 metadata entries
                            .forEach(entry -> 
                                System.out.println("     " + entry.getKey() + ": " + entry.getValue()));
                    }
                } else {
                    System.out.println("   ✗ Export failed: " + exportResult.getMessage());
                }
            }
            
            // Test batch export
            testBatchExport(exporter, documents);
            
            // Test preview generation
            testPreviewGeneration(exporter, documents.get(0));
            
            // Test format conversion
            testFormatConversion(exporter, documents.get(0));
            
        } catch (Exception e) {
            System.err.println("   Export workflow test failed: " + e.getMessage());
        }
    }
    
    private static ExportRequest createExportRequest(DocumentExporter exporter, Document document) {
        ExportOptions options = new ExportOptions();
        options.setQuality(85);
        options.setPageSize("A4");
        options.setIncludeMetadata(true);
        options.setCompressOutput(false);
        
        // Format-specific options
        switch (exporter.getExportFormat()) {
            case PDF:
                options.addCustomOption("pdf_version", "1.7");
                options.addCustomOption("add_page_numbers", true);
                options.addCustomOption("header_text", "System Documentation");
                break;
            case HTML:
                options.addCustomOption("include_toc", true);
                options.addCustomOption("color_scheme", "light");
                options.addCustomOption("responsive_design", true);
                break;
            case JSON:
                options.addCustomOption("include_export_options", true);
                options.addCustomOption("date_format", "ISO_8601");
                break;
        }
        
        return new ExportRequest(
            "req_" + System.currentTimeMillis(),
            document,
            options,
            "demo_user"
        );
    }
    
    private static void testBatchExport(DocumentExporter exporter, List<Document> documents) {
        try {
            System.out.println("\n2. Testing batch export...");
            
            ExportOptions batchOptions = new ExportOptions();
            batchOptions.setQuality(75); // Lower quality for batch processing
            batchOptions.setCompressOutput(true);
            
            BatchExportRequest batchRequest = new BatchExportRequest(
                "batch_" + System.currentTimeMillis(),
                documents,
                batchOptions,
                "demo_user"
            );
            
            var batchFuture = exporter.exportBatch(batchRequest);
            BatchExportResult batchResult = batchFuture.get();
            
            if (batchResult.isSuccess()) {
                System.out.println("   ✓ Batch export completed successfully");
                System.out.println("   Total Documents: " + batchResult.getTotalDocuments());
                System.out.println("   Successful Exports: " + batchResult.getSuccessfulExports());
                System.out.println("   Failed Exports: " + batchResult.getFailedExports());
                System.out.println("   Progress: " + String.format("%.1f", batchResult.getProgressPercentage()) + "%");
            } else {
                System.out.println("   ✗ Batch export failed: " + batchResult.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("   Batch export test failed: " + e.getMessage());
        }
    }
    
    private static void testPreviewGeneration(DocumentExporter exporter, Document document) {
        try {
            System.out.println("\n3. Testing preview generation...");
            
            PreviewRequest previewRequest = new PreviewRequest(
                "preview_" + System.currentTimeMillis(),
                document,
                "A4",
                "demo_user"
            );
            
            var previewFuture = exporter.generatePreview(previewRequest);
            PreviewResult previewResult = previewFuture.get();
            
            if (previewResult.isSuccess()) {
                System.out.println("   ✓ Preview generated successfully");
                System.out.println("   Preview Size: " + previewResult.getPreviewSize());
                System.out.println("   Preview Format: " + previewResult.getPreviewFormat());
                System.out.println("   Generated At: " + previewResult.getGeneratedAt());
            } else {
                System.out.println("   ✗ Preview generation failed: " + previewResult.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("   Preview generation test failed: " + e.getMessage());
        }
    }
    
    private static void testFormatConversion(DocumentExporter exporter, Document document) {
        try {
            System.out.println("\n4. Testing format conversion...");
            
            // Test conversion to different formats
            ExportFormat[] targetFormats = {ExportFormat.TXT, ExportFormat.HTML, ExportFormat.PDF};
            
            for (ExportFormat targetFormat : targetFormats) {
                if (exporter.isConversionSupported(targetFormat) && targetFormat != exporter.getExportFormat()) {
                    ExportOptions conversionOptions = new ExportOptions();
                    conversionOptions.setQuality(70);
                    
                    ConversionRequest conversionRequest = new ConversionRequest(
                        "conv_" + System.currentTimeMillis(),
                        document,
                        targetFormat,
                        conversionOptions,
                        "demo_user"
                    );
                    
                    var conversionFuture = exporter.convertFormat(conversionRequest);
                    ConversionResult conversionResult = conversionFuture.get();
                    
                    if (conversionResult.isSuccess()) {
                        System.out.println("   ✓ Conversion to " + targetFormat + " successful");
                        System.out.println("     Original Format: " + conversionResult.getOriginalFormat());
                        System.out.println("     Target Format: " + conversionResult.getTargetFormat());
                        System.out.println("     Conversion Time: " + conversionResult.getConversionTime());
                    } else {
                        System.out.println("   ✗ Conversion to " + targetFormat + " failed: " + conversionResult.getMessage());
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("   Format conversion test failed: " + e.getMessage());
        }
    }
    
    private static void testExporterSpecificFeatures(DocumentExporter exporter) {
        System.out.println("\n5. Testing exporter-specific features...");
        
        try {
            if (exporter instanceof PDFExporter) {
                testPDFExporterFeatures((PDFExporter) exporter);
            } else if (exporter instanceof HTMLExporter) {
                testHTMLExporterFeatures((HTMLExporter) exporter);
            } else if (exporter instanceof JSONExporter) {
                testJSONExporterFeatures((JSONExporter) exporter);
            }
        } catch (Exception e) {
            System.err.println("   Exporter-specific test failed: " + e.getMessage());
        }
    }
    
    private static void testPDFExporterFeatures(PDFExporter pdfExporter) {
        System.out.println("   Testing PDF Exporter specific features:");
        System.out.println("   - Default Font: " + pdfExporter.getDefaultFont());
        System.out.println("   - Default Font Size: " + pdfExporter.getDefaultFontSize());
        System.out.println("   - Page Orientation: " + pdfExporter.getPageOrientation());
        System.out.println("   - Bookmarks Enabled: " + pdfExporter.isBookmarksEnabled());
        System.out.println("   - Hyperlinks Enabled: " + pdfExporter.isHyperlinksEnabled());
        System.out.println("   - File Extension: " + pdfExporter.getFileExtension());
        System.out.println("   - MIME Type: " + pdfExporter.getMimeType());
        System.out.println("   ✓ PDF Exporter features tested");
    }
    
    private static void testHTMLExporterFeatures(HTMLExporter htmlExporter) {
        System.out.println("   Testing HTML Exporter specific features:");
        System.out.println("   - HTML Version: " + htmlExporter.getHtmlVersion());
        System.out.println("   - Include CSS: " + htmlExporter.isIncludeCSS());
        System.out.println("   - Include JavaScript: " + htmlExporter.isIncludeJavaScript());
        System.out.println("   - Responsive Design: " + htmlExporter.isResponsiveDesign());
        System.out.println("   - CSS Framework: " + htmlExporter.getCssFramework());
        System.out.println("   - File Extension: " + htmlExporter.getFileExtension());
        System.out.println("   - MIME Type: " + htmlExporter.getMimeType());
        System.out.println("   ✓ HTML Exporter features tested");
    }
    
    private static void testJSONExporterFeatures(JSONExporter jsonExporter) {
        System.out.println("   Testing JSON Exporter specific features:");
        System.out.println("   - Pretty Print: " + jsonExporter.isPrettyPrint());
        System.out.println("   - Include Schema: " + jsonExporter.isIncludeSchema());
        System.out.println("   - Validate Output: " + jsonExporter.isValidateOutput());
        System.out.println("   - Date Format: " + jsonExporter.getDateFormat());
        System.out.println("   - Indent Size: " + jsonExporter.getIndentSize());
        System.out.println("   - File Extension: " + jsonExporter.getFileExtension());
        System.out.println("   - MIME Type: " + jsonExporter.getMimeType());
        System.out.println("   ✓ JSON Exporter features tested");
    }
    
    private static void displayExporterStatus(DocumentExporter exporter) {
        System.out.println("\n6. Exporter Status Information:");
        
        ExporterStatus status = exporter.getStatus();
        System.out.println("   Exporter ID: " + status.getExporterId());
        System.out.println("   Exporter Name: " + status.getExporterName());
        System.out.println("   Export Format: " + status.getExportFormat());
        System.out.println("   Current State: " + status.getCurrentState());
        System.out.println("   File Extension: " + status.getFileExtension());
        System.out.println("   MIME Type: " + status.getMimeType());
        System.out.println("   Status Time: " + status.getStatusTime());
        
        // Display document processor information
        DocumentProcessor processor = exporter.getDocumentProcessor();
        System.out.println("   Document Processor: " + processor.getClass().getSimpleName());
        
        // Display validator information
        FormatValidator validator = exporter.getValidator();
        System.out.println("   Format Validator: Configured for " + exporter.getExportFormat());
        
        // Display compression manager
        CompressionManager compressionManager = exporter.getCompressionManager();
        System.out.println("   Compression Manager: Available");
        
        // Display security manager
        SecurityManager securityManager = exporter.getSecurityManager();
        System.out.println("   Security Manager: Available");
        
        // Test configuration features
        System.out.println("   Configuration Features:");
        System.out.println("     Supports Compression: " + exporter.supportsFeature("compression"));
        System.out.println("     Supports Encryption: " + exporter.supportsFeature("encryption"));
        System.out.println("     Supports Watermark: " + exporter.supportsFeature("watermark"));
        System.out.println("     Supports Batch Export: " + exporter.supportsFeature("batch_export"));
    }
}

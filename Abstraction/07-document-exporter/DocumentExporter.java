package abstraction.documentexporter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract Document Exporter class defining the template for all document export formats
 * Uses Template Method pattern to enforce common export workflow while allowing customization
 */
public abstract class DocumentExporter {
    
    protected String exporterId;
    protected String exporterName;
    protected ExportFormat exportFormat;
    protected ExportState currentState;
    protected DocumentProcessor documentProcessor;
    protected FormatValidator validator;
    protected CompressionManager compressionManager;
    protected SecurityManager securityManager;
    protected Map<String, Object> configuration;
    
    public DocumentExporter(String exporterId, String exporterName, ExportFormat format, 
                           Map<String, Object> configuration) {
        this.exporterId = exporterId;
        this.exporterName = exporterName;
        this.exportFormat = format;
        this.configuration = configuration;
        this.currentState = ExportState.INITIALIZED;
        
        this.documentProcessor = createDocumentProcessor();
        this.validator = new FormatValidator(format);
        this.compressionManager = new CompressionManager();
        this.securityManager = new SecurityManager();
        
        initialize();
    }
    
    // Template method for document export
    public final CompletableFuture<ExportResult> exportDocument(ExportRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Pre-export validation
                ValidationResult preValidation = performPreExportValidation(request);
                if (!preValidation.isSuccess()) {
                    return ExportResult.failure("Pre-export validation failed: " + preValidation.getMessage());
                }
                
                currentState = ExportState.PROCESSING;
                
                // Document preprocessing
                ProcessingResult processingResult = preprocessDocument(request.getDocument());
                if (!processingResult.isSuccess()) {
                    currentState = ExportState.ERROR;
                    return ExportResult.failure("Document preprocessing failed: " + processingResult.getMessage());
                }
                
                // Format-specific export (abstract method)
                ExportResult coreResult = performCoreExport(request, processingResult.getProcessedDocument());
                if (!coreResult.isSuccess()) {
                    currentState = ExportState.ERROR;
                    return coreResult;
                }
                
                // Post-processing
                ExportResult postProcessedResult = performPostProcessing(coreResult, request);
                if (!postProcessedResult.isSuccess()) {
                    currentState = ExportState.ERROR;
                    return postProcessedResult;
                }
                
                // Security and compression
                ExportResult finalResult = applySecurityAndCompression(postProcessedResult, request);
                
                currentState = ExportState.COMPLETED;
                return finalResult;
                
            } catch (Exception e) {
                currentState = ExportState.ERROR;
                return ExportResult.failure("Export failed: " + e.getMessage());
            }
        });
    }
    
    // Template method for batch export
    public final CompletableFuture<BatchExportResult> exportBatch(BatchExportRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                currentState = ExportState.BATCH_PROCESSING;
                
                BatchExportResult batchResult = new BatchExportResult(request.getBatchId());
                
                for (Document document : request.getDocuments()) {
                    ExportRequest singleRequest = new ExportRequest(
                        "batch_" + System.currentTimeMillis(),
                        document,
                        request.getExportOptions(),
                        request.getRequestedBy()
                    );
                    
                    ExportResult singleResult = exportDocument(singleRequest).get();
                    batchResult.addResult(document.getDocumentId(), singleResult);
                    
                    // Update progress
                    batchResult.updateProgress();
                }
                
                currentState = ExportState.COMPLETED;
                return batchResult;
                
            } catch (Exception e) {
                currentState = ExportState.ERROR;
                return BatchExportResult.failure("Batch export failed: " + e.getMessage());
            }
        });
    }
    
    // Abstract methods to be implemented by concrete exporters
    protected abstract void initialize();
    protected abstract DocumentProcessor createDocumentProcessor();
    protected abstract ExportResult performCoreExport(ExportRequest request, ProcessedDocument document);
    protected abstract ValidationResult validateFormatSpecificOptions(ExportOptions options);
    protected abstract String getFileExtension();
    protected abstract String getMimeType();
    
    // Concrete methods with default implementations
    protected ValidationResult performPreExportValidation(ExportRequest request) {
        try {
            // Check document
            if (request.getDocument() == null) {
                return ValidationResult.failure("Document is null");
            }
            
            // Check document content
            if (request.getDocument().getContent() == null || request.getDocument().getContent().isEmpty()) {
                return ValidationResult.failure("Document content is empty");
            }
            
            // Validate export options
            ValidationResult optionsValidation = validateExportOptions(request.getExportOptions());
            if (!optionsValidation.isSuccess()) {
                return optionsValidation;
            }
            
            // Format-specific validation
            ValidationResult formatValidation = validateFormatSpecificOptions(request.getExportOptions());
            if (!formatValidation.isSuccess()) {
                return formatValidation;
            }
            
            // Security validation
            ValidationResult securityValidation = securityManager.validateExportPermissions(request);
            if (!securityValidation.isSuccess()) {
                return securityValidation;
            }
            
            return ValidationResult.success("Pre-export validation passed");
            
        } catch (Exception e) {
            return ValidationResult.failure("Pre-export validation error: " + e.getMessage());
        }
    }
    
    protected ProcessingResult preprocessDocument(Document document) {
        try {
            return documentProcessor.processDocument(document);
        } catch (Exception e) {
            return ProcessingResult.failure("Document preprocessing failed: " + e.getMessage());
        }
    }
    
    protected ExportResult performPostProcessing(ExportResult coreResult, ExportRequest request) {
        try {
            // Apply post-processing filters
            if (request.getExportOptions().isApplyWatermark()) {
                coreResult = applyWatermark(coreResult, request.getExportOptions().getWatermarkText());
            }
            
            // Apply metadata
            if (request.getExportOptions().isIncludeMetadata()) {
                coreResult = addMetadata(coreResult, request.getDocument().getMetadata());
            }
            
            // Validate output
            ValidationResult outputValidation = validator.validateExportedContent(coreResult);
            if (!outputValidation.isSuccess()) {
                return ExportResult.failure("Output validation failed: " + outputValidation.getMessage());
            }
            
            return coreResult;
            
        } catch (Exception e) {
            return ExportResult.failure("Post-processing failed: " + e.getMessage());
        }
    }
    
    protected ExportResult applySecurityAndCompression(ExportResult result, ExportRequest request) {
        try {
            ExportResult securedResult = result;
            
            // Apply encryption if requested
            if (request.getExportOptions().isEncryptOutput()) {
                securedResult = securityManager.encryptExport(securedResult, request.getExportOptions().getEncryptionKey());
            }
            
            // Apply compression if requested
            if (request.getExportOptions().isCompressOutput()) {
                securedResult = compressionManager.compressExport(securedResult, request.getExportOptions().getCompressionLevel());
            }
            
            // Add digital signature if requested
            if (request.getExportOptions().isAddDigitalSignature()) {
                securedResult = securityManager.addDigitalSignature(securedResult, request.getExportOptions().getSigningKey());
            }
            
            return securedResult;
            
        } catch (Exception e) {
            return ExportResult.failure("Security/compression processing failed: " + e.getMessage());
        }
    }
    
    protected ValidationResult validateExportOptions(ExportOptions options) {
        if (options == null) {
            return ValidationResult.failure("Export options are null");
        }
        
        // Validate quality settings
        if (options.getQuality() < 0 || options.getQuality() > 100) {
            return ValidationResult.failure("Quality must be between 0 and 100");
        }
        
        // Validate page settings
        if (options.getPageSize() != null && !isValidPageSize(options.getPageSize())) {
            return ValidationResult.failure("Invalid page size: " + options.getPageSize());
        }
        
        // Validate compression level
        if (options.isCompressOutput() && (options.getCompressionLevel() < 1 || options.getCompressionLevel() > 9)) {
            return ValidationResult.failure("Compression level must be between 1 and 9");
        }
        
        return ValidationResult.success("Export options validated");
    }
    
    protected boolean isValidPageSize(String pageSize) {
        return pageSize.matches("A[0-9]|LETTER|LEGAL|TABLOID");
    }
    
    protected ExportResult applyWatermark(ExportResult result, String watermarkText) {
        // Default watermark implementation
        ExportResult watermarkedResult = new ExportResult(result);
        watermarkedResult.addMetadata("watermark", watermarkText);
        watermarkedResult.addMetadata("watermark_applied", LocalDateTime.now().toString());
        return watermarkedResult;
    }
    
    protected ExportResult addMetadata(ExportResult result, Map<String, Object> metadata) {
        ExportResult metadataResult = new ExportResult(result);
        
        if (metadata != null) {
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                metadataResult.addMetadata("doc_" + entry.getKey(), entry.getValue().toString());
            }
        }
        
        metadataResult.addMetadata("export_timestamp", LocalDateTime.now().toString());
        metadataResult.addMetadata("exporter_id", exporterId);
        metadataResult.addMetadata("export_format", exportFormat.toString());
        
        return metadataResult;
    }
    
    // Preview functionality
    public CompletableFuture<PreviewResult> generatePreview(PreviewRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Create preview-specific export options
                ExportOptions previewOptions = createPreviewOptions(request);
                
                // Create export request for preview
                ExportRequest exportRequest = new ExportRequest(
                    "preview_" + System.currentTimeMillis(),
                    request.getDocument(),
                    previewOptions,
                    request.getRequestedBy()
                );
                
                // Generate preview
                ExportResult exportResult = exportDocument(exportRequest).get();
                
                if (exportResult.isSuccess()) {
                    return PreviewResult.success("Preview generated successfully", 
                                               exportResult.getExportedContent(), 
                                               request.getPreviewSize());
                } else {
                    return PreviewResult.failure("Preview generation failed: " + exportResult.getMessage());
                }
                
            } catch (Exception e) {
                return PreviewResult.failure("Preview generation error: " + e.getMessage());
            }
        });
    }
    
    protected ExportOptions createPreviewOptions(PreviewRequest request) {
        ExportOptions options = new ExportOptions();
        options.setQuality(50); // Lower quality for preview
        options.setPageSize(request.getPreviewSize());
        options.setIncludeMetadata(false);
        options.setCompressOutput(false);
        options.setEncryptOutput(false);
        return options;
    }
    
    // Conversion and optimization
    public CompletableFuture<ConversionResult> convertFormat(ConversionRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Validate conversion compatibility
                if (!isConversionSupported(request.getTargetFormat())) {
                    return ConversionResult.failure("Conversion to " + request.getTargetFormat() + " not supported");
                }
                
                // Export to current format first
                ExportRequest exportRequest = new ExportRequest(
                    "convert_" + System.currentTimeMillis(),
                    request.getDocument(),
                    request.getExportOptions(),
                    request.getRequestedBy()
                );
                
                ExportResult exportResult = exportDocument(exportRequest).get();
                
                if (exportResult.isSuccess()) {
                    // Perform format conversion
                    return performFormatConversion(exportResult, request.getTargetFormat());
                } else {
                    return ConversionResult.failure("Export failed during conversion: " + exportResult.getMessage());
                }
                
            } catch (Exception e) {
                return ConversionResult.failure("Format conversion failed: " + e.getMessage());
            }
        });
    }
    
    protected boolean isConversionSupported(ExportFormat targetFormat) {
        // Default implementation - override in concrete classes
        return false;
    }
    
    protected ConversionResult performFormatConversion(ExportResult exportResult, ExportFormat targetFormat) {
        // Default implementation - override in concrete classes
        return ConversionResult.failure("Format conversion not implemented");
    }
    
    // Getters and status methods
    public String getExporterId() { return exporterId; }
    public String getExporterName() { return exporterName; }
    public ExportFormat getExportFormat() { return exportFormat; }
    public ExportState getCurrentState() { return currentState; }
    
    public ExporterStatus getStatus() {
        return new ExporterStatus(
            exporterId,
            exporterName,
            exportFormat,
            currentState,
            getFileExtension(),
            getMimeType(),
            LocalDateTime.now()
        );
    }
    
    public DocumentProcessor getDocumentProcessor() { return documentProcessor; }
    public FormatValidator getValidator() { return validator; }
    public CompressionManager getCompressionManager() { return compressionManager; }
    public SecurityManager getSecurityManager() { return securityManager; }
    
    // Configuration
    public boolean supportsFeature(String feature) {
        return configuration.containsKey(feature) && 
               Boolean.TRUE.equals(configuration.get(feature));
    }
    
    public Object getConfigurationValue(String key) {
        return configuration.get(key);
    }
}

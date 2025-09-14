package abstraction.documentexporter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Data models and enums for the Document Exporter abstraction system
 */

// Export format enumeration
enum ExportFormat {
    PDF, DOCX, HTML, XML, JSON, CSV, XLSX, PPTX, TXT, RTF, MARKDOWN, EPUB
}

// Export state enumeration
enum ExportState {
    INITIALIZED, PROCESSING, BATCH_PROCESSING, COMPLETED, ERROR
}

// Document class
class Document {
    private String documentId;
    private String title;
    private String content;
    private DocumentType documentType;
    private Map<String, Object> metadata;
    private List<DocumentSection> sections;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    
    public Document(String documentId, String title, String content, DocumentType documentType) {
        this.documentId = documentId;
        this.title = title;
        this.content = content;
        this.documentType = documentType;
        this.metadata = new HashMap<>();
        this.sections = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public String getDocumentId() { return documentId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public DocumentType getDocumentType() { return documentType; }
    public Map<String, Object> getMetadata() { return metadata; }
    public List<DocumentSection> getSections() { return sections; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getModifiedAt() { return modifiedAt; }
    
    public void setTitle(String title) { 
        this.title = title; 
        this.modifiedAt = LocalDateTime.now();
    }
    public void setContent(String content) { 
        this.content = content; 
        this.modifiedAt = LocalDateTime.now();
    }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public void setSections(List<DocumentSection> sections) { this.sections = sections; }
    
    public void addMetadata(String key, Object value) {
        this.metadata.put(key, value);
        this.modifiedAt = LocalDateTime.now();
    }
    
    public void addSection(DocumentSection section) {
        this.sections.add(section);
        this.modifiedAt = LocalDateTime.now();
    }
}

// Document type enumeration
enum DocumentType {
    TEXT, REPORT, PRESENTATION, SPREADSHEET, FORM, TEMPLATE, MANUAL, ARTICLE
}

// Document section class
class DocumentSection {
    private String sectionId;
    private String title;
    private String content;
    private int order;
    private SectionType sectionType;
    private Map<String, Object> formatting;
    
    public DocumentSection(String sectionId, String title, String content, int order, SectionType sectionType) {
        this.sectionId = sectionId;
        this.title = title;
        this.content = content;
        this.order = order;
        this.sectionType = sectionType;
        this.formatting = new HashMap<>();
    }
    
    // Getters and setters
    public String getSectionId() { return sectionId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public int getOrder() { return order; }
    public SectionType getSectionType() { return sectionType; }
    public Map<String, Object> getFormatting() { return formatting; }
    
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setOrder(int order) { this.order = order; }
    public void setFormatting(Map<String, Object> formatting) { this.formatting = formatting; }
}

// Section type enumeration
enum SectionType {
    HEADER, PARAGRAPH, LIST, TABLE, IMAGE, CODE, QUOTE, FOOTER
}

// Processed document class
class ProcessedDocument {
    private Document originalDocument;
    private String processedContent;
    private Map<String, Object> processingMetadata;
    private List<ProcessingStep> processingSteps;
    
    public ProcessedDocument(Document originalDocument, String processedContent) {
        this.originalDocument = originalDocument;
        this.processedContent = processedContent;
        this.processingMetadata = new HashMap<>();
        this.processingSteps = new ArrayList<>();
    }
    
    // Getters and setters
    public Document getOriginalDocument() { return originalDocument; }
    public String getProcessedContent() { return processedContent; }
    public Map<String, Object> getProcessingMetadata() { return processingMetadata; }
    public List<ProcessingStep> getProcessingSteps() { return processingSteps; }
    
    public void setProcessedContent(String processedContent) { this.processedContent = processedContent; }
    public void setProcessingMetadata(Map<String, Object> processingMetadata) { this.processingMetadata = processingMetadata; }
    public void addProcessingStep(ProcessingStep step) { this.processingSteps.add(step); }
}

// Processing step class
class ProcessingStep {
    private String stepName;
    private String description;
    private LocalDateTime timestamp;
    private boolean successful;
    
    public ProcessingStep(String stepName, String description, boolean successful) {
        this.stepName = stepName;
        this.description = description;
        this.successful = successful;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getStepName() { return stepName; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isSuccessful() { return successful; }
}

// Export request class
class ExportRequest {
    private String requestId;
    private Document document;
    private ExportOptions exportOptions;
    private String requestedBy;
    private LocalDateTime requestTime;
    
    public ExportRequest(String requestId, Document document, ExportOptions exportOptions, String requestedBy) {
        this.requestId = requestId;
        this.document = document;
        this.exportOptions = exportOptions;
        this.requestedBy = requestedBy;
        this.requestTime = LocalDateTime.now();
    }
    
    // Getters
    public String getRequestId() { return requestId; }
    public Document getDocument() { return document; }
    public ExportOptions getExportOptions() { return exportOptions; }
    public String getRequestedBy() { return requestedBy; }
    public LocalDateTime getRequestTime() { return requestTime; }
}

// Export options class
class ExportOptions {
    private int quality = 85;
    private String pageSize = "A4";
    private boolean includeMetadata = true;
    private boolean compressOutput = false;
    private int compressionLevel = 5;
    private boolean encryptOutput = false;
    private String encryptionKey;
    private boolean addDigitalSignature = false;
    private String signingKey;
    private boolean applyWatermark = false;
    private String watermarkText;
    private Map<String, Object> customOptions;
    
    public ExportOptions() {
        this.customOptions = new HashMap<>();
    }
    
    // Getters and setters
    public int getQuality() { return quality; }
    public String getPageSize() { return pageSize; }
    public boolean isIncludeMetadata() { return includeMetadata; }
    public boolean isCompressOutput() { return compressOutput; }
    public int getCompressionLevel() { return compressionLevel; }
    public boolean isEncryptOutput() { return encryptOutput; }
    public String getEncryptionKey() { return encryptionKey; }
    public boolean isAddDigitalSignature() { return addDigitalSignature; }
    public String getSigningKey() { return signingKey; }
    public boolean isApplyWatermark() { return applyWatermark; }
    public String getWatermarkText() { return watermarkText; }
    public Map<String, Object> getCustomOptions() { return customOptions; }
    
    public void setQuality(int quality) { this.quality = quality; }
    public void setPageSize(String pageSize) { this.pageSize = pageSize; }
    public void setIncludeMetadata(boolean includeMetadata) { this.includeMetadata = includeMetadata; }
    public void setCompressOutput(boolean compressOutput) { this.compressOutput = compressOutput; }
    public void setCompressionLevel(int compressionLevel) { this.compressionLevel = compressionLevel; }
    public void setEncryptOutput(boolean encryptOutput) { this.encryptOutput = encryptOutput; }
    public void setEncryptionKey(String encryptionKey) { this.encryptionKey = encryptionKey; }
    public void setAddDigitalSignature(boolean addDigitalSignature) { this.addDigitalSignature = addDigitalSignature; }
    public void setSigningKey(String signingKey) { this.signingKey = signingKey; }
    public void setApplyWatermark(boolean applyWatermark) { this.applyWatermark = applyWatermark; }
    public void setWatermarkText(String watermarkText) { this.watermarkText = watermarkText; }
    public void setCustomOptions(Map<String, Object> customOptions) { this.customOptions = customOptions; }
    
    public void addCustomOption(String key, Object value) {
        this.customOptions.put(key, value);
    }
}

// Export result class
class ExportResult {
    private boolean success;
    private String message;
    private byte[] exportedContent;
    private String fileName;
    private String mimeType;
    private long fileSize;
    private Map<String, String> metadata;
    private LocalDateTime exportTime;
    
    private ExportResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.metadata = new HashMap<>();
        this.exportTime = LocalDateTime.now();
    }
    
    // Copy constructor
    public ExportResult(ExportResult other) {
        this.success = other.success;
        this.message = other.message;
        this.exportedContent = other.exportedContent;
        this.fileName = other.fileName;
        this.mimeType = other.mimeType;
        this.fileSize = other.fileSize;
        this.metadata = new HashMap<>(other.metadata);
        this.exportTime = other.exportTime;
    }
    
    public static ExportResult success(String message, byte[] content, String fileName, String mimeType) {
        ExportResult result = new ExportResult(true, message);
        result.exportedContent = content;
        result.fileName = fileName;
        result.mimeType = mimeType;
        result.fileSize = content != null ? content.length : 0;
        return result;
    }
    
    public static ExportResult failure(String message) {
        return new ExportResult(false, message);
    }
    
    // Getters and setters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public byte[] getExportedContent() { return exportedContent; }
    public String getFileName() { return fileName; }
    public String getMimeType() { return mimeType; }
    public long getFileSize() { return fileSize; }
    public Map<String, String> getMetadata() { return metadata; }
    public LocalDateTime getExportTime() { return exportTime; }
    
    public void setExportedContent(byte[] exportedContent) { 
        this.exportedContent = exportedContent; 
        this.fileSize = exportedContent != null ? exportedContent.length : 0;
    }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    
    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
    }
}

// Batch export request class
class BatchExportRequest {
    private String batchId;
    private List<Document> documents;
    private ExportOptions exportOptions;
    private String requestedBy;
    private LocalDateTime requestTime;
    
    public BatchExportRequest(String batchId, List<Document> documents, ExportOptions exportOptions, String requestedBy) {
        this.batchId = batchId;
        this.documents = documents;
        this.exportOptions = exportOptions;
        this.requestedBy = requestedBy;
        this.requestTime = LocalDateTime.now();
    }
    
    // Getters
    public String getBatchId() { return batchId; }
    public List<Document> getDocuments() { return documents; }
    public ExportOptions getExportOptions() { return exportOptions; }
    public String getRequestedBy() { return requestedBy; }
    public LocalDateTime getRequestTime() { return requestTime; }
}

// Batch export result class
class BatchExportResult {
    private String batchId;
    private boolean success;
    private String message;
    private Map<String, ExportResult> results;
    private int totalDocuments;
    private int processedDocuments;
    private int successfulExports;
    private int failedExports;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    public BatchExportResult(String batchId) {
        this.batchId = batchId;
        this.results = new HashMap<>();
        this.startTime = LocalDateTime.now();
        this.processedDocuments = 0;
        this.successfulExports = 0;
        this.failedExports = 0;
    }
    
    public static BatchExportResult failure(String message) {
        BatchExportResult result = new BatchExportResult("failed_batch");
        result.success = false;
        result.message = message;
        result.endTime = LocalDateTime.now();
        return result;
    }
    
    public void addResult(String documentId, ExportResult exportResult) {
        results.put(documentId, exportResult);
        if (exportResult.isSuccess()) {
            successfulExports++;
        } else {
            failedExports++;
        }
    }
    
    public void updateProgress() {
        processedDocuments = results.size();
        if (processedDocuments == totalDocuments) {
            success = failedExports == 0;
            message = success ? "Batch export completed successfully" : 
                     "Batch export completed with " + failedExports + " failures";
            endTime = LocalDateTime.now();
        }
    }
    
    // Getters and setters
    public String getBatchId() { return batchId; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Map<String, ExportResult> getResults() { return results; }
    public int getTotalDocuments() { return totalDocuments; }
    public int getProcessedDocuments() { return processedDocuments; }
    public int getSuccessfulExports() { return successfulExports; }
    public int getFailedExports() { return failedExports; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    
    public void setTotalDocuments(int totalDocuments) { this.totalDocuments = totalDocuments; }
    
    public double getProgressPercentage() {
        return totalDocuments > 0 ? (double) processedDocuments / totalDocuments * 100.0 : 0.0;
    }
}

// Preview request class
class PreviewRequest {
    private String requestId;
    private Document document;
    private String previewSize;
    private String requestedBy;
    
    public PreviewRequest(String requestId, Document document, String previewSize, String requestedBy) {
        this.requestId = requestId;
        this.document = document;
        this.previewSize = previewSize;
        this.requestedBy = requestedBy;
    }
    
    // Getters
    public String getRequestId() { return requestId; }
    public Document getDocument() { return document; }
    public String getPreviewSize() { return previewSize; }
    public String getRequestedBy() { return requestedBy; }
}

// Preview result class
class PreviewResult {
    private boolean success;
    private String message;
    private byte[] previewContent;
    private String previewFormat;
    private String previewSize;
    private LocalDateTime generatedAt;
    
    private PreviewResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.generatedAt = LocalDateTime.now();
    }
    
    public static PreviewResult success(String message, byte[] content, String previewSize) {
        PreviewResult result = new PreviewResult(true, message);
        result.previewContent = content;
        result.previewSize = previewSize;
        result.previewFormat = "image/png"; // Default preview format
        return result;
    }
    
    public static PreviewResult failure(String message) {
        return new PreviewResult(false, message);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public byte[] getPreviewContent() { return previewContent; }
    public String getPreviewFormat() { return previewFormat; }
    public String getPreviewSize() { return previewSize; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
}

// Conversion request class
class ConversionRequest {
    private String requestId;
    private Document document;
    private ExportFormat targetFormat;
    private ExportOptions exportOptions;
    private String requestedBy;
    
    public ConversionRequest(String requestId, Document document, ExportFormat targetFormat, 
                           ExportOptions exportOptions, String requestedBy) {
        this.requestId = requestId;
        this.document = document;
        this.targetFormat = targetFormat;
        this.exportOptions = exportOptions;
        this.requestedBy = requestedBy;
    }
    
    // Getters
    public String getRequestId() { return requestId; }
    public Document getDocument() { return document; }
    public ExportFormat getTargetFormat() { return targetFormat; }
    public ExportOptions getExportOptions() { return exportOptions; }
    public String getRequestedBy() { return requestedBy; }
}

// Conversion result class
class ConversionResult {
    private boolean success;
    private String message;
    private ExportResult convertedResult;
    private ExportFormat originalFormat;
    private ExportFormat targetFormat;
    private LocalDateTime conversionTime;
    
    private ConversionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.conversionTime = LocalDateTime.now();
    }
    
    public static ConversionResult success(String message, ExportResult convertedResult, 
                                         ExportFormat originalFormat, ExportFormat targetFormat) {
        ConversionResult result = new ConversionResult(true, message);
        result.convertedResult = convertedResult;
        result.originalFormat = originalFormat;
        result.targetFormat = targetFormat;
        return result;
    }
    
    public static ConversionResult failure(String message) {
        return new ConversionResult(false, message);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ExportResult getConvertedResult() { return convertedResult; }
    public ExportFormat getOriginalFormat() { return originalFormat; }
    public ExportFormat getTargetFormat() { return targetFormat; }
    public LocalDateTime getConversionTime() { return conversionTime; }
}

// Exporter status class
class ExporterStatus {
    private String exporterId;
    private String exporterName;
    private ExportFormat exportFormat;
    private ExportState currentState;
    private String fileExtension;
    private String mimeType;
    private LocalDateTime statusTime;
    
    public ExporterStatus(String exporterId, String exporterName, ExportFormat exportFormat,
                         ExportState currentState, String fileExtension, String mimeType,
                         LocalDateTime statusTime) {
        this.exporterId = exporterId;
        this.exporterName = exporterName;
        this.exportFormat = exportFormat;
        this.currentState = currentState;
        this.fileExtension = fileExtension;
        this.mimeType = mimeType;
        this.statusTime = statusTime;
    }
    
    // Getters
    public String getExporterId() { return exporterId; }
    public String getExporterName() { return exporterName; }
    public ExportFormat getExportFormat() { return exportFormat; }
    public ExportState getCurrentState() { return currentState; }
    public String getFileExtension() { return fileExtension; }
    public String getMimeType() { return mimeType; }
    public LocalDateTime getStatusTime() { return statusTime; }
}

// Result classes for various operations
class ValidationResult {
    private boolean success;
    private String message;
    
    private ValidationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static ValidationResult success(String message) {
        return new ValidationResult(true, message);
    }
    
    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}

class ProcessingResult {
    private boolean success;
    private String message;
    private ProcessedDocument processedDocument;
    
    private ProcessingResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static ProcessingResult success(String message, ProcessedDocument processedDocument) {
        ProcessingResult result = new ProcessingResult(true, message);
        result.processedDocument = processedDocument;
        return result;
    }
    
    public static ProcessingResult failure(String message) {
        return new ProcessingResult(false, message);
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ProcessedDocument getProcessedDocument() { return processedDocument; }
}

// Custom exception for document export operations
class DocumentExportException extends RuntimeException {
    public DocumentExportException(String message) {
        super(message);
    }
    
    public DocumentExportException(String message, Throwable cause) {
        super(message, cause);
    }
}

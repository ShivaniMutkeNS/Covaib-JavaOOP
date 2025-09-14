package abstraction.documentexporter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.security.MessageDigest;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

/**
 * Supporting components for the Document Exporter abstraction system
 */

// Abstract document processor
abstract class DocumentProcessor {
    protected Map<String, Object> processingConfig;
    
    public DocumentProcessor() {
        this.processingConfig = new HashMap<>();
        initializeProcessor();
    }
    
    protected abstract void initializeProcessor();
    public abstract ProcessingResult processDocument(Document document);
    public abstract boolean supportsDocumentType(DocumentType documentType);
    
    protected ProcessingResult validateDocument(Document document) {
        if (document == null) {
            return ProcessingResult.failure("Document is null");
        }
        
        if (document.getContent() == null || document.getContent().trim().isEmpty()) {
            return ProcessingResult.failure("Document content is empty");
        }
        
        if (!supportsDocumentType(document.getDocumentType())) {
            return ProcessingResult.failure("Document type " + document.getDocumentType() + " not supported");
        }
        
        return ProcessingResult.success("Document validation passed", null);
    }
    
    protected String sanitizeContent(String content) {
        if (content == null) return "";
        
        // Remove potentially harmful content
        content = content.replaceAll("<script[^>]*>.*?</script>", "");
        content = content.replaceAll("javascript:", "");
        content = content.replaceAll("on\\w+\\s*=", "");
        
        return content.trim();
    }
    
    protected Map<String, Object> extractContentMetadata(String content) {
        Map<String, Object> metadata = new HashMap<>();
        
        if (content != null) {
            metadata.put("character_count", content.length());
            metadata.put("word_count", content.split("\\s+").length);
            metadata.put("line_count", content.split("\n").length);
            metadata.put("paragraph_count", content.split("\n\\s*\n").length);
        }
        
        return metadata;
    }
}

// Generic document processor implementation
class GenericDocumentProcessor extends DocumentProcessor {
    
    @Override
    protected void initializeProcessor() {
        processingConfig.put("sanitize_content", true);
        processingConfig.put("extract_metadata", true);
        processingConfig.put("normalize_whitespace", true);
        processingConfig.put("remove_empty_sections", true);
    }
    
    @Override
    public ProcessingResult processDocument(Document document) {
        try {
            // Validate document
            ProcessingResult validation = validateDocument(document);
            if (!validation.isSuccess()) {
                return validation;
            }
            
            String processedContent = document.getContent();
            ProcessedDocument processedDoc = new ProcessedDocument(document, processedContent);
            
            // Sanitize content if enabled
            if (Boolean.TRUE.equals(processingConfig.get("sanitize_content"))) {
                processedContent = sanitizeContent(processedContent);
                processedDoc.addProcessingStep(new ProcessingStep("sanitize", "Content sanitized", true));
            }
            
            // Normalize whitespace if enabled
            if (Boolean.TRUE.equals(processingConfig.get("normalize_whitespace"))) {
                processedContent = normalizeWhitespace(processedContent);
                processedDoc.addProcessingStep(new ProcessingStep("normalize_whitespace", "Whitespace normalized", true));
            }
            
            // Process sections
            List<DocumentSection> processedSections = processSections(document.getSections());
            processedDoc.addProcessingStep(new ProcessingStep("process_sections", "Sections processed", true));
            
            // Extract metadata if enabled
            if (Boolean.TRUE.equals(processingConfig.get("extract_metadata"))) {
                Map<String, Object> contentMetadata = extractContentMetadata(processedContent);
                processedDoc.getProcessingMetadata().putAll(contentMetadata);
                processedDoc.addProcessingStep(new ProcessingStep("extract_metadata", "Metadata extracted", true));
            }
            
            processedDoc.setProcessedContent(processedContent);
            
            return ProcessingResult.success("Document processed successfully", processedDoc);
            
        } catch (Exception e) {
            return ProcessingResult.failure("Document processing failed: " + e.getMessage());
        }
    }
    
    @Override
    public boolean supportsDocumentType(DocumentType documentType) {
        // Generic processor supports all document types
        return true;
    }
    
    private String normalizeWhitespace(String content) {
        if (content == null) return "";
        
        // Replace multiple spaces with single space
        content = content.replaceAll("\\s+", " ");
        // Remove leading/trailing whitespace from lines
        content = content.replaceAll("(?m)^\\s+|\\s+$", "");
        // Remove empty lines
        content = content.replaceAll("(?m)^\\s*$\\n", "");
        
        return content.trim();
    }
    
    private List<DocumentSection> processSections(List<DocumentSection> sections) {
        List<DocumentSection> processedSections = new ArrayList<>();
        
        for (DocumentSection section : sections) {
            if (section.getContent() != null && !section.getContent().trim().isEmpty()) {
                String processedContent = sanitizeContent(section.getContent());
                processedContent = normalizeWhitespace(processedContent);
                
                DocumentSection processedSection = new DocumentSection(
                    section.getSectionId(),
                    section.getTitle(),
                    processedContent,
                    section.getOrder(),
                    section.getSectionType()
                );
                processedSection.setFormatting(section.getFormatting());
                processedSections.add(processedSection);
            }
        }
        
        return processedSections;
    }
}

// Format validator class
class FormatValidator {
    private ExportFormat supportedFormat;
    private Map<String, Object> validationRules;
    
    public FormatValidator(ExportFormat supportedFormat) {
        this.supportedFormat = supportedFormat;
        this.validationRules = new HashMap<>();
        initializeValidationRules();
    }
    
    private void initializeValidationRules() {
        switch (supportedFormat) {
            case PDF:
                validationRules.put("max_pages", 10000);
                validationRules.put("max_file_size_mb", 100);
                validationRules.put("supports_encryption", true);
                break;
            case DOCX:
                validationRules.put("max_pages", 5000);
                validationRules.put("max_file_size_mb", 50);
                validationRules.put("supports_track_changes", true);
                break;
            case HTML:
                validationRules.put("max_file_size_mb", 10);
                validationRules.put("validate_html", true);
                break;
            case XML:
                validationRules.put("validate_xml_structure", true);
                validationRules.put("max_file_size_mb", 20);
                break;
            case JSON:
                validationRules.put("validate_json_structure", true);
                validationRules.put("max_file_size_mb", 15);
                break;
            default:
                validationRules.put("max_file_size_mb", 25);
        }
    }
    
    public ValidationResult validateExportedContent(ExportResult exportResult) {
        try {
            if (exportResult == null || !exportResult.isSuccess()) {
                return ValidationResult.failure("Export result is null or failed");
            }
            
            byte[] content = exportResult.getExportedContent();
            if (content == null || content.length == 0) {
                return ValidationResult.failure("Exported content is empty");
            }
            
            // Check file size
            double fileSizeMB = content.length / (1024.0 * 1024.0);
            Integer maxSizeMB = (Integer) validationRules.get("max_file_size_mb");
            if (maxSizeMB != null && fileSizeMB > maxSizeMB) {
                return ValidationResult.failure("File size " + String.format("%.2f", fileSizeMB) + 
                                              "MB exceeds maximum " + maxSizeMB + "MB");
            }
            
            // Format-specific validation
            ValidationResult formatValidation = validateFormatSpecific(content);
            if (!formatValidation.isSuccess()) {
                return formatValidation;
            }
            
            return ValidationResult.success("Content validation passed");
            
        } catch (Exception e) {
            return ValidationResult.failure("Content validation error: " + e.getMessage());
        }
    }
    
    private ValidationResult validateFormatSpecific(byte[] content) {
        try {
            String contentStr = new String(content);
            
            switch (supportedFormat) {
                case HTML:
                    return validateHTML(contentStr);
                case XML:
                    return validateXML(contentStr);
                case JSON:
                    return validateJSON(contentStr);
                default:
                    return ValidationResult.success("No specific validation required");
            }
        } catch (Exception e) {
            return ValidationResult.failure("Format-specific validation failed: " + e.getMessage());
        }
    }
    
    private ValidationResult validateHTML(String content) {
        // Basic HTML validation
        if (!content.trim().startsWith("<") || !content.trim().endsWith(">")) {
            return ValidationResult.failure("Invalid HTML structure");
        }
        
        // Check for balanced tags (simplified)
        long openTags = content.chars().filter(ch -> ch == '<').count();
        long closeTags = content.chars().filter(ch -> ch == '>').count();
        
        if (openTags != closeTags) {
            return ValidationResult.failure("Unbalanced HTML tags");
        }
        
        return ValidationResult.success("HTML validation passed");
    }
    
    private ValidationResult validateXML(String content) {
        // Basic XML validation
        if (!content.trim().startsWith("<")) {
            return ValidationResult.failure("Invalid XML structure");
        }
        
        // Check for XML declaration or root element
        if (!content.contains("<?xml") && !content.matches("\\s*<\\w+.*>.*</\\w+>\\s*")) {
            return ValidationResult.failure("Invalid XML format");
        }
        
        return ValidationResult.success("XML validation passed");
    }
    
    private ValidationResult validateJSON(String content) {
        // Basic JSON validation
        String trimmed = content.trim();
        if (!(trimmed.startsWith("{") && trimmed.endsWith("}")) && 
            !(trimmed.startsWith("[") && trimmed.endsWith("]"))) {
            return ValidationResult.failure("Invalid JSON structure");
        }
        
        return ValidationResult.success("JSON validation passed");
    }
    
    public boolean supportsFeature(String feature) {
        return validationRules.containsKey(feature) && 
               Boolean.TRUE.equals(validationRules.get(feature));
    }
}

// Compression manager class
class CompressionManager {
    private Map<String, Object> compressionConfig;
    
    public CompressionManager() {
        this.compressionConfig = new HashMap<>();
        this.compressionConfig.put("default_level", 6);
        this.compressionConfig.put("max_level", 9);
        this.compressionConfig.put("buffer_size", 8192);
    }
    
    public ExportResult compressExport(ExportResult exportResult, int compressionLevel) {
        try {
            if (exportResult == null || !exportResult.isSuccess()) {
                return ExportResult.failure("Cannot compress failed export result");
            }
            
            // Validate compression level
            if (compressionLevel < 1 || compressionLevel > 9) {
                compressionLevel = (Integer) compressionConfig.get("default_level");
            }
            
            byte[] originalContent = exportResult.getExportedContent();
            if (originalContent == null || originalContent.length == 0) {
                return ExportResult.failure("No content to compress");
            }
            
            // Perform GZIP compression
            byte[] compressedContent = compressData(originalContent, compressionLevel);
            
            // Create compressed result
            ExportResult compressedResult = new ExportResult(exportResult);
            compressedResult.setExportedContent(compressedContent);
            compressedResult.setFileName(exportResult.getFileName() + ".gz");
            compressedResult.addMetadata("compressed", "true");
            compressedResult.addMetadata("compression_level", String.valueOf(compressionLevel));
            compressedResult.addMetadata("original_size", String.valueOf(originalContent.length));
            compressedResult.addMetadata("compressed_size", String.valueOf(compressedContent.length));
            
            double compressionRatio = (double) compressedContent.length / originalContent.length;
            compressedResult.addMetadata("compression_ratio", String.format("%.2f", compressionRatio));
            
            return compressedResult;
            
        } catch (Exception e) {
            return ExportResult.failure("Compression failed: " + e.getMessage());
        }
    }
    
    private byte[] compressData(byte[] data, int level) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (GZIPOutputStream gzipOut = new GZIPOutputStream(baos) {
            {
                // Set compression level (this is a simplified approach)
                def.setLevel(level);
            }
        }) {
            gzipOut.write(data);
        }
        
        return baos.toByteArray();
    }
    
    public byte[] decompressData(byte[] compressedData) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (GZIPInputStream gzipIn = new GZIPInputStream(bais)) {
            byte[] buffer = new byte[(Integer) compressionConfig.get("buffer_size")];
            int len;
            while ((len = gzipIn.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        }
        
        return baos.toByteArray();
    }
    
    public double calculateCompressionRatio(int originalSize, int compressedSize) {
        return originalSize > 0 ? (double) compressedSize / originalSize : 0.0;
    }
}

// Security manager class
class SecurityManager {
    private Map<String, Object> securityConfig;
    
    public SecurityManager() {
        this.securityConfig = new HashMap<>();
        this.securityConfig.put("default_algorithm", "AES");
        this.securityConfig.put("key_length", 256);
        this.securityConfig.put("hash_algorithm", "SHA-256");
    }
    
    public ValidationResult validateExportPermissions(ExportRequest request) {
        try {
            // Check if user has export permissions
            if (request.getRequestedBy() == null || request.getRequestedBy().trim().isEmpty()) {
                return ValidationResult.failure("No user specified for export request");
            }
            
            // Check document access permissions
            Document document = request.getDocument();
            if (document != null && document.getMetadata().containsKey("restricted")) {
                Boolean isRestricted = (Boolean) document.getMetadata().get("restricted");
                if (Boolean.TRUE.equals(isRestricted)) {
                    return ValidationResult.failure("Document access restricted");
                }
            }
            
            // Check export options permissions
            ExportOptions options = request.getExportOptions();
            if (options != null) {
                if (options.isEncryptOutput() && !hasEncryptionPermission(request.getRequestedBy())) {
                    return ValidationResult.failure("User does not have encryption permissions");
                }
                
                if (options.isAddDigitalSignature() && !hasSigningPermission(request.getRequestedBy())) {
                    return ValidationResult.failure("User does not have digital signing permissions");
                }
            }
            
            return ValidationResult.success("Export permissions validated");
            
        } catch (Exception e) {
            return ValidationResult.failure("Permission validation error: " + e.getMessage());
        }
    }
    
    public ExportResult encryptExport(ExportResult exportResult, String encryptionKey) {
        try {
            if (exportResult == null || !exportResult.isSuccess()) {
                return ExportResult.failure("Cannot encrypt failed export result");
            }
            
            if (encryptionKey == null || encryptionKey.length() < 8) {
                return ExportResult.failure("Invalid encryption key");
            }
            
            byte[] originalContent = exportResult.getExportedContent();
            if (originalContent == null) {
                return ExportResult.failure("No content to encrypt");
            }
            
            // Simulate encryption (in real implementation, use proper encryption)
            byte[] encryptedContent = simulateEncryption(originalContent, encryptionKey);
            
            ExportResult encryptedResult = new ExportResult(exportResult);
            encryptedResult.setExportedContent(encryptedContent);
            encryptedResult.addMetadata("encrypted", "true");
            encryptedResult.addMetadata("encryption_algorithm", (String) securityConfig.get("default_algorithm"));
            encryptedResult.addMetadata("encryption_timestamp", LocalDateTime.now().toString());
            
            return encryptedResult;
            
        } catch (Exception e) {
            return ExportResult.failure("Encryption failed: " + e.getMessage());
        }
    }
    
    public ExportResult addDigitalSignature(ExportResult exportResult, String signingKey) {
        try {
            if (exportResult == null || !exportResult.isSuccess()) {
                return ExportResult.failure("Cannot sign failed export result");
            }
            
            if (signingKey == null || signingKey.length() < 8) {
                return ExportResult.failure("Invalid signing key");
            }
            
            byte[] content = exportResult.getExportedContent();
            if (content == null) {
                return ExportResult.failure("No content to sign");
            }
            
            // Generate digital signature (simplified)
            String signature = generateDigitalSignature(content, signingKey);
            
            ExportResult signedResult = new ExportResult(exportResult);
            signedResult.addMetadata("digital_signature", signature);
            signedResult.addMetadata("signature_algorithm", (String) securityConfig.get("hash_algorithm"));
            signedResult.addMetadata("signing_timestamp", LocalDateTime.now().toString());
            signedResult.addMetadata("signed_by", signingKey.substring(0, Math.min(8, signingKey.length())) + "***");
            
            return signedResult;
            
        } catch (Exception e) {
            return ExportResult.failure("Digital signing failed: " + e.getMessage());
        }
    }
    
    private boolean hasEncryptionPermission(String userId) {
        // Simplified permission check
        return userId != null && (userId.contains("admin") || userId.contains("secure"));
    }
    
    private boolean hasSigningPermission(String userId) {
        // Simplified permission check
        return userId != null && (userId.contains("admin") || userId.contains("authorized"));
    }
    
    private byte[] simulateEncryption(byte[] data, String key) throws Exception {
        // Simplified encryption simulation using XOR
        MessageDigest md = MessageDigest.getInstance((String) securityConfig.get("hash_algorithm"));
        byte[] keyBytes = md.digest(key.getBytes());
        
        byte[] encrypted = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            encrypted[i] = (byte) (data[i] ^ keyBytes[i % keyBytes.length]);
        }
        
        return encrypted;
    }
    
    private String generateDigitalSignature(byte[] content, String signingKey) throws Exception {
        MessageDigest md = MessageDigest.getInstance((String) securityConfig.get("hash_algorithm"));
        
        // Combine content and signing key
        byte[] combined = new byte[content.length + signingKey.getBytes().length];
        System.arraycopy(content, 0, combined, 0, content.length);
        System.arraycopy(signingKey.getBytes(), 0, combined, content.length, signingKey.getBytes().length);
        
        byte[] hash = md.digest(combined);
        
        // Convert to hex string
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        
        return sb.toString();
    }
    
    public boolean verifyDigitalSignature(byte[] content, String signature, String signingKey) {
        try {
            String expectedSignature = generateDigitalSignature(content, signingKey);
            return expectedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
}

package abstraction.documentexporter;

import java.time.LocalDateTime;
import java.util.*;

/**
 * JSON Document Exporter - Concrete implementation for JSON format
 * Demonstrates structured data export with JSON-specific features
 */
public class JSONExporter extends DocumentExporter {
    
    private JSONRenderer jsonRenderer;
    private JSONSchemaValidator schemaValidator;
    private JSONFormatter jsonFormatter;
    
    // JSON-specific configuration
    private boolean prettyPrint;
    private boolean includeSchema;
    private boolean validateOutput;
    private String dateFormat;
    private int indentSize;
    
    public JSONExporter(String exporterId, String exporterName, Map<String, Object> configuration) {
        super(exporterId, exporterName, ExportFormat.JSON, configuration);
    }
    
    @Override
    protected void initialize() {
        // Initialize JSON-specific settings
        this.prettyPrint = (Boolean) configuration.getOrDefault("pretty_print", true);
        this.includeSchema = (Boolean) configuration.getOrDefault("include_schema", false);
        this.validateOutput = (Boolean) configuration.getOrDefault("validate_output", true);
        this.dateFormat = (String) configuration.getOrDefault("date_format", "ISO_8601");
        this.indentSize = (Integer) configuration.getOrDefault("indent_size", 2);
        
        this.jsonRenderer = new JSONRenderer();
        this.schemaValidator = new JSONSchemaValidator();
        this.jsonFormatter = new JSONFormatter();
        
        // Configure JSON renderer
        configureJSONRenderer();
    }
    
    @Override
    protected DocumentProcessor createDocumentProcessor() {
        return new JSONDocumentProcessor();
    }
    
    @Override
    protected ExportResult performCoreExport(ExportRequest request, ProcessedDocument document) {
        try {
            // Create JSON document structure
            Map<String, Object> jsonDoc = createJSONDocument(document, request.getExportOptions());
            
            // Render content to JSON
            String jsonContent = renderToJSON(jsonDoc, request.getExportOptions());
            
            // Validate JSON if enabled
            if (validateOutput) {
                ValidationResult validation = validateJSON(jsonContent);
                if (!validation.isSuccess()) {
                    return ExportResult.failure("JSON validation failed: " + validation.getMessage());
                }
            }
            
            // Generate filename
            String fileName = generateFileName(document.getOriginalDocument(), request.getExportOptions());
            
            ExportResult result = ExportResult.success(
                "JSON export completed successfully",
                jsonContent.getBytes(),
                fileName,
                getMimeType()
            );
            
            // Add JSON-specific metadata
            addJSONMetadata(result, jsonDoc, request.getExportOptions());
            
            return result;
            
        } catch (Exception e) {
            return ExportResult.failure("JSON export failed: " + e.getMessage());
        }
    }
    
    @Override
    protected ValidationResult validateFormatSpecificOptions(ExportOptions options) {
        try {
            Map<String, Object> customOptions = options.getCustomOptions();
            
            // Validate date format
            if (customOptions.containsKey("date_format")) {
                String format = (String) customOptions.get("date_format");
                if (!isValidDateFormat(format)) {
                    return ValidationResult.failure("Invalid date format: " + format);
                }
            }
            
            // Validate indent size
            if (customOptions.containsKey("indent_size")) {
                Integer indent = (Integer) customOptions.get("indent_size");
                if (indent != null && (indent < 0 || indent > 8)) {
                    return ValidationResult.failure("Indent size must be between 0 and 8");
                }
            }
            
            // Validate schema URL if provided
            if (customOptions.containsKey("schema_url")) {
                String schemaUrl = (String) customOptions.get("schema_url");
                if (!isValidURL(schemaUrl)) {
                    return ValidationResult.failure("Invalid schema URL: " + schemaUrl);
                }
            }
            
            return ValidationResult.success("JSON options validated");
            
        } catch (Exception e) {
            return ValidationResult.failure("JSON options validation error: " + e.getMessage());
        }
    }
    
    @Override
    protected String getFileExtension() {
        return ".json";
    }
    
    @Override
    protected String getMimeType() {
        return "application/json";
    }
    
    private void configureJSONRenderer() {
        Map<String, Object> rendererConfig = new HashMap<>();
        rendererConfig.put("pretty_print", prettyPrint);
        rendererConfig.put("include_schema", includeSchema);
        rendererConfig.put("date_format", dateFormat);
        rendererConfig.put("indent_size", indentSize);
        rendererConfig.put("escape_unicode", false);
        
        jsonRenderer.configure(rendererConfig);
    }
    
    private Map<String, Object> createJSONDocument(ProcessedDocument document, ExportOptions options) {
        Map<String, Object> jsonDoc = new LinkedHashMap<>();
        
        // Document metadata
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("document_id", document.getOriginalDocument().getDocumentId());
        metadata.put("title", document.getOriginalDocument().getTitle());
        metadata.put("document_type", document.getOriginalDocument().getDocumentType().toString());
        metadata.put("created_at", formatDate(document.getOriginalDocument().getCreatedAt()));
        metadata.put("modified_at", formatDate(document.getOriginalDocument().getModifiedAt()));
        metadata.put("export_timestamp", formatDate(LocalDateTime.now()));
        
        // Add custom metadata
        if (document.getOriginalDocument().getMetadata() != null) {
            metadata.put("custom_metadata", document.getOriginalDocument().getMetadata());
        }
        
        jsonDoc.put("metadata", metadata);
        
        // Document content
        Map<String, Object> content = new LinkedHashMap<>();
        content.put("main_content", document.getProcessedContent());
        
        // Process sections
        if (!document.getOriginalDocument().getSections().isEmpty()) {
            List<Map<String, Object>> sections = new ArrayList<>();
            for (DocumentSection section : document.getOriginalDocument().getSections()) {
                sections.add(processSectionToJSON(section));
            }
            content.put("sections", sections);
        }
        
        jsonDoc.put("content", content);
        
        // Processing information
        if (!document.getProcessingSteps().isEmpty()) {
            List<Map<String, Object>> processingSteps = new ArrayList<>();
            for (ProcessingStep step : document.getProcessingSteps()) {
                Map<String, Object> stepData = new LinkedHashMap<>();
                stepData.put("step_name", step.getStepName());
                stepData.put("description", step.getDescription());
                stepData.put("successful", step.isSuccessful());
                stepData.put("timestamp", formatDate(step.getTimestamp()));
                processingSteps.add(stepData);
            }
            jsonDoc.put("processing_steps", processingSteps);
        }
        
        // Export options
        if (options.getCustomOptions().getOrDefault("include_export_options", false).equals(true)) {
            jsonDoc.put("export_options", serializeExportOptions(options));
        }
        
        // Schema reference if enabled
        if (includeSchema) {
            jsonDoc.put("$schema", getSchemaURL(options));
        }
        
        // Statistics
        Map<String, Object> statistics = generateDocumentStatistics(document);
        jsonDoc.put("statistics", statistics);
        
        return jsonDoc;
    }
    
    private Map<String, Object> processSectionToJSON(DocumentSection section) {
        Map<String, Object> sectionData = new LinkedHashMap<>();
        
        sectionData.put("section_id", section.getSectionId());
        sectionData.put("title", section.getTitle());
        sectionData.put("content", section.getContent());
        sectionData.put("order", section.getOrder());
        sectionData.put("section_type", section.getSectionType().toString());
        
        // Add formatting information
        if (section.getFormatting() != null && !section.getFormatting().isEmpty()) {
            sectionData.put("formatting", section.getFormatting());
        }
        
        // Process content based on section type
        switch (section.getSectionType()) {
            case LIST:
                sectionData.put("parsed_content", parseListContent(section.getContent()));
                break;
            case TABLE:
                sectionData.put("parsed_content", parseTableContent(section.getContent()));
                break;
            case CODE:
                sectionData.put("parsed_content", parseCodeContent(section.getContent()));
                break;
            default:
                // For other types, content is already included as-is
                break;
        }
        
        return sectionData;
    }
    
    private List<String> parseListContent(String content) {
        List<String> items = new ArrayList<>();
        if (content != null) {
            String[] lines = content.split("\n");
            for (String line : lines) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    // Remove bullet points or numbers
                    trimmed = trimmed.replaceAll("^[•\\-\\*\\d+\\.\\)\\s]+", "").trim();
                    if (!trimmed.isEmpty()) {
                        items.add(trimmed);
                    }
                }
            }
        }
        return items;
    }
    
    private List<List<String>> parseTableContent(String content) {
        List<List<String>> table = new ArrayList<>();
        if (content != null) {
            String[] rows = content.split("\n");
            for (String row : rows) {
                if (!row.trim().isEmpty()) {
                    List<String> cells = Arrays.asList(row.split(","));
                    // Trim each cell
                    cells = cells.stream().map(String::trim).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
                    table.add(cells);
                }
            }
        }
        return table;
    }
    
    private Map<String, Object> parseCodeContent(String content) {
        Map<String, Object> codeData = new LinkedHashMap<>();
        codeData.put("raw_code", content);
        
        // Try to detect language (simplified)
        String language = detectCodeLanguage(content);
        if (language != null) {
            codeData.put("detected_language", language);
        }
        
        // Basic code statistics
        if (content != null) {
            codeData.put("line_count", content.split("\n").length);
            codeData.put("character_count", content.length());
        }
        
        return codeData;
    }
    
    private String detectCodeLanguage(String code) {
        if (code == null) return null;
        
        // Simple language detection based on keywords
        if (code.contains("public class") || code.contains("import java")) return "java";
        if (code.contains("def ") || code.contains("import ")) return "python";
        if (code.contains("function ") || code.contains("var ") || code.contains("const ")) return "javascript";
        if (code.contains("#include") || code.contains("int main")) return "c";
        if (code.contains("SELECT") || code.contains("FROM")) return "sql";
        
        return null;
    }
    
    private Map<String, Object> serializeExportOptions(ExportOptions options) {
        Map<String, Object> optionsData = new LinkedHashMap<>();
        
        optionsData.put("quality", options.getQuality());
        optionsData.put("page_size", options.getPageSize());
        optionsData.put("include_metadata", options.isIncludeMetadata());
        optionsData.put("compress_output", options.isCompressOutput());
        optionsData.put("encrypt_output", options.isEncryptOutput());
        optionsData.put("apply_watermark", options.isApplyWatermark());
        
        if (options.getCustomOptions() != null && !options.getCustomOptions().isEmpty()) {
            optionsData.put("custom_options", options.getCustomOptions());
        }
        
        return optionsData;
    }
    
    private Map<String, Object> generateDocumentStatistics(ProcessedDocument document) {
        Map<String, Object> stats = new LinkedHashMap<>();
        
        Document originalDoc = document.getOriginalDocument();
        String content = document.getProcessedContent();
        
        // Content statistics
        if (content != null) {
            stats.put("character_count", content.length());
            stats.put("word_count", content.split("\\s+").length);
            stats.put("line_count", content.split("\n").length);
            stats.put("paragraph_count", content.split("\n\\s*\n").length);
        }
        
        // Section statistics
        stats.put("section_count", originalDoc.getSections().size());
        
        Map<String, Integer> sectionTypeCounts = new HashMap<>();
        for (DocumentSection section : originalDoc.getSections()) {
            String type = section.getSectionType().toString();
            sectionTypeCounts.put(type, sectionTypeCounts.getOrDefault(type, 0) + 1);
        }
        stats.put("section_types", sectionTypeCounts);
        
        // Processing statistics
        stats.put("processing_steps_count", document.getProcessingSteps().size());
        
        long successfulSteps = document.getProcessingSteps().stream()
            .mapToLong(step -> step.isSuccessful() ? 1 : 0)
            .sum();
        stats.put("successful_processing_steps", successfulSteps);
        
        return stats;
    }
    
    private String renderToJSON(Map<String, Object> jsonDoc, ExportOptions options) {
        try {
            JSONRenderingOptions renderOptions = new JSONRenderingOptions();
            renderOptions.setPrettyPrint(prettyPrint);
            renderOptions.setIndentSize(indentSize);
            renderOptions.setDateFormat(dateFormat);
            
            return jsonRenderer.render(jsonDoc, renderOptions);
            
        } catch (Exception e) {
            throw new DocumentExportException("JSON rendering failed: " + e.getMessage(), e);
        }
    }
    
    private ValidationResult validateJSON(String jsonContent) {
        try {
            return schemaValidator.validate(jsonContent);
        } catch (Exception e) {
            return ValidationResult.failure("JSON validation error: " + e.getMessage());
        }
    }
    
    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        
        switch (dateFormat) {
            case "ISO_8601":
                return dateTime.toString();
            case "UNIX_TIMESTAMP":
                return String.valueOf(dateTime.toEpochSecond(java.time.ZoneOffset.UTC));
            case "READABLE":
                return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            default:
                return dateTime.toString();
        }
    }
    
    private String getSchemaURL(ExportOptions options) {
        return options.getCustomOptions().getOrDefault("schema_url", 
            "https://schemas.documentexporter.com/document-v1.0.json").toString();
    }
    
    private String generateFileName(Document document, ExportOptions options) {
        String baseName = document.getTitle() != null ? 
            document.getTitle().replaceAll("[^a-zA-Z0-9]", "_") : 
            "document_" + document.getDocumentId();
        
        String timestamp = LocalDateTime.now().toString().replaceAll("[^0-9]", "").substring(0, 14);
        
        return baseName + "_" + timestamp + getFileExtension();
    }
    
    private void addJSONMetadata(ExportResult result, Map<String, Object> jsonDoc, ExportOptions options) {
        result.addMetadata("json_format", "application/json");
        result.addMetadata("pretty_print", String.valueOf(prettyPrint));
        result.addMetadata("includes_schema", String.valueOf(includeSchema));
        result.addMetadata("date_format", dateFormat);
        result.addMetadata("indent_size", String.valueOf(indentSize));
        result.addMetadata("validated", String.valueOf(validateOutput));
        
        // Add document statistics
        @SuppressWarnings("unchecked")
        Map<String, Object> stats = (Map<String, Object>) jsonDoc.get("statistics");
        if (stats != null) {
            result.addMetadata("character_count", stats.get("character_count").toString());
            result.addMetadata("section_count", stats.get("section_count").toString());
        }
    }
    
    @Override
    protected boolean isConversionSupported(ExportFormat targetFormat) {
        // JSON can be converted to certain formats
        return targetFormat == ExportFormat.XML || 
               targetFormat == ExportFormat.CSV || 
               targetFormat == ExportFormat.TXT;
    }
    
    @Override
    protected ConversionResult performFormatConversion(ExportResult exportResult, ExportFormat targetFormat) {
        try {
            switch (targetFormat) {
                case XML:
                    return convertToXML(exportResult);
                case CSV:
                    return convertToCSV(exportResult);
                case TXT:
                    return convertToText(exportResult);
                default:
                    return ConversionResult.failure("Conversion to " + targetFormat + " not supported");
            }
        } catch (Exception e) {
            return ConversionResult.failure("Conversion failed: " + e.getMessage());
        }
    }
    
    private ConversionResult convertToXML(ExportResult jsonResult) {
        String jsonContent = new String(jsonResult.getExportedContent());
        String xmlContent = jsonRenderer.convertToXML(jsonContent);
        
        ExportResult xmlResult = ExportResult.success(
            "JSON converted to XML",
            xmlContent.getBytes(),
            jsonResult.getFileName().replace(".json", ".xml"),
            "application/xml"
        );
        
        return ConversionResult.success("JSON to XML conversion completed", 
                                      xmlResult, ExportFormat.JSON, ExportFormat.XML);
    }
    
    private ConversionResult convertToCSV(ExportResult jsonResult) {
        String jsonContent = new String(jsonResult.getExportedContent());
        String csvContent = jsonRenderer.convertToCSV(jsonContent);
        
        ExportResult csvResult = ExportResult.success(
            "JSON converted to CSV",
            csvContent.getBytes(),
            jsonResult.getFileName().replace(".json", ".csv"),
            "text/csv"
        );
        
        return ConversionResult.success("JSON to CSV conversion completed", 
                                      csvResult, ExportFormat.JSON, ExportFormat.CSV);
    }
    
    private ConversionResult convertToText(ExportResult jsonResult) {
        String jsonContent = new String(jsonResult.getExportedContent());
        String textContent = jsonRenderer.convertToText(jsonContent);
        
        ExportResult textResult = ExportResult.success(
            "JSON converted to text",
            textContent.getBytes(),
            jsonResult.getFileName().replace(".json", ".txt"),
            "text/plain"
        );
        
        return ConversionResult.success("JSON to text conversion completed", 
                                      textResult, ExportFormat.JSON, ExportFormat.TXT);
    }
    
    private boolean isValidDateFormat(String format) {
        return Arrays.asList("ISO_8601", "UNIX_TIMESTAMP", "READABLE").contains(format);
    }
    
    private boolean isValidURL(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }
    
    // Getters for JSON-specific properties
    public boolean isPrettyPrint() { return prettyPrint; }
    public boolean isIncludeSchema() { return includeSchema; }
    public boolean isValidateOutput() { return validateOutput; }
    public String getDateFormat() { return dateFormat; }
    public int getIndentSize() { return indentSize; }
    public JSONRenderer getJsonRenderer() { return jsonRenderer; }
    public JSONSchemaValidator getSchemaValidator() { return schemaValidator; }
    public JSONFormatter getJsonFormatter() { return jsonFormatter; }
}

// JSON-specific document processor
class JSONDocumentProcessor extends GenericDocumentProcessor {
    
    @Override
    protected void initializeProcessor() {
        super.initializeProcessor();
        processingConfig.put("escape_json", true);
        processingConfig.put("normalize_unicode", true);
        processingConfig.put("validate_structure", true);
        processingConfig.put("extract_structured_data", true);
    }
    
    @Override
    public ProcessingResult processDocument(Document document) {
        // Call parent processing first
        ProcessingResult baseResult = super.processDocument(document);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        
        ProcessedDocument processedDoc = baseResult.getProcessedDocument();
        
        // JSON-specific processing
        if (Boolean.TRUE.equals(processingConfig.get("escape_json"))) {
            escapeJSONContent(processedDoc);
        }
        
        if (Boolean.TRUE.equals(processingConfig.get("normalize_unicode"))) {
            normalizeUnicode(processedDoc);
        }
        
        if (Boolean.TRUE.equals(processingConfig.get("extract_structured_data"))) {
            extractStructuredData(processedDoc);
        }
        
        return ProcessingResult.success("JSON document processing completed", processedDoc);
    }
    
    private void escapeJSONContent(ProcessedDocument document) {
        String content = document.getProcessedContent();
        
        if (content != null) {
            // Escape JSON special characters
            content = content.replace("\\", "\\\\")
                            .replace("\"", "\\\"")
                            .replace("\b", "\\b")
                            .replace("\f", "\\f")
                            .replace("\n", "\\n")
                            .replace("\r", "\\r")
                            .replace("\t", "\\t");
            
            document.setProcessedContent(content);
        }
        
        document.addProcessingStep(new ProcessingStep("json_escape", "JSON characters escaped", true));
    }
    
    private void normalizeUnicode(ProcessedDocument document) {
        String content = document.getProcessedContent();
        
        if (content != null) {
            // Normalize Unicode characters (simplified)
            content = java.text.Normalizer.normalize(content, java.text.Normalizer.Form.NFC);
            document.setProcessedContent(content);
        }
        
        document.addProcessingStep(new ProcessingStep("unicode_normalization", "Unicode normalized", true));
    }
    
    private void extractStructuredData(ProcessedDocument document) {
        // Extract structured data from content for better JSON representation
        Map<String, Object> structuredData = new HashMap<>();
        
        String content = document.getProcessedContent();
        if (content != null) {
            // Extract URLs
            List<String> urls = extractURLs(content);
            if (!urls.isEmpty()) {
                structuredData.put("extracted_urls", urls);
            }
            
            // Extract email addresses
            List<String> emails = extractEmails(content);
            if (!emails.isEmpty()) {
                structuredData.put("extracted_emails", emails);
            }
            
            // Extract dates
            List<String> dates = extractDates(content);
            if (!dates.isEmpty()) {
                structuredData.put("extracted_dates", dates);
            }
        }
        
        if (!structuredData.isEmpty()) {
            document.getProcessingMetadata().put("structured_data", structuredData);
        }
        
        document.addProcessingStep(new ProcessingStep("extract_structured_data", "Structured data extracted", true));
    }
    
    private List<String> extractURLs(String content) {
        List<String> urls = new ArrayList<>();
        // Simple URL extraction regex
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            urls.add(matcher.group());
        }
        
        return urls;
    }
    
    private List<String> extractEmails(String content) {
        List<String> emails = new ArrayList<>();
        // Simple email extraction regex
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            emails.add(matcher.group());
        }
        
        return emails;
    }
    
    private List<String> extractDates(String content) {
        List<String> dates = new ArrayList<>();
        // Simple date extraction regex (YYYY-MM-DD format)
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            dates.add(matcher.group());
        }
        
        return dates;
    }
}

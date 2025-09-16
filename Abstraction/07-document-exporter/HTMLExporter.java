package abstraction.documentexporter;

import java.time.LocalDateTime;
import java.util.*;

/**
 * HTML Document Exporter - Concrete implementation for HTML format
 * Demonstrates web-specific export logic and HTML-specific features
 */
public class HTMLExporter extends DocumentExporter {
    
    private HTMLRenderer htmlRenderer;
    private CSSStyleManager cssStyleManager;
    private JavaScriptManager jsManager;
    private HTMLTemplateEngine templateEngine;
    
    // HTML-specific configuration
    private String htmlVersion;
    private boolean includeCSS;
    private boolean includeJavaScript;
    private boolean responsiveDesign;
    private String cssFramework;
    
    public HTMLExporter(String exporterId, String exporterName, Map<String, Object> configuration) {
        super(exporterId, exporterName, ExportFormat.HTML, configuration);
    }
    
    @Override
    protected void initialize() {
        // Initialize HTML-specific settings
        this.htmlVersion = (String) configuration.getOrDefault("html_version", "HTML5");
        this.includeCSS = (Boolean) configuration.getOrDefault("include_css", true);
        this.includeJavaScript = (Boolean) configuration.getOrDefault("include_javascript", false);
        this.responsiveDesign = (Boolean) configuration.getOrDefault("responsive_design", true);
        this.cssFramework = (String) configuration.getOrDefault("css_framework", "bootstrap");
        
        this.htmlRenderer = new HTMLRenderer();
        this.cssStyleManager = new CSSStyleManager(cssFramework);
        this.jsManager = new JavaScriptManager();
        this.templateEngine = new HTMLTemplateEngine();
        
        // Configure HTML renderer
        configureHTMLRenderer();
    }
    
    @Override
    protected DocumentProcessor createDocumentProcessor() {
        return new HTMLDocumentProcessor();
    }
    
    @Override
    protected ExportResult performCoreExport(ExportRequest request, ProcessedDocument document) {
        try {
            // Create HTML document structure
            HTMLDocument htmlDoc = createHTMLDocument(document, request.getExportOptions());
            
            // Render content to HTML
            String htmlContent = renderToHTML(htmlDoc, request.getExportOptions());
            
            // Generate filename
            String fileName = generateFileName(document.getOriginalDocument(), request.getExportOptions());
            
            ExportResult result = ExportResult.success(
                "HTML export completed successfully",
                htmlContent.getBytes(),
                fileName,
                getMimeType()
            );
            
            // Add HTML-specific metadata
            addHTMLMetadata(result, htmlDoc, request.getExportOptions());
            
            return result;
            
        } catch (Exception e) {
            return ExportResult.failure("HTML export failed: " + e.getMessage());
        }
    }
    
    @Override
    protected ValidationResult validateFormatSpecificOptions(ExportOptions options) {
        try {
            // Validate HTML-specific options
            Map<String, Object> customOptions = options.getCustomOptions();
            
            if (customOptions.containsKey("html_version")) {
                String version = (String) customOptions.get("html_version");
                if (!isValidHTMLVersion(version)) {
                    return ValidationResult.failure("Invalid HTML version: " + version);
                }
            }
            
            if (customOptions.containsKey("css_framework")) {
                String framework = (String) customOptions.get("css_framework");
                if (!isValidCSSFramework(framework)) {
                    return ValidationResult.failure("Invalid CSS framework: " + framework);
                }
            }
            
            if (customOptions.containsKey("viewport_width")) {
                Integer width = (Integer) customOptions.get("viewport_width");
                if (width != null && (width < 320 || width > 3840)) {
                    return ValidationResult.failure("Viewport width must be between 320 and 3840 pixels");
                }
            }
            
            return ValidationResult.success("HTML options validated");
            
        } catch (Exception e) {
            return ValidationResult.failure("HTML options validation error: " + e.getMessage());
        }
    }
    
    @Override
    protected String getFileExtension() {
        return ".html";
    }
    
    @Override
    protected String getMimeType() {
        return "text/html";
    }
    
    private void configureHTMLRenderer() {
        Map<String, Object> rendererConfig = new HashMap<>();
        rendererConfig.put("html_version", htmlVersion);
        rendererConfig.put("include_css", includeCSS);
        rendererConfig.put("include_javascript", includeJavaScript);
        rendererConfig.put("responsive_design", responsiveDesign);
        rendererConfig.put("css_framework", cssFramework);
        rendererConfig.put("minify_output", false);
        
        htmlRenderer.configure(rendererConfig);
    }
    
    private HTMLDocument createHTMLDocument(ProcessedDocument document, ExportOptions options) {
        HTMLDocument htmlDoc = new HTMLDocument();
        
        // Set document properties
        htmlDoc.setTitle(document.getOriginalDocument().getTitle());
        htmlDoc.setLanguage(options.getCustomOptions().getOrDefault("language", "en").toString());
        htmlDoc.setCharset("UTF-8");
        htmlDoc.setViewport("width=device-width, initial-scale=1.0");
        
        // Add meta tags
        addMetaTags(htmlDoc, document, options);
        
        // Add CSS if enabled
        if (includeCSS) {
            addCSS(htmlDoc, options);
        }
        
        // Add JavaScript if enabled
        if (includeJavaScript) {
            addJavaScript(htmlDoc, options);
        }
        
        // Process document content
        processDocumentContent(htmlDoc, document, options);
        
        return htmlDoc;
    }
    
    private void addMetaTags(HTMLDocument htmlDoc, ProcessedDocument document, ExportOptions options) {
        // Standard meta tags
        htmlDoc.addMetaTag("generator", "Document Exporter HTML v1.0");
        htmlDoc.addMetaTag("created", LocalDateTime.now().toString());
        
        // SEO meta tags
        if (options.getCustomOptions().containsKey("description")) {
            htmlDoc.addMetaTag("description", options.getCustomOptions().get("description").toString());
        }
        
        if (options.getCustomOptions().containsKey("keywords")) {
            htmlDoc.addMetaTag("keywords", options.getCustomOptions().get("keywords").toString());
        }
        
        if (options.getCustomOptions().containsKey("author")) {
            htmlDoc.addMetaTag("author", options.getCustomOptions().get("author").toString());
        }
        
        // Responsive design meta tag
        if (responsiveDesign) {
            htmlDoc.addMetaTag("viewport", "width=device-width, initial-scale=1.0");
        }
        
        // Open Graph meta tags for social sharing
        if (options.getCustomOptions().getOrDefault("include_og_tags", false).equals(true)) {
            htmlDoc.addMetaTag("og:title", document.getOriginalDocument().getTitle());
            htmlDoc.addMetaTag("og:type", "article");
            htmlDoc.addMetaTag("og:url", options.getCustomOptions().getOrDefault("canonical_url", "").toString());
        }
    }
    
    private void addCSS(HTMLDocument htmlDoc, ExportOptions options) {
        // Add CSS framework if specified
        if (cssFramework != null && !cssFramework.isEmpty()) {
            String frameworkCSS = cssStyleManager.getFrameworkCSS(cssFramework);
            htmlDoc.addCSS(frameworkCSS, true); // External CSS
        }
        
        // Add custom CSS
        String customCSS = generateCustomCSS(options);
        htmlDoc.addCSS(customCSS, false); // Inline CSS
        
        // Add responsive CSS if enabled
        if (responsiveDesign) {
            String responsiveCSS = cssStyleManager.getResponsiveCSS();
            htmlDoc.addCSS(responsiveCSS, false);
        }
    }
    
    private void addJavaScript(HTMLDocument htmlDoc, ExportOptions options) {
        // Add framework JavaScript if needed
        if (cssFramework.equals("bootstrap")) {
            htmlDoc.addJavaScript(jsManager.getBootstrapJS(), true); // External JS
        }
        
        // Add custom JavaScript
        if (options.getCustomOptions().containsKey("custom_js")) {
            String customJS = options.getCustomOptions().get("custom_js").toString();
            htmlDoc.addJavaScript(customJS, false); // Inline JS
        }
        
        // Add interactive features JavaScript
        if (options.getCustomOptions().getOrDefault("interactive_features", false).equals(true)) {
            String interactiveJS = jsManager.getInteractiveJS();
            htmlDoc.addJavaScript(interactiveJS, false);
        }
    }
    
    private String generateCustomCSS(ExportOptions options) {
        StringBuilder css = new StringBuilder();
        
        // Basic styling
        css.append("body { font-family: Arial, sans-serif; line-height: 1.6; margin: 0; padding: 20px; }\n");
        css.append("h1, h2, h3, h4, h5, h6 { color: #333; margin-top: 1.5em; }\n");
        css.append("p { margin-bottom: 1em; }\n");
        css.append("table { border-collapse: collapse; width: 100%; margin: 1em 0; }\n");
        css.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        css.append("th { background-color: #f2f2f2; }\n");
        css.append("code { background-color: #f4f4f4; padding: 2px 4px; border-radius: 3px; }\n");
        css.append("blockquote { border-left: 4px solid #ccc; margin: 1em 0; padding-left: 1em; font-style: italic; }\n");
        
        // Custom color scheme if specified
        if (options.getCustomOptions().containsKey("color_scheme")) {
            String colorScheme = options.getCustomOptions().get("color_scheme").toString();
            css.append(cssStyleManager.getColorSchemeCSS(colorScheme));
        }
        
        // Print styles
        css.append("@media print {\n");
        css.append("  body { font-size: 12pt; }\n");
        css.append("  .no-print { display: none; }\n");
        css.append("}\n");
        
        return css.toString();
    }
    
    private void processDocumentContent(HTMLDocument htmlDoc, ProcessedDocument document, ExportOptions options) {
        HTMLBodyBuilder bodyBuilder = new HTMLBodyBuilder();
        
        // Add document title
        if (document.getOriginalDocument().getTitle() != null) {
            bodyBuilder.addHeading(1, document.getOriginalDocument().getTitle());
        }
        
        // Add table of contents if requested
        if (options.getCustomOptions().getOrDefault("include_toc", false).equals(true)) {
            String toc = generateTableOfContents(document);
            bodyBuilder.addRawHTML(toc);
        }
        
        // Add main content
        String content = document.getProcessedContent();
        if (content != null && !content.isEmpty()) {
            bodyBuilder.addParagraph(content);
        }
        
        // Process sections
        for (DocumentSection section : document.getOriginalDocument().getSections()) {
            processSection(bodyBuilder, section, options);
        }
        
        // Add footer if specified
        if (options.getCustomOptions().containsKey("footer_text")) {
            bodyBuilder.addFooter(options.getCustomOptions().get("footer_text").toString());
        }
        
        htmlDoc.setBody(bodyBuilder.build());
    }
    
    private void processSection(HTMLBodyBuilder bodyBuilder, DocumentSection section, ExportOptions options) {
        // Add section title
        if (section.getTitle() != null && !section.getTitle().isEmpty()) {
            int headingLevel = Math.min(6, Math.max(1, section.getOrder() + 1));
            bodyBuilder.addHeading(headingLevel, section.getTitle());
        }
        
        // Add section content based on type
        switch (section.getSectionType()) {
            case HEADER:
                processHeaderSection(bodyBuilder, section);
                break;
            case PARAGRAPH:
                processParagraphSection(bodyBuilder, section);
                break;
            case LIST:
                processListSection(bodyBuilder, section);
                break;
            case TABLE:
                processTableSection(bodyBuilder, section);
                break;
            case IMAGE:
                processImageSection(bodyBuilder, section);
                break;
            case CODE:
                processCodeSection(bodyBuilder, section);
                break;
            case QUOTE:
                processQuoteSection(bodyBuilder, section);
                break;
            default:
                processParagraphSection(bodyBuilder, section);
        }
    }
    
    private void processHeaderSection(HTMLBodyBuilder bodyBuilder, DocumentSection section) {
        bodyBuilder.addHeading(2, section.getContent());
    }
    
    private void processParagraphSection(HTMLBodyBuilder bodyBuilder, DocumentSection section) {
        bodyBuilder.addParagraph(section.getContent());
    }
    
    private void processListSection(HTMLBodyBuilder bodyBuilder, DocumentSection section) {
        String[] items = section.getContent().split("\n");
        List<String> listItems = new ArrayList<>();
        
        for (String item : items) {
            if (!item.trim().isEmpty()) {
                listItems.add(item.trim());
            }
        }
        
        bodyBuilder.addUnorderedList(listItems);
    }
    
    private void processTableSection(HTMLBodyBuilder bodyBuilder, DocumentSection section) {
        // Parse table content (simplified - assumes CSV-like format)
        String[] rows = section.getContent().split("\n");
        List<List<String>> tableData = new ArrayList<>();
        
        for (String row : rows) {
            if (!row.trim().isEmpty()) {
                List<String> cells = Arrays.asList(row.split(","));
                tableData.add(cells);
            }
        }
        
        if (!tableData.isEmpty()) {
            bodyBuilder.addTable(tableData, true); // First row as header
        }
    }
    
    private void processImageSection(HTMLBodyBuilder bodyBuilder, DocumentSection section) {
        // Process image reference
        String imageUrl = section.getContent().trim();
        String altText = section.getTitle() != null ? section.getTitle() : "Image";
        bodyBuilder.addImage(imageUrl, altText);
    }
    
    private void processCodeSection(HTMLBodyBuilder bodyBuilder, DocumentSection section) {
        bodyBuilder.addCodeBlock(section.getContent());
    }
    
    private void processQuoteSection(HTMLBodyBuilder bodyBuilder, DocumentSection section) {
        bodyBuilder.addBlockquote(section.getContent());
    }
    
    private String generateTableOfContents(ProcessedDocument document) {
        StringBuilder toc = new StringBuilder();
        toc.append("<div class=\"table-of-contents\">\n");
        toc.append("<h2>Table of Contents</h2>\n");
        toc.append("<ul>\n");
        
        for (DocumentSection section : document.getOriginalDocument().getSections()) {
            if (section.getTitle() != null && !section.getTitle().isEmpty()) {
                String anchor = section.getTitle().toLowerCase().replaceAll("[^a-z0-9]", "-");
                toc.append("<li><a href=\"#").append(anchor).append("\">")
                   .append(section.getTitle()).append("</a></li>\n");
            }
        }
        
        toc.append("</ul>\n");
        toc.append("</div>\n");
        
        return toc.toString();
    }
    
    private String renderToHTML(HTMLDocument htmlDoc, ExportOptions options) {
        try {
            // Configure rendering options
            HTMLRenderingOptions renderOptions = new HTMLRenderingOptions();
            renderOptions.setMinify(options.getCustomOptions().getOrDefault("minify", false).equals(true));
            renderOptions.setIndentSize(4);
            renderOptions.setPrettyPrint(true);
            
            // Render HTML
            return htmlRenderer.render(htmlDoc, renderOptions);
            
        } catch (Exception e) {
            throw new DocumentExportException("HTML rendering failed: " + e.getMessage(), e);
        }
    }
    
    private String generateFileName(Document document, ExportOptions options) {
        String baseName = document.getTitle() != null ? 
            document.getTitle().replaceAll("[^a-zA-Z0-9]", "_") : 
            "document_" + document.getDocumentId();
        
        String timestamp = LocalDateTime.now().toString().replaceAll("[^0-9]", "").substring(0, 14);
        
        return baseName + "_" + timestamp + getFileExtension();
    }
    
    private void addHTMLMetadata(ExportResult result, HTMLDocument htmlDoc, ExportOptions options) {
        result.addMetadata("html_version", htmlVersion);
        result.addMetadata("includes_css", String.valueOf(includeCSS));
        result.addMetadata("includes_javascript", String.valueOf(includeJavaScript));
        result.addMetadata("responsive_design", String.valueOf(responsiveDesign));
        result.addMetadata("css_framework", cssFramework);
        result.addMetadata("character_encoding", htmlDoc.getCharset());
        result.addMetadata("language", htmlDoc.getLanguage());
        
        if (htmlDoc.getMetaTags().containsKey("description")) {
            result.addMetadata("meta_description", htmlDoc.getMetaTags().get("description"));
        }
    }
    
    @Override
    protected boolean isConversionSupported(ExportFormat targetFormat) {
        // HTML can be converted to certain formats
        return targetFormat == ExportFormat.PDF || 
               targetFormat == ExportFormat.TXT || 
               targetFormat == ExportFormat.DOCX;
    }
    
    @Override
    protected ConversionResult performFormatConversion(ExportResult exportResult, ExportFormat targetFormat) {
        try {
            switch (targetFormat) {
                case PDF:
                    return convertToPDF(exportResult);
                case TXT:
                    return convertToText(exportResult);
                case DOCX:
                    return convertToDocx(exportResult);
                default:
                    return ConversionResult.failure("Conversion to " + targetFormat + " not supported");
            }
        } catch (Exception e) {
            return ConversionResult.failure("Conversion failed: " + e.getMessage());
        }
    }
    
    private ConversionResult convertToPDF(ExportResult htmlResult) {
        // Simplified HTML to PDF conversion
        String htmlContent = new String(htmlResult.getExportedContent());
        byte[] pdfContent = htmlRenderer.convertToPDF(htmlContent);
        
        ExportResult pdfResult = ExportResult.success(
            "HTML converted to PDF",
            pdfContent,
            htmlResult.getFileName().replace(".html", ".pdf"),
            "application/pdf"
        );
        
        return ConversionResult.success("HTML to PDF conversion completed", 
                                      pdfResult, ExportFormat.HTML, ExportFormat.PDF);
    }
    
    private ConversionResult convertToText(ExportResult htmlResult) {
        // Simplified HTML to text conversion
        String htmlContent = new String(htmlResult.getExportedContent());
        String textContent = htmlRenderer.stripHTML(htmlContent);
        
        ExportResult textResult = ExportResult.success(
            "HTML converted to text",
            textContent.getBytes(),
            htmlResult.getFileName().replace(".html", ".txt"),
            "text/plain"
        );
        
        return ConversionResult.success("HTML to text conversion completed", 
                                      textResult, ExportFormat.HTML, ExportFormat.TXT);
    }
    
    private ConversionResult convertToDocx(ExportResult htmlResult) {
        // Simplified HTML to DOCX conversion
        String htmlContent = new String(htmlResult.getExportedContent());
        byte[] docxContent = htmlRenderer.convertToDocx(htmlContent);
        
        ExportResult docxResult = ExportResult.success(
            "HTML converted to DOCX",
            docxContent,
            htmlResult.getFileName().replace(".html", ".docx"),
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        );
        
        return ConversionResult.success("HTML to DOCX conversion completed", 
                                      docxResult, ExportFormat.HTML, ExportFormat.DOCX);
    }
    
    private boolean isValidHTMLVersion(String version) {
        return Arrays.asList("HTML4", "XHTML", "HTML5").contains(version);
    }
    
    private boolean isValidCSSFramework(String framework) {
        return Arrays.asList("bootstrap", "foundation", "bulma", "tailwind", "none").contains(framework);
    }
    
    // Getters for HTML-specific properties
    public String getHtmlVersion() { return htmlVersion; }
    public boolean isIncludeCSS() { return includeCSS; }
    public boolean isIncludeJavaScript() { return includeJavaScript; }
    public boolean isResponsiveDesign() { return responsiveDesign; }
    public String getCssFramework() { return cssFramework; }
    public HTMLRenderer getHtmlRenderer() { return htmlRenderer; }
    public CSSStyleManager getCssStyleManager() { return cssStyleManager; }
    public JavaScriptManager getJsManager() { return jsManager; }
    public HTMLTemplateEngine getTemplateEngine() { return templateEngine; }
}

// HTML-specific document processor
class HTMLDocumentProcessor extends GenericDocumentProcessor {
    
    @Override
    protected void initializeProcessor() {
        super.initializeProcessor();
        processingConfig.put("escape_html", true);
        processingConfig.put("convert_markdown", false);
        processingConfig.put("optimize_for_web", true);
        processingConfig.put("generate_anchors", true);
    }
    
    @Override
    public ProcessingResult processDocument(Document document) {
        // Call parent processing first
        ProcessingResult baseResult = super.processDocument(document);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        
        ProcessedDocument processedDoc = baseResult.getProcessedDocument();
        
        // HTML-specific processing
        if (Boolean.TRUE.equals(processingConfig.get("escape_html"))) {
            escapeHTMLContent(processedDoc);
        }
        
        if (Boolean.TRUE.equals(processingConfig.get("optimize_for_web"))) {
            optimizeForWeb(processedDoc);
        }
        
        if (Boolean.TRUE.equals(processingConfig.get("generate_anchors"))) {
            generateAnchors(processedDoc);
        }
        
        return ProcessingResult.success("HTML document processing completed", processedDoc);
    }
    
    private void escapeHTMLContent(ProcessedDocument document) {
        String content = document.getProcessedContent();
        
        // Escape HTML special characters
        content = content.replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;")
                        .replace("\"", "&quot;")
                        .replace("'", "&#39;");
        
        document.setProcessedContent(content);
        document.addProcessingStep(new ProcessingStep("html_escape", "HTML characters escaped", true));
    }
    
    private void optimizeForWeb(ProcessedDocument document) {
        String content = document.getProcessedContent();
        
        // Optimize line breaks for web display
        content = content.replaceAll("\n\n+", "\n\n"); // Normalize multiple line breaks
        content = content.replaceAll("\n", "<br>\n"); // Convert line breaks to HTML
        
        document.setProcessedContent(content);
        document.addProcessingStep(new ProcessingStep("web_optimization", "Content optimized for web", true));
    }
    
    private void generateAnchors(ProcessedDocument document) {
        // Generate anchor IDs for sections
        for (DocumentSection section : document.getOriginalDocument().getSections()) {
            if (section.getTitle() != null && !section.getTitle().isEmpty()) {
                String anchor = section.getTitle().toLowerCase().replaceAll("[^a-z0-9]", "-");
                section.getFormatting().put("anchor_id", anchor);
            }
        }
        
        document.addProcessingStep(new ProcessingStep("generate_anchors", "Section anchors generated", true));
    }
}

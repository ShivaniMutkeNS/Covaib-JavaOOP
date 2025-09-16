package abstraction.documentexporter;

import java.util.*;

/**
 * HTML-specific components and helper classes for HTML document export
 */

// HTML Document class
class HTMLDocument {
    private String title;
    private String language;
    private String charset;
    private String viewport;
    private Map<String, String> metaTags;
    private List<CSSResource> cssResources;
    private List<JavaScriptResource> jsResources;
    private String body;
    
    public HTMLDocument() {
        this.metaTags = new HashMap<>();
        this.cssResources = new ArrayList<>();
        this.jsResources = new ArrayList<>();
        this.charset = "UTF-8";
        this.language = "en";
    }
    
    public void addMetaTag(String name, String content) {
        metaTags.put(name, content);
    }
    
    public void addCSS(String css, boolean external) {
        cssResources.add(new CSSResource(css, external));
    }
    
    public void addJavaScript(String js, boolean external) {
        jsResources.add(new JavaScriptResource(js, external));
    }
    
    // Getters and setters
    public String getTitle() { return title; }
    public String getLanguage() { return language; }
    public String getCharset() { return charset; }
    public String getViewport() { return viewport; }
    public Map<String, String> getMetaTags() { return metaTags; }
    public List<CSSResource> getCssResources() { return cssResources; }
    public List<JavaScriptResource> getJsResources() { return jsResources; }
    public String getBody() { return body; }
    
    public void setTitle(String title) { this.title = title; }
    public void setLanguage(String language) { this.language = language; }
    public void setCharset(String charset) { this.charset = charset; }
    public void setViewport(String viewport) { this.viewport = viewport; }
    public void setBody(String body) { this.body = body; }
}

// CSS Resource class
class CSSResource {
    private String content;
    private boolean external;
    private String href;
    
    public CSSResource(String content, boolean external) {
        this.content = content;
        this.external = external;
        if (external) {
            this.href = content; // For external, content is the URL
        }
    }
    
    // Getters
    public String getContent() { return content; }
    public boolean isExternal() { return external; }
    public String getHref() { return href; }
}

// JavaScript Resource class
class JavaScriptResource {
    private String content;
    private boolean external;
    private String src;
    
    public JavaScriptResource(String content, boolean external) {
        this.content = content;
        this.external = external;
        if (external) {
            this.src = content; // For external, content is the URL
        }
    }
    
    // Getters
    public String getContent() { return content; }
    public boolean isExternal() { return external; }
    public String getSrc() { return src; }
}

// HTML Body Builder
class HTMLBodyBuilder {
    private StringBuilder html;
    private int indentLevel;
    
    public HTMLBodyBuilder() {
        this.html = new StringBuilder();
        this.indentLevel = 0;
    }
    
    public HTMLBodyBuilder addHeading(int level, String text) {
        level = Math.max(1, Math.min(6, level));
        String anchor = text.toLowerCase().replaceAll("[^a-z0-9]", "-");
        
        indent();
        html.append("<h").append(level).append(" id=\"").append(anchor).append("\">")
            .append(escapeHtml(text)).append("</h").append(level).append(">\n");
        return this;
    }
    
    public HTMLBodyBuilder addParagraph(String text) {
        indent();
        html.append("<p>").append(escapeHtml(text)).append("</p>\n");
        return this;
    }
    
    public HTMLBodyBuilder addUnorderedList(List<String> items) {
        indent();
        html.append("<ul>\n");
        indentLevel++;
        
        for (String item : items) {
            indent();
            html.append("<li>").append(escapeHtml(item)).append("</li>\n");
        }
        
        indentLevel--;
        indent();
        html.append("</ul>\n");
        return this;
    }
    
    public HTMLBodyBuilder addOrderedList(List<String> items) {
        indent();
        html.append("<ol>\n");
        indentLevel++;
        
        for (String item : items) {
            indent();
            html.append("<li>").append(escapeHtml(item)).append("</li>\n");
        }
        
        indentLevel--;
        indent();
        html.append("</ol>\n");
        return this;
    }
    
    public HTMLBodyBuilder addTable(List<List<String>> tableData, boolean firstRowHeader) {
        if (tableData.isEmpty()) return this;
        
        indent();
        html.append("<table>\n");
        indentLevel++;
        
        // Add header if first row is header
        if (firstRowHeader && !tableData.isEmpty()) {
            List<String> headerRow = tableData.get(0);
            indent();
            html.append("<thead>\n");
            indentLevel++;
            indent();
            html.append("<tr>\n");
            indentLevel++;
            
            for (String cell : headerRow) {
                indent();
                html.append("<th>").append(escapeHtml(cell.trim())).append("</th>\n");
            }
            
            indentLevel--;
            indent();
            html.append("</tr>\n");
            indentLevel--;
            indent();
            html.append("</thead>\n");
        }
        
        // Add body rows
        indent();
        html.append("<tbody>\n");
        indentLevel++;
        
        int startIndex = firstRowHeader ? 1 : 0;
        for (int i = startIndex; i < tableData.size(); i++) {
            List<String> row = tableData.get(i);
            indent();
            html.append("<tr>\n");
            indentLevel++;
            
            for (String cell : row) {
                indent();
                html.append("<td>").append(escapeHtml(cell.trim())).append("</td>\n");
            }
            
            indentLevel--;
            indent();
            html.append("</tr>\n");
        }
        
        indentLevel--;
        indent();
        html.append("</tbody>\n");
        indentLevel--;
        indent();
        html.append("</table>\n");
        
        return this;
    }
    
    public HTMLBodyBuilder addImage(String src, String alt) {
        indent();
        html.append("<img src=\"").append(escapeHtml(src))
            .append("\" alt=\"").append(escapeHtml(alt))
            .append("\" class=\"img-responsive\">\n");
        return this;
    }
    
    public HTMLBodyBuilder addCodeBlock(String code) {
        indent();
        html.append("<pre><code>").append(escapeHtml(code)).append("</code></pre>\n");
        return this;
    }
    
    public HTMLBodyBuilder addBlockquote(String quote) {
        indent();
        html.append("<blockquote>").append(escapeHtml(quote)).append("</blockquote>\n");
        return this;
    }
    
    public HTMLBodyBuilder addFooter(String footerText) {
        indent();
        html.append("<footer class=\"document-footer\">\n");
        indentLevel++;
        indent();
        html.append("<p>").append(escapeHtml(footerText)).append("</p>\n");
        indentLevel--;
        indent();
        html.append("</footer>\n");
        return this;
    }
    
    public HTMLBodyBuilder addRawHTML(String rawHtml) {
        html.append(rawHtml);
        return this;
    }
    
    public HTMLBodyBuilder addDiv(String cssClass, String content) {
        indent();
        html.append("<div class=\"").append(escapeHtml(cssClass)).append("\">\n");
        indentLevel++;
        indent();
        html.append(escapeHtml(content)).append("\n");
        indentLevel--;
        indent();
        html.append("</div>\n");
        return this;
    }
    
    private void indent() {
        for (int i = 0; i < indentLevel; i++) {
            html.append("    ");
        }
    }
    
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
    
    public String build() {
        return html.toString();
    }
}

// CSS Style Manager
class CSSStyleManager {
    private String framework;
    private Map<String, String> frameworkCSS;
    private Map<String, String> colorSchemes;
    
    public CSSStyleManager(String framework) {
        this.framework = framework;
        this.frameworkCSS = new HashMap<>();
        this.colorSchemes = new HashMap<>();
        initializeFrameworks();
        initializeColorSchemes();
    }
    
    private void initializeFrameworks() {
        // Bootstrap CSS CDN
        frameworkCSS.put("bootstrap", "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css");
        
        // Foundation CSS CDN
        frameworkCSS.put("foundation", "https://cdn.jsdelivr.net/npm/foundation-sites@6.7.4/dist/css/foundation.min.css");
        
        // Bulma CSS CDN
        frameworkCSS.put("bulma", "https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css");
        
        // Tailwind CSS CDN
        frameworkCSS.put("tailwind", "https://cdn.tailwindcss.com");
    }
    
    private void initializeColorSchemes() {
        // Light color scheme
        colorSchemes.put("light", 
            "body { background-color: #ffffff; color: #333333; }\n" +
            "h1, h2, h3, h4, h5, h6 { color: #2c3e50; }\n" +
            "a { color: #3498db; }\n" +
            "a:hover { color: #2980b9; }\n"
        );
        
        // Dark color scheme
        colorSchemes.put("dark", 
            "body { background-color: #2c3e50; color: #ecf0f1; }\n" +
            "h1, h2, h3, h4, h5, h6 { color: #3498db; }\n" +
            "a { color: #e74c3c; }\n" +
            "a:hover { color: #c0392b; }\n" +
            "table { background-color: #34495e; }\n" +
            "th { background-color: #2c3e50; }\n"
        );
        
        // Blue color scheme
        colorSchemes.put("blue", 
            "body { background-color: #f8f9fa; color: #495057; }\n" +
            "h1, h2, h3, h4, h5, h6 { color: #007bff; }\n" +
            "a { color: #0056b3; }\n" +
            "a:hover { color: #004085; }\n"
        );
    }
    
    public String getFrameworkCSS(String frameworkName) {
        return frameworkCSS.getOrDefault(frameworkName, "");
    }
    
    public String getColorSchemeCSS(String schemeName) {
        return colorSchemes.getOrDefault(schemeName, "");
    }
    
    public String getResponsiveCSS() {
        return 
            "@media (max-width: 768px) {\n" +
            "  body { padding: 10px; font-size: 14px; }\n" +
            "  table { font-size: 12px; }\n" +
            "  h1 { font-size: 24px; }\n" +
            "  h2 { font-size: 20px; }\n" +
            "}\n" +
            "@media (max-width: 480px) {\n" +
            "  body { padding: 5px; font-size: 12px; }\n" +
            "  h1 { font-size: 20px; }\n" +
            "  h2 { font-size: 18px; }\n" +
            "  table, thead, tbody, th, td, tr { display: block; }\n" +
            "  th { display: none; }\n" +
            "}\n";
    }
}

// JavaScript Manager
class JavaScriptManager {
    private Map<String, String> frameworkJS;
    
    public JavaScriptManager() {
        this.frameworkJS = new HashMap<>();
        initializeFrameworks();
    }
    
    private void initializeFrameworks() {
        // Bootstrap JS CDN
        frameworkJS.put("bootstrap", "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js");
        
        // jQuery CDN
        frameworkJS.put("jquery", "https://code.jquery.com/jquery-3.6.0.min.js");
        
        // Foundation JS CDN
        frameworkJS.put("foundation", "https://cdn.jsdelivr.net/npm/foundation-sites@6.7.4/dist/js/foundation.min.js");
    }
    
    public String getBootstrapJS() {
        return frameworkJS.get("bootstrap");
    }
    
    public String getjQueryJS() {
        return frameworkJS.get("jquery");
    }
    
    public String getInteractiveJS() {
        return 
            "// Interactive features\n" +
            "document.addEventListener('DOMContentLoaded', function() {\n" +
            "    // Smooth scrolling for anchor links\n" +
            "    document.querySelectorAll('a[href^=\"#\"]').forEach(anchor => {\n" +
            "        anchor.addEventListener('click', function (e) {\n" +
            "            e.preventDefault();\n" +
            "            const target = document.querySelector(this.getAttribute('href'));\n" +
            "            if (target) {\n" +
            "                target.scrollIntoView({ behavior: 'smooth' });\n" +
            "            }\n" +
            "        });\n" +
            "    });\n" +
            "    \n" +
            "    // Table sorting functionality\n" +
            "    document.querySelectorAll('th').forEach(header => {\n" +
            "        header.style.cursor = 'pointer';\n" +
            "        header.addEventListener('click', function() {\n" +
            "            sortTable(this);\n" +
            "        });\n" +
            "    });\n" +
            "});\n" +
            "\n" +
            "function sortTable(header) {\n" +
            "    const table = header.closest('table');\n" +
            "    const tbody = table.querySelector('tbody');\n" +
            "    const rows = Array.from(tbody.querySelectorAll('tr'));\n" +
            "    const index = Array.from(header.parentNode.children).indexOf(header);\n" +
            "    \n" +
            "    rows.sort((a, b) => {\n" +
            "        const aVal = a.children[index].textContent.trim();\n" +
            "        const bVal = b.children[index].textContent.trim();\n" +
            "        return aVal.localeCompare(bVal, undefined, { numeric: true });\n" +
            "    });\n" +
            "    \n" +
            "    rows.forEach(row => tbody.appendChild(row));\n" +
            "}\n";
    }
}

// HTML Template Engine
class HTMLTemplateEngine {
    private Map<String, String> templates;
    
    public HTMLTemplateEngine() {
        this.templates = new HashMap<>();
        initializeTemplates();
    }
    
    private void initializeTemplates() {
        // Basic document template
        templates.put("basic", 
            "<!DOCTYPE html>\n" +
            "<html lang=\"{{language}}\">\n" +
            "<head>\n" +
            "    <meta charset=\"{{charset}}\">\n" +
            "    <meta name=\"viewport\" content=\"{{viewport}}\">\n" +
            "    <title>{{title}}</title>\n" +
            "    {{meta_tags}}\n" +
            "    {{css_resources}}\n" +
            "</head>\n" +
            "<body>\n" +
            "    {{body}}\n" +
            "    {{js_resources}}\n" +
            "</body>\n" +
            "</html>"
        );
        
        // Article template
        templates.put("article", 
            "<!DOCTYPE html>\n" +
            "<html lang=\"{{language}}\">\n" +
            "<head>\n" +
            "    <meta charset=\"{{charset}}\">\n" +
            "    <meta name=\"viewport\" content=\"{{viewport}}\">\n" +
            "    <title>{{title}}</title>\n" +
            "    {{meta_tags}}\n" +
            "    {{css_resources}}\n" +
            "</head>\n" +
            "<body>\n" +
            "    <article class=\"document-content\">\n" +
            "        {{body}}\n" +
            "    </article>\n" +
            "    {{js_resources}}\n" +
            "</body>\n" +
            "</html>"
        );
    }
    
    public String getTemplate(String templateName) {
        return templates.getOrDefault(templateName, templates.get("basic"));
    }
    
    public String processTemplate(String template, Map<String, String> variables) {
        String result = template;
        
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            result = result.replace(placeholder, entry.getValue() != null ? entry.getValue() : "");
        }
        
        return result;
    }
}

// HTML Renderer
class HTMLRenderer {
    private Map<String, Object> rendererConfig;
    
    public HTMLRenderer() {
        this.rendererConfig = new HashMap<>();
    }
    
    public void configure(Map<String, Object> config) {
        this.rendererConfig.putAll(config);
    }
    
    public String render(HTMLDocument document, HTMLRenderingOptions options) {
        try {
            HTMLTemplateEngine templateEngine = new HTMLTemplateEngine();
            String template = templateEngine.getTemplate("basic");
            
            Map<String, String> variables = new HashMap<>();
            variables.put("language", document.getLanguage());
            variables.put("charset", document.getCharset());
            variables.put("viewport", document.getViewport());
            variables.put("title", document.getTitle());
            variables.put("meta_tags", renderMetaTags(document.getMetaTags()));
            variables.put("css_resources", renderCSSResources(document.getCssResources()));
            variables.put("js_resources", renderJSResources(document.getJsResources()));
            variables.put("body", document.getBody());
            
            String html = templateEngine.processTemplate(template, variables);
            
            if (options.isMinify()) {
                html = minifyHTML(html);
            } else if (options.isPrettyPrint()) {
                html = prettyPrintHTML(html, options.getIndentSize());
            }
            
            return html;
            
        } catch (Exception e) {
            throw new RuntimeException("HTML rendering failed: " + e.getMessage(), e);
        }
    }
    
    private String renderMetaTags(Map<String, String> metaTags) {
        StringBuilder sb = new StringBuilder();
        
        for (Map.Entry<String, String> entry : metaTags.entrySet()) {
            sb.append("    <meta name=\"").append(entry.getKey())
              .append("\" content=\"").append(escapeHtml(entry.getValue())).append("\">\n");
        }
        
        return sb.toString();
    }
    
    private String renderCSSResources(List<CSSResource> cssResources) {
        StringBuilder sb = new StringBuilder();
        
        for (CSSResource css : cssResources) {
            if (css.isExternal()) {
                sb.append("    <link rel=\"stylesheet\" href=\"").append(css.getHref()).append("\">\n");
            } else {
                sb.append("    <style>\n").append(css.getContent()).append("\n    </style>\n");
            }
        }
        
        return sb.toString();
    }
    
    private String renderJSResources(List<JavaScriptResource> jsResources) {
        StringBuilder sb = new StringBuilder();
        
        for (JavaScriptResource js : jsResources) {
            if (js.isExternal()) {
                sb.append("    <script src=\"").append(js.getSrc()).append("\"></script>\n");
            } else {
                sb.append("    <script>\n").append(js.getContent()).append("\n    </script>\n");
            }
        }
        
        return sb.toString();
    }
    
    private String minifyHTML(String html) {
        // Simple HTML minification
        return html.replaceAll("\\s+", " ")
                  .replaceAll(">\\s+<", "><")
                  .trim();
    }
    
    private String prettyPrintHTML(String html, int indentSize) {
        // Simple pretty printing (basic implementation)
        String indent = " ".repeat(indentSize);
        return html.replace("><", ">\n" + indent + "<");
    }
    
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
    
    public String stripHTML(String html) {
        // Simple HTML tag removal
        return html.replaceAll("<[^>]+>", "")
                  .replaceAll("&nbsp;", " ")
                  .replaceAll("&amp;", "&")
                  .replaceAll("&lt;", "<")
                  .replaceAll("&gt;", ">")
                  .replaceAll("&quot;", "\"")
                  .replaceAll("&#39;", "'")
                  .trim();
    }
    
    public byte[] convertToPDF(String htmlContent) {
        // Simulate HTML to PDF conversion
        String pdfContent = "PDF_HEADER\n" + stripHTML(htmlContent) + "\nPDF_FOOTER";
        return pdfContent.getBytes();
    }
    
    public byte[] convertToDocx(String htmlContent) {
        // Simulate HTML to DOCX conversion
        String docxContent = "DOCX_HEADER\n" + stripHTML(htmlContent) + "\nDOCX_FOOTER";
        return docxContent.getBytes();
    }
}

// HTML Rendering Options
class HTMLRenderingOptions {
    private boolean minify;
    private boolean prettyPrint;
    private int indentSize;
    private boolean includeComments;
    
    public HTMLRenderingOptions() {
        this.minify = false;
        this.prettyPrint = true;
        this.indentSize = 4;
        this.includeComments = false;
    }
    
    // Getters and setters
    public boolean isMinify() { return minify; }
    public boolean isPrettyPrint() { return prettyPrint; }
    public int getIndentSize() { return indentSize; }
    public boolean isIncludeComments() { return includeComments; }
    
    public void setMinify(boolean minify) { this.minify = minify; }
    public void setPrettyPrint(boolean prettyPrint) { this.prettyPrint = prettyPrint; }
    public void setIndentSize(int indentSize) { this.indentSize = indentSize; }
    public void setIncludeComments(boolean includeComments) { this.includeComments = includeComments; }
}

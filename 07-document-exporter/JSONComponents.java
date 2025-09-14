package abstraction.documentexporter;

import java.util.*;

/**
 * JSON-specific components and helper classes for JSON document export
 */

// JSON Renderer
class JSONRenderer {
    private Map<String, Object> rendererConfig;
    
    public JSONRenderer() {
        this.rendererConfig = new HashMap<>();
    }
    
    public void configure(Map<String, Object> config) {
        this.rendererConfig.putAll(config);
    }
    
    public String render(Map<String, Object> jsonDoc, JSONRenderingOptions options) {
        try {
            if (options.isPrettyPrint()) {
                return renderPretty(jsonDoc, 0, options.getIndentSize());
            } else {
                return renderCompact(jsonDoc);
            }
        } catch (Exception e) {
            throw new RuntimeException("JSON rendering failed: " + e.getMessage(), e);
        }
    }
    
    private String renderPretty(Object obj, int currentIndent, int indentSize) {
        StringBuilder sb = new StringBuilder();
        String indent = " ".repeat(currentIndent);
        String nextIndent = " ".repeat(currentIndent + indentSize);
        
        if (obj == null) {
            sb.append("null");
        } else if (obj instanceof String) {
            sb.append("\"").append(escapeString((String) obj)).append("\"");
        } else if (obj instanceof Number || obj instanceof Boolean) {
            sb.append(obj.toString());
        } else if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            sb.append("{\n");
            
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                sb.append(nextIndent)
                  .append("\"").append(escapeString(entry.getKey())).append("\": ")
                  .append(renderPretty(entry.getValue(), currentIndent + indentSize, indentSize));
                
                if (iterator.hasNext()) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            
            sb.append(indent).append("}");
        } else if (obj instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) obj;
            sb.append("[\n");
            
            for (int i = 0; i < list.size(); i++) {
                sb.append(nextIndent)
                  .append(renderPretty(list.get(i), currentIndent + indentSize, indentSize));
                
                if (i < list.size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            
            sb.append(indent).append("]");
        } else {
            // Fallback for other types
            sb.append("\"").append(escapeString(obj.toString())).append("\"");
        }
        
        return sb.toString();
    }
    
    private String renderCompact(Object obj) {
        StringBuilder sb = new StringBuilder();
        
        if (obj == null) {
            sb.append("null");
        } else if (obj instanceof String) {
            sb.append("\"").append(escapeString((String) obj)).append("\"");
        } else if (obj instanceof Number || obj instanceof Boolean) {
            sb.append(obj.toString());
        } else if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            sb.append("{");
            
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                sb.append("\"").append(escapeString(entry.getKey())).append("\":")
                  .append(renderCompact(entry.getValue()));
                
                if (iterator.hasNext()) {
                    sb.append(",");
                }
            }
            
            sb.append("}");
        } else if (obj instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) obj;
            sb.append("[");
            
            for (int i = 0; i < list.size(); i++) {
                sb.append(renderCompact(list.get(i)));
                if (i < list.size() - 1) {
                    sb.append(",");
                }
            }
            
            sb.append("]");
        } else {
            // Fallback for other types
            sb.append("\"").append(escapeString(obj.toString())).append("\"");
        }
        
        return sb.toString();
    }
    
    private String escapeString(String str) {
        if (str == null) return "";
        
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\b", "\\b")
                  .replace("\f", "\\f")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    public String convertToXML(String jsonContent) {
        // Simplified JSON to XML conversion
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<document>\n");
        
        // Parse JSON and convert to XML (simplified)
        String[] lines = jsonContent.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.contains(":")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim().replaceAll("[\",]", "");
                    String value = parts[1].trim().replaceAll("[\",]", "");
                    
                    if (!key.isEmpty() && !value.isEmpty()) {
                        xml.append("  <").append(key).append(">")
                           .append(value).append("</").append(key).append(">\n");
                    }
                }
            }
        }
        
        xml.append("</document>");
        return xml.toString();
    }
    
    public String convertToCSV(String jsonContent) {
        // Simplified JSON to CSV conversion
        StringBuilder csv = new StringBuilder();
        
        // Extract key-value pairs and convert to CSV
        String[] lines = jsonContent.split("\n");
        csv.append("Key,Value\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.contains(":")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim().replaceAll("[\",]", "");
                    String value = parts[1].trim().replaceAll("[\",]", "");
                    
                    if (!key.isEmpty() && !value.isEmpty()) {
                        csv.append("\"").append(key).append("\",\"").append(value).append("\"\n");
                    }
                }
            }
        }
        
        return csv.toString();
    }
    
    public String convertToText(String jsonContent) {
        // Simplified JSON to text conversion
        StringBuilder text = new StringBuilder();
        
        String[] lines = jsonContent.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.contains(":")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim().replaceAll("[\",]", "");
                    String value = parts[1].trim().replaceAll("[\",]", "");
                    
                    if (!key.isEmpty() && !value.isEmpty()) {
                        text.append(key).append(": ").append(value).append("\n");
                    }
                }
            }
        }
        
        return text.toString();
    }
}

// JSON Rendering Options
class JSONRenderingOptions {
    private boolean prettyPrint;
    private int indentSize;
    private String dateFormat;
    private boolean escapeUnicode;
    private boolean sortKeys;
    
    public JSONRenderingOptions() {
        this.prettyPrint = true;
        this.indentSize = 2;
        this.dateFormat = "ISO_8601";
        this.escapeUnicode = false;
        this.sortKeys = false;
    }
    
    // Getters and setters
    public boolean isPrettyPrint() { return prettyPrint; }
    public int getIndentSize() { return indentSize; }
    public String getDateFormat() { return dateFormat; }
    public boolean isEscapeUnicode() { return escapeUnicode; }
    public boolean isSortKeys() { return sortKeys; }
    
    public void setPrettyPrint(boolean prettyPrint) { this.prettyPrint = prettyPrint; }
    public void setIndentSize(int indentSize) { this.indentSize = indentSize; }
    public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }
    public void setEscapeUnicode(boolean escapeUnicode) { this.escapeUnicode = escapeUnicode; }
    public void setSortKeys(boolean sortKeys) { this.sortKeys = sortKeys; }
}

// JSON Schema Validator
class JSONSchemaValidator {
    private Map<String, Object> validationRules;
    
    public JSONSchemaValidator() {
        this.validationRules = new HashMap<>();
        initializeValidationRules();
    }
    
    private void initializeValidationRules() {
        // Basic JSON validation rules
        validationRules.put("max_depth", 10);
        validationRules.put("max_string_length", 10000);
        validationRules.put("max_array_size", 1000);
        validationRules.put("max_object_properties", 100);
    }
    
    public ValidationResult validate(String jsonContent) {
        try {
            // Basic JSON structure validation
            if (jsonContent == null || jsonContent.trim().isEmpty()) {
                return ValidationResult.failure("JSON content is empty");
            }
            
            String trimmed = jsonContent.trim();
            if (!isValidJSONStructure(trimmed)) {
                return ValidationResult.failure("Invalid JSON structure");
            }
            
            // Check nesting depth
            int depth = calculateNestingDepth(trimmed);
            Integer maxDepth = (Integer) validationRules.get("max_depth");
            if (depth > maxDepth) {
                return ValidationResult.failure("JSON nesting depth " + depth + " exceeds maximum " + maxDepth);
            }
            
            // Check for balanced brackets
            if (!hasBalancedBrackets(trimmed)) {
                return ValidationResult.failure("Unbalanced brackets in JSON");
            }
            
            // Check string lengths (simplified)
            if (hasExcessivelyLongStrings(trimmed)) {
                return ValidationResult.failure("JSON contains excessively long strings");
            }
            
            return ValidationResult.success("JSON validation passed");
            
        } catch (Exception e) {
            return ValidationResult.failure("JSON validation error: " + e.getMessage());
        }
    }
    
    private boolean isValidJSONStructure(String json) {
        // Basic JSON structure check
        return (json.startsWith("{") && json.endsWith("}")) || 
               (json.startsWith("[") && json.endsWith("]"));
    }
    
    private int calculateNestingDepth(String json) {
        int maxDepth = 0;
        int currentDepth = 0;
        
        for (char c : json.toCharArray()) {
            if (c == '{' || c == '[') {
                currentDepth++;
                maxDepth = Math.max(maxDepth, currentDepth);
            } else if (c == '}' || c == ']') {
                currentDepth--;
            }
        }
        
        return maxDepth;
    }
    
    private boolean hasBalancedBrackets(String json) {
        int braceCount = 0;
        int bracketCount = 0;
        boolean inString = false;
        boolean escaped = false;
        
        for (char c : json.toCharArray()) {
            if (escaped) {
                escaped = false;
                continue;
            }
            
            if (c == '\\') {
                escaped = true;
                continue;
            }
            
            if (c == '"') {
                inString = !inString;
                continue;
            }
            
            if (!inString) {
                switch (c) {
                    case '{':
                        braceCount++;
                        break;
                    case '}':
                        braceCount--;
                        if (braceCount < 0) return false;
                        break;
                    case '[':
                        bracketCount++;
                        break;
                    case ']':
                        bracketCount--;
                        if (bracketCount < 0) return false;
                        break;
                }
            }
        }
        
        return braceCount == 0 && bracketCount == 0;
    }
    
    private boolean hasExcessivelyLongStrings(String json) {
        Integer maxLength = (Integer) validationRules.get("max_string_length");
        boolean inString = false;
        boolean escaped = false;
        int currentStringLength = 0;
        
        for (char c : json.toCharArray()) {
            if (escaped) {
                escaped = false;
                if (inString) currentStringLength++;
                continue;
            }
            
            if (c == '\\') {
                escaped = true;
                if (inString) currentStringLength++;
                continue;
            }
            
            if (c == '"') {
                if (inString) {
                    // End of string
                    if (currentStringLength > maxLength) {
                        return true;
                    }
                    currentStringLength = 0;
                }
                inString = !inString;
                continue;
            }
            
            if (inString) {
                currentStringLength++;
            }
        }
        
        return false;
    }
    
    public ValidationResult validateAgainstSchema(String jsonContent, String schemaUrl) {
        // Simplified schema validation
        try {
            // In a real implementation, this would fetch and validate against the schema
            ValidationResult basicValidation = validate(jsonContent);
            if (!basicValidation.isSuccess()) {
                return basicValidation;
            }
            
            // Additional schema-specific checks would go here
            return ValidationResult.success("JSON schema validation passed");
            
        } catch (Exception e) {
            return ValidationResult.failure("Schema validation error: " + e.getMessage());
        }
    }
}

// JSON Formatter
class JSONFormatter {
    
    public String format(String jsonContent, JSONFormattingOptions options) {
        try {
            if (options.isMinify()) {
                return minify(jsonContent);
            } else if (options.isPrettyPrint()) {
                return prettyPrint(jsonContent, options.getIndentSize());
            }
            
            return jsonContent;
            
        } catch (Exception e) {
            throw new RuntimeException("JSON formatting failed: " + e.getMessage(), e);
        }
    }
    
    private String minify(String json) {
        StringBuilder result = new StringBuilder();
        boolean inString = false;
        boolean escaped = false;
        
        for (char c : json.toCharArray()) {
            if (escaped) {
                result.append(c);
                escaped = false;
                continue;
            }
            
            if (c == '\\') {
                result.append(c);
                escaped = true;
                continue;
            }
            
            if (c == '"') {
                inString = !inString;
                result.append(c);
                continue;
            }
            
            if (inString) {
                result.append(c);
            } else if (!Character.isWhitespace(c)) {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    private String prettyPrint(String json, int indentSize) {
        StringBuilder result = new StringBuilder();
        int indentLevel = 0;
        boolean inString = false;
        boolean escaped = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (escaped) {
                result.append(c);
                escaped = false;
                continue;
            }
            
            if (c == '\\') {
                result.append(c);
                escaped = true;
                continue;
            }
            
            if (c == '"') {
                inString = !inString;
                result.append(c);
                continue;
            }
            
            if (inString) {
                result.append(c);
                continue;
            }
            
            switch (c) {
                case '{':
                case '[':
                    result.append(c);
                    indentLevel++;
                    if (i + 1 < json.length() && json.charAt(i + 1) != '}' && json.charAt(i + 1) != ']') {
                        result.append('\n').append(" ".repeat(indentLevel * indentSize));
                    }
                    break;
                case '}':
                case ']':
                    indentLevel--;
                    if (result.length() > 0 && result.charAt(result.length() - 1) != '\n') {
                        result.append('\n').append(" ".repeat(indentLevel * indentSize));
                    }
                    result.append(c);
                    break;
                case ',':
                    result.append(c);
                    result.append('\n').append(" ".repeat(indentLevel * indentSize));
                    break;
                case ':':
                    result.append(c).append(' ');
                    break;
                default:
                    if (!Character.isWhitespace(c)) {
                        result.append(c);
                    }
                    break;
            }
        }
        
        return result.toString();
    }
    
    public String sortKeys(String jsonContent) {
        // Simplified key sorting (would need proper JSON parsing in real implementation)
        return jsonContent; // Placeholder implementation
    }
    
    public String removeComments(String jsonContent) {
        // Remove JSON comments (non-standard but sometimes used)
        return jsonContent.replaceAll("//.*", "")
                         .replaceAll("/\\*.*?\\*/", "");
    }
}

// JSON Formatting Options
class JSONFormattingOptions {
    private boolean prettyPrint;
    private boolean minify;
    private int indentSize;
    private boolean sortKeys;
    private boolean removeComments;
    
    public JSONFormattingOptions() {
        this.prettyPrint = true;
        this.minify = false;
        this.indentSize = 2;
        this.sortKeys = false;
        this.removeComments = false;
    }
    
    // Getters and setters
    public boolean isPrettyPrint() { return prettyPrint; }
    public boolean isMinify() { return minify; }
    public int getIndentSize() { return indentSize; }
    public boolean isSortKeys() { return sortKeys; }
    public boolean isRemoveComments() { return removeComments; }
    
    public void setPrettyPrint(boolean prettyPrint) { this.prettyPrint = prettyPrint; }
    public void setMinify(boolean minify) { this.minify = minify; }
    public void setIndentSize(int indentSize) { this.indentSize = indentSize; }
    public void setSortKeys(boolean sortKeys) { this.sortKeys = sortKeys; }
    public void setRemoveComments(boolean removeComments) { this.removeComments = removeComments; }
}

// JSON Path Utility
class JSONPath {
    
    public static Object getValue(Map<String, Object> json, String path) {
        String[] parts = path.split("\\.");
        Object current = json;
        
        for (String part : parts) {
            if (current instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) current;
                current = map.get(part);
            } else if (current instanceof List && part.matches("\\d+")) {
                @SuppressWarnings("unchecked")
                List<Object> list = (List<Object>) current;
                int index = Integer.parseInt(part);
                if (index >= 0 && index < list.size()) {
                    current = list.get(index);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        
        return current;
    }
    
    public static boolean setValue(Map<String, Object> json, String path, Object value) {
        String[] parts = path.split("\\.");
        Object current = json;
        
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            
            if (current instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) current;
                current = map.get(part);
                if (current == null) {
                    return false;
                }
            } else {
                return false;
            }
        }
        
        if (current instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) current;
            map.put(parts[parts.length - 1], value);
            return true;
        }
        
        return false;
    }
    
    public static boolean hasPath(Map<String, Object> json, String path) {
        return getValue(json, path) != null;
    }
}

// JSON Statistics
class JSONStatistics {
    private int totalKeys;
    private int totalValues;
    private int maxDepth;
    private int totalArrays;
    private int totalObjects;
    private Map<String, Integer> valueTypeCounts;
    
    public JSONStatistics() {
        this.valueTypeCounts = new HashMap<>();
    }
    
    public static JSONStatistics analyze(Map<String, Object> json) {
        JSONStatistics stats = new JSONStatistics();
        stats.analyzeObject(json, 0);
        return stats;
    }
    
    private void analyzeObject(Object obj, int depth) {
        maxDepth = Math.max(maxDepth, depth);
        
        if (obj == null) {
            incrementValueType("null");
        } else if (obj instanceof String) {
            incrementValueType("string");
            totalValues++;
        } else if (obj instanceof Number) {
            incrementValueType("number");
            totalValues++;
        } else if (obj instanceof Boolean) {
            incrementValueType("boolean");
            totalValues++;
        } else if (obj instanceof Map) {
            totalObjects++;
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            totalKeys += map.size();
            
            for (Object value : map.values()) {
                analyzeObject(value, depth + 1);
            }
        } else if (obj instanceof List) {
            totalArrays++;
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) obj;
            
            for (Object item : list) {
                analyzeObject(item, depth + 1);
            }
        }
    }
    
    private void incrementValueType(String type) {
        valueTypeCounts.put(type, valueTypeCounts.getOrDefault(type, 0) + 1);
    }
    
    // Getters
    public int getTotalKeys() { return totalKeys; }
    public int getTotalValues() { return totalValues; }
    public int getMaxDepth() { return maxDepth; }
    public int getTotalArrays() { return totalArrays; }
    public int getTotalObjects() { return totalObjects; }
    public Map<String, Integer> getValueTypeCounts() { return valueTypeCounts; }
    
    @Override
    public String toString() {
        return String.format("JSONStatistics{keys=%d, values=%d, depth=%d, arrays=%d, objects=%d, types=%s}", 
                           totalKeys, totalValues, maxDepth, totalArrays, totalObjects, valueTypeCounts);
    }
}

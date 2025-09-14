# Immutable App Config Loader

## Problem Statement
Build a Config class that loads values (API keys, DB credentials) from a file once. Ensure immutability → values cannot be altered at runtime. Expose controlled getters only.

## Solution Overview

### Key Features
1. **Immutable Design**: Once loaded, configuration values cannot be modified
2. **Thread-Safe Singleton**: Safe for multi-threaded environments
3. **File-Based Loading**: Loads configuration from properties file
4. **Controlled Access**: Only getter methods are exposed
5. **Default Values**: Support for default values when keys are missing

### Encapsulation Principles Demonstrated

#### 1. Data Hiding
- Configuration map is private and final
- No direct access to internal data structure
- All access through controlled methods

#### 2. Immutability
- Uses `Collections.unmodifiableMap()` to prevent modifications
- Private constructor prevents external instantiation
- No setter methods provided

#### 3. Thread Safety
- Double-checked locking pattern for singleton
- Uses `ConcurrentHashMap` for thread-safe initialization
- Volatile instance variable for visibility

#### 4. Controlled Interface
- Only getter methods are public
- No methods to modify configuration after loading
- Safe defaults for missing values

## Class Structure

### Config Class
```java
public final class Config {
    private static volatile Config instance;
    private final Map<String, String> configMap;
    
    // Private constructor
    private Config(String configFilePath)
    
    // Public getters only
    public String getValue(String key)
    public String getValue(String key, String defaultValue)
    public boolean containsKey(String key)
    public Set<String> getAllKeys()
    public Map<String, String> getAllConfigurations()
}
```

## Usage Example

```java
// Load configuration
Config config = Config.getInstance("config.properties");

// Get values
String dbUrl = config.getValue("db.url");
String apiKey = config.getValue("api.key", "default-key");

// Check existence
if (config.containsKey("cache.enabled")) {
    // Handle cache configuration
}

// Get all keys
Set<String> keys = config.getAllKeys();
```

## Configuration File Format

```
# Application Configuration
app.name=MyApp
app.version=1.0.0

# Database Configuration
db.url=jdbc:mysql://localhost:3306/mydb
db.user=admin
db.password=secret123

# API Configuration
api.key=sk-1234567890abcdef
api.base.url=https://api.example.com
```

## Benefits

1. **Security**: Sensitive data cannot be modified at runtime
2. **Consistency**: Same configuration across all threads
3. **Performance**: Loaded once, accessed many times
4. **Maintainability**: Clear separation of configuration concerns
5. **Thread Safety**: Safe for concurrent access

## Anti-Patterns Avoided

- ❌ Public setters that could modify configuration
- ❌ Mutable data structures exposed directly
- ❌ Non-thread-safe singleton implementation
- ❌ Direct file access in getter methods
- ❌ Exposing internal implementation details

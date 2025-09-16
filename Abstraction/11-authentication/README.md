# Authentication System - Abstraction Project

## 🔐 Project Overview

This project demonstrates advanced abstraction concepts in Java through a comprehensive authentication system. It showcases how different authentication methods (Database, OAuth, Biometric) can share common functionality while maintaining their unique security characteristics and implementation details.

## 🏗️ Architecture

### Core Components

1. **AuthenticationType.java** - Enumeration defining authentication methods with security levels
2. **UserRole.java** - User roles and permission system
3. **User.java** - User entity with security features (MFA, account locking, password management)
4. **AuthenticationSession.java** - Session management with expiry and tracking
5. **AuthenticationProvider.java** - Abstract base class for all authentication methods
6. **DatabaseAuthProvider.java** - Traditional username/password authentication
7. **OAuthProvider.java** - OAuth 2.0 flow simulation (Google, GitHub, etc.)
8. **BiometricAuthProvider.java** - Biometric authentication (fingerprint, face, voice, iris)
9. **AuthenticationDemo.java** - Comprehensive demonstration of all features

## 🎯 Key Abstraction Concepts Demonstrated

### 1. Abstract Classes
- `AuthenticationProvider` defines common authentication behavior
- Forces concrete classes to implement provider-specific methods:
  - `authenticate()` - Core authentication logic
  - `validateCredentials()` - Credential validation
  - `configure()` - Provider-specific configuration
  - `isHealthy()` - Health check implementation

### 2. Polymorphism
- Same interface methods behave differently across providers
- `authenticate()` works differently for database vs. OAuth vs. biometric
- Runtime method resolution based on actual provider type
- Unified session creation across all providers

### 3. Encapsulation
- Provider-specific data hidden within concrete classes
- User credentials and security data protected
- Session management encapsulated with controlled access
- Security features like MFA and account locking managed internally

### 4. Inheritance
- All providers inherit from `AuthenticationProvider`
- Shared functionality with specialized security behavior
- Code reuse through inherited methods
- Override capabilities for provider-specific needs

## 🚀 Authentication Methods & Features

### 💾 Database Authentication
- **Security Level**: Low (1/4)
- **Session Duration**: 5 minutes
- **Features**:
  - Username/password validation
  - Account lockout after failed attempts
  - Password change functionality
  - User management (add, remove, update roles)
  - Connection health monitoring

### 🌐 OAuth 2.0 Authentication
- **Security Level**: Medium (2/4)
- **Session Duration**: 1 hour
- **Features**:
  - Authorization code flow simulation
  - Access token management
  - External provider integration (Google, GitHub, Facebook)
  - Token revocation
  - Consent flow simulation

### 👆 Biometric Authentication
- **Security Level**: Maximum (4/4)
- **Session Duration**: 2 hours
- **Features**:
  - Multiple biometric types (fingerprint, face, voice, iris)
  - Template enrollment and management
  - Accuracy threshold configuration
  - Hardware calibration
  - Match score calculation

## 🔒 Security Features

### User Management
- **Account States**: Active, inactive, locked
- **Failed Login Protection**: Auto-lock after 5 failed attempts
- **Password Security**: Hashed storage, change tracking
- **Login History**: IP tracking, attempt logging
- **Multi-Factor Authentication**: TOTP simulation

### Session Security
- **Session Expiry**: Configurable based on auth method
- **Access Tracking**: Usage count and last access time
- **Session Extension**: Configurable session renewal
- **IP Validation**: Session tied to IP address
- **User Agent Tracking**: Browser/device identification

### Role-Based Access Control
- **Roles**: Guest, User, Moderator, Admin, Super Admin
- **Permissions**: Granular permission system
- **Level-Based Access**: Hierarchical access control
- **Permission Checking**: Runtime permission validation

## 🔧 How to Compile and Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command line access

### Compilation
```bash
# Navigate to the project directory
cd "c:\Users\Shivani Mutke\Documents\Covaib-JavaOOP\abstraction\11-authentication"

# Compile all Java files
javac *.java

# Run the authentication demonstration
java AuthenticationDemo
```

## 📈 Expected Output

The demonstration will show:

1. **Polymorphism Demo** - Same interface, different authentication methods
2. **Provider-Specific Features** - Unique capabilities per authentication type
3. **Session Management** - Session lifecycle and security
4. **Security Scenarios** - Account lockout, failed attempts, password changes
5. **Multi-Factor Authentication** - Enhanced security with MFA

## 🎓 Learning Objectives

After studying this project, you should understand:

- How abstraction simplifies complex security systems
- Polymorphic behavior in authentication scenarios
- Inheritance hierarchies in security domain
- Encapsulation for sensitive security data
- Enum usage for type-safe security constants
- Session management and security best practices
- Multi-factor authentication implementation
- Role-based access control systems

## 🔍 Code Analysis Points

### Abstract vs Concrete Methods
- **Abstract**: `authenticate()`, `validateCredentials()`, `configure()`, `isHealthy()`
- **Concrete**: `preAuthenticationCheck()`, `createSession()`, `logAuthenticationAttempt()`

### Provider Differentiation
- **Database**: Traditional credentials with user management
- **OAuth**: External provider integration with token flows
- **Biometric**: Hardware-based authentication with templates

### Design Patterns Used
- **Template Method**: AuthenticationProvider defines authentication flow
- **Strategy Pattern**: Different authentication strategies
- **Factory Pattern**: Session creation
- **Observer Pattern**: Login attempt tracking

## 🚀 Extension Ideas

1. Add more authentication methods (LDAP, Certificate, API Key)
2. Implement real OAuth integration with actual providers
3. Add real biometric hardware integration
4. Create a web-based authentication interface
5. Add audit logging and compliance features
6. Implement single sign-on (SSO) capabilities
7. Add device fingerprinting and risk assessment
8. Create administrative dashboard for user management

---

*This project demonstrates enterprise-level Java abstraction concepts through a practical authentication system that mirrors real-world security implementations.*

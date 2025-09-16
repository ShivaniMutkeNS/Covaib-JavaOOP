package composition.analytics;

/**
 * Default Error Handler implementation
 */
public class DefaultErrorHandler implements ErrorHandler {
    
    @Override
    public void handleError(PipelineError error) {
        System.err.printf("❌ Pipeline Error [%s]: %s - %s\n", 
                         error.getErrorType(), error.getPipelineId(), error.getMessage());
        
        if (error.getException() != null) {
            System.err.println("   Exception: " + error.getException().getMessage());
        }
        
        // In a real implementation, this might:
        // - Log to a centralized logging system
        // - Send alerts to monitoring systems
        // - Trigger automatic recovery procedures
        // - Store error details in a database
    }
    
    @Override
    public String getHandlerName() {
        return "Default Error Handler";
    }
}

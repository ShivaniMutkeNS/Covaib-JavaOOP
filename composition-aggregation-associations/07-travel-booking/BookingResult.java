package composition.travel;

/**
 * Booking Result data class
 */
public class BookingResult {
    private final boolean success;
    private final String message;
    private final String confirmationNumber;
    
    public BookingResult(boolean success, String message, String confirmationNumber) {
        this.success = success;
        this.message = message;
        this.confirmationNumber = confirmationNumber;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getConfirmationNumber() { return confirmationNumber; }
}

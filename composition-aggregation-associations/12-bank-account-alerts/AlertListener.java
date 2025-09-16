package composition.banking;

/**
 * Alert Listener interface for observing bank account alerts
 */
public interface AlertListener {
    void onAlert(Alert alert);
}

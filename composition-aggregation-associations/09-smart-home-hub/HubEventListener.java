package composition.smarthome;

/**
 * Hub Event Listener interface for observing hub events
 */
public interface HubEventListener {
    void onHubEvent(String hubId, String message, HubStatus status);
}

package composition.smarthome;

/**
 * Hub Status data class
 */
public class HubStatus {
    private final String hubId;
    private final boolean isOnline;
    private final int totalDevices;
    private final int onlineDevices;
    private final int deviceGroups;
    private final int activeRules;
    private final long timestamp;
    
    public HubStatus(String hubId, boolean isOnline, int totalDevices, int onlineDevices, 
                    int deviceGroups, int activeRules, long timestamp) {
        this.hubId = hubId;
        this.isOnline = isOnline;
        this.totalDevices = totalDevices;
        this.onlineDevices = onlineDevices;
        this.deviceGroups = deviceGroups;
        this.activeRules = activeRules;
        this.timestamp = timestamp;
    }
    
    public String getHubId() { return hubId; }
    public boolean isOnline() { return isOnline; }
    public int getTotalDevices() { return totalDevices; }
    public int getOnlineDevices() { return onlineDevices; }
    public int getDeviceGroups() { return deviceGroups; }
    public int getActiveRules() { return activeRules; }
    public long getTimestamp() { return timestamp; }
}

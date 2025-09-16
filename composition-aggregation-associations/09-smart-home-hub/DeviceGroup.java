package composition.smarthome;

import java.util.*;

/**
 * Device Group for managing multiple devices together
 */
public class DeviceGroup {
    private final String groupName;
    private final List<SmartDevice> devices;
    
    public DeviceGroup(String groupName) {
        this.groupName = groupName;
        this.devices = new ArrayList<>();
    }
    
    public void addDevice(SmartDevice device) {
        if (!devices.contains(device)) {
            devices.add(device);
        }
    }
    
    public void removeDevice(String deviceId) {
        devices.removeIf(device -> device.getDeviceId().equals(deviceId));
    }
    
    public List<SmartDevice> getDevices() {
        return new ArrayList<>(devices);
    }
    
    public int getDeviceCount() {
        return devices.size();
    }
    
    public String getGroupName() {
        return groupName;
    }
}

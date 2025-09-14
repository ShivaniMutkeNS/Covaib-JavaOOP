
/**
 * Demo class to demonstrate Smart Home Device State
 */
public class SmartHomeDemo {
    public static void main(String[] args) {
        System.out.println("=== Smart Home Device State Demo ===\n");
        
        // Test different device types
        testAirConditioner();
        testLight();
        testSpeaker();
        testWashingMachine();
        testPowerManagement();
        testStateTransitions();
    }
    
    private static void testAirConditioner() {
        System.out.println("=== Air Conditioner Test ===");
        SmartDevice ac = new SmartDevice("AC001", "Living Room AC", SmartDevice.DeviceType.AIR_CONDITIONER);
        
        System.out.println("Initial state: " + ac.getCurrentState());
        System.out.println("Temperature: " + ac.getTemperature() + "°C");
        
        // Turn on AC
        boolean success = ac.turnOn();
        System.out.println("Turn on AC: " + (success ? "SUCCESS" : "FAILED"));
        
        // Set temperature
        ac.setTemperature(20.0);
        System.out.println("Set temperature to 20°C: " + (ac.getTemperature() == 20.0 ? "SUCCESS" : "FAILED"));
        
        // Set mode
        ac.setMode(SmartDevice.DeviceMode.COOL);
        System.out.println("Set mode to COOL: " + (ac.getMode() == SmartDevice.DeviceMode.COOL ? "SUCCESS" : "FAILED"));
        
        // Try invalid temperature
        ac.setTemperature(50.0);
        System.out.println("Set invalid temperature 50°C: " + (ac.getTemperature() == 20.0 ? "SUCCESS" : "FAILED"));
        
        System.out.println("Final state: " + ac.getCurrentState());
        System.out.println();
    }
    
    private static void testLight() {
        System.out.println("=== Light Test ===");
        SmartDevice light = new SmartDevice("LIGHT001", "Bedroom Light", SmartDevice.DeviceType.LIGHT);
        
        System.out.println("Initial state: " + light.getCurrentState());
        
        // Turn on light
        light.turnOn();
        System.out.println("Turn on light: " + (light.getCurrentState() == SmartDevice.DeviceState.ON ? "SUCCESS" : "FAILED"));
        
        // Set brightness
        light.setBrightness(75);
        System.out.println("Set brightness to 75%: " + (light.getBrightness() == 75 ? "SUCCESS" : "FAILED"));
        
        // Try invalid brightness
        light.setBrightness(150);
        System.out.println("Set invalid brightness 150%: " + (light.getBrightness() == 75 ? "SUCCESS" : "FAILED"));
        
        // Set mode
        light.setMode(SmartDevice.DeviceMode.BRIGHT);
        System.out.println("Set mode to BRIGHT: " + (light.getMode() == SmartDevice.DeviceMode.BRIGHT ? "SUCCESS" : "FAILED"));
        
        System.out.println();
    }
    
    private static void testSpeaker() {
        System.out.println("=== Speaker Test ===");
        SmartDevice speaker = new SmartDevice("SPEAKER001", "Kitchen Speaker", SmartDevice.DeviceType.SPEAKER);
        
        // Turn on speaker
        speaker.turnOn();
        System.out.println("Turn on speaker: " + (speaker.getCurrentState() == SmartDevice.DeviceState.ON ? "SUCCESS" : "FAILED"));
        
        // Set volume
        speaker.setVolume(60);
        System.out.println("Set volume to 60%: " + (speaker.getVolume() == 60 ? "SUCCESS" : "FAILED"));
        
        // Set mode
        speaker.setMode(SmartDevice.DeviceMode.LOUD);
        System.out.println("Set mode to LOUD: " + (speaker.getMode() == SmartDevice.DeviceMode.LOUD ? "SUCCESS" : "FAILED"));
        
        System.out.println();
    }
    
    private static void testWashingMachine() {
        System.out.println("=== Washing Machine Test ===");
        SmartDevice washer = new SmartDevice("WASHER001", "Laundry Washer", SmartDevice.DeviceType.WASHING_MACHINE);
        
        // Turn on washer
        washer.turnOn();
        System.out.println("Turn on washer: " + (washer.getCurrentState() == SmartDevice.DeviceState.ON ? "SUCCESS" : "FAILED"));
        
        // Set program
        washer.setProgram("Delicate");
        System.out.println("Set program to Delicate: " + ("Delicate".equals(washer.getCurrentProgram()) ? "SUCCESS" : "FAILED"));
        
        // Set mode
        washer.setMode(SmartDevice.DeviceMode.DELICATE);
        System.out.println("Set mode to DELICATE: " + (washer.getMode() == SmartDevice.DeviceMode.DELICATE ? "SUCCESS" : "FAILED"));
        
        System.out.println();
    }
    
    private static void testPowerManagement() {
        System.out.println("=== Power Management Test ===");
        SmartDevice device = new SmartDevice("DEV001", "Test Device", SmartDevice.DeviceType.LIGHT);
        
        // Turn on device
        device.turnOn();
        System.out.println("Device state after turn on: " + device.getCurrentState());
        
        // Turn off main power
        device.turnMainPowerOff();
        System.out.println("Main power off: " + (!device.isMainPowerOn() ? "SUCCESS" : "FAILED"));
        System.out.println("Device state after main power off: " + device.getCurrentState());
        
        // Try to turn on device with main power off
        boolean success = device.turnOn();
        System.out.println("Turn on device with main power off: " + (success ? "SUCCESS" : "FAILED"));
        
        // Turn main power back on
        device.turnMainPowerOn();
        System.out.println("Main power back on: " + (device.isMainPowerOn() ? "SUCCESS" : "FAILED"));
        
        // Now turn on device
        success = device.turnOn();
        System.out.println("Turn on device with main power on: " + (success ? "SUCCESS" : "FAILED"));
        
        System.out.println();
    }
    
    private static void testStateTransitions() {
        System.out.println("=== State Transitions Test ===");
        SmartDevice device = new SmartDevice("DEV002", "Test Device 2", SmartDevice.DeviceType.TV);
        
        System.out.println("Initial state: " + device.getCurrentState());
        
        // Valid transitions
        device.turnOn();
        System.out.println("Turn on: " + device.getCurrentState());
        
        device.setStandby();
        System.out.println("Set standby: " + device.getCurrentState());
        
        device.turnOn();
        System.out.println("Turn on from standby: " + device.getCurrentState());
        
        device.setMaintenance();
        System.out.println("Set maintenance: " + device.getCurrentState());
        
        device.turnOff();
        System.out.println("Turn off from maintenance: " + device.getCurrentState());
        
        // Display state history
        System.out.println("\nState History:");
        for (SmartDevice.StateTransition transition : device.getStateHistory()) {
            System.out.println(transition);
        }
        
        System.out.println();
    }
}

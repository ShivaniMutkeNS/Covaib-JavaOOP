package composition.smarthome;

import java.util.*;

/**
 * MAANG-Level Smart Home Hub Demo
 * Demonstrates runtime device management, automation, voice commands, and scene execution
 */
public class SmartHomeHubDemo {
    
    public static void main(String[] args) {
        System.out.println("🏠 MAANG-Level Smart Home Hub System Demo");
        System.out.println("=========================================");
        
        // Create smart home hub
        SmartHomeHub hub = new SmartHomeHub("HOME_HUB_001");
        
        // Add event listener for monitoring
        hub.addEventListener(new HubEventListener() {
            @Override
            public void onHubEvent(String hubId, String message, HubStatus status) {
                System.out.println("📢 [" + hubId + "] " + message);
            }
        });
        
        System.out.println("\n=== 1. Adding Smart Devices ===");
        
        // Add various smart devices
        SmartLight livingRoomLight = new SmartLight("living_room_light", "Living Room Light");
        SmartLight bedroomLight = new SmartLight("bedroom_light", "Bedroom Light");
        SmartLight kitchenLight = new SmartLight("kitchen_light", "Kitchen Light");
        SmartThermostat thermostat = new SmartThermostat("main_thermostat", "Main Thermostat");
        SecuritySystem security = new SecuritySystem("security_system", "Home Security");
        VoiceAssistant assistant = new VoiceAssistant("voice_assistant", "Smart Assistant");
        
        hub.addDevice(livingRoomLight);
        hub.addDevice(bedroomLight);
        hub.addDevice(kitchenLight);
        hub.addDevice(thermostat);
        hub.addDevice(security);
        hub.addDevice(assistant);
        
        hub.displayHubStatus();
        
        System.out.println("\n=== 2. Device Control Operations ===");
        
        // Control individual devices
        hub.controlDevice("living_room_light", "turn_on");
        hub.controlDevice("living_room_light", "set_brightness", 75);
        hub.controlDevice("living_room_light", "set_color", "#FF6B6B");
        
        hub.controlDevice("main_thermostat", "set_temperature", 24.0);
        hub.controlDevice("main_thermostat", "set_mode", "auto");
        
        hub.controlDevice("security_system", "arm_home");
        
        System.out.println("\n=== 3. Device Grouping ===");
        
        // Create device groups
        hub.createGroup("all_lights", Arrays.asList("living_room_light", "bedroom_light", "kitchen_light"));
        hub.createGroup("living_area", Arrays.asList("living_room_light", "main_thermostat"));
        
        // Control groups
        System.out.println("\nTurning on all lights...");
        hub.controlGroup("all_lights", "turn_on");
        hub.controlGroup("all_lights", "set_brightness", 60);
        
        System.out.println("\n=== 4. Automation Rules ===");
        
        // Add automation rules
        MotionBasedLightingRule motionRule = new MotionBasedLightingRule(
            "Motion Lighting", "security_system", "living_room_light");
        
        TemperatureBasedRule tempRule = new TemperatureBasedRule(
            "Auto Heating", "main_thermostat", 20.0, "heat");
        
        hub.addAutomationRule(motionRule);
        hub.addAutomationRule(tempRule);
        
        // Trigger automation
        System.out.println("\nSimulating motion detection...");
        hub.controlDevice("security_system", "simulate_motion");
        
        System.out.println("\nSimulating low temperature...");
        hub.controlDevice("main_thermostat", "set_temperature", 18.0);
        
        System.out.println("\n=== 5. Voice Commands ===");
        
        // Process voice commands
        String[] voiceCommands = {
            "Turn on living room lights",
            "Set temperature to 22 degrees",
            "Turn off all lights",
            "Set brightness to 50 percent",
            "Arm security system away"
        };
        
        for (String command : voiceCommands) {
            System.out.println("\n🎤 Voice: \"" + command + "\"");
            DeviceControlResult result = hub.processVoiceCommand(command);
            System.out.println("   Result: " + result.getMessage());
        }
        
        System.out.println("\n=== 6. Scene Management ===");
        
        // Create scenes
        List<SceneAction> movieNightActions = Arrays.asList(
            new SceneAction("living_room_light", "turn_on", new Object[0]),
            new SceneAction("living_room_light", "set_brightness", new Object[]{20}, 500),
            new SceneAction("living_room_light", "set_color", new Object[]{"#4A90E2"}, 500),
            new SceneAction("bedroom_light", "turn_off", new Object[0], 1000),
            new SceneAction("kitchen_light", "turn_off", new Object[0], 500),
            new SceneAction("main_thermostat", "set_temperature", new Object[]{22.0}, 1000)
        );
        
        List<SceneAction> goodMorningActions = Arrays.asList(
            new SceneAction("living_room_light", "turn_on", new Object[0]),
            new SceneAction("living_room_light", "set_brightness", new Object[]{80}, 500),
            new SceneAction("kitchen_light", "turn_on", new Object[0], 1000),
            new SceneAction("main_thermostat", "set_temperature", new Object[]{23.0}, 500),
            new SceneAction("security_system", "disarm", new Object[0], 2000)
        );
        
        hub.createScene("Movie Night", movieNightActions);
        hub.createScene("Good Morning", goodMorningActions);
        
        // Execute scenes
        System.out.println("\nExecuting 'Movie Night' scene...");
        SceneExecutionResult movieResult = hub.executeScene("Movie Night");
        System.out.println("Scene result: " + movieResult.getMessage());
        
        try {
            Thread.sleep(3000); // Wait for scene to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\nExecuting 'Good Morning' scene...");
        SceneExecutionResult morningResult = hub.executeScene("Good Morning");
        System.out.println("Scene result: " + morningResult.getMessage());
        
        System.out.println("\n=== 7. Advanced Automation Testing ===");
        
        // Test security automation
        System.out.println("\nTesting security automation...");
        hub.controlDevice("security_system", "arm_away");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        hub.controlDevice("security_system", "simulate_door");
        
        System.out.println("\n=== 8. Device Discovery and Management ===");
        
        // Simulate device discovery
        List<SmartDevice> discoveredDevices = hub.discoverDevices();
        System.out.println("Discovered " + discoveredDevices.size() + " new devices");
        
        // Remove a device
        System.out.println("\nRemoving bedroom light...");
        hub.removeDevice("bedroom_light");
        
        System.out.println("\n=== 9. Final Hub Status ===");
        hub.displayHubStatus();
        
        System.out.println("\n=== 10. Performance and Metrics ===");
        
        // Display performance metrics
        HubStatus finalStatus = hub.getHubStatus();
        System.out.println("\n📊 Hub Performance Summary:");
        System.out.println("   Total Devices: " + finalStatus.getTotalDevices());
        System.out.println("   Online Devices: " + finalStatus.getOnlineDevices());
        System.out.println("   Device Groups: " + finalStatus.getDeviceGroups());
        System.out.println("   Active Rules: " + finalStatus.getActiveRules());
        System.out.println("   Uptime: " + (System.currentTimeMillis() - finalStatus.getTimestamp()) + "ms");
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("✅ Smart Home Hub successfully demonstrated:");
        System.out.println("   • Runtime device addition/removal");
        System.out.println("   • Device grouping and bulk operations");
        System.out.println("   • Automation rules with triggers");
        System.out.println("   • Voice command processing");
        System.out.println("   • Scene management and execution");
        System.out.println("   • Security integration");
        System.out.println("   • Event-driven architecture");
        System.out.println("   • Asynchronous automation processing");
        
        // Cleanup
        hub.shutdown();
    }
}

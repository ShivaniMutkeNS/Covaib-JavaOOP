package composition.smarthome;

/**
 * Voice Command Parser for natural language processing
 */
public class VoiceCommandParser {
    
    public static VoiceCommand parse(String voiceInput) {
        if (voiceInput == null || voiceInput.trim().isEmpty()) {
            return null;
        }
        
        String input = voiceInput.toLowerCase().trim();
        
        // Simple pattern matching for demo purposes
        // In real implementation, this would use NLP libraries
        
        // Turn on/off commands
        if (input.contains("turn on") || input.contains("switch on")) {
            String target = extractTarget(input);
            return new VoiceCommand(target, "turn_on", new Object[0], isGroupTarget(target));
        }
        
        if (input.contains("turn off") || input.contains("switch off")) {
            String target = extractTarget(input);
            return new VoiceCommand(target, "turn_off", new Object[0], isGroupTarget(target));
        }
        
        // Temperature commands
        if (input.contains("set temperature") || input.contains("temperature to")) {
            String target = extractTarget(input);
            Double temperature = extractTemperature(input);
            if (temperature != null) {
                return new VoiceCommand(target, "set_temperature", new Object[]{temperature}, false);
            }
        }
        
        // Brightness commands
        if (input.contains("brightness") || input.contains("dim")) {
            String target = extractTarget(input);
            Integer brightness = extractBrightness(input);
            if (brightness != null) {
                return new VoiceCommand(target, "set_brightness", new Object[]{brightness}, isGroupTarget(target));
            }
        }
        
        // Security commands
        if (input.contains("arm") && input.contains("home")) {
            return new VoiceCommand("security", "arm_home", new Object[0], false);
        }
        
        if (input.contains("arm") && input.contains("away")) {
            return new VoiceCommand("security", "arm_away", new Object[0], false);
        }
        
        if (input.contains("disarm")) {
            return new VoiceCommand("security", "disarm", new Object[0], false);
        }
        
        return null; // Could not parse command
    }
    
    private static String extractTarget(String input) {
        // Extract device or group names from voice input
        if (input.contains("living room")) return "living_room_lights";
        if (input.contains("bedroom")) return "bedroom_light";
        if (input.contains("kitchen")) return "kitchen_light";
        if (input.contains("thermostat")) return "main_thermostat";
        if (input.contains("security")) return "security_system";
        if (input.contains("all lights")) return "all_lights";
        
        // Default fallback
        return "living_room_lights";
    }
    
    private static boolean isGroupTarget(String target) {
        return target.equals("all_lights") || target.contains("_lights");
    }
    
    private static Double extractTemperature(String input) {
        // Extract temperature from voice input
        String[] words = input.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            try {
                double temp = Double.parseDouble(words[i]);
                if (temp >= 10 && temp <= 35) { // Reasonable temperature range
                    return temp;
                }
            } catch (NumberFormatException e) {
                // Continue searching
            }
        }
        return null;
    }
    
    private static Integer extractBrightness(String input) {
        // Extract brightness percentage from voice input
        String[] words = input.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            try {
                int brightness = Integer.parseInt(words[i].replace("%", ""));
                if (brightness >= 0 && brightness <= 100) {
                    return brightness;
                }
            } catch (NumberFormatException e) {
                // Continue searching
            }
        }
        
        // Handle common phrases
        if (input.contains("full") || input.contains("maximum")) return 100;
        if (input.contains("half")) return 50;
        if (input.contains("low") || input.contains("dim")) return 20;
        
        return null;
    }
}

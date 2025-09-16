package composition.smarthome;

import java.util.*;

/**
 * Scene Manager for Smart Home Hub
 */
public class SceneManager {
    private static final Map<String, List<SceneAction>> scenes = new HashMap<>();
    
    public static void createScene(String sceneName, List<SceneAction> actions) {
        scenes.put(sceneName, new ArrayList<>(actions));
    }
    
    public static List<SceneAction> getScene(String sceneName) {
        List<SceneAction> actions = scenes.get(sceneName);
        return actions != null ? new ArrayList<>(actions) : null;
    }
    
    public static Set<String> getAvailableScenes() {
        return new HashSet<>(scenes.keySet());
    }
    
    public static void removeScene(String sceneName) {
        scenes.remove(sceneName);
    }
}

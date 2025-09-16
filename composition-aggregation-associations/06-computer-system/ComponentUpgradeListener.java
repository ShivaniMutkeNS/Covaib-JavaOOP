package composition.computer;

/**
 * Component Upgrade Listener Interface for Observer Pattern
 */
public interface ComponentUpgradeListener {
    void onComponentUpgrade(String computerSerial, String componentType, String oldSpec, String newSpec);
}

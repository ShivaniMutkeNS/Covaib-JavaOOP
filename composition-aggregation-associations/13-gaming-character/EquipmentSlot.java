package composition.gaming;

/**
 * Equipment slot types for character equipment
 */
public enum EquipmentSlot {
    HELMET("Helmet"),
    CHEST("Chest Armor"),
    LEGS("Leg Armor"),
    BOOTS("Boots"),
    GLOVES("Gloves"),
    MAIN_HAND("Main Hand Weapon"),
    OFF_HAND("Off Hand Weapon/Shield"),
    RING("Ring"),
    NECKLACE("Necklace"),
    BELT("Belt");
    
    private final String displayName;
    
    EquipmentSlot(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

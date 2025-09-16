package composition.gaming;

/**
 * Event listener interface for character events
 */
public interface CharacterEventListener {
    void onCharacterEvent(String characterId, String eventMessage);
    void onLevelUp(String characterId, int newLevel);
    void onDeath(String characterId);
    void onRespawn(String characterId);
    void onEquipmentChange(String characterId, String itemName, boolean equipped);
    void onStatusEffectApplied(String characterId, String effectName);
    void onStatusEffectExpired(String characterId, String effectName);
}

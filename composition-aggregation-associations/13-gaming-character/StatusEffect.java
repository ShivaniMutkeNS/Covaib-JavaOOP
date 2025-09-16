package composition.gaming;

/**
 * Status effect system for temporary character modifications
 */
public class StatusEffect {
    private final String name;
    private final String description;
    private final StatusEffectType type;
    private final int duration; // in seconds
    private final AttributeType affectedAttribute;
    private final int modifier;
    private final long startTime;
    private boolean active;
    
    public StatusEffect(String name, StatusEffectType type, int duration, 
                       AttributeType affectedAttribute, int modifier) {
        this.name = name;
        this.description = generateDescription(type, affectedAttribute, modifier);
        this.type = type;
        this.duration = duration;
        this.affectedAttribute = affectedAttribute;
        this.modifier = modifier;
        this.startTime = System.currentTimeMillis();
        this.active = true;
    }
    
    private String generateDescription(StatusEffectType type, AttributeType attr, int mod) {
        String effect = mod > 0 ? "increases" : "decreases";
        return String.format("%s %s by %d", type.name().toLowerCase(), 
                           attr.name().toLowerCase(), Math.abs(mod));
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > duration * 1000L;
    }
    
    public int getRemainingDuration() {
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.max(0, duration - (int)(elapsed / 1000));
    }
    
    public void expire() {
        this.active = false;
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public StatusEffectType getType() { return type; }
    public int getDuration() { return duration; }
    public AttributeType getAffectedAttribute() { return affectedAttribute; }
    public int getModifier() { return modifier; }
    public long getStartTime() { return startTime; }
    public boolean isActive() { return active && !isExpired(); }
    
    @Override
    public String toString() {
        return String.format("%s (%ds remaining)", name, getRemainingDuration());
    }
}

enum StatusEffectType {
    BUFF, DEBUFF, DOT, HOT, STUN, POISON, REGENERATION
}

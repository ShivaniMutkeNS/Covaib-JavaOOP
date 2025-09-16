package composition.gaming;

/**
 * Character statistics tracking for analytics and progression
 */
public class CharacterStats {
    private int attacksPerformed;
    private int skillsUsed;
    private int itemsEquipped;
    private int deaths;
    private long damageDealt;
    private long damageTaken;
    private long healingReceived;
    private int levelsGained;
    private long experienceGained;
    private long playtimeSeconds;
    private double distanceTraveled;
    private final long createdAt;
    
    public CharacterStats() {
        this.attacksPerformed = 0;
        this.skillsUsed = 0;
        this.itemsEquipped = 0;
        this.deaths = 0;
        this.damageDealt = 0;
        this.damageTaken = 0;
        this.healingReceived = 0;
        this.levelsGained = 0;
        this.experienceGained = 0;
        this.playtimeSeconds = 0;
        this.distanceTraveled = 0.0;
        this.createdAt = System.currentTimeMillis();
    }
    
    // Increment methods
    public void incrementAttacksPerformed() { attacksPerformed++; }
    public void incrementSkillsUsed() { skillsUsed++; }
    public void incrementItemsEquipped() { itemsEquipped++; }
    public void incrementDeaths() { deaths++; }
    public void incrementLevelsGained() { levelsGained++; }
    
    // Add methods
    public void addDamageDealt(long damage) { damageDealt += damage; }
    public void addDamageTaken(long damage) { damageTaken += damage; }
    public void addHealingReceived(long healing) { healingReceived += healing; }
    public void addExperienceGained(long exp) { experienceGained += exp; }
    public void addPlaytime(long seconds) { playtimeSeconds += seconds; }
    public void addDistanceTraveled(double distance) { distanceTraveled += distance; }
    
    // Getters
    public int getAttacksPerformed() { return attacksPerformed; }
    public int getSkillsUsed() { return skillsUsed; }
    public int getItemsEquipped() { return itemsEquipped; }
    public int getDeaths() { return deaths; }
    public long getDamageDealt() { return damageDealt; }
    public long getDamageTaken() { return damageTaken; }
    public long getHealingReceived() { return healingReceived; }
    public int getLevelsGained() { return levelsGained; }
    public long getExperienceGained() { return experienceGained; }
    public long getPlaytimeSeconds() { return playtimeSeconds; }
    public double getDistanceTraveled() { return distanceTraveled; }
    public long getCreatedAt() { return createdAt; }
    
    // Calculated stats
    public double getAverageDamagePerAttack() {
        return attacksPerformed > 0 ? (double) damageDealt / attacksPerformed : 0.0;
    }
    
    public double getSurvivalRatio() {
        return deaths > 0 ? (double) (attacksPerformed + skillsUsed) / deaths : Double.MAX_VALUE;
    }
    
    public long getCharacterAge() {
        return System.currentTimeMillis() - createdAt;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Stats: Attacks=%d, Skills=%d, Items=%d, Deaths=%d, Damage=%.0f/%.0f, Levels=%d, Exp=%d",
            attacksPerformed, skillsUsed, itemsEquipped, deaths, 
            (double) damageDealt, (double) damageTaken, levelsGained, experienceGained
        );
    }
}

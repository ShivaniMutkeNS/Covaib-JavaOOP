public abstract class Character {
    protected String name;
    protected String characterClass;
    protected int level;
    protected int health;
    protected int mana;
    protected int attack;
    protected int defense;
    
    public Character(String name, String characterClass, int level) {
        this.name = name;
        this.characterClass = characterClass;
        this.level = level;
        this.health = 100;
        this.mana = 50;
        this.attack = 20;
        this.defense = 15;
    }
    
    public abstract void attack();
    public abstract void specialMove();
    public abstract String getCharacterFeatures();
    
    public String getCharacterInfo() {
        return String.format("Character: %s (%s), Level: %d, Health: %d, Attack: %d", 
                           name, characterClass, level, health, attack);
    }
}

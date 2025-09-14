public class Warrior extends Character {
    public Warrior(String name, int level) {
        super(name, "Warrior", level);
        this.health = 150;
        this.attack = 30;
        this.defense = 25;
    }
    
    @Override
    public void attack() {
        System.out.println(name + " swings a mighty sword!");
    }
    
    @Override
    public void specialMove() {
        System.out.println(name + " uses Berserker Rage!");
    }
    
    @Override
    public String getCharacterFeatures() {
        return "Warrior Features: High Health: " + health + ", High Attack: " + attack + 
               ", Special: Berserker Rage, Weapon: Sword";
    }
}

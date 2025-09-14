public class GamingDemo {
    public static void main(String[] args) {
        System.out.println("🎮 GAMING CHARACTERS 🎮");
        System.out.println("=" .repeat(50));
        
        Character[] characters = {
            new Warrior("Aragorn", 10)
        };
        
        System.out.println("\n📋 CHARACTER INFORMATION:");
        for (Character character : characters) {
            System.out.println(character.getCharacterInfo());
        }
        
        System.out.println("\n⚔️ COMBAT DEMONSTRATION:");
        for (Character character : characters) {
            character.attack();
            character.specialMove();
            System.out.println(character.getCharacterFeatures());
        }
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
}

public class ZooDemo {
    public static void main(String[] args) {
        System.out.println("🦁 ZOO ANIMALS DEMO 🦁");
        System.out.println("=" .repeat(30));
        
        Animal[] animals = {
            new Lion("Simba"),
            new Lion("Nala")
        };
        
        System.out.println("\n📋 ANIMAL INFORMATION:");
        for (Animal animal : animals) {
            System.out.println("Animal: " + animal.name + " (" + animal.species + ")");
        }
        
        System.out.println("\n🎵 ANIMAL SOUNDS:");
        for (Animal animal : animals) {
            animal.makeSound();
        }
        
        System.out.println("\n🏃 ANIMAL MOVEMENT:");
        for (Animal animal : animals) {
            animal.move();
        }
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
}
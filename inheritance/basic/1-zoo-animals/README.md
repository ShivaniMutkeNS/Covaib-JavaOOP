# 🦁 Zoo Animals - Inheritance & Method Overriding

## Problem Statement
Create a zoo management system with different types of animals. Implement inheritance with abstract base class `Animal` and concrete subclasses `Lion`, `Elephant`, and `Penguin`. Each animal should override the abstract methods `makeSound()` and `move()` with their specific behaviors.

## Learning Objectives
- **Abstract Classes**: Understanding abstract methods and classes
- **Method Overriding**: Implementing polymorphic behavior
- **Inheritance Basics**: Creating is-a relationships
- **Runtime Polymorphism**: Using parent references to call child methods

## Class Hierarchy

```
Animal (Abstract)
├── Lion
├── Elephant
└── Penguin
```

## Key Concepts Demonstrated

### 1. Abstract Base Class (`Animal`)
- Contains abstract methods that must be implemented by subclasses
- Provides common functionality (eat, sleep, getters)
- Cannot be instantiated directly

### 2. Method Overriding
- Each subclass provides its own implementation of `makeSound()` and `move()`
- Demonstrates polymorphic behavior at runtime

### 3. Constructor Chaining
- Subclasses call parent constructor using `super()`
- Ensures proper initialization of inherited fields

## Code Structure

### Animal.java (Abstract Base Class)
```java
public abstract class Animal {
    protected String name;
    protected int age;
    protected String species;
    protected boolean isHealthy;
    
    // Abstract methods - must be implemented by subclasses
    public abstract String makeSound();
    public abstract String move();
    
    // Concrete methods - shared by all animals
    public String eat() { ... }
    public String sleep() { ... }
}
```

### Lion.java (Concrete Subclass)
```java
public class Lion extends Animal {
    private String maneColor;
    private boolean isAlpha;
    
    @Override
    public String makeSound() {
        return isAlpha ? "ROAR! ROAR! ROAR!" : "Roar!";
    }
    
    @Override
    public String move() {
        return "prowls gracefully across the savanna";
    }
    
    // Lion-specific methods
    public String hunt() { ... }
    public String leadPride() { ... }
}
```

### Elephant.java (Concrete Subclass)
```java
public class Elephant extends Animal {
    private double trunkLength;
    private int tuskCount;
    private String herdRole;
    
    @Override
    public String makeSound() {
        return age < 5 ? "trumpets softly" : "trumpets loudly";
    }
    
    @Override
    public String move() {
        return "walks majestically with heavy footsteps";
    }
    
    // Elephant-specific methods
    public String useTrunk() { ... }
    public String remember() { ... }
}
```

### Penguin.java (Concrete Subclass)
```java
public class Penguin extends Animal {
    private String habitat;
    private boolean canSwim;
    private String mateStatus;
    
    @Override
    public String makeSound() {
        return age < 2 ? "chirps" : "honks";
    }
    
    @Override
    public String move() {
        return canSwim ? "waddles and swims" : "waddles clumsily";
    }
    
    // Penguin-specific methods
    public String swim() { ... }
    public String slide() { ... }
}
```

## How to Run

1. Compile all Java files:
```bash
javac *.java
```

2. Run the demo:
```bash
java ZooDemo
```

## Expected Output

```
🦁 ZOO ANIMALS DEMONSTRATION 🦁
==================================================

📋 ANIMAL INFORMATION:
--------------------------------------------------
Name: Simba, Species: Lion, Age: 8, Healthy: true, Mane Color: Golden, Alpha: true
Name: Nala, Species: Lion, Age: 6, Healthy: true, Mane Color: Dark Brown, Alpha: false
Name: Dumbo, Species: Elephant, Age: 12, Healthy: true, Trunk Length: 2.5m, Tusks: 2, Role: Matriarch
Name: Babar, Species: Elephant, Age: 25, Healthy: true, Trunk Length: 3.0m, Tusks: 1, Role: Bull
Name: Pingu, Species: Penguin, Age: 3, Healthy: true, Habitat: Antarctica, Can Swim: true, Mate Status: Single
Name: Penny, Species: Penguin, Age: 5, Healthy: true, Habitat: Arctic, Can Swim: true, Mate Status: Mated

🔊 SOUNDS:
--------------------------------------------------
Simba roars majestically: ROAR! ROAR! ROAR!
Nala roars: Roar!
Dumbo trumpets loudly: TRUMPET! TRUMPET! TRUMPET!
Babar trumpets loudly: TRUMPET! TRUMPET! TRUMPET!
Pingu honks: Honk! Honk! Honk!
Penny honks: Honk! Honk! Honk!

🏃 MOVEMENT:
--------------------------------------------------
Simba prowls gracefully across the savanna
Nala prowls gracefully across the savanna
Dumbo walks majestically with heavy footsteps
Babar walks majestically with heavy footsteps
Pingu waddles on land and swims gracefully in water
Penny waddles on land and swims gracefully in water

🍽️ EATING BEHAVIOR:
--------------------------------------------------
Simba is eating food
Nala is eating food
Dumbo is eating food
Babar is eating food
Pingu is eating food
Penny is eating food

😴 SLEEPING BEHAVIOR:
--------------------------------------------------
Simba is sleeping peacefully
Nala is sleeping peacefully
Dumbo is sleeping peacefully
Babar is sleeping peacefully
Pingu is sleeping peacefully
Penny is sleeping peacefully

🦁 LION-SPECIFIC BEHAVIORS:
--------------------------------------------------
Simba stalks prey with stealth and precision
Simba leads the pride with authority
Nala stalks prey with stealth and precision
Nala follows the pride hierarchy

🐘 ELEPHANT-SPECIFIC BEHAVIORS:
--------------------------------------------------
Dumbo uses its 2.5m long trunk to grab food
Dumbo remembers everything with its excellent memory
Dumbo sprays water with its trunk to cool down
Babar uses its 3.0m long trunk to grab food
Babar remembers everything with its excellent memory

🐧 PENGUIN-SPECIFIC BEHAVIORS:
--------------------------------------------------
Pingu dives into the water and swims like a torpedo
Pingu slides on its belly across the ice
Pingu huddles with other penguins for warmth
Pingu dives deep to catch fish
Penny dives into the water and swims like a torpedo
Penny slides on its belly across the ice

🔄 RUNTIME POLYMORPHISM DEMONSTRATION:
--------------------------------------------------
Processing animals using polymorphism:
1. Simba (Lion)
   Sound: Simba roars majestically: ROAR! ROAR! ROAR!
   Movement: Simba prowls gracefully across the savanna

2. Nala (Lion)
   Sound: Nala roars: Roar!
   Movement: Nala prowls gracefully across the savanna

3. Dumbo (Elephant)
   Sound: Dumbo trumpets loudly: TRUMPET! TRUMPET! TRUMPET!
   Movement: Dumbo walks majestically with heavy footsteps

4. Babar (Elephant)
   Sound: Babar trumpets loudly: TRUMPET! TRUMPET! TRUMPET!
   Movement: Babar walks majestically with heavy footsteps

5. Pingu (Penguin)
   Sound: Pingu honks: Honk! Honk! Honk!
   Movement: Pingu waddles on land and swims gracefully in water

6. Penny (Penguin)
   Sound: Penny honks: Honk! Honk! Honk!
   Movement: Penny waddles on land and swims gracefully in water

✨ DEMONSTRATION COMPLETE! ✨
```

## Key Learning Points

1. **Abstract Methods**: Force subclasses to implement specific behaviors
2. **Method Overriding**: Each subclass provides its own implementation
3. **Polymorphism**: Same method call produces different results based on object type
4. **Inheritance**: Code reuse and shared functionality
5. **Encapsulation**: Private fields with public getters
6. **Constructor Chaining**: Proper initialization using `super()`

## Advanced Concepts Demonstrated

- **Runtime Polymorphism**: Method calls resolved at runtime based on actual object type
- **Abstract Class Design**: Cannot instantiate abstract classes directly
- **Method Overriding vs Overloading**: Different concepts with different purposes
- **Access Modifiers**: Protected fields accessible to subclasses
- **String Formatting**: Professional output formatting

This example demonstrates the fundamental concepts of inheritance and polymorphism in Java, providing a solid foundation for understanding more complex object-oriented programming patterns.

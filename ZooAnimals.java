abstract class Animal {
    abstract void makeSound();
    abstract void move();
}

class Lion extends Animal {
    void makeSound() {
        System.out.println("Roar");
    }
    void move() {
        System.out.println("Lion runs");
    }
}

class Elephant extends Animal {
    void makeSound() {
        System.out.println("Trumpet");
    }
    void move() {
        System.out.println("Elephant walks");
    }
}

class Penguin extends Animal {
    void makeSound() {
        System.out.println("Squawk");
    }
    void move() {
        System.out.println("Penguin waddles");
    }
}

public class ZooAnimals {
    public static void main(String[] args) {
        Animal[] animals = { new Lion(), new Elephant(), new Penguin() };
        for (Animal animal : animals) {
            animal.makeSound();
            animal.move();
        }
    }
}
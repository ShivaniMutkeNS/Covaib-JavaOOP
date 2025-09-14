public class Lion extends Animal {
    public Lion(String name) {
        super(name, "Lion");
    }
    
    @Override
    public void makeSound() {
        System.out.println(name + " roars loudly!");
    }
    
    @Override
    public void move() {
        System.out.println(name + " prowls through the savanna");
    }
}
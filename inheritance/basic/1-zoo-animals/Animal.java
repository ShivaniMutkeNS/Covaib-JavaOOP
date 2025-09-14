public abstract class Animal {
    protected String name;
    protected String species;
    
    public Animal(String name, String species) {
        this.name = name;
        this.species = species;
    }
    
    public abstract void makeSound();
    public abstract void move();
    
    public void eat() {
        System.out.println(name + " is eating");
    }
}
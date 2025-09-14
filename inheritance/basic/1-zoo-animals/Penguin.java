/**
 * Penguin class extending Animal
 * Demonstrates method overriding and inheritance
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class Penguin extends Animal {
    private String habitat;
    private boolean canSwim;
    private String mateStatus;
    
    /**
     * Constructor for Penguin
     * @param name The name of the penguin
     * @param age The age of the penguin
     * @param habitat The natural habitat of the penguin
     * @param canSwim Whether the penguin can swim
     * @param mateStatus The mating status (Single, Mated, Parent)
     */
    public Penguin(String name, int age, String habitat, boolean canSwim, String mateStatus) {
        super(name, age, "Penguin");
        this.habitat = habitat;
        this.canSwim = canSwim;
        this.mateStatus = mateStatus;
    }
    
    /**
     * Override makeSound method with penguin-specific sound
     * @return String representation of penguin's call
     */
    @Override
    public String makeSound() {
        if (age < 2) {
            return name + " chirps: *chirp* *chirp*";
        } else {
            return name + " honks: Honk! Honk! Honk!";
        }
    }
    
    /**
     * Override move method with penguin-specific movement
     * @return String description of penguin's movement
     */
    @Override
    public String move() {
        if (canSwim) {
            return name + " waddles on land and swims gracefully in water";
        } else {
            return name + " waddles clumsily on land";
        }
    }
    
    /**
     * Penguin-specific method for swimming
     * @return String description of swimming behavior
     */
    public String swim() {
        if (canSwim) {
            return name + " dives into the water and swims like a torpedo";
        } else {
            return name + " cannot swim and stays on land";
        }
    }
    
    /**
     * Penguin-specific method for sliding
     * @return String description of sliding behavior
     */
    public String slide() {
        return name + " slides on its belly across the ice";
    }
    
    /**
     * Penguin-specific method for huddling
     * @return String description of huddling behavior
     */
    public String huddle() {
        return name + " huddles with other penguins for warmth";
    }
    
    /**
     * Penguin-specific method for fishing
     * @return String description of fishing behavior
     */
    public String fish() {
        if (canSwim) {
            return name + " dives deep to catch fish";
        } else {
            return name + " looks longingly at the fish in the water";
        }
    }
    
    /**
     * Getter for habitat
     * @return The natural habitat of the penguin
     */
    public String getHabitat() {
        return habitat;
    }
    
    /**
     * Getter for swimming ability
     * @return True if the penguin can swim
     */
    public boolean canSwim() {
        return canSwim;
    }
    
    /**
     * Getter for mate status
     * @return The mating status of the penguin
     */
    public String getMateStatus() {
        return mateStatus;
    }
    
    /**
     * Override getInfo to include penguin-specific information
     * @return String with penguin details
     */
    @Override
    public String getInfo() {
        return super.getInfo() + String.format(", Habitat: %s, Can Swim: %s, Mate Status: %s", 
                                              habitat, canSwim, mateStatus);
    }
}

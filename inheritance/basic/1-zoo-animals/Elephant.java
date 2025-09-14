/**
 * Elephant class extending Animal
 * Demonstrates method overriding and inheritance
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class Elephant extends Animal {
    private double trunkLength;
    private int tuskCount;
    private String herdRole;
    
    /**
     * Constructor for Elephant
     * @param name The name of the elephant
     * @param age The age of the elephant
     * @param trunkLength The length of the elephant's trunk in meters
     * @param tuskCount The number of tusks (0, 1, or 2)
     * @param herdRole The role in the herd (Matriarch, Bull, Calf, etc.)
     */
    public Elephant(String name, int age, double trunkLength, int tuskCount, String herdRole) {
        super(name, age, "Elephant");
        this.trunkLength = trunkLength;
        this.tuskCount = tuskCount;
        this.herdRole = herdRole;
    }
    
    /**
     * Override makeSound method with elephant-specific sound
     * @return String representation of elephant's trumpet
     */
    @Override
    public String makeSound() {
        if (age < 5) {
            return name + " trumpets softly: *trumpet* *trumpet*";
        } else {
            return name + " trumpets loudly: TRUMPET! TRUMPET! TRUMPET!";
        }
    }
    
    /**
     * Override move method with elephant-specific movement
     * @return String description of elephant's movement
     */
    @Override
    public String move() {
        return name + " walks majestically with heavy footsteps";
    }
    
    /**
     * Elephant-specific method for using trunk
     * @return String description of trunk usage
     */
    public String useTrunk() {
        return name + " uses its " + trunkLength + "m long trunk to grab food";
    }
    
    /**
     * Elephant-specific method for memory behavior
     * @return String description of memory behavior
     */
    public String remember() {
        return name + " remembers everything with its excellent memory";
    }
    
    /**
     * Elephant-specific method for water behavior
     * @return String description of water behavior
     */
    public String sprayWater() {
        return name + " sprays water with its trunk to cool down";
    }
    
    /**
     * Getter for trunk length
     * @return The length of the elephant's trunk
     */
    public double getTrunkLength() {
        return trunkLength;
    }
    
    /**
     * Getter for tusk count
     * @return The number of tusks
     */
    public int getTuskCount() {
        return tuskCount;
    }
    
    /**
     * Getter for herd role
     * @return The role in the herd
     */
    public String getHerdRole() {
        return herdRole;
    }
    
    /**
     * Override getInfo to include elephant-specific information
     * @return String with elephant details
     */
    @Override
    public String getInfo() {
        return super.getInfo() + String.format(", Trunk Length: %.1fm, Tusks: %d, Role: %s", 
                                              trunkLength, tuskCount, herdRole);
    }
}

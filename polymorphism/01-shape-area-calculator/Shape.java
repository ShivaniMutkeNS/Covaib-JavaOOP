/**
 * Base shape abstraction for area calculation.
 */
public abstract class Shape {
    public abstract double calculateArea();

    public String getName() {
        return getClass().getSimpleName();
    }
}



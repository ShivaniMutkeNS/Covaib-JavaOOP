package composition.gaming;

/**
 * Position class for 3D coordinates in the game world
 */
public class Position {
    private final double x;
    private final double y;
    private final double z;
    
    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    
    public double distanceTo(Position other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    public Position add(double deltaX, double deltaY, double deltaZ) {
        return new Position(x + deltaX, y + deltaY, z + deltaZ);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return Double.compare(position.x, x) == 0 &&
               Double.compare(position.y, y) == 0 &&
               Double.compare(position.z, z) == 0;
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y, z);
    }
    
    @Override
    public String toString() {
        return String.format("Position(%.2f, %.2f, %.2f)", x, y, z);
    }
}

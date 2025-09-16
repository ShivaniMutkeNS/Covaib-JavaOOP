package composition.gaming;

/**
 * Strategy pattern for character movement systems
 */
public interface MovementStrategy {
    String getStrategyName();
    MovementResult move(GameCharacter character, Position targetPosition);
    double getMovementSpeed();
    boolean canMoveToPosition(Position from, Position to);
}

/**
 * Standard movement strategy implementation
 */
class StandardMovementStrategy implements MovementStrategy {
    private static final double BASE_SPEED = 1.0;
    
    @Override
    public String getStrategyName() {
        return "Standard Movement";
    }
    
    @Override
    public MovementResult move(GameCharacter character, Position targetPosition) {
        Position currentPos = character.getPosition();
        
        if (!canMoveToPosition(currentPos, targetPosition)) {
            return new MovementResult(false, "Cannot move to target position", currentPos);
        }
        
        double distance = calculateDistance(currentPos, targetPosition);
        double movementTime = distance / getMovementSpeed();
        
        character.setPosition(targetPosition);
        
        String message = String.format("Moved to position (%.1f, %.1f, %.1f) in %.1fs", 
                                     targetPosition.getX(), targetPosition.getY(), 
                                     targetPosition.getZ(), movementTime);
        
        return new MovementResult(true, message, targetPosition);
    }
    
    @Override
    public double getMovementSpeed() {
        return BASE_SPEED;
    }
    
    @Override
    public boolean canMoveToPosition(Position from, Position to) {
        double distance = calculateDistance(from, to);
        return distance <= 100.0; // Max movement distance per action
    }
    
    private double calculateDistance(Position from, Position to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}

/**
 * Fast movement strategy - higher speed, limited range
 */
class FastMovementStrategy implements MovementStrategy {
    private static final double FAST_SPEED = 2.0;
    private static final double MAX_DISTANCE = 50.0;
    
    @Override
    public String getStrategyName() {
        return "Fast Movement";
    }
    
    @Override
    public MovementResult move(GameCharacter character, Position targetPosition) {
        Position currentPos = character.getPosition();
        
        if (!canMoveToPosition(currentPos, targetPosition)) {
            return new MovementResult(false, "Target too far for fast movement", currentPos);
        }
        
        double distance = calculateDistance(currentPos, targetPosition);
        double movementTime = distance / getMovementSpeed();
        
        character.setPosition(targetPosition);
        
        String message = String.format("Fast moved to position (%.1f, %.1f, %.1f) in %.1fs", 
                                     targetPosition.getX(), targetPosition.getY(), 
                                     targetPosition.getZ(), movementTime);
        
        return new MovementResult(true, message, targetPosition);
    }
    
    @Override
    public double getMovementSpeed() {
        return FAST_SPEED;
    }
    
    @Override
    public boolean canMoveToPosition(Position from, Position to) {
        double distance = calculateDistance(from, to);
        return distance <= MAX_DISTANCE;
    }
    
    private double calculateDistance(Position from, Position to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}

/**
 * Teleport movement strategy - instant movement with cooldown
 */
class TeleportMovementStrategy implements MovementStrategy {
    private static final double TELEPORT_RANGE = 200.0;
    private static final long COOLDOWN_MS = 30000; // 30 seconds
    private long lastTeleport = 0;
    
    @Override
    public String getStrategyName() {
        return "Teleport Movement";
    }
    
    @Override
    public MovementResult move(GameCharacter character, Position targetPosition) {
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastTeleport < COOLDOWN_MS) {
            long remainingCooldown = (COOLDOWN_MS - (currentTime - lastTeleport)) / 1000;
            return new MovementResult(false, 
                "Teleport on cooldown for " + remainingCooldown + " seconds", 
                character.getPosition());
        }
        
        Position currentPos = character.getPosition();
        
        if (!canMoveToPosition(currentPos, targetPosition)) {
            return new MovementResult(false, "Target too far for teleport", currentPos);
        }
        
        character.setPosition(targetPosition);
        lastTeleport = currentTime;
        
        String message = String.format("Teleported to position (%.1f, %.1f, %.1f)", 
                                     targetPosition.getX(), targetPosition.getY(), 
                                     targetPosition.getZ());
        
        return new MovementResult(true, message, targetPosition);
    }
    
    @Override
    public double getMovementSpeed() {
        return Double.MAX_VALUE; // Instant
    }
    
    @Override
    public boolean canMoveToPosition(Position from, Position to) {
        double distance = calculateDistance(from, to);
        return distance <= TELEPORT_RANGE;
    }
    
    private double calculateDistance(Position from, Position to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}

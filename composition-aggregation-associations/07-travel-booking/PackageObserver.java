package composition.travel;

/**
 * Package Observer Interface for Observer Pattern
 */
public interface PackageObserver {
    void onPackageUpdate(String packageId, String message, PackageStatus status);
}

package composition.analytics;

/**
 * Data Filter interface for filtering data records
 */
public interface DataFilter {
    boolean accept(DataRecord record);
}

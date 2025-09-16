package composition.analytics;

/**
 * Data Transformer interface for transforming data records
 */
public interface DataTransformer {
    DataRecord transform(DataRecord record);
}

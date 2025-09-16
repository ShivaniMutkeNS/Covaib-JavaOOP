package composition.reconciliation;

import java.math.BigDecimal;
import java.util.*;

/**
 * Supporting classes for the Payment Reconciliation System
 */

/**
 * Represents a discrepancy between internal and external records
 */
class Discrepancy {
    private final String discrepancyId;
    private final DiscrepancyType type;
    private final String description;
    private final PaymentRecord internalRecord;
    private final ExternalRecord externalRecord;
    private final DiscrepancySeverity severity;
    private final long detectedAt;
    private final Map<String, Object> additionalData;
    
    public Discrepancy(DiscrepancyType type, String description, 
                      PaymentRecord internalRecord, ExternalRecord externalRecord, 
                      DiscrepancySeverity severity) {
        this.discrepancyId = UUID.randomUUID().toString();
        this.type = type;
        this.description = description;
        this.internalRecord = internalRecord;
        this.externalRecord = externalRecord;
        this.severity = severity;
        this.detectedAt = System.currentTimeMillis();
        this.additionalData = new HashMap<>();
    }
    
    public void addAdditionalData(String key, Object value) {
        additionalData.put(key, value);
    }
    
    // Getters
    public String getDiscrepancyId() { return discrepancyId; }
    public DiscrepancyType getType() { return type; }
    public String getDescription() { return description; }
    public PaymentRecord getInternalRecord() { return internalRecord; }
    public ExternalRecord getExternalRecord() { return externalRecord; }
    public DiscrepancySeverity getSeverity() { return severity; }
    public long getDetectedAt() { return detectedAt; }
    public Map<String, Object> getAdditionalData() { return new HashMap<>(additionalData); }
    
    @Override
    public String toString() {
        return String.format("Discrepancy[%s]: %s - %s (%s)", 
                           type.getDisplayName(), description, severity.getDisplayName(), discrepancyId);
    }
}

/**
 * Represents a match between internal and external records
 */
class RecordMatch {
    private final PaymentRecord internalRecord;
    private final ExternalRecord externalRecord;
    private final double confidenceScore;
    private final long matchedAt;
    private final String matchReason;
    
    public RecordMatch(PaymentRecord internalRecord, ExternalRecord externalRecord, double confidenceScore) {
        this.internalRecord = internalRecord;
        this.externalRecord = externalRecord;
        this.confidenceScore = confidenceScore;
        this.matchedAt = System.currentTimeMillis();
        this.matchReason = "Automated matching";
    }
    
    public RecordMatch(PaymentRecord internalRecord, ExternalRecord externalRecord, 
                      double confidenceScore, String matchReason) {
        this.internalRecord = internalRecord;
        this.externalRecord = externalRecord;
        this.confidenceScore = confidenceScore;
        this.matchedAt = System.currentTimeMillis();
        this.matchReason = matchReason;
    }
    
    // Getters
    public PaymentRecord getInternalRecord() { return internalRecord; }
    public ExternalRecord getExternalRecord() { return externalRecord; }
    public double getConfidenceScore() { return confidenceScore; }
    public long getMatchedAt() { return matchedAt; }
    public String getMatchReason() { return matchReason; }
    
    @Override
    public String toString() {
        return String.format("Match[%.1f%%]: %s <-> %s", 
                           confidenceScore * 100, 
                           internalRecord.getTransactionId(), 
                           externalRecord.getReferenceId());
    }
}

/**
 * Represents a potential match candidate
 */
class MatchCandidate {
    private final ExternalRecord externalRecord;
    private final double confidence;
    private final String reason;
    
    public MatchCandidate(ExternalRecord externalRecord, double confidence, String reason) {
        this.externalRecord = externalRecord;
        this.confidence = confidence;
        this.reason = reason;
    }
    
    // Getters
    public ExternalRecord getExternalRecord() { return externalRecord; }
    public double getConfidence() { return confidence; }
    public String getReason() { return reason; }
    
    @Override
    public String toString() {
        return String.format("Candidate[%.1f%%]: %s - %s", 
                           confidence * 100, externalRecord.getReferenceId(), reason);
    }
}

/**
 * Represents the result of a matching operation
 */
class MatchResult {
    private final boolean matched;
    private final ExternalRecord matchedRecord;
    private final double confidenceScore;
    private final String message;
    
    public MatchResult(boolean matched, ExternalRecord matchedRecord, double confidenceScore, String message) {
        this.matched = matched;
        this.matchedRecord = matchedRecord;
        this.confidenceScore = confidenceScore;
        this.message = message;
    }
    
    // Getters
    public boolean isMatched() { return matched; }
    public ExternalRecord getMatchedRecord() { return matchedRecord; }
    public double getConfidenceScore() { return confidenceScore; }
    public String getMessage() { return message; }
}

/**
 * Represents a discrepancy resolution
 */
class DiscrepancyResolution {
    private final String resolutionId;
    private final Discrepancy discrepancy;
    private final ResolutionAction action;
    private final String resolution;
    private final boolean resolved;
    private final long resolvedAt;
    private final String resolvedBy;
    private final Map<String, Object> resolutionData;
    
    public DiscrepancyResolution(Discrepancy discrepancy, ResolutionAction action, 
                               String resolution, boolean resolved, String resolvedBy) {
        this.resolutionId = UUID.randomUUID().toString();
        this.discrepancy = discrepancy;
        this.action = action;
        this.resolution = resolution;
        this.resolved = resolved;
        this.resolvedAt = System.currentTimeMillis();
        this.resolvedBy = resolvedBy;
        this.resolutionData = new HashMap<>();
    }
    
    public void addResolutionData(String key, Object value) {
        resolutionData.put(key, value);
    }
    
    // Getters
    public String getResolutionId() { return resolutionId; }
    public Discrepancy getDiscrepancy() { return discrepancy; }
    public ResolutionAction getAction() { return action; }
    public String getResolution() { return resolution; }
    public boolean isResolved() { return resolved; }
    public long getResolvedAt() { return resolvedAt; }
    public String getResolvedBy() { return resolvedBy; }
    public Map<String, Object> getResolutionData() { return new HashMap<>(resolutionData); }
    
    @Override
    public String toString() {
        return String.format("Resolution[%s]: %s - %s", 
                           action.getDisplayName(), resolution, resolved ? "Resolved" : "Pending");
    }
}

/**
 * Reconciliation settings and configuration
 */
class ReconciliationSettings {
    private BigDecimal amountTolerance;
    private long dateTolerance;
    private double confidenceThreshold;
    private boolean autoResolveMinorDiscrepancies;
    private boolean requireManualApproval;
    private int maxProcessingThreads;
    private boolean enableMLMatching;
    private Map<String, Object> customSettings;
    
    public ReconciliationSettings() {
        this.amountTolerance = new BigDecimal("0.01");
        this.dateTolerance = 24 * 60 * 60 * 1000L; // 1 day
        this.confidenceThreshold = 0.8;
        this.autoResolveMinorDiscrepancies = true;
        this.requireManualApproval = false;
        this.maxProcessingThreads = 5;
        this.enableMLMatching = false;
        this.customSettings = new HashMap<>();
    }
    
    // Getters and setters
    public BigDecimal getAmountTolerance() { return amountTolerance; }
    public void setAmountTolerance(BigDecimal amountTolerance) { this.amountTolerance = amountTolerance; }
    
    public long getDateTolerance() { return dateTolerance; }
    public void setDateTolerance(long dateTolerance) { this.dateTolerance = dateTolerance; }
    
    public double getConfidenceThreshold() { return confidenceThreshold; }
    public void setConfidenceThreshold(double confidenceThreshold) { this.confidenceThreshold = confidenceThreshold; }
    
    public boolean isAutoResolveMinorDiscrepancies() { return autoResolveMinorDiscrepancies; }
    public void setAutoResolveMinorDiscrepancies(boolean autoResolveMinorDiscrepancies) { 
        this.autoResolveMinorDiscrepancies = autoResolveMinorDiscrepancies; 
    }
    
    public boolean isRequireManualApproval() { return requireManualApproval; }
    public void setRequireManualApproval(boolean requireManualApproval) { 
        this.requireManualApproval = requireManualApproval; 
    }
    
    public int getMaxProcessingThreads() { return maxProcessingThreads; }
    public void setMaxProcessingThreads(int maxProcessingThreads) { this.maxProcessingThreads = maxProcessingThreads; }
    
    public boolean isEnableMLMatching() { return enableMLMatching; }
    public void setEnableMLMatching(boolean enableMLMatching) { this.enableMLMatching = enableMLMatching; }
    
    public void setCustomSetting(String key, Object value) { customSettings.put(key, value); }
    public Object getCustomSetting(String key) { return customSettings.get(key); }
}

/**
 * Reconciliation metrics and statistics
 */
class ReconciliationMetrics {
    private int totalReconciliationRuns;
    private int totalInternalRecords;
    private int totalExternalRecords;
    private int totalMatches;
    private int totalDiscrepancies;
    private int resolvedDiscrepancies;
    private int unresolvedDiscrepancies;
    private double averageMatchRate;
    private double averageResolutionRate;
    private long totalProcessingTime;
    private long averageProcessingTime;
    private final Map<DiscrepancyType, Integer> discrepancyTypeCounts;
    private final Map<ResolutionAction, Integer> resolutionActionCounts;
    
    public ReconciliationMetrics() {
        this.discrepancyTypeCounts = new HashMap<>();
        this.resolutionActionCounts = new HashMap<>();
        resetMetrics();
    }
    
    public void resetMetrics() {
        this.totalReconciliationRuns = 0;
        this.totalInternalRecords = 0;
        this.totalExternalRecords = 0;
        this.totalMatches = 0;
        this.totalDiscrepancies = 0;
        this.resolvedDiscrepancies = 0;
        this.unresolvedDiscrepancies = 0;
        this.averageMatchRate = 0.0;
        this.averageResolutionRate = 0.0;
        this.totalProcessingTime = 0;
        this.averageProcessingTime = 0;
        this.discrepancyTypeCounts.clear();
        this.resolutionActionCounts.clear();
    }
    
    public void recordReconciliationRun() { totalReconciliationRuns++; }
    public void recordInternalRecordsIngested(int count) { totalInternalRecords += count; }
    public void recordExternalRecordsIngested(int count) { totalExternalRecords += count; }
    public void recordMatches(int count) { totalMatches += count; }
    public void recordDiscrepancies(int count) { totalDiscrepancies += count; }
    public void incrementResolvedDiscrepancies() { resolvedDiscrepancies++; }
    public void incrementUnresolvedDiscrepancies() { unresolvedDiscrepancies++; }
    
    public void updateMatchRate(double matchRate) {
        averageMatchRate = (averageMatchRate * (totalReconciliationRuns - 1) + matchRate) / totalReconciliationRuns;
    }
    
    public void updateResolutionRate(double resolutionRate) {
        averageResolutionRate = (averageResolutionRate * (totalReconciliationRuns - 1) + resolutionRate) / totalReconciliationRuns;
    }
    
    public void recordProcessingTime(long processingTime) {
        totalProcessingTime += processingTime;
        averageProcessingTime = totalProcessingTime / totalReconciliationRuns;
    }
    
    public void recordDiscrepancyType(DiscrepancyType type) {
        discrepancyTypeCounts.merge(type, 1, Integer::sum);
    }
    
    public void recordResolutionAction(ResolutionAction action) {
        resolutionActionCounts.merge(action, 1, Integer::sum);
    }
    
    // Getters
    public int getTotalReconciliationRuns() { return totalReconciliationRuns; }
    public int getTotalInternalRecords() { return totalInternalRecords; }
    public int getTotalExternalRecords() { return totalExternalRecords; }
    public int getTotalMatches() { return totalMatches; }
    public int getTotalDiscrepancies() { return totalDiscrepancies; }
    public int getResolvedDiscrepancies() { return resolvedDiscrepancies; }
    public int getUnresolvedDiscrepancies() { return unresolvedDiscrepancies; }
    public double getAverageMatchRate() { return averageMatchRate; }
    public double getAverageResolutionRate() { return averageResolutionRate; }
    public long getTotalProcessingTime() { return totalProcessingTime; }
    public long getAverageProcessingTime() { return averageProcessingTime; }
    public Map<DiscrepancyType, Integer> getDiscrepancyTypeCounts() { return new HashMap<>(discrepancyTypeCounts); }
    public Map<ResolutionAction, Integer> getResolutionActionCounts() { return new HashMap<>(resolutionActionCounts); }
    
    @Override
    public String toString() {
        return String.format("ReconciliationMetrics[Runs: %d, Match Rate: %.1f%%, Resolution Rate: %.1f%%, Avg Time: %dms]",
                           totalReconciliationRuns, averageMatchRate, averageResolutionRate, averageProcessingTime);
    }
}

/**
 * Event listener interface for reconciliation events
 */
interface ReconciliationEventListener {
    void onReconciliationEvent(String engineId, String message);
    void onReconciliationStarted(String engineId);
    void onReconciliationCompleted(String engineId, ReconciliationSummary summary);
    void onDiscrepancyDetected(String engineId, Discrepancy discrepancy);
    void onDiscrepancyResolved(String engineId, DiscrepancyResolution resolution);
    void onMatchFound(String engineId, RecordMatch match);
}

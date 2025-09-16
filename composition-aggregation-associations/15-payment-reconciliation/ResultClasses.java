package composition.reconciliation;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Result classes for the Payment Reconciliation System
 */

/**
 * Result of data ingestion operation
 */
class DataIngestionResult {
    private final boolean success;
    private final String message;
    private final int recordsIngested;
    private final long timestamp;
    
    public DataIngestionResult(boolean success, String message, int recordsIngested) {
        this.success = success;
        this.message = message;
        this.recordsIngested = recordsIngested;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getRecordsIngested() { return recordsIngested; }
    public long getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("DataIngestionResult[%s]: %s (%d records)", 
                           success ? "SUCCESS" : "FAILED", message, recordsIngested);
    }
}

/**
 * Result of reconciliation process initiation
 */
class ReconciliationProcessResult {
    private final boolean success;
    private final String message;
    private final CompletableFuture<ReconciliationSummary> reconciliationFuture;
    private final long timestamp;
    
    public ReconciliationProcessResult(boolean success, String message, 
                                     CompletableFuture<ReconciliationSummary> reconciliationFuture) {
        this.success = success;
        this.message = message;
        this.reconciliationFuture = reconciliationFuture;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public CompletableFuture<ReconciliationSummary> getReconciliationFuture() { return reconciliationFuture; }
    public long getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("ReconciliationProcessResult[%s]: %s", 
                           success ? "SUCCESS" : "FAILED", message);
    }
}

/**
 * Result of matching operation
 */
class MatchingResult {
    private final List<RecordMatch> matches;
    private final List<PaymentRecord> unmatchedInternal;
    private final List<ExternalRecord> unmatchedExternal;
    private final long timestamp;
    
    public MatchingResult(List<RecordMatch> matches, List<PaymentRecord> unmatchedInternal, 
                         List<ExternalRecord> unmatchedExternal) {
        this.matches = new ArrayList<>(matches);
        this.unmatchedInternal = new ArrayList<>(unmatchedInternal);
        this.unmatchedExternal = new ArrayList<>(unmatchedExternal);
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public List<RecordMatch> getMatches() { return new ArrayList<>(matches); }
    public List<PaymentRecord> getUnmatchedInternal() { return new ArrayList<>(unmatchedInternal); }
    public List<ExternalRecord> getUnmatchedExternal() { return new ArrayList<>(unmatchedExternal); }
    public long getTimestamp() { return timestamp; }
    
    public int getTotalMatches() { return matches.size(); }
    public int getTotalUnmatchedInternal() { return unmatchedInternal.size(); }
    public int getTotalUnmatchedExternal() { return unmatchedExternal.size(); }
    
    @Override
    public String toString() {
        return String.format("MatchingResult[Matches: %d, Unmatched Internal: %d, Unmatched External: %d]",
                           matches.size(), unmatchedInternal.size(), unmatchedExternal.size());
    }
}

/**
 * Result of discrepancy analysis
 */
class DiscrepancyAnalysisResult {
    private final List<Discrepancy> discrepancies;
    private final long timestamp;
    
    public DiscrepancyAnalysisResult(List<Discrepancy> discrepancies) {
        this.discrepancies = new ArrayList<>(discrepancies);
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public List<Discrepancy> getDiscrepancies() { return new ArrayList<>(discrepancies); }
    public long getTimestamp() { return timestamp; }
    
    public int getTotalDiscrepancies() { return discrepancies.size(); }
    
    public Map<DiscrepancyType, Integer> getDiscrepancyTypeCounts() {
        Map<DiscrepancyType, Integer> counts = new HashMap<>();
        for (Discrepancy discrepancy : discrepancies) {
            counts.merge(discrepancy.getType(), 1, Integer::sum);
        }
        return counts;
    }
    
    public Map<DiscrepancySeverity, Integer> getSeverityCounts() {
        Map<DiscrepancySeverity, Integer> counts = new HashMap<>();
        for (Discrepancy discrepancy : discrepancies) {
            counts.merge(discrepancy.getSeverity(), 1, Integer::sum);
        }
        return counts;
    }
    
    @Override
    public String toString() {
        return String.format("DiscrepancyAnalysisResult[Total: %d discrepancies]", discrepancies.size());
    }
}

/**
 * Result of resolution process
 */
class ResolutionResult {
    private final List<DiscrepancyResolution> resolutions;
    private final long timestamp;
    
    public ResolutionResult(List<DiscrepancyResolution> resolutions) {
        this.resolutions = new ArrayList<>(resolutions);
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public List<DiscrepancyResolution> getResolutions() { return new ArrayList<>(resolutions); }
    public long getTimestamp() { return timestamp; }
    
    public int getTotalResolutions() { return resolutions.size(); }
    
    public int getResolvedCount() {
        return (int) resolutions.stream().mapToLong(r -> r.isResolved() ? 1 : 0).sum();
    }
    
    public int getUnresolvedCount() {
        return getTotalResolutions() - getResolvedCount();
    }
    
    public Map<ResolutionAction, Integer> getActionCounts() {
        Map<ResolutionAction, Integer> counts = new HashMap<>();
        for (DiscrepancyResolution resolution : resolutions) {
            counts.merge(resolution.getAction(), 1, Integer::sum);
        }
        return counts;
    }
    
    @Override
    public String toString() {
        return String.format("ResolutionResult[Total: %d, Resolved: %d, Unresolved: %d]",
                           getTotalResolutions(), getResolvedCount(), getUnresolvedCount());
    }
}

/**
 * Comprehensive reconciliation summary
 */
class ReconciliationSummary {
    private final String engineId;
    private final long timestamp;
    private final int totalInternal;
    private final int totalExternal;
    private final int matchedRecords;
    private final int totalDiscrepancies;
    private final int resolvedDiscrepancies;
    private final double matchRate;
    private final double resolutionRate;
    private final MatchingResult matchingResult;
    private final DiscrepancyAnalysisResult discrepancyResult;
    private final ResolutionResult resolutionResult;
    
    public ReconciliationSummary(String engineId, long timestamp, int totalInternal, int totalExternal,
                               int matchedRecords, int totalDiscrepancies, int resolvedDiscrepancies,
                               double matchRate, double resolutionRate, MatchingResult matchingResult,
                               DiscrepancyAnalysisResult discrepancyResult, ResolutionResult resolutionResult) {
        this.engineId = engineId;
        this.timestamp = timestamp;
        this.totalInternal = totalInternal;
        this.totalExternal = totalExternal;
        this.matchedRecords = matchedRecords;
        this.totalDiscrepancies = totalDiscrepancies;
        this.resolvedDiscrepancies = resolvedDiscrepancies;
        this.matchRate = matchRate;
        this.resolutionRate = resolutionRate;
        this.matchingResult = matchingResult;
        this.discrepancyResult = discrepancyResult;
        this.resolutionResult = resolutionResult;
    }
    
    // Getters
    public String getEngineId() { return engineId; }
    public long getTimestamp() { return timestamp; }
    public int getTotalInternal() { return totalInternal; }
    public int getTotalExternal() { return totalExternal; }
    public int getMatchedRecords() { return matchedRecords; }
    public int getTotalDiscrepancies() { return totalDiscrepancies; }
    public int getResolvedDiscrepancies() { return resolvedDiscrepancies; }
    public double getMatchRate() { return matchRate; }
    public double getResolutionRate() { return resolutionRate; }
    public MatchingResult getMatchingResult() { return matchingResult; }
    public DiscrepancyAnalysisResult getDiscrepancyResult() { return discrepancyResult; }
    public ResolutionResult getResolutionResult() { return resolutionResult; }
    
    @Override
    public String toString() {
        return String.format("ReconciliationSummary[Engine: %s, Matches: %d/%d (%.1f%%), Discrepancies: %d, Resolved: %d (%.1f%%)]",
                           engineId, matchedRecords, totalInternal, matchRate, 
                           totalDiscrepancies, resolvedDiscrepancies, resolutionRate);
    }
}

/**
 * Reconciliation result containing summary and metadata
 */
class ReconciliationResult {
    private final String resultId;
    private final ReconciliationSummary summary;
    private final Map<String, Object> metadata;
    private final long completedAt;
    
    public ReconciliationResult(ReconciliationSummary summary) {
        this.resultId = UUID.randomUUID().toString();
        this.summary = summary;
        this.metadata = new HashMap<>();
        this.completedAt = System.currentTimeMillis();
    }
    
    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }
    
    // Getters
    public String getResultId() { return resultId; }
    public ReconciliationSummary getSummary() { return summary; }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    public long getCompletedAt() { return completedAt; }
    
    @Override
    public String toString() {
        return String.format("ReconciliationResult[%s]: %s", resultId, summary);
    }
}

/**
 * Reconciliation report
 */
class ReconciliationReport {
    private final String reportId;
    private final ReportType reportType;
    private final String generatedBy;
    private final long generatedAt;
    private final Map<String, Object> summaryStatistics;
    private final List<ReportSection> sections;
    
    public ReconciliationReport(ReportType reportType, String generatedBy) {
        this.reportId = UUID.randomUUID().toString();
        this.reportType = reportType;
        this.generatedBy = generatedBy;
        this.generatedAt = System.currentTimeMillis();
        this.summaryStatistics = new HashMap<>();
        this.sections = new ArrayList<>();
    }
    
    public void addSummaryStatistic(String key, Object value) {
        summaryStatistics.put(key, value);
    }
    
    public void addSection(ReportSection section) {
        sections.add(section);
    }
    
    // Getters
    public String getReportId() { return reportId; }
    public ReportType getReportType() { return reportType; }
    public String getGeneratedBy() { return generatedBy; }
    public long getGeneratedAt() { return generatedAt; }
    public Map<String, Object> getSummaryStatistics() { return new HashMap<>(summaryStatistics); }
    public List<ReportSection> getSections() { return new ArrayList<>(sections); }
    
    @Override
    public String toString() {
        return String.format("ReconciliationReport[%s]: %s generated by %s", 
                           reportType.getDisplayName(), reportId, generatedBy);
    }
}

/**
 * Report section
 */
class ReportSection {
    private final String title;
    private final String content;
    private final long createdAt;
    
    public ReportSection(String title, String content) {
        this.title = title;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
    }
    
    // Getters
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public long getCreatedAt() { return createdAt; }
    
    @Override
    public String toString() {
        return String.format("ReportSection[%s]: %d characters", title, content.length());
    }
}

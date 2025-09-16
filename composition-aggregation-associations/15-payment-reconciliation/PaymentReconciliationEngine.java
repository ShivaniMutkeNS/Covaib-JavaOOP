package composition.reconciliation;

import java.util.*;
import java.util.concurrent.*;

/**
 * MAANG-Level Payment Reconciliation System using Composition
 * Demonstrates: Strategy Pattern, Observer Pattern, State Pattern, Command Pattern
 */
public class PaymentReconciliationEngine {
    private final String engineId;
    private final Map<String, PaymentRecord> internalRecords;
    private final Map<String, ExternalRecord> externalRecords;
    private final Map<String, ReconciliationResult> reconciliationResults;
    private final List<ReconciliationEventListener> listeners;
    private final ReconciliationSettings settings;
    private final ReconciliationMetrics metrics;
    private final ExecutorService processingExecutor;
    private ReconciliationStrategy reconciliationStrategy;
    private MatchingStrategy matchingStrategy;
    private DiscrepancyResolutionStrategy discrepancyStrategy;
    private ReportingStrategy reportingStrategy;
    private ReconciliationState state;
    private final long createdAt;
    private long lastReconciliationTime;
    
    public PaymentReconciliationEngine(String engineId) {
        this.engineId = engineId;
        this.internalRecords = new ConcurrentHashMap<>();
        this.externalRecords = new ConcurrentHashMap<>();
        this.reconciliationResults = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();
        this.settings = new ReconciliationSettings();
        this.metrics = new ReconciliationMetrics();
        this.processingExecutor = Executors.newFixedThreadPool(5);
        this.reconciliationStrategy = new StandardReconciliationStrategy();
        this.matchingStrategy = new ExactMatchingStrategy();
        this.discrepancyStrategy = new AutomaticDiscrepancyResolutionStrategy();
        this.reportingStrategy = new DetailedReportingStrategy();
        this.state = ReconciliationState.IDLE;
        this.createdAt = System.currentTimeMillis();
        this.lastReconciliationTime = 0;
    }
    
    // Runtime strategy swapping - core composition flexibility
    public void setReconciliationStrategy(ReconciliationStrategy strategy) {
        this.reconciliationStrategy = strategy;
        notifyListeners("Reconciliation strategy updated: " + strategy.getStrategyName());
    }
    
    public void setMatchingStrategy(MatchingStrategy strategy) {
        this.matchingStrategy = strategy;
        notifyListeners("Matching strategy updated: " + strategy.getStrategyName());
    }
    
    public void setDiscrepancyResolutionStrategy(DiscrepancyResolutionStrategy strategy) {
        this.discrepancyStrategy = strategy;
        notifyListeners("Discrepancy resolution strategy updated: " + strategy.getStrategyName());
    }
    
    public void setReportingStrategy(ReportingStrategy strategy) {
        this.reportingStrategy = strategy;
        notifyListeners("Reporting strategy updated: " + strategy.getStrategyName());
    }
    
    // Data ingestion
    public DataIngestionResult ingestInternalRecords(List<PaymentRecord> records) {
        if (state == ReconciliationState.PROCESSING) {
            return new DataIngestionResult(false, "Cannot ingest data during processing", 0);
        }
        
        int ingestedCount = 0;
        for (PaymentRecord record : records) {
            if (validatePaymentRecord(record)) {
                internalRecords.put(record.getTransactionId(), record);
                ingestedCount++;
            }
        }
        
        metrics.recordInternalRecordsIngested(ingestedCount);
        notifyListeners("Ingested " + ingestedCount + " internal payment records");
        
        return new DataIngestionResult(true, "Internal records ingested successfully", ingestedCount);
    }
    
    public DataIngestionResult ingestExternalRecords(List<ExternalRecord> records) {
        if (state == ReconciliationState.PROCESSING) {
            return new DataIngestionResult(false, "Cannot ingest data during processing", 0);
        }
        
        int ingestedCount = 0;
        for (ExternalRecord record : records) {
            if (validateExternalRecord(record)) {
                externalRecords.put(record.getReferenceId(), record);
                ingestedCount++;
            }
        }
        
        metrics.recordExternalRecordsIngested(ingestedCount);
        notifyListeners("Ingested " + ingestedCount + " external records");
        
        return new DataIngestionResult(true, "External records ingested successfully", ingestedCount);
    }
    
    // Main reconciliation process
    public ReconciliationProcessResult startReconciliation() {
        if (state == ReconciliationState.PROCESSING) {
            return new ReconciliationProcessResult(false, "Reconciliation already in progress", null);
        }
        
        if (internalRecords.isEmpty() || externalRecords.isEmpty()) {
            return new ReconciliationProcessResult(false, "Insufficient data for reconciliation", null);
        }
        
        state = ReconciliationState.PROCESSING;
        lastReconciliationTime = System.currentTimeMillis();
        
        CompletableFuture<ReconciliationSummary> reconciliationFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return performReconciliation();
            } catch (Exception e) {
                notifyListeners("Reconciliation failed: " + e.getMessage());
                return null;
            } finally {
                state = ReconciliationState.COMPLETED;
            }
        }, processingExecutor);
        
        notifyListeners("Reconciliation process started");
        
        return new ReconciliationProcessResult(true, "Reconciliation started successfully", reconciliationFuture);
    }
    
    private ReconciliationSummary performReconciliation() {
        notifyListeners("Starting reconciliation process");
        
        // Phase 1: Matching
        MatchingResult matchingResult = performMatching();
        
        // Phase 2: Discrepancy identification
        DiscrepancyAnalysisResult discrepancyResult = analyzeDiscrepancies(matchingResult);
        
        // Phase 3: Resolution attempts
        ResolutionResult resolutionResult = resolveDiscrepancies(discrepancyResult);
        
        // Phase 4: Generate summary
        ReconciliationSummary summary = generateSummary(matchingResult, discrepancyResult, resolutionResult);
        
        // Update metrics
        updateMetrics(summary);
        
        notifyListeners("Reconciliation process completed");
        
        return summary;
    }
    
    private MatchingResult performMatching() {
        notifyListeners("Performing record matching");
        
        List<RecordMatch> matches = new ArrayList<>();
        List<PaymentRecord> unmatchedInternal = new ArrayList<>();
        List<ExternalRecord> unmatchedExternal = new ArrayList<>(externalRecords.values());
        
        for (PaymentRecord internal : internalRecords.values()) {
            MatchResult matchResult = matchingStrategy.findMatch(internal, externalRecords.values());
            
            if (matchResult.isMatched()) {
                ExternalRecord external = matchResult.getMatchedRecord();
                matches.add(new RecordMatch(internal, external, matchResult.getConfidenceScore()));
                unmatchedExternal.remove(external);
            } else {
                unmatchedInternal.add(internal);
            }
        }
        
        return new MatchingResult(matches, unmatchedInternal, unmatchedExternal);
    }
    
    private DiscrepancyAnalysisResult analyzeDiscrepancies(MatchingResult matchingResult) {
        notifyListeners("Analyzing discrepancies");
        
        List<Discrepancy> discrepancies = new ArrayList<>();
        
        // Analyze matched records for discrepancies
        for (RecordMatch match : matchingResult.getMatches()) {
            List<Discrepancy> matchDiscrepancies = reconciliationStrategy.identifyDiscrepancies(
                match.getInternalRecord(), match.getExternalRecord());
            discrepancies.addAll(matchDiscrepancies);
        }
        
        // Create discrepancies for unmatched records
        for (PaymentRecord unmatched : matchingResult.getUnmatchedInternal()) {
            discrepancies.add(new Discrepancy(DiscrepancyType.MISSING_EXTERNAL, 
                "No external record found", unmatched, null, DiscrepancySeverity.HIGH));
        }
        
        for (ExternalRecord unmatched : matchingResult.getUnmatchedExternal()) {
            discrepancies.add(new Discrepancy(DiscrepancyType.MISSING_INTERNAL, 
                "No internal record found", null, unmatched, DiscrepancySeverity.HIGH));
        }
        
        return new DiscrepancyAnalysisResult(discrepancies);
    }
    
    private ResolutionResult resolveDiscrepancies(DiscrepancyAnalysisResult discrepancyResult) {
        notifyListeners("Resolving discrepancies");
        
        List<DiscrepancyResolution> resolutions = new ArrayList<>();
        
        for (Discrepancy discrepancy : discrepancyResult.getDiscrepancies()) {
            DiscrepancyResolution resolution = discrepancyStrategy.resolveDiscrepancy(discrepancy);
            resolutions.add(resolution);
            
            if (resolution.isResolved()) {
                metrics.incrementResolvedDiscrepancies();
            } else {
                metrics.incrementUnresolvedDiscrepancies();
            }
        }
        
        return new ResolutionResult(resolutions);
    }
    
    private ReconciliationSummary generateSummary(MatchingResult matchingResult, 
                                                 DiscrepancyAnalysisResult discrepancyResult, 
                                                 ResolutionResult resolutionResult) {
        
        int totalInternal = internalRecords.size();
        int totalExternal = externalRecords.size();
        int matchedRecords = matchingResult.getMatches().size();
        int totalDiscrepancies = discrepancyResult.getDiscrepancies().size();
        int resolvedDiscrepancies = (int) resolutionResult.getResolutions().stream()
                .mapToLong(r -> r.isResolved() ? 1 : 0).sum();
        
        double matchRate = totalInternal > 0 ? (double) matchedRecords / totalInternal * 100 : 0;
        double resolutionRate = totalDiscrepancies > 0 ? (double) resolvedDiscrepancies / totalDiscrepancies * 100 : 0;
        
        return new ReconciliationSummary(
            engineId, System.currentTimeMillis(), totalInternal, totalExternal,
            matchedRecords, totalDiscrepancies, resolvedDiscrepancies,
            matchRate, resolutionRate, matchingResult, discrepancyResult, resolutionResult
        );
    }
    
    private void updateMetrics(ReconciliationSummary summary) {
        metrics.recordReconciliationRun();
        metrics.updateMatchRate(summary.getMatchRate());
        metrics.updateResolutionRate(summary.getResolutionRate());
        metrics.recordProcessingTime(System.currentTimeMillis() - lastReconciliationTime);
    }
    
    // Reporting and analysis
    public ReconciliationReport generateReport(ReportType reportType) {
        return reportingStrategy.generateReport(reconciliationResults.values(), reportType, settings);
    }
    
    public List<Discrepancy> getUnresolvedDiscrepancies() {
        return reconciliationResults.values().stream()
                .flatMap(result -> result.getSummary().getDiscrepancyResult().getDiscrepancies().stream())
                .filter(d -> !isDiscrepancyResolved(d))
                .collect(Collectors.toList());
    }
    
    public ReconciliationMetrics getMetrics() {
        return metrics;
    }
    
    // Data validation
    private boolean validatePaymentRecord(PaymentRecord record) {
        return record != null && 
               record.getTransactionId() != null && 
               !record.getTransactionId().trim().isEmpty() &&
               record.getAmount() != null &&
               record.getAmount().compareTo(BigDecimal.ZERO) > 0;
    }
    
    private boolean validateExternalRecord(ExternalRecord record) {
        return record != null && 
               record.getReferenceId() != null && 
               !record.getReferenceId().trim().isEmpty() &&
               record.getAmount() != null &&
               record.getAmount().compareTo(BigDecimal.ZERO) > 0;
    }
    
    private boolean isDiscrepancyResolved(Discrepancy discrepancy) {
        // Check if discrepancy has been resolved in any reconciliation result
        return reconciliationResults.values().stream()
                .flatMap(result -> result.getSummary().getResolutionResult().getResolutions().stream())
                .anyMatch(resolution -> resolution.getDiscrepancy().equals(discrepancy) && resolution.isResolved());
    }
    
    // Event handling
    public void addEventListener(ReconciliationEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeEventListener(ReconciliationEventListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners(String message) {
        for (ReconciliationEventListener listener : listeners) {
            processingExecutor.submit(() -> listener.onReconciliationEvent(engineId, message));
        }
    }
    
    // Lifecycle management
    public void shutdown() {
        processingExecutor.shutdown();
        try {
            if (!processingExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                processingExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            processingExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        notifyListeners("Reconciliation engine shutdown");
    }
    
    // Getters
    public String getEngineId() { return engineId; }
    public ReconciliationState getState() { return state; }
    public ReconciliationSettings getSettings() { return settings; }
    public int getInternalRecordCount() { return internalRecords.size(); }
    public int getExternalRecordCount() { return externalRecords.size(); }
    public long getCreatedAt() { return createdAt; }
    public long getLastReconciliationTime() { return lastReconciliationTime; }
}

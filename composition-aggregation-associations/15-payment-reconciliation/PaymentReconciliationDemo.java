package composition.reconciliation;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Comprehensive demo for the Payment Reconciliation System
 * Demonstrates: Strategy Pattern, Observer Pattern, State Pattern, Command Pattern
 */
public class PaymentReconciliationDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Payment Reconciliation System Demo ===\n");
        
        // Create reconciliation engine
        PaymentReconciliationEngine engine = new PaymentReconciliationEngine("RECON_ENGINE_001");
        
        // Add event listener
        ReconciliationEventListener logger = new ReconciliationEventLogger();
        engine.addEventListener(logger);
        
        System.out.println("1. ENGINE INITIALIZATION");
        System.out.println("========================");
        System.out.printf("Created reconciliation engine: %s\n", engine.getEngineId());
        System.out.printf("Initial state: %s\n", engine.getState().getDisplayName());
        System.out.printf("Settings: Amount tolerance = %s, Confidence threshold = %.1f\n", 
                         engine.getSettings().getAmountTolerance(), 
                         engine.getSettings().getConfidenceThreshold());
        
        System.out.println("\n2. DATA PREPARATION");
        System.out.println("===================");
        
        // Create sample internal payment records
        List<PaymentRecord> internalRecords = createSampleInternalRecords();
        System.out.printf("Created %d internal payment records\n", internalRecords.size());
        
        // Create sample external records (bank statements)
        List<ExternalRecord> externalRecords = createSampleExternalRecords();
        System.out.printf("Created %d external records\n", externalRecords.size());
        
        System.out.println("\n3. DATA INGESTION");
        System.out.println("==================");
        
        // Ingest internal records
        DataIngestionResult internalIngestion = engine.ingestInternalRecords(internalRecords);
        System.out.println("Internal records ingestion: " + internalIngestion.getMessage());
        System.out.printf("Records ingested: %d\n", internalIngestion.getRecordsIngested());
        
        // Ingest external records
        DataIngestionResult externalIngestion = engine.ingestExternalRecords(externalRecords);
        System.out.println("External records ingestion: " + externalIngestion.getMessage());
        System.out.printf("Records ingested: %d\n", externalIngestion.getRecordsIngested());
        
        System.out.printf("Engine now contains: %d internal, %d external records\n", 
                         engine.getInternalRecordCount(), engine.getExternalRecordCount());
        
        System.out.println("\n4. STRATEGY CONFIGURATION");
        System.out.println("==========================");
        
        // Test different reconciliation strategies
        System.out.println("Testing Standard Reconciliation Strategy:");
        testReconciliationWithStrategy(engine, new StandardReconciliationStrategy(), 
                                     new FuzzyMatchingStrategy(), 
                                     new AutomaticDiscrepancyResolutionStrategy());
        
        System.out.println("\nSwitching to Strict Reconciliation Strategy:");
        testReconciliationWithStrategy(engine, new StrictReconciliationStrategy(), 
                                     new ExactMatchingStrategy(), 
                                     new ManualReviewDiscrepancyResolutionStrategy());
        
        System.out.println("\nSwitching to Flexible Reconciliation Strategy:");
        testReconciliationWithStrategy(engine, new FlexibleReconciliationStrategy(), 
                                     new MLMatchingStrategy(), 
                                     new RuleBasedDiscrepancyResolutionStrategy());
        
        System.out.println("\n5. COMPREHENSIVE RECONCILIATION");
        System.out.println("================================");
        
        // Perform comprehensive reconciliation
        performComprehensiveReconciliation(engine);
        
        System.out.println("\n6. REPORTING AND ANALYTICS");
        System.out.println("===========================");
        
        // Generate different types of reports
        generateReports(engine);
        
        System.out.println("\n7. METRICS AND PERFORMANCE");
        System.out.println("===========================");
        
        // Display metrics
        displayMetrics(engine);
        
        System.out.println("\n8. ADVANCED FEATURES");
        System.out.println("====================");
        
        // Test advanced features
        testAdvancedFeatures(engine);
        
        System.out.println("\n9. ERROR HANDLING AND EDGE CASES");
        System.out.println("=================================");
        
        // Test error scenarios
        testErrorScenarios(engine);
        
        System.out.println("\n10. CLEANUP");
        System.out.println("===========");
        
        // Shutdown engine
        engine.shutdown();
        System.out.println("Reconciliation engine shutdown complete");
        
        System.out.println("\n=== Demo Complete ===");
    }
    
    private static void testReconciliationWithStrategy(PaymentReconciliationEngine engine,
                                                     ReconciliationStrategy reconciliationStrategy,
                                                     MatchingStrategy matchingStrategy,
                                                     DiscrepancyResolutionStrategy resolutionStrategy) {
        
        // Set strategies
        engine.setReconciliationStrategy(reconciliationStrategy);
        engine.setMatchingStrategy(matchingStrategy);
        engine.setDiscrepancyResolutionStrategy(resolutionStrategy);
        
        System.out.printf("  Reconciliation: %s\n", reconciliationStrategy.getStrategyName());
        System.out.printf("  Matching: %s\n", matchingStrategy.getStrategyName());
        System.out.printf("  Resolution: %s\n", resolutionStrategy.getStrategyName());
        
        // Start reconciliation
        ReconciliationProcessResult result = engine.startReconciliation();
        if (result.isSuccess()) {
            try {
                ReconciliationSummary summary = result.getReconciliationFuture().get(10, TimeUnit.SECONDS);
                System.out.printf("  Result: %d matches (%.1f%%), %d discrepancies (%.1f%% resolved)\n",
                                 summary.getMatchedRecords(), summary.getMatchRate(),
                                 summary.getTotalDiscrepancies(), summary.getResolutionRate());
            } catch (Exception e) {
                System.out.println("  Error waiting for reconciliation: " + e.getMessage());
            }
        } else {
            System.out.println("  Failed to start: " + result.getMessage());
        }
    }
    
    private static void performComprehensiveReconciliation(PaymentReconciliationEngine engine) {
        // Configure for comprehensive analysis
        engine.setReconciliationStrategy(new StandardReconciliationStrategy());
        engine.setMatchingStrategy(new FuzzyMatchingStrategy());
        engine.setDiscrepancyResolutionStrategy(new AutomaticDiscrepancyResolutionStrategy());
        engine.setReportingStrategy(new DetailedReportingStrategy());
        
        // Adjust settings for thorough analysis
        ReconciliationSettings settings = engine.getSettings();
        settings.setAmountTolerance(new BigDecimal("0.50"));
        settings.setConfidenceThreshold(0.75);
        settings.setAutoResolveMinorDiscrepancies(true);
        
        System.out.println("Starting comprehensive reconciliation...");
        
        ReconciliationProcessResult result = engine.startReconciliation();
        if (result.isSuccess()) {
            try {
                ReconciliationSummary summary = result.getReconciliationFuture().get(15, TimeUnit.SECONDS);
                
                System.out.println("\nReconciliation Summary:");
                System.out.println("======================");
                System.out.printf("Engine ID: %s\n", summary.getEngineId());
                System.out.printf("Timestamp: %s\n", new Date(summary.getTimestamp()));
                System.out.printf("Internal Records: %d\n", summary.getTotalInternal());
                System.out.printf("External Records: %d\n", summary.getTotalExternal());
                System.out.printf("Successful Matches: %d (%.1f%%)\n", 
                                 summary.getMatchedRecords(), summary.getMatchRate());
                System.out.printf("Total Discrepancies: %d\n", summary.getTotalDiscrepancies());
                System.out.printf("Resolved Discrepancies: %d (%.1f%%)\n", 
                                 summary.getResolvedDiscrepancies(), summary.getResolutionRate());
                
                // Display match details
                displayMatchDetails(summary.getMatchingResult());
                
                // Display discrepancy details
                displayDiscrepancyDetails(summary.getDiscrepancyResult());
                
                // Display resolution details
                displayResolutionDetails(summary.getResolutionResult());
                
            } catch (Exception e) {
                System.out.println("Error during comprehensive reconciliation: " + e.getMessage());
            }
        } else {
            System.out.println("Failed to start comprehensive reconciliation: " + result.getMessage());
        }
    }
    
    private static void displayMatchDetails(MatchingResult matchingResult) {
        System.out.println("\nMatch Details:");
        System.out.println("--------------");
        
        List<RecordMatch> matches = matchingResult.getMatches();
        System.out.printf("Total Matches: %d\n", matches.size());
        
        // Show top 5 matches
        matches.stream()
                .sorted((a, b) -> Double.compare(b.getConfidenceScore(), a.getConfidenceScore()))
                .limit(5)
                .forEach(match -> {
                    System.out.printf("  Match (%.1f%% confidence): %s <-> %s\n",
                                     match.getConfidenceScore() * 100,
                                     match.getInternalRecord().getTransactionId(),
                                     match.getExternalRecord().getReferenceId());
                });
        
        System.out.printf("Unmatched Internal: %d\n", matchingResult.getTotalUnmatchedInternal());
        System.out.printf("Unmatched External: %d\n", matchingResult.getTotalUnmatchedExternal());
    }
    
    private static void displayDiscrepancyDetails(DiscrepancyAnalysisResult discrepancyResult) {
        System.out.println("\nDiscrepancy Analysis:");
        System.out.println("--------------------");
        
        List<Discrepancy> discrepancies = discrepancyResult.getDiscrepancies();
        System.out.printf("Total Discrepancies: %d\n", discrepancies.size());
        
        // Group by type
        Map<DiscrepancyType, Integer> typeCounts = discrepancyResult.getDiscrepancyTypeCounts();
        System.out.println("By Type:");
        typeCounts.forEach((type, count) -> 
                System.out.printf("  %s: %d\n", type.getDisplayName(), count));
        
        // Group by severity
        Map<DiscrepancySeverity, Integer> severityCounts = discrepancyResult.getSeverityCounts();
        System.out.println("By Severity:");
        severityCounts.forEach((severity, count) -> 
                System.out.printf("  %s: %d\n", severity.getDisplayName(), count));
    }
    
    private static void displayResolutionDetails(ResolutionResult resolutionResult) {
        System.out.println("\nResolution Summary:");
        System.out.println("------------------");
        
        System.out.printf("Total Resolutions: %d\n", resolutionResult.getTotalResolutions());
        System.out.printf("Resolved: %d\n", resolutionResult.getResolvedCount());
        System.out.printf("Unresolved: %d\n", resolutionResult.getUnresolvedCount());
        
        // Group by action
        Map<ResolutionAction, Integer> actionCounts = resolutionResult.getActionCounts();
        System.out.println("By Action:");
        actionCounts.forEach((action, count) -> 
                System.out.printf("  %s: %d\n", action.getDisplayName(), count));
    }
    
    private static void generateReports(PaymentReconciliationEngine engine) {
        // Generate different types of reports
        System.out.println("Generating Summary Report:");
        ReconciliationReport summaryReport = engine.generateReport(ReportType.SUMMARY);
        System.out.println(engine.getSettings().toString()); // Placeholder for formatted report
        
        System.out.println("\nGenerating Discrepancy Report:");
        ReconciliationReport discrepancyReport = engine.generateReport(ReportType.DISCREPANCY);
        System.out.println("Discrepancy report generated: " + discrepancyReport.getReportId());
        
        System.out.println("\nGenerating Performance Report:");
        ReconciliationReport performanceReport = engine.generateReport(ReportType.PERFORMANCE);
        System.out.println("Performance report generated: " + performanceReport.getReportId());
    }
    
    private static void displayMetrics(PaymentReconciliationEngine engine) {
        ReconciliationMetrics metrics = engine.getMetrics();
        
        System.out.println("Reconciliation Metrics:");
        System.out.println("======================");
        System.out.printf("Total Runs: %d\n", metrics.getTotalReconciliationRuns());
        System.out.printf("Total Internal Records: %d\n", metrics.getTotalInternalRecords());
        System.out.printf("Total External Records: %d\n", metrics.getTotalExternalRecords());
        System.out.printf("Total Matches: %d\n", metrics.getTotalMatches());
        System.out.printf("Total Discrepancies: %d\n", metrics.getTotalDiscrepancies());
        System.out.printf("Average Match Rate: %.1f%%\n", metrics.getAverageMatchRate());
        System.out.printf("Average Resolution Rate: %.1f%%\n", metrics.getAverageResolutionRate());
        System.out.printf("Average Processing Time: %d ms\n", metrics.getAverageProcessingTime());
    }
    
    private static void testAdvancedFeatures(PaymentReconciliationEngine engine) {
        System.out.println("Testing ML-based matching strategy:");
        engine.setMatchingStrategy(new MLMatchingStrategy());
        
        System.out.println("Testing rule-based discrepancy resolution:");
        engine.setDiscrepancyResolutionStrategy(new RuleBasedDiscrepancyResolutionStrategy());
        
        System.out.println("Testing custom settings:");
        ReconciliationSettings settings = engine.getSettings();
        settings.setEnableMLMatching(true);
        settings.setMaxProcessingThreads(10);
        settings.setCustomSetting("custom_threshold", 0.95);
        
        System.out.printf("ML Matching Enabled: %s\n", settings.isEnableMLMatching());
        System.out.printf("Max Processing Threads: %d\n", settings.getMaxProcessingThreads());
        System.out.printf("Custom Threshold: %s\n", settings.getCustomSetting("custom_threshold"));
    }
    
    private static void testErrorScenarios(PaymentReconciliationEngine engine) {
        System.out.println("Testing error scenarios:");
        
        // Test empty data ingestion
        DataIngestionResult emptyResult = engine.ingestInternalRecords(new ArrayList<>());
        System.out.println("Empty ingestion: " + emptyResult.getMessage());
        
        // Test invalid records
        List<PaymentRecord> invalidRecords = Arrays.asList(
                new PaymentRecord(null, "ORDER001", new BigDecimal("100"), "USD", 
                                PaymentMethod.CREDIT_CARD, PaymentStatus.COMPLETED, 
                                System.currentTimeMillis(), "CUST001", "MERCH001")
        );
        
        DataIngestionResult invalidResult = engine.ingestInternalRecords(invalidRecords);
        System.out.println("Invalid records: " + invalidResult.getMessage());
        
        // Test concurrent reconciliation
        ReconciliationProcessResult concurrentResult = engine.startReconciliation();
        System.out.println("Concurrent reconciliation: " + concurrentResult.getMessage());
    }
    
    // Helper methods to create sample data
    private static List<PaymentRecord> createSampleInternalRecords() {
        List<PaymentRecord> records = new ArrayList<>();
        
        // Perfect matches
        records.add(new PaymentRecord("TXN001", "ORDER001", new BigDecimal("99.99"), "USD",
                PaymentMethod.CREDIT_CARD, PaymentStatus.COMPLETED, 
                System.currentTimeMillis() - 86400000, "CUST001", "MERCH001"));
        
        records.add(new PaymentRecord("TXN002", "ORDER002", new BigDecimal("250.00"), "USD",
                PaymentMethod.DEBIT_CARD, PaymentStatus.COMPLETED, 
                System.currentTimeMillis() - 172800000, "CUST002", "MERCH001"));
        
        // Amount discrepancy
        records.add(new PaymentRecord("TXN003", "ORDER003", new BigDecimal("150.00"), "USD",
                PaymentMethod.BANK_TRANSFER, PaymentStatus.COMPLETED, 
                System.currentTimeMillis() - 259200000, "CUST003", "MERCH001"));
        
        // Date discrepancy
        records.add(new PaymentRecord("TXN004", "ORDER004", new BigDecimal("75.50"), "USD",
                PaymentMethod.DIGITAL_WALLET, PaymentStatus.COMPLETED, 
                System.currentTimeMillis() - 345600000, "CUST004", "MERCH001"));
        
        // Missing external match
        records.add(new PaymentRecord("TXN005", "ORDER005", new BigDecimal("300.00"), "USD",
                PaymentMethod.CREDIT_CARD, PaymentStatus.COMPLETED, 
                System.currentTimeMillis() - 432000000, "CUST005", "MERCH001"));
        
        // Currency mismatch
        records.add(new PaymentRecord("TXN006", "ORDER006", new BigDecimal("120.00"), "EUR",
                PaymentMethod.CREDIT_CARD, PaymentStatus.COMPLETED, 
                System.currentTimeMillis() - 518400000, "CUST006", "MERCH001"));
        
        return records;
    }
    
    private static List<ExternalRecord> createSampleExternalRecords() {
        List<ExternalRecord> records = new ArrayList<>();
        
        // Perfect matches
        records.add(new ExternalRecord("EXT001", "BANK001", new BigDecimal("99.99"), "USD",
                "Payment for ORDER001", System.currentTimeMillis() - 86400000,
                "ACC123456", "Customer Payment", RecordSource.BANK_STATEMENT));
        
        records.add(new ExternalRecord("EXT002", "BANK002", new BigDecimal("250.00"), "USD",
                "Payment for ORDER002", System.currentTimeMillis() - 172800000,
                "ACC123456", "Customer Payment", RecordSource.BANK_STATEMENT));
        
        // Amount discrepancy (fee deducted)
        records.add(new ExternalRecord("EXT003", "BANK003", new BigDecimal("147.50"), "USD",
                "Payment for ORDER003 (fee deducted)", System.currentTimeMillis() - 259200000,
                "ACC123456", "Customer Payment", RecordSource.BANK_STATEMENT));
        
        // Date discrepancy (settlement delay)
        records.add(new ExternalRecord("EXT004", "BANK004", new BigDecimal("75.50"), "USD",
                "Payment for ORDER004", System.currentTimeMillis() - 432000000,
                "ACC123456", "Customer Payment", RecordSource.BANK_STATEMENT));
        
        // Currency mismatch
        records.add(new ExternalRecord("EXT006", "BANK006", new BigDecimal("120.00"), "USD",
                "Payment for ORDER006 (converted)", System.currentTimeMillis() - 518400000,
                "ACC123456", "Customer Payment", RecordSource.BANK_STATEMENT));
        
        // Extra external record (no internal match)
        records.add(new ExternalRecord("EXT007", "BANK007", new BigDecimal("50.00"), "USD",
                "Refund processing", System.currentTimeMillis() - 604800000,
                "ACC123456", "Refund", RecordSource.BANK_STATEMENT));
        
        return records;
    }
}

/**
 * Event listener implementation for logging reconciliation events
 */
class ReconciliationEventLogger implements ReconciliationEventListener {
    
    @Override
    public void onReconciliationEvent(String engineId, String message) {
        System.out.println("[EVENT] " + engineId + ": " + message);
    }
    
    @Override
    public void onReconciliationStarted(String engineId) {
        System.out.println("[STARTED] Reconciliation started for engine: " + engineId);
    }
    
    @Override
    public void onReconciliationCompleted(String engineId, ReconciliationSummary summary) {
        System.out.println("[COMPLETED] Reconciliation completed for engine: " + engineId);
        System.out.println("  Summary: " + summary);
    }
    
    @Override
    public void onDiscrepancyDetected(String engineId, Discrepancy discrepancy) {
        System.out.println("[DISCREPANCY] " + engineId + ": " + discrepancy);
    }
    
    @Override
    public void onDiscrepancyResolved(String engineId, DiscrepancyResolution resolution) {
        System.out.println("[RESOLVED] " + engineId + ": " + resolution);
    }
    
    @Override
    public void onMatchFound(String engineId, RecordMatch match) {
        System.out.println("[MATCH] " + engineId + ": " + match);
    }
}

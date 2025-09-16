package composition.reconciliation;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Strategy pattern for report generation
 */
public interface ReportingStrategy {
    String getStrategyName();
    ReconciliationReport generateReport(Collection<ReconciliationResult> results, 
                                      ReportType reportType, ReconciliationSettings settings);
    String formatReport(ReconciliationReport report);
}

/**
 * Detailed reporting strategy implementation
 */
class DetailedReportingStrategy implements ReportingStrategy {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public String getStrategyName() {
        return "Detailed Reporting";
    }
    
    @Override
    public ReconciliationReport generateReport(Collection<ReconciliationResult> results, 
                                             ReportType reportType, ReconciliationSettings settings) {
        
        ReconciliationReport report = new ReconciliationReport(reportType, getStrategyName());
        
        switch (reportType) {
            case SUMMARY:
                generateSummaryReport(report, results);
                break;
            case DETAILED:
                generateDetailedReport(report, results);
                break;
            case DISCREPANCY:
                generateDiscrepancyReport(report, results);
                break;
            case EXCEPTION:
                generateExceptionReport(report, results);
                break;
            case TREND_ANALYSIS:
                generateTrendAnalysisReport(report, results);
                break;
            case AUDIT_TRAIL:
                generateAuditTrailReport(report, results);
                break;
            case PERFORMANCE:
                generatePerformanceReport(report, results);
                break;
        }
        
        return report;
    }
    
    @Override
    public String formatReport(ReconciliationReport report) {
        StringBuilder formatted = new StringBuilder();
        
        formatted.append("=== ").append(report.getReportType().getDisplayName()).append(" ===\n");
        formatted.append("Generated: ").append(dateFormat.format(new Date(report.getGeneratedAt()))).append("\n");
        formatted.append("Strategy: ").append(report.getGeneratedBy()).append("\n\n");
        
        // Add summary statistics
        Map<String, Object> summary = report.getSummaryStatistics();
        if (!summary.isEmpty()) {
            formatted.append("SUMMARY STATISTICS:\n");
            formatted.append("==================\n");
            for (Map.Entry<String, Object> entry : summary.entrySet()) {
                formatted.append(String.format("%-25s: %s\n", entry.getKey(), entry.getValue()));
            }
            formatted.append("\n");
        }
        
        // Add detailed sections
        for (ReportSection section : report.getSections()) {
            formatted.append(section.getTitle().toUpperCase()).append(":\n");
            formatted.append("=".repeat(section.getTitle().length() + 1)).append("\n");
            formatted.append(section.getContent()).append("\n\n");
        }
        
        return formatted.toString();
    }
    
    private void generateSummaryReport(ReconciliationReport report, Collection<ReconciliationResult> results) {
        int totalRuns = results.size();
        int totalMatches = results.stream().mapToInt(r -> r.getSummary().getMatchedRecords()).sum();
        int totalDiscrepancies = results.stream().mapToInt(r -> r.getSummary().getTotalDiscrepancies()).sum();
        int resolvedDiscrepancies = results.stream().mapToInt(r -> r.getSummary().getResolvedDiscrepancies()).sum();
        
        double avgMatchRate = results.stream().mapToDouble(r -> r.getSummary().getMatchRate()).average().orElse(0.0);
        double avgResolutionRate = results.stream().mapToDouble(r -> r.getSummary().getResolutionRate()).average().orElse(0.0);
        
        report.addSummaryStatistic("Total Reconciliation Runs", totalRuns);
        report.addSummaryStatistic("Total Matches", totalMatches);
        report.addSummaryStatistic("Total Discrepancies", totalDiscrepancies);
        report.addSummaryStatistic("Resolved Discrepancies", resolvedDiscrepancies);
        report.addSummaryStatistic("Average Match Rate", String.format("%.1f%%", avgMatchRate));
        report.addSummaryStatistic("Average Resolution Rate", String.format("%.1f%%", avgResolutionRate));
        
        // Add recent runs section
        StringBuilder recentRuns = new StringBuilder();
        results.stream()
                .sorted((a, b) -> Long.compare(b.getSummary().getTimestamp(), a.getSummary().getTimestamp()))
                .limit(10)
                .forEach(result -> {
                    ReconciliationSummary summary = result.getSummary();
                    recentRuns.append(String.format("Run %s: %d matches, %d discrepancies (%.1f%% match rate)\n",
                            dateFormat.format(new Date(summary.getTimestamp())),
                            summary.getMatchedRecords(),
                            summary.getTotalDiscrepancies(),
                            summary.getMatchRate()));
                });
        
        report.addSection(new ReportSection("Recent Reconciliation Runs", recentRuns.toString()));
    }
    
    private void generateDetailedReport(ReconciliationReport report, Collection<ReconciliationResult> results) {
        generateSummaryReport(report, results); // Include summary
        
        // Add detailed breakdown for each run
        StringBuilder detailedBreakdown = new StringBuilder();
        
        for (ReconciliationResult result : results) {
            ReconciliationSummary summary = result.getSummary();
            detailedBreakdown.append(String.format("=== Run: %s ===\n", 
                    dateFormat.format(new Date(summary.getTimestamp()))));
            detailedBreakdown.append(String.format("Engine ID: %s\n", summary.getEngineId()));
            detailedBreakdown.append(String.format("Internal Records: %d\n", summary.getTotalInternal()));
            detailedBreakdown.append(String.format("External Records: %d\n", summary.getTotalExternal()));
            detailedBreakdown.append(String.format("Matches: %d (%.1f%%)\n", 
                    summary.getMatchedRecords(), summary.getMatchRate()));
            detailedBreakdown.append(String.format("Discrepancies: %d\n", summary.getTotalDiscrepancies()));
            detailedBreakdown.append(String.format("Resolved: %d (%.1f%%)\n", 
                    summary.getResolvedDiscrepancies(), summary.getResolutionRate()));
            detailedBreakdown.append("\n");
        }
        
        report.addSection(new ReportSection("Detailed Breakdown", detailedBreakdown.toString()));
    }
    
    private void generateDiscrepancyReport(ReconciliationReport report, Collection<ReconciliationResult> results) {
        Map<DiscrepancyType, Integer> discrepancyCounts = new HashMap<>();
        Map<DiscrepancySeverity, Integer> severityCounts = new HashMap<>();
        List<Discrepancy> allDiscrepancies = new ArrayList<>();
        
        // Collect all discrepancies
        for (ReconciliationResult result : results) {
            List<Discrepancy> discrepancies = result.getSummary().getDiscrepancyResult().getDiscrepancies();
            allDiscrepancies.addAll(discrepancies);
            
            for (Discrepancy discrepancy : discrepancies) {
                discrepancyCounts.merge(discrepancy.getType(), 1, Integer::sum);
                severityCounts.merge(discrepancy.getSeverity(), 1, Integer::sum);
            }
        }
        
        // Add summary statistics
        report.addSummaryStatistic("Total Discrepancies", allDiscrepancies.size());
        report.addSummaryStatistic("Unique Types", discrepancyCounts.size());
        
        // Discrepancy type breakdown
        StringBuilder typeBreakdown = new StringBuilder();
        discrepancyCounts.entrySet().stream()
                .sorted(Map.Entry.<DiscrepancyType, Integer>comparingByValue().reversed())
                .forEach(entry -> typeBreakdown.append(String.format("%-20s: %d\n", 
                        entry.getKey().getDisplayName(), entry.getValue())));
        
        report.addSection(new ReportSection("Discrepancy Types", typeBreakdown.toString()));
        
        // Severity breakdown
        StringBuilder severityBreakdown = new StringBuilder();
        severityCounts.entrySet().stream()
                .sorted(Map.Entry.<DiscrepancySeverity, Integer>comparingByKey())
                .forEach(entry -> severityBreakdown.append(String.format("%-10s: %d\n", 
                        entry.getKey().getDisplayName(), entry.getValue())));
        
        report.addSection(new ReportSection("Severity Distribution", severityBreakdown.toString()));
    }
    
    private void generateExceptionReport(ReconciliationReport report, Collection<ReconciliationResult> results) {
        List<Discrepancy> criticalDiscrepancies = new ArrayList<>();
        List<Discrepancy> unresolvedDiscrepancies = new ArrayList<>();
        
        for (ReconciliationResult result : results) {
            List<Discrepancy> discrepancies = result.getSummary().getDiscrepancyResult().getDiscrepancies();
            
            for (Discrepancy discrepancy : discrepancies) {
                if (discrepancy.getSeverity() == DiscrepancySeverity.CRITICAL) {
                    criticalDiscrepancies.add(discrepancy);
                }
                
                // Check if discrepancy is unresolved
                boolean resolved = result.getSummary().getResolutionResult().getResolutions().stream()
                        .anyMatch(res -> res.getDiscrepancy().equals(discrepancy) && res.isResolved());
                
                if (!resolved) {
                    unresolvedDiscrepancies.add(discrepancy);
                }
            }
        }
        
        report.addSummaryStatistic("Critical Discrepancies", criticalDiscrepancies.size());
        report.addSummaryStatistic("Unresolved Discrepancies", unresolvedDiscrepancies.size());
        
        // Critical discrepancies section
        StringBuilder criticalSection = new StringBuilder();
        for (Discrepancy discrepancy : criticalDiscrepancies) {
            criticalSection.append(String.format("ID: %s\n", discrepancy.getDiscrepancyId()));
            criticalSection.append(String.format("Type: %s\n", discrepancy.getType().getDisplayName()));
            criticalSection.append(String.format("Description: %s\n", discrepancy.getDescription()));
            criticalSection.append(String.format("Detected: %s\n", 
                    dateFormat.format(new Date(discrepancy.getDetectedAt()))));
            criticalSection.append("---\n");
        }
        
        report.addSection(new ReportSection("Critical Discrepancies", criticalSection.toString()));
    }
    
    private void generateTrendAnalysisReport(ReconciliationReport report, Collection<ReconciliationResult> results) {
        // Analyze trends over time
        List<ReconciliationResult> sortedResults = results.stream()
                .sorted((a, b) -> Long.compare(a.getSummary().getTimestamp(), b.getSummary().getTimestamp()))
                .collect(Collectors.toList());
        
        StringBuilder trendAnalysis = new StringBuilder();
        
        if (sortedResults.size() >= 2) {
            ReconciliationSummary first = sortedResults.get(0).getSummary();
            ReconciliationSummary last = sortedResults.get(sortedResults.size() - 1).getSummary();
            
            double matchRateTrend = last.getMatchRate() - first.getMatchRate();
            double resolutionRateTrend = last.getResolutionRate() - first.getResolutionRate();
            
            trendAnalysis.append(String.format("Match Rate Trend: %+.1f%%\n", matchRateTrend));
            trendAnalysis.append(String.format("Resolution Rate Trend: %+.1f%%\n", resolutionRateTrend));
            
            // Calculate moving averages
            if (sortedResults.size() >= 5) {
                double recentAvgMatch = sortedResults.stream()
                        .skip(Math.max(0, sortedResults.size() - 5))
                        .mapToDouble(r -> r.getSummary().getMatchRate())
                        .average().orElse(0.0);
                
                trendAnalysis.append(String.format("Recent 5-run Match Rate Average: %.1f%%\n", recentAvgMatch));
            }
        }
        
        report.addSection(new ReportSection("Trend Analysis", trendAnalysis.toString()));
    }
    
    private void generateAuditTrailReport(ReconciliationReport report, Collection<ReconciliationResult> results) {
        StringBuilder auditTrail = new StringBuilder();
        
        for (ReconciliationResult result : results) {
            ReconciliationSummary summary = result.getSummary();
            auditTrail.append(String.format("Timestamp: %s\n", 
                    dateFormat.format(new Date(summary.getTimestamp()))));
            auditTrail.append(String.format("Engine: %s\n", summary.getEngineId()));
            auditTrail.append(String.format("Records Processed: %d internal, %d external\n", 
                    summary.getTotalInternal(), summary.getTotalExternal()));
            auditTrail.append(String.format("Results: %d matches, %d discrepancies\n", 
                    summary.getMatchedRecords(), summary.getTotalDiscrepancies()));
            
            // Add resolution actions
            Map<ResolutionAction, Long> actionCounts = summary.getResolutionResult().getResolutions().stream()
                    .collect(Collectors.groupingBy(DiscrepancyResolution::getAction, Collectors.counting()));
            
            if (!actionCounts.isEmpty()) {
                auditTrail.append("Resolution Actions: ");
                actionCounts.forEach((action, count) -> 
                        auditTrail.append(String.format("%s(%d) ", action.getDisplayName(), count)));
                auditTrail.append("\n");
            }
            
            auditTrail.append("---\n");
        }
        
        report.addSection(new ReportSection("Audit Trail", auditTrail.toString()));
    }
    
    private void generatePerformanceReport(ReconciliationReport report, Collection<ReconciliationResult> results) {
        // Performance metrics would be calculated here
        // For this demo, we'll simulate some performance data
        
        report.addSummaryStatistic("Total Runs Analyzed", results.size());
        report.addSummaryStatistic("Average Processing Time", "2.3 seconds");
        report.addSummaryStatistic("Peak Memory Usage", "45 MB");
        report.addSummaryStatistic("Throughput", "1,250 records/second");
        
        StringBuilder performanceDetails = new StringBuilder();
        performanceDetails.append("Processing Time Distribution:\n");
        performanceDetails.append("  < 1 second:   15%\n");
        performanceDetails.append("  1-3 seconds:  60%\n");
        performanceDetails.append("  3-5 seconds:  20%\n");
        performanceDetails.append("  > 5 seconds:   5%\n\n");
        
        performanceDetails.append("Resource Utilization:\n");
        performanceDetails.append("  CPU Usage:    Average 35%, Peak 78%\n");
        performanceDetails.append("  Memory Usage: Average 32MB, Peak 45MB\n");
        performanceDetails.append("  I/O Wait:     Average 12%\n");
        
        report.addSection(new ReportSection("Performance Metrics", performanceDetails.toString()));
    }
}

/**
 * Summary reporting strategy - generates concise reports
 */
class SummaryReportingStrategy implements ReportingStrategy {
    
    @Override
    public String getStrategyName() {
        return "Summary Reporting";
    }
    
    @Override
    public ReconciliationReport generateReport(Collection<ReconciliationResult> results, 
                                             ReportType reportType, ReconciliationSettings settings) {
        
        ReconciliationReport report = new ReconciliationReport(reportType, getStrategyName());
        
        // Generate high-level summary only
        int totalRuns = results.size();
        int totalMatches = results.stream().mapToInt(r -> r.getSummary().getMatchedRecords()).sum();
        int totalDiscrepancies = results.stream().mapToInt(r -> r.getSummary().getTotalDiscrepancies()).sum();
        
        double avgMatchRate = results.stream().mapToDouble(r -> r.getSummary().getMatchRate()).average().orElse(0.0);
        
        report.addSummaryStatistic("Runs", totalRuns);
        report.addSummaryStatistic("Matches", totalMatches);
        report.addSummaryStatistic("Discrepancies", totalDiscrepancies);
        report.addSummaryStatistic("Match Rate", String.format("%.1f%%", avgMatchRate));
        
        return report;
    }
    
    @Override
    public String formatReport(ReconciliationReport report) {
        StringBuilder formatted = new StringBuilder();
        formatted.append("=== RECONCILIATION SUMMARY ===\n");
        
        Map<String, Object> summary = report.getSummaryStatistics();
        for (Map.Entry<String, Object> entry : summary.entrySet()) {
            formatted.append(String.format("%s: %s\n", entry.getKey(), entry.getValue()));
        }
        
        return formatted.toString();
    }
}

package composition.reconciliation;

import java.math.BigDecimal;
import java.util.*;

/**
 * Strategy pattern for reconciliation logic
 */
public interface ReconciliationStrategy {
    String getStrategyName();
    List<Discrepancy> identifyDiscrepancies(PaymentRecord internal, ExternalRecord external);
    boolean isAcceptableVariance(BigDecimal amount1, BigDecimal amount2);
    double calculateMatchConfidence(PaymentRecord internal, ExternalRecord external);
}

/**
 * Standard reconciliation strategy implementation
 */
class StandardReconciliationStrategy implements ReconciliationStrategy {
    private static final BigDecimal TOLERANCE_THRESHOLD = new BigDecimal("0.01");
    private static final long DATE_TOLERANCE_MS = 24 * 60 * 60 * 1000L; // 1 day
    
    @Override
    public String getStrategyName() {
        return "Standard Reconciliation";
    }
    
    @Override
    public List<Discrepancy> identifyDiscrepancies(PaymentRecord internal, ExternalRecord external) {
        List<Discrepancy> discrepancies = new ArrayList<>();
        
        // Amount discrepancy check
        if (!isAcceptableVariance(internal.getAmount(), external.getAmount())) {
            BigDecimal difference = internal.getAmount().subtract(external.getAmount()).abs();
            discrepancies.add(new Discrepancy(
                DiscrepancyType.AMOUNT_MISMATCH,
                String.format("Amount difference: %s", difference),
                internal, external,
                difference.compareTo(new BigDecimal("10")) > 0 ? DiscrepancySeverity.HIGH : DiscrepancySeverity.MEDIUM
            ));
        }
        
        // Currency mismatch check
        if (!Objects.equals(internal.getCurrency(), external.getCurrency())) {
            discrepancies.add(new Discrepancy(
                DiscrepancyType.CURRENCY_MISMATCH,
                String.format("Currency mismatch: %s vs %s", internal.getCurrency(), external.getCurrency()),
                internal, external, DiscrepancySeverity.HIGH
            ));
        }
        
        // Date discrepancy check
        long dateDifference = Math.abs(internal.getTransactionDate() - external.getSettlementDate());
        if (dateDifference > DATE_TOLERANCE_MS) {
            discrepancies.add(new Discrepancy(
                DiscrepancyType.DATE_MISMATCH,
                String.format("Date difference: %d days", dateDifference / (24 * 60 * 60 * 1000)),
                internal, external, DiscrepancySeverity.MEDIUM
            ));
        }
        
        return discrepancies;
    }
    
    @Override
    public boolean isAcceptableVariance(BigDecimal amount1, BigDecimal amount2) {
        BigDecimal difference = amount1.subtract(amount2).abs();
        return difference.compareTo(TOLERANCE_THRESHOLD) <= 0;
    }
    
    @Override
    public double calculateMatchConfidence(PaymentRecord internal, ExternalRecord external) {
        double confidence = 0.0;
        
        // Amount match (40% weight)
        if (isAcceptableVariance(internal.getAmount(), external.getAmount())) {
            confidence += 0.4;
        } else {
            BigDecimal difference = internal.getAmount().subtract(external.getAmount()).abs();
            BigDecimal percentage = difference.divide(internal.getAmount(), 4, BigDecimal.ROUND_HALF_UP);
            confidence += Math.max(0, 0.4 * (1 - percentage.doubleValue()));
        }
        
        // Currency match (20% weight)
        if (Objects.equals(internal.getCurrency(), external.getCurrency())) {
            confidence += 0.2;
        }
        
        // Date proximity (20% weight)
        long dateDifference = Math.abs(internal.getTransactionDate() - external.getSettlementDate());
        if (dateDifference <= DATE_TOLERANCE_MS) {
            confidence += 0.2 * (1.0 - (double) dateDifference / DATE_TOLERANCE_MS);
        }
        
        // Reference matching (20% weight)
        if (containsReference(external.getDescription(), internal.getTransactionId()) ||
            containsReference(external.getDescription(), internal.getOrderId())) {
            confidence += 0.2;
        }
        
        return Math.min(1.0, confidence);
    }
    
    private boolean containsReference(String description, String reference) {
        if (description == null || reference == null) return false;
        return description.toLowerCase().contains(reference.toLowerCase());
    }
}

/**
 * Strict reconciliation strategy - zero tolerance for discrepancies
 */
class StrictReconciliationStrategy implements ReconciliationStrategy {
    
    @Override
    public String getStrategyName() {
        return "Strict Reconciliation";
    }
    
    @Override
    public List<Discrepancy> identifyDiscrepancies(PaymentRecord internal, ExternalRecord external) {
        List<Discrepancy> discrepancies = new ArrayList<>();
        
        // Exact amount match required
        if (internal.getAmount().compareTo(external.getAmount()) != 0) {
            discrepancies.add(new Discrepancy(
                DiscrepancyType.AMOUNT_MISMATCH,
                "Exact amount match required",
                internal, external, DiscrepancySeverity.HIGH
            ));
        }
        
        // Exact currency match required
        if (!Objects.equals(internal.getCurrency(), external.getCurrency())) {
            discrepancies.add(new Discrepancy(
                DiscrepancyType.CURRENCY_MISMATCH,
                "Exact currency match required",
                internal, external, DiscrepancySeverity.HIGH
            ));
        }
        
        // Same day transaction required
        if (!isSameDay(internal.getTransactionDate(), external.getSettlementDate())) {
            discrepancies.add(new Discrepancy(
                DiscrepancyType.DATE_MISMATCH,
                "Same day transaction required",
                internal, external, DiscrepancySeverity.HIGH
            ));
        }
        
        return discrepancies;
    }
    
    @Override
    public boolean isAcceptableVariance(BigDecimal amount1, BigDecimal amount2) {
        return amount1.compareTo(amount2) == 0;
    }
    
    @Override
    public double calculateMatchConfidence(PaymentRecord internal, ExternalRecord external) {
        // All criteria must match exactly for high confidence
        boolean exactAmount = internal.getAmount().compareTo(external.getAmount()) == 0;
        boolean exactCurrency = Objects.equals(internal.getCurrency(), external.getCurrency());
        boolean sameDay = isSameDay(internal.getTransactionDate(), external.getSettlementDate());
        
        if (exactAmount && exactCurrency && sameDay) {
            return 1.0;
        } else if (exactAmount && exactCurrency) {
            return 0.8;
        } else if (exactAmount) {
            return 0.6;
        } else {
            return 0.0;
        }
    }
    
    private boolean isSameDay(long timestamp1, long timestamp2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(timestamp1);
        cal2.setTimeInMillis(timestamp2);
        
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}

/**
 * Flexible reconciliation strategy - higher tolerance for discrepancies
 */
class FlexibleReconciliationStrategy implements ReconciliationStrategy {
    private static final BigDecimal TOLERANCE_PERCENTAGE = new BigDecimal("0.05"); // 5%
    private static final long DATE_TOLERANCE_MS = 7 * 24 * 60 * 60 * 1000L; // 7 days
    
    @Override
    public String getStrategyName() {
        return "Flexible Reconciliation";
    }
    
    @Override
    public List<Discrepancy> identifyDiscrepancies(PaymentRecord internal, ExternalRecord external) {
        List<Discrepancy> discrepancies = new ArrayList<>();
        
        // Flexible amount check (5% tolerance)
        if (!isAcceptableVariance(internal.getAmount(), external.getAmount())) {
            BigDecimal difference = internal.getAmount().subtract(external.getAmount()).abs();
            BigDecimal percentage = difference.divide(internal.getAmount(), 4, BigDecimal.ROUND_HALF_UP);
            
            DiscrepancySeverity severity = percentage.compareTo(new BigDecimal("0.10")) > 0 ? 
                DiscrepancySeverity.HIGH : DiscrepancySeverity.LOW;
            
            discrepancies.add(new Discrepancy(
                DiscrepancyType.AMOUNT_MISMATCH,
                String.format("Amount variance: %.2f%%", percentage.multiply(new BigDecimal("100"))),
                internal, external, severity
            ));
        }
        
        // Currency mismatch (still important)
        if (!Objects.equals(internal.getCurrency(), external.getCurrency())) {
            discrepancies.add(new Discrepancy(
                DiscrepancyType.CURRENCY_MISMATCH,
                "Currency mismatch detected",
                internal, external, DiscrepancySeverity.MEDIUM
            ));
        }
        
        // Flexible date check (7 days tolerance)
        long dateDifference = Math.abs(internal.getTransactionDate() - external.getSettlementDate());
        if (dateDifference > DATE_TOLERANCE_MS) {
            discrepancies.add(new Discrepancy(
                DiscrepancyType.DATE_MISMATCH,
                String.format("Date difference exceeds 7 days: %d days", dateDifference / (24 * 60 * 60 * 1000)),
                internal, external, DiscrepancySeverity.LOW
            ));
        }
        
        return discrepancies;
    }
    
    @Override
    public boolean isAcceptableVariance(BigDecimal amount1, BigDecimal amount2) {
        BigDecimal difference = amount1.subtract(amount2).abs();
        BigDecimal tolerance = amount1.multiply(TOLERANCE_PERCENTAGE);
        return difference.compareTo(tolerance) <= 0;
    }
    
    @Override
    public double calculateMatchConfidence(PaymentRecord internal, ExternalRecord external) {
        double confidence = 0.0;
        
        // Amount match with tolerance (50% weight)
        if (isAcceptableVariance(internal.getAmount(), external.getAmount())) {
            confidence += 0.5;
        } else {
            BigDecimal difference = internal.getAmount().subtract(external.getAmount()).abs();
            BigDecimal percentage = difference.divide(internal.getAmount(), 4, BigDecimal.ROUND_HALF_UP);
            confidence += Math.max(0, 0.5 * (1 - percentage.doubleValue() * 2)); // More lenient
        }
        
        // Currency match (25% weight)
        if (Objects.equals(internal.getCurrency(), external.getCurrency())) {
            confidence += 0.25;
        }
        
        // Date proximity (25% weight)
        long dateDifference = Math.abs(internal.getTransactionDate() - external.getSettlementDate());
        if (dateDifference <= DATE_TOLERANCE_MS) {
            confidence += 0.25 * (1.0 - (double) dateDifference / DATE_TOLERANCE_MS);
        }
        
        return Math.min(1.0, confidence);
    }
}

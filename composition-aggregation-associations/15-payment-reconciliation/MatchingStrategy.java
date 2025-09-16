package composition.reconciliation;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Strategy pattern for record matching logic
 */
public interface MatchingStrategy {
    String getStrategyName();
    MatchResult findMatch(PaymentRecord internal, Collection<ExternalRecord> externalRecords);
    List<MatchCandidate> findPotentialMatches(PaymentRecord internal, Collection<ExternalRecord> externalRecords);
}

/**
 * Exact matching strategy - requires perfect matches
 */
class ExactMatchingStrategy implements MatchingStrategy {
    
    @Override
    public String getStrategyName() {
        return "Exact Matching";
    }
    
    @Override
    public MatchResult findMatch(PaymentRecord internal, Collection<ExternalRecord> externalRecords) {
        for (ExternalRecord external : externalRecords) {
            if (isExactMatch(internal, external)) {
                return new MatchResult(true, external, 1.0, "Exact match found");
            }
        }
        return new MatchResult(false, null, 0.0, "No exact match found");
    }
    
    @Override
    public List<MatchCandidate> findPotentialMatches(PaymentRecord internal, Collection<ExternalRecord> externalRecords) {
        return externalRecords.stream()
                .filter(external -> isExactMatch(internal, external))
                .map(external -> new MatchCandidate(external, 1.0, "Exact match"))
                .collect(Collectors.toList());
    }
    
    private boolean isExactMatch(PaymentRecord internal, ExternalRecord external) {
        return internal.getAmount().compareTo(external.getAmount()) == 0 &&
               Objects.equals(internal.getCurrency(), external.getCurrency()) &&
               isSameDay(internal.getTransactionDate(), external.getSettlementDate());
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
 * Fuzzy matching strategy - uses confidence scoring
 */
class FuzzyMatchingStrategy implements MatchingStrategy {
    private static final double MIN_CONFIDENCE_THRESHOLD = 0.7;
    
    @Override
    public String getStrategyName() {
        return "Fuzzy Matching";
    }
    
    @Override
    public MatchResult findMatch(PaymentRecord internal, Collection<ExternalRecord> externalRecords) {
        ExternalRecord bestMatch = null;
        double bestConfidence = 0.0;
        
        for (ExternalRecord external : externalRecords) {
            double confidence = calculateMatchConfidence(internal, external);
            if (confidence > bestConfidence && confidence >= MIN_CONFIDENCE_THRESHOLD) {
                bestMatch = external;
                bestConfidence = confidence;
            }
        }
        
        if (bestMatch != null) {
            return new MatchResult(true, bestMatch, bestConfidence, 
                String.format("Fuzzy match with %.1f%% confidence", bestConfidence * 100));
        }
        
        return new MatchResult(false, null, 0.0, "No match above confidence threshold");
    }
    
    @Override
    public List<MatchCandidate> findPotentialMatches(PaymentRecord internal, Collection<ExternalRecord> externalRecords) {
        return externalRecords.stream()
                .map(external -> new MatchCandidate(external, 
                    calculateMatchConfidence(internal, external), "Fuzzy match candidate"))
                .filter(candidate -> candidate.getConfidence() >= MIN_CONFIDENCE_THRESHOLD)
                .sorted((a, b) -> Double.compare(b.getConfidence(), a.getConfidence()))
                .collect(Collectors.toList());
    }
    
    private double calculateMatchConfidence(PaymentRecord internal, ExternalRecord external) {
        double confidence = 0.0;
        
        // Amount similarity (40% weight)
        confidence += calculateAmountSimilarity(internal.getAmount(), external.getAmount()) * 0.4;
        
        // Currency match (20% weight)
        if (Objects.equals(internal.getCurrency(), external.getCurrency())) {
            confidence += 0.2;
        }
        
        // Date proximity (20% weight)
        confidence += calculateDateProximity(internal.getTransactionDate(), external.getSettlementDate()) * 0.2;
        
        // Reference similarity (20% weight)
        confidence += calculateReferenceSimilarity(internal, external) * 0.2;
        
        return Math.min(1.0, confidence);
    }
    
    private double calculateAmountSimilarity(BigDecimal amount1, BigDecimal amount2) {
        if (amount1.compareTo(BigDecimal.ZERO) == 0 || amount2.compareTo(BigDecimal.ZERO) == 0) {
            return amount1.compareTo(amount2) == 0 ? 1.0 : 0.0;
        }
        
        BigDecimal difference = amount1.subtract(amount2).abs();
        BigDecimal average = amount1.add(amount2).divide(new BigDecimal("2"), 4, BigDecimal.ROUND_HALF_UP);
        BigDecimal percentageDiff = difference.divide(average, 4, BigDecimal.ROUND_HALF_UP);
        
        return Math.max(0.0, 1.0 - percentageDiff.doubleValue());
    }
    
    private double calculateDateProximity(long date1, long date2) {
        long difference = Math.abs(date1 - date2);
        long maxAcceptableDiff = 7 * 24 * 60 * 60 * 1000L; // 7 days
        
        if (difference <= maxAcceptableDiff) {
            return 1.0 - (double) difference / maxAcceptableDiff;
        }
        return 0.0;
    }
    
    private double calculateReferenceSimilarity(PaymentRecord internal, ExternalRecord external) {
        String description = external.getDescription().toLowerCase();
        String transactionId = internal.getTransactionId().toLowerCase();
        String orderId = internal.getOrderId() != null ? internal.getOrderId().toLowerCase() : "";
        
        if (description.contains(transactionId) || description.contains(orderId)) {
            return 1.0;
        }
        
        // Calculate string similarity using Levenshtein distance
        double transactionSimilarity = calculateStringSimilarity(description, transactionId);
        double orderSimilarity = orderId.isEmpty() ? 0.0 : calculateStringSimilarity(description, orderId);
        
        return Math.max(transactionSimilarity, orderSimilarity);
    }
    
    private double calculateStringSimilarity(String s1, String s2) {
        if (s1.length() == 0 || s2.length() == 0) return 0.0;
        
        int distance = levenshteinDistance(s1, s2);
        int maxLength = Math.max(s1.length(), s2.length());
        
        return 1.0 - (double) distance / maxLength;
    }
    
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
}

/**
 * Machine learning-based matching strategy (simulated)
 */
class MLMatchingStrategy implements MatchingStrategy {
    private static final double ML_CONFIDENCE_THRESHOLD = 0.8;
    
    @Override
    public String getStrategyName() {
        return "ML-Based Matching";
    }
    
    @Override
    public MatchResult findMatch(PaymentRecord internal, Collection<ExternalRecord> externalRecords) {
        ExternalRecord bestMatch = null;
        double bestScore = 0.0;
        
        for (ExternalRecord external : externalRecords) {
            double mlScore = simulateMLPrediction(internal, external);
            if (mlScore > bestScore && mlScore >= ML_CONFIDENCE_THRESHOLD) {
                bestMatch = external;
                bestScore = mlScore;
            }
        }
        
        if (bestMatch != null) {
            return new MatchResult(true, bestMatch, bestScore, 
                String.format("ML prediction with %.1f%% confidence", bestScore * 100));
        }
        
        return new MatchResult(false, null, 0.0, "No ML match above threshold");
    }
    
    @Override
    public List<MatchCandidate> findPotentialMatches(PaymentRecord internal, Collection<ExternalRecord> externalRecords) {
        return externalRecords.stream()
                .map(external -> new MatchCandidate(external, 
                    simulateMLPrediction(internal, external), "ML prediction"))
                .filter(candidate -> candidate.getConfidence() >= ML_CONFIDENCE_THRESHOLD)
                .sorted((a, b) -> Double.compare(b.getConfidence(), a.getConfidence()))
                .collect(Collectors.toList());
    }
    
    private double simulateMLPrediction(PaymentRecord internal, ExternalRecord external) {
        // Simulate ML model prediction using weighted features
        double[] features = extractFeatures(internal, external);
        double[] weights = {0.3, 0.25, 0.2, 0.15, 0.1}; // Simulated learned weights
        
        double score = 0.0;
        for (int i = 0; i < Math.min(features.length, weights.length); i++) {
            score += features[i] * weights[i];
        }
        
        // Apply sigmoid function to normalize to [0,1]
        return 1.0 / (1.0 + Math.exp(-score));
    }
    
    private double[] extractFeatures(PaymentRecord internal, ExternalRecord external) {
        // Extract normalized features for ML model
        double amountSimilarity = calculateAmountFeature(internal.getAmount(), external.getAmount());
        double currencyMatch = Objects.equals(internal.getCurrency(), external.getCurrency()) ? 1.0 : 0.0;
        double dateProximity = calculateDateFeature(internal.getTransactionDate(), external.getSettlementDate());
        double referenceMatch = calculateReferenceFeature(internal, external);
        double amountRange = calculateAmountRangeFeature(internal.getAmount());
        
        return new double[]{amountSimilarity, currencyMatch, dateProximity, referenceMatch, amountRange};
    }
    
    private double calculateAmountFeature(BigDecimal amount1, BigDecimal amount2) {
        if (amount1.compareTo(BigDecimal.ZERO) == 0 || amount2.compareTo(BigDecimal.ZERO) == 0) {
            return amount1.compareTo(amount2) == 0 ? 1.0 : 0.0;
        }
        
        BigDecimal ratio = amount1.divide(amount2, 4, BigDecimal.ROUND_HALF_UP);
        return Math.exp(-Math.abs(Math.log(ratio.doubleValue()))); // Exponential decay from 1.0
    }
    
    private double calculateDateFeature(long date1, long date2) {
        long difference = Math.abs(date1 - date2);
        long dayInMs = 24 * 60 * 60 * 1000L;
        double daysDiff = (double) difference / dayInMs;
        
        return Math.exp(-daysDiff / 3.0); // Exponential decay with 3-day half-life
    }
    
    private double calculateReferenceFeature(PaymentRecord internal, ExternalRecord external) {
        String description = external.getDescription().toLowerCase();
        String transactionId = internal.getTransactionId().toLowerCase();
        
        if (description.contains(transactionId)) {
            return 1.0;
        }
        
        // Partial match scoring
        int matches = 0;
        String[] descWords = description.split("\\s+");
        String[] idWords = transactionId.split("\\s+");
        
        for (String descWord : descWords) {
            for (String idWord : idWords) {
                if (descWord.equals(idWord) && descWord.length() > 2) {
                    matches++;
                }
            }
        }
        
        return Math.min(1.0, (double) matches / Math.max(descWords.length, idWords.length));
    }
    
    private double calculateAmountRangeFeature(BigDecimal amount) {
        // Normalize amount to [0,1] range for different amount categories
        double amountValue = amount.doubleValue();
        
        if (amountValue < 10) return 0.1;
        else if (amountValue < 100) return 0.3;
        else if (amountValue < 1000) return 0.5;
        else if (amountValue < 10000) return 0.7;
        else return 0.9;
    }
}

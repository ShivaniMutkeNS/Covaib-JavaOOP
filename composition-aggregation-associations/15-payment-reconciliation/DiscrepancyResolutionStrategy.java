package composition.reconciliation;

import java.math.BigDecimal;
import java.util.*;

/**
 * Strategy pattern for discrepancy resolution
 */
public interface DiscrepancyResolutionStrategy {
    String getStrategyName();
    DiscrepancyResolution resolveDiscrepancy(Discrepancy discrepancy);
    boolean canAutoResolve(Discrepancy discrepancy);
    List<ResolutionAction> getSuggestedActions(Discrepancy discrepancy);
}

/**
 * Automatic discrepancy resolution strategy
 */
class AutomaticDiscrepancyResolutionStrategy implements DiscrepancyResolutionStrategy {
    private static final BigDecimal AUTO_RESOLVE_THRESHOLD = new BigDecimal("1.00");
    
    @Override
    public String getStrategyName() {
        return "Automatic Resolution";
    }
    
    @Override
    public DiscrepancyResolution resolveDiscrepancy(Discrepancy discrepancy) {
        if (canAutoResolve(discrepancy)) {
            return performAutoResolution(discrepancy);
        } else {
            return createManualReviewResolution(discrepancy);
        }
    }
    
    @Override
    public boolean canAutoResolve(Discrepancy discrepancy) {
        switch (discrepancy.getType()) {
            case AMOUNT_MISMATCH:
                return isMinorAmountDiscrepancy(discrepancy);
            case DATE_MISMATCH:
                return isMinorDateDiscrepancy(discrepancy);
            case REFERENCE_MISMATCH:
                return discrepancy.getSeverity() == DiscrepancySeverity.LOW;
            case INVALID_DATA:
                return false; // Always requires manual review
            case MISSING_INTERNAL:
            case MISSING_EXTERNAL:
                return false; // Always requires manual review
            default:
                return discrepancy.getSeverity() == DiscrepancySeverity.LOW;
        }
    }
    
    @Override
    public List<ResolutionAction> getSuggestedActions(Discrepancy discrepancy) {
        List<ResolutionAction> actions = new ArrayList<>();
        
        if (canAutoResolve(discrepancy)) {
            actions.add(ResolutionAction.AUTO_RESOLVED);
        } else {
            actions.add(ResolutionAction.MANUAL_REVIEW);
            
            if (discrepancy.getSeverity() == DiscrepancySeverity.CRITICAL) {
                actions.add(ResolutionAction.ESCALATED);
            }
        }
        
        return actions;
    }
    
    private DiscrepancyResolution performAutoResolution(Discrepancy discrepancy) {
        String resolution = generateAutoResolutionMessage(discrepancy);
        return new DiscrepancyResolution(discrepancy, ResolutionAction.AUTO_RESOLVED, 
                                       resolution, true, "System");
    }
    
    private DiscrepancyResolution createManualReviewResolution(Discrepancy discrepancy) {
        String resolution = "Requires manual review due to " + discrepancy.getType().getDisplayName();
        ResolutionAction action = discrepancy.getSeverity() == DiscrepancySeverity.CRITICAL ? 
                                ResolutionAction.ESCALATED : ResolutionAction.MANUAL_REVIEW;
        
        return new DiscrepancyResolution(discrepancy, action, resolution, false, "System");
    }
    
    private boolean isMinorAmountDiscrepancy(Discrepancy discrepancy) {
        if (discrepancy.getInternalRecord() == null || discrepancy.getExternalRecord() == null) {
            return false;
        }
        
        BigDecimal difference = discrepancy.getInternalRecord().getAmount()
                .subtract(discrepancy.getExternalRecord().getAmount()).abs();
        
        return difference.compareTo(AUTO_RESOLVE_THRESHOLD) <= 0;
    }
    
    private boolean isMinorDateDiscrepancy(Discrepancy discrepancy) {
        if (discrepancy.getInternalRecord() == null || discrepancy.getExternalRecord() == null) {
            return false;
        }
        
        long dateDifference = Math.abs(discrepancy.getInternalRecord().getTransactionDate() - 
                                     discrepancy.getExternalRecord().getSettlementDate());
        long threeDaysInMs = 3 * 24 * 60 * 60 * 1000L;
        
        return dateDifference <= threeDaysInMs;
    }
    
    private String generateAutoResolutionMessage(Discrepancy discrepancy) {
        switch (discrepancy.getType()) {
            case AMOUNT_MISMATCH:
                return "Minor amount difference within acceptable tolerance - auto-resolved";
            case DATE_MISMATCH:
                return "Date difference within acceptable range - auto-resolved";
            case REFERENCE_MISMATCH:
                return "Reference mismatch with low severity - auto-resolved";
            default:
                return "Discrepancy auto-resolved based on configured rules";
        }
    }
}

/**
 * Manual review discrepancy resolution strategy
 */
class ManualReviewDiscrepancyResolutionStrategy implements DiscrepancyResolutionStrategy {
    
    @Override
    public String getStrategyName() {
        return "Manual Review Required";
    }
    
    @Override
    public DiscrepancyResolution resolveDiscrepancy(Discrepancy discrepancy) {
        ResolutionAction action = determineRequiredAction(discrepancy);
        String resolution = generateManualReviewMessage(discrepancy, action);
        
        return new DiscrepancyResolution(discrepancy, action, resolution, false, "System");
    }
    
    @Override
    public boolean canAutoResolve(Discrepancy discrepancy) {
        return false; // This strategy never auto-resolves
    }
    
    @Override
    public List<ResolutionAction> getSuggestedActions(Discrepancy discrepancy) {
        List<ResolutionAction> actions = new ArrayList<>();
        
        switch (discrepancy.getSeverity()) {
            case CRITICAL:
                actions.add(ResolutionAction.ESCALATED);
                actions.add(ResolutionAction.MANUAL_REVIEW);
                break;
            case HIGH:
                actions.add(ResolutionAction.MANUAL_REVIEW);
                actions.add(ResolutionAction.ESCALATED);
                break;
            case MEDIUM:
                actions.add(ResolutionAction.MANUAL_REVIEW);
                actions.add(ResolutionAction.PENDING_APPROVAL);
                break;
            case LOW:
                actions.add(ResolutionAction.MANUAL_REVIEW);
                actions.add(ResolutionAction.IGNORED);
                break;
        }
        
        return actions;
    }
    
    private ResolutionAction determineRequiredAction(Discrepancy discrepancy) {
        if (discrepancy.getSeverity() == DiscrepancySeverity.CRITICAL) {
            return ResolutionAction.ESCALATED;
        } else if (discrepancy.getType() == DiscrepancyType.MISSING_INTERNAL || 
                   discrepancy.getType() == DiscrepancyType.MISSING_EXTERNAL) {
            return ResolutionAction.ESCALATED;
        } else {
            return ResolutionAction.MANUAL_REVIEW;
        }
    }
    
    private String generateManualReviewMessage(Discrepancy discrepancy, ResolutionAction action) {
        StringBuilder message = new StringBuilder();
        message.append("Manual review required for ").append(discrepancy.getType().getDisplayName());
        message.append(" (").append(discrepancy.getSeverity().getDisplayName()).append(" severity)");
        
        if (action == ResolutionAction.ESCALATED) {
            message.append(" - Escalated to supervisor");
        }
        
        return message.toString();
    }
}

/**
 * Rule-based discrepancy resolution strategy
 */
class RuleBasedDiscrepancyResolutionStrategy implements DiscrepancyResolutionStrategy {
    private final Map<DiscrepancyType, ResolutionRule> resolutionRules;
    
    public RuleBasedDiscrepancyResolutionStrategy() {
        this.resolutionRules = new HashMap<>();
        initializeDefaultRules();
    }
    
    @Override
    public String getStrategyName() {
        return "Rule-Based Resolution";
    }
    
    @Override
    public DiscrepancyResolution resolveDiscrepancy(Discrepancy discrepancy) {
        ResolutionRule rule = resolutionRules.get(discrepancy.getType());
        
        if (rule != null && rule.canApply(discrepancy)) {
            return rule.apply(discrepancy);
        } else {
            return createDefaultResolution(discrepancy);
        }
    }
    
    @Override
    public boolean canAutoResolve(Discrepancy discrepancy) {
        ResolutionRule rule = resolutionRules.get(discrepancy.getType());
        return rule != null && rule.canAutoResolve(discrepancy);
    }
    
    @Override
    public List<ResolutionAction> getSuggestedActions(Discrepancy discrepancy) {
        ResolutionRule rule = resolutionRules.get(discrepancy.getType());
        
        if (rule != null) {
            return rule.getSuggestedActions(discrepancy);
        } else {
            return Arrays.asList(ResolutionAction.MANUAL_REVIEW);
        }
    }
    
    public void addResolutionRule(DiscrepancyType type, ResolutionRule rule) {
        resolutionRules.put(type, rule);
    }
    
    private void initializeDefaultRules() {
        // Amount mismatch rule
        resolutionRules.put(DiscrepancyType.AMOUNT_MISMATCH, new ResolutionRule() {
            @Override
            public boolean canApply(Discrepancy discrepancy) {
                return discrepancy.getInternalRecord() != null && discrepancy.getExternalRecord() != null;
            }
            
            @Override
            public boolean canAutoResolve(Discrepancy discrepancy) {
                BigDecimal difference = discrepancy.getInternalRecord().getAmount()
                        .subtract(discrepancy.getExternalRecord().getAmount()).abs();
                return difference.compareTo(new BigDecimal("5.00")) <= 0;
            }
            
            @Override
            public DiscrepancyResolution apply(Discrepancy discrepancy) {
                if (canAutoResolve(discrepancy)) {
                    return new DiscrepancyResolution(discrepancy, ResolutionAction.AUTO_RESOLVED,
                            "Amount difference within $5 tolerance - auto-resolved", true, "System");
                } else {
                    return new DiscrepancyResolution(discrepancy, ResolutionAction.MANUAL_REVIEW,
                            "Amount difference exceeds tolerance - requires review", false, "System");
                }
            }
            
            @Override
            public List<ResolutionAction> getSuggestedActions(Discrepancy discrepancy) {
                if (canAutoResolve(discrepancy)) {
                    return Arrays.asList(ResolutionAction.AUTO_RESOLVED, ResolutionAction.IGNORED);
                } else {
                    return Arrays.asList(ResolutionAction.MANUAL_REVIEW, ResolutionAction.SYSTEM_CORRECTION);
                }
            }
        });
        
        // Missing record rules
        resolutionRules.put(DiscrepancyType.MISSING_EXTERNAL, new ResolutionRule() {
            @Override
            public boolean canApply(Discrepancy discrepancy) {
                return discrepancy.getInternalRecord() != null;
            }
            
            @Override
            public boolean canAutoResolve(Discrepancy discrepancy) {
                return false; // Missing records always require manual review
            }
            
            @Override
            public DiscrepancyResolution apply(Discrepancy discrepancy) {
                return new DiscrepancyResolution(discrepancy, ResolutionAction.ESCALATED,
                        "Missing external record - requires investigation", false, "System");
            }
            
            @Override
            public List<ResolutionAction> getSuggestedActions(Discrepancy discrepancy) {
                return Arrays.asList(ResolutionAction.ESCALATED, ResolutionAction.MANUAL_REVIEW);
            }
        });
    }
    
    private DiscrepancyResolution createDefaultResolution(Discrepancy discrepancy) {
        return new DiscrepancyResolution(discrepancy, ResolutionAction.MANUAL_REVIEW,
                "No specific rule found - requires manual review", false, "System");
    }
    
    /**
     * Interface for resolution rules
     */
    interface ResolutionRule {
        boolean canApply(Discrepancy discrepancy);
        boolean canAutoResolve(Discrepancy discrepancy);
        DiscrepancyResolution apply(Discrepancy discrepancy);
        List<ResolutionAction> getSuggestedActions(Discrepancy discrepancy);
    }
}

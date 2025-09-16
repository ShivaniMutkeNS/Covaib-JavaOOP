package composition.travel;

import java.util.List;

/**
 * Package Quote data class
 */
public class PackageQuote {
    private final String packageId;
    private final double basePrice;
    private final double finalPrice;
    private final double discount;
    private final String pricingStrategy;
    private final List<String> includedServices;
    
    public PackageQuote(String packageId, double basePrice, double finalPrice, double discount,
                       String pricingStrategy, List<String> includedServices) {
        this.packageId = packageId;
        this.basePrice = basePrice;
        this.finalPrice = finalPrice;
        this.discount = discount;
        this.pricingStrategy = pricingStrategy;
        this.includedServices = includedServices;
    }
    
    public String getPackageId() { return packageId; }
    public double getBasePrice() { return basePrice; }
    public double getFinalPrice() { return finalPrice; }
    public double getDiscount() { return discount; }
    public String getPricingStrategy() { return pricingStrategy; }
    public List<String> getIncludedServices() { return includedServices; }
}

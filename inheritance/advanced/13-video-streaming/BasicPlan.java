public class BasicPlan extends SubscriptionPlan {
    public BasicPlan(String planId, String planName, double price, String billingCycle) {
        super(planId, planName, price, billingCycle);
    }
    
    @Override
    public String getResolution() {
        return "480p";
    }
    
    @Override
    public int getMaxDevices() {
        return 1;
    }
    
    @Override
    public String getPlanFeatures() {
        return "Basic Plan Features: Resolution: 480p, Devices: 1, Ads: Yes, Download: No";
    }
}

public abstract class SubscriptionPlan {
    protected String planId;
    protected String planName;
    protected double price;
    protected String billingCycle;
    protected String status;
    
    public SubscriptionPlan(String planId, String planName, double price, String billingCycle) {
        this.planId = planId;
        this.planName = planName;
        this.price = price;
        this.billingCycle = billingCycle;
        this.status = "Active";
    }
    
    public abstract String getResolution();
    public abstract int getMaxDevices();
    public abstract String getPlanFeatures();
    
    public String getPlanInfo() {
        return String.format("Plan: %s, Name: %s, Price: $%.2f/%s, Status: %s", 
                           planId, planName, price, billingCycle, status);
    }
}

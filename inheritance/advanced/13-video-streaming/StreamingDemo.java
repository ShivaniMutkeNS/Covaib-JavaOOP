public class StreamingDemo {
    public static void main(String[] args) {
        System.out.println("📺 VIDEO STREAMING PLANS 📺");
        System.out.println("=" .repeat(50));
        
        SubscriptionPlan[] plans = {
            new BasicPlan("BASIC001", "Basic Plan", 9.99, "month")
        };
        
        System.out.println("\n📋 PLAN INFORMATION:");
        for (SubscriptionPlan plan : plans) {
            System.out.println(plan.getPlanInfo());
        }
        
        System.out.println("\n🔧 PLAN FEATURES:");
        for (SubscriptionPlan plan : plans) {
            System.out.println(plan.getPlanFeatures());
        }
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
}

package composition.travel;

/**
 * Customer Information data class
 */
public class CustomerInfo {
    private final String customerId;
    private final String name;
    private final String email;
    private final String phone;
    private final String membershipTier;
    
    public CustomerInfo(String customerId, String name, String email, String phone, String membershipTier) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipTier = membershipTier;
    }
    
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getMembershipTier() { return membershipTier; }
}

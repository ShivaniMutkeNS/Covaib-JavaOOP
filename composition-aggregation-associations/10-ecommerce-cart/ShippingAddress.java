package composition.ecommerce;

/**
 * Shipping Address data class
 */
public class ShippingAddress {
    private final String name;
    private final String street;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String country;
    
    public ShippingAddress(String name, String street, String city, String state, String zipCode, String country) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }
    
    public String getName() { return name; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }
}

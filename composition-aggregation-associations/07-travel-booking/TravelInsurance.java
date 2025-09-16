package composition.travel;

/**
 * Travel Insurance component
 */
public class TravelInsurance {
    private final String type;
    private final String provider;
    private final double premium;
    private final double coverage;
    private final String[] coveredEvents;
    
    public TravelInsurance(String type, String provider, double premium, double coverage, String[] coveredEvents) {
        this.type = type;
        this.provider = provider;
        this.premium = premium;
        this.coverage = coverage;
        this.coveredEvents = coveredEvents;
    }
    
    // Getters
    public String getType() { return type; }
    public String getProvider() { return provider; }
    public double getPremium() { return premium; }
    public double getCoverage() { return coverage; }
    public String[] getCoveredEvents() { return coveredEvents; }
}

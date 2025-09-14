/**
 * Abstract base class for all vehicle types
 * Demonstrates inheritance vs composition for pricing strategies
 * 
 * @author Expert Developer
 * @version 1.0
 */
public abstract class Vehicle {
    protected String vehicleId;
    protected String driverId;
    protected String vehicleType;
    protected String make;
    protected String model;
    protected int year;
    protected String color;
    protected String licensePlate;
    protected String status;
    protected String location;
    protected double latitude;
    protected double longitude;
    protected int maxPassengers;
    protected int currentPassengers;
    protected boolean isAvailable;
    protected double baseFare;
    protected double perKmRate;
    protected double perMinuteRate;
    protected String fuelType;
    protected double fuelEfficiency;
    protected String transmission;
    protected String[] features;
    protected int featureCount;
    protected int maxFeatures;
    protected String[] amenities;
    protected int amenityCount;
    protected int maxAmenities;
    protected String insuranceStatus;
    protected String registrationStatus;
    protected String inspectionStatus;
    protected double rating;
    protected int totalRides;
    protected double totalEarnings;
    protected String lastServiceDate;
    protected int mileage;
    protected String[] maintenanceHistory;
    protected int maintenanceCount;
    protected int maxMaintenance;
    
    /**
     * Constructor for Vehicle
     * @param vehicleId Unique vehicle identifier
     * @param driverId Driver identifier
     * @param vehicleType Type of vehicle
     * @param make Vehicle make
     * @param model Vehicle model
     * @param year Vehicle year
     * @param color Vehicle color
     * @param licensePlate License plate number
     * @param location Current location
     * @param latitude Latitude coordinate
     * @param longitude Longitude coordinate
     * @param maxPassengers Maximum number of passengers
     * @param baseFare Base fare amount
     * @param perKmRate Rate per kilometer
     * @param perMinuteRate Rate per minute
     * @param fuelType Type of fuel
     * @param fuelEfficiency Fuel efficiency (km/liter)
     * @param transmission Transmission type
     */
    public Vehicle(String vehicleId, String driverId, String vehicleType, String make, String model, 
                   int year, String color, String licensePlate, String location, double latitude, 
                   double longitude, int maxPassengers, double baseFare, double perKmRate, 
                   double perMinuteRate, String fuelType, double fuelEfficiency, String transmission) {
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.vehicleType = vehicleType;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.licensePlate = licensePlate;
        this.status = "Available";
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.maxPassengers = maxPassengers;
        this.currentPassengers = 0;
        this.isAvailable = true;
        this.baseFare = baseFare;
        this.perKmRate = perKmRate;
        this.perMinuteRate = perMinuteRate;
        this.fuelType = fuelType;
        this.fuelEfficiency = fuelEfficiency;
        this.transmission = transmission;
        this.features = new String[20];
        this.featureCount = 0;
        this.maxFeatures = 20;
        this.amenities = new String[15];
        this.amenityCount = 0;
        this.maxAmenities = 15;
        this.insuranceStatus = "Valid";
        this.registrationStatus = "Valid";
        this.inspectionStatus = "Passed";
        this.rating = 0.0;
        this.totalRides = 0;
        this.totalEarnings = 0.0;
        this.lastServiceDate = "";
        this.mileage = 0;
        this.maintenanceHistory = new String[50];
        this.maintenanceCount = 0;
        this.maxMaintenance = 50;
    }
    
    /**
     * Abstract method to calculate fare
     * Each vehicle type has different fare calculation logic
     * @param distance Distance in kilometers
     * @param duration Duration in minutes
     * @param surgeMultiplier Surge pricing multiplier
     * @return The calculated fare
     */
    public abstract double calculateFare(double distance, double duration, double surgeMultiplier);
    
    /**
     * Abstract method to get vehicle features
     * Each vehicle type has different features
     * @return String description of vehicle features
     */
    public abstract String getVehicleFeatures();
    
    /**
     * Abstract method to check if vehicle can accommodate passengers
     * Each vehicle type has different passenger capacity rules
     * @param passengerCount Number of passengers
     * @return True if vehicle can accommodate passengers
     */
    public abstract boolean canAccommodatePassengers(int passengerCount);
    
    /**
     * Abstract method to get seating rules
     * Each vehicle type has different seating rules
     * @return String description of seating rules
     */
    public abstract String getSeatingRules();
    
    /**
     * Concrete method to start ride
     * @param passengerCount Number of passengers
     * @return True if ride started successfully
     */
    public boolean startRide(int passengerCount) {
        if (!isAvailable) {
            System.out.println("Vehicle is not available for ride");
            return false;
        }
        
        if (!canAccommodatePassengers(passengerCount)) {
            System.out.println("Vehicle cannot accommodate " + passengerCount + " passengers");
            return false;
        }
        
        this.currentPassengers = passengerCount;
        this.isAvailable = false;
        this.status = "In Transit";
        
        System.out.println("Ride started with " + passengerCount + " passengers");
        System.out.println("Vehicle: " + make + " " + model + " (" + year + ")");
        System.out.println("Driver: " + driverId);
        System.out.println("Location: " + location);
        
        return true;
    }
    
    /**
     * Concrete method to end ride
     * @param distance Distance traveled
     * @param duration Duration of ride
     * @param surgeMultiplier Surge pricing multiplier
     * @return True if ride ended successfully
     */
    public boolean endRide(double distance, double duration, double surgeMultiplier) {
        if (isAvailable) {
            System.out.println("No active ride to end");
            return false;
        }
        
        double fare = calculateFare(distance, duration, surgeMultiplier);
        this.totalEarnings += fare;
        this.totalRides++;
        this.currentPassengers = 0;
        this.isAvailable = true;
        this.status = "Available";
        
        System.out.println("Ride ended successfully");
        System.out.println("Distance: " + String.format("%.2f", distance) + " km");
        System.out.println("Duration: " + String.format("%.2f", duration) + " minutes");
        System.out.println("Fare: $" + String.format("%.2f", fare));
        System.out.println("Total earnings: $" + String.format("%.2f", totalEarnings));
        
        return true;
    }
    
    /**
     * Concrete method to update location
     * @param newLocation New location
     * @param latitude New latitude
     * @param longitude New longitude
     * @return True if location updated successfully
     */
    public boolean updateLocation(String newLocation, double latitude, double longitude) {
        this.location = newLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        
        System.out.println("Location updated to: " + newLocation);
        return true;
    }
    
    /**
     * Concrete method to add feature
     * @param feature Feature description
     * @return True if feature added successfully
     */
    public boolean addFeature(String feature) {
        if (feature == null || feature.trim().isEmpty()) {
            System.out.println("Error: Feature cannot be empty");
            return false;
        }
        
        if (featureCount >= maxFeatures) {
            System.out.println("Maximum features reached. Cannot add more features.");
            return false;
        }
        
        features[featureCount] = feature;
        featureCount++;
        System.out.println("Feature added: " + feature);
        return true;
    }
    
    /**
     * Concrete method to add amenity
     * @param amenity Amenity description
     * @return True if amenity added successfully
     */
    public boolean addAmenity(String amenity) {
        if (amenity == null || amenity.trim().isEmpty()) {
            System.out.println("Error: Amenity cannot be empty");
            return false;
        }
        
        if (amenityCount >= maxAmenities) {
            System.out.println("Maximum amenities reached. Cannot add more amenities.");
            return false;
        }
        
        amenities[amenityCount] = amenity;
        amenityCount++;
        System.out.println("Amenity added: " + amenity);
        return true;
    }
    
    /**
     * Concrete method to update rating
     * @param newRating New rating (1-5)
     * @return True if rating updated successfully
     */
    public boolean updateRating(double newRating) {
        if (newRating < 1.0 || newRating > 5.0) {
            System.out.println("Error: Rating must be between 1.0 and 5.0");
            return false;
        }
        
        this.rating = newRating;
        System.out.println("Rating updated to: " + String.format("%.1f", newRating));
        return true;
    }
    
    /**
     * Concrete method to add maintenance record
     * @param maintenanceRecord Maintenance record description
     * @return True if record added successfully
     */
    public boolean addMaintenanceRecord(String maintenanceRecord) {
        if (maintenanceRecord == null || maintenanceRecord.trim().isEmpty()) {
            System.out.println("Error: Maintenance record cannot be empty");
            return false;
        }
        
        if (maintenanceCount >= maxMaintenance) {
            System.out.println("Maximum maintenance records reached. Cannot add more records.");
            return false;
        }
        
        maintenanceHistory[maintenanceCount] = maintenanceRecord;
        maintenanceCount++;
        System.out.println("Maintenance record added: " + maintenanceRecord);
        return true;
    }
    
    /**
     * Concrete method to get vehicle information
     * @return String with vehicle details
     */
    public String getVehicleInfo() {
        return String.format("Vehicle: %s %s (%d), Type: %s, Status: %s, Location: %s, Rating: %.1f", 
                           make, model, year, vehicleType, status, location, rating);
    }
    
    /**
     * Concrete method to get earnings summary
     * @return String with earnings summary
     */
    public String getEarningsSummary() {
        return String.format("Earnings Summary: Total Rides: %d, Total Earnings: $%.2f, Average per Ride: $%.2f", 
                           totalRides, totalEarnings, totalRides > 0 ? totalEarnings / totalRides : 0.0);
    }
    
    /**
     * Concrete method to check if vehicle needs service
     * @return True if vehicle needs service
     */
    public boolean needsService() {
        return mileage > 10000 || (java.time.LocalDate.now().toString().compareTo(lastServiceDate) > 30);
    }
    
    /**
     * Getter for vehicle ID
     * @return The vehicle ID
     */
    public String getVehicleId() {
        return vehicleId;
    }
    
    /**
     * Getter for driver ID
     * @return The driver ID
     */
    public String getDriverId() {
        return driverId;
    }
    
    /**
     * Getter for vehicle type
     * @return The vehicle type
     */
    public String getVehicleType() {
        return vehicleType;
    }
    
    /**
     * Getter for make
     * @return The make
     */
    public String getMake() {
        return make;
    }
    
    /**
     * Getter for model
     * @return The model
     */
    public String getModel() {
        return model;
    }
    
    /**
     * Getter for year
     * @return The year
     */
    public int getYear() {
        return year;
    }
    
    /**
     * Getter for color
     * @return The color
     */
    public String getColor() {
        return color;
    }
    
    /**
     * Getter for license plate
     * @return The license plate
     */
    public String getLicensePlate() {
        return licensePlate;
    }
    
    /**
     * Getter for status
     * @return The status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Getter for location
     * @return The location
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Getter for latitude
     * @return The latitude
     */
    public double getLatitude() {
        return latitude;
    }
    
    /**
     * Getter for longitude
     * @return The longitude
     */
    public double getLongitude() {
        return longitude;
    }
    
    /**
     * Getter for max passengers
     * @return The maximum passengers
     */
    public int getMaxPassengers() {
        return maxPassengers;
    }
    
    /**
     * Getter for current passengers
     * @return The current passengers
     */
    public int getCurrentPassengers() {
        return currentPassengers;
    }
    
    /**
     * Getter for available status
     * @return True if vehicle is available
     */
    public boolean isAvailable() {
        return isAvailable;
    }
    
    /**
     * Getter for base fare
     * @return The base fare
     */
    public double getBaseFare() {
        return baseFare;
    }
    
    /**
     * Getter for per km rate
     * @return The per km rate
     */
    public double getPerKmRate() {
        return perKmRate;
    }
    
    /**
     * Getter for per minute rate
     * @return The per minute rate
     */
    public double getPerMinuteRate() {
        return perMinuteRate;
    }
    
    /**
     * Getter for fuel type
     * @return The fuel type
     */
    public String getFuelType() {
        return fuelType;
    }
    
    /**
     * Getter for fuel efficiency
     * @return The fuel efficiency
     */
    public double getFuelEfficiency() {
        return fuelEfficiency;
    }
    
    /**
     * Getter for transmission
     * @return The transmission
     */
    public String getTransmission() {
        return transmission;
    }
    
    /**
     * Getter for rating
     * @return The rating
     */
    public double getRating() {
        return rating;
    }
    
    /**
     * Getter for total rides
     * @return The total rides
     */
    public int getTotalRides() {
        return totalRides;
    }
    
    /**
     * Getter for total earnings
     * @return The total earnings
     */
    public double getTotalEarnings() {
        return totalEarnings;
    }
    
    /**
     * Override toString method
     * @return String representation of the vehicle
     */
    @Override
    public String toString() {
        return getVehicleInfo();
    }
}

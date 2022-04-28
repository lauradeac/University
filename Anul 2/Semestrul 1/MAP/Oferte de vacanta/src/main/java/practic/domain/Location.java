package practic.domain;

public class Location extends Entity<Long> {
    private Double locationId;
    private String locationName;

    public Location(Double locationId, String locationName) {
        this.locationId = locationId;
        this.locationName = locationName;
    }

    public Double getLocationId() {
        return locationId;
    }

    public Double setLocationId(Double locationId) {
        this.locationId = locationId;
        return null;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String toString() {
        return  locationId.intValue() + " Location: " + locationName;
    }
}

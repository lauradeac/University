package practic.domain;

public class Hotel extends Entity<Long>{
    private Double hotelId;
    private Double locationId;
    private String hotelName;
    private int noRooms;
    private Double pricePerNight;
    private Enum<Type> type;

    public Hotel(Double hotelId, Double locationId, String hotelName, int noRooms, Double pricePerNight, Enum<Type> type) {
        this.hotelId = hotelId;
        this.locationId = locationId;
        this.hotelName = hotelName;
        this.noRooms = noRooms;
        this.pricePerNight = pricePerNight;
        this.type = type;
    }

    public Double getHotelId() {
        return hotelId;
    }

    public void setHotelId(Double hotelId) {
        this.hotelId = hotelId;
    }

    public Double getLocationId() {
        return locationId;
    }

    public void setLocationId(Double locationId) {
        this.locationId = locationId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getNoRooms() {
        return noRooms;
    }

    public void setNoRooms(int noRooms) {
        this.noRooms = noRooms;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Enum<Type> getType() {
        return type;
    }

    public void setType(Enum<Type> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "hotelId=" + hotelId.intValue()+
                ", locationId=" + locationId.intValue() +
                ", hotelName='" + hotelName + '\'' +
                ", noRooms=" + noRooms +
                ", pricePerNight=" + pricePerNight.intValue() +
                ", type=" + type +
                '}';
    }
}

package practic.domain;

public class HotelDTO {
    private String hotelName;
    private int noRooms;
    private Double pricePerNight;
    private Enum<Type> type;

    public HotelDTO(String hotelName, int noRooms, Double pricePerNight, Enum<Type> type) {
        this.hotelName = hotelName;
        this.noRooms = noRooms;
        this.pricePerNight = pricePerNight;
        this.type = type;
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
        return "HotelDTO{" +
                "hotelName='" + hotelName + '\'' +
                ", noRooms=" + noRooms +
                ", pricePerNight=" + pricePerNight +
                ", type=" + type +
                '}';
    }
}

package practic.domain;

import java.time.LocalDate;

public class ClientOfferDTO {
    private String hotelName;
    private String locationName;
    private LocalDate startDate;
    private LocalDate endDate;

    public ClientOfferDTO(String hotelName, String locationName, LocalDate startDate, LocalDate endDate) {
        this.hotelName = hotelName;
        this.locationName = locationName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

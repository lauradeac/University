package practic.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reservation extends Entity<Long> {
    private Double reservationId;
    private Double clientId;
    private Double hotelId;
    private LocalDateTime startDate;
    private int noNights;

    public Reservation(Double reservationId, Double clientId, Double hotelId, LocalDateTime startDate, int noNights) {
        this.reservationId = reservationId;
        this.clientId = clientId;
        this.hotelId = hotelId;
        this.startDate = startDate;
        this.noNights = noNights;
    }

    public Double getReservationId() {
        return reservationId;
    }

    public void setReservationId(Double reservationId) {
        this.reservationId = reservationId;
    }

    public Double getClientId() {
        return clientId;
    }

    public void setClientId(Double clientId) {
        this.clientId = clientId;
    }

    public Double getHotelId() {
        return hotelId;
    }

    public void setHotelId(Double hotelId) {
        this.hotelId = hotelId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getNoNights() {
        return noNights;
    }

    public void setNoNights(int noNights) {
        this.noNights = noNights;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //("yyyy-MM-dd HH:mm");
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", clientId=" + clientId +
                ", hotelId=" + hotelId +
                ", startDate=" + startDate +
                ", noNights=" + noNights +
                '}';
    }
}

package practic.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SpecialOffer extends Entity<Long>{
    private Double specialOfferId;
    private Double hotelId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int percents;

    public SpecialOffer(Double specialOfferId, Double hotelId, LocalDate startDate, LocalDate endDate, int percents) {
        this.specialOfferId = specialOfferId;
        this.hotelId = hotelId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percents = percents;
    }

    public Double getSpecialOfferId() {
        return specialOfferId;
    }

    public void setSpecialOfferId(Double specialOfferId) {
        this.specialOfferId = specialOfferId;
    }

    public Double getHotelId() {
        return hotelId;
    }

    public void setHotelId(Double hotelId) {
        this.hotelId = hotelId;
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

    public int getPercents() {
        return percents;
    }

    public void setPercents(int percents) {
        this.percents = percents;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "SpecialOffer{" +
                "specialOfferId=" + specialOfferId.intValue() +
                ", hotelId=" + hotelId.intValue() +
                ", startDate=" + startDate.format(dtf) +
                ", endDate=" + endDate.format(dtf) +
                ", percents=" + percents +
                '}';
    }
}

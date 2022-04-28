package practic.repository;

import practic.domain.Reservation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationFileRepository extends AbstractFileRepository<Long, Reservation>{

    public ReservationFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    public Reservation extractEntity(List<String> attributes) {
        Reservation reservation = new Reservation(Double.parseDouble(attributes.get(0)), Double.parseDouble(attributes.get(1)),
                Double.parseDouble(attributes.get(2)), LocalDateTime.parse(attributes.get(3)), Integer.parseInt(attributes.get(4)));
        reservation.setId(Long.parseLong(attributes.get(0)));
        return reservation;
    }

    @Override
    protected String createEntityAsString(Reservation entity) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return entity.getReservationId().intValue()+";"+ entity.getClientId().intValue()+";"+entity.getHotelId().intValue()+";"+
                entity.getStartDate()+";"+entity.getNoNights();
    }
}

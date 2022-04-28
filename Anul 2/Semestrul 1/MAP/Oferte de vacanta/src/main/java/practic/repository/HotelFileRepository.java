package practic.repository;

import practic.domain.Hotel;
import practic.domain.Location;
import practic.domain.Type;

import java.io.ObjectInputFilter;
import java.net.Proxy;
import java.util.List;

public class HotelFileRepository extends AbstractFileRepository<Long, Hotel>{

    public HotelFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    public Hotel extractEntity(List<String> attributes) {
        Hotel hotel = new Hotel(Double.parseDouble(attributes.get(0)), Double.parseDouble(attributes.get(1)), attributes.get(2), Integer.parseInt(attributes.get(3)), Double.parseDouble(attributes.get(4)), Type.valueOf(attributes.get(5)));
        hotel.setId(Long.parseLong(attributes.get(0)));
        return hotel;
    }

    @Override
    protected String createEntityAsString(Hotel entity) {
        return entity.getHotelId()+ ";" + entity.getLocationId() + ";"
                + entity.getHotelName() + ";"+ entity.getNoRooms()+ ";"+
                  entity.getPricePerNight() + ";"+entity.getType();
    }


}

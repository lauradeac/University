package practic.repository;

import practic.domain.Location;

import java.util.List;

public class LocationFileRepository extends AbstractFileRepository<Long, Location> {

    public LocationFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    public Location extractEntity(List<String> attributes) {
        Location location = new Location(Double.parseDouble(attributes.get(0)), attributes.get(1));
        location.setId(Long.parseLong(attributes.get(0)));
        return location;
    }

    @Override
    protected String createEntityAsString(Location entity) {
        return entity.getLocationId() + ";" + entity.getLocationName();
    }
}

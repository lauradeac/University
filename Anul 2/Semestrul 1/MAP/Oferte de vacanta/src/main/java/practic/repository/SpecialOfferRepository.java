package practic.repository;

import practic.domain.SpecialOffer;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class SpecialOfferRepository extends AbstractFileRepository<Long, SpecialOffer>{

    public SpecialOfferRepository(String fileName) {
        super(fileName);
    }

    @Override
    public SpecialOffer extractEntity(List<String> attributes) {
        SpecialOffer specialOffer = new SpecialOffer(Double.parseDouble(attributes.get(0)), Double.parseDouble(attributes.get(1)), LocalDate.parse(attributes.get(2)), LocalDate.parse(attributes.get(3)), Integer.parseInt(attributes.get(4)));
        specialOffer.setId(Long.parseLong(attributes.get(0)));
        return specialOffer;
    }

    @Override
    protected String createEntityAsString(SpecialOffer entity) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return entity.getSpecialOfferId() + ";"+ entity.getHotelId()+";"+
                entity.getStartDate().format(dtf)+";"+entity.getEndDate().format(dtf)+";"+ entity.getPercents();
    }
}

package socialnetwork.repository.file;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.util.List;

public class PrieteniiFile extends AbstractFileRepository<Tuple<Long,Long>,Prietenie>{

    public PrieteniiFile(String fileName, Validator<Prietenie> validator) {
        super(fileName, validator);
    }

    /**
     * creeaza un el de tip prietenie dintr-un string
     * @param attributes lista de stringuri
     * @return el de tip prietenie
     */
    @Override
    public Prietenie extractEntity(List<String> attributes) {
        //TODO: implement method
        Prietenie p = new Prietenie(Long.parseLong(attributes.get(0)),Long.parseLong(attributes.get(1)),Integer.parseInt(attributes.get(2)),Integer.parseInt(attributes.get(3)),Integer.parseInt(attributes.get(4)));

        return p;
    }

    /**
     * concateneaza inf despre un obiect de tip Prietenie intr-un string
     * @param entity obiectul de tip Prietenie
     * @return stringul resprectiv
     */
    @Override
    protected String createEntityAsString(Prietenie entity) {
        return entity.getId().getLeft()+";"+entity.getId().getRight()+";"+entity.getYear()+";"+entity.getMonth()+";"+entity.getDay();
    }

}

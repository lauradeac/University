package practic.repository;

import practic.domain.Client;
import practic.domain.Hobby;
import practic.domain.Hotel;
import practic.domain.Type;

import java.util.List;

public class ClientFileRepository extends AbstractFileRepository<Long, Client>{

    /**
     * Constructorul clasei
     *
     * @param fileName - String
     */
    public ClientFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    public Client extractEntity(List<String> attributes) {
        Client client = new Client(Long.parseLong(attributes.get(0)), attributes.get(1), Integer.parseInt(attributes.get(2)), Integer.parseInt(attributes.get(3)), Hobby.valueOf(attributes.get(4)));
        client.setId(Long.parseLong(attributes.get(0)));
        return client;
    }

    @Override
    protected String createEntityAsString(Client entity) {
        return entity.getClientId() + ";"+ entity.getName()+";"+ entity.getFidelityGrade()+";"+ entity.getAge()+";"+entity.getHobby();
    }
}

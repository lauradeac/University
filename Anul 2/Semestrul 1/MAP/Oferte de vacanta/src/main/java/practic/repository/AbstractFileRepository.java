package practic.repository;


import practic.domain.Entity;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Aceasta clasa implementeaza sablonul de proiectare Template Method;
 */

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {

    String fileName;

    /**
     * Constructorul clasei
     * @param fileName - String
     */

    public AbstractFileRepository(String fileName) {

        this.fileName = fileName;
        loadData();
    }


    /**
     * Metoda care incarca datele din fisier
     */
    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String newLine;
            while ((newLine = reader.readLine()) != null) {
                List<String> data = Arrays.asList(newLine.split(";"));
                E entity = extractEntity(data);
                //System.out.println(newLine);
                super.save(entity);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes - List<String></>
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);

    /**
     * creeaza o entitate de tip E ca si String pt. scrierea in fisier
     * @param entity - E
     * @return string - o entitate de tip E
     */

    protected abstract String createEntityAsString(E entity);

    /**
     * metoda ce salveaza entitatile in fisier
     * @param entity entity must be not null
     * @return entitatea, daca e salvata deja
     *         si null, altfel
     */
    @Override
    public E save(E entity) {
        E result = super.save(entity);
        if (result == null)
            writeToFile(entity);
        // daca exista, il returneaza
        return result;
    }


    /**
     * metoda ce sterge din fisier o entitate de tip ID
     * @param id id must be not null
     * @return null - daca entitatea e stearsa
     */

    @Override
    public E delete(ID id)  {
//        E result = super.delete(id);
//        if (result!= null){
//            writeToFile(result);
//        }
//        return result;

        E result = super.findOne(id);
        if (result != null) {
            super.delete(id);
            Iterable<E> entities = super.findAll();
            PrintWriter pw = null;
            String fileName = "data/locations.txt";

//            if(super.getClass().getName().contains("MemberFileRepository")) {
//                fileName = "data/echipa.txt";
//            }
//            else fileName = "data/users.csv";
            try {
                pw = new PrintWriter(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            pw.close();
            for(E entity: entities){
                if (entity.getId() != id){
                    writeToFile(entity);
                }
            }
        }
        return null;

    }

    /**
     * metoda ce scrie entitatile in fisier
     * @param entity - E
     */
    protected void writeToFile(E entity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(createEntityAsString(entity));
            writer.newLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

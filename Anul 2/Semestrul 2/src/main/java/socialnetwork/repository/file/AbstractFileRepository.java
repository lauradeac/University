package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;

import java.util.Arrays;
import java.util.List;


public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
        loadData();

    }

    /**
     * incarca datele din fisier in memorie
     */
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){
                List<String> attr=Arrays.asList(linie.split(";"));
                E e=extractEntity(attr);
                super.save(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes list of attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);

    /**
     * concateneaza el despre un obiect E intr-un string
     * @param entity entitatea
     * @return stringul format
     */
    protected abstract String createEntityAsString(E entity);

    /**
     * salveaza un el in memorie si in fisier
     * @param entity el salvat
     * @return el adaugat
     */
    @Override
    public E save(E entity){
            E e = super.save(entity);
            if (e == null) {
                writeToFile(entity);
            }
            return e;

    }

    @Override
    public E update(E entity) {
        E e=super.update(entity);
        if (e==null)
        {
            writeToFileDelete();
        }
        return e;
    }

    protected void writeToFileDelete(){
        try(PrintWriter writer = new PrintWriter(fileName))
        {
            writer.print("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            for(E entity:findAll()){
                bW.write(createEntityAsString(entity));
                bW.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * sterge un el din memorie si fisier
     * @param id id-ul el sters
     * @return el sters
     */
    public E delete(ID id){
        E e=super.delete(id);
        if(e!=null) writeToFile();
        return e;
    }

    /**
     * adauga un obiect de tip E in fisier
     * @param entity el care va fi scris la sf fisierului
     */
    protected void writeToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * inlocuieste continutul fisierului cu el. din memorie
     */
    protected void writeToFile(){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,false))) {
            for(E entity:findAll()) {
                bW.write(createEntityAsString(entity));
                bW.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}


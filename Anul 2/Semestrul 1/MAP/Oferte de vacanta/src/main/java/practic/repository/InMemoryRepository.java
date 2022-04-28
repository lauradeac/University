package practic.repository;



import practic.domain.Entity;

import java.util.*;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    Map<ID,E> entities;

    /**
     * Constructorul clasei
     */

    public InMemoryRepository() {

        entities = new HashMap<ID,E>();
    }

    /**
     * Cauta o entitate
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return entitatea cu id-ul corespunzator
     * @throws IllegalArgumentException - daca id-ul e null
     */
    @Override
    public E findOne(ID id){

        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }


    /**
     * Cauta toate entitatile
     * @return toate entitatile
     */

    @Override
    public Iterable<E> findAll() {

        return entities.values();
    }


    /**
     * Metoda care salveaza o entitate in repo
     * @param entity entity must be not null
     * @return null - daca entitate a fost salvata
     *              - daca entitatea exista o returneaza, altfel
     * @throws IllegalArgumentException - daca entitatea e null
     */

    @Override
    public E save(E entity) {

        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");

        if(entity.getId()!= null) {
            if (entities.get(entity.getId()) != null) {
                return entity;
            } else {

                entities.put(entity.getId(), entity);
            }
        }
        return null;
    }


    /**
     * Metoda care sterge dupa id o entitate
     * @param id id must be not null
     * @return null - daca entitatea a fost stearsa
     * @throws IllegalArgumentException - id-ul entitatii cautate trebuie sa existe si sa nu fie null
     */

    @Override
    public E delete(ID id) {

        if(id==null)
            throw new IllegalArgumentException("id must be not null");
        if(findOne(id)==null){
            throw new IllegalArgumentException("It doesn't exist entity with given id");
        }
        else{
            entities.remove(id);
        }
        return null;
    }


    /**
     * Metoda care modifica o entitate
     * @param entity entity must not be null
     * @return null - daca entitatea s-a modificat
     *              - returneaza entitatea altfel
     * @throws IllegalArgumentException - daca id-ul entitatii este null
     */

    @Override
    public E update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");

        entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;
    }

}
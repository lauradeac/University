package persistence.repository;

public interface Repository<ID, T> {
    void add(T elem);
    void delete(T elem);
    void update(ID id, T elem);
    T findById(ID id);
    Iterable<T> findAll();
    //Collection<T> getAll();
}

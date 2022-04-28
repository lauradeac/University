package persistence.repository;


import model.Angajat;

public interface AngajatRepo extends Repository<Integer, Angajat>{
    Angajat getAngajatByUsername(String username);
}

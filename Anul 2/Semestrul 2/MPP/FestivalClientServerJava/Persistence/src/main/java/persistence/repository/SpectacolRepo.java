package persistence.repository;

import model.Spectacol;

import java.time.LocalDateTime;
import java.util.List;

public interface SpectacolRepo extends Repository<Integer, Spectacol>{

    List<Spectacol> findByDate(LocalDateTime date);
}

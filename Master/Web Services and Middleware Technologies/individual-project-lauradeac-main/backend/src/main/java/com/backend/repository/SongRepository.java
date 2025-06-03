package com.backend.repository;

import com.backend.domain.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {

    List<Song> findAllByPlaylistPlaylistId(Integer playlistId);

    Optional<Song> findByTitle(String title);
}

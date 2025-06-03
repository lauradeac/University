package com.backend.service;

import com.backend.domain.builder.PlaylistBuilder;
import com.backend.domain.builder.SongBuilder;
import com.backend.domain.dto.PlaylistDTO;
import com.backend.domain.dto.SongDTO;
import com.backend.domain.entity.Genre;
import com.backend.domain.entity.Playlist;
import com.backend.domain.entity.Song;
import com.backend.domain.exception.SongException;
import com.backend.repository.PlaylistRepository;
import com.backend.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    private final PlaylistRepository playlistRepository;

    private final SongBuilder songBuilder;

    public ResponseEntity<?> addSongToPlaylist(SongDTO songDTO) throws SongException {
        Optional<Song> verifyDuplicate = songRepository.findByTitle(songDTO.getTitle());
        if (verifyDuplicate.isPresent()) {
            throw new SongException("A song with the title: " + songDTO.getTitle() + "already exists.");
        }

        Optional<Playlist> playlist = playlistRepository.findById(Integer.valueOf(songDTO.getPlaylistId()));
        if (playlist.isEmpty()) {
            throw new SongException("Playlist does not exist");
        }

        Song song = songBuilder.generateEntityFromDTO(songDTO);
        songRepository.save(song);

        return ResponseEntity.status(HttpStatus.OK).body(SongBuilder.generateSongDTOFromEntity(song));
    }

    public ResponseEntity<?> editSong(SongDTO songDTO) throws SongException {
        Optional<Song> verifyDuplicate = songRepository.findById(Integer.valueOf(songDTO.getSongId()));
        if (verifyDuplicate.isEmpty()) {
            throw new SongException("A song does not exist with id: " + songDTO.getSongId());
        }

        Optional<Playlist> playlist = playlistRepository.findById(Integer.valueOf(songDTO.getPlaylistId()));
        if (playlist.isEmpty()) {
            throw new SongException("Playlist does not exist");
        }

        Song song = songBuilder.generateEntityFromDTO(songDTO);
        song.setSongId(Integer.parseInt(songDTO.getSongId()));
        songRepository.save(song);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    public ResponseEntity<?> getPlaylist(Integer id) throws SongException {
        Optional<Playlist> playlist = playlistRepository.findById(id);

        if (playlist.isEmpty()) {
            throw new SongException("Playlist does not exist.");
        }

        PlaylistDTO playlistDTO = PlaylistBuilder.generateDTOFromEntity(playlist.get());

        return ResponseEntity.status(HttpStatus.OK).body(playlistDTO);
    }

    public ResponseEntity<?> getAllSongsByPlaylistId(Integer playlistId) throws SongException {
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        List<Song> songs = songRepository.findAllByPlaylistPlaylistId(playlist.get().getPlaylistId());
        if (songs.isEmpty()) {
            throw new SongException("There are no songs in this playlist!");
        }
        return new ResponseEntity<>(songs
                .stream()
                .map(SongBuilder::generateSongDTOFromEntity)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteSongById(Integer songId) throws SongException {
        Optional<Song> song = songRepository.findById(songId);

        if (song.isPresent()) {
            songRepository.deleteById(songId);
        } else {
            throw new SongException("There are no song in this playlist with id: " + songId + "!");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public List<String> getAllGenres() {
        List<String> genres = new ArrayList<>();
        for (Genre genre : Genre.values()) {
            genres.add(genre.getDisplayName());
        }
        return genres;
    }


}

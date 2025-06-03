package org.example.service;

import lombok.AllArgsConstructor;
import org.example.domain.builder.PlaylistBuilder;
import org.example.domain.builder.SongBuilder;
import org.example.domain.dto.PlaylistDTO;
import org.example.domain.dto.SongDTO;
import org.example.domain.entity.Genre;
import org.example.domain.entity.Playlist;
import org.example.domain.entity.Song;
import org.example.domain.exception.SongException;
import org.example.domain.validation.SongValidator;
import org.example.repository.PlaylistRepository;
import org.example.repository.SongRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    private final PlaylistRepository playlistRepository;

    private final SongBuilder songBuilder;

    private static final Logger logger = LoggerFactory.getLogger(SongService.class);

    public ResponseEntity<?> addSongToPlaylist(SongDTO songDTO) throws SongException {
        logger.info("Song Service - Adding song...");
        Optional<Song> verifyDuplicate = songRepository.findByTitle(songDTO.getTitle());
        if (verifyDuplicate.isPresent()) {
            throw new SongException("A song with the title: " + songDTO.getTitle() + "already exists.");
        }

        Optional<Playlist> playlist = playlistRepository.findById(Integer.valueOf(songDTO.getPlaylistId()));
        if (playlist.isEmpty()) {
            throw new SongException("Playlist does not exist");
        }

        String errors = SongValidator.validate(songDTO);
        if (!errors.isEmpty()) {
            throw new SongException(errors);
        }
        Song song = songBuilder.generateEntityFromDTO(songDTO);
        songRepository.save(song);

        return ResponseEntity.status(HttpStatus.OK).body(SongBuilder.generateSongDTOFromEntity(song));
    }

    public ResponseEntity<?> editSong(SongDTO songDTO) throws SongException {
        logger.info("Song Service - Editing song...");
        Optional<Song> verifyDuplicate = songRepository.findById(songDTO.getSongId());
        if (verifyDuplicate.isEmpty()) {
            throw new SongException("A song does not exist with id: " + songDTO.getSongId());
        }

        Optional<Playlist> playlist = playlistRepository.findById(Integer.valueOf(songDTO.getPlaylistId()));
        if (playlist.isEmpty()) {
            throw new SongException("Playlist does not exist");
        }

        String errors = SongValidator.validate(songDTO);
        if (!errors.isEmpty()) {
            throw new SongException(errors);
        }

        Song song = songBuilder.generateEntityFromDTO(songDTO);
        song.setSongId(songDTO.getSongId());
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

    public List<SongDTO> getAllSongsByPlaylistId(Integer playlistId) throws SongException {
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        List<Song> songs = songRepository.findAllByPlaylistPlaylistId(playlist.get().getPlaylistId());
        if (songs.isEmpty()) {
            throw new SongException("There are no songs in this playlist!");
        }
        return songs.stream()
                .map(SongBuilder::generateSongDTOFromEntity)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> deleteSongById(Integer songId) throws SongException {
        logger.info("Song Service - Deleting song...");
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

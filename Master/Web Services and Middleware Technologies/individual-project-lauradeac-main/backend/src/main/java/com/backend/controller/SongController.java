package com.backend.controller;

import com.backend.domain.dto.SongDTO;
import com.backend.domain.exception.SongException;
import com.backend.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlist")
public class SongController {

    @Autowired
    private final SongService songService;

    @PostMapping("/add-song")
    public ResponseEntity<?> addSongToPlaylist(@RequestBody SongDTO songDTO) {
        try {
            return songService.addSongToPlaylist(songDTO);
        } catch (SongException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/edit-song")
    public ResponseEntity<?> editSong(@RequestBody SongDTO songDTO) {
        try {
            return songService.editSong(songDTO);
        } catch (SongException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getPlaylist(@PathVariable("id") Integer id) {
        try {
            return songService.getPlaylist(id);
        } catch (SongException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/songs/{playlistId}")
    public ResponseEntity<?> getAllSongsByPlaylistId(@PathVariable("playlistId") Integer playlistId) {
        try {
            return songService.getAllSongsByPlaylistId(playlistId);
        } catch (SongException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-song/{songId}")
    public ResponseEntity<?> deleteSongById(@PathVariable("songId") Integer songId) {
        try {
            return songService.deleteSongById(songId);
        } catch (SongException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/genres")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllGenres() {
        return songService.getAllGenres();
    }

}

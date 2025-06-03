package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.domain.dto.SongDTO;
import org.example.domain.entity.Message;
import org.example.domain.exception.SongException;
import org.example.rabbitmq.RabbitMqSender;
import org.example.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/playlist")
public class SongController {

    private RabbitMqSender rabbitMqSender;

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    @Autowired
    private final SongService songService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public SongController(RabbitMqSender rabbitMqSender, SongService songService) {
        this.rabbitMqSender = rabbitMqSender;
        this.songService = songService;
    }

    @Value("${app.message}")
    private String message;

    @PostMapping(value="/add-song")
    public ResponseEntity<?> addSongToPlaylist(@RequestBody SongDTO songDTO) throws SongException {
        try {
            logger.info("Song Controller(Producer) - Received request to add song to playlist.");
            String response = rabbitMqSender.send(new Message("CREATE", objectMapper.writeValueAsString(songDTO)));
            //songService.addSongToPlaylist(songDTO);
            logger.info("Song added successfully to playlist.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/edit-song")
    public ResponseEntity<?> editSong(@RequestBody SongDTO songDTO) {
        try {
            logger.info("Song Controller(Producer) - Received request to edit song from playlist.");
            String response = rabbitMqSender.send(new Message("UPDATE", objectMapper.writeValueAsString(songDTO)));
            logger.info("Song edited successfully to playlist.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
           // songService.editSong(songDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getPlaylist(@PathVariable("id") Integer id) {
        try {
            logger.info("Song Controller(Producer) - Received request to get playlist.");
            //String response = rabbitMqSender.send(new Message("READ", ""));
            logger.info("Song edited successfully to playlist.");
            //return ResponseEntity.status(HttpStatus.OK).body("\"" + response + "\"");
            return songService.getPlaylist(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/songs/{playlistId}")
    public List<SongDTO> getAllSongsByPlaylistId(@PathVariable("playlistId") Integer playlistId) throws JsonProcessingException {
            logger.info("Song Controller(Producer) - Received request to get all songs.");
            String response = rabbitMqSender.send(new Message("READ", ""));
            SongDTO[] cars = objectMapper.readValue(response, SongDTO[].class);
            return Arrays.asList(cars);
    }

    @DeleteMapping("/delete-song/{songId}")
    public void deleteSongById(@PathVariable("songId") Integer songId) {
        try {
            SongDTO songDTO = new SongDTO();
            songDTO.setSongId(songId);
            logger.info("Song Controller(Producer) - Received request to delete song from playlist.");
            rabbitMqSender.send(new Message("DELETE", objectMapper.writeValueAsString(songDTO)));
            //songService.deleteSongById(songId);
            logger.info("Song deleted successfully.");
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    @GetMapping("/genres")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllGenres() {
        return songService.getAllGenres();
    }


//    @PostMapping("/add-song")
//    public ResponseEntity<?> addSongToPlaylist(@RequestBody SongDTO songDTO) {
//        try {
//            return songService.addSongToPlaylist(songDTO);
//        } catch (SongException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//    @PutMapping("/edit-song")
//    public ResponseEntity<?> editSong(@RequestBody SongDTO songDTO) {
//        try {
//            return songService.editSong(songDTO);
//        } catch (SongException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//    @GetMapping("/getById/{id}")
//    public ResponseEntity<?> getPlaylist(@PathVariable("id") Integer id) {
//        try {
//            return songService.getPlaylist(id);
//        } catch (SongException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
//
//    @GetMapping("/songs/{playlistId}")
//    public ResponseEntity<?> getAllSongsByPlaylistId(@PathVariable("playlistId") Integer playlistId) {
//        try {
//            return songService.getAllSongsByPlaylistId(playlistId);
//        } catch (SongException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/delete-song/{songId}")
//    public ResponseEntity<?> deleteSongById(@PathVariable("songId") Integer songId) {
//        try {
//            return songService.deleteSongById(songId);
//        } catch (SongException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
//
//    @GetMapping("/genres")
//    @ResponseStatus(HttpStatus.OK)
//    public List<String> getAllGenres() {
//        return songService.getAllGenres();
//    }

}

package org.example.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.dto.SongDTO;
import org.example.domain.entity.Message;
import org.example.domain.exception.SongException;
import org.example.service.SongService;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RabbitListener(queues = "${spring.rabbitmq.queue}")
public class RabbitMqReceiver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final SongService songService;

    @Autowired
    public RabbitMqReceiver(SongService songService) {
        this.songService = songService;
    }

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);

    @RabbitHandler
    public String receivedMessage(Message message) throws SongException, JsonProcessingException {
        logger.info("Song Details Received By Consumer is.. " + message);

        String messagePrefix = "Consumer1: ";
        logger.info(messagePrefix + "Am primit mesajul: " + message.toString());
        SongDTO songDTO;

        switch (message.getOperation()) {
            case "CREATE":
                songDTO = objectMapper.readValue(message.getMessage(), SongDTO.class);
                ResponseEntity<?> addedSong = songService.addSongToPlaylist(songDTO);
                System.out.println(messagePrefix + "Added the following Song: " + addedSong);
                return objectMapper.writeValueAsString(addedSong);
            case "READ":
                List<SongDTO> Songs = songService.getAllSongsByPlaylistId(1);
                System.out.println(messagePrefix + "Read the following Songs: " + objectMapper.writeValueAsString(Songs));
                return objectMapper.writeValueAsString(Songs);
            case "UPDATE":
                songDTO = objectMapper.readValue(message.getMessage(), SongDTO.class);
                ResponseEntity<?> updatedSong = songService.editSong(songDTO);
                System.out.println(messagePrefix + "Updated the following Song " + updatedSong);
                return objectMapper.writeValueAsString(updatedSong);
            case "DELETE":
                songDTO = objectMapper.readValue(message.getMessage(), SongDTO.class);
                songService.deleteSongById(songDTO.getSongId());
                System.out.println(messagePrefix + "Deleted the Song with the following id " + songDTO.getSongId());
                return "";
            default:
                System.err.println(messagePrefix + "Received unknown message " + message);
        }
        return "";
    }
}

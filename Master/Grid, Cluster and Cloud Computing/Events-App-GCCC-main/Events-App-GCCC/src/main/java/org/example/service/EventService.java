package org.example.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.RequiredArgsConstructor;
import org.example.domain.builder.EventBuilder;
import org.example.domain.dto.EventDTO;
import org.example.domain.entity.Category;
import org.example.domain.entity.Event;
import org.example.domain.exception.EventException;
import org.example.domain.validation.EventValidator;
import org.example.repository.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final EventBuilder eventBuilder;

    public ResponseEntity<?> addEvent(EventDTO eventDTO) throws EventException, IOException {
        Optional<Event> verifyDuplicate = eventRepository.findByNameAndLocationAndDateTime(
                eventDTO.getName(), eventDTO.getLocation(), LocalDateTime.parse(eventDTO.getDateTime()));
        if (verifyDuplicate.isPresent()) {
            throw new EventException("An event with the name: " + eventDTO.getName() + "already exists.");
        }

        String errors = EventValidator.validate(eventDTO);
        if (!errors.isEmpty()) {
            throw new EventException(errors);
        }

        // Generăm un nume unic pentru imagine
        String uniqueBlobName = generateUniqueBlobName();

        // Construim URL-ul Blob pentru imagine
        String blobUrl = constructBlobUrl(uniqueBlobName);

        // Încărcăm imaginea în Azure Blob Storage și actualizăm URL-ul imaginii în obiectul Event
        uploadImageToBlobStorage(eventDTO.getFile().getBytes(), uniqueBlobName, "gccblobevents", "photos", "gPKuRiTZC7Ozle5yHMP/3vzlatbBpOzRt63/3L/0rae8j02i26QryX0XrXm30z5Stf7p12pIljV++AStHQb7eQ==");

        // Creăm obiectul Event
        Event event = Event.builder()
                .name(eventDTO.getName())
                .location(eventDTO.getLocation())
                .dateTime(LocalDateTime.parse(eventDTO.getDateTime()))
                .category(Category.valueOf(eventDTO.getCategory()))
                .description(eventDTO.getDescription())
                .imageUrl(blobUrl)
                .build();

        eventRepository.save(event);

        return ResponseEntity.status(HttpStatus.OK).body(EventBuilder.generateEventDTOFromEntity(event));
    }

    public String uploadImageToBlobStorage(byte[] imageData, String imageNameParam, String accountName, String containerName, String accountKey) {
        BlobServiceClientBuilder builder = new BlobServiceClientBuilder()
                .connectionString("DefaultEndpointsProtocol=https;AccountName=" + accountName + ";AccountKey=" + accountKey + ";EndpointSuffix=core.windows.net");
        BlobContainerClient containerClient = builder.buildClient().getBlobContainerClient(containerName);

        // Încarcă datele imaginii într-un obiect ByteArrayInputStream
        InputStream inputStream = new ByteArrayInputStream(imageData);

        // Creează un client pentru blob și încarcă imaginea în containerul Azure Blob
        BlobClient blobClient = containerClient.getBlobClient(imageNameParam);
        blobClient.upload(inputStream, imageData.length);

        // Returnează URL-ul imaginii încărcate
        return "https://" + accountName + ".blob.core.windows.net/" + containerName + "/" + imageNameParam;
    }

    private String generateUniqueBlobName() {
        return UUID.randomUUID() + ".jpg";
    }

    private String constructBlobUrl(String uniqueBlobName) {
        String baseUrl = "https://gccblobevents.blob.core.windows.net/";
        String containerName = "photos";

        return baseUrl + containerName + "/" + uniqueBlobName;
    }

    public ResponseEntity<?> editEvent(EventDTO eventDTO) throws EventException {
        Optional<Event> eventExists = eventRepository.findById(eventDTO.getEventId());
        if (eventExists.isEmpty()) {
            throw new EventException("An event does not exist with id: " + eventDTO.getEventId());
        }

        String errors = EventValidator.validate(eventDTO);
        if (!errors.isEmpty()) {
            throw new EventException(errors);
        }

        Event event = eventBuilder.generateEntityFromDTO(eventDTO);
        event.setEventId(eventDTO.getEventId());
        eventRepository.save(event);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<?> getAllEvents() throws EventException {
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty()) {
            throw new EventException("There are no events in this database!");
        }
        return new ResponseEntity<>(events
                .stream()
                .map(EventBuilder::generateEventDTOFromEntity)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteEventById(Integer eventId) throws EventException {
        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isPresent()) {
            eventRepository.deleteById(eventId);
        } else {
            throw new EventException("There are no event in the database with id: " + eventId + "!");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        for (Category category : Category.values()) {
            categories.add(category.getDisplayName());
        }
        return categories;
    }

    public EventDTO getEventById(Integer eventId) throws EventException {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Event event = optionalEvent.orElseThrow(() -> new EventException("Evenimentul cu id-ul " + eventId + " nu a fost găsit."));
        return eventBuilder.generateEventDTOFromEntity(event);
    }
}

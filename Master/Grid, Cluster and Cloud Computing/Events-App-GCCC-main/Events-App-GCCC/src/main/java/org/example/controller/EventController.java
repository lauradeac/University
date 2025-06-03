package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.EventDTO;
import org.example.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.events.EventException;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/events")
public class EventController {

    @Autowired
    private final EventService eventService;

    @PostMapping(value = "/add")
    public ResponseEntity<?> addEvent(@ModelAttribute EventDTO eventDTO) throws org.example.domain.exception.EventException {
        try {
            return eventService.addEvent(eventDTO);
        } catch (EventException | IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editEvent(@ModelAttribute EventDTO eventDTO) throws org.example.domain.exception.EventException {
        try {
            return eventService.editEvent(eventDTO);
        } catch (EventException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllEvents() throws org.example.domain.exception.EventException {
        try {
            return eventService.getAllEvents();
        } catch (EventException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<?> deleteEventById(@PathVariable("eventId") Integer eventId) throws org.example.domain.exception.EventException {
        try {
            return eventService.deleteEventById(eventId);
        } catch (EventException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/getById/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable("eventId") Integer eventId) throws org.example.domain.exception.EventException {
        try {
            EventDTO event = eventService.getEventById(eventId);
            if (event != null) {
                return ResponseEntity.ok(event);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EventException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare la obținerea evenimentului: " + e.getMessage());
        }
    }

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllCategories() {
        return eventService.getAllCategories();
    }
}

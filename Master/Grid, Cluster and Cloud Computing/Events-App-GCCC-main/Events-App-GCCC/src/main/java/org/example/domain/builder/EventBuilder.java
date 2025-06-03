package org.example.domain.builder;

import org.example.domain.dto.EventDTO;
import org.example.domain.entity.Category;
import org.example.domain.entity.Event;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventBuilder {

    public static EventDTO generateEventDTOFromEntity(Event event) {
        return EventDTO.builder()
                .eventId(event.getEventId())
                .name(event.getName())
                .location(event.getLocation())
                .dateTime(String.valueOf(event.getDateTime()))
                .category(event.getCategory().getDisplayName())
                .description(event.getDescription())
                .imageUrl(event.getImageUrl())
                .build();
    }

    public Event generateEntityFromDTO(EventDTO eventDTO) {
        return Event.builder()
                .name(eventDTO.getName())
                .location(eventDTO.getLocation())
                .dateTime(LocalDateTime.parse(eventDTO.getDateTime()))
                .category(Category.valueOf(eventDTO.getCategory()))
                .description(eventDTO.getDescription())
                .imageUrl(eventDTO.getImageUrl())
                .build();
    }
}

package org.example.domain.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventDTO {

    private Integer eventId;

    private String name;

    private String location;

    private String dateTime;

    private String category;

    private String description;

    private String imageUrl;

    private MultipartFile file;
}

package com.backend.domain.dto;

import com.backend.domain.entity.Genre;
import com.backend.domain.entity.Playlist;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SongDTO {

    private String songId;

    private String title;

    private String artist;

    private Integer duration;

    private String genre;

    private String playlistId;
}

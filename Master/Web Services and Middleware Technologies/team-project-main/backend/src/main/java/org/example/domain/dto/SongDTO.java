package org.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SongDTO {

    private Integer songId;

    private String title;

    private String artist;

    private Integer duration;

    private String genre;

    private String playlistId;

    public SongDTO() {}

    @Override
    public String toString() {
        return "SongDTO{" +
                "songId='" + songId + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", genre='" + genre + '\'' +
                ", playlistId='" + playlistId + '\'' +
                '}';
    }
}

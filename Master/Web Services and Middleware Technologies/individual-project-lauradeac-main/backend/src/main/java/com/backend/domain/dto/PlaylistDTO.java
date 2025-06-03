package com.backend.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PlaylistDTO {

    private String id;

    private String name;

    private String description;

    private List<SongDTO> songList;
}

package com.backend.domain.builder;

import com.backend.domain.dto.PlaylistDTO;
import com.backend.domain.dto.SongDTO;
import com.backend.domain.entity.Playlist;
import com.backend.domain.entity.Song;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlaylistBuilder {
    public static PlaylistDTO generateDTOFromEntity(Playlist playlist) {
        List<SongDTO> songDTOList = generateSongsListForPlaylist(playlist);

        return PlaylistDTO.builder()
                .id(String.valueOf(playlist.getPlaylistId()))
                .name(playlist.getName())
                .description(playlist.getDescription())
                .songList(songDTOList)
                .build();
    }

    private static List<SongDTO> generateSongsListForPlaylist(Playlist playlist) {
        List<SongDTO> songDTOList = new ArrayList<>();
        List<Song> songs = playlist.getSongs();
        return songs.isEmpty()
                ? songDTOList
                : songs
                .stream()
                .map(SongBuilder::generateSongsDTOFromEntity)
                .collect(Collectors.toList());
    }
}

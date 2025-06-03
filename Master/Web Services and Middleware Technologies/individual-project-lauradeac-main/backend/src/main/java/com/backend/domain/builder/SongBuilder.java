package com.backend.domain.builder;

import com.backend.domain.dto.SongDTO;
import com.backend.domain.entity.Genre;
import com.backend.domain.entity.Song;
import com.backend.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SongBuilder {

    @Autowired
    public PlaylistRepository playlistRepository;

    public static SongDTO generateSongsDTOFromEntity(Song song) {
        return SongDTO.builder()
                .songId(String.valueOf(song.getSongId()))
                .title(song.getTitle())
                .artist(song.getArtist())
                .duration(song.getDuration())
                .genre(song.getGenre().getDisplayName())
                .playlistId(String.valueOf(song.getPlaylist().getPlaylistId()))
                .build();
    }

    public static SongDTO generateSongDTOFromEntity(Song song) {
        return SongDTO.builder()
                .songId(String.valueOf(song.getSongId()))
                .title(song.getTitle())
                .artist(song.getArtist())
                .duration(song.getDuration())
                .genre(song.getGenre().getDisplayName())
                .playlistId(String.valueOf(song.getPlaylist().getPlaylistId()))
                .build();
    }

    public Song generateEntityFromDTO(SongDTO songDTO) {
        return Song.builder()
                .title(songDTO.getTitle())
                .artist(songDTO.getArtist())
                .duration(songDTO.getDuration())
                .genre(Genre.valueOf(songDTO.getGenre()))
                .playlist(playlistRepository.findById(Integer.valueOf(songDTO.getPlaylistId())).get())
                .build();
    }
}

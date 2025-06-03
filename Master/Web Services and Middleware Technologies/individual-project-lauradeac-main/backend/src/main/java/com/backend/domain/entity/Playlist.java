package com.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "playlist")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id", unique = true, nullable = false)
    private int playlistId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Song> songs;

    @Override
    public String toString() {
        return "Playlist{" +
                "playlistId=" + playlistId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}



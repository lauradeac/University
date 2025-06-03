package com.backend.domain.entity;

public enum Genre {
    ROCK("ROCK"),
    POP("POP"),
    HIP_HOP("HIP_HOP"),
    CLASSICAL("CLASSICAL"),
    JAZZ("JAZZ"),
    COUNTRY("COUNTRY"),
    ELECTRONIC("ELECTRONIC"),
    REGGAE("REGGAE"),
    FOLK("FOLK"),
    METAL("METAL");

    private String genre;

    Genre(String genre) {
        this.genre = genre;
    }

    public String getDisplayName() {
        return genre;
    }

    Genre() {}

    public static Genre fromString(String str) {
        for (Genre genre : Genre.values()) {
            if (genre.genre.equalsIgnoreCase(str)) {
                return genre;
            }
        }
        return null;
    }
}
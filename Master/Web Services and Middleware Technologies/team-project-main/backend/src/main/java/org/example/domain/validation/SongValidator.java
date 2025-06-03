package org.example.domain.validation;


import org.example.domain.dto.SongDTO;

public class SongValidator {
    public static String validate(SongDTO song) {
        StringBuilder errors = new StringBuilder();

        if (!isValidString(song.getTitle())) {
            errors.append("Invalid title! Title must contain only characters.\n");
        }

        if (!isValidString(song.getArtist())) {
            errors.append("Invalid artist! Artist must contain only characters.\n");
        }

        if (song.getDuration() <= 0) {
            errors.append("Invalid duration! Duration must be a positive number.\n");
        }

        if (song.getGenre() == null) {
            errors.append("Invalid genre!\n");
        }

        return errors.toString();
    }

    private static boolean isValidString(String str) {
        return str != null && !str.isEmpty() && str.matches("[a-zA-Z ]+");
    }
}

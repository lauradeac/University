package org.example.domain.validation;

import org.example.domain.dto.EventDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class EventValidator {
    public static String validate(EventDTO event) {
        StringBuilder errors = new StringBuilder();

        if (isValidString(event.getName())) {
            errors.append("Invalid name! Name must contain only characters.\n");
        }

        if (!isValidLocation(event.getLocation())) {
            errors.append("Invalid location!\n");
        }

        if (event.getDateTime() == null || !isValidDateTime(event.getDateTime())) {
            errors.append("Invalid Date!\n");
        }

        if (isValidString(event.getDescription())) {
            errors.append("Invalid description!\n");
        }

        if (event.getCategory() == null) {
            errors.append("Invalid category!\n");
        }

        return errors.toString();
    }

    private static boolean isValidString(String str) {
        return str == null || str.isEmpty() || !str.matches("[a-zA-Z ]+");
    }

    private static boolean isValidLocation(String str) {
        return str != null && !str.isEmpty();
    }

    private static boolean isValidDateTime(String dateTime) {
        try {
            LocalDateTime.parse(dateTime); // Try parsing the dateTime string
            return true; // If successful, it's a valid dateTime
        } catch (DateTimeParseException e) {
            return false; // Otherwise, it's invalid
        }
    }

}

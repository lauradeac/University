package model.exceptions;

/**
 * Clasa de exceptii pt Service
 */
public class Exception extends RuntimeException {
    public Exception() {
    }

    public Exception(String message) {
        super(message);
    }
}

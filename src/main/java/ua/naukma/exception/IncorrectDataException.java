package ua.naukma.exception;

public class IncorrectDataException extends IllegalArgumentException {
    public IncorrectDataException(String message) {
        super(message);
    }
}

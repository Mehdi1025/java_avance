package fr.uha.miage;

public class NotAnEnumException extends IllegalArgumentException {
    public NotAnEnumException(String message) {
        super(message);
    }
}
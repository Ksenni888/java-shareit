package ru.practicum.shareit.exeption;

public class NoInformationFoundException extends RuntimeException {
    public NoInformationFoundException(String message) {
        super(message);
    }
}
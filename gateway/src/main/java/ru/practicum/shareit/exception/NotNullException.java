package ru.practicum.shareit.exception;

public class NotNullException extends RuntimeException {
    public NotNullException(String reason) {
        super(reason);
    }
}
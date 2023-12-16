package ru.practicum.shareit.exeption;

public class ExistExeption extends RuntimeException {
    public ExistExeption(String message) {
        super(message);
    }
}
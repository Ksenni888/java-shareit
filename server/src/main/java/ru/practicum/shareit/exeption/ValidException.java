package ru.practicum.shareit.exeption;

    public class ValidException extends RuntimeException {
        public ValidException(String message) {
            super(message);
        }
    }
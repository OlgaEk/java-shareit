package ru.practicum.shareit.exception;

public class AccessNotAllowed extends RuntimeException {
    public AccessNotAllowed(String message) {
        super(message);
    }
}

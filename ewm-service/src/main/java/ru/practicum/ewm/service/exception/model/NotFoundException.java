package ru.practicum.ewm.service.exception.model;

public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(message, "The required object was not found.");
    }
}

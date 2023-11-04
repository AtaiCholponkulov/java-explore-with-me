package ru.practicum.ewm.service.exception.model;

public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(message, "Integrity constraint has been violated.");
    }
}

package ru.practicum.ewm.service.exception.model;

public class BadRequestException extends BaseException {
    public BadRequestException(String message) {
        super(message, "Incorrectly made request.");
    }
}

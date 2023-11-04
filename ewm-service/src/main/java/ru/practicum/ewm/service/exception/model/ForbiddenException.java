package ru.practicum.ewm.service.exception.model;

public class ForbiddenException extends BaseException{
    public ForbiddenException(String message) {
        super(message, "For the requested operation the conditions are not met.");
    }
}

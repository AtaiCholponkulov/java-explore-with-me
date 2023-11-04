package ru.practicum.ewm.service.exception.model;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    protected final String reason;
    protected BaseException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}

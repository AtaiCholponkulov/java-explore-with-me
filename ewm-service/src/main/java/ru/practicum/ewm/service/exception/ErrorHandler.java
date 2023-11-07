package ru.practicum.ewm.service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.service.exception.model.BadRequestException;
import ru.practicum.ewm.service.exception.model.ConflictException;
import ru.practicum.ewm.service.exception.model.ForbiddenException;
import ru.practicum.ewm.service.exception.model.NotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.warn(e.getMessage(), e);
        return ApiError.builder()
                .message(e.getMessage())
                .status("INTERNAL_SERVER_ERROR")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException e) {
        log.warn(e.getMessage(), e);
        return ApiError.builder()
                .message(e.getMessage())
                .reason(e.getReason())
                .status("BAD_REQUEST")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final MissingServletRequestParameterException e) {
        log.warn(e.getMessage(), e);
        return ApiError.builder()
                .message(e.getMessage())
                .status("BAD_REQUEST")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(final ForbiddenException e) {
        log.warn(e.getMessage(), e);
        return ApiError.builder()
                .message(e.getMessage())
                .reason(e.getReason())
                .status("FORBIDDEN")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.warn(e.getMessage(), e);
        return ApiError.builder()
                .message(e.getMessage())
                .reason(e.getReason())
                .status("NOT_FOUND")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNotFoundException(final ConflictException e) {
        log.warn(e.getMessage(), e);
        return ApiError.builder()
                .message(e.getMessage())
                .reason(e.getReason())
                .status("CONFLICT")
                .timestamp(LocalDateTime.now())
                .build();
    }
}

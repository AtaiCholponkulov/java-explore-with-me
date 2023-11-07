package ru.practicum.ewm.service.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {

    private final String errors;
    private final String message;
    private final String reason;
    private final String status;
    private final LocalDateTime timestamp;
}

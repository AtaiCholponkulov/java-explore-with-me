package ru.practicum.ewm.service.valid;

import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.category.NewCategoryDto;
import ru.practicum.ewm.service.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.service.dto.compilation.UpdateCompilationRequestDto;
import ru.practicum.ewm.service.dto.event.*;
import ru.practicum.ewm.service.dto.user.NewUserRequestDto;
import ru.practicum.ewm.service.exception.model.BadRequestException;
import ru.practicum.ewm.service.exception.model.ForbiddenException;
import ru.practicum.ewm.service.model.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static void validatePaginationParams(int from, int size) {
        if (from < 0) {
            throw new BadRequestException("Request parameter: from. Error: negative. Value: " + from);
        } else if (size < 1) {
            throw new BadRequestException("Request parameter: size. Error: negative or zero. Value: " + size);
        }
    }

    public static void validateRangeStartAndEndParams(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Request parameters: rangeStart, rangeEnd. " +
                    "Error: the start is after the end. Values: rangeStart=" + rangeStart + ", rangeEnd=" + rangeEnd);
        }
    }

    public static void validateNewUser(NewUserRequestDto user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Field: name. Error: empty. Value: " + name);
        } else if (name.length() < 2 || name.length() > 250) {
            throw new BadRequestException("Field: name. Error: shorter than 2 or longer than 250. Value: " + name);
        }

        String email = user.getEmail();
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Field: email. Error: empty. Value: " + email);
        } else if (email.length() < 6
                || email.length() > 254
                || !email.contains("@")
                || email.indexOf("@") != email.lastIndexOf("@")
                || email.indexOf("@") > 64
                || email.substring(email.indexOf("@") + 1).indexOf(".") > 63) {
            throw new BadRequestException("Field: email. Error: wrong format. Value: " + email);
        }
    }

    public static void validateNewEvent(NewEventDto event) {
        String annotation = event.getAnnotation();
        if (annotation == null || annotation.isBlank()) {
            throw new BadRequestException("Field: annotation. Error: empty. Value: " + annotation);
        } else if (annotation.length() < 20 || annotation.length() > 2000) {
            throw new BadRequestException("Field: annotation. Error: shorter than 20 or longer than 2000. Value: " + annotation);
        }

        if (event.getCategory() == null) {
            throw new BadRequestException("Field: category. Error: empty. Value: null");
        }

        String description = event.getDescription();
        if (description == null || description.isBlank()) {
            throw new BadRequestException("Field: description. Error: empty. Value: " + description);
        } else if (description.length() < 20 || description.length() > 7000) {
            throw new BadRequestException("Field: description. Error: shorter than 20 or longer than 7000. Value: " + description);
        }

        if (event.getEventDate() == null) {
            throw new BadRequestException("Field: eventDate. Error: empty. Value: null");
        } else {
            LocalDateTime eventDate;
            try {
                eventDate = LocalDateTime.parse(event.getEventDate(), DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new BadRequestException("Field: eventDate. Error: wrong format, must be yyyy-MM-dd HH:mm:ss. Value: " + event.getEventDate());
            }
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Field: eventDate. Error: date in past or too soon. Value: " + eventDate);
            }
        }

        LocationDto location = event.getLocation();
        if (location == null) {
            throw new BadRequestException("Field: location. Error: empty. Value: null");
        } else {
            if (location.getLat() == null) {
                throw new BadRequestException("Field: location.lat. Error: empty. Value: null");
            } else if (location.getLat() < -90 || location.getLat() > 90) {
                throw new ForbiddenException("Field: location.lat. Error: impossible value. Value: " + location.getLat());
            }
            if (location.getLon() == null) {
                throw new BadRequestException("Field: location.lon. Error: empty. Value: null");
            } else if (location.getLon() < -180 || location.getLon() > 180) {
                throw new ForbiddenException("Field: location.lon. Error: impossible value. Value: " + location.getLon());
            }
        }

        if (event.getPaid() == null) {
            event.setPaid(false);
        }

        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }

        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }

        String title = event.getTitle();
        if (title == null || title.isBlank()) {
            throw new BadRequestException("Field: title. Error: empty. Value: " + title);
        } else if (title.length() < 3 || title.length() > 120) {
            throw new BadRequestException("Field: title. Error: shorter than 3 or longer than 120. Value: " + title);
        }
    }

    public static void validateEventUserUpdate(UpdateEventUserRequestDto eventUpdate) {
        String annotation = eventUpdate.getAnnotation();
        if (annotation != null) {
            if (annotation.isBlank()) {
                throw new BadRequestException("Field: annotation. Error: empty. Value: " + annotation);
            } else if (annotation.length() < 20 || annotation.length() > 2000) {
                throw new BadRequestException("Field: annotation. Error: shorter than 20 or longer than 2000. Value: " + annotation);
            }
        }

        String description = eventUpdate.getDescription();
        if (description != null) {
            if (description.isBlank()) {
                throw new BadRequestException("Field: description. Error: empty. Value: " + description);
            } else if (description.length() < 20 || description.length() > 7000) {
                throw new BadRequestException("Field: description. Error: shorter than 20 or longer than 7000. Value: " + description);
            }
        }

        if (eventUpdate.getEventDate() != null) {
            try {
                LocalDateTime eventDate = LocalDateTime.parse(eventUpdate.getEventDate(), DATE_TIME_FORMATTER);
                if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                    throw new BadRequestException("Field: eventDate. Error: date in past or too soon. Value: " + eventDate);
                }
            } catch (DateTimeParseException e) {
                throw new BadRequestException("Field: eventDate. Error: wrong format, must be yyyy-MM-dd HH:mm:ss. Value: " + eventUpdate.getEventDate());
            }
        }

        LocationDto location = eventUpdate.getLocation();
        if (location != null) {
            if (location.getLat() != null && (location.getLat() < -90 || location.getLat() > 90)) {
                throw new ForbiddenException("Field: location.lat. Error: impossible value. Value: " + location.getLat());
            }
            if (location.getLon() != null && (location.getLon() < -180 || location.getLon() > 180)) {
                throw new ForbiddenException("Field: location.lon. Error: impossible value. Value: " + location.getLon());
            }
        }

        String title = eventUpdate.getTitle();
        if (title != null) {
            if (title.isBlank()) {
                throw new BadRequestException("Field: title. Error: empty. Value: " + title);
            } else if (title.length() < 3 || title.length() > 120) {
                throw new BadRequestException("Field: title. Error: shorter than 3 or longer than 120. Value: " + title);
            }
        }
    }

    public static void validateEventAdminUpdate(UpdateEventAdminRequestDto eventUpdate) {
        String annotation = eventUpdate.getAnnotation();
        if (annotation != null) {
            if (annotation.isBlank()) {
                throw new BadRequestException("Field: annotation. Error: empty. Value: " + annotation);
            } else if (annotation.length() < 20 || annotation.length() > 2000) {
                throw new BadRequestException("Field: annotation. Error: shorter than 20 or longer than 2000. Value: " + annotation);
            }
        }

        String description = eventUpdate.getDescription();
        if (description != null) {
            if (description.isBlank()) {
                throw new BadRequestException("Field: description. Error: empty. Value: " + description);
            } else if (description.length() < 20 || description.length() > 7000) {
                throw new BadRequestException("Field: description. Error: shorter than 20 or longer than 7000. Value: " + description);
            }
        }

        if (eventUpdate.getEventDate() != null) {
            try {
                LocalDateTime eventDate = LocalDateTime.parse(eventUpdate.getEventDate(), DATE_TIME_FORMATTER);
                if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                    throw new BadRequestException("Field: eventDate. Error: date in past or too soon. Value: " + eventDate);
                }
            } catch (DateTimeParseException e) {
                throw new BadRequestException("Field: eventDate. Error: wrong format, must be yyyy-MM-dd HH:mm:ss. Value: " + eventUpdate.getEventDate());
            }
        }

        LocationDto location = eventUpdate.getLocation();
        if (location != null) {
            if (location.getLat() != null && (location.getLat() < -90 || location.getLat() > 90)) {
                throw new ForbiddenException("Field: location.lat. Error: impossible value. Value: " + location.getLat());
            }
            if (location.getLon() != null && (location.getLon() < -180 || location.getLon() > 180)) {
                throw new ForbiddenException("Field: location.lon. Error: impossible value. Value: " + location.getLon());
            }
        }

        String title = eventUpdate.getTitle();
        if (title != null) {
            if (title.isBlank()) {
                throw new BadRequestException("Field: title. Error: empty. Value: " + title);
            } else if (title.length() < 3 || title.length() > 120) {
                throw new BadRequestException("Field: title. Error: shorter than 3 or longer than 120. Value: " + title);
            }
        }
    }

    public static void validate(EventRequestStatusUpdateRequestDto requestStatusUpdates) {
        if (requestStatusUpdates.getRequestIds() == null || requestStatusUpdates.getRequestIds().isEmpty()) {
            throw new BadRequestException("Field: requestIds. Error: empty. Value: " + requestStatusUpdates.getRequestIds());
        }
        String status = requestStatusUpdates.getStatus();
        if (status == null) {
            throw new BadRequestException("Field: status. Error: empty. Value: null");
        }
        try {
            switch (Status.valueOf(status)) {
                case PENDING:
                case CANCELED:
                    throw new BadRequestException("Field: status. Error: wrong value. Value: " + status);
            }
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Field: status. Error: wrong value. Value: " + status);
        }

    }

    public static void validateNewCategory(NewCategoryDto newCategory) {
        validateCategoryName(newCategory.getName());
    }

    public static void validateUpdateCategory(CategoryDto cat) {
        validateCategoryName(cat.getName());
    }

    private static void validateCategoryName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Field: name. Error: empty. Value: " + name);
        } else if (name.length() < 2 || name.length() > 50) {
            throw new BadRequestException("Field: name. Error: shorter than 1 or longer than 50. Value: " + name);
        }
    }

    public static void validateNewCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }

        String title = newCompilationDto.getTitle();
        if (title == null || title.isBlank()) {
            throw new BadRequestException("Field: title. Error: empty. Value: " + title);
        } else if (title.isEmpty() || title.length() > 50) {
            throw new BadRequestException("Field: title. Error: shorter than 1 or longer than 50. Value: " + title);
        }
    }

    public static void validateUpdateCompilationRequest(UpdateCompilationRequestDto compilationUpdate) {
        String title = compilationUpdate.getTitle();
        if (title != null) {
            if (title.isBlank()) {
                throw new BadRequestException("Field: title. Error: empty. Value: " + title);
            } else if (title.isEmpty() || title.length() > 50) {
                throw new BadRequestException("Field: title. Error: shorter than 1 or longer than 50. Value: " + title);
            }
        }
    }
}

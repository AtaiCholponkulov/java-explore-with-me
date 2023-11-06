package ru.practicum.ewm.service.mapper;

import ru.practicum.ewm.service.dto.event.*;
import ru.practicum.ewm.service.model.Category;
import ru.practicum.ewm.service.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.valid.Validator.DATE_TIME_FORMATTER;

public class EventMapper {

    public static Event map(NewEventDto newEvent) {
        return Event.builder()
                .annotation(newEvent.getAnnotation())
                .category(new Category(newEvent.getCategory(), ""))
                .description(newEvent.getDescription())
                .eventDate(LocalDateTime.parse(newEvent.getEventDate(), DATE_TIME_FORMATTER))
                .latitude(newEvent.getLocation().getLat())
                .longitude(newEvent.getLocation().getLon())
                .paid(newEvent.getPaid())
                .participantLimit(newEvent.getParticipantLimit())
                .requestModeration(newEvent.getRequestModeration())
                .title(newEvent.getTitle())
                .build();
    }

    public static EventFullDto mapToFullDto(Event event) {
        return new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.map(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getId(),
                UserMapper.mapToShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                0,
                event.getCreatedOn(),
                event.getDescription(),
                LocationDto.builder()
                        .lat(event.getLatitude())
                        .lon(event.getLongitude())
                        .build(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState());
    }

    public static List<EventFullDto> mapToFullDtos(List<Event> events) {
        return events.stream()
                .map(EventMapper::mapToFullDto)
                .collect(Collectors.toList());
    }

    public static EventShortDto mapToShortDto(Event event) {
        return new EventShortDto(

                event.getAnnotation(),
                CategoryMapper.map(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getId(),
                UserMapper.mapToShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                0);
    }

    public static List<EventShortDto> mapToShortDtos(List<Event> events) {
        return events.stream()
                .map(EventMapper::mapToShortDto)
                .collect(Collectors.toList());
    }
}

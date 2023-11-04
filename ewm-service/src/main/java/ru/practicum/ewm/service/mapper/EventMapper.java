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
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.map(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.mapToShortDto(event.getInitiator()))
                .location(Location.builder()
                        .lat(event.getLatitude())
                        .lon(event.getLongitude())
                        .build())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }

    public static List<EventFullDto> mapToFullDtos(List<Event> events) {
        return events.stream()
                .map(EventMapper::mapToFullDto)
                .collect(Collectors.toList());
    }

    public static EventShortDto mapToShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.map(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.mapToShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public static List<EventShortDto> mapToShortDtos(List<Event> events) {
        return events.stream()
                .map(EventMapper::mapToShortDto)
                .collect(Collectors.toList());
    }
}

package ru.practicum.ewm.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.event.*;
import ru.practicum.ewm.service.service.PrivateService;

import java.util.List;

import static ru.practicum.ewm.service.mapper.EventMapper.map;
import static ru.practicum.ewm.service.mapper.EventMapper.mapToFullDto;
import static ru.practicum.ewm.service.mapper.EventMapper.mapToShortDtos;
import static ru.practicum.ewm.service.mapper.ParticipationRequestMapper.map;
import static ru.practicum.ewm.service.valid.Validator.*;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class PrivateController {

    private final PrivateService service;

    //-------------------------------------------------Private: События-------------------------------------------------
    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByInitiator(@PathVariable int userId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        validatePaginationParams(from, size);
        return mapToShortDtos(service.getEventsByInitiator(userId, from, size));
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable int userId,
                                 @RequestBody NewEventDto newEvent) {
        validateNewEvent(newEvent);
        return mapToFullDto(service.addEvent(map(newEvent), userId));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByInitiator(@PathVariable int userId,
                                            @PathVariable int eventId) {
        return mapToFullDto(service.getEventByInitiator(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventByInitiator(@PathVariable int userId,
                                               @PathVariable int eventId,
                                               @RequestBody UpdateEventUserRequest eventUpdate) {
        validateEventUserUpdate(eventUpdate);
        return mapToFullDto(service.updateEventByInitiator(userId, eventId, eventUpdate));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForEventByInitiator(@PathVariable int userId,
                                                                        @PathVariable int eventId) {
        return map(service.getRequestsForEventByInitiator(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusOfRequestsByInitiator(@PathVariable int userId,
                                                                            @PathVariable int eventId,
                                                                            @RequestBody EventRequestStatusUpdateRequest requestStatusUpdates) {
        validate(requestStatusUpdates);
        return service.updateStatusOfRequestsByInitiator(userId, eventId, requestStatusUpdates);
    }

    //--------------------------------------------Private: Запросы на участие-------------------------------------------
    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getParticipationsByRequester(@PathVariable int userId) {
        return map(service.getParticipationsByRequester(userId));
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable int userId,
                                                           @RequestParam int eventId) {
        return map(service.addParticipationRequest(userId, eventId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationByRequester(@PathVariable int userId,
                                                                  @PathVariable int requestId) {
        return map(service.cancelParticipationByRequester(userId, requestId));
    }
}

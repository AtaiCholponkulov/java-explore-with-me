package ru.practicum.ewm.service.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.dto.event.*;

import java.util.List;

@Service
public interface PrivateService {

    /** Private: События */
    List<EventShortDto> getEventsByInitiator(int userId, int from, int size);

    EventFullDto addEvent(NewEventDto newEvent, int userId);

    EventFullDto getEventByInitiator(int userId, int eventId);

    EventFullDto updateEventByInitiator(int userId, int eventId, UpdateEventUserRequestDto eventUpdate);

    List<ParticipationRequestDto> getRequestsForEventByInitiator(int userId, int eventId);

    EventRequestStatusUpdateResultDto updateStatusOfRequestsByInitiator(int userId, int eventId, EventRequestStatusUpdateRequestDto requestStatusUpdates);

    /** Private: Запросы на участие */
    List<ParticipationRequestDto> getParticipationsByRequester(int userId);

    ParticipationRequestDto addParticipationRequest(int userId, int eventId);

    ParticipationRequestDto cancelParticipationByRequester(int userId, int requestId);
}

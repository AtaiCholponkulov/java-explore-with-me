package ru.practicum.ewm.service.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.service.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.ewm.service.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.service.model.Event;
import ru.practicum.ewm.service.model.ParticipationRequest;

import java.util.List;

@Service
public interface PrivateService {

    //-------------------------------------------------Private: События-------------------------------------------------
    List<Event> getEventsByInitiator(int userId, int from, int size);

    Event addEvent(Event newEvent, int userId);

    Event getEventByInitiator(int userId, int eventId);

    Event updateEventByInitiator(int userId, int eventId, UpdateEventUserRequest eventUpdate);

    List<ParticipationRequest> getRequestsForEventByInitiator(int userId, int eventId);

    EventRequestStatusUpdateResult updateStatusOfRequestsByInitiator(int userId, int eventId, EventRequestStatusUpdateRequest requestStatusUpdates);

    //--------------------------------------------Private: Запросы на участие-------------------------------------------
    List<ParticipationRequest> getParticipationsByRequester(int userId);

    ParticipationRequest addParticipationRequest(int userId, int eventId);

    ParticipationRequest cancelParticipationByRequester(int userId, int requestId);
}

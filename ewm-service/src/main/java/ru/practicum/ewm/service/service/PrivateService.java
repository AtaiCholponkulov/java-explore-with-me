package ru.practicum.ewm.service.service;

import ru.practicum.ewm.service.dto.comment.CommentDto;
import ru.practicum.ewm.service.dto.comment.CommentWithSubsDto;
import ru.practicum.ewm.service.dto.comment.TextCommentDto;
import ru.practicum.ewm.service.dto.event.*;

import java.util.List;

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

    /**
     * Private: Комментарии
     */
    List<CommentWithSubsDto> getCommentsByCommenter(int commenterId, int from, int size);

    CommentDto addComment(TextCommentDto comment, int eventAuthorId, int eventId, int commenterId);

    CommentDto addSubComment(int commentAuthorId, int commentId, TextCommentDto subComment, int subCommentAuthorId);

    CommentDto getCommentByCommenter(int commenterId, int commentId);

    CommentDto updateCommentByCommenter(int commenterId, int commentId, TextCommentDto commentUpdate);

    void deleteCommentByCommenter(int commenterId, int commentId);
}

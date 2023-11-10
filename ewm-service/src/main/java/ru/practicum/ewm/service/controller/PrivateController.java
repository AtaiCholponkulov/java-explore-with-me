package ru.practicum.ewm.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.comment.CommentDto;
import ru.practicum.ewm.service.dto.comment.ParentComment;
import ru.practicum.ewm.service.dto.comment.SubCommentDto;
import ru.practicum.ewm.service.dto.comment.TextCommentDto;
import ru.practicum.ewm.service.dto.event.*;
import ru.practicum.ewm.service.service.PrivateService;

import java.util.List;

import static ru.practicum.ewm.service.valid.Validator.*;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class PrivateController {

    private final PrivateService service;

    /** Private: События */
    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByInitiator(@PathVariable int userId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        validatePaginationParams(from, size);
        return service.getEventsByInitiator(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable int userId,
                                 @RequestBody NewEventDto newEvent) {
        validateNewEvent(newEvent);
        return service.addEvent(newEvent, userId);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByInitiator(@PathVariable int userId,
                                            @PathVariable int eventId) {
        return service.getEventByInitiator(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventByInitiator(@PathVariable int userId,
                                               @PathVariable int eventId,
                                               @RequestBody UpdateEventUserRequestDto eventUpdate) {
        validateEventUserUpdate(eventUpdate);
        return service.updateEventByInitiator(userId, eventId, eventUpdate);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForEventByInitiator(@PathVariable int userId,
                                                                        @PathVariable int eventId) {
        return service.getRequestsForEventByInitiator(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto updateStatusOfRequestsByInitiator(@PathVariable int userId,
                                                                               @PathVariable int eventId,
                                                                               @RequestBody EventRequestStatusUpdateRequestDto requestStatusUpdates) {
        validate(requestStatusUpdates);
        return service.updateStatusOfRequestsByInitiator(userId, eventId, requestStatusUpdates);
    }

    /** Private: Запросы на участие */
    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getParticipationsByRequester(@PathVariable int userId) {
        return service.getParticipationsByRequester(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable int userId,
                                                           @RequestParam int eventId) {
        return service.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationByRequester(@PathVariable int userId,
                                                                  @PathVariable int requestId) {
        return service.cancelParticipationByRequester(userId, requestId);
    }

    /** Private: Комментарии */
    @GetMapping("/{userId}/comments")
    public List<ParentComment> getCommentsByCommenter(@PathVariable(name = "userId") int commenterId,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        validatePaginationParams(from, size);
        return service.getCommentsByCommenter(commenterId, from, size);
    }

    @PostMapping("/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable(name = "userId") int eventAuthorId,
                                 @PathVariable int eventId,
                                 @RequestBody TextCommentDto comment,
                                 @RequestParam(name = "userId") int commenterId) {
        validate(comment);
        return service.addComment(comment, eventAuthorId, eventId, commenterId);
    }

    @PostMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SubCommentDto addSubComment(@PathVariable(name = "userId") int commentAuthorId,
                                       @PathVariable int commentId,
                                       @RequestBody TextCommentDto subComment,
                                       @RequestParam(name = "userId") int subCommentAuthorId) {
        validate(subComment);
        return service.addSubComment(commentAuthorId, commentId, subComment, subCommentAuthorId);
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public CommentDto getCommentByCommenter(@PathVariable(name = "userId") int commenterId,
                                            @PathVariable int commentId) {
        return service.getCommentByCommenter(commenterId, commentId);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public CommentDto updateCommentByCommenter(@PathVariable(name = "userId") int commenterId,
                                               @PathVariable int commentId,
                                               @RequestBody TextCommentDto commentUpdate) {
        validateCommentUpdate(commentUpdate);
        return service.updateCommentByCommenter(commenterId, commentId, commentUpdate);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByCommenter(@PathVariable(name = "userId") int commenterId,
                                         @PathVariable int commentId) {
        service.deleteCommentByCommenter(commenterId, commentId);
    }
}

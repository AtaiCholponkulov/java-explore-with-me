package ru.practicum.ewm.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.dto.comment.CommentDto;
import ru.practicum.ewm.service.dto.comment.TextCommentDto;
import ru.practicum.ewm.service.dto.event.*;
import ru.practicum.ewm.service.exception.model.ConflictException;
import ru.practicum.ewm.service.exception.model.ForbiddenException;
import ru.practicum.ewm.service.exception.model.NotFoundException;
import ru.practicum.ewm.service.mapper.EventMapper;
import ru.practicum.ewm.service.model.*;
import ru.practicum.ewm.service.repository.*;

import java.time.LocalDateTime;
import java.util.*;

import static ru.practicum.ewm.service.mapper.EventMapper.mapToFullDto;
import static ru.practicum.ewm.service.mapper.EventMapper.mapToShortDtos;
import static ru.practicum.ewm.service.mapper.ParticipationRequestMapper.map;
import static ru.practicum.ewm.service.mapper.CommentMapper.map;
import static ru.practicum.ewm.service.valid.Validator.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateServiceImpl implements PrivateService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;
    private final CommentRepository commentRepository;
    private static final String EVENT_AUTHOR_COMMENT_PREFIX = "Comment by event initiator: ";

    /** Private: События */
    @Override
    public List<EventShortDto> getEventsByInitiator(int userId, int from, int size) {
        /* check user existence */
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        return mapToShortDtos(eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size)));
    }

    @Override
    @Transactional
    public EventFullDto addEvent(NewEventDto newEventDto, int userId) {
        Event newEvent = EventMapper.map(newEventDto);

        /* check user existence */
        newEvent.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found")));

        /* check category existence */
        int categoryId = newEvent.getCategory().getId();
        newEvent.setCategory(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + categoryId + " was not found")));

        /* set creation date and time */
        newEvent.setCreatedOn(LocalDateTime.now());

        /* set state */
        newEvent.setState(State.PENDING);

        return mapToFullDto(eventRepository.save(newEvent));
    }

    @Override
    public EventFullDto getEventByInitiator(int userId, int eventId) {
        /* check user existence */
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        /* check event existence and its initiator */
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("User with id=" + userId + " is not owner of event with id=" + eventId);
        }
        return mapToFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByInitiator(int userId, int eventId, UpdateEventUserRequestDto eventUpdate) {
        /* check user existence */
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        /* check event existence and its initiator */
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("User with id=" + userId + " is not owner of event with id=" + eventId);
        }

        /* check event state */
        switch (event.getState()) {
            case PUBLISHED:
            case CANCELED:
                throw new ConflictException("Only pending or canceled events can be changed");
        }

        /* update fields if needed */
        if (eventUpdate.getAnnotation() != null) {
            event.setAnnotation(eventUpdate.getAnnotation());
        }

        Integer categoryId = eventUpdate.getCategory();
        if (categoryId != null) {
            categoryRepository.findById(categoryId)
                    .ifPresentOrElse(event::setCategory, () -> {
                        throw new NotFoundException("Category with id=" + categoryId + " was not found");
                    });
        }

        if (eventUpdate.getDescription() != null) {
            event.setDescription(eventUpdate.getDescription());
        }

        if (eventUpdate.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(eventUpdate.getEventDate(), DATE_TIME_FORMATTER));
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException("Field: eventDate. Error: date in past or too soon. Value: " + event.getEventDate());
        }

        LocationDto location = eventUpdate.getLocation();
        if (location != null) {
            if (location.getLon() != null) {
                event.setLongitude(location.getLon());
            }
            if (location.getLat() != null) {
                event.setLatitude(location.getLat());
            }
        }

        if (eventUpdate.getPaid() != null) {
            event.setPaid(eventUpdate.getPaid());
        }

        if (eventUpdate.getParticipantLimit() != null) {
            if (eventUpdate.getParticipantLimit() != 0
                    && eventUpdate.getParticipantLimit() < event.getConfirmedRequests()) {
                throw new ConflictException("Field: participantLimit. Error: conflict with 'event' confirmedRequests. Value: " + eventUpdate.getParticipantLimit());
            }
            event.setParticipantLimit(eventUpdate.getParticipantLimit());
        }

        if (eventUpdate.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdate.getRequestModeration());
        }

        if (eventUpdate.getStateAction() != null) {
            switch (eventUpdate.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
            }
        }

        if (eventUpdate.getTitle() != null) {
            event.setTitle(eventUpdate.getTitle());
        }
        return mapToFullDto(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsForEventByInitiator(int userId, int eventId) {
        /* check user existence */
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        /* check event existence and its initiator */
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("User with id=" + userId + " is not owner of event with id=" + eventId);
        }

        List<ParticipationRequest> requests = requestRepository.findAllByEventId(eventId);
        return requests != null ? map(requests) : new ArrayList<>();
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResultDto updateStatusOfRequestsByInitiator(int userId, int eventId, EventRequestStatusUpdateRequestDto requestStatusUpdates) {
        /* check user existence */
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        /* check event existence and its initiator */
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("User with id=" + userId + " is not owner of event with id=" + eventId);
        }

        /* get all 'event' participation requests */
        List<ParticipationRequest> requests = requestRepository.findAllByEventIdOrderById(eventId);
        Set<Integer> ids = new HashSet<>(requestStatusUpdates.getRequestIds());

        Status newStatus = Status.valueOf(requestStatusUpdates.getStatus());
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        List<ParticipationRequest> pendingRequests = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            if (!ids.contains(request.getId())) {
                switch (request.getStatus()) {
                    case CONFIRMED:
                        confirmedRequests.add(map(request));
                        break;
                    case REJECTED:
                        rejectedRequests.add(map(request));
                        break;
                    case PENDING:
                        pendingRequests.add(request);
                }
                continue;
            }
            Status oldStatus = request.getStatus();
            switch (newStatus) {
                case REJECTED:
                    if (oldStatus == Status.PENDING) {
                        request.setStatus(newStatus);
                    } else if (oldStatus != Status.REJECTED) {
                        throw new ConflictException("Field 'status' can be changed if it is PENDING");
                    }
                    rejectedRequests.add(map(request));
                    break;
                case CONFIRMED:
                    if (oldStatus == Status.PENDING) {
                        if (event.getParticipantLimit() != 0
                                && Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
                            throw new ConflictException("The participant limit has been reached");
                        }
                        request.setStatus(newStatus);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    } else if (oldStatus != Status.CONFIRMED) {
                        throw new ConflictException("Field 'status' can be changed if it is PENDING");
                    }
                    confirmedRequests.add(map(request));
            }
            ids.remove(request.getId());
        }

        if (!ids.isEmpty()) {
            throw new NotFoundException("Participation request with id=" + ids.iterator().next() + " was not found");
        }

        if (newStatus == Status.CONFIRMED
                && event.getConfirmedRequests() != 0
                && Objects.equals(event.getParticipantLimit(), event.getConfirmedRequests())) {
            for (ParticipationRequest pendingRequest : pendingRequests) {
                pendingRequest.setStatus(Status.REJECTED);
                rejectedRequests.add(map(pendingRequest));
            }
        }
        return new EventRequestStatusUpdateResultDto(confirmedRequests, rejectedRequests);
    }

    /** Private: Запросы на участие */
    @Override
    public List<ParticipationRequestDto> getParticipationsByRequester(int userId) {
        /* check user existence */
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(userId);
        return requests != null ? map(requests) : new ArrayList<>();
    }

    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(int userId, int eventId) {
        /* check user existence */
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        /* check event existence */
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        /* check participation request not existence */
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Participation request already exists");
        }

        /* check requester is not event initiator */
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("Participation request author is event initiator");
        }

        /* check event is published */
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event with id=" + eventId + " is not published");
        }

        /* set status */
        Status status = Status.PENDING;
        if (event.getParticipantLimit() != 0 && Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
            /* # of confirmed met participant limit */
            throw new ConflictException("Event with id=" + eventId + " is full");
        } else if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            status = Status.CONFIRMED;
            /* increment # of confirmed req-s */
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        /* add request */
        return map(requestRepository.save(
                ParticipationRequest.builder()
                        .created(LocalDateTime.now())
                        .event(event)
                        .requester(requester)
                        .status(status)
                        .build()));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationByRequester(int userId, int requestId) {
        /* check user existence */
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        /* check participation request existence and its author */
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));
        if (request.getRequester().getId() != userId) {
            throw new NotFoundException("User with id=" + userId + " is not owner of participation request with id=" + requestId);
        }

        /* check request status is PENDING */
        if (request.getStatus() != Status.PENDING) {
            throw new NotFoundException("Request with id=" + requestId + " was not found");
        }
        request.setStatus(Status.CANCELED);
        return map(requestRepository.save(request));
    }

    /** Private: Комментарии */
    @Override
    public List<CommentDto> getCommentsByCommenter(int commenterId) {
        /* check commenter existence */
        if (!userRepository.existsById(commenterId)) {
            throw new NotFoundException("User with id=" + commenterId + " was not found");
        }
        return map(commentRepository.findAllByAuthorIdOrderByCreatedOnDesc(commenterId));
    }

    @Override
    @Transactional
    public CommentDto addComment(TextCommentDto newComment, int eventAuthorId, int eventId, int commenterId) {
        /* check event author and commenter existence */
        if (!userRepository.existsById(eventAuthorId)) {
            throw new NotFoundException("User with id=" + eventAuthorId + " was not found");
        }
        User commenter = userRepository.findById(commenterId)
                .orElseThrow(() -> new NotFoundException("User with id=" + commenterId + " was not found"));

        /* check event existence and it is PUBLISHED */
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        if (event.getInitiator().getId() != eventAuthorId) {
            throw new NotFoundException("No such event with id=" + eventId + " and with initiator with id=" + eventAuthorId);
        }

        return map(commentRepository.save(Comment.builder()
                .text(eventAuthorId == commenterId
                        ? EVENT_AUTHOR_COMMENT_PREFIX + newComment.getText()
                        : newComment.getText())
                .author(commenter)
                .event(event)
                .createdOn(LocalDateTime.now())
                .build()));
    }

    @Override
    public CommentDto getCommentByCommenter(int commenterId, int commentId) {
        /* check commenter and comment existence */
        if (!userRepository.existsById(commenterId)) {
            throw new NotFoundException("User with id=" + commenterId + " was not found");
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));

        /* check comment author */
        if (comment.getAuthor().getId() != commenterId) {
            throw new NotFoundException("User with id=" + commenterId + " is not owner of the comment with id=" + commentId);
        }
        return map(comment);
    }

    @Override
    @Transactional
    public CommentDto updateCommentByCommenter(int commenterId, int commentId, TextCommentDto commentUpdate) {
        /* check user and comment existence */
        if (!userRepository.existsById(commenterId)) {
            throw new NotFoundException("User with id=" + commenterId + " was not found");
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));

        /* check comment author */
        if (comment.getAuthor().getId() != commenterId) {
            throw new ConflictException("Cannot edit because user with id=" + commenterId + " is not author of the comment with id=" + commentId);
        }

        if (commentUpdate.getText() != null) {
            comment.setText(comment.getEvent().getInitiator().getId() == commenterId
                    ? EVENT_AUTHOR_COMMENT_PREFIX + commentUpdate.getText()
                    : commentUpdate.getText());
        }
        return map(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteCommentByCommenter(int commenterId, int commentId) {
        /* check user and comment existence */
        if (!userRepository.existsById(commenterId)) {
            throw new NotFoundException("User with id=" + commenterId + " was not found");
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));

        /* check comment author */
        if (comment.getAuthor().getId() != commenterId) {
            throw new ConflictException("Cannot delete because user with id=" + commenterId + " is not author of the comment with id=" + commentId);
        }

        categoryRepository.deleteById(commentId);
    }
}

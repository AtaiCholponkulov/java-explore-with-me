package ru.practicum.ewm.service.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.category.NewCategoryDto;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.service.dto.compilation.UpdateCompilationRequestDto;
import ru.practicum.ewm.service.dto.event.AdminStateAction;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.LocationDto;
import ru.practicum.ewm.service.dto.event.UpdateEventAdminRequestDto;
import ru.practicum.ewm.service.dto.user.NewUserRequestDto;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.exception.model.ConflictException;
import ru.practicum.ewm.service.exception.model.NotFoundException;
import ru.practicum.ewm.service.mapper.EventMapper;
import ru.practicum.ewm.service.model.*;
import ru.practicum.ewm.service.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.mapper.CompilationMapper.map;
import static ru.practicum.ewm.service.mapper.EventMapper.mapToFullDto;
import static ru.practicum.ewm.service.mapper.EventMapper.mapToFullDtos;
import static ru.practicum.ewm.service.mapper.UserMapper.map;
import static ru.practicum.ewm.service.mapper.UserMapper.mapToDto;
import static ru.practicum.ewm.service.mapper.CategoryMapper.map;
import static ru.practicum.ewm.service.valid.Validator.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final CompilationEventRepository compilationEventRepository;

    /** Admin: Пользователи */
    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        List<User> res = new ArrayList<>();
        if (ids == null) { /* no ids were provided - get all users */
            res = userRepository.findAll(page).getContent();
        } else if (!ids.isEmpty()) { /* ids provided */
            res = userRepository.findByIdIn(ids, page);
        }
        return map(res);
    }

    @Override
    @Transactional
    public UserDto addUser(NewUserRequestDto newUser) {
        if (userRepository.existsByName(newUser.getName())) {
            throw new ConflictException("Field: name. Error: reserved. Value: " + newUser.getName());
        }
        return mapToDto(userRepository.save(map(newUser)));
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    /** Admin: Категории */
    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto cat) {
        if (categoryRepository.existsByName(cat.getName())) {
            throw new ConflictException("Field: name. Error: already exists. Value: " + cat.getName());
        }
        return map(categoryRepository.save(map(cat)));
    }

    @Override
    @Transactional
    public void deleteCategory(int catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("The category is not empty");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto cat) {
        if (!categoryRepository.existsById(cat.getId())) {
            throw new NotFoundException("Category with id=" + cat.getId() + " was not found");
        }
        categoryRepository.findByName(cat.getName()).ifPresent(dbCategory -> {
            if (!Objects.equals(dbCategory.getId(), cat.getId())) {
                throw new ConflictException("Field: name. Error: already exists. Value: " + cat.getName());
            }
        });
        return map(categoryRepository.save(map(cat)));
    }

    /** Admin: События */
    @Override
    public List<EventFullDto> getEvents(List<Integer> users,
                                        List<State> states,
                                        List<Integer> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        int from,
                                        int size) {
        QEvent event = QEvent.event;
        BooleanExpression predicate = event.isNotNull();

        if (users != null && !users.isEmpty()) {
            predicate = predicate.and(event.initiator.id.in(users));
        }

        if (states != null && !states.isEmpty()) {
            predicate = predicate.and(event.state.in(states));
        }

        if (categories != null && !categories.isEmpty()) {
            predicate = predicate.and(event.category.id.in(categories));
        }

        if (rangeStart != null) {
            predicate = predicate.and(event.createdOn.goe(rangeStart));
        }

        if (rangeEnd != null) {
            predicate = predicate.and(event.createdOn.loe(rangeEnd));
        }

        return mapToFullDtos(eventRepository.findAll(predicate, PageRequest.of(from / size, size)).getContent());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(int eventId, UpdateEventAdminRequestDto eventUpdate) {
        /* check event existence */
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        /* change event fields if needed */
        String annotation = eventUpdate.getAnnotation();
        Integer categoryId = eventUpdate.getCategory();
        String description = eventUpdate.getDescription();
        String eventDate = eventUpdate.getEventDate();
        LocationDto location = eventUpdate.getLocation();
        Boolean paid = eventUpdate.getPaid();
        Integer participantLimit = eventUpdate.getParticipantLimit();
        Boolean requestModeration = eventUpdate.getRequestModeration();
        AdminStateAction stateAction = eventUpdate.getStateAction();
        String title = eventUpdate.getTitle();

        if (annotation != null) {
            event.setAnnotation(annotation);
        }

        if (categoryId != null) {
            categoryRepository.findById(categoryId)
                    .ifPresentOrElse(event::setCategory, () -> {
                        throw new NotFoundException("Category with id=" + categoryId + " was not found");
                    });
        }

        if (description != null) {
            event.setDescription(description);
        }

        if (eventDate != null) {
            event.setEventDate(LocalDateTime.parse(eventDate, DATE_TIME_FORMATTER));
        }

        if (location != null) {
            if (location.getLon() != null) {
                event.setLongitude(location.getLon());
            }
            if (location.getLat() != null) {
                event.setLatitude(location.getLat());
            }
        }

        if (paid != null) {
            event.setPaid(paid);
        }

        if (participantLimit != null) {
            if (participantLimit != 0
                    && participantLimit < event.getConfirmedRequests()) {
                throw new ConflictException("Field: participantLimit. Error: conflict with 'event' confirmedRequests. Value: " + participantLimit);
            }
            event.setParticipantLimit(participantLimit);
        }

        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }

        if (stateAction != null) {
            switch (stateAction) {
                case PUBLISH_EVENT:
                    if (event.getState() != State.PENDING) {
                        throw new ConflictException("Cannot publish the event because it's not in the right state: " + event.getState());
                    }
                    if (LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
                        throw new ConflictException("Cannot publish the event because it's eventDate is too soon: " + event.getEventDate().format(DATE_TIME_FORMATTER));
                    }
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    if (event.getState() == State.PUBLISHED) {
                        throw new ConflictException("Cannot reject the event because it's not in the right state: " + event.getState());
                    }
                    event.setState(State.REJECTED);
            }
        }

        if (title != null) {
            event.setTitle(title);
        }
        return mapToFullDto(eventRepository.save(event));
    }

    /** Admin: Подборки событий */
    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        /* save new compilation */
        Compilation compilation = compilationRepository.save(map(newCompilationDto));
        CompilationDto res = map(compilation);

        List<Integer> eventIds = newCompilationDto.getEvents();
        List<Event> events = new ArrayList<>();
        if (eventIds != null && !eventIds.isEmpty()) {
            events = eventRepository.findAllByIdIn(eventIds);

            /* check event ids existence */
            events.forEach(event -> eventIds.remove(event.getId()));
            if (!eventIds.isEmpty()) {
                throw new NotFoundException("Event with id=" + eventIds.get(0) + " was not found");
            }

            /* save all new Compilation-Event pairings */
            compilationEventRepository.saveAll(events.stream()
                    .map(event -> CompilationEvent.builder()
                            .compilation(compilation)
                            .event(event)
                            .build())
                    .collect(Collectors.toList()));
        }

        /* set compilation dto events */
        res.setEvents(events.stream()
                .map(EventMapper::mapToShortDto)
                .collect(Collectors.toList()));
        return res;
    }

    @Override
    @Transactional
    public void deleteCompilation(int compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found");
        }

        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(int compId, UpdateCompilationRequestDto compilationUpdate) {
        /* check compilation existence */
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        /* update compilation */
        if (compilationUpdate.getPinned() != null) {
            compilation.setPinned(compilationUpdate.getPinned());
        }
        if (compilationUpdate.getTitle() != null) {
            compilation.setTitle(compilationUpdate.getTitle());
        }
        compilationRepository.save(compilation);
        CompilationDto res = map(compilation);

        List<Integer> eventIds = compilationUpdate.getEvents();
        List<Event> events = new ArrayList<>();
        if (eventIds == null) {
            events = eventRepository.findAllByIdIn(compilationEventRepository.getCompilationEventIds(compId));
        } else {
            /* delete previous Compilation-Event pairings */
            compilationEventRepository.deleteAllByCompilationId(compId);
            if (!eventIds.isEmpty()) {
                /* check event ids existence */
                List<Integer> existingIds = eventRepository.getAllIds();
                eventIds.forEach(id -> {
                    if (!existingIds.contains(id)) {
                        throw new NotFoundException("Event with id=" + id + " was not found");
                    }
                });
                events = eventRepository.findAllByIdIn(eventIds);

                /* save all new Compilation-Event pairings */
                compilationEventRepository.saveAll(events.stream()
                        .map(event -> CompilationEvent.builder()
                                .compilation(compilation)
                                .event(event)
                                .build())
                        .collect(Collectors.toList()));
            }
        }
        /* set compilation dto events */
        res.setEvents(events.stream()
                .map(EventMapper::mapToShortDto)
                .collect(Collectors.toList()));
        return res;
    }
}

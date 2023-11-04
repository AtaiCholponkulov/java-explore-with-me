package ru.practicum.ewm.service.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.service.dto.event.Location;
import ru.practicum.ewm.service.dto.event.State;
import ru.practicum.ewm.service.dto.event.UpdateEventAdminRequest;
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

    //------------------------------------------------Admin: Пользователи-----------------------------------------------
    @Override
    public List<User> getUsers(List<Integer> ids, int from, int size) {
        Pageable page = PageRequest.of(0, from + size);
        List<User> res = new ArrayList<>();
        if (ids == null) { //no ids were provided - get all users
            res = userRepository.findAll(page).getContent();
            res = from < res.size() ? res.subList(from, res.size()) : new ArrayList<>();
        } else if (!ids.isEmpty()) { //ids provided
            res = userRepository.findByIdIn(ids, page);
            res = from < res.size() ? res.subList(from, res.size()) : new ArrayList<>();
        }
        return res;
    }

    @Override
    @Transactional
    public User addUser(User newUser) {
        if (userRepository.existsByName(newUser.getName())) {
            throw new ConflictException("Field: name. Error: reserved. Value: " + newUser.getName());
        }
        return userRepository.save(newUser);
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    //-------------------------------------------------Admin: Категории-------------------------------------------------
    @Override
    @Transactional
    public Category addCategory(Category cat) {
        if (categoryRepository.existsByName(cat.getName())) {
            throw new ConflictException("Field: name. Error: already exists. Value: " + cat.getName());
        }
        return categoryRepository.save(cat);
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
    public Category updateCategory(Category cat) {
        if (!categoryRepository.existsById(cat.getId())) {
            throw new NotFoundException("Category with id=" + cat.getId() + " was not found");
        }
        categoryRepository.findByName(cat.getName()).ifPresent(dbCategory -> {
            if (!Objects.equals(dbCategory.getId(), cat.getId())) {
                throw new ConflictException("Field: name. Error: already exists. Value: " + cat.getName());
            }
        });
        return categoryRepository.save(cat);
    }

    //--------------------------------------------------Admin: События--------------------------------------------------
    @Override
    public List<Event> getEvents(List<Integer> users,
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

        Pageable page = PageRequest.of(0, from + size);
        List<Event> res = eventRepository.findAll(predicate, page).getContent();
        return from < res.size() ? res.subList(from, res.size()) : new ArrayList<>();
    }

    @Override
    @Transactional
    public Event updateEvent(int eventId, UpdateEventAdminRequest eventUpdate) {
        //check event existence
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        //change event fields if needed
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

        Location location = eventUpdate.getLocation();
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

        if (eventUpdate.getTitle() != null) {
            event.setTitle(eventUpdate.getTitle());
        }
        return eventRepository.save(event);
    }

    //----------------------------------------------Admin: Подборки событий---------------------------------------------
    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        //save new compilation
        Compilation compilation = compilationRepository.save(map(newCompilationDto));
        CompilationDto res = map(compilation);

        List<Integer> eventIds = newCompilationDto.getEvents();
        List<Event> events = new ArrayList<>();
        if (eventIds != null && !eventIds.isEmpty()) {
            //check event ids existence
            List<Integer> existingIds = eventRepository.getAllIds();
            eventIds.forEach(id -> {
                if (!existingIds.contains(id)) {
                    throw new NotFoundException("Event with id=" + id + " was not found");
                }
            });
            events = eventRepository.findAllByIdIn(eventIds);

            //save all new Compilation-Event pairings
            compilationEventRepository.saveAll(events.stream()
                    .map(event -> CompilationEvent.builder()
                            .compilation(compilation)
                            .event(event)
                            .build())
                    .collect(Collectors.toList()));
        }

        //set compilation dto events
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
    public CompilationDto updateCompilation(int compId, UpdateCompilationRequest compilationUpdate) {
        //check compilation existence
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        //update compilation
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
            //delete previous Compilation-Event pairings
            compilationEventRepository.deleteAllByCompilationId(compId);
            if (!eventIds.isEmpty()) {
                //check event ids existence
                List<Integer> existingIds = eventRepository.getAllIds();
                eventIds.forEach(id -> {
                    if (!existingIds.contains(id)) {
                        throw new NotFoundException("Event with id=" + id + " was not found");
                    }
                });
                events = eventRepository.findAllByIdIn(eventIds);

                //save all new Compilation-Event pairings
                compilationEventRepository.saveAll(events.stream()
                        .map(event -> CompilationEvent.builder()
                                .compilation(compilation)
                                .event(event)
                                .build())
                        .collect(Collectors.toList()));
            }
        }
        //set compilation dto events
        res.setEvents(events.stream()
                .map(EventMapper::mapToShortDto)
                .collect(Collectors.toList()));
        return res;
    }
}

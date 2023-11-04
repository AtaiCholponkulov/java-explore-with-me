package ru.practicum.ewm.service.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.controller.EventSort;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.EventShortDto;
import ru.practicum.ewm.service.dto.event.State;
import ru.practicum.ewm.service.exception.model.NotFoundException;
import ru.practicum.ewm.service.mapper.EventMapper;
import ru.practicum.ewm.service.model.*;
import ru.practicum.ewm.service.repository.CategoryRepository;
import ru.practicum.ewm.service.repository.CompilationEventRepository;
import ru.practicum.ewm.service.repository.CompilationRepository;
import ru.practicum.ewm.service.repository.EventRepository;
import ru.practicum.stats.client.model.StatsClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.mapper.CompilationMapper.map;
import static ru.practicum.ewm.service.mapper.EventMapper.mapToFullDto;
import static ru.practicum.ewm.service.mapper.EventMapper.mapToShortDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicServiceImpl implements PublicService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final StatsClient statsClient;
    private final CompilationRepository compilationRepository;
    private final CompilationEventRepository compilationEventRepository;

    //------------------------------------------------Public: Категории-------------------------------------------------
    @Override
    public List<Category> getCategories(int from, int size) {
        Page<Category> categoryPage =  categoryRepository.findAll(PageRequest.of(0, from + size));
        int pageSize = categoryPage.getNumberOfElements();
        return from < pageSize ? categoryPage.getContent().subList(from, pageSize) : new ArrayList<>();
    }

    @Override
    public Category getCategory(int catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
    }

    //-------------------------------------------------Public: События--------------------------------------------------
    @Override
    public List<EventShortDto> getEvents(String text,
                                         List<Integer> categories,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         boolean onlyAvailable,
                                         EventSort sort,
                                         int from,
                                         int size) {
        QEvent event = QEvent.event;
        BooleanExpression predicate = event.isNotNull().and(event.state.eq(State.PUBLISHED));

        if (text != null) {
            predicate = predicate.and(event.annotation.containsIgnoreCase(text)
                    .or(event.description.containsIgnoreCase(text)));
        }

        if (categories != null && !categories.isEmpty()) {
            predicate = predicate.and(event.category.id.in(categories));
        }

        if (paid != null) {
            predicate = predicate.and(event.paid.eq(paid));
        }

        if (rangeStart != null) {
            predicate = predicate.and(event.eventDate.goe(rangeStart));
        }

        if (rangeEnd != null) {
            predicate = predicate.and(event.eventDate.loe(rangeEnd));
        }

        if (onlyAvailable) {
            predicate = predicate.and(event.participantLimit.gt(0)
                    .and(event.participantLimit.gt(event.confirmedRequests))
                    .or(event.participantLimit.eq(0)));
        }

        Pageable page = PageRequest.of(0, from + size);
        Page<Event> eventPage = eventRepository.findAll(predicate, page);
        int pageSize = eventPage.getNumberOfElements();

        if (from >= pageSize) {
            return new ArrayList<>();
        }

        List<Integer> eventIds = new ArrayList<>();
        List<EventShortDto> response = new ArrayList<>();

        //map to short dtos
        eventPage.getContent().subList(from, pageSize).forEach(curEvent -> {
            eventIds.add(curEvent.getId());
            response.add(mapToShortDto(curEvent));
        });

        //set views
        Map<Integer, Integer> views = statsClient.getViews(eventIds);
        response.forEach(eventShortDto -> eventShortDto.setViews(views.getOrDefault(eventShortDto.getId(), 0)));

        //sort
        if (sort != null && response.size() > 1) {
            switch (sort) {
                case VIEWS:
                    response.sort(Comparator.comparingInt(EventShortDto::getViews));
                    break;
                case EVENT_DATE:
                    response.sort((event1, event2) -> {
                        if (event1.getEventDate().isBefore(event2.getEventDate())) {
                            return -1;
                        } else if (event1.getEventDate().isEqual(event2.getEventDate())) {
                            return 0;
                        } else {
                            return 1;
                        }
                    });
            }
        }
        return response;
    }

    @Override
    public EventFullDto getEvent(int id) {
        //check event existence and state
        Event event = eventRepository.findByIdAndState(id, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found"));

        //set views
        Map<Integer, Integer> views = statsClient.getViews(List.of(id));
        EventFullDto response = mapToFullDto(event);
        response.setViews(views.getOrDefault(id, 0));
        return response;
    }

    //----------------------------------------------Public: Подборки событий--------------------------------------------
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(0, from + size);
        List<Compilation> compilations = pinned != null
                ? compilationRepository.findAllByPinned(pinned, page)
                : compilationRepository.findAll(page).getContent();

        return from >= compilations.size()
                ? new ArrayList<>()
                : compilations.subList(from, compilations.size())
                        .stream()
                        .map(compilation -> {
                            CompilationDto compilationDto = map(compilation);
                            compilationDto.setEvents(
                                    compilationEventRepository.getCompilationEvents(compilation.getId())
                                            .stream()
                                            .map(EventMapper::mapToShortDto)
                                            .collect(Collectors.toList()));
                            return compilationDto;
                        })
                        .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(int compId) {
        //check compilation existence
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        CompilationDto res = map(compilation);

        //set compilation events
        res.setEvents(compilationEventRepository.getCompilationEvents(compId)
                .stream()
                .map(EventMapper::mapToShortDto)
                .collect(Collectors.toList()));
        return res;
    }
}
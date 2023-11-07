package ru.practicum.ewm.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.service.dto.category.CategoryDto;
import ru.practicum.ewm.service.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.dto.event.EventFullDto;
import ru.practicum.ewm.service.dto.event.EventShortDto;
import ru.practicum.ewm.service.service.PublicService;
import ru.practicum.stats.client.model.StatsClient;
import ru.practicum.stats.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.service.valid.Validator.*;

@RestController
@RequiredArgsConstructor
public class PublicController {

    private final PublicService service;
    private final StatsClient statsClient;
    private static final String APP = "ewm-main-service";

    /** Public: Категории */
    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        validatePaginationParams(from, size);
        return service.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable int catId) {
        return service.getCategory(catId);
    }

    /** Public: События */
    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                         @RequestParam(required = false) EventSort sort,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        validateRangeStartAndEndParams(rangeStart, rangeEnd);
        validatePaginationParams(from, size);
        List<EventShortDto> response = service.getEvents(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size);

        /* add hit */
        statsClient.addHit(
                new EndpointHitDto(APP,
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        LocalDateTime.now()));
        return response;
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable int id, HttpServletRequest request) {
        EventFullDto response = service.getEvent(id);

        /* add hit */
        statsClient.addHit(
                new EndpointHitDto(APP,
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        LocalDateTime.now()));
        return response;
    }

    /** Public: Подборки событий */
    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        validatePaginationParams(from, size);
        return service.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable int compId) {
        return service.getCompilation(compId);
    }
}

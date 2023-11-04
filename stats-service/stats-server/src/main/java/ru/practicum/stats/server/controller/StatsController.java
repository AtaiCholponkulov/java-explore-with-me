package ru.practicum.stats.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.exception.model.BadRequestException;
import ru.practicum.stats.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService service;

    @GetMapping("/stats")
    public List<ViewStatsDto> get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(defaultValue = "false") boolean unique) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new BadRequestException("Request parameters: start, end. " +
                    "Error: the start is after the end. Values: start=" + start + ", end=" + end);
        }
        return service.get(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody EndpointHitDto endpointHitDto) {
        service.add(endpointHitDto);
    }

    @GetMapping("/stats/views")
    public Map<Integer, Integer> getViews(@RequestParam List<String> uris) {
        return service.getViews(uris);
    }
}

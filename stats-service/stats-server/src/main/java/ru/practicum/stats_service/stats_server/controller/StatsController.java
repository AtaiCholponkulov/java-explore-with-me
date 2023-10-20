package ru.practicum.stats_service.stats_server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats_service.stats_dto.EndpointHitDto;
import ru.practicum.stats_service.stats_dto.ViewStatsDto;
import ru.practicum.stats_service.stats_server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/stats")
    public List<ViewStatsDto> get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(defaultValue = "false") boolean unique) {
        return statsService.get(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public void add(@RequestBody EndpointHitDto endpointHitDto) {
        statsService.add(endpointHitDto);
    }
}

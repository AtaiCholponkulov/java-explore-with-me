package ru.practicum.stats.server.service;

import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public interface StatsService {

    void add(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    Map<Integer, Integer> getViews(List<String> uris);
}

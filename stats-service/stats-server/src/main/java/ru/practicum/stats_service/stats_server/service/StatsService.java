package ru.practicum.stats_service.stats_server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats_service.stats_dto.EndpointHitDto;
import ru.practicum.stats_service.stats_dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatsService {

    @Transactional
    void add(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}

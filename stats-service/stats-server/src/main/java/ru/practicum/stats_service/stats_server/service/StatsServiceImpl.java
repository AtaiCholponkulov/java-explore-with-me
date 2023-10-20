package ru.practicum.stats_service.stats_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats_service.stats_dto.EndpointHitDto;
import ru.practicum.stats_service.stats_dto.ViewStatsDto;
import ru.practicum.stats_service.stats_server.model.EndpointHit;
import ru.practicum.stats_service.stats_server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public void add(EndpointHitDto endpointHitDto) {
        statsRepository.save(map(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        // get db records
        List<EndpointHit> data = uris == null || uris.isEmpty()
                ? statsRepository.get(start, end)
                : statsRepository.get(start, end, uris);

        // key: obj(app, uri), value: list(ips)
        Map<ViewStatsDto, List<String>> ipMap = new HashMap<>();
        data.forEach(eH -> {
            ViewStatsDto vSD = new ViewStatsDto(eH.getApp(), eH.getUri());
            if (!ipMap.containsKey(vSD)) {
                ipMap.put(vSD, new ArrayList<>());
            }
            ipMap.get(vSD).add(eH.getIp());
        });

        // return ipMap's keys with 'hits' field updated from ipMap's values
        if (unique) {// count unique ips
            return ipMap.entrySet()
                    .stream()
                    .map(entry -> {
                        ViewStatsDto key = entry.getKey();
                        key.setHits(new HashSet<>(entry.getValue()).size());
                        return key;
                    })
                    .collect(Collectors.toList());
        } else {// count all ips
            return ipMap.entrySet()
                    .stream()
                    .map(entry -> {
                        ViewStatsDto key = entry.getKey();
                        key.setHits(entry.getValue().size());
                        return key;
                    })
                    .collect(Collectors.toList());
        }
    }

    private EndpointHit map(EndpointHitDto endpointHitDto) {
        return new EndpointHit(null,
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp());
    }
}

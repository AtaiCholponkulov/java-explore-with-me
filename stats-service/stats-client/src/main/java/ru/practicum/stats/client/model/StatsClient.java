package ru.practicum.stats.client.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.EndpointHitDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory("http://stats-service:9091"))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void addHit(EndpointHitDto itemDto) {
        post("/hit", itemDto);
    }

    public Map<Integer, Integer> getViews(List<Integer> eventIds) {
        String uris = "/stats/views?uris=/events/" + eventIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("&uris=/events/"));
        ResponseEntity<String> response = rest.getForEntity(uris, String.class);
        String body = response.getBody();
        if (body != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(body, TypeFactory.defaultInstance().constructMapType(Map.class, Integer.class, Integer.class));
            } catch (JsonProcessingException e) {
                return new HashMap<>();
            }
        }
        return new HashMap<>();
    }
}

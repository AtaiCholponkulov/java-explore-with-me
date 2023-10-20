package ru.practicum.stats_service.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stats_service.stats_server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT eh " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.timestamp > ?1 " +
            "AND eh.timestamp < ?2")
    List<EndpointHit> get(LocalDateTime start, LocalDateTime end);

    @Query("SELECT eh " +
            "FROM EndpointHit AS eh " +
            "WHERE eh.timestamp > ?1 " +
            "AND eh.timestamp < ?2 " +
            "AND eh.uri IN ?3")
    List<EndpointHit> get(LocalDateTime start, LocalDateTime end, List<String> uris);
}

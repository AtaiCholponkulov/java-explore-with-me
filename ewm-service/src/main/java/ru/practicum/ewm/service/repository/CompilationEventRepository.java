package ru.practicum.ewm.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.service.model.CompilationEvent;
import ru.practicum.ewm.service.model.Event;

import java.util.List;

@Repository
public interface CompilationEventRepository extends JpaRepository<CompilationEvent, Integer> {

    @Query("SELECT e.id " +
            "FROM CompilationEvent AS ce " +
            "JOIN ce.compilation AS c " +
            "JOIN ce.event AS e " +
            "WHERE c.id = ?1")
    List<Integer> getCompilationEventIds(int compId);

    void deleteAllByCompilationId(int compId);

    @Query("SELECT e " +
            "FROM CompilationEvent AS ce " +
            "JOIN ce.event AS e " +
            "WHERE ce.compilation.id = ?1")
    List<Event> getCompilationEvents(int compId);

}

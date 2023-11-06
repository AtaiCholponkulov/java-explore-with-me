package ru.practicum.ewm.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.service.model.State;
import ru.practicum.ewm.service.model.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {

    List<Event> findAllByInitiatorId(int initiatorId, Pageable page);

    Optional<Event> findByIdAndState(int eventId, State state);

    @Query("SELECT e.id " +
            "FROM Event AS e")
    List<Integer> getAllIds();

    List<Event> findAllByIdIn(List<Integer> ids);

    boolean existsByCategoryId(int catId);
}

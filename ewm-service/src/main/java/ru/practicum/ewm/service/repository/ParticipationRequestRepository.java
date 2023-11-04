package ru.practicum.ewm.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.service.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    Optional<ParticipationRequest> findByRequesterIdAndEventId(int userId, int eventId);

    List<ParticipationRequest> findAllByRequesterId(int userId);

    List<ParticipationRequest> findAllByEventId(int eventId);

    List<ParticipationRequest> findAllByEventIdOrderById(int eventId);
}

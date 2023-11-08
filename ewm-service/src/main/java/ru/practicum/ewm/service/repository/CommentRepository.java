package ru.practicum.ewm.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.service.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByAuthorIdOrderByCreatedOnDesc(int userId);

    List<Comment> findAllByEventIdOrderByCreatedOnDesc(int eventId);
}

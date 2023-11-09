package ru.practicum.ewm.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.service.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByAuthorIdAndParentCommentIdIsNullOrderByCreatedOnDesc(int userId, Pageable page);

    List<Comment> findAllByEventIdAndParentCommentIdIsNullOrderByCreatedOnDesc(int eventId, Pageable page);

    List<Comment> findAllByEventIdAndParentCommentIdIsNullOrderByCreatedOnAsc(int eventId, Pageable page);

    List<Comment> findAllByParentCommentIdIn(List<Integer> commentIds);

    List<Comment> findAllByParentCommentIdOrderByCreatedOnAsc(int commentId);
}

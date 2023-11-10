package ru.practicum.ewm.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.service.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c " +
            "FROM Comment AS c " +
            "JOIN FETCH c.author AS a " +
            "JOIN FETCH c.event AS e " +
            "WHERE a.id = ?1 " +
            "AND c.parentComment IS NULL " +
            "ORDER BY c.createdOn DESC")
    List<Comment> findAllParentComments(int userId, Pageable page);

    @Query("SELECT c " +
            "FROM Comment AS c " +
            "JOIN FETCH c.author AS a " +
            "JOIN FETCH c.event AS e " +
            "WHERE e.id = ?1 " +
            "AND c.parentComment IS NULL " +
            "ORDER BY c.createdOn DESC")
    List<Comment> findAllEventParentCommentsDesc(int eventId, Pageable page);

    @Query("SELECT c " +
            "FROM Comment AS c " +
            "JOIN FETCH c.author AS a " +
            "JOIN FETCH c.event AS e " +
            "WHERE e.id = ?1 " +
            "AND c.parentComment IS NULL " +
            "ORDER BY c.createdOn ASC")
    List<Comment> findAllEventParentCommentsAsc(int eventId, Pageable page);

    @Query("SELECT c " +
            "FROM Comment AS c " +
            "JOIN FETCH c.author AS a " +
            "JOIN FETCH c.event AS e " +
            "JOIN FETCH c.parentComment AS pc " +
            "WHERE pc.id IN ?1")
    List<Comment> findAllSubComments(List<Integer> commentIds);

    @Query("SELECT c " +
            "FROM Comment AS c " +
            "JOIN FETCH c.author AS a " +
            "JOIN FETCH c.event AS e " +
            "JOIN FETCH c.parentComment AS pc " +
            "WHERE pc.id = ?1 " +
            "ORDER BY c.createdOn ASC")
    List<Comment> findAllSubComments(int commentId);
}

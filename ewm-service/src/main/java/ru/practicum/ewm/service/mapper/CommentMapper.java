package ru.practicum.ewm.service.mapper;

import ru.practicum.ewm.service.dto.comment.CommentDto;
import ru.practicum.ewm.service.dto.comment.ParentComment;
import ru.practicum.ewm.service.dto.comment.SubCommentDto;
import ru.practicum.ewm.service.model.Comment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommentMapper {

    public static CommentDto mapToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getEvent().getId(),
                comment.getCreatedOn());
    }

    public static SubCommentDto mapToSubDto(Comment comment) {
        return new SubCommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getEvent().getId(),
                comment.getCreatedOn(),
                comment.getParentComment().getId());
    }

    public static List<SubCommentDto> map(List<Comment> subComments) {
        return subComments.stream()
                .map(CommentMapper::mapToSubDto)
                .collect(Collectors.toList());
    }

    public static ParentComment map(Comment comment, List<SubCommentDto> subs) {
        return new ParentComment(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getEvent().getId(),
                comment.getCreatedOn(),
                subs);
    }

    public static List<ParentComment> map(List<Comment> parentComments,
                                       Map<Integer, List<SubCommentDto>> subComments) {
        return parentComments.stream()
                .map(comment -> map(
                        comment,
                        sortedByEventDateAsc(subComments.getOrDefault(comment.getId(), null))))
                .collect(Collectors.toList());
    }

    private static List<SubCommentDto> sortedByEventDateAsc(List<SubCommentDto> comments) {
        if (comments == null) {
            return null;
        }
        comments.sort((comment1, comment2) -> {
            if (comment1.getCreatedOn().isBefore(comment2.getCreatedOn())) {
                return -1;
            } else if (comment1.getCreatedOn().isAfter(comment2.getCreatedOn())) {
                return 1;
            } else {
                return 0;
            }
        });
        return  comments;
    }
}

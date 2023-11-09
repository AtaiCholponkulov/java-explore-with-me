package ru.practicum.ewm.service.mapper;

import ru.practicum.ewm.service.dto.comment.CommentDto;
import ru.practicum.ewm.service.dto.comment.CommentWithSubsDto;
import ru.practicum.ewm.service.model.Comment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommentMapper {

    public static CommentDto map(Comment subComment) {
        return new CommentDto(
                subComment.getId(),
                subComment.getText(),
                subComment.getAuthor().getName(),
                subComment.getCreatedOn(),
                subComment.getParentComment() == null
                        ? null
                        : subComment.getParentComment().getId());
    }

    public static List<CommentDto> map(List<Comment> subComments) {
        return subComments.stream()
                .map(CommentMapper::map)
                .collect(Collectors.toList());
    }

    public static CommentWithSubsDto map(Comment comment, List<CommentDto> subs) {
        return new CommentWithSubsDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreatedOn(),
                subs);
    }

    public static List<CommentWithSubsDto> map(List<Comment> parentComments,
                                               Map<Integer, List<CommentDto>> subComments) {
        return parentComments.stream()
                .map(comment -> map(
                        comment,
                        sortedByEventDateAsc(subComments.getOrDefault(comment.getId(), null))))
                .collect(Collectors.toList());
    }

    private static List<CommentDto> sortedByEventDateAsc(List<CommentDto> comments) {
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

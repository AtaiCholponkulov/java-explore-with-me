package ru.practicum.ewm.service.mapper;

import ru.practicum.ewm.service.dto.comment.CommentDto;
import ru.practicum.ewm.service.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public static CommentDto map(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .createdOn(comment.getCreatedOn())
                .build();
    }

    public static List<CommentDto> map(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::map)
                .collect(Collectors.toList());
    }
}

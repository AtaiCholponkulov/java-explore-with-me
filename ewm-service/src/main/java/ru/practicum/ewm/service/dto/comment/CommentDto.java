package ru.practicum.ewm.service.dto.comment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private Integer id;
    private String text;
    private String authorName;
    private LocalDateTime createdOn;
}

package ru.practicum.ewm.service.dto.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SubCommentDto extends CommentDto {

    private Integer parentCommentId;

    public SubCommentDto(Integer id,
                         String text,
                         String authorName,
                         Integer eventId,
                         LocalDateTime createdOn,
                         Integer parentCommentId) {
        super(id, text, authorName, eventId, createdOn);
        this.parentCommentId = parentCommentId;
    }
}

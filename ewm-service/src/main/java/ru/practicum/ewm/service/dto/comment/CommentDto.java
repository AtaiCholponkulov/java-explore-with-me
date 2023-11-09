package ru.practicum.ewm.service.dto.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    protected Integer id;
    protected String text;
    protected String authorName;
    protected LocalDateTime createdOn;
    private Integer parentCommentId;

    public CommentDto(Integer id,
                      String text,
                      String authorName,
                      LocalDateTime createdOn) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.createdOn = createdOn;
    }

    public CommentDto(Integer id,
                      String text,
                      String authorName,
                      LocalDateTime createdOn,
                      Integer parentCommentId) {
        this(id, text, authorName, createdOn);
        this.parentCommentId = parentCommentId;
    }
}

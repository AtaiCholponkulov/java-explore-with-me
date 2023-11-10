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
    protected Integer eventId;
    protected LocalDateTime createdOn;

    public CommentDto(Integer id,
                      String text,
                      String authorName,
                      Integer eventId,
                      LocalDateTime createdOn) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.createdOn = createdOn;
        this.eventId = eventId;
    }
}

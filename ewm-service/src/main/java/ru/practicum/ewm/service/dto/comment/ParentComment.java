package ru.practicum.ewm.service.dto.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ParentComment extends CommentDto {

    private List<SubCommentDto> subs;

    public ParentComment(Integer id,
                         String text,
                         String authorName,
                         Integer eventId,
                         LocalDateTime createdOn,
                         List<SubCommentDto> subs) {
        super(id, text, authorName, eventId, createdOn);
        this.subs = subs;
    }
}
